package org.openhab.binding.resolvbus.internal;

import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;

public interface ResolVBUSListener {
	
	/**
	 * Inform all the interested items in this method
	 * @param name of the Item
	 * @param value of the Item
	 */
	public void publishUpdate(String name, String value);
	
	public void processInputStream(ResolVBUSInputStream vbusStream);

}
