/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather;

import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * The interface to implement to provide a binding for Weather.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface WeatherBindingProvider extends BindingProvider {

	/**
	 * Returns the item object by itemName.
	 */
	public Item getItem(String itemName);

	/**
	 * Returns the WeatherBindingConfig for an item by name.
	 */
	public WeatherBindingConfig getBindingFor(String itemName);

	/**
	 * Returns true, if a binding for the specified locationId is available.
	 */
	public boolean hasBinding(String locationId);

}
