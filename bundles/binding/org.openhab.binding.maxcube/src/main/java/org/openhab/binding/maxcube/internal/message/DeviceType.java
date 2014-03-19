/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

/**
* This enumeration represents the different message types provided by the MAX!Cube protocol. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public enum DeviceType {
	Invalid(256), Cube (0), HeatingThermostat(1), HeatingThermostatPlus(2), WallMountedThermostat(
			3), ShutterContact(4), PushButton(5);

	private int value;

	private DeviceType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static DeviceType create(int value) {
		switch(value) {
		case 0:
	    	return Cube;
		case 1:
			return HeatingThermostat;
		case 2:
			return HeatingThermostatPlus;
		case 3:
			return WallMountedThermostat;
		case 4: 
			return ShutterContact;
		case 5:
			return PushButton;
		default:
			return Invalid;
		}
	}
	
	public String toString() {
	    switch(value) {
	    case 0:
	    	return "Cube";
	    case 1:
	      return "Thermostat";
	    case 2:
	      return "Thermostat+";
	    case 3:
	      return "Wallmounted Thermostat";
	    case 4: 
	      return "Shutter Contact";
	    case 5:
	      return "Push Button";
	    default:
	      return "Invalid";
	    }
	  }
}