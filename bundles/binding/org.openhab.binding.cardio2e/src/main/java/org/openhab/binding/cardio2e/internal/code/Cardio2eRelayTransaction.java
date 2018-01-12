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
 * Cardio2e Binding structured relay transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eRelayTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 5793088723067605989L;
	private static boolean smartSendingEnabledClass = false; // Signals whether
																// the whole
																// class is
																// smart sending
																// enabled.
	private Cardio2eRelayStates relayState = null;
	private boolean blindUpMode = false;
	private boolean blindDownMode = false;

	public Cardio2eRelayTransaction() {
		super.setObjectType(Cardio2eObjectTypes.RELAY);
	}

	public Cardio2eRelayTransaction(short objectNumber) { // Simple relay GET
															// transaction
															// constructor.
		setTransactionType(Cardio2eTransactionTypes.GET);
		super.setObjectType(Cardio2eObjectTypes.RELAY);
		setObjectNumber(objectNumber);
	}

	public Cardio2eRelayTransaction(short objectNumber,
			Cardio2eRelayStates relayState) { // Simple SET transactionType
												// constructor for relay
												// control.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.RELAY);
		setObjectNumber(objectNumber);
		setRelayState(relayState);
	}

	public Cardio2eRelayTransaction(short objectNumber, boolean relayON) { // Simple
																			// SET
																			// transactionType
																			// constructor
																			// for
																			// simple
																			// relay
																			// control
																			// (on=opened)
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.RELAY);
		setObjectNumber(objectNumber);
		setRelayON(relayON);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber <= CARDIO2E_MAX_RELAY_NUMBER) {
			super.setObjectNumber(objectNumber);
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public Cardio2eRelayStates getRelayState() {
		return relayState;
	}

	public void setRelayState(Cardio2eRelayStates relayState) {
		isDataComplete = false;
		this.relayState = relayState;
	}

	public boolean getRelayON() {
		return (relayState == Cardio2eRelayStates.OPENED);
	}

	public void setRelayON(boolean relayON) {
		if (relayON) {
			setRelayState(Cardio2eRelayStates.OPENED);
		} else {
			setRelayState(Cardio2eRelayStates.CLOSED);
		}
	}

	public boolean getBlindUpMode() {
		return blindUpMode;
	}

	public void setBlindUpMode(boolean enableBlindUpMode) {
		if ((blindDownMode) && (enableBlindUpMode))
			blindDownMode = false;
		blindUpMode = enableBlindUpMode;
	}

	public boolean getBlindDownMode() {
		return blindDownMode;
	}

	public void setBlindDownMode(boolean enableBlindDownMode) {
		if ((blindUpMode) && (enableBlindDownMode))
			blindUpMode = false;
		blindDownMode = enableBlindDownMode;
	}

	public boolean getBlindMode() {
		return (blindUpMode || blindDownMode);
	}

	public void blindMoveUp() {
		if (blindUpMode) {
			setRelayON(true);
		} else {
			if (blindDownMode) {
				setRelayON(false);
			} else {
				throw new UnsupportedOperationException(
						"Blind move up not allowed for this relay");
			}
		}
	}

	public boolean blindIsMovingUp() {
		return (blindUpMode && getRelayON());
	}

	public void blindMoveDown() {
		if (blindDownMode) {
			setRelayON(true);
		} else {
			if (blindUpMode) {
				setRelayON(false);
			} else {
				throw new UnsupportedOperationException(
						"Blind move down not allowed for this relay");
			}
		}
	}

	public boolean blindIsMovingDown() {
		return (blindDownMode && getRelayON());
	}

	public void blindStop() {
		if ((blindUpMode) || (blindDownMode)) {
			setRelayON(false);
		} else {
			throw new UnsupportedOperationException(
					"Blind stop not allowed for this relay");
		}
	}

	public boolean blindIsStopped() {
		return (!getRelayON());
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
				verified = ((getObjectNumber() != -1) && (relayState != null));
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
				returnString = String.format("%s%s %s %d %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), relayState.symbol,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			}
		}
		return returnString;
	}

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eRelayTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eRelayTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eRelayTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
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