/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubMsgConst;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxyListener;
import org.openhab.binding.insteonhub.internal.hardware.api.InsteonHubCommand;
import org.openhab.binding.insteonhub.internal.util.InsteonHubBindingLogUtil;
import org.openhab.binding.insteonhub.internal.util.InsteonHubByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class does all of the heaving lifting for serial I/O communication with
 * the Insteon Hub.
 * 
 * @author Eric Thill
 * 
 */
public class InsteonHubSerialTransport {

	private static final Logger logger = LoggerFactory
			.getLogger(InsteonHubSerialTransport.class);

	private static final int MAX_SEND_ATTEMPTS = 3;
	private static final int MAX_ACK_QUEUE_SIZE = 128;

	private final BlockingQueue<InsteonHubCommand> commandQueue = new LinkedBlockingQueue<InsteonHubCommand>();
	private final BlockingQueue<Ack> ackQueue = new LinkedBlockingQueue<Ack>();
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

	public void enqueueCommand(InsteonHubCommand command) {
		commandQueue.add(command);
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

	private class Listener implements Runnable {
		@Override
		public void run() {
			try {
				while (listener == this) {
					byte[] msg = readMsg(inputStream);
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

	}

	private byte[] readMsg(InputStream in) throws IOException {
		// read to 0x02 "start of message"
		while (InsteonHubByteUtil.readByte(in) != InsteonHubMsgConst.STX)
			;
		// read command type byte
		byte cmd = InsteonHubByteUtil.readByte(in);

		Integer msgSize = InsteonHubMsgConst.REC_MSG_SIZES.get(cmd);
		if (msgSize == null) {
			logger.warn("Received unknown command type '" + cmd + "'");
			return null;
		}
		byte[] msg = new byte[msgSize];
		msg[0] = InsteonHubMsgConst.STX;
		msg[1] = cmd;
		InsteonHubByteUtil.fillBuffer(in, msg, 2);

		if (logger.isDebugEnabled()) {
			logger.debug("Received Message from INSTEON Hub: "
					+ Hex.encodeHexString(msg));
		}

		return msg;
	}

	private void handleMessage(byte[] msg) {
		byte cmd = msg[1];

		if (cmd == InsteonHubMsgConst.REC_CODE_INSTEON_STD_MSG) {
			// INSTEON Standard Message
			// parse device and flag
			String device = InsteonHubByteUtil.encodeDeviceHex(msg, 2);
			InsteonHubStdMsgFlags flags = new InsteonHubStdMsgFlags(
					InsteonHubByteUtil.byteToUnsignedInt(msg[8]));
			if (flags.isAck()) {
				// ack flag => response to value check
				alertLevelUpdate(device,
						InsteonHubByteUtil.byteToUnsignedInt(msg[10]));
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
							: 0);
					break;
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
					logger.trace("Received ACK");
				}
				ackQueue.add(new Ack(msg, true));
			} else if (ack == InsteonHubMsgConst.NAK) {
				if (logger.isTraceEnabled()) {
					logger.trace("Received NAK");
				}
				ackQueue.add(new Ack(msg, false));
			}
			// Make sure we don't fill up the queue with ACKs
			while (ackQueue.size() > MAX_ACK_QUEUE_SIZE) {
				// we poll the head of the queue because it's been sitting there
				// for a while. It's probably not for us. Remove it and add
				// this.
				ackQueue.poll();
			}
		}
	}

	private void alertLevelUpdate(String device, int level) {
		synchronized (listeners) {
			for (InsteonHubProxyListener listener : listeners) {
				listener.onLevelUpdate(device.toUpperCase(), level);
			}
		}
	}

	private class Sender implements Runnable {
		@Override
		public void run() {
			try {
				while (sender == this) {
					InsteonHubCommand command = null;
					try {
						command = commandQueue.poll(5, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// ignore
					}
					if (command != null) {
						boolean success = false;
						int attempt = 0;
						while (!success && attempt < MAX_SEND_ATTEMPTS) {
							// send the command over the wire
							byte[] sent = sendCommand(outputStream, command);
							// if a command was sent, get its ack
							if (sent != null) {
								// poll the act for this request
								Ack ack = pollAck(sent);
								// if an ack was found, parse it
								if (ack != null) {
									success = ack.success;
								} else {
									if(logger.isDebugEnabled()) {
										logger.debug("Timed-out waiting on Ack for "
												+ Hex.encodeHexString(sent));
									}
								}
							}
							attempt++;
						}
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

	private Ack pollAck(byte[] sent) throws InterruptedException {
		Ack ack = null;
		byte[] ackMsg = null;
		while (ack == null || !Arrays.equals(sent, ackMsg)) {
			ack = ackQueue.poll(2, TimeUnit.SECONDS);
			if (ack == null) {
				// timeout => return null
				return null;
			}
			ackMsg = Arrays.copyOfRange(ack.msg, 0, ack.msg.length - 1);
		}
		return ack;
	}

	private byte[] sendCommand(OutputStream outputStream,
			InsteonHubCommand command) throws IOException {
		if (command.getType() == null) {
			return null;
		}
		String device = command.getDevice();
		switch (command.getType()) {
		case GET_LEVEL:
			return send(outputStream, device, 0x05,
					InsteonHubMsgConst.CMD1_STATUS_REQUEST, (byte) 0x02);
		case OFF_FAST:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_OFF_FAST,
					InsteonHubMsgConst.CMD2_NO_VALUE);
		case ON_FAST:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_ON_FAST,
					InsteonHubMsgConst.CMD2_NO_VALUE);
		case OFF:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_OFF,
					InsteonHubMsgConst.CMD2_NO_VALUE);
		case ON:
			return send(outputStream, device, 0x0F, InsteonHubMsgConst.CMD1_ON,
					(byte) command.getLevel());
		case DIM:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_DIM,
					InsteonHubMsgConst.CMD2_NO_VALUE);
		case BRT:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_BRT,
					InsteonHubMsgConst.CMD2_NO_VALUE);
		case START_DIM:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_START_DIM_BRT,
					InsteonHubMsgConst.CMD2_DIM);
		case START_BRT:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_START_DIM_BRT,
					InsteonHubMsgConst.CMD2_BRT);
		case STOP_DIM_BRT:
			return send(outputStream, device, 0x0F,
					InsteonHubMsgConst.CMD1_STOP_DIM_BRT,
					InsteonHubMsgConst.CMD2_NO_VALUE);
		default:
			return null;
		}
	}

	private byte[] send(OutputStream outputStream, String device, int flag,
			byte cmd1, byte cmd2) throws IOException {
		try {
			byte[] buf = new byte[8];
			buf[0] = InsteonHubMsgConst.STX;
			buf[1] = InsteonHubMsgConst.SND_CODE_SEND_INSTEON_STD_OR_EXT_MSG;
			System.arraycopy(Hex.decodeHex(device.toCharArray()), 0, buf, 2, 3);
			buf[5] = (byte) flag;
			buf[6] = cmd1;
			buf[7] = cmd2;
			if (logger.isDebugEnabled()) {
				logger.debug("Sending message to INSTEON Hub: "
						+ Hex.encodeHexString(buf));
			}
			outputStream.write(buf);
			return buf;
		} catch (DecoderException e) {
			throw new IOException(
					"Could not create message.  Could not encode device hex '"
							+ device + "'", e);
		}
	}

	private static class Ack {
		public final byte[] msg;
		public final boolean success;

		public Ack(byte[] msg, boolean success) {
			this.msg = msg;
			this.success = success;
		}
	}
}
