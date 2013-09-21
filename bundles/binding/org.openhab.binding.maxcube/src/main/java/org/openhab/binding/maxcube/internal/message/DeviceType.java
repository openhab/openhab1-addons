/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.maxcube.internal.message;

/**
* This enumeration represents the different message types provided by the MAX!Cube protocol. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.3.0
*/
public enum DeviceType {
	// TODO: Plug Adapter device id not known yet
	Invalid(256), HeatingThermostat(1), HeatingThermostatPlus(2), WallMountedThermostat(
			3), ShutterContact(4), PushButton(5) , PlugAdapter(-1);

	private int value;

	private DeviceType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static DeviceType create(int value) {
		switch(value) {
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
}