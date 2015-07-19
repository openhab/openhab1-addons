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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	/** the list for listeners */
	private final List<EBusConnectorEventListener> listeners = 
			new ArrayList<EBusConnectorEventListener>();

	/** serial receive buffer */
	protected final ByteBuffer inputBuffer = ByteBuffer.allocate(100);

	/** input stream for eBus communication*/
	protected InputStream inputStream;

	/** output stream for eBus communication*/
	protected OutputStream outputStream;

	/** counts the re-connection tries */
	protected int reConnectCounter = 0;

	/** The thread pool to execute events without blocking  */
	protected ExecutorService threadPool;

	protected long lastReceiveTime = 0;
	
	/**
	 * Constructor
	 */
	public AbstractEBusConnector() {
		super("eBUS Connection Thread");
		this.setDaemon(true);
	}

	/**
	 * Connects the connector to it's backend system. It's important
	 * to connect before start the thread.
	 * @return
	 * @throws IOException
	 */
	public boolean connect() throws IOException {
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
		
		int read = -1;
		int bufferSize = 0;
		
		// loop until interrupt or reconnector count is -1 (to many retries)
		while (!isInterrupted() || reConnectCounter == -1) {
			try {

				if(inputStream == null) {
					reconnect();

				} else {
					read = inputStream.read();
					lastReceiveTime = System.nanoTime();
					bufferSize = inputStream.available();
					
					if(read == -1) {
						logger.info("eBUS read timeout occured, no data on bus ...");

						// currently disabled because of file handler issues!
						// reconnect
						//reconnect();

					} else {

						byte receivedByte = (byte)(read & 0xFF);

						// write received byte to input buffer
						inputBuffer.put(receivedByte);

						// the 0xAA byte is a end of a packet
						if(receivedByte == EBusTelegram.SYN) {

							// check if the buffer is empty and ready for
							// sending data
							onEBusSyncReceived(bufferSize==0);
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
				logger.error("eBUS telegram buffer overflow - not enough sync bytes received! Try to adjust eBus adapter.");
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
	protected void onEBusSyncReceived(boolean allowSend) throws IOException {

		if(inputBuffer.position() == 1 && inputBuffer.get(0) == EBusTelegram.SYN) {
			logger.trace("Auto-SYN byte received");

		} else if(inputBuffer.position() == 2 && inputBuffer.get(0) == EBusTelegram.SYN) {
			logger.warn("Collision on eBUS detected (SYN DATA SYNC Sequence) ...");

		}else if(inputBuffer.position() < 5) {
			logger.trace("Telegram to small, skip! Buffer: {}", 
					EBusUtils.toHexDumpString(inputBuffer));

		} else {
			byte[] receivedTelegram = Arrays.copyOf(inputBuffer.array(), inputBuffer.position());
			// execute event
			onEBusTelegramReceived(receivedTelegram);
		}

		// reset receive buffer
		inputBuffer.clear();
	}

	/**
	 * Called if a valid eBus telegram was received. Send to event
	 * listeners via thread pool to prevent blocking.
	 * @param telegram
	 */
	protected void onEBusTelegramReceived(final byte[] receivedTelegram) {
		
		if(threadPool == null) {
			logger.warn("ThreadPool not ready!");
			return;
		}
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				
				final EBusTelegram telegram = EBusUtils.processEBusData(receivedTelegram);
				if(telegram != null) {
					for (EBusConnectorEventListener listener : listeners) {
						listener.onTelegramReceived(telegram);
					}
				} else {
					logger.debug("Received telegram was invalid, skip!");
				}
				
			}
		});
	}

}
