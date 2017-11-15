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
