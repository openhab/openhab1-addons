/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

import java.util.List;

import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.core.items.Item;

/**
 * Helper class to iterate through all provider items.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class ProviderItemIterator {
	protected HomematicContext context = HomematicContext.getInstance();

	/**
	 * Iterate through all providers and their items, creates a converter and
	 * calls the callback.
	 */
	public void iterate(HomematicBindingConfig bindingConfig, ProviderItemIteratorCallback callback) {
		for (HomematicBindingProvider provider : context.getProviders()) {
			List<Item> items = provider.getItemsFor(bindingConfig);
			for (Item item : items) {
				HomematicBindingConfig providerBindingConfig = provider.getBindingFor(item.getName());
				Converter<?> converter = context.getConverterFactory().createConverter(item, providerBindingConfig);
				if (converter != null) {
					callback.next(providerBindingConfig, item, converter);
				}
			}
		}
	}

	/**
	 * This callback interface is executed for every item in all providers.
	 */
	public interface ProviderItemIteratorCallback {
		public void next(HomematicBindingConfig providerBindingConfig, Item item, Converter<?> converter);
	}
}
