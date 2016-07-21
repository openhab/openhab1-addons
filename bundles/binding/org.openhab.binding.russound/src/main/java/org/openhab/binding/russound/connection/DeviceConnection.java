/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.russound.connection;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.russound.utils.StringHexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <b>Note:</b>This class was originally developed by Tom Gutwin but suffered
 * heavy refactoring recently.
 * </p>
 * <br>
 * 
 * A class that wraps the communication to Russound devices using the Ethernet
 * Integra Serial Control Protocol (eISCP).
 * 
 * @author Tom Gutwin P.Eng
 * @author Thomas.Eichstaedt-Engelen (Refactoring)
 * @author Pauli Anttila (Simplified, rewritten and added status update listener
 *         functionality)
 */
public class DeviceConnection {

	private static final Logger logger = LoggerFactory
			.getLogger(DeviceConnection.class);

	/** Connection test interval in milliseconds **/
	private static final int CONNECTION_TEST_INTERVAL = 600000;

	private DataListener dataListener = null;
	private ConnectionProvider mConnectionProvider;
	private int retryCount = 1;
	private ConnectionSupervisor connectionSupervisor = null;
	private CommandProvider mSupervisorCommandProvider = null;
	private ConnectionStateListener mConnectionStateListener;

	private SendCommandFormatter mSendCommandFormatter = null;

	private InputParser mInputParser = null;

	/**
	 * Constructor that takes your receivers IP and port.
	 **/
	public DeviceConnection(ConnectionProvider connectionProvider,
			InputParser inputParser, CommandProvider provider) {

		mConnectionProvider = connectionProvider;
		mInputParser = inputParser;
		mSupervisorCommandProvider = provider;

	}

	public void setSendCommandFormatter(SendCommandFormatter formatter) {
		mSendCommandFormatter = formatter;
	}

	/**
	 * Get retry count value.
	 **/
	private int getRetryCount() {
		return retryCount;
	}

	/**
	 * Set retry count value. How many times command is retried when error
	 * occurs.
	 **/
	private void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * Connects to the receiver by opening a socket connection through the IP
	 * and port defined on constructor.
	 **/
	public boolean connect() {
		boolean returnValue = false;
		returnValue = mConnectionProvider.connect();
		// start status update listener
		if (dataListener == null) {
			dataListener = new DataListener();
			dataListener.start();
		}

		// start connection tester
		if (connectionSupervisor == null) {
			connectionSupervisor = new ConnectionSupervisor(
					CONNECTION_TEST_INTERVAL);
		}
		logger.debug("connect() returning: " + returnValue);
		return returnValue;
	}

	public void setConnectionStateListener(ConnectionStateListener listener) {
		mConnectionStateListener = listener;
	}

	/**
	 * Closes the socket connection.
	 * 
	 * @return true if the closed successfully
	 **/
	public boolean disconnect() {
		logger.debug("Disconnect called");
		boolean isConnected = mConnectionProvider.isConnected();
		try {
			if (dataListener != null) {
				dataListener.setInterrupted(true);
				dataListener = null;
				logger.debug("closed data listener!");
			}
			if (connectionSupervisor != null) {
				connectionSupervisor.stopConnectionTester();
				connectionSupervisor = null;
				logger.debug("closed connection tester!");
			}
			mConnectionProvider.disconnect();
			isConnected = false;
		} catch (IOException ioException) {
			logger.error("Closing connection throws an exception!", ioException);
		} finally {
			mConnectionStateListener.isConnected(false);
		}

		return isConnected;
	}

	/**
	 * Sends to command to the receiver. It does not wait for a reply.
	 * 
	 * @param command
	 *            the command to send.
	 **/
	public void sendCommand(byte[] command) {

		sendCommand(command, false, retryCount);
	}

	/**
	 * Sends to command to the receiver.
	 * 
	 * @param byteArray
	 *            the command to send.
	 * @param closeSocket
	 *            flag to close the connection when done or leave it open.
	 * @param retry
	 *            retry count.
	 **/
	private void sendCommand(byte[] command, boolean closeSocket, int retry) {

		logger.debug(
				"send command called with bytes: "
						+ StringHexUtils.byteArrayToHex(command),
				"close socket: " + closeSocket);
		byte[] byteArray = command;
		if (mSendCommandFormatter != null)
			byteArray = mSendCommandFormatter.processCommand(command);
		boolean connected = connect();
		logger.trace("Is connected: " + connected);
		if (connected) {
			try {

				logger.debug("Sending {} bytes: {}", byteArray.length,
						StringHexUtils.byteArrayToHex(byteArray));

				mConnectionProvider.getOutputStream().write(byteArray);
				mConnectionProvider.getOutputStream().flush();
				if (mConnectionStateListener != null)
					mConnectionStateListener.isConnected(true);
				else
					logger.debug("Connection state listener is null");
				logger.trace("message sent");
			} catch (IOException ioException) {
				logger.error("Error occured when sending command", ioException);

				if (retry > 0) {
					logger.debug("Retry {}...", retry);
					disconnect();
					sendCommand(byteArray, closeSocket, retry--);
				} else {
					disconnect();
				}

			}
		}

		// finally close the socket if required ...
		if (closeSocket) {
			disconnect();
		}
	}

	/**
	 * This method wait any state messages form receiver.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws EiscpException
	 **/
	private void waitStateMessages() throws NumberFormatException, IOException,
			InterruptedException {

		if (mConnectionProvider.isConnected()) {

			logger.trace("Waiting status messages");

			int avail = 0;
			int total = 0;
			byte[] data = new byte[1024];
			while (true) {

				try {
					avail = mConnectionProvider.getInputStream().available();
					if (avail == 0) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					logger.trace("Avail: " + avail);
					try {
						int readCount = mConnectionProvider.getInputStream()
								.read(data, total, avail);
						total += readCount;
						byte[] trimmedData = Arrays.copyOf(data, total);

						mInputParser.parse(trimmedData);

					} catch (ArrayIndexOutOfBoundsException e) {
						logger.debug("array index out of bound", e);
					}

				} catch (IOException e) {
					logger.error("Could not receive Russound data", e);
					e.printStackTrace();
				}
				data = new byte[1024];
				total = 0;
			}

		} else {
			throw new IOException("Not Connected to Receiver");
		}
	}

	private class DataListener extends Thread {

		private boolean interrupted = false;

		DataListener() {
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
			this.interrupt();
		}

		@Override
		public void run() {

			// logger.debug("Data listener started");

			boolean restartConnection = false;
			int sleepLength = 1000;

			// as long as no interrupt is requested, continue running
			while (!interrupted) {
				try {
					waitStateMessages();
					sleepLength = 1000;

				}

				catch (SocketTimeoutException e) {

					// logger.error(
					// "No data received during supervision interval ({} sec)!",
					// SOCKET_TIMEOUT);

					restartConnection = true;

				} catch (Exception e) {

					if (interrupted != true && this.isInterrupted() != true) {
						logger.error("Error occured during message waiting", e);

						restartConnection = true;
						if (sleepLength < 1000 * 60 * 30)
							sleepLength = sleepLength * 2;
						// sleep a while, to prevent fast looping if error
						// situation
						// is permanent
						mysleep(sleepLength);
					}
				}

				if (restartConnection) {
					restartConnection = false;

					// reopen connection
					logger.debug("Reconnecting...");

					try {
						// connected = false;
						connect();
					} catch (Exception ex) {
						logger.error("Reconnection invoking error", ex);
					}

				}
			}

			logger.debug("Data listener stopped");
		}

		private void mysleep(long milli) {
			try {
				sleep(milli);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
	}

	private class ConnectionSupervisor {
		private Timer timer;

		public ConnectionSupervisor(int milliseconds) {
			logger.debug(
					"Connection supervisor started, interval {} milliseconds",
					milliseconds);

			timer = new Timer();
			timer.schedule(new Task(), milliseconds, milliseconds);
		}

		public void stopConnectionTester() {
			timer.cancel();
		}

		class Task extends TimerTask {
			public void run() {
				logger.debug("Attempting to confirm connection: "
						+ mConnectionProvider.isConnected());
				// if (mConnectionProvider.isConnected()) {
				// logger.debug("Test connection to {}:{}");// ,
				// receiverIP,receiverPort);
				try {
					byte[] command = mSupervisorCommandProvider.getCommand();
					logger.trace("Sending command: " + command);

					sendCommand(command, false, 0);
					logger.trace("connection supervisor command sent");
				} catch (Exception e) {
					logger.error("Error in connection supervisor", e);
					mConnectionStateListener.isConnected(false);
				}
				// }
			}
		}
	}
}
