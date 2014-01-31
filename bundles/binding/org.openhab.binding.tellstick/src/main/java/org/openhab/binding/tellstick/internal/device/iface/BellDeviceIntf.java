package org.openhab.binding.tellstick.internal.device.iface;

import org.openhab.binding.tellstick.internal.device.TellstickException;


/**
 * Devices that can perform the action bell.
 * Does not implement super interface Device since this should only perform the bell action.
 * @author peec
 *
 */
public interface BellDeviceIntf {

	
	/**
	 * Bells the bell, meaning a sound will be played from the belling device.
	 * @throws TellstickException 
	 */
	public void bell() throws TellstickException;
	
	/**
	 * Returns the name of the device.
	 * @return
	 */
	public String getType();
	
}
