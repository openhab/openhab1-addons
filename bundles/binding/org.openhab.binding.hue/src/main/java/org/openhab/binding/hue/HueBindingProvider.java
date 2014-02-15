/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue;

import org.openhab.binding.hue.internal.HueBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Philips Hue devices.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Roman Hartmann
 * @since 1.2.0
 */
public interface HueBindingProvider extends BindingProvider {

	/**
	 * Returns the configuration for the item with the given name.
	 * 
	 * @param itemName
	 * @return The configuration if there is an item with the given name, null
	 *         otherwise.
	 */
	public HueBindingConfig getItemConfig(String itemName);

}
