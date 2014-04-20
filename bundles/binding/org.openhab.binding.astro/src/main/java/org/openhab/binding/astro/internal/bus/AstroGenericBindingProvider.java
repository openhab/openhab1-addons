/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.bus;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the binding format and provides Astro binding informations.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class AstroGenericBindingProvider extends AbstractGenericBindingProvider implements AstroBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(AstroGenericBindingProvider.class);

	private BindingConfigParser parser = new BindingConfigParser();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "astro";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// validation is done in processBindingConfiguration
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		AstroBindingConfig config = parser.parse(item, bindingConfig);
		logger.debug("Adding item {} with {}", item.getName(), config);
		addBindingConfig(item, config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstroBindingConfig getBindingFor(String itemName) {
		return (AstroBindingConfig) bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean providesBindingFor(AstroType astroType) {
		for (BindingConfig config : bindingConfigs.values()) {
			if (config instanceof AstroBindingConfig) {
				if (((AstroBindingConfig) config).getType() == astroType) {
					return true;
				}
			}
		}
		return false;
	}

}
