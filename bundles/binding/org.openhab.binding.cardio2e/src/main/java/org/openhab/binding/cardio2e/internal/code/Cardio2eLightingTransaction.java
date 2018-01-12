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
 * Cardio2e Binding structured lighting transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eLightingTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = -2160039035118499037L;
	private static boolean smartSendingEnabledClass = false; // Signals whether
																// the whole
																// class is
																// smart sending
																// enabled.
	private byte lightIntensity = -1; // -1=Not set, signaling lightIntensity
										// not set.
	public boolean lightIntensityCorrection = false; // If true,
														// getLightIntensity()
														// returns 0 when
														// lightIntensity = 1
														// (Cardio 2é DM1
														// modules consider
														// light is off for
														// lower values of 2,
														// because system sets
														// DM1 modules light
														// intensity value to 1%
														// after auto lighting
														// by presence ends).

	public Cardio2eLightingTransaction() {
		super.setObjectType(Cardio2eObjectTypes.LIGHTING);
	}

	public Cardio2eLightingTransaction(short objectNumber) { // Simple lighting
																// GET
																// transaction
																// constructor.
		setTransactionType(Cardio2eTransactionTypes.GET);
		super.setObjectType(Cardio2eObjectTypes.LIGHTING);
		setObjectNumber(objectNumber);
	}

	public Cardio2eLightingTransaction(short objectNumber, byte lightIntensity) { // Simple
																					// SET
																					// transactionType
																					// constructor
																					// for
																					// lighting
																					// intensity
																					// control.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.LIGHTING);
		setObjectNumber(objectNumber);
		setLightIntensity(lightIntensity);
	}

	public Cardio2eLightingTransaction(short objectNumber, boolean lightPowered) { // Simple
																					// SET
																					// transactionType
																					// constructor
																					// for
																					// lighting
																					// power
																					// control.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.LIGHTING);
		setObjectNumber(objectNumber);
		setLightPowered(lightPowered);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber <= CARDIO2E_MAX_LIGHT_NUMBER) {
			super.setObjectNumber(objectNumber);
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public byte getLightIntensity() {
		return ((lightIntensityCorrection) && (lightIntensity == 1)) ? 0
				: lightIntensity;
	}

	public void setLightIntensity(byte lightIntensity) {
		isDataComplete = false;
		if ((lightIntensity >= CARDIO2E_MIN_LIGHTING_INTENSITY)
				&& (lightIntensity <= CARDIO2E_MAX_LIGHTING_INTENSITY)) {
			this.lightIntensity = lightIntensity;
		} else {
			throw new IllegalArgumentException("invalid lightIntensity '"
					+ lightIntensity + "'");
		}
	}

	public boolean getLightPowered() {
		return (getLightIntensity() > CARDIO2E_MIN_LIGHTING_INTENSITY);
	}

	public void setLightPowered(boolean lightPowered) {
		if (lightPowered) {
			setLightIntensity(CARDIO2E_MAX_LIGHTING_INTENSITY);
		} else {
			setLightIntensity(CARDIO2E_MIN_LIGHTING_INTENSITY);
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
				verified = (getObjectNumber() != -1);
				break;
			case NACK:
				verified = (getErrorCode() != -1);
				break;
			case INFORMATION:
			case SET:
				verified = ((getObjectNumber() != -1) && (lightIntensity != -1));
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
						getObjectNumber(), lightIntensity,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			}
		}
		return returnString;
	}

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eLightingTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eLightingTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eLightingTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
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
