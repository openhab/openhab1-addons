/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.code;

import org.apache.commons.lang.StringUtils;

/**
 * Cardio2e Binding structured security transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eSecurityTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 2179515148738989284L;
	private static boolean smartSendingEnabledClass = false; // Signals whether
																// the whole
																// class is
																// smart sending
																// enabled.
	private Cardio2eSecurityStates securityState = null;
	private String securityCode = null;

	public Cardio2eSecurityTransaction() {
		super.setObjectType(Cardio2eObjectTypes.SECURITY);
	}

	public Cardio2eSecurityTransaction(Cardio2eTransactionTypes transactionType) { // Simple
																					// security
																					// transactionType
																					// constructor,
																					// suitable
																					// for
																					// simple
																					// GET
																					// transactions
																					// use.
		setTransactionType(transactionType);
		super.setObjectType(Cardio2eObjectTypes.SECURITY);
		setObjectNumber((byte) 1);
	}

	public Cardio2eSecurityTransaction(Cardio2eSecurityStates securityState,
			String securityCode) { // Simple SET security arm / disarm
									// transaction constructor.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.SECURITY);
		setObjectNumber((byte) 1);
		setSecurityState(securityState);
		setSecurityCode(securityCode);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber == 1) {
			super.setObjectNumber(objectNumber);
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		isDataComplete = false;
		if ((securityCode.length() <= CARDIO2E_MAX_SECURITY_CODE_LENGTH)
				&& StringUtils.isNumeric(securityCode)) {
			this.securityCode = securityCode;
		} else {
			throw new IllegalArgumentException("invalid securityCode '"
					+ securityCode + "'");
		}
	}

	public Cardio2eSecurityStates getSecurityState() {
		return securityState;
	}

	public void setSecurityState(Cardio2eSecurityStates securityState) {
		isDataComplete = false;
		this.securityState = securityState;
	}

	public boolean getSecurityArmed() {
		return (securityState == Cardio2eSecurityStates.ARMED);
	}

	public void setSecurityArmed(boolean securityArmed) {
		if (securityArmed) {
			setSecurityState(Cardio2eSecurityStates.ARMED);
		} else {
			setSecurityState(Cardio2eSecurityStates.DISARMED);
		}
	}

	public boolean isDataVerified() { // Returns true if object data matches
										// with Cardio2e data specs and data is
										// congruent.
		boolean verified = false;
		if (super.isDataVerified()) {
			switch (getTransactionType()) {
			case ACK:
			case GET:
				verified = (getObjectNumber() == 1);
				break;
			case NACK:
				verified = (getErrorCode() != -1);
				break;
			case INFORMATION:
				verified = ((getObjectNumber() == 1) && (securityState != null));
			case SET:
				verified = ((getObjectNumber() == 1) && (securityState != null) && (securityCode != null));
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
				returnString = String.format("%s%s %s %d %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), securityState.symbol,
						CARDIO2E_END_TRANSACTION_CHARACTER);
			case SET:
				returnString = String.format("%s%s %s %d %s %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), securityState.symbol, securityCode,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			}
		}
		return returnString;
	}

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eSecurityTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eSecurityTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eSecurityTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
	}
}
