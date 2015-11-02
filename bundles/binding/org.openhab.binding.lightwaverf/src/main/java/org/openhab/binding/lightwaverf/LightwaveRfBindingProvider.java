/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf;

import java.util.List;

import org.openhab.binding.lightwaverf.internal.LightwaveRfItemDirection;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public interface LightwaveRfBindingProvider extends BindingProvider {

	/**
	 * Returns the Lightwave DeviceId for this itemString
	 * 
	 * @param itemString
	 * @return
	 */
	String getDeviceId(String itemString);

	/**
	 * Returns the Lightwave DeviceId for this itemString
	 * 
	 * @param itemString
	 * @return
	 */
	String getRoomId(String itemString);

	/**
	 * Returns a list of all items that match this roomId, deviceId. If deviceId
	 * is null then will return all items that match just the room
	 * 
	 * @param roomId
	 * @param deviceId
	 * @return
	 */
	List<String> getBindingItemsForRoomDevice(String roomId, String deviceId);

	/**
	 * Returns a list of all items that match this room.
	 * 
	 * @param roomId
	 * @return
	 */
	List<String> getBindingItemsForRoom(String roomId);

	/**
	 * Returns a list of all items that match this serial.
	 * 
	 * @param serial
	 * @return
	 */
	List<String> getBindingItemsForSerial(String serial);

	/**
	 * Returns a list of all items that match this Type
	 * 
	 * @param version
	 * @return
	 */
	List<String> getBindingItemsForType(LightwaveRfType version);

	/**
	 * Returns the type for an item by itemName
	 * 
	 * @param itemName
	 * @return
	 */
	LightwaveRfType getTypeForItemName(String itemName);

	/**
	 * Get the poll interval for the given itemName
	 * 
	 * @param itemName
	 * @return
	 */
	int getPollInterval(String itemName);
	
	/**
	 * Get the direction for the given itemName.
	 * @param itemName
	 * @return
	 */
	LightwaveRfItemDirection getDirection(String itemName);
}
