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
 * Cardio2e Binding structured HVAC control transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eHvacControlTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 3140812809946223591L;
	public static boolean smartSendingEnabledClass = false; // Signals whether
															// the whole class
															// is smart sending
															// enabled.
	public Cardio2eHvacSystemModes singleHvacSystemMode = null;
	private Cardio2eHvacFanStates hvacFanState = null;
	private Cardio2eHvacSystemModes hvacSystemMode = null;
	private Cardio2eHvacSystemModes simplePowerOnNextHvacSystemMode = Cardio2eHvacSystemModes.AUTO;
	private double hvacHeatingSetPoint = -1.00;
	private double hvacCoolingSetPoint = -1.00;

	public Cardio2eHvacControlTransaction() {
		super.setObjectType(Cardio2eObjectTypes.HVAC_CONTROL);
	}

	public Cardio2eHvacControlTransaction(short objectNumber) { // Simple HVAC
																// control GET
																// constructor.
		setTransactionType(Cardio2eTransactionTypes.GET);
		super.setObjectType(Cardio2eObjectTypes.HVAC_CONTROL);
		setObjectNumber(objectNumber);
	}

	public Cardio2eHvacControlTransaction(short objectNumber,
			double hvacHeatingSetPoint, double hvacCoolingSetPoint,
			Cardio2eHvacFanStates hvacFanState,
			Cardio2eHvacSystemModes hvacSystemMode) { // Simple HVAC control SET
														// constructor.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.HVAC_CONTROL);
		setObjectNumber(objectNumber);
		setHvacHeatingSetPoint(hvacHeatingSetPoint);
		setHvacCoolingSetPoint(hvacCoolingSetPoint);
		setHvacFanState(hvacFanState);
		setHvacSystemMode(hvacSystemMode);
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

	public Cardio2eHvacFanStates getHvacFanState() {
		return hvacFanState;
	}

	public void setHvacFanState(Cardio2eHvacFanStates hvacFanState) {
		isDataComplete = false;
		this.hvacFanState = hvacFanState;
	}

	public boolean getHvacFanRunning() {
		return (hvacFanState == Cardio2eHvacFanStates.RUNNING);
	}

	public void setHvacFanRunning(boolean hvacFanRunning) {
		if (hvacFanRunning) {
			setHvacFanState(Cardio2eHvacFanStates.RUNNING);
		} else {
			setHvacFanState(Cardio2eHvacFanStates.STOP);
		}
	}

	public Cardio2eHvacSystemModes getHvacSystemMode() {
		return hvacSystemMode;
	}

	public void setHvacSystemMode(Cardio2eHvacSystemModes hvacSystemMode) {
		isDataComplete = false;
		this.hvacSystemMode = hvacSystemMode;
		if (hvacSystemMode != Cardio2eHvacSystemModes.OFF)
			simplePowerOnNextHvacSystemMode = hvacSystemMode;
	}

	public double getHvacHeatingSetPoint() {
		return hvacHeatingSetPoint;
	}

	public void setHvacHeatingSetPoint(double hvacHeatingSetPoint) {
		isDataComplete = false;
		double min = CARDIO2E_MIN_HVAC_SET_POINT;
		double max = CARDIO2E_MAX_HVAC_SET_POINT;
		// Prevents exceeding limits with INFORMATION transactions when ECONOMY
		// mode is active
		if (getTransactionType() == Cardio2eTransactionTypes.INFORMATION) {
			min = min - CARDIO2E_HVAC_SET_POINT_ECONOMY_OFFSET;
			max = max + CARDIO2E_HVAC_SET_POINT_ECONOMY_OFFSET;
		}
		if ((hvacHeatingSetPoint >= min) && (hvacHeatingSetPoint <= max)) {
			this.hvacHeatingSetPoint = hvacHeatingSetPoint;
		} else {
			throw new IllegalArgumentException("invalid hvacHeatingSetPoint '"
					+ hvacHeatingSetPoint + "'");
		}
	}

	public double getHvacCoolingSetPoint() {
		return hvacCoolingSetPoint;
	}

	public void setHvacCoolingSetPoint(double hvacCoolingSetPoint) {
		isDataComplete = false;
		double min = CARDIO2E_MIN_HVAC_SET_POINT;
		double max = CARDIO2E_MAX_HVAC_SET_POINT;
		// Prevents exceeding limits with INFORMATION transactions when ECONOMY
		// mode is active
		if (getTransactionType() == Cardio2eTransactionTypes.INFORMATION) {
			min = min - CARDIO2E_HVAC_SET_POINT_ECONOMY_OFFSET;
			max = max + CARDIO2E_HVAC_SET_POINT_ECONOMY_OFFSET;
		}
		if ((hvacCoolingSetPoint >= min) && (hvacCoolingSetPoint <= max)) {
			this.hvacCoolingSetPoint = hvacCoolingSetPoint;
		} else {
			throw new IllegalArgumentException("invalid hvacCoolingSetPoint '"
					+ hvacCoolingSetPoint + "'");
		}
	}

	public boolean getSingleHvacSystemModeOn() {
		boolean singleHvacSystemModeOn = false;
		if (singleHvacSystemMode != null) {
			if (hvacSystemMode != null) {
				if (singleHvacSystemMode == Cardio2eHvacSystemModes.OFF) {
					singleHvacSystemModeOn = getHvacFanRunning();
				} else {
					singleHvacSystemModeOn = (hvacSystemMode == singleHvacSystemMode);
				}
			}
		} else {
			throw new IllegalArgumentException(
					"To use getSingleHvacSystemModeOn(), singleHvacSystemMode cannot be null");
		}
		return singleHvacSystemModeOn;
	}

	public void setSingleHvacSystemModeOn(boolean singleHvacSystemModeOn) {
		if (singleHvacSystemMode != null) {
			if (singleHvacSystemMode == Cardio2eHvacSystemModes.OFF) {
				setHvacFanRunning(singleHvacSystemModeOn);
			} else {
				setHvacSystemMode((singleHvacSystemModeOn) ? singleHvacSystemMode
						: Cardio2eHvacSystemModes.OFF);
			}
		} else {
			throw new IllegalArgumentException(
					"To use setSingleHvacSystemModeOn(), singleHvacSystemMode cannot be null");
		}
	}

	public double getSingleHvacSetPoint() {
		double singleSetPoint = 0.00;
		if (singleHvacSystemMode != null) {
			if (hvacSystemMode != null) {
				switch (singleHvacSystemMode) {
				case COOLING:
					singleSetPoint = hvacCoolingSetPoint;
					break;
				case HEATING:
					singleSetPoint = hvacHeatingSetPoint;
					break;
				default:
					throw new IllegalArgumentException(
							"Cannot determine which setpoint option (heating or cooling) is suitable for '"
									+ singleHvacSystemMode
									+ "' simpleControlHvacSystemMode");
				}
			}
		} else {
			throw new IllegalArgumentException(
					"To use getSingleHvacSetPoint(), singleHvacSystemMode cannot be null");
		}
		return singleSetPoint;
	}

	public void setSingleHvacSetPoint(double singleSetPoint) {
		if (singleHvacSystemMode != null) {
			switch (singleHvacSystemMode) {
			case COOLING:
				setHvacCoolingSetPoint(singleSetPoint);
				break;
			case HEATING:
				setHvacHeatingSetPoint(singleSetPoint);
				break;
			default:
				throw new IllegalArgumentException(
						"Cannot determine which setpoint option (heating or cooling) is suitable for '"
								+ singleHvacSystemMode
								+ "' simpleControlHvacSystemMode");
			}
		} else {
			throw new IllegalArgumentException(
					"To use getSingleHvacSetPoint(), singleHvacSystemMode cannot be null");
		}
	}

	public boolean isPoweredOn() {
		boolean poweredOn = false;
		if (hvacSystemMode != null) {
			if (hvacSystemMode != Cardio2eHvacSystemModes.OFF)
				poweredOn = true;
		} else {
			throw new IllegalArgumentException(
					"Cannot determine if HVAC system is powered on, because hvacSystemMode is null");
		}
		return poweredOn;
	}

	public void simplePowerOn(boolean powerOn) {
		setHvacSystemMode((powerOn) ? simplePowerOnNextHvacSystemMode
				: Cardio2eHvacSystemModes.OFF);
	}

	public int getKnxDpt20_105() {
		int dpt20_105Mode = 0;
		if (hvacSystemMode != null) {
			switch (hvacSystemMode) {
			case AUTO:
				dpt20_105Mode = 0;
				break;
			case HEATING:
				dpt20_105Mode = 1;
				break;
			case COOLING:
				dpt20_105Mode = 3;
				break;
			case OFF:
				dpt20_105Mode = 6;
				break;
			case NORMAL:
				dpt20_105Mode = 254; // No KNX DPT 20.105 compliant value
				break;
			case ECONOMY:
				dpt20_105Mode = 255; // No KNX DPT 20.105 compliant value
				break;
			default:
				throw new IllegalArgumentException(
						"Cannot parse '"
								+ hvacSystemMode
								+ "' Cardio 2é HVAC System Mode to KNX DPT 20.105 value");
			}
		} else {
			throw new IllegalArgumentException(
					"Cannot parse null hvacSystemMode to KNX DPT 20.105");
		}
		return dpt20_105Mode;
	}

	public void setKnxDpt20_105(int dpt20_105) {
		switch (dpt20_105) {
		case 0:
			setHvacSystemMode(Cardio2eHvacSystemModes.AUTO);
			break;
		case 1:
			setHvacSystemMode(Cardio2eHvacSystemModes.HEATING);
			break;
		case 3:
			setHvacSystemMode(Cardio2eHvacSystemModes.COOLING);
			break;
		case 6:
			setHvacSystemMode(Cardio2eHvacSystemModes.OFF);
			break;
		case 254: // No KNX DPT 20.105 compliant value
			setHvacSystemMode(Cardio2eHvacSystemModes.NORMAL);
			break;
		case 255: // No KNX DPT 20.105 compliant value
			setHvacSystemMode(Cardio2eHvacSystemModes.ECONOMY);
			break;
		default:
			throw new IllegalArgumentException("Cannot parse KNX DPT 20.105 '"
					+ dpt20_105 + "' value to a Cardio 2é HVAC System Mode");
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
				verified = ((getObjectNumber() != -1)
						&& (hvacHeatingSetPoint != -1.00)
						&& (hvacCoolingSetPoint != -1.00)
						&& (hvacFanState != null) && (hvacSystemMode != null));
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
				returnString = String.format("%s%s %s %d %.2f %.2f %s %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), hvacHeatingSetPoint,
						hvacCoolingSetPoint, hvacFanState.symbol,
						hvacSystemMode.symbol,
						CARDIO2E_END_TRANSACTION_CHARACTER);
			case SET:
				returnString = String.format("%s%s %s %d %.0f %.0f %s %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getObjectNumber(), hvacHeatingSetPoint,
						hvacCoolingSetPoint, hvacFanState.symbol,
						hvacSystemMode.symbol,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			}
		}
		return returnString;
	}

	public static void setSmartSendingEnabledClass(boolean enabled) {
		Cardio2eHvacControlTransaction.smartSendingEnabledClass = enabled;
	}

	public static boolean getSmartSendingEnabledClass() {
		// This function replaces superclass Cardio2eTransaction
		// getSmartSendingEnabledClass() function.
		return Cardio2eHvacControlTransaction.smartSendingEnabledClass;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// This function replaces superclass Cardio2eTransaction
		// isSmartSendingEnabled() function.
		return (Cardio2eHvacControlTransaction.getSmartSendingEnabledClass() || smartSendingEnabledTransaction);
	}
}
