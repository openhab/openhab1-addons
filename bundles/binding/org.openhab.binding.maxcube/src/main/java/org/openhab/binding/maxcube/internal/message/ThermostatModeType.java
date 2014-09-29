/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.PrimitiveType;

/**
* This enumeration represents the different mode types of a MAX!Cube heating thermostat. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public enum ThermostatModeType implements PrimitiveType, State, Command {
	AUTOMATIC, MANUAL, VACATION, BOOST;

	public String format(String pattern) {
		return String.format(pattern, this.toString());
	}
}
