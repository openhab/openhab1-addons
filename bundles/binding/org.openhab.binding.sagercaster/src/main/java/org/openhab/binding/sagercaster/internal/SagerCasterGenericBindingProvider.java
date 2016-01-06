/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sagercaster.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sagercaster.SagerCasterBindingConfig;
import org.openhab.binding.sagercaster.SagerCasterBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * SagerCaster binding information from it.
 * </p>
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <p>
 * <code>
 * sagercaster="keyword"
 * </code>
 * <p>
 * <p>
 * Where keyword can be one of : 
 * <ul>
 * <li><code>compass</code></li>
 * <li><code>windtrend</code></li>
 * <li><code>pressuretrend</code></li>
 * <li><code>forecast</code></li>
 * <li><code>velocity</code></li>
 * <li><code>windfrom</code></li>
 * <li><code>windto</code></li>
 * <li><code>windbearing</code></li>
 * <li><code>sealevelpressure</code></li>
 * <li><code>cloudlevel</code></li>
 * <li><code>raining</code></li>
 * <li><code>windspeed</code></li>
 * </ul>
 *
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class SagerCasterGenericBindingProvider extends AbstractGenericBindingProvider implements SagerCasterBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(SagerCasterGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "sagercaster";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem || item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only String- and NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,	String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		SagerCasterBindingConfig config = parseBindingConfig(bindingConfig,	item);
		logger.debug("Adding item {} with {}", item.getName(), config);
		addBindingConfig(item, config);
	}

	private SagerCasterBindingConfig parseBindingConfig(String bindingConfig,
			Item item) throws BindingConfigParseException {

		String command = StringUtils.trim(bindingConfig);
		CommandType commandType = CommandType.fromString(command);

		return new SagerCasterBindingConfig(commandType, item);
	}

	@Override
	public Iterable<String> getItemNamesBy(CommandType commandType) {
		Set<String> items = new HashSet<String>();

		for (String key : bindingConfigs.keySet()) {
			SagerCasterBindingConfig config = (SagerCasterBindingConfig) bindingConfigs
					.get(key);
			if (config.commandType == commandType) {
				items.add(key);
			}
		}
		return items;
	}

	@Override
	public SagerCasterBindingConfig getConfig(String itemName) {
		SagerCasterBindingConfig config = (SagerCasterBindingConfig) bindingConfigs.get(itemName);
		return config;
	}
}
