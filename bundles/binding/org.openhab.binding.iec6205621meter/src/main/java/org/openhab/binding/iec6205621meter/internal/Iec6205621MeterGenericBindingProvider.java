/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.iec6205621meter.internal;

import java.util.StringTokenizer;

import org.openhab.binding.iec6205621meter.Iec6205621MeterBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class is responsible for parsing the binding configuration.
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * 
 * <ul>
 * 	<li><code>{ iec6205621meter="meter1:1.8.1" }</code> - shows the tarif 1 counter value of 'meter1'</li>
 * 	<li><code>{ iec6205621meter="meter1:1.8.2" }</code> - shows the tarif 1 counter value of 'meter1'</li>
 * 	<li><code>{  iec6205621meter="meter2:16.7" }</code> - shows the current power usage value of 'meter2'</li>
 * </ul>
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.5.0
 */
public class Iec6205621MeterGenericBindingProvider extends
		AbstractGenericBindingProvider implements Iec6205621MeterBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(Iec6205621MeterGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "iec6205621meter";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Number- and StringItems are allowed - please check your *.items configuration");
		}
		logger.debug(bindingConfig);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		Iec6205621MeterBindingConfig config = new Iec6205621MeterBindingConfig();
		StringTokenizer tokenizer = new StringTokenizer(bindingConfig.trim(),
				":");
		String[] tokens = new String[tokenizer.countTokens()];
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokenizer.nextToken();
		}
		config.meterName = tokens[0].trim();
		config.obis = tokens[1].trim();
		config.itemType = item.getClass();
		addBindingConfig(item, config);
	}

	@Override
	public String getObis(String itemName) {
		Iec6205621MeterBindingConfig config = (Iec6205621MeterBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.obis : null;
	}

	@Override
	public String getMeterName(String itemName) {
		Iec6205621MeterBindingConfig config = (Iec6205621MeterBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.meterName : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		Iec6205621MeterBindingConfig config = (Iec6205621MeterBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}

	class Iec6205621MeterBindingConfig implements BindingConfig {
		public String obis;
		public String meterName;
		public Class<? extends Item> itemType;
	}

}
