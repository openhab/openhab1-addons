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
 * Cardio2e Binding structured HVAC temperature transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eHvacTemperatureTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 6789216588228925388L;
	private double hvacTemperature;
	private Cardio2eHvacSystemModes hvacSystemMode = null;

	public Cardio2eHvacTemperatureTransaction() {
		super.setObjectType(Cardio2eObjectTypes.HVAC_TEMPERATURE);
	}

	public Cardio2eHvacTemperatureTransaction(short objectNumber) { // Simple
																	// HVAC
																	// temperature
																	// GET
																	// constructor..
		setTransactionType(Cardio2eTransactionTypes.GET);
		super.setObjectType(Cardio2eObjectTypes.HVAC_TEMPERATURE);
		setObjectNumber(objectNumber);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber <= CARDIO2E_MAX_HVAC_ZONE_NUMBER) {
			super.setObjectNumber(objectNumber);
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public double getHvacTemperature() {
		return hvacTemperature;
	}

	public void setHvacTemperature(double hvacTemperature) {
		isDataComplete = false;
		this.hvacTemperature = hvacTemperature;
	}

	public Cardio2eHvacSystemModes getHvacSystemMode() {
		return hvacSystemMode;
	}

	public void setHvacSystemMode(Cardio2eHvacSystemModes hvacSystemMode) {
		isDataComplete = false;
		this.hvacSystemMode = hvacSystemMode;
	}

	@SuppressWarnings("incomplete-switch")
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
				verified = ((getObjectNumber() != -1) && (hvacSystemMode != null));
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
				returnString = String.format("%s%s %s %d %.2f %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), hvacTemperature,
						hvacSystemMode.symbol,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			}
		}
		return returnString;
	}

	public boolean isSmartSendingEnabled() { // Always returns false because
												// this class do not support
												// smartSending.
		return false;
	}
}
