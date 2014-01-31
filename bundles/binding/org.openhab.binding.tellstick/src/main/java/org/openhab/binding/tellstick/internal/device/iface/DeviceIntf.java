package org.openhab.binding.tellstick.internal.device.iface;

import org.openhab.binding.tellstick.internal.device.TellstickException;


/**
 * A generic device.
 * @author peec
 *
 */
public interface DeviceIntf{

	
	
	/**
	 * Turns on the device.
	 * @return 
	 */
	public void on() throws TellstickException;
	
	/**
	 * Turns off the device.
	 */
	public void off() throws TellstickException;
	
	
	/**
	 * Returns the name of the device.
	 * @return
	 */
	public String getType();
	
}
