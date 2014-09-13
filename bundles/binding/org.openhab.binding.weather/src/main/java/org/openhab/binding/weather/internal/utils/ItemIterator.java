/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.utils;

import org.openhab.binding.weather.WeatherBindingProvider;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;
import org.openhab.core.items.Item;

/**
 * Iterates through all weather items.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ItemIterator {
	protected WeatherContext context = WeatherContext.getInstance();

	/**
	 * Iterates through all items and calls the callback.
	 */
	public void iterate(ItemIteratorCallback callback) {
		for (WeatherBindingProvider provider : context.getProviders()) {
			for (String itemName : provider.getItemNames()) {
				WeatherBindingConfig bindingConfig = provider.getBindingFor(itemName);
				Item item = provider.getItem(itemName);
				callback.next(bindingConfig, item);
			}
		}
	}

	/**
	 * This callback interface is executed for every item in all providers.
	 */
	public interface ItemIteratorCallback {
		public void next(WeatherBindingConfig bindingConfig, Item item);
	}

}