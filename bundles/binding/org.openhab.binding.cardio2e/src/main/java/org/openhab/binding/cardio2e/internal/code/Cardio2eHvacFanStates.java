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
 * Enumerates the various HvacFanStates for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eHvacFanStates {

	STOP {
		{
			symbol = 'S';
		}
	},

	RUNNING {
		{
			symbol = 'R';
		}
	};

	public char symbol;

	public static Cardio2eHvacFanStates fromString(String hvacFanState) {

		if ("".equals(hvacFanState)) {
			return null;
		} else {
			hvacFanState = hvacFanState.toUpperCase();
		}

		for (Cardio2eHvacFanStates type : Cardio2eHvacFanStates.values()) {
			if (type.name().equals(hvacFanState)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid hvacFanState '"
				+ hvacFanState + "'");
	}

	public static Cardio2eHvacFanStates fromSymbol(char hvacFanState) {
		for (Cardio2eHvacFanStates type : Cardio2eHvacFanStates.values()) {
			if (type.symbol == hvacFanState) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid hvacFanState '"
				+ hvacFanState + "'");
	}

}
