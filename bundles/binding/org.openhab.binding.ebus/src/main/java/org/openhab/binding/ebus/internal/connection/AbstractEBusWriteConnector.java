/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.connection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.openhab.binding.ebus.internal.EBusTelegram;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Sowada
 * @since 1.7.1
 */
public abstract class AbstractEBusWriteConnector extends AbstractEBusConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractEBusWriteConnector.class);

	/** eBUS lockout */
	private static int LOCKOUT_COUNTER_MAX = 3;

	/** next send try is blocked */
	private boolean blockNextSend;

	/** last send try caused a collision */
	private boolean lastSendCollisionDetected = false;

	/** current lockout counter */
	private int lockCounter = 0;

	/** internal structure to store send attempts */
	protected class QueueEntry {
		public byte[] buffer;
		public int sendAttempts = 0;

		public QueueEntry(byte[] buffer) {
			this.buffer = buffer;
		}
	}

	/** the send output queue */
	private final Queue<QueueEntry> outputQueue = new LinkedBlockingQueue<QueueEntry>(20);

	/** default sender id */
	private byte senderId = (byte)0xFF;

	/** data to send */
	private QueueEntry sendEntry;

	/**
	 * Returns the eBus binding used sender Id
	 * @return
	 */
	public byte getSenderId() {
		return senderId;
	}

	/**
	 * Set the eBus binding sender Id
	 * @param senderId The new id, default is 0xFF
	 */
	public void setSenderId(byte senderId) {
		this.senderId = senderId;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.internal.connection.AbstractEBusConnector#connect()
	 */
	@Override
	protected boolean connect() throws IOException {

		// reset eBUS counter
		lockCounter = LOCKOUT_COUNTER_MAX;

		// reset global variables
		lastSendCollisionDetected = false;
		blockNextSend = false;

		outputQueue.clear();

		return super.connect();
	}

	/**
	 * Add a byte array to send queue.
	 * @param data
	 * @return
	 */
	public boolean addToSendQueue(byte[] data) {

		if(data == null || data.length == 0) {
			logger.trace("Send data is empty, skip");
			return false;
		}

		byte crc = 0;
		for (int i = 0; i < data.length-1; i++) {
			byte b = data[i];
			crc = EBusUtils.crc8_tab(b, crc);
		}

		// replace crc with calculated value
		data[data.length-1] = crc;

		boolean result = false;
		
		try {
			result = outputQueue.add(new QueueEntry(data));
		} catch (IllegalStateException e) {
			logger.error("Send queue is full! The eBUS service will reset the queue to ensure proper operation.");
			
			outputQueue.clear();
			resetSend();
			
			result = outputQueue.add(new QueueEntry(data));
		}
		
		return result;
	}

	private void checkSendStatus() {

		if(lockCounter > 0)
			lockCounter--;

		// blocked for this send slot because a collision
		if(blockNextSend) {
			logger.trace("Sender was blocked for this SYN ...");
			blockNextSend = false;
			return;
		}

		// counter not zero, it's not allowed to send yet
		if(lockCounter > 0) {
			logger.trace("No access to ebus because the lock counter ...");
			return;
		}

		// currently no data to send
		if(outputQueue.isEmpty()) {
			return;
		}

		if(sendEntry != null) {
			if(sendEntry.sendAttempts == 10) {
				logger.error("Skip telegram {} after 10 attempts ...", 
						EBusUtils.toHexDumpString(sendEntry.buffer));
				resetSend();
			}

			return;
		}

		// get next entry from stack
		sendEntry = outputQueue.peek();
	}
	
	/**
	 * Internal send function. Send and read to detect byte collisions.
	 * @param secondTry
	 * @throws IOException
	 */
	protected void send(boolean secondTry) throws IOException {

		if(sendEntry == null) {
			return;
		}

		byte[] dataOutputBuffers = sendEntry.buffer;
		ByteBuffer sendBuffer = ByteBuffer.allocate(100);

		// count as send attempt
		sendEntry.sendAttempts++;

		int read = 0;
		byte readByte = 0;
		long readWriteDelay = 0;
		
		// clear input buffer to start by zero
		resetInputBuffer();
		
		// send command
		for (int i = 0; i < dataOutputBuffers.length; i++) {
			byte b = dataOutputBuffers[i];

			writeByte(b);
			
			if(i==0) {
				
				read = readByte(true);
				readByte = (byte) (read & 0xFF);
				sendBuffer.put(readByte);

				if(b != readByte) {

					// written and read byte not identical, that's
					// a collision
					if(readByte == EBusTelegram.SYN) {
						logger.debug("eBus collision with SYN detected!");
					} else {
						logger.debug("eBus collision detected! 0x{}", 
								EBusUtils.toHexDumpString(readByte));
					}

					// last send try was a collision
					if(lastSendCollisionDetected) {
						logger.warn("A second collision occured!");
						resetSend();
						return;
					}
					// priority class identical
					else if((byte) (readByte & 0x0F) == (byte) (b & 0x0F)) {
						logger.trace("Priority class match, restart after next SYN ...");
						lastSendCollisionDetected = true;

					} else {
						logger.trace("Priority class doesn't match, blocked for next SYN ...");
						blockNextSend = true;
					}

					// stop after a collision
					return;
				}
			}

		}

		// sending master data finish

		// reset global variables
		lastSendCollisionDetected = false;
		blockNextSend = false;

		// if this telegram a broadcast?
		if(dataOutputBuffers[1] == (byte)0xFE) {

			logger.trace("Broadcast send ..............");

			// sende master sync
			writeByte(EBusTelegram.SYN);
			sendBuffer.put(EBusTelegram.SYN);

		} else {

			readWriteDelay = System.nanoTime();
			
			// copy input data to result buffer
			sendBuffer.put(dataOutputBuffers, 1, dataOutputBuffers.length-1);
			getInputStream().skip(dataOutputBuffers.length-1);
			readWriteDelay = (System.nanoTime() - readWriteDelay) / 1000;
			
			logger.trace("readin delay "+readWriteDelay);
			
			read = readByte(true);
			if(read != -1) {

				byte ack = (byte) (read & 0xFF);
				sendBuffer.put(ack);

				if(ack == EBusTelegram.ACK_OK) {

					boolean isMasterAddr = EBusUtils.isMasterAddress(dataOutputBuffers[1]);

					// if the telegram is a slave telegram we will
					// get data from slave
					if(!isMasterAddr) {

						// len of answer
						byte nn2 = (byte) (readByte(true) & 0xFF);
						sendBuffer.put(nn2);

						byte crc = EBusUtils.crc8_tab(nn2, (byte) 0);

						if(nn2 > 16) {
							logger.warn("slave data to long, invalid!");

							// resend telegram (max. once)
							if(!resend(secondTry))
								return;
						}

						// read slave data, be aware of 0x0A bytes
						while(nn2 > 0) {
							byte d = (byte) (readByte(true) & 0xFF);
							sendBuffer.put(d);
							crc = EBusUtils.crc8_tab(d, crc);

							if(d != (byte)0xA) {
								nn2--;
							}
						}

						// read slave crc
						byte crc2 = (byte) (readByte(true) & 0xFF);
						sendBuffer.put(crc2);

						// check slave crc
						if(crc2 != crc) {
							logger.warn("Slave CRC wrong, resend!");

							// Resend telegram (max. once)
							if(!resend(secondTry))
								return;
						}

						// sende master sync
						writeByte(EBusTelegram.ACK_OK);
						sendBuffer.put(EBusTelegram.ACK_OK);
					} // isMasterAddr check

					// send SYN byte
					writeByte(EBusTelegram.SYN);
					sendBuffer.put(EBusTelegram.SYN);

				} else if(ack == EBusTelegram.ACK_FAIL) {

					// clear uncompleted telegram
					sendBuffer.clear();

					// resend telegram (max. once)
					if(!resend(secondTry))
						return;

				} else if(ack == EBusTelegram.SYN) {
					logger.debug("No answer from slave for telegram: {}", EBusUtils.toHexDumpString(sendBuffer));

					// clear uncompleted telegram or it will result
					// in uncomplete but valid telegram!
					sendBuffer.clear();

					// resend instead skip ...
					//resetSend();
					return;

				} else {
					// Wow, wrong answer, and now?
					logger.debug("Received wrong telegram: {}", EBusUtils.toHexDumpString(sendBuffer));

					// clear uncompleted telegram
					sendBuffer.clear();

					// resend telegram (max. once)
					if(!resend(secondTry))
						return;
				}
			}
		}


		// after send process the received telegram
		if(sendBuffer.position() > 0) {
			byte[] buffer = Arrays.copyOf(sendBuffer.array(), sendBuffer.position());
			logger.debug("Succcesful send: {}", EBusUtils.toHexDumpString(buffer));
			onEBusTelegramReceived(buffer);
		}

		// reset send module
		resetSend();
	}

	/**
	 * Resend data if it's the first try or call resetSend()
	 * @param secondTry
	 * @return
	 * @throws IOException
	 */
	private boolean resend(boolean secondTry) throws IOException {
		if(!secondTry) {
			send(true);
			return true;

		} else {
			logger.warn("Resend failed, remove data from sending queue ...");
			resetSend();
			return false;
		}
	}

	/**
	 * Reset the send routine
	 */
	private void resetSend() {

		// reset ebus counter
		lockCounter = LOCKOUT_COUNTER_MAX;

		// reset global variables
		lastSendCollisionDetected = false;
		blockNextSend = false;

		// remove entry from sending queue
		sendEntry = null;
		outputQueue.poll();
	}

	/**
	 * Called event if a SYN packet has been received
	 * @throws IOException
	 */
	@Override
	protected void onEBusSyncReceived(boolean allowSend) throws IOException {

		if(allowSend)
			send(false);

		// afterwards check for next sending slot
		checkSendStatus();

		// run read connector
		super.onEBusSyncReceived(allowSend);
	}
	
	/**
	 * Writes one byte to the backend
	 * @param b
	 * @throws IOException
	 */
	protected abstract void writeByte(int b) throws IOException;

	/**
	 * Resets the input buffer of input stream
	 * @throws IOException
	 */
	protected abstract void resetInputBuffer() throws IOException;
	
	/**
	 * Return the undelaying input stream
	 * @return
	 */
	protected abstract InputStream getInputStream();
}
