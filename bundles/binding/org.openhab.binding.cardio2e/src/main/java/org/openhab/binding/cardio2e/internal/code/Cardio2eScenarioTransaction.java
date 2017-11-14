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
 * Cardio2e Binding structured scenario transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eScenarioTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = -2305337044178383893L;
	private static boolean smartSendingEnabledClass = false; // Signals whether
																// the whole
																// class is
																// smart sending
																// enabled.
	private String securityCode = null;

	public Cardio2eScenarioTransaction() {
		super.setObjectType(Cardio2eObjectTypes.SCENARIO);
	}

	public Cardio2eScenarioTransaction(short objectNumber) { // Simple SET
																// (execute)
																// scenario
																// transaction
																// constructor.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.SCENARIO);
		setObjectNumber(objectNumber);
	}

	public Cardio2eScenarioTransaction(short objectNumber, String securityCode) { // Simple
																					// SET
																					// (execute)
																					// scenario
																					// with
																					// security
																					// code
																					// transaction
																					// constructor.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.SCENARIO);
		setObjectNumber(objectNumber);
		setSecurityCode(securityCode);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber <= CARDIO2E_MAX_SCENARIO_NUMBER) {
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

	@SuppressWarnings("incomplete-switch")
	public boolean isDataVerified() { // Returns true if object data matches
										// with Cardio2e data specs and data is
										// congruent.
		boolean verified = false;
		if (super.isDataVerified()) {
			switch (getTransactionType()) {
			case ACK:
			case SET:
				verified = (getObjectNumber() != -1);
				break;
			case NACK:
				verified = (getErrorCode() != -1);
				break;
			}
		}
		return verified;
	}

	@SuppressWarnings("incomplete-switch")
	public String toString() {
		String returnString = null;
		if (isDataVerified()) {
			switch (getTransactionType()) {
			case ACK:
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
			case SET:
				if (securityCode == null) {
					returnString = String.format("%s%s %s %d%s",
							CARDIO2E_START_TRANSACTION_INITIATOR,
							getTransactionType().symbol,
							getObjectType().symbol, getObjectNumber(),
							CARDIO2E_END_TRANSACTION_CHARACTER);
				} else {
					returnString = String.format("%s%s %s %d %s%s",
							CARDIO2E_START_TRANSACTION_INITIATOR,
							getTransactionType().symbol,
							getObjectType().symbol, getObjectNumber(),
							securityCode, CARDIO2E_END_TRANSACTION_CHARACTER);
				}
				break;
			}
		}
		return returnString;
	}

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eScenarioTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eScenarioTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eScenarioTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
	}
}
