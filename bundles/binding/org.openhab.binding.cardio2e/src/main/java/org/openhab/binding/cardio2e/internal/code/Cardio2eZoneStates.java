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
 * Enumerates the various ZoneStates for Cardio2e binding Also provides Cardio2e
 * string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eZoneStates {

	NORMAL {
		{
			symbol = 'N';
		}
	},

	OPEN {
		{
			symbol = 'O';
		}
	},

	CLOSED {
		{
			symbol = 'C';
		}
	},

	ERROR {
		{
			symbol = 'E';
		}
	};

	public char symbol;

	public static Cardio2eZoneStates fromString(String zoneState) {

		if ("".equals(zoneState)) {
			return null;
		} else {
			zoneState = zoneState.toUpperCase();
		}

		for (Cardio2eZoneStates type : Cardio2eZoneStates.values()) {
			if (type.name().equals(zoneState)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid zoneState '" + zoneState
				+ "'");
	}

	public static Cardio2eZoneStates fromSymbol(char zoneState) {
		for (Cardio2eZoneStates type : Cardio2eZoneStates.values()) {
			if (type.symbol == zoneState) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid zoneState '" + zoneState
				+ "'");
	}

}
