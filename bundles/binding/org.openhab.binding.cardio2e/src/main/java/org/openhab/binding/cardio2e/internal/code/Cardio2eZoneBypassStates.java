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
 * Enumerates the various ZoneBypassStates for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eZoneBypassStates {

	BYPASSED {
		{
			symbol = 'Y';
		}
	},

	NOT_BYPASSED {
		{
			symbol = 'N';
		}
	};

	public char symbol;

	public static Cardio2eZoneBypassStates fromString(String zoneBypassState) {

		if ("".equals(zoneBypassState)) {
			return null;
		} else {
			zoneBypassState = zoneBypassState.toUpperCase();
		}

		for (Cardio2eZoneBypassStates type : Cardio2eZoneBypassStates.values()) {
			if (type.name().equals(zoneBypassState)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid zoneBypassState '"
				+ zoneBypassState + "'");
	}

	public static Cardio2eZoneBypassStates fromSymbol(char zoneBypassState) {
		for (Cardio2eZoneBypassStates type : Cardio2eZoneBypassStates.values()) {
			if (type.symbol == zoneBypassState) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid zoneBypassState '"
				+ zoneBypassState + "'");
	}

}
