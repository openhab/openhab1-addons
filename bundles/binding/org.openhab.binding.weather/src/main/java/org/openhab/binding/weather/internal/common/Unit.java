/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.common;

/**
 * Unit definition the binding is able to convert to.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public enum Unit {
	FAHRENHEIT, MPH, INCHES, BEAUFORT, KNOTS, MPS;

	/**
	 * Parses the string and returns the Unit enum.
	 */
	public static Unit parse(String name) {
		if (name == null) {
			return null;
		} else if (FAHRENHEIT.toString().equalsIgnoreCase(name)) {
			return FAHRENHEIT;
		} else if (MPH.toString().equalsIgnoreCase(name)) {
			return MPH;
		} else if (INCHES.toString().equalsIgnoreCase(name)) {
			return INCHES;
		} else if (BEAUFORT.toString().equalsIgnoreCase(name)) {
			return BEAUFORT;
		} else if (KNOTS.toString().equalsIgnoreCase(name)) {
			return KNOTS;
		} else if (MPS.toString().equalsIgnoreCase(name)) {
			return MPS;
		} else {
			return null;
		}
	}
}
