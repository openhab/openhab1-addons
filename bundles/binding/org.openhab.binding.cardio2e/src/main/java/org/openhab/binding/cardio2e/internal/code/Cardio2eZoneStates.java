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
