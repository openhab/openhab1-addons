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
