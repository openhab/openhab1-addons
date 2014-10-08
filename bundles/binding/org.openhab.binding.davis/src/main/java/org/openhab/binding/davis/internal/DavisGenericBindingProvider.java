/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.davis.DavisBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DavisGenericBindingProvider extends AbstractGenericBindingProvider implements DavisBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(DavisGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "davis";
	}

	/**
	 * @{inheritDoc
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem || item instanceof DateTimeItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
					+ "', only Number- and StringItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		DavisBindingConfig config = new DavisBindingConfig(bindingConfig);
		addBindingConfig(item, config);
	}

	/**
	 * @{inheritDoc
	 */
	public List<String> getConfiguredKeys() {
		Set<String> eventTypes = new HashSet<String>();

		Iterator<BindingConfig> it = bindingConfigs.values().iterator();
		while (it.hasNext()) {
			BindingConfig config = it.next();
			eventTypes.add(((DavisBindingConfig) config).key);
		}

		return new ArrayList<String>(eventTypes);
	}

	/**
	 * @{inheritDoc
	 */
	public List<String> getItemNamesForKey(String key) {
		Set<String> itemNames = new HashSet<String>();
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			DavisBindingConfig config = (DavisBindingConfig) entry.getValue();
			if (config.key.equals(key)) {
				itemNames.add(entry.getKey());
			}
		}
		return new ArrayList<String>(itemNames);
	}

	/**
	 * @{inheritDoc
	 */
	public String getConfiguredKeyForItem(String itemName) {
		return ((DavisBindingConfig) bindingConfigs.get(itemName)).key;
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings.
	 */
	class DavisBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		public String key;

		public DavisBindingConfig(String bindingConfig) {
			key = bindingConfig;
		}
	}
}
