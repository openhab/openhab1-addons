/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.com;

import java.util.Deque;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Enumeration;

import org.openhab.binding.cardio2e.internal.code.Cardio2eDecoder;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDecoder.Cardio2eDecodedTransactionListener;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDecodedTransactionEvent;
import org.openhab.binding.cardio2e.internal.code.Cardio2eLoginCommands;
import org.openhab.binding.cardio2e.internal.code.Cardio2eLoginTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eObjectTypes;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransactionTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * Cardio2e COM RS-232 handling class.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eCom {
	private static final Logger logger = LoggerFactory
			.getLogger(Cardio2eCom.class);
	private static final char CARDIO2E_END_TRANSACTION_TEST_CHARACTER = '\n'; // for
																				// testMode
																				// Cardio2eTransaction.CARDIO2E_END_TRANSACTION_CHARACTER
																				// substitution
	private static final int MIN_SENDING_TIMER_CYCLE_PERIOD = 50; // Lower limit
																	// for
																	// sending
																	// timer
																	// cycle
																	// period,
																	// prevents
																	// performance
																	// issues.
	private static final int MAX_SENDING_TRIES = 3; // Maximum tries for
													// transaction sending until
													// ACK/NACK is received
	private int minDelayBetweenReceivingAndSending = 200; // Minimum delay in
															// milliseconds that
															// we must wait
															// before send when
															// any data is
															// received.
	private int minDelayBetweenSendings = 300; // Minimum delay in milliseconds
												// between send actions.
	private int sendingTimerCyclePeriod; // Calculated sending timer cycle
											// period in milliseconds (must be
											// G.C.D. of
											// minDelayBetweenReceivingAndSending
											// and minDelayBetweenSendings)
	public boolean testMode = false;
	public Cardio2eDecoder decoder = null;
	private DecodedTransactionListener decodedTransactionListener = null;
	private SerialPort serialPort = null;
	private transient Vector<Cardio2eComEventListener> comEventListeners;
	private volatile Timer timer = null;
	private volatile TimerTask cyclicSend = null;
	private volatile boolean cyclicSending = false;
	private volatile Deque<Cardio2eTransaction> sendBuffer = new LinkedList<Cardio2eTransaction>();
	private volatile long lastReceivedTimeStamp;
	private volatile long lastSendTimeStamp;
	private volatile Cardio2eTransaction nextSendingTransaction = null;

	public Cardio2eCom() {
		calculateSendingTimerCyclePeriod();
		initializeDecodedTransactionListener();
	}

	public Cardio2eCom(String serialPort) {
		calculateSendingTimerCyclePeriod();
		initializeDecodedTransactionListener();
		this.setSerialPort(serialPort);
	}

	private void initializeDecodedTransactionListener() {
		decoder = new Cardio2eDecoder();
		decodedTransactionListener = new DecodedTransactionListener();
		decoder.addDecodedTransactionListener(decodedTransactionListener);
	}

	public interface Cardio2eComEventListener extends EventListener {
		public void receivedData(Cardio2eReceivedDataEvent e);

		public void isConnected(Cardio2eConnectionEvent e);
	}

	synchronized public void addReceivedDataListener(Cardio2eComEventListener l) {
		if (comEventListeners == null) {
			comEventListeners = new Vector<Cardio2eComEventListener>();
		}
		comEventListeners.addElement(l);
	}

	synchronized public void removeReceivedDataListener(
			Cardio2eComEventListener l) {
		if (comEventListeners == null) {
			comEventListeners = new Vector<Cardio2eComEventListener>();
		}
		comEventListeners.removeElement(l);
	}

	@SuppressWarnings("unchecked")
	protected void signalReceivedData(String data) {
		if (comEventListeners != null && !comEventListeners.isEmpty()) {
			Cardio2eReceivedDataEvent event = new Cardio2eReceivedDataEvent(
					this, data);
			Vector<Cardio2eComEventListener> targets; // make a copy of the
														// listener list in case
														// anyone adds/removes
														// listeners
			synchronized (this) {
				targets = (Vector<Cardio2eComEventListener>) comEventListeners
						.clone();
			}
			Enumeration<Cardio2eComEventListener> e = targets.elements();
			while (e.hasMoreElements()) {
				Cardio2eComEventListener l = (Cardio2eComEventListener) e
						.nextElement();
				l.receivedData(event);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void signalIsConnected(boolean isConnected) {
		if (comEventListeners != null && !comEventListeners.isEmpty()) {
			Cardio2eConnectionEvent event = new Cardio2eConnectionEvent(this,
					isConnected);
			Vector<Cardio2eComEventListener> targets; // make a copy of the
														// listener list in case
														// anyone adds/removes
														// listeners
			synchronized (this) {
				targets = (Vector<Cardio2eComEventListener>) comEventListeners
						.clone();
			}
			Enumeration<Cardio2eComEventListener> e = targets.elements();
			while (e.hasMoreElements()) {
				Cardio2eComEventListener l = (Cardio2eComEventListener) e
						.nextElement();
				l.isConnected(event);
			}
		}
	}

	public synchronized void connect() {
		try {
			serialPort.openPort();
			if (serialPort.isOpened()) {
				serialPort.setParams(SerialPort.BAUDRATE_9600,
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				timer = new Timer();
				serialPort.addEventListener(new PortReader(),
						SerialPort.MASK_RXCHAR);
			}
		} catch (SerialPortException ex) {
			logger.warn("Cannot open port: '{}'", ex.toString());
		} catch (Exception ex) {
			logger.warn("Unknown error in opening port: '{}'", ex.toString());
		} finally {
			signalIsConnected(isConnected());
		}
	}

	public void sendTransaction(Cardio2eTransaction transaction) { // Add
																	// transaction.primitiveStringTransaction
																	// to
																	// sendBuffer
																	// (get it
																	// from
																	// .toString()
																	// method if
																	// it is
																	// null)
		if (transaction != null) {
			if (tryConnect()) {
				boolean transactionAdded = false;
				try {
					Cardio2eTransaction sendingTransaction = transaction
							.deepClone(); // Prevents transaction modify after
											// add to buffer
					if (sendingTransaction.primitiveStringTransaction == null)
						sendingTransaction.primitiveStringTransaction = sendingTransaction
								.toString();
					if (sendingTransaction.isSmartSendingEnabled()) { // SMARTSENDING
																		// enqueue
						synchronized (sendBuffer) {
							boolean matchesLastTransaction = primitiveStringMatchesLastTransactionLikeMeInSendBuffer(
									sendingTransaction,
									sendingTransaction
											.smartSendingCanReplaceLikeMe(),
									!sendingTransaction.smartSendingEnqueueFirst);
							if (sendingTransaction.smartSendingEnqueueFirst) { // Enqueue
																				// first
																				// if
																				// was
																				// ordered
								sendBuffer.addFirst(sendingTransaction);
								transactionAdded = true;
							} else {
								if (!matchesLastTransaction) { // Enqueue last
																// if was not
																// found any
																// transaction
																// that matches
																// at buffer
																// tail
									sendBuffer.addLast(sendingTransaction);
									transactionAdded = true;
								}
							}
						}
					} else { // SIMPLE enqueue
						sendBuffer.addLast(sendingTransaction);
						transactionAdded = true;
					}
				} catch (Exception ex) {
					logger.warn("Unknown error in sending data: '{}'",
							ex.toString());
				} finally {
					if (transactionAdded)
						cyclicSendControl(true); // If a transaction is added,
													// will enable cyclic
													// sending and will start it
													// if not was started yet
				}
			} else {
				logger.warn("Error in sending data: port closed and cannot open");
			}
		} else {
			logger.warn("Error in sending data: transaction object cannot be null");
		}
	}

	private boolean primitiveStringMatchesLastTransactionLikeMeInSendBuffer(
			Cardio2eTransaction transaction,
			boolean purgeAllTransactionsLikeMe,
			boolean avoidPurgeLastTransactionIfPrimitiveStringMatches) {
		boolean foundLastTransactionIsLike = false;
		boolean primitiveStringMatch = false;
		Cardio2eTransaction currentTransaction = null;
		Iterator<Cardio2eTransaction> it = sendBuffer.descendingIterator();
		while (((!foundLastTransactionIsLike) || (purgeAllTransactionsLikeMe))
				&& (it.hasNext())) {
			currentTransaction = it.next();
			if ((currentTransaction.isLike(transaction))
					&& (currentTransaction.getTransactionType() == transaction
							.getTransactionType())) {
				if (foundLastTransactionIsLike) {
					if (purgeAllTransactionsLikeMe)
						it.remove(); // Purge others if was ordered
				} else {
					foundLastTransactionIsLike = true;
					if (currentTransaction.primitiveStringTransaction
							.equals(transaction.primitiveStringTransaction))
						primitiveStringMatch = true;
					if (purgeAllTransactionsLikeMe) { // Purge last if was
														// ordered and
														// conditions match
						if (!(primitiveStringMatch && avoidPurgeLastTransactionIfPrimitiveStringMatches))
							it.remove();
					}
				}
			}
		}
		return primitiveStringMatch;
	}

	private void clearBuffer() { // Clear all sending request stored in buffer
		cyclicSendControl(false);
		sendBuffer.clear();
	}

	public synchronized void disconnect() {
		if (isConnected()) {
			try {
				serialPort.removeEventListener();
				clearBuffer();
				tryLogout();
				Thread.sleep(minDelayBetweenSendings * MAX_SENDING_TRIES * 2);
				serialPort.closePort();
				signalIsConnected(isConnected());
			} catch (SerialPortException ex) {
				logger.warn("Cannot close port: '{}'", ex.toString());
			} catch (Exception ex) {
				logger.warn("Unknown error in closing port: '{}'",
						ex.toString());
			}
			if (!isConnected()) {
				timer.cancel();
				timer.purge();
			}
		}
	}

	public boolean isConnected() {
		if (serialPort != null) {
			return serialPort.isOpened();
		} else {
			return false;
		}
	}

	public String getSerialPort() {
		if (serialPort != null) {
			return serialPort.getPortName();
		} else {
			return null;
		}
	}

	public void setSerialPort(String serialPort) {
		this.serialPort = new SerialPort(serialPort);
	}

	private boolean tryConnect() {
		if (!isConnected())
			connect();
		return isConnected();
	}

	private synchronized void tryLogout() throws InterruptedException {
		// Note: Logout command is obsolete: only provided for legacy
		// compatibility.
		Cardio2eTransaction logoutTransaction = new Cardio2eLoginTransaction(
				Cardio2eLoginCommands.LOGOUT);
		sendTransaction(logoutTransaction);
	}

	private synchronized void cyclicSendControl(boolean enable) {
		if (enable) {
			if (!cyclicSending) {
				synchronized (timer) {
					cyclicSending = true;
					cyclicSend = new CyclicSend();
					timer.schedule(cyclicSend, 0, sendingTimerCyclePeriod);
				}
			}
		} else {
			if (cyclicSending) {
				synchronized (cyclicSend) {
					cyclicSend.cancel();
					cyclicSend = null;
					timer.purge();
					cyclicSending = false;
				}
			}
		}
	}

	public void setMinDelayBetweenReceivingAndSending(
			int minDelayBetweenReceivingAndSending) {
		this.minDelayBetweenReceivingAndSending = minDelayBetweenReceivingAndSending;
		calculateSendingTimerCyclePeriod();
	}

	public int getMinDelayBetweenReceivingAndSending() {
		return minDelayBetweenReceivingAndSending;
	}

	public void setMinDelayBetweenSendings(int minDelayBetweenSendings) {
		this.minDelayBetweenSendings = minDelayBetweenSendings;
		calculateSendingTimerCyclePeriod();
	}

	public int getMinDelayBetweenSendings() {
		return minDelayBetweenSendings;
	}

	private void calculateSendingTimerCyclePeriod() {
		int a = minDelayBetweenReceivingAndSending;
		int b = minDelayBetweenSendings;
		while (b > 0) {
			int temp = b;
			b = a % b;
			a = temp;
		}
		sendingTimerCyclePeriod = ((a < MIN_SENDING_TIMER_CYCLE_PERIOD) ? MIN_SENDING_TIMER_CYCLE_PERIOD
				: a);
		logger.trace("Calculated Sending Timer Cycle Period is {} ms.",
				sendingTimerCyclePeriod);
	}

	private void processSend() {
		if (nextSendingTransaction != null) { // Clear nextSendingTransaction if
												// ACK received or maximum tries
												// reached
			String primitiveSentTransaction = nextSendingTransaction.primitiveStringTransaction;
			primitiveSentTransaction = primitiveSentTransaction.substring(0,
					primitiveSentTransaction.length() - 1); // Removes
															// end
															// transaction
															// char
			if ((nextSendingTransaction.receiptACK)
					|| (nextSendingTransaction.sendingTries >= MAX_SENDING_TRIES)) {
				try {
					sendBuffer.remove(nextSendingTransaction);
				} catch (Exception ex) {
					logger.warn(
							"Error in removing transaction from sending buffer: '{}'",
							ex.toString());
				} finally {
					boolean discarded = !nextSendingTransaction.receiptACK;
					nextSendingTransaction = null;
					if (discarded)
						logger.warn(
								"Transaction '{}' was discarded, because it was sent {} times and no ACK receipt was received.",
								primitiveSentTransaction, MAX_SENDING_TRIES);
				}
			} else {
				logger.debug(
						"No ACK was received for '{}'. Will send it again...",
						primitiveSentTransaction);
			}
		}
		if (nextSendingTransaction == null) { // Looking for the next
												// transaction to send
			nextSendingTransaction = sendBuffer.peekFirst();
		}
		if (nextSendingTransaction == null) {
			cyclicSendControl(false);
		} else {
			try {
				lastSendTimeStamp = System.currentTimeMillis(); // Stores
																// time
																// stamp
																// for
																// timing
																// control
				String stringTransaction = nextSendingTransaction.primitiveStringTransaction;
				if (testMode) { // Adapts END transaction
								// character to test console
								// when test mode is enabled
					stringTransaction = stringTransaction
							.replace(
									Cardio2eTransaction.CARDIO2E_END_TRANSACTION_CHARACTER,
									Cardio2eCom.CARDIO2E_END_TRANSACTION_TEST_CHARACTER);
				}
				serialPort.writeString(stringTransaction);
				stringTransaction = stringTransaction.substring(0,
						stringTransaction.length() - 1); // Removes
															// end
															// transaction
															// char
				logger.debug("Sent '{}' to Cardio 2é", stringTransaction);
			} catch (SerialPortException ex) {
				logger.warn(
						"There was an error on writing string to port: '{}'",
						ex.toString());
			} catch (Exception ex) {
				logger.warn("Unexpected error in sending data: '{}'",
						ex.toString());
			} finally {
				try {
					if ((nextSendingTransaction.getTransactionType() != Cardio2eTransactionTypes.SET)
							|| (nextSendingTransaction.getObjectType() == Cardio2eObjectTypes.LOGIN)
							|| (testMode))
						nextSendingTransaction.receiptACK = true; // Only
																	// some SET
																	// transactions
																	// returns
																	// ACK
																	// (except
																	// login),
																	// so
																	// ACK
																	// is
																	// forced
																	// to all
																	// others
																	// transactions.
																	// Is
																	// also
																	// forced
																	// ACK
																	// if
																	// test
																	// mode
																	// is
																	// enabled.
					nextSendingTransaction.sendingTries++;
				} catch (Exception ex) {
					logger.warn(
							"Unexpected error in processing send data: '{}'",
							ex.toString());
				}
			}
		}
	}

	private class PortReader implements SerialPortEventListener {
		@Override
		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0) {
				try {
					lastReceivedTimeStamp = System.currentTimeMillis(); // Stores
																		// time
																		// stamp
																		// for
																		// timing
																		// control
					String data = serialPort.readString(event.getEventValue());
					if (testMode) { // Adapts END transaction
									// character to test console
									// when test mode is enabled
						data = data
								.replace(
										Cardio2eCom.CARDIO2E_END_TRANSACTION_TEST_CHARACTER,
										Cardio2eTransaction.CARDIO2E_END_TRANSACTION_CHARACTER);
					}
					logger.trace("Received data from Cardio 2é: '{}'", data);
					signalReceivedData(data);
					decoder.decodeReceivedCardio2eStream(data);
				} catch (SerialPortException ex) {
					logger.warn(
							"Error in receiving string from COM-port: '{}'",
							ex.toString());
				} catch (Exception ex) {
					logger.warn(
							"Error in processing string from COM-port: '{}'",
							ex.toString());
				}
			}
		}
	}

	private class CyclicSend extends TimerTask {
		public synchronized void run() {
			if ((lastReceivedTimeStamp + minDelayBetweenReceivingAndSending) <= (System
					.currentTimeMillis())) {
				if ((lastSendTimeStamp + minDelayBetweenSendings) <= (System
						.currentTimeMillis())) {
					processSend();
				}
			}
		}
	}

	private class DecodedTransactionListener implements
			Cardio2eDecodedTransactionListener {
		public DecodedTransactionListener() {
		}

		public void decodedTransaction(Cardio2eDecodedTransactionEvent e) {
			Cardio2eTransaction transaction = e.getDecodedTransaction();
			String primitiveReceivedTransaction = transaction.primitiveStringTransaction;
			primitiveReceivedTransaction = primitiveReceivedTransaction
					.substring(0, primitiveReceivedTransaction.length() - 1); // Removes
																				// end
																				// transaction
																				// char
			logger.debug("Decoded '{}'", primitiveReceivedTransaction);
			Cardio2eTransaction lastSentTransaction = nextSendingTransaction;
			if (lastSentTransaction != null) { // Checks receipt ACK (only if
												// lastSentTransaction is not
												// null and transaction type is
												// "SET")
				try {
					if (lastSentTransaction.getTransactionType() == Cardio2eTransactionTypes.SET) {
						if ((transaction.getTransactionType() == Cardio2eTransactionTypes.ACK)
								|| (transaction.getTransactionType() == Cardio2eTransactionTypes.NACK)) {
							if (lastSentTransaction.isLike(transaction)) {
								lastSentTransaction.receiptACK = true;
								String primitiveSentTransaction = lastSentTransaction.primitiveStringTransaction;
								primitiveSentTransaction = primitiveSentTransaction
										.substring(0, primitiveSentTransaction
												.length() - 1); // Removes
																// end
																// transaction
																// char
								logger.debug(
										"Receipt ACK: '{}' {} transaction RECEIVED.",
										primitiveSentTransaction,
										lastSentTransaction.getObjectType());
							}
						}
					}
				} catch (Exception ex) {
					logger.warn(
							"Unexpected error in checking sent transaction receipt ACK: '{}'",
							ex.toString());
				}
			}
		}
	}
}