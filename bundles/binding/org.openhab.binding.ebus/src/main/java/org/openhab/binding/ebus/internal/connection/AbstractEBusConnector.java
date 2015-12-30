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
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.openhab.binding.ebus.internal.EBusTelegram;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Sowada
 * @since 1.7.0
 */
public abstract class AbstractEBusConnector extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractEBusConnector.class);

	/** the send output queue */
	private final Queue<byte[]> outputQueue = new LinkedBlockingQueue<byte[]>(20);

	/** the list for listeners */
	private final List<EBusConnectorEventListener> listeners = new ArrayList<EBusConnectorEventListener>();

	/** serial receive buffer */
	private final ByteBuffer inputBuffer = ByteBuffer.allocate(100);

	/** input stream for eBus communication*/
	protected InputStream inputStream;

	/** output stream for eBus communication*/
	protected OutputStream outputStream;

	/** current lockout counter */
	private int lockCounter = 0;

	/** next send try is blocked */
	private boolean blockNextSend;

	/** last send try caused a collision */
	private boolean lastSendCollisionDetected = false;

	/** eBus lockout */
	private static int LOCKOUT_COUNTER_MAX = 3;

	private int reConnectCounter = 0;
	
	/** default sender id */
	private byte senderId = (byte)0xFF;

	/** The thread pool to execute events without blocking  */
	private ExecutorService threadPool;
	
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

	/**
	 * Constructor
	 */
	public AbstractEBusConnector() {
		super("eBus Connection Thread");
		this.setDaemon(true);
	}

	/**
	 * Connects the connector to it's backend system. It's important
	 * to connect before start the thread.
	 * @return
	 * @throws IOException
	 */
	public boolean connect() throws IOException {
		


		// reset ebus counter
		lockCounter = LOCKOUT_COUNTER_MAX;

		// reset global variables
		lastSendCollisionDetected = false;
		blockNextSend = false;
		
		outputQueue.clear();
		inputBuffer.clear();
		
		return true;
	}

	/**
	 * Disconnects the connector from it's backend system.
	 * @return
	 * @throws IOException
	 */
	public boolean disconnect() throws IOException {

		if(inputStream != null)
			inputStream.close();
		
		if(outputStream != null)
			outputStream.close();
		
		return true;
	}

	/**
	 * Add an eBus listener to receive valid eBus telegrams
	 * @param listener
	 */
	public void addEBusEventListener(EBusConnectorEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove an eBus listener
	 * @param listener
	 * @return
	 */
	public boolean removeEBusEventListener(EBusConnectorEventListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Reconnect to the eBus up to 50 times. Connection can be lost by restart
	 * heating system etc. Warning, can cause an file handler issue
	 * on Linux (Windows not tested!) with serial connections.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void reconnect() throws IOException, InterruptedException {
		if(disconnect()) {
			if(!connect()) {
				
				if(reConnectCounter < 50) {
					reConnectCounter++;
				} else {
					logger.error("Stop eBus connector after 50 tries, please check eBus adapter and restart eBus binding.");
					reConnectCounter = -1;
				}
				
				int sleepTime = reConnectCounter > 24 ? 30 : 5;
				
				logger.warn("Unable to connector to eBus, retry in {} seconds ...", sleepTime);
				Thread.sleep(sleepTime*1000);
				
			} else {
				reConnectCounter = 0;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		// create new thread pool to send received telegrams
		threadPool = Executors.newCachedThreadPool();
		
		// loop until interrupt or reconnector count is -1 (to many retries)
		while (!isInterrupted() || reConnectCounter == -1) {
			try {

				if(inputStream == null) {
					reconnect();

				} else {
					int read = inputStream.read();
					if(read == -1) {
						logger.error("eBus read timeout occured, ignore it currently!");

						// currently disabled because of file handler issues!
						// reconnect
						//reconnect();

					} else {
						
						byte receivedByte = (byte)(read & 0xFF);

						// write received byte to input buffer
						inputBuffer.put(receivedByte);

						// the 0xAA byte is a end of a packet
						if(receivedByte == EBusTelegram.SYN) {
							onEBusSyncReceived();
						}
					}
				}

			} catch (IOException e) {
				logger.error("An IO exception has occured! Try to reconnect eBus connector ...", e);

				try {
					reconnect();
				} catch (IOException e1) {
					logger.error(e.toString(), e);
				} catch (InterruptedException e1) {
					logger.error(e.toString(), e);
				}

			} catch (BufferOverflowException e) {
				logger.error("eBus telegram buffer overflow - not enough sync bytes received! Try to adjust eBus adapter.");
				inputBuffer.clear();
				
			} catch (Exception e) {
				logger.error(e.toString(), e);
				inputBuffer.clear();
			}
		}

		// disconnect the connector e.g. close serial port
		try {
			disconnect();
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
		
		// shutdown threadpool
		if(threadPool != null && !threadPool.isShutdown())
			threadPool.shutdown();
	}

	/**
	 * Called event if a SYN packet has been received
	 * @throws IOException
	 */
	protected void onEBusSyncReceived() throws IOException {

		if(inputBuffer.position() == 1 && inputBuffer.get(0) == EBusTelegram.SYN) {
			if(lockCounter > 0) lockCounter--;
			logger.trace("Auto-SYN byte received");

			// send a telegram from queue if available
			send(false);

		} else if(inputBuffer.position() == 2 && inputBuffer.get(0) == EBusTelegram.SYN) {
			logger.warn("Collision on eBus detected (SYN DATA SYNC Sequence) ...");
			blockNextSend = true;

			// send a telegram from queue if available
			send(false);

		}else if(inputBuffer.position() < 5) {
			if(lockCounter > 0) lockCounter--;
			logger.trace("Telegram to small, skip!");

			// send a telegram from queue if available
			send(false);

		} else {
			if(lockCounter > 0) lockCounter--;
			byte[] receivedTelegram = Arrays.copyOf(inputBuffer.array(), inputBuffer.position());

			// send a telegram from queue if available, time critical!
			send(false);

			// After senden we can process the last received telegram
			final EBusTelegram telegram = EBusUtils.processEBusData(receivedTelegram);
			if(telegram != null) {

				// execute event
				onEBusTelegramReceived(telegram);

			} else {
				logger.debug("Received telegram was invalid, skip!");
			}
		}

		// reset receive buffer
		inputBuffer.clear();
	}

	/**
	 * Called if a valid eBus telegram was received. Send to event
	 * listeners via thread pool to prevent blocking.
	 * @param telegram
	 */
	protected void onEBusTelegramReceived(final EBusTelegram telegram) {
		
		if(threadPool == null) {
			logger.warn("ThreadPool not ready!");
			return;
		}
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				for (EBusConnectorEventListener listener : listeners) {
					listener.onTelegramReceived(telegram);
				}
			}
		});
	}

	/**
	 * Add a byte array to send queue.
	 * @param data
	 * @return
	 */
	public boolean send(byte[] data) {
		
		if(data == null || data.length == 0) {
			logger.debug("Send data is empty, skip");
			return false;
		}
		
		byte crc = 0;
		for (int i = 0; i < data.length-1; i++) {
			byte b = data[i];
			crc = EBusUtils.crc8_tab(b, crc);
		}

		// replace crc with calculated value
		data[data.length-1] = crc;

		return outputQueue.add(data);
	}

	/**
	 * Internal send function. Send and read to detect byte collisions.
	 * @param secondTry
	 * @throws IOException
	 */
	protected void send(boolean secondTry) throws IOException {

		// blocked for this send slot because a collision
		if(blockNextSend) {
			logger.trace("Sender was blocked for this SYN ...");
			blockNextSend = false;
			return;
		}

		// currently no data to send
		if(outputQueue.isEmpty()) {
			logger.trace("Send buffer is empty, nothing to send...");
			return;
		}

		// counter not zero, it's not allowed to send yet
		if(lockCounter > 0) {
			logger.trace("No access to ebus because the lock counter ...");
			return;
		}

		byte[] dataOutputBuffer = outputQueue.peek();
		logger.debug("EBusSerialPortEvent.send() data: {}", EBusUtils.toHexDumpString(dataOutputBuffer));

		// clear first
		inputBuffer.clear();

		boolean isMasterAddr = EBusUtils.isMasterAddress(dataOutputBuffer[1]);

		// send command
		for (int i = 0; i < dataOutputBuffer.length; i++) {
			byte b = dataOutputBuffer[i];
			outputStream.write(b);

			// directly read the current wrote byte from bus
			int read = inputStream.read();
			if(read != -1) {

				byte r = (byte) (read & 0xFF);
				inputBuffer.put(r);

				// do arbitation on on first byte only
				if(i == 0 && b != r) {

					// written and read byte not identical, that's
					// a collision
					logger.warn("eBus collision detected!");

					// last send try was a collision
					if(lastSendCollisionDetected) {
						logger.warn("A second collision occured!");
						resetSend();
						return;
					}
					// priority class identical
					else if((byte) (r & 0x0F) == (byte) (b & 0x0F)) {
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
		if(dataOutputBuffer[1] == (byte)0xFE) {
			logger.warn("Broadcast send ..............");

			// sende master sync
			outputStream.write(EBusTelegram.SYN);
			inputBuffer.put(EBusTelegram.SYN);

		} else {

			int read = inputStream.read();
			if(read != -1) {
				byte ack = (byte) (read & 0xFF);
				inputBuffer.put(ack);

				if(ack == EBusTelegram.ACK_OK) {

					// if the telegram is a slave telegram we will
					// get data from slave
					if(!isMasterAddr) {

						// len of answer
						byte nn2 = (byte) (inputStream.read() & 0xFF);
						inputBuffer.put(nn2);

						byte crc = EBusUtils.crc8_tab(nn2, (byte) 0);

						if(nn2 > 16) {
							logger.warn("slave data to lang, invalid!");

							// resend telegram (max. once)
							if(!resend(secondTry))
								return;
						}

						// read slave data, be aware of 0x0A bytes
						while(nn2 > 0) {
							byte d = (byte) (inputStream.read() & 0xFF);
							inputBuffer.put(d);
							crc = EBusUtils.crc8_tab(d, crc);

							if(d != (byte)0xA) {
								nn2--;
							}
						}

						// read slave crc
						byte crc2 = (byte) (inputStream.read() & 0xFF);
						inputBuffer.put(crc2);

						// check slave crc
						if(crc2 != crc) {
							logger.warn("Slave CRC wrong, resend!");

							// Resend telegram (max. once)
							if(!resend(secondTry))
								return;
						}

						// sende master sync
						outputStream.write(EBusTelegram.ACK_OK);
						inputBuffer.put(EBusTelegram.ACK_OK);
					} // isMasterAddr check

					// send SYN byte
					outputStream.write(EBusTelegram.SYN);
					inputBuffer.put(EBusTelegram.SYN);

				} else if(ack == EBusTelegram.ACK_FAIL) {
					
					// clear uncompleted telegram
					inputBuffer.clear();
					
					// resend telegram (max. once)
					if(!resend(secondTry))
						return;

				} else if(ack == EBusTelegram.SYN) {
					logger.warn("No answer from slave, skip ...");
					
					// clear uncompleted telegram or it will result
					// in uncomplete but valid telegram!
					inputBuffer.clear();
					
					resetSend();
					return;

				} else {
					// Wow, wrong answer, and now?
					logger.warn("Received wrong telegram: {}", EBusUtils.toHexDumpString(inputBuffer));

					// clear uncompleted telegram
					inputBuffer.clear();
					
					// resend telegram (max. once)
					if(!resend(secondTry))
						return;
				}
			}
		}

		// after send process the received telegram
		byte[] buffer = Arrays.copyOf(inputBuffer.array(), inputBuffer.position());
		final EBusTelegram telegram = EBusUtils.processEBusData(buffer);
		if(telegram != null) {
			onEBusTelegramReceived(telegram);

		} else {
			logger.debug("Received telegram was invalid, skip!");
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
		outputQueue.poll();
	}
}
