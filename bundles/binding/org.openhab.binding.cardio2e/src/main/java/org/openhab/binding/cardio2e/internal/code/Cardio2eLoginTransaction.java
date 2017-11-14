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
 * Cardio2e Binding structured transaction model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eLoginTransaction extends Cardio2eTransaction {
	private static final long serialVersionUID = 3396631977094337554L;
	private Cardio2eLoginCommands loginCommand = null;
	private String programCode = null;

	public Cardio2eLoginTransaction() { // Simple logout constructor (this
										// transaction is obsolete: provided for
										// legacy compatibility).
		super.setObjectType(Cardio2eObjectTypes.LOGIN);
	}

	public Cardio2eLoginTransaction(Cardio2eLoginCommands logoutCommand) { // Simple
																			// set
																			// logout
																			// constructor
																			// (logout
																			// command
																			// is
																			// obsolete:
																			// only
																			// provided
																			// for
																			// legacy
																			// compatibility).
		if (logoutCommand == Cardio2eLoginCommands.LOGOUT) {
			setTransactionType(Cardio2eTransactionTypes.SET);
			super.setObjectType(Cardio2eObjectTypes.LOGIN);
			setLoginCommand(logoutCommand);
		} else {
			throw new UnsupportedOperationException(logoutCommand.toString());
		}
	}

	public Cardio2eLoginTransaction(String programCode) { // Simple set login
															// constructor.
		setTransactionType(Cardio2eTransactionTypes.SET);
		super.setObjectType(Cardio2eObjectTypes.LOGIN);
		setLoginCommand(Cardio2eLoginCommands.LOGIN);
		setProgramCode(programCode);
	}

	public void setTransactionType(Cardio2eTransactionTypes transactionType) {
		if (transactionType != Cardio2eTransactionTypes.GET) {
			super.setTransactionType(transactionType);
		} else {
			throw new IllegalArgumentException("invalid transactionType '"
					+ transactionType + "for a LOGIN transaction");
		}
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

	public Cardio2eLoginCommands getLoginCommand() {
		return loginCommand;
	}

	public void setLoginCommand(Cardio2eLoginCommands loginCommand) {
		isDataComplete = false;
		this.loginCommand = loginCommand;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		isDataComplete = false;
		if ((programCode.length() <= CARDIO2E_MAX_PROGRAM_CODE_LENGTH)
				&& StringUtils.isNumeric(programCode)) {
			this.programCode = programCode;
		} else {
			throw new IllegalArgumentException("invalid programCode '"
					+ programCode + "'");
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
			case INFORMATION:
				verified = (loginCommand != null);
				break;
			case NACK:
				verified = (getErrorCode() > 0);
				break;
			case SET:
				verified = ((loginCommand == Cardio2eLoginCommands.LOGOUT) || ((loginCommand != null) && (programCode != null)));
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
			case INFORMATION: // Signals Login Information End (@I P E)
				returnString = String.format("%s%s %s E%s",
						CARDIO2E_START_TRANSACTION_INITIATOR,
						getTransactionType().symbol, getObjectType().symbol,
						CARDIO2E_END_TRANSACTION_CHARACTER);
				break;
			case SET:
				if (programCode != null) {
					returnString = String.format("%s%s %s %s %s%s",
							CARDIO2E_START_TRANSACTION_INITIATOR,
							getTransactionType().symbol,
							getObjectType().symbol, loginCommand.symbol,
							programCode, CARDIO2E_END_TRANSACTION_CHARACTER);
				} else {
					returnString = String.format("%s%s %s %s%s",
							CARDIO2E_START_TRANSACTION_INITIATOR,
							getTransactionType().symbol,
							getObjectType().symbol, loginCommand.symbol,
							CARDIO2E_END_TRANSACTION_CHARACTER);
				}
				break;
			}
		}
		return returnString;
	}

	public boolean isSmartSendingEnabled() { // Returns true because this class
												// would be always smartSending
												// enabled.
		return true;
	}
}
