/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha;

import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaDevice;
import org.openhab.core.binding.BindingProvider;

/**
 * Interface for FritzAHA binding providers
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaBindingProvider extends BindingProvider {
	/**
	 * Gets device config corresponding to the item specified.
	 * 
	 * @param itemName
	 *            Name of the item for which to get the device config
	 * @return Device config corresponding to item
	 */
	public FritzahaDevice getDeviceConfig(String itemName);
}
