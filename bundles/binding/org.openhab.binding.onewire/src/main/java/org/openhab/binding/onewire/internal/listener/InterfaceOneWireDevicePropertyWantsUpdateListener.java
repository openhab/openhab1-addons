package org.openhab.binding.onewire.internal.listener;

import java.util.EventListener;

/**
 * This Interface definies a Listener for Items which wanted to be updated
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public interface InterfaceOneWireDevicePropertyWantsUpdateListener extends EventListener {

	/**
	 * This method must be implemenented by the classes, which implements the Listener
	 * 
	 * @param wantsUpdateEvent
	 */
	public void devicePropertyWantsUpdate(OneWireDevicePropertyWantsUpdateEvent wantsUpdateEvent);

}
