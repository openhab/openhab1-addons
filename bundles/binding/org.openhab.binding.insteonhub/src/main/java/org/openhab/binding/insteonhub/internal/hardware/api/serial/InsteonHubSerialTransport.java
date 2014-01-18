/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.hardware.api.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Hex;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubLevelUpdateType;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubMsgConst;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxyListener;
import org.openhab.binding.insteonhub.internal.util.InsteonHubBindingLogUtil;
import org.openhab.binding.insteonhub.internal.util.InsteonHubByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class does all of the heaving lifting for serial I/O communication with
 * the Insteon Hub.
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubSerialTransport {

	private static final Logger logger = LoggerFactory
			.getLogger(InsteonHubSerialTransport.class);

	private final BlockingQueue<byte[]> commandQueue = new LinkedBlockingQueue<byte[]>();
	private final Set<InsteonHubProxyListener> listeners = new HashSet<InsteonHubProxyListener>();
	private final InsteonHubSerialProxy proxy;
	private volatile Listener listener;
	private volatile Sender sender;
	private InputStream inputStream;
	private OutputStream outputStream;

	public InsteonHubSerialTransport(InsteonHubSerialProxy proxy) {
		this.proxy = proxy;
	}

	public synchronized boolean isStarted() {
		return inputStream != null;
	}

	public synchronized void start(InputStream in, OutputStream out) {
		this.inputStream = in;
		this.outputStream = out;
		listener = new Listener();
		sender = new Sender();
		new Thread(listener, proxy.getConnectionString() + " listener").start();
		new Thread(sender, proxy.getConnectionString() + " sender").start();
	}

	public synchronized void stop() {
		inputStream = null;
		outputStream = null;
		listener = null;
		sender = null;
	}

	public void enqueueCommand(byte[] msg) {
		commandQueue.add(msg);
	}

	public void addListener(InsteonHubProxyListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(InsteonHubProxyListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	// Takes commands off the command queue and sends them to the Hub.
	private class Sender implements Runnable {
		@Override
		public void run() {
			try {
				// check run condition
				while (sender == this) {
					// take message off queue
					byte[] msg = null;
					try {
						msg = commandQueue.poll(5, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// ignore: msg will be null and not processed
					}

					// process message
					if (msg != null) {
						outputStream.write(msg);
						outputStream.flush();
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
				InsteonHubBindingLogUtil.logCommunicationFailure(logger, proxy,
						t);
				proxy.reconnect();
			}
		}
	};

	// Listens for messages from the Hub and passes them to the handleMessage
	// method
	private class Listener implements Runnable {
		@Override
		public void run() {
			try {
				while (listener == this) {
					// read next messages
					byte[] msg = readMsg(inputStream);
					// if msg was read, pass to handleMessage
					if (msg != null) {
						handleMessage(msg);
					}
				}
			} catch (Throwable t) {
				InsteonHubBindingLogUtil.logCommunicationFailure(logger, proxy,
						t);
				proxy.reconnect();
			}
		}

		private byte[] readMsg(InputStream in) throws IOException {
			// read to 0x02 "start of message"
			byte b;
			while ((b = InsteonHubByteUtil.readByte(in)) != InsteonHubMsgConst.STX) {
				if (logger.isDebugEnabled()) {
					logger.debug("Ignoring non STX byte: " + b);
				}
			}
			// read command type byte
			byte cmd = InsteonHubByteUtil.readByte(in);

			// based on command type, figure out number of messages to read
			Integer msgSize = InsteonHubMsgConst.REC_MSG_SIZES.get(cmd);
			if (msgSize == null) {
				// we may go out of sync... log this. We need to add/fix
				// REC_MSG_SIZES
				// FIXME change to warn. There is currently a known bug with extended message types showing this, so it's debug for now.
				logger.debug("Received unknown command type '" + cmd
						+ "' - If you see this frequently, "
						+ "please save debug logs and report this as a bug.");
				return null;
			}
			byte[] msg = new byte[msgSize];
			msg[0] = InsteonHubMsgConst.STX;
			msg[1] = cmd;
			InsteonHubByteUtil.fillBuffer(in, msg, 2);
			
			if(cmd == InsteonHubMsgConst.SND_CODE_SEND_INSTEON_STD_OR_EXT_MSG) {
				if(new InsteonHubStdMsgFlags(msg[5]).isExtended()) {
					// read 14 more bytes and add them to the end of the msg
					byte[] extendedBytes = new byte[14];
					InsteonHubByteUtil.fillBuffer(in, extendedBytes, 0);
					byte[] extMsg = new byte[msg.length+extendedBytes.length];
					System.arraycopy(msg, 0, extMsg, 0, msg.length);
					System.arraycopy(extendedBytes, 0, extMsg, msg.length, extendedBytes.length);
					msg = extMsg;
				}
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Received Message from INSTEON Hub: "
						+ Hex.encodeHexString(msg));
			}

			return msg;
		}
	}

	private void handleMessage(byte[] msg) {
		byte cmd = msg[1];

		if (cmd == InsteonHubMsgConst.REC_CODE_INSTEON_STD_MSG) {
			// INSTEON Standard Message
			// parse device and flag
			String device = InsteonHubByteUtil.encodeDeviceHex(msg, 2);
			InsteonHubStdMsgFlags flags = new InsteonHubStdMsgFlags(
					InsteonHubByteUtil.byteToUnsignedInt(msg[8]));
			if (flags.isAck()
					&& msg[9] == InsteonHubMsgConst.CMD1_STATUS_REQUEST) {
				// ack flag => response to value check
				int level = InsteonHubByteUtil.byteToUnsignedInt(msg[10]);
				if (logger.isDebugEnabled()) {
					logger.debug("Alerting level update device='" + device
							+ "' level=" + level);
				}
				alertLevelUpdate(device, level, InsteonHubLevelUpdateType.STATUS_RESPONSE);
			} else {
				// not an ack => check if this could have changed a value
				byte cmd1 = msg[9];
				switch (cmd1) {
				case InsteonHubMsgConst.CMD1_OFF:
				case InsteonHubMsgConst.CMD1_ON:
				case InsteonHubMsgConst.CMD1_OFF_FAST:
				case InsteonHubMsgConst.CMD1_ON_FAST:
					// On or Off => 255 or 0 level
					alertLevelUpdate(device, cmd1 == InsteonHubMsgConst.CMD1_ON
							|| cmd1 == InsteonHubMsgConst.CMD1_ON_FAST ? 255
							: 0, InsteonHubLevelUpdateType.STATUS_CHANGE);
				case InsteonHubMsgConst.CMD1_DIM:
				case InsteonHubMsgConst.CMD1_BRT:
				case InsteonHubMsgConst.CMD1_STOP_DIM_BRT:
					// something analog changed => request level
					proxy.requestDeviceLevel(device);
					if (logger.isTraceEnabled()) {
						logger.trace("Requesting level for device " + device);
					}
					break;
				}
			}
		} else if (cmd == InsteonHubMsgConst.SND_CODE_SEND_INSTEON_STD_OR_EXT_MSG) {
			// INSTEON ACK/NAK Message
			byte ack = msg[8];
			if (ack == InsteonHubMsgConst.ACK) {
				if (logger.isTraceEnabled()) {
					logger.trace("Received message with ACK: "
							+ Hex.encodeHexString(msg));
				}
			} else if (ack == InsteonHubMsgConst.NAK) {
				if (logger.isDebugEnabled()) {
					logger.debug("Received message with NAK: "
							+ Hex.encodeHexString(msg) + " - Will resend!");
				}
				// parse original message from the NAK message (NAK is an
				// added
				// last byte)
				byte[] originMsg = Arrays.copyOfRange(msg, 0, msg.length - 1);
				// re-send the message
				// (NAK means message could not be handled at that time)
				enqueueCommand(originMsg);
			}
		}
	}

	private void alertLevelUpdate(String device, int level, InsteonHubLevelUpdateType updateType) {
		synchronized (listeners) {
			for (InsteonHubProxyListener listener : listeners) {
				listener.onLevelUpdate(device.toUpperCase(), level, updateType);
			}
		}
	}
}
