package org.openhab.binding.onewire.internal.listener;

import java.util.EventObject;

/**
 * EventClass for InterfaceOneWireDevicePropertyWantsUpdateListener
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public class OneWireDevicePropertyWantsUpdateEvent extends EventObject {

	private static final long serialVersionUID = -6971853348646127138L;

	/**
	 * Item name
	 */
	private String ivItemName = null;

	/**
	 * @param pvSource
	 * @param pvItemName
	 */
	public OneWireDevicePropertyWantsUpdateEvent(Object pvSource, String pvItemName) {
		super(pvSource);
		this.ivItemName = pvItemName;
	}

	/**
	 * @return name of the item which wants to be updated
	 */
	public String getItemName() {
		return ivItemName;
	}

}
