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
