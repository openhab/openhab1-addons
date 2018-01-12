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
 * Cardio2e Binding structured Version transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eVersionTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = -6011325654448157237L;
	private Cardio2eVersionTypes versionType = null;
	private String version = null;

	public Cardio2eVersionTransaction() {
		super.setObjectType(Cardio2eObjectTypes.VERSION);
	}

	public void setObjectType(Cardio2eObjectTypes objectType) {
		throw new UnsupportedOperationException("setObjectType");
	}

	public short getObjectNumber() {
		throw new UnsupportedOperationException("setObjectNumber");
	}

	public void setObjectNumber(short objectNumber) {
		throw new UnsupportedOperationException("setObjectNumber");
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		isDataComplete = false;
		this.version = version;
	}

	public Cardio2eVersionTypes getVersionType() {
		return versionType;
	}

	public void setVersionType(Cardio2eVersionTypes versionType) {
		isDataComplete = false;
		this.versionType = versionType;
	}

	@SuppressWarnings("incomplete-switch")
	public boolean isDataVerified() { // Returns true if object data matches
										// with Cardio2e data specs and data is
										// congruent.
		boolean verified = false;
		if (super.isDataVerified()) {
			switch (getTransactionType()) {
			case ACK:
			case NACK:
			case GET:
				verified = true;
				break;
			case INFORMATION:
				verified = ((versionType != null) && (version != null));
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
			case GET: // Note: Cardio2e response to GET version transaction type
						// is only 'C' type version. Other version types can be
						// obtained by login command only.
				returnString = String.format("%s%s %s %s",
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
				returnString = String.format("%s%s %s %s %s%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						getVersionType().symbol, version,
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
