/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.code;

/**
 * Cardio2e Binding structured curtain transaction model (only for newest
 * Cardio2e versions).
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eCurtainTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 160027467017676320L;
	private static boolean smartSendingEnabledClass = false; // Signals whether
																// the whole
																// class is
																// smart sending
																// enabled.
	private byte openingPercentage = -1; // -1=Not set, signaling
											// openingPercentage not set.
	private boolean lastMoveWasUp = false;
	private boolean lastMoveWasDown = false;

	public Cardio2eCurtainTransaction() {
		super.setObjectType(Cardio2eObjectTypes.CURTAIN);
	}

	public Cardio2eCurtainTransaction(short objectNumber) { // Simple curtain
															// GET transaction
															// constructor.
		setTransactionType(Cardio2eTransactionTypes.GET);
		super.setObjectType(Cardio2eObjectTypes.CURTAIN);
		setObjectNumber(objectNumber);
	}

	public Cardio2eCurtainTransaction(short objectNumber, byte openingPercentage) { // Simple
																					// SET
																					// transactionType
																					// constructor
																					// for
																					// curtain
																					// opening
																					// percentage
																					// control.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.CURTAIN);
		setObjectNumber(objectNumber);
		setOpeningPercentage(openingPercentage);
	}

	public Cardio2eCurtainTransaction(short objectNumber, boolean curtainOpen) { // Simple
																					// SET
																					// transactionType
																					// constructor
																					// for
																					// simple
																					// curtain
																					// opening
																					// control.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.CURTAIN);
		setObjectNumber(objectNumber);
		setCurtainOpen(curtainOpen);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber <= CARDIO2E_MAX_CURTAIN_NUMBER) {
			super.setObjectNumber(objectNumber);
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public byte getOpeningPercentage() {
		return openingPercentage;
	}

	public void setOpeningPercentage(byte openingPercentage) {
		isDataComplete = false;
		if ((openingPercentage >= CARDIO2E_MIN_CURTAIN_OPENING_PERCENTAGE)
				&& (openingPercentage <= CARDIO2E_MAX_CURTAIN_OPENING_PERCENTAGE)) {
			if (this.openingPercentage != -1) {
				if (openingPercentage > this.openingPercentage) {
					lastMoveWasDown = false;
					lastMoveWasUp = true;
				} else if (openingPercentage < this.openingPercentage) {
					lastMoveWasUp = false;
					lastMoveWasDown = true;
				} else {
					lastMoveWasUp = false;
					lastMoveWasDown = false;
				}
			}
			this.openingPercentage = openingPercentage;
		} else {
			throw new IllegalArgumentException("invalid openingPercentage '"
					+ openingPercentage + "'");
		}
	}

	public boolean getCurtainOpen() {
		return (openingPercentage > CARDIO2E_MIN_CURTAIN_OPENING_PERCENTAGE);
	}

	public void setCurtainOpen(boolean curtainOpen) {
		if (curtainOpen) {
			setOpeningPercentage(CARDIO2E_MAX_CURTAIN_OPENING_PERCENTAGE);
		} else {
			setOpeningPercentage(CARDIO2E_MIN_CURTAIN_OPENING_PERCENTAGE);
		}
	}

	public byte getClosingPercentage() {
		return (byte) (CARDIO2E_MAX_CURTAIN_OPENING_PERCENTAGE - openingPercentage);
	}

	public void setClosingPercentage(byte closingPercentage) {
		setOpeningPercentage((byte) (CARDIO2E_MAX_CURTAIN_OPENING_PERCENTAGE - closingPercentage));
	}

	public boolean getCurtainClosed() {
		return (openingPercentage == CARDIO2E_MIN_CURTAIN_OPENING_PERCENTAGE);
	}

	public void setCurtainClosed(boolean curtainClosed) {
		setCurtainOpen(!curtainClosed);
	}

	public boolean getLastMoveWasUp() {
		return lastMoveWasUp;
	}

	public boolean getLastMoveWasDown() {
		return lastMoveWasDown;
	}

	public boolean isDataVerified() { // Returns true if object data matches
										// with Cardio2e data specs and data is
										// congruent.
		boolean verified = false;
		if (super.isDataVerified()) {
			switch (getTransactionType()) {
			case ACK:
			case GET:
				verified = (getObjectNumber() != -1);
				break;
			case NACK:
				verified = (getErrorCode() != -1);
				break;
			case INFORMATION:
			case SET:
				verified = ((getObjectNumber() != -1) && (openingPercentage != -1));
				break;
			}
		}
		return verified;
	}

	public String toString() {
		String returnString = null;
		if (isDataVerified()) {
			switch (getTransactionType()) {
			case ACK:
			case GET:
				returnString = String.format("%s%s %s %d%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			case NACK:
				if (getObjectNumber() == -1) {
					returnString = String.format("%s%s %s %d%s",
							CARDIO2E_START_TRANSACTION_INITIATOR,
							getTransactionType().symbol,
							getObjectType().symbol, getErrorCode(),
							CARDIO2E_END_TRANSACTION_CHARACTER);
				} else {
					returnString = String.format("%s%s %s %d %d%s",
							CARDIO2E_START_TRANSACTION_INITIATOR,
							getTransactionType().symbol,
							getObjectType().symbol, getObjectNumber(),
							getErrorCode(), CARDIO2E_END_TRANSACTION_CHARACTER);
				}
				break;
			case INFORMATION:
			case SET:
				returnString = String.format("%s%s %s %d %d%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), openingPercentage,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			}
		}
		return returnString;
	}

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eCurtainTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eCurtainTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eCurtainTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
	}

	public boolean smartSendingCanReplaceLikeMe() {
		// This function has been replaced in this Cardio2eTransaction subclass
		// in order to return true.
		// When returns true, smart sending will be able to delete the
		// transactions like this from the send buffer and add a new
		// transaction that replaces them.
		return true;
	}
}
