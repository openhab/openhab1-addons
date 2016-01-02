/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upb.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openhab.binding.upb.internal.UPBReader.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to write data to the UPB modem.
 * 
 * @author cvanorman
 *
 */
public class UPBWriter {

	/**
	 * Time in milliseconds to wait for an ACK from the modem after writing a
	 * message.
	 */
	private static long ACK_TIMEOUT = 500;

	private static final Logger logger = LoggerFactory.getLogger(UPBWriter.class);

	/**
	 * Asynchronous queue for writing data to the UPB modem.
	 */
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	/**
	 * The UPB modem's OutputStream.
	 */
	private OutputStream outputStream;

	/**
	 * UPBReader that is monitoring the modem's InputStream.
	 */
	private UPBReader upbReader;

	/**
	 * Instantiates a new {@link UPBWriter} using the given modem
	 * {@link OutputStream}.
	 * 
	 * @param outputStream
	 *            the {@link OutputStream} from the UPB modem.
	 * @param upbReader
	 *            the {@link UPBReader} that is monitoring the same UPB modem.
	 */
	public UPBWriter(OutputStream outputStream, UPBReader upbReader) {
		this.outputStream = outputStream;
		this.upbReader = upbReader;
	}

	/**
	 * Queues a message to be written to the modem.
	 * 
	 * @param message
	 *            the message to be written.
	 */
	public void queueMessage(MessageBuilder message) {
		String data = message.build();
		logger.debug("Queueing message {}.", data);
		executor.execute(new Message(data.getBytes()));
	}

	/**
	 * Cancels all queued messages and releases resources. This instance cannot
	 * be used again and a new {@link UPBWriter} must be instantiated after
	 * calling this method.
	 */
	public void shutdown() {
		executor.shutdownNow();
	}

	/**
	 * {@link Runnable} implementation used to write data to the UPB modem.
	 * 
	 * @author cvanorman
	 *
	 */
	private class Message implements Runnable, Listener {

		private boolean waitingOnAck = true;
		private byte[] data;

		private Message(byte[] data) {
			this.data = data;
		}

		private synchronized void ackReceived() {
			waitingOnAck = false;
			notify();
		}

		private synchronized void waitForAck() {
			long start = System.currentTimeMillis();
			while (waitingOnAck && (System.currentTimeMillis() - start) < ACK_TIMEOUT) {
				try {
					wait(ACK_TIMEOUT);
				} catch (InterruptedException e) {

				}

				if (!waitingOnAck) {
					logger.debug("Message {} ack received.", new String(data));
				} else {
					logger.debug("Message {} ack timed out.", new String(data));
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void messageReceived(UPBMessage message) {
			switch (message.getType()) {
			case NONE:
			case ACCEPT:
				break;
			default:
				upbReader.removeListener(this);
				ackReceived();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			try {

				upbReader.addListener(this);

				logger.debug("Writing bytes: {}", new String(data));
				outputStream.write(0x14);
				outputStream.write(data);
				outputStream.write(0x0d);

				waitForAck();
			} catch (IOException e) {
			}
		}
	}
}
