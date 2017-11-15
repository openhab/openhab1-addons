/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Cardio2e Binding structured transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eTransaction implements Cloneable, Serializable {
	private static final long serialVersionUID = 1108810793151175547L;
	public static final char CARDIO2E_START_TRANSACTION_INITIATOR = '@';
	public static final char CARDIO2E_END_TRANSACTION_CHARACTER = '\r';
	public static final short CARDIO2E_MIN_OBJECT_NUMBER = 1;
	public static final short CARDIO2E_MAX_OBJECT_NUMBER = 160;
	public static final short CARDIO2E_MAX_LIGHT_NUMBER = 160;
	public static final byte CARDIO2E_MIN_LIGHTING_INTENSITY = 0;
	public static final byte CARDIO2E_MAX_LIGHTING_INTENSITY = 100;
	public static final byte CARDIO2E_MAX_RELAY_NUMBER = 40;
	public static final byte CARDIO2E_MAX_SCENARIO_NUMBER = 42;
	public static final byte CARDIO2E_MAX_SECURITY_ZONE_NUMBER = 16;
	public static final byte CARDIO2E_MAX_SECURITY_CODE_LENGTH = 6;
	public static final byte CARDIO2E_MAX_PROGRAM_CODE_LENGTH = 6;
	public static final short CARDIO2E_MAX_HVAC_ZONE_NUMBER = 5;
	public static final double CARDIO2E_MIN_HVAC_SET_POINT = 5.00;
	public static final double CARDIO2E_MAX_HVAC_SET_POINT = 35.00;
	public static final double CARDIO2E_HVAC_SET_POINT_ECONOMY_OFFSET = 2.00;
	public static final short CARDIO2E_MAX_CURTAIN_NUMBER = 80;
	public static final byte CARDIO2E_MIN_CURTAIN_OPENING_PERCENTAGE = 0;
	public static final byte CARDIO2E_MAX_CURTAIN_OPENING_PERCENTAGE = 100;
	public String primitiveStringTransaction = null; // In this variable can
														// store a primitive
														// sent / received
														// string transaction.
	public boolean isDataComplete = false; // A decoder object will set
											// isDataComplete = true when decode
											// process is successfully complete.
											// Auto set to false on any object
											// data set.
	public boolean mustBeSent = false; // Signals whether this transaction is
										// mandatory to be sent.
	public boolean smartSendingEnabledTransaction = false; // Signals whether
															// the transaction
															// is smart sending
															// enabled.
	public boolean smartSendingEnqueueFirst = false; // Signals whether the
														// transaction will be
														// enqueued as the first
														// element of the send
														// FIFO buffer (absolute
														// priority).
	public boolean receiptACK = false; // Signals whether this transaction was
										// acknowledge by Cardio 2é (ACK or NACK
										// was received)
	public byte sendingTries = 0; // Number of attempts to send this
									// transaction
	private Cardio2eTransactionTypes transactionType;
	private Cardio2eObjectTypes objectType;
	private short objectNumber = -1; // -1=Not set, for no object number
										// transaction encoding.
	private int errorCode = -1; // -1=Not set (no error).

	public Cardio2eTransaction() {
	}

	public Cardio2eTransaction clone() {
		try {
			return (Cardio2eTransaction) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public Cardio2eTransaction deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Cardio2eTransaction) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public String toString() { // Method toString will be used to encode object
								// data to Cardio2e stream string type
								// transaction. In this super class, no complete
								// data will be available, so toString will
								// returns null.
		return null;
	}

	public Cardio2eTransactionTypes getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Cardio2eTransactionTypes transactionType) {
		isDataComplete = false;
		this.transactionType = transactionType;
	}

	public Cardio2eObjectTypes getObjectType() {
		return objectType;
	}

	protected void setObjectType(Cardio2eObjectTypes objectType) {
		isDataComplete = false;
		this.objectType = objectType;
	}

	public short getObjectNumber() {
		return objectNumber;
	}

	public void setObjectNumber(short objectNumber) {
		isDataComplete = false;
		if ((objectNumber >= CARDIO2E_MIN_OBJECT_NUMBER)
				&& (objectNumber <= CARDIO2E_MAX_OBJECT_NUMBER)) {
			this.objectNumber = objectNumber;
		} else {
			throw new IllegalArgumentException("invalid objectNumber '"
					+ objectNumber + "'");
		}
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		isDataComplete = false;
		this.errorCode = errorCode;
	}

	public String getErrorCodeDescription() {
		String description = "Unknown error or no error";
		switch (errorCode) {
		case 1:
			description = "Object type specified by the transaction is not recognized";
			break;
		case 2:
			description = "Object number is out of range for the object type specified";
			break;
		case 3:
			description = "One or more parameters are not valid";
			break;
		case 4:
			description = "Security code is not valid";
			break;
		case 5:
			description = "Transaction S (Set) not supported for the requested type of object";
		case 6:
			description = "Transaction G (Get) not supported for the requested type of object";
			break;
		case 7:
			description = "Transaction is refused because security is armed";
			break;
		case 8:
			description = "This zone cannot be ignored";
			break;
		case 16:
			description = "Security cannot be armed because there are open zones";
			break;
		case 17:
			description = "Security cannot be armed because there is a power problem";
			break;
		case 18:
			description = "Security cannot be armed for an unknown reason";
			break;
		}
		return description;
	}

	public boolean isDataVerified() { // Returns true if object data matches
										// with Cardio2e data specs and data is
										// congruent.
		return ((transactionType != null) && (objectType != null));
	}

	public boolean isLike(Cardio2eTransaction transaction) { // Returns true if
																// "transaction"
																// matches
																// ObjectType
																// and
																// ObjectNumber
																// (same Cardio
																// 2é system
																// object).
		Boolean isLikeMe = false;
		Cardio2eObjectTypes myObjectType = getObjectType();
		if (transaction.getObjectType() == myObjectType) {
			if ((myObjectType == Cardio2eObjectTypes.ZONES)
					|| (myObjectType == Cardio2eObjectTypes.ZONES_BYPASS)
					|| (myObjectType == Cardio2eObjectTypes.DATE_AND_TIME)
					|| (myObjectType == Cardio2eObjectTypes.LOGIN)
					|| (myObjectType == Cardio2eObjectTypes.VERSION)) {
				isLikeMe = true;
			} else {
				isLikeMe = (transaction.getObjectNumber() == getObjectNumber());
			}
		}
		return isLikeMe;
	}

	public static boolean getSmartSendingEnabledClass() {
		// By default returns false.
		// This function has been replaced in SmartSending enabled subclasses in
		// order to signal when it is enabled.
		return false;
	}

	public boolean isSmartSendingEnabled() { // Returns true if this transaction
												// or its class is smart sending
												// enabled.
		// By default returns false.
		return false;
	}

	public boolean smartSendingCanReplaceLikeMe() {
		// By default returns false.
		// This function has been replaced in Cardio2eLightingTransaction,
		// Cardio2eRelayTransaction and Cardio2eCurtainTransaction subclasses in
		// order to return true.
		// When returns true, smart sending will be able to delete the
		// transactions like this from the send buffer and add a new transaction
		// that replaces them.
		return false;
	}
}
