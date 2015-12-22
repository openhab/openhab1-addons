/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryBindingConfig;
import org.openhab.core.types.Command;

/**
 * Device interface
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */
public interface SimpleBinaryIDevice {
	/**
	 * Open device connection
	 * 
	 * @return
	 */
	public Boolean open();

	/**
	 * Close device connection
	 * 
	 */
	public void close();

	/**
	 * Send data to device
	 * 
	 * @param itemName
	 *            Item name
	 * @param command
	 *            Command to send
	 * @param config
	 *            Item config
	 */
	public void sendData(String itemName, Command command, SimpleBinaryBindingConfig config);

	/**
	 * Resend last sended data
	 * 
	 */
	public void resendData();

	/**
	 * Check new data for all connected devices
	 * 
	 */
	public void checkNewData();

	/**
	 * Return last sended data
	 * 
	 * @return
	 */
	public SimpleBinaryItemData getLastSentData();

	/**
	 * Remember last sended data
	 * 
	 * @param data
	 */
	public void setLastSentData(SimpleBinaryItemData data);

}
