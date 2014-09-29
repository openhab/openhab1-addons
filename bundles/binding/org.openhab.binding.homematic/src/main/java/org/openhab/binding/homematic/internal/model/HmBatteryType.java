/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

/**
 * Definition of the different battery types.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public enum HmBatteryType {
	AA, AAA, CR2032, CR2016, LR44, WINMATIC_ACCU;

	@Override
	public String toString() {
		switch (this) {
		case AA:
			return "AA/Mignon/LR06";
		case AAA:
			return "AAA/Micro/LR03";
		case CR2032:
			return "CR2032";
		case CR2016:
			return "CR2016";
		case LR44:
			return "LR44";
		case WINMATIC_ACCU:
			return "Accu-Pack 10,8 V";
		}
		return null;
	}

}
