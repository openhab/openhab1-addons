package org.openhab.binding.tellstick.internal.device.iface;

import org.openhab.binding.tellstick.internal.device.TellstickException;


/**
 * A device that can be dimmed.
 * @author peec
 *
 */
public interface DimmableDeviceIntf extends DeviceIntf{

	/**
	 * Dims lights to a certain level.
	 */
	public void dim(int level) throws TellstickException;
	
	
}
