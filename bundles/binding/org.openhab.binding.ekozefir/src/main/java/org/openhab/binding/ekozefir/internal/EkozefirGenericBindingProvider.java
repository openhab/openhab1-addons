/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.internal;

import org.openhab.binding.ekozefir.EkozefirBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public class EkozefirGenericBindingProvider extends AbstractGenericBindingProvider implements EkozefirBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirGenericBindingProvider.class);
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ekozefir";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		addBindingConfig(item, parseConfiguration(item, bindingConfig));
	}

	private EkozefirBindingConfig parseConfiguration(Item item, String bindingConfig)
			throws BindingConfigParseException {
		String configs[] = bindingConfig.split(":");
		if (configs.length == 2) {
			if (configs[0].length() == 1)
				return new EkozefirBindingConfig(configs[1], configs[0].charAt(0));
		}
		throw new BindingConfigParseException("Could not parse config");
	}

	@Override
	public EkozefirBindingConfig getConfig(String itemName) {
		return (EkozefirBindingConfig) bindingConfigs.get(itemName);
	}

}
