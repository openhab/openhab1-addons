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
 * Cardio2e Binding structured date and time transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eDateTimeTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 7460821351007261636L;
	private static boolean smartSendingEnabledClass = false; // Signals whether
																// the whole
																// class is
																// smart sending
																// enabled.
	private Cardio2eDateTime dateTime = null;

	public Cardio2eDateTimeTransaction() {
		super.setObjectType(Cardio2eObjectTypes.DATE_AND_TIME);
	}

	public Cardio2eDateTimeTransaction(Cardio2eTransactionTypes transactionType) { // Simple
																					// DateTime
																					// transactionType
																					// constructor.
																					// If
																					// transactionType
																					// =
																					// SET
																					// it
																					// auto
																					// stores
																					// DateTime
																					// from
																					// system
																					// clock.
		setTransactionType(transactionType);
		super.setObjectType(Cardio2eObjectTypes.DATE_AND_TIME);
		if (transactionType == Cardio2eTransactionTypes.SET) {
			dateTime = new Cardio2eDateTime(); // No Cardio2eDateTime parameters
												// -> it gets system clock date
												// and time.
		}
	}

	public Cardio2eDateTimeTransaction(Cardio2eDateTime dateTime) { // Simple
																	// DateTime
																	// constructor
																	// for SET
																	// DateTime.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.DATE_AND_TIME);
		setDateTime(dateTime);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public short getObjectNumber() { // ObjectNumber not applicable in this
										// subclass. Throws exception.
		throw new UnsupportedOperationException("getObjectNumber");
	}

	public void setObjectNumber(short objectNumber) { // ObjectNumber not
														// applicable in this
														// subclass. Throws
														// exception.
		throw new UnsupportedOperationException("setObjectNumber");
	}

	public Cardio2eDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(Cardio2eDateTime dateTime) {
		isDataComplete = false;
		this.dateTime = dateTime;
	}

	public boolean isDataVerified() { // Returns true if object data matches
										// with Cardio2e data specs and data is
										// congruent.
		boolean verified = false;
		if (super.isDataVerified()) {
			switch (getTransactionType()) {
			case ACK:
			case GET:
				verified = true;
				break;
			case NACK:
				verified = (getErrorCode() != -1);
				break;
			case INFORMATION:
			case SET:
				verified = (dateTime != null);
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
				returnString = String.format("%s%s %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			case NACK:
				returnString = String.format("%s%s %s %d%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getErrorCode(), CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			case INFORMATION:
			case SET:
				returnString = String
						.format("%s%s %s %s%s",
								CARDIO2E_START_TRANSACTION_INITIATOR,
								getTransactionType().symbol,
								getObjectType().symbol, dateTime.toString(),
								CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			}
		}
		return returnString;
	}

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eDateTimeTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eDateTimeTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eDateTimeTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
	}
}
