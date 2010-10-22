/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.bluetooth.internal;

/**
 * This class is a data structure to hold all relevant information about a bluetooth device.
 * 
 * @author Kai Kreuzer
 *
 * @since 0.3.0
 *
 */
public class BluetoothDevice {

	private String address;
	private String friendlyName;
	private boolean paired;

	/**
	 * Default constructor which directly initializes the fields
	 * 
	 * @param address the address of the bluetooth device, e.g. "EC3B2BD562D2"
	 * @param friendlyName the name the user has given the device, e.g. "iPhone"
	 * @param paired true, if the device has been paired with the host and is therefore trusted
	 */
	public BluetoothDevice(String address, String friendlyName, boolean paired) {
		this.address = address;
		this.friendlyName = friendlyName;
		this.paired = paired;
	}

	/**
	 * returns the address of the bluetooth device
	 * 
	 * @return the address of the bluetooth device
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * returns the friendly name of the bluetooth device
	 * 
	 * @return the friendly name of the bluetooth device
	 */
	public String getFriendlyName() {
		return friendlyName;
	}

	/**
	 * tells whether the device is paired with the host
	 * 
	 * @return true, if the device has been paired
	 */
	public boolean isPaired() {
		return paired;
	}

	@Override
	public String toString() {
		return address + " (" + friendlyName + ")" + (paired ? "!" : "?");
	}
	
}
