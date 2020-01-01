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
 * Enumerates the various RelayStates for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eRelayStates {

	OPENED { // Opened is equal to appliance ON
		{
			symbol = 'O';
		}
	},

	CLOSED { // Closed is equal to appliance OFF
		{
			symbol = 'C';
		}
	};

	public char symbol;

	public static Cardio2eRelayStates fromString(String relayState) {

		if ("".equals(relayState)) {
			return null;
		} else {
			relayState = relayState.toUpperCase();
		}

		for (Cardio2eRelayStates type : Cardio2eRelayStates.values()) {
			if (type.name().equals(relayState)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid relayState '" + relayState
				+ "'");
	}

	public static Cardio2eRelayStates fromSymbol(char relayState) {
		for (Cardio2eRelayStates type : Cardio2eRelayStates.values()) {
			if (type.symbol == relayState) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid relayState '" + relayState
				+ "'");
	}

}
