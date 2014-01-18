/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULListener;
import org.openhab.io.transport.cul.CULMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract base class for all CULHandler which brings some convenience
 * regarding registering listeners and detecting forbidden messages.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public abstract class AbstractCULHandler implements CULHandler, CULHandlerInternal {

	/**
	 * Thread which sends all queued commands to the CUL.
	 * 
	 * @author Till Klocke
	 * @since 1.4.0
	 * 
	 */
	private class SendThread extends Thread {

		private final Logger logger = LoggerFactory.getLogger(SendThread.class);

		@Override
		public void run() {
			while (!isInterrupted()) {
				String command = sendQueue.poll();
				if (command != null) {
					if (!command.endsWith("\r\n")) {
						command = command + "\r\n";
					}
					try {
						writeMessage(command);
					} catch (CULCommunicationException e) {
						logger.error("Error while writing command to CUL", e);
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					logger.debug("Error while sleeping in SendThread", e);
				}
			}
		}
	}

	/**
	 * Wrapper class wraps a CULListener and a received Strings and gets
	 * executed by a executor in its own thread.
	 * 
	 * @author Till Klocke
	 * @since 1.4.0
	 * 
	 */
	private static class NotifyDataReceivedRunner implements Runnable {

		private String message;
		private CULListener listener;

		public NotifyDataReceivedRunner(CULListener listener, String message) {
			this.message = message;
			this.listener = listener;
		}

		@Override
		public void run() {
			listener.dataReceived(message);
		}

	}

	/**
	 * Executor to handle received messages. Every listern should be called in
	 * its own thread.
	 */
	protected Executor receiveExecutor = Executors.newCachedThreadPool();
	protected SendThread sendThread = new SendThread();

	protected String deviceName;
	protected CULMode mode;

	protected List<CULListener> listeners = new ArrayList<CULListener>();

	protected Queue<String> sendQueue = new ConcurrentLinkedQueue<String>();

	protected AbstractCULHandler(String deviceName, CULMode mode) {
		this.mode = mode;
		this.deviceName = deviceName;
	}

	@Override
	public CULMode getCULMode() {
		return mode;
	}

	@Override
	public void registerListener(CULListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void unregisterListener(CULListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public boolean hasListeners() {
		return listeners.size() > 0;
	}

	@Override
	public void open() throws CULDeviceException {
		openHardware();
		sendThread.start();
	}

	@Override
	public void close() {
		sendThread.interrupt();
		closeHardware();
	}

	/**
	 * initialize the CUL hardware and open the connection
	 * 
	 * @throws CULDeviceException
	 */
	protected abstract void openHardware() throws CULDeviceException;

	/**
	 * Close the connection to the hardware and clean up all resources.
	 */
	protected abstract void closeHardware();

	@Override
	public void send(String command) {
		if (isMessageAllowed(command)) {
			sendQueue.add(command);
		}
	}

	@Override
	public void sendWithoutCheck(String message) throws CULCommunicationException {
		sendQueue.add(message);
	}

	/**
	 * Write a message to the CUL.
	 * 
	 * @param message
	 * @throws CULCommunicationException
	 */
	protected abstract void writeMessage(String message) throws CULCommunicationException;

	/**
	 * Checks if the message would alter the RF mode of this device.
	 * 
	 * @param message
	 *            The message to check
	 * @return true if the message doesn't alter the RF mode, false if it does.
	 */
	protected boolean isMessageAllowed(String message) {
		if (message.startsWith("X") || message.startsWith("x")) {
			return false;
		}
		if (message.startsWith("Ar")) {
			return false;
		}
		return true;
	}

	/**
	 * Notifies each CULListener about the received data in its own thread.
	 * 
	 * @param data
	 */
	protected void notifyDataReceived(String data) {
		for (final CULListener listener : listeners) {
			receiveExecutor.execute(new NotifyDataReceivedRunner(listener, data));
		}
	}

	protected void notifyError(Exception e) {
		for (CULListener listener : listeners) {
			listener.error(e);
		}
	}
}
