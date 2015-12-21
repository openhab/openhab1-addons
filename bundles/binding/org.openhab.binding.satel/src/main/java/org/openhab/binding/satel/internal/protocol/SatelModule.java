/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.openhab.binding.satel.internal.event.ConnectionStatusEvent;
import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.SatelEventListener;
import org.openhab.binding.satel.internal.event.IntegraVersionEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.command.ClearTroublesCommand;
import org.openhab.binding.satel.internal.protocol.command.ControlObjectCommand;
import org.openhab.binding.satel.internal.protocol.command.IntegraStateCommand;
import org.openhab.binding.satel.internal.protocol.command.IntegraStatusCommand;
import org.openhab.binding.satel.internal.protocol.command.IntegraVersionCommand;
import org.openhab.binding.satel.internal.protocol.command.NewStatesCommand;
import org.openhab.binding.satel.internal.protocol.command.SatelCommand;
import org.openhab.binding.satel.internal.protocol.command.SetClockCommand;
import org.openhab.binding.satel.internal.types.ControlType;
import org.openhab.binding.satel.internal.types.DoorsState;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.binding.satel.internal.types.OutputControl;
import org.openhab.binding.satel.internal.types.OutputState;
import org.openhab.binding.satel.internal.types.PartitionControl;
import org.openhab.binding.satel.internal.types.PartitionState;
import org.openhab.binding.satel.internal.types.StateType;
import org.openhab.binding.satel.internal.types.ZoneState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents abstract communication module and is responsible for
 * exchanging data between the binding and connected physical module.
 * Communication happens by sending commands and receiving response from the
 * module. Each command class must extend {@link SatelCommand} and be added to
 * <code>SatelModule.supportedCommands</code> map in
 * <code>SatelModule.registerCommands</code> method.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public abstract class SatelModule extends EventDispatcher implements SatelEventListener {
	private static final Logger logger = LoggerFactory.getLogger(SatelModule.class);

	private static final byte FRAME_SYNC = (byte) 0xfe;
	private static final byte FRAME_SYNC_ESC = (byte) 0xf0;
	private static final byte[] FRAME_START = { FRAME_SYNC, FRAME_SYNC };
	private static final byte[] FRAME_END = { FRAME_SYNC, (byte) 0x0d };

	private final Map<Byte, SatelCommand> supportedCommands = new ConcurrentHashMap<Byte, SatelCommand>();
	private final BlockingQueue<SatelMessage> sendQueue = new LinkedBlockingQueue<SatelMessage>();

	private IntegraType integraType;
	private int timeout;
	private String integraVersion;
	private CommunicationChannel channel;
	private CommunicationWatchdog communicationWatchdog;

	/*
	 * Helper interface for connecting and disconnecting to specific module
	 * type. Each module type should implement these methods to provide input
	 * and output streams and way to disconnect from the module.
	 */
	protected interface CommunicationChannel {

		InputStream getInputStream() throws IOException;

		OutputStream getOutputStream() throws IOException;

		void disconnect();
	}

	/*
	 * Helper interface to handle communication timeouts.
	 */
	protected interface TimeoutTimer {

		void start();

		void stop();
	}

	/**
	 * Creates new instance of the class.
	 * 
	 * @param timeout
	 *            timeout value in milliseconds for connect/read/write
	 *            operations
	 */
	public SatelModule(int timeout) {
		this.integraType = IntegraType.UNKNOWN;
		this.timeout = timeout;

		addEventListener(this);
		registerCommands();
	}

	/**
	 * Returns type of Integra connected to the module.
	 * 
	 * @return Integra type
	 */
	public IntegraType getIntegraType() {
		return this.integraType;
	}

	/**
	 * Returns configured timeout value.
	 * 
	 * @return timeout value as milliseconds
	 */
	public int getTimeout() {
		return this.timeout;
	}

	public boolean isConnected() {
		return this.channel != null;
	}

	/**
	 * Returns status of initialization.
	 * 
	 * @return <code>true</code> if module is properly initialized and ready for
	 *         sending commands
	 */
	public boolean isInitialized() {
		return this.integraType != IntegraType.UNKNOWN;
	}

	protected abstract CommunicationChannel connect();

	/**
	 * Starts communication.
	 */
	public synchronized void open() {
		this.communicationWatchdog = new CommunicationWatchdog();
	}

	/**
	 * Stops communication by disconnecting from the module and stopping all
	 * background tasks.
	 */
	public synchronized void close() {
		if (this.communicationWatchdog != null) {
			this.communicationWatchdog.close();
			this.communicationWatchdog = null;
		}
	}

	/**
	 * Enqueues specified command in send queue if not already enqueued.
	 * 
	 * @param cmd
	 *            command to enqueue
	 * @return <code>true</code> if operation succeeded
	 */
	public boolean sendCommand(SatelMessage cmd) {
		try {
			if (this.sendQueue.contains(cmd)) {
				logger.debug("Command already in the queue: {}", cmd);
			} else {
				this.sendQueue.put(cmd);
				logger.trace("Command enqueued: {}", cmd);
			}
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void incomingEvent(SatelEvent event) {
		if (event instanceof IntegraVersionEvent) {
			IntegraVersionEvent versionEvent = (IntegraVersionEvent) event;
			this.integraType = IntegraType.valueOf(versionEvent.getType() & 0xFF);
			this.integraVersion = versionEvent.getVersion();
			logger.info("Connection to {} initialized. Version: {}.", this.integraType.getName(), this.integraVersion);
		}
	}

	private void registerCommands() {
		this.supportedCommands.put(IntegraVersionCommand.COMMAND_CODE, new IntegraVersionCommand(this));
		this.supportedCommands.put(NewStatesCommand.COMMAND_CODE, new NewStatesCommand(this));
		for (StateType state : PartitionState.values()) {
			this.supportedCommands.put(state.getRefreshCommand(), new IntegraStateCommand(state, this));
		}
		for (StateType state : ZoneState.values()) {
			this.supportedCommands.put(state.getRefreshCommand(), new IntegraStateCommand(state, this));
		}
		for (StateType state : OutputState.values()) {
			this.supportedCommands.put(state.getRefreshCommand(), new IntegraStateCommand(state, this));
		}
		for (StateType state : DoorsState.values()) {
			this.supportedCommands.put(state.getRefreshCommand(), new IntegraStateCommand(state, this));
		}
		for (ControlType ct : PartitionControl.values()) {
			this.supportedCommands.put(ct.getControlCommand(), new ControlObjectCommand(ct, this));
		}
		for (ControlType ct : OutputControl.values()) {
			this.supportedCommands.put(ct.getControlCommand(), new ControlObjectCommand(ct, this));
		}
		this.supportedCommands.put(IntegraStatusCommand.COMMAND_CODE, new IntegraStatusCommand(this));
		this.supportedCommands.put(ClearTroublesCommand.COMMAND_CODE, new ClearTroublesCommand(this));
		this.supportedCommands.put(SetClockCommand.COMMAND_CODE, new SetClockCommand(this));
	}

	private SatelMessage readMessage() {
		try {
			InputStream is = this.channel.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			boolean inMessage = false;
			int syncBytes = 0;

			while (true) {
				// if timed out, exit
				int c = is.read();
				if (c < 0) {
					return null;
				}

				byte b = (byte) c;

				if (b == FRAME_SYNC) {
					if (inMessage) {
						if (syncBytes == 0) {
							// special sequence or end of message
							// wait for next byte
						} else {
							logger.warn("Received frame sync bytes, discarding input: {}", baos.size());
							// clear gathered bytes, we wait for new message
							inMessage = false;
							baos.reset();
						}
					}
					++syncBytes;
				} else {
					if (inMessage) {
						if (syncBytes == 0) {
							// in sync, we have next message byte
							baos.write(b);
						} else if (syncBytes == 1) {
							if (b == FRAME_SYNC_ESC) {
								baos.write(FRAME_SYNC);
							} else if (b == FRAME_END[1]) {
								// end of message
								break;
							} else {
								logger.warn("Received invalid byte {}, discarding input: {}", String.format("%02X", b),
										baos.size());
								// clear gathered bytes, we have new message
								inMessage = false;
								baos.reset();
							}
						} else {
							logger.error("Sync bytes in message: {}", syncBytes);
						}
					} else if (syncBytes >= 2) {
						// synced, we have first message byte
						inMessage = true;
						baos.write(b);
					} else {
						// discard all bytes until synced
					}
					syncBytes = 0;
				}

				// if meanwhile thread has been interrupted, exit the loop
				if (Thread.interrupted()) {
					return null;
				}
			}

			// return read message
			return SatelMessage.fromBytes(baos.toByteArray());

		} catch (IOException e) {
			if (!Thread.interrupted()) {
				logger.error("Unexpected exception occurred during reading a message", e);
			}
		}

		return null;
	}

	private boolean writeMessage(SatelMessage message) {
		try {
			OutputStream os = this.channel.getOutputStream();

			os.write(FRAME_START);
			for (byte b : message.getBytes()) {
				os.write(b);
				if (b == FRAME_SYNC) {
					os.write(FRAME_SYNC_ESC);
				}
			}
			os.write(FRAME_END);
			os.flush();
			return true;

		} catch (IOException e) {
			if (!Thread.interrupted()) {
				logger.error("Unexpected exception occurred during writing a message", e);
			}
		}

		return false;
	}

	private synchronized void disconnect() {
		this.sendQueue.clear();
		if (this.channel != null) {
			this.channel.disconnect();
			this.channel = null;
			// notify about connection status change
			this.dispatchEvent(new ConnectionStatusEvent(false));
		}
	}

	private void communicationLoop(TimeoutTimer timeoutTimer) {
		long reconnectionTime = 10 * 1000;
		boolean receivedResponse = false;

		try {
			while (!Thread.interrupted()) {
				// connect, if not connected yet
				if (this.channel == null) {
					long connectStartTime = System.currentTimeMillis();
					synchronized (this) {
						this.channel = connect();
					}

					if (this.channel == null) {
						// notify about connection failure
						this.dispatchEvent(new ConnectionStatusEvent(false));
						// try to reconnect after a while, if connection hasn't
						// been established
						Thread.sleep(reconnectionTime - System.currentTimeMillis() + connectStartTime);
						continue;
					}
				}

				SatelMessage message = this.sendQueue.take(), response = null;
				SatelCommand command = this.supportedCommands.get(message.getCommand());

				if (command == null) {
					logger.error("Unsupported command: {}", message);
					continue;
				}

				logger.debug("Sending message: {}", message);
				timeoutTimer.start();
				boolean sent = this.writeMessage(message);
				timeoutTimer.stop();

				if (sent) {
					logger.trace("Waiting for response");
					timeoutTimer.start();
					response = this.readMessage();
					timeoutTimer.stop();
					if (response != null) {
						logger.debug("Got response: {}", response);

						if (!receivedResponse) {
							receivedResponse = true;
							// notify about connection success after first response from the module
							this.dispatchEvent(new ConnectionStatusEvent(true));
						}

						command.handleResponse(response);
					}
				}

				// if either send or receive failed, exit thread
				if (!sent || response == null) {
					break;
				}

			}
		} catch (InterruptedException e) {
			// exit thread
		} catch (Exception e) {
			// unexpected error, log and exit thread
			logger.info("Unhandled exception occurred in communication loop, disconnecting.", e);
		} finally {
			// stop counting if thread interrupted
			timeoutTimer.stop();
		}

		disconnect();
	}

	/*
	 * Respawns communication thread in case on any error and interrupts it in
	 * case read/write operations take too long.
	 */
	private class CommunicationWatchdog extends Timer implements TimeoutTimer {
		private Thread thread;
		private volatile long lastActivity;

		public CommunicationWatchdog() {
			this.thread = null;
			this.lastActivity = 0;

			this.schedule(new TimerTask() {
				@Override
				public void run() {
					CommunicationWatchdog.this.checkThread();
				}
			}, 0, 1000);
		}

		@Override
		public void start() {
			this.lastActivity = System.currentTimeMillis();
		}

		@Override
		public void stop() {
			this.lastActivity = 0;
		}

		public void close() {
			// cancel timer first to prevent reconnect
			this.cancel();
			// then stop communication thread
			if (this.thread != null) {
				this.thread.interrupt();
				try {
					this.thread.join();
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}

		private void startCommunication() {
			if (this.thread != null && this.thread.isAlive()) {
				logger.error("Start communication canceled: communication thread is still alive");
				return;
			}
			// start new thread
			this.thread = new Thread(new Runnable() {
				@Override
				public void run() {
					logger.debug("Communication thread started");
					SatelModule.this.communicationLoop(CommunicationWatchdog.this);
					logger.debug("Communication thread stopped");
				}
			});
			this.thread.start();
			// if module is not initialized yet, send version command
			if (!SatelModule.this.isInitialized()) {
				SatelModule.this.sendCommand(IntegraVersionCommand.buildMessage());
			}
		}

		private void checkThread() {
			logger.trace("Checking communication thread: {}, {}", this.thread != null,
					Boolean.toString(this.thread != null && this.thread.isAlive()));
			if (this.thread != null && this.thread.isAlive()) {
				long timePassed = (this.lastActivity == 0) ? 0 : System.currentTimeMillis() - this.lastActivity;

				if (timePassed > SatelModule.this.timeout) {
					logger.error("Send/receive timeout, disconnecting module.");
					stop();
					this.thread.interrupt();
					try {
						// wait for the thread to terminate
						this.thread.join(100);
					} catch (InterruptedException e) {
						// ignore
					}
					SatelModule.this.disconnect();
				}
			} else {
				startCommunication();
			}
		}
	}
}
