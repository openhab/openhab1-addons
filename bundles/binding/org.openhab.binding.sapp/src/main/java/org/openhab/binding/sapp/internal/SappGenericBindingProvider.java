/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.sapp.SappBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Paolo Denti
 * @since 1.0.0
 */
public class SappGenericBindingProvider extends AbstractGenericBindingProvider implements SappBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(SappGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "sapp";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		logger.debug(String.format("validating item '%s' against config '%s'", item, bindingConfig));
		// TODO
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			addBindingConfig(item, new SappBindingConfig(item.getName(), bindingConfig));
		} else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}
}
