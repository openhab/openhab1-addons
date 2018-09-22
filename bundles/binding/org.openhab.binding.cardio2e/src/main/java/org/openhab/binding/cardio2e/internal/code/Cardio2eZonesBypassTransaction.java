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
 * Cardio2e Binding structured zones bypass transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eZonesBypassTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 6741635170384079476L;
	private static boolean smartSendingEnabledClass = false; // Signals whether
																// the whole
																// class is
																// smart sending
																// enabled.
	public boolean invertZoneBypassBooleanStates = false;
	private short maxStatesLenght = 0;
	private Cardio2eZoneBypassStates zoneBypassStates[] = null;

	public Cardio2eZonesBypassTransaction() {
		super.setObjectType(Cardio2eObjectTypes.ZONES_BYPASS);
	}

	public Cardio2eZonesBypassTransaction(short objectNumber) { // Simple zone
																// bypass GET
																// transaction
																// constructor.
		setTransactionType(Cardio2eTransactionTypes.GET);
		super.setObjectType(Cardio2eObjectTypes.ZONES_BYPASS);
		setObjectNumber(objectNumber);
	}

	public Cardio2eZonesBypassTransaction(short objectNumber,
			Cardio2eZoneBypassStates zoneBypassStates[]) { // Simple SET
															// transactionType
															// constructor for
															// zone bypass.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.ZONES_BYPASS);
		setObjectNumber(objectNumber);
		setZoneBypassStates(zoneBypassStates);
	}

	public Cardio2eZonesBypassTransaction(short objectNumber,
			boolean zoneBypassStates[]) { // Simple SET transactionType
											// constructor for zone bypass.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.ZONES_BYPASS);
		setObjectNumber(objectNumber);
		setZoneBypassBooleanStates(zoneBypassStates);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if (objectNumber <= CARDIO2E_MAX_SECURITY_ZONE_NUMBER) {
			super.setObjectNumber(objectNumber);
			maxStatesLenght = (short) (1 + (CARDIO2E_MAX_SECURITY_ZONE_NUMBER - objectNumber));
			zoneBypassStates = null; // objectNumber change must destroy
										// zoneBypassStates array, because
										// zoneStates array length is relative
										// to objectNumber
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public short getMaxStatesLenght() {
		return maxStatesLenght;
	}

	public Cardio2eZoneBypassStates[] getZoneBypassStates() {
		return zoneBypassStates;
	}

	public boolean[] getZoneBypassBooleanStates() {
		boolean parsedZoneBypassStates[] = null;
		if (zoneBypassStates != null) {
			int length = zoneBypassStates.length;
			if ((length >= 1) && (length <= maxStatesLenght)) {
				parsedZoneBypassStates = new boolean[zoneBypassStates.length];
				for (int i = 0; i < length; i++) {
					if (zoneBypassStates[i] != null) {
						if (zoneBypassStates[i] == Cardio2eZoneBypassStates.BYPASSED) {
							parsedZoneBypassStates[i] = !invertZoneBypassBooleanStates;
						} else if (zoneBypassStates[i] == Cardio2eZoneBypassStates.NOT_BYPASSED) {
							parsedZoneBypassStates[i] = invertZoneBypassBooleanStates;
						} else {
							throw new IllegalArgumentException(
									"invalid zoneBypassState value at position "
											+ i);
						}
					} else {
						throw new IllegalArgumentException(
								"null zoneBypassState value at position " + i);
					}
				}
			} else {
				throw new IllegalArgumentException(
						"invalid zoneBypassStates length '"
								+ zoneBypassStates.length
								+ "' or objectNumber not set");
			}
		} else {
			throw new IllegalArgumentException(
					"cannot parse null zoneBypassStates values");
		}
		return parsedZoneBypassStates;
	}

	public void setZoneBypassStates(Cardio2eZoneBypassStates zoneBypassStates[]) {
		isDataComplete = false;
		if (zoneBypassStates != null) {
			if ((zoneBypassStates.length >= 1)
					&& (zoneBypassStates.length <= maxStatesLenght)) {
				this.zoneBypassStates = zoneBypassStates;
			} else {
				throw new IllegalArgumentException(
						"invalid zoneBypassStates length '"
								+ zoneBypassStates.length
								+ "' or objectNumber not set");
			}
		} else {
			throw new IllegalArgumentException(
					"zoneBypassStates cannot be null");
		}
	}

	public void setZoneBypassBooleanStates(boolean zoneBypassStates[]) {
		Cardio2eZoneBypassStates parsedZoneBypassStates[] = null;
		if (zoneBypassStates != null) {
			int length = zoneBypassStates.length;
			if ((length >= 1) && (length <= maxStatesLenght)) {
				parsedZoneBypassStates = new Cardio2eZoneBypassStates[zoneBypassStates.length];
				for (int i = 0; i < length; i++) {
					boolean bypassState = (invertZoneBypassBooleanStates) ? !zoneBypassStates[i]
							: zoneBypassStates[i];
					if (bypassState) {
						parsedZoneBypassStates[i] = Cardio2eZoneBypassStates.BYPASSED;
					} else {
						parsedZoneBypassStates[i] = Cardio2eZoneBypassStates.NOT_BYPASSED;
					}
				}
				setZoneBypassStates(parsedZoneBypassStates);
			} else {
				throw new IllegalArgumentException(
						"invalid zoneBypassStates length '"
								+ zoneBypassStates.length
								+ "' or objectNumber not set");
			}
		} else {
			throw new IllegalArgumentException(
					"zoneBypassStates cannot be null");
		}
	}

	public Cardio2eZoneBypassStates getZoneBypassState() {
		Cardio2eZoneBypassStates zoneBypassState = null;
		if (zoneBypassStates != null) {
			if ((getObjectNumber() >= CARDIO2E_MIN_OBJECT_NUMBER)
					&& (zoneBypassStates.length == 1)) {
				zoneBypassState = zoneBypassStates[0];
			} else {
				throw new IllegalArgumentException("invalid object data");
			}
		}
		return zoneBypassState;
	}

	public boolean getZoneBypassBooleanState() {
		boolean zoneBypassBooleanState;
		if (zoneBypassStates[0] != null) {
			if (zoneBypassStates[0] == Cardio2eZoneBypassStates.BYPASSED) {
				zoneBypassBooleanState = !invertZoneBypassBooleanStates;
			} else if (zoneBypassStates[0] == Cardio2eZoneBypassStates.NOT_BYPASSED) {
				zoneBypassBooleanState = invertZoneBypassBooleanStates;
			} else {
				throw new IllegalArgumentException(
						"invalid zoneBypassState value");
			}
		} else {
			throw new IllegalArgumentException(
					"cannot parse null zoneBypassState value");
		}
		return zoneBypassBooleanState;
	}

	public void setZoneBypassState(Cardio2eZoneBypassStates zoneBypassState) {
		Cardio2eZoneBypassStates zoneBypassStateAsArray[];
		if (zoneBypassState != null) {
			if (getObjectNumber() >= CARDIO2E_MIN_OBJECT_NUMBER) {
				zoneBypassStateAsArray = new Cardio2eZoneBypassStates[1];
				zoneBypassStateAsArray[0] = zoneBypassState;
				setZoneBypassStates(zoneBypassStateAsArray);
			} else {
				throw new IllegalArgumentException(
						"cannot set zoneState because objectNumber is not properly defined");
			}
		} else {
			throw new IllegalArgumentException("zoneBypassState cannot be null");
		}
	}

	public void setZoneBypassBooleanState(boolean zoneBypassState) {
		if (zoneBypassState) {
			setZoneBypassState((invertZoneBypassBooleanStates) ? Cardio2eZoneBypassStates.NOT_BYPASSED
					: Cardio2eZoneBypassStates.BYPASSED);
		} else {
			setZoneBypassState((invertZoneBypassBooleanStates) ? Cardio2eZoneBypassStates.BYPASSED
					: Cardio2eZoneBypassStates.NOT_BYPASSED);
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
				if ((getObjectNumber() != -1) && (zoneBypassStates != null)) {
					verified = true; // Verified will be true, excepts null
										// value in array is found.
					for (Cardio2eZoneBypassStates s : zoneBypassStates) {
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
				returnString = "";
				for (Cardio2eZoneBypassStates s : zoneBypassStates) {
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

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eZonesBypassTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eZonesBypassTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eZonesBypassTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
	}
}
