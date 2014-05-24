/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul;

import java.util.List;

import org.openhab.binding.maxcul.internal.MaxCulBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Paul Hampson (cyclingengineer)
 * @since 1.5.0
 */
public interface MaxCulBindingProvider extends BindingProvider {

	MaxCulBindingConfig getConfigForItemName(String itemName);

	/**
	 * This will return the first config found for a particular serial number
	 * @param serial Serial number of device
	 * @return First configuration found, null if none are found
	 */
	MaxCulBindingConfig getConfigForSerialNumber(String serial);

	/**
	 * This will return a collection of configs for a particular serial number
	 * @param serial Serial number of devices
	 * @return All configurations found, null if none are found
	 */
	List<MaxCulBindingConfig> getConfigsForSerialNumber(String serial);
}
