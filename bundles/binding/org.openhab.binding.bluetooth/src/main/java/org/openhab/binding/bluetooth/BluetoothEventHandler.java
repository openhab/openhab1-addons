/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
