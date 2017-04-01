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

import org.openhab.binding.cardio2e.internal.code.Cardio2eLoginCommands;
import org.openhab.binding.cardio2e.internal.code.Cardio2eLoginTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransaction;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * Cardio2e COM RS-232 handling class.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.10.0
 */

public class Cardio2eCom {
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
	private SerialPort serialPort = null;
	private transient Vector<Cardio2eComEventListener> comEventListeners;
	private volatile Timer timer = null;
	private volatile TimerTask cyclicSend = null;
	private volatile boolean cyclicSending = false;
	private volatile Deque<Cardio2eTransaction> sendBuffer = new LinkedList<Cardio2eTransaction>();
	private volatile long lastReceivedTimeStamp;
	private volatile long lastSendTimeStamp;

	public Cardio2eCom() {
		calculateSendingTimerCyclePeriod();
	}

	public Cardio2eCom(String serialPort) {
		calculateSendingTimerCyclePeriod();
		this.setSerialPort(serialPort);
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
			// System.out.println("Can not open port т: " + ex);
		} catch (Exception e) {
			// System.out.println("Unknown error opening port: " + e);
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
				} catch (Exception e) {
					// System.out.println("Unknown error sending data: " + e);
				} finally {
					if (transactionAdded)
						cyclicSendControl(true); // If a transaction is added,
													// will enable cyclic
													// sending and will start it
													// if not was started yet
				}
			} else {
				// System.out.println("Error sending data: port closed and cannot open");
			}
		} else {
			// System.out.println("Error sending data: transaction object cannot be null");
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
		sendBuffer.clear();
	}

	public synchronized void disconnect() {
		if (isConnected()) {
			try {
				serialPort.removeEventListener();
				clearBuffer();
				tryLogout();
				Thread.sleep(minDelayBetweenSendings * 2);
				serialPort.closePort();
				signalIsConnected(isConnected());
			} catch (SerialPortException ex) {
				// System.out.println("Can not close port т: " + ex);
			} catch (Exception e) {
				// System.out.println("Unknown error closing port т: " + e);
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
		sendTransaction(new Cardio2eLoginTransaction(
				Cardio2eLoginCommands.LOGOUT));
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
		// System.out.println("Calculated Sending Timer Cycle Period is " +
		// sendingTimerCyclePeriod);//Testing output
	}

	private class PortReader implements SerialPortEventListener {
		@Override
		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0) {
				try {
					lastReceivedTimeStamp = System.currentTimeMillis(); // stores
																		// time
																		// stamp
																		// for
																		// timing
																		// control
					String data = serialPort.readString(event.getEventValue());
					/*
					 * System.out.print("Received response: " + receivedData+
					 * "( ");//Testing output for (char a :
					 * receivedData.toCharArray()){
					 * System.out.print(String.format("%04x", (int) a)+" "); }
					 * System.out.print(" )\n");
					 */
					signalReceivedData((testMode) ? data
							.replace(
									Cardio2eCom.CARDIO2E_END_TRANSACTION_TEST_CHARACTER,
									Cardio2eTransaction.CARDIO2E_END_TRANSACTION_CHARACTER)
							: data);
				} catch (SerialPortException ex) {
					// System.out.println("Error in receiving string from COM-port: "
					// + ex);
				}
			}
		}
	}

	private class CyclicSend extends TimerTask {
		public synchronized void run() {
			synchronized (sendBuffer) {
				if (sendBuffer.isEmpty()) {
					cyclicSendControl(false);
				} else {
					if ((lastReceivedTimeStamp + minDelayBetweenReceivingAndSending) <= (System
							.currentTimeMillis())) {
						if ((lastSendTimeStamp + minDelayBetweenSendings) <= (System
								.currentTimeMillis())) { // Do send
							try {
								lastSendTimeStamp = System.currentTimeMillis(); // stores
																				// time
																				// stamp
																				// for
																				// timing
																				// control
								String data = sendBuffer.poll().primitiveStringTransaction;
								String stringTransaction = ((testMode) ? data
										.replace(
												Cardio2eTransaction.CARDIO2E_END_TRANSACTION_CHARACTER,
												Cardio2eCom.CARDIO2E_END_TRANSACTION_TEST_CHARACTER)
										: data);
								serialPort.writeString(stringTransaction);
								// System.out.println("Sent "+stringTransaction);
								// //Testing output
							} catch (SerialPortException ex) {
								// System.out.println("There is an error on writing string to port т: "
								// + ex);
							} catch (Exception ex) {
								// System.out.println("Unexpected error sending data: "
								// + ex);
							}
						}
					}
				}
			}
		}
	}
}