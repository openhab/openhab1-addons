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
 * Cardio2e Binding structured zones transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eZonesTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = -7910172422528076632L;
	public boolean invertZoneDetection = false;
	private short maxStatesLenght = 0;
	private Cardio2eZonesMask zonesMask = null;
	private Cardio2eZoneStates zoneStates[] = null;

	public Cardio2eZonesTransaction() {
		super.setObjectType(Cardio2eObjectTypes.ZONES);
	}

	public Cardio2eZonesTransaction(short objectNumber) { // Simple zones GET
															// transaction
															// constructor.
		setTransactionType(Cardio2eTransactionTypes.GET);
		super.setObjectType(Cardio2eObjectTypes.ZONES);
		setObjectNumber(objectNumber);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber <= CARDIO2E_MAX_SECURITY_ZONE_NUMBER) {
			super.setObjectNumber(objectNumber);
			maxStatesLenght = (short) (1 + (CARDIO2E_MAX_SECURITY_ZONE_NUMBER - objectNumber));
			zoneStates = null; // objectNumber change must destroy zoneStates
								// array, because zoneStates array length is
								// relative to objectNumber
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public short getMaxStatesLenght() {
		return maxStatesLenght;
	}

	public Cardio2eZonesMask getZonesMask() {
		return zonesMask;
	}

	public void setZonesMask(Cardio2eZonesMask zonesMask) {
		this.zonesMask = zonesMask;
	}

	public Cardio2eZoneStates[] getZoneStates() {
		return zoneStates;
	}

	public void setZoneStates(Cardio2eZoneStates zoneStates[]) {
		isDataComplete = false;
		if (zoneStates != null) {
			if ((zoneStates.length >= 1)
					&& (zoneStates.length <= maxStatesLenght)) {
				this.zoneStates = zoneStates;
			} else {
				throw new IllegalArgumentException(
						"invalid zoneStates length '" + zoneStates.length
								+ "' or objectNumber not set");
			}
		} else {
			throw new IllegalArgumentException("zoneStates cannot be null");
		}
	}

	public Cardio2eZoneStates getZoneState() {
		Cardio2eZoneStates zoneState = null;
		if (zoneStates != null) {
			if ((getObjectNumber() >= CARDIO2E_MIN_OBJECT_NUMBER)
					&& (zoneStates.length == 1)) {
				zoneState = zoneStates[0];
			} else {
				throw new IllegalArgumentException("invalid object data");
			}
		}
		return zoneState;
	}

	public void setZoneState(Cardio2eZoneStates zoneState) {
		Cardio2eZoneStates zoneStateAsArray[];
		if (zoneState != null) {
			if (getObjectNumber() >= CARDIO2E_MIN_OBJECT_NUMBER) {
				zoneStateAsArray = new Cardio2eZoneStates[1];
				zoneStateAsArray[0] = zoneState;
				setZoneStates(zoneStateAsArray);
			} else {
				throw new IllegalArgumentException(
						"cannot set zoneState because objectNumber is not properly defined");
			}
		} else {
			throw new IllegalArgumentException("zoneState cannot be null");
		}
	}

	public boolean[] getZoneDetections() { // Returns boolean array with zones
											// detection data based on on
											// zoneStates[] && zonesMask[]
											// matches.
		short objectNumber = getObjectNumber();
		if ((objectNumber >= CARDIO2E_MIN_OBJECT_NUMBER)
				&& (zoneStates != null) && (zonesMask != null)) {
			int length = zoneStates.length;
			if (length > 0) {
				Cardio2eZoneStates zonesMask[] = this.zonesMask.getZonesMask();
				boolean zoneDetections[] = new boolean[length];
				int zoneNumberOffset = (objectNumber - 1);
				for (byte i = 0; i < length; i++) {
					if (invertZoneDetection) {
						zoneDetections[i] = (zoneStates[i] == zonesMask[i
								+ zoneNumberOffset]);
					} else {
						zoneDetections[i] = (zoneStates[i] != zonesMask[i
								+ zoneNumberOffset]);
					}
				}
				return zoneDetections;
			} else {
				throw new IllegalArgumentException("zoneStates is empty");
			}
		} else {
			throw new IllegalArgumentException(
					"invalid data (check objectNumber / zoneStates / zonesMask)");
		}
	}

	public boolean getZoneDetection() { // Returns boolean value with
										// objectNumber zone detection data
										// based on based on zoneStates[] &&
										// zonesMask[] matches.
		return getZoneDetection(getObjectNumber());
	}

	public boolean getZoneDetection(short zoneNumber) { // Returns boolean value
														// with specified
														// zoneNumber zone
														// detection data based
														// on zoneStates[] &&
														// zonesMask[] matches.
		if ((zoneStates != null) && (zonesMask != null)) {
			boolean zoneDetection = false;
			short objectNumber = getObjectNumber();
			if ((zoneNumber >= CARDIO2E_MIN_OBJECT_NUMBER)) {
				if ((zoneNumber >= objectNumber)
						&& (zoneNumber < (objectNumber + zoneStates.length))) { // Checks
																				// zone
																				// range
					zoneDetection = (zoneStates[(zoneNumber - objectNumber)] != zonesMask
							.getZonesMask()[(zoneNumber - 1)]);
					return (invertZoneDetection) ? !zoneDetection
							: zoneDetection;
				} else {
					throw new IllegalArgumentException("zoneNumber '"
							+ objectNumber + "' is out of range");
				}
			} else {
				throw new IllegalArgumentException("invalid objectNumber '"
						+ objectNumber + "'");
			}
		} else {
			throw new IllegalArgumentException(
					"invalid zoneStates or zonesMask");
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
			case GET:
				verified = (getObjectNumber() != -1);
				break;
			case NACK:
				verified = (getErrorCode() != -1);
				break;
			case INFORMATION:
				if ((getObjectNumber() != -1) && (zoneStates != null)) {
					verified = true; // Verified will be true, excepts null
										// value in array is found.
					for (Cardio2eZoneStates s : zoneStates) {
						if (s == null) {
							verified = false;
						}
					}
				}
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
				returnString = "";
				for (Cardio2eZoneStates s : zoneStates) {
					returnString = returnString + s.symbol;
				}
				returnString = String.format("%s%s %s %d %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), returnString,
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
