/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upb.internal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class monitors the input stream of a UPB modem. This is done
 * asynchronously. When messages are received, they are broadcast to all
 * subscribed {@link Listener listeners}.
 * 
 * @author cvanorman
 *
 */
public class UPBReader implements Runnable {

	/**
	 * Listener class for handling received messages. A listener can be added by
	 * calling {@link UPBReader#addListener(Listener)}.
	 * 
	 * @author cvanorman
	 *
	 */
	public interface Listener {

		/**
		 * Called whenever a message has been received from the UPB modem.
		 * 
		 * @param message
		 *            the message that was received.
		 */
		void messageReceived(UPBMessage message);
	}

	private static final Logger logger = LoggerFactory.getLogger(UPBReader.class);

	private Collection<Listener> listeners = new LinkedHashSet<>();
	private byte[] buffer = new byte[512];
	private int bufferLength = 0;
	private InputStream inputStream;
	private Thread thread;

	/**
	 * Instantiates a new {@link UPBReader}.
	 * 
	 * @param inputStream
	 *            the inputStream from the UPB modem.
	 */
	public UPBReader(InputStream inputStream) {
		this.inputStream = inputStream;

		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Subscribes the listener to any future message events.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public synchronized void addListener(Listener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the listener from further messages.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public synchronized void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	/**
	 * Adds data to the buffer.
	 * 
	 * @param data
	 *            the data to add.
	 * @param length
	 *            the length of data to add.
	 */
	private void addData(byte[] data, int length) {

		if (bufferLength + length > buffer.length) {
			// buffer overflow discard entire buffer
			bufferLength = 0;
		}

		System.arraycopy(data, 0, buffer, bufferLength, length);

		bufferLength += length;

		interpretBuffer();
	}

	/**
	 * Shuts the reader down.
	 */
	public void shutdown() {
		if (thread != null) {
			thread.interrupt();
		}
	}

	/**
	 * Attempts to interpret any messages that may be contained in the buffer.
	 */
	private void interpretBuffer() {
		if (bufferLength > 1) {
			String type = new String(new byte[] { buffer[0], buffer[1] });
			int messageLength = 2;
			boolean isMessage = "PU".equals(type);

			if (isMessage) {
				// The message is sent as ASCII so we need to read 4 bytes and
				// interpret them as 2 bytes to get the control word.
				byte[] header = DatatypeConverter.parseHexBinary(new String(new byte[] { buffer[2], buffer[3],
						buffer[4], buffer[5] }));
				ControlWord controlWord = new ControlWord();
				controlWord.setBytes(header[1], header[0]);

				// The message is sent as ASCII so we need to read 2 bytes for
				// every byte in the message (plus 2 for the inital type code).
				messageLength = controlWord.getPacketLength() * 2 + 2;
			}

			if (messageLength <= bufferLength) {
				String message = new String(Arrays.copyOfRange(buffer, 0, messageLength));
				logger.debug("UPB Message: {}", message);

				int remainingBuffer = bufferLength - messageLength;

				if (remainingBuffer > 0) {
					System.arraycopy(buffer, messageLength, buffer, 0, remainingBuffer);
				}
				bufferLength = 0;

				notifyListeners(UPBMessage.fromString(message));
			}
		}
	}

	private synchronized void notifyListeners(UPBMessage message) {
		for (Listener l : new ArrayList<>(listeners)) {
			l.messageReceived(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		byte[] buffer = new byte[256];
		try {
			for (int len = -1; (len = inputStream.read(buffer, 0, buffer.length)) > 0;) {

				if (Thread.interrupted()) {
					break;
				}

				addData(buffer, len);
			}
		} catch (Exception e) {
			logger.debug("Failed to read input stream.", e);
		}
		logger.debug("UPB MessageInterpreter stopped.");
	}
}
