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
 * Enumerates the various LoginCommands for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eLoginCommands {

	LOGIN {
		{
			symbol = 'I';
		}
	},

	LOGOUT { // Logout is obsolete: only provided for legacy compatibility.
		{
			symbol = 'O';
		}
	};

	public char symbol;

	public static Cardio2eLoginCommands fromString(String loginCommand) {

		if ("".equals(loginCommand)) {
			return null;
		} else {
			loginCommand = loginCommand.toUpperCase();
		}

		for (Cardio2eLoginCommands type : Cardio2eLoginCommands.values()) {
			if (type.name().equals(loginCommand)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid loginCommand '"
				+ loginCommand + "'");
	}

	public static Cardio2eLoginCommands fromSymbol(char loginCommand) {
		for (Cardio2eLoginCommands type : Cardio2eLoginCommands.values()) {
			if (type.symbol == loginCommand) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid loginCommand '"
				+ loginCommand + "'");
	}

}
