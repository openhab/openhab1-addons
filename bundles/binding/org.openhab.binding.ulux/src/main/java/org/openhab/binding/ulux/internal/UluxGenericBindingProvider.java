/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxGenericBindingProvider extends AbstractGenericBindingProvider implements UluxBindingProvider {

	private static final Logger LOG = LoggerFactory.getLogger(UluxGenericBindingProvider.class);

	private static final Gson PARSER = new Gson();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UluxBindingConfig getBinding(String itemName) {
		return (UluxBindingConfig) this.bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "ulux";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context, final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		LOG.debug("Processing binding configuration: '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		final UluxBindingConfig config = PARSER.fromJson(bindingConfig, UluxBindingConfig.class);

		// TODO backgroundLight
		// TODO lockMode
		// TODO page
		// TODO display
		// TODO proximity

		addBindingConfig(item, config);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(final Item item, final String bindingConfig) throws BindingConfigParseException {
		// if (!(item instanceof NumberItem)) {
		// throw new BindingConfigParseException(
		// "item '"
		// + item.getName()
		// + "' is of type '"
		// + item.getClass().getSimpleName()
		// +
		// "', only NumberItems are allowed - please check your *.items configuration");
		// }
	}

}
