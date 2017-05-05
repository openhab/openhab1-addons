/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.temperlan;

import java.util.Map;

import org.openhab.binding.temperlan.internal.TemperlanBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author snoerenberg
 * @since 1.0
 */
public interface TemperlanBindingProvider extends BindingProvider {

	/**
	 * Returns the Item Configuration identified by {@code itemName}.
	 * 
	 * @param itemName
	 *            the name of the Item.
	 * @return The Item Configuration identified by {@code itemName}.
	 * 
	 */
	public TemperlanBindingConfig getItemConfig(String itemName);

	/**
	 * Gets all of the configurations registered to a particular device
	 * identified by {@code deviceIp}
	 * 
	 * @param deviceUid
	 *            the uid of the device
	 * @param items
	 *            the map of configurations to populate. key=itemName,
	 *            value=configuration
	 */
	public void getDeviceConfigs(String deviceUid,
			Map<String, TemperlanBindingConfig> configs);
	
}
