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
