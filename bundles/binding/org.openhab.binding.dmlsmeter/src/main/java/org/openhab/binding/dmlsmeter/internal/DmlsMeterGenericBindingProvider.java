/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmlsmeter.internal;

import java.util.StringTokenizer;

import org.openhab.binding.dmlsmeter.DmlsMeterBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.4.0
 */
public class DmlsMeterGenericBindingProvider extends AbstractGenericBindingProvider implements DmlsMeterBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(DmlsMeterGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "dmlsmeter";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Number- and StringItems are allowed - please check your *.items configuration");
		}
		logger.debug(bindingConfig);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		DmlsMeterBindingConfig config = new DmlsMeterBindingConfig();
		//TODO add own config parser class 
		StringTokenizer tokenizer = new StringTokenizer(bindingConfig.trim(), ";");
		String[] tokens = new String[tokenizer.countTokens()];  
		for( int i = 0; i < tokens.length; i++ ) {  
			tokens[i] = tokenizer.nextToken();  
		}  
		config.meterName=tokens[0].trim();	
		config.obis=tokens[1].trim();
		config.itemType = item.getClass();
		addBindingConfig(item, config);
	}

	@Override
	public String getObis(String itemName) {
		DmlsMeterBindingConfig config = (DmlsMeterBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.obis : null;
	}

	@Override
	public String getMeterName(String itemName) {
		DmlsMeterBindingConfig config = (DmlsMeterBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.meterName : null;
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		DmlsMeterBindingConfig config = (DmlsMeterBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	class DmlsMeterBindingConfig implements BindingConfig {
		public String obis;
		public String meterName;
		public Class<? extends Item> itemType;
	}

}
