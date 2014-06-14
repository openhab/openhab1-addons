/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecotouch.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.openhab.binding.ecotouch.EcoTouchBindingProvider;
import org.openhab.binding.ecotouch.EcoTouchTags;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Sebastian Held <sebastian.held@gmx.de>
 * @since 1.5.0
 */
public class EcoTouchGenericBindingProvider extends
		AbstractGenericBindingProvider implements EcoTouchBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(EcoTouchGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ecotouch";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			EcoTouchBindingConfig config = parseBindingConfig(item,
					EcoTouchTags.fromString(bindingConfig));
			addBindingConfig(item, config);
		} else {
			logger.warn("bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}

	/**
	 * Checks if the bindingConfig contains a valid binding type and returns an
	 * appropriate instance.
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException
	 *             if bindingConfig is no valid binding type
	 */
	protected EcoTouchBindingConfig parseBindingConfig(Item item,
			EcoTouchTags bindingConfig) throws BindingConfigParseException {
		if (EcoTouchTags.validateBinding(bindingConfig, item.getClass())) {
			return new EcoTouchBindingConfig(bindingConfig);
		} else {
			throw new BindingConfigParseException("'" + bindingConfig
					+ "' is no valid binding type");
		}
	}

	class EcoTouchBindingConfig implements BindingConfig {
		final private EcoTouchTags type;

		public EcoTouchBindingConfig(EcoTouchTags type) {
			this.type = type;
		}

		public EcoTouchTags getType() {
			return type;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getItemNamesForType(EcoTouchTags eventType) {
		Set<String> itemNames = new HashSet<String>();
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			EcoTouchBindingConfig heatpumpConfig = (EcoTouchBindingConfig) entry
					.getValue();
			if (heatpumpConfig.getType().equals(eventType)) {
				itemNames.add(entry.getKey());
			}
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getActiveTags() {
		Set<String> tagNames = new HashSet<String>();
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			EcoTouchBindingConfig heatpumpConfig = (EcoTouchBindingConfig) entry
					.getValue();
			tagNames.add(heatpumpConfig.getType().getTagName());
		}
		return tagNames.toArray(new String[tagNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public EcoTouchTags[] getActiveItems() {
		ArrayList<EcoTouchTags> items = new ArrayList<EcoTouchTags>();
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			EcoTouchBindingConfig heatpumpConfig = (EcoTouchBindingConfig) entry
					.getValue();
			items.add(heatpumpConfig.getType());
		}
		return items.toArray(new EcoTouchTags[items.size()]);
	}
}
