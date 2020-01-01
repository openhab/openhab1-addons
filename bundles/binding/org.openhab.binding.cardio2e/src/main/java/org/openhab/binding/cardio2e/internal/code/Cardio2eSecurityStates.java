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
 * Enumerates the various SecurityStates for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eSecurityStates {

	ARMED {
		{
			symbol = 'A';
		}
	},

	DISARMED {
		{
			symbol = 'D';
		}
	};

	public char symbol;

	public static Cardio2eSecurityStates fromString(String securityState) {

		if ("".equals(securityState)) {
			return null;
		} else {
			securityState = securityState.toUpperCase();
		}

		for (Cardio2eSecurityStates type : Cardio2eSecurityStates.values()) {
			if (type.name().equals(securityState)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid securityState '"
				+ securityState + "'");
	}

	public static Cardio2eSecurityStates fromSymbol(char securityState) {
		for (Cardio2eSecurityStates type : Cardio2eSecurityStates.values()) {
			if (type.symbol == securityState) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid securityState '"
				+ securityState + "'");
	}

}
