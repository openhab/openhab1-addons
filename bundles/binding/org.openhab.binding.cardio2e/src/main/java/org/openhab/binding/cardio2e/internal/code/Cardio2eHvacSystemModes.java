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
