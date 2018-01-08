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
 * Enumerates the various HvacSystemModes for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eHvacSystemModes {

	AUTO {
		{
			symbol = 'A';
		}
	},

	HEATING {
		{
			symbol = 'H';
		}
	},

	COOLING {
		{
			symbol = 'C';
		}
	},

	OFF {
		{
			symbol = 'O';
		}
	},

	ECONOMY {
		{
			symbol = 'E';
		}
	},

	NORMAL {
		{
			symbol = 'N';
		}
	};

	public char symbol;

	public static Cardio2eHvacSystemModes fromString(String hvacSystemMode) {

		if ("".equals(hvacSystemMode)) {
			return null;
		} else {
			hvacSystemMode = hvacSystemMode.toUpperCase();
		}

		for (Cardio2eHvacSystemModes type : Cardio2eHvacSystemModes.values()) {
			if (type.name().equals(hvacSystemMode)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid hvacSystemMode '"
				+ hvacSystemMode + "'");
	}

	public static Cardio2eHvacSystemModes fromSymbol(char hvacSystemMode) {
		for (Cardio2eHvacSystemModes type : Cardio2eHvacSystemModes.values()) {
			if (type.symbol == hvacSystemMode) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid hvacSystemMode '"
				+ hvacSystemMode + "'");
	}

}
