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

package org.openhab.binding.bluetooth;

import org.openhab.binding.bluetooth.internal.BluetoothDevice;


/**
 * This interface must be implemented in order to be notified about the results of the bluetooth discovery service.
 * Implementing classes must register themselves as a service in order to be taken into account.
 * 
 * @author Kai Kreuzer
 * 
 * @since 0.3.0
 *
 */
public interface BluetoothEventHandler {

	/**
	 * called, if a bluetooth device has just been discovered
	 * 
	 * @param device the newly discovered device
	 */
	public void handleDeviceInRange(BluetoothDevice device);

	/**
	 * called, if a bluetooth device cannot be found anymore
	 * 
	 * @param device the device that is not in range anymore
	 */
	public void handleDeviceOutOfRange(BluetoothDevice device);
	
	/**
	 * called, after each complete device discovery run, even
	 * if no information has changed.
	 * 
	 * @param a list of all devices that are currently in range
	 */
	public void handleAllDevicesInRange(Iterable<BluetoothDevice> devices);
	
	/**
	 * tells, whether this handler will do anything at all, if being called.
	 * If no handers are active, the bluetooth discovery service is halted,
	 * so this method should always return a helpful value.
	 * 
	 * @return true, if the handler will show activity if called. If it stays
	 * idle for any kind of input, it should return false here.
	 */
	public boolean isActive();
}
