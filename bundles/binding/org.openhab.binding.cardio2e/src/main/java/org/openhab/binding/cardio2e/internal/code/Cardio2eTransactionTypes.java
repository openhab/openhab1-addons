/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.cardio2e.internal.code;

/**
 * Enumerates the various TransactionTypes for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eTransactionTypes {

	SET {
		{
			symbol = 'S';
		}
	},

	GET {
		{
			symbol = 'G';
		}
	},

	ACK {
		{
			symbol = 'A';
		}
	},

	NACK {
		{
			symbol = 'N';
		}
	},

	INFORMATION {
		{
			symbol = 'I';
		}
	};

	public char symbol;

	public static Cardio2eTransactionTypes fromString(String transactionType) {

		if ("".equals(transactionType)) {
			return null;
		} else {
			transactionType = transactionType.toUpperCase();
		}

		for (Cardio2eTransactionTypes type : Cardio2eTransactionTypes.values()) {
			if (type.name().equals(transactionType)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid transactionType '"
				+ transactionType + "'");
	}

	public static Cardio2eTransactionTypes fromSymbol(char transactionType) {
		for (Cardio2eTransactionTypes type : Cardio2eTransactionTypes.values()) {
			if (type.symbol == transactionType) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid transactionType '"
				+ transactionType + "'");
	}

}
