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
 * Enumerates the various ObjectTypes for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eObjectTypes {

	LIGHTING {
		{
			symbol = 'L';
		}
	},

	RELAY {
		{
			symbol = 'R';
		}
	},

	HVAC_CONTROL {
		{
			symbol = 'H';
		}
	},

	HVAC_TEMPERATURE {
		{
			symbol = 'T';
		}
	},

	DATE_AND_TIME {
		{
			symbol = 'D';
		}
	},

	SCENARIO {
		{
			symbol = 'M';
		}
	},

	SECURITY {
		{
			symbol = 'S';
		}
	},

	ZONES {
		{
			symbol = 'Z';
		}
	},

	ZONES_BYPASS {
		{
			symbol = 'B';
		}
	},

	LOGIN {
		{
			symbol = 'P';
		}
	},

	VERSION {
		{
			symbol = 'V';
		}
	},

	CURTAIN { // Only in newer versions of Cardio2e
		{
			symbol = 'C';
		}
	};

	public char symbol;

	public static Cardio2eObjectTypes fromString(String objectType) {

		if ("".equals(objectType)) {
			return null;
		} else {
			objectType = objectType.toUpperCase();
		}

		for (Cardio2eObjectTypes type : Cardio2eObjectTypes.values()) {
			if (type.name().equals(objectType)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid objectType '" + objectType
				+ "'");
	}

	public static Cardio2eObjectTypes fromSymbol(char objectType) {
		for (Cardio2eObjectTypes type : Cardio2eObjectTypes.values()) {
			if (type.symbol == objectType) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid objectType '" + objectType
				+ "'");
	}

}
