/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekey.internal;

import org.openhab.binding.ekey.EKeyBindingProvider;
import org.openhab.binding.ekey.internal.EKeyBindingConfig.InterestType;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * Every Item can be interested in ONE information that is received with the eKey UDP-packet
 * Each time a new packet arrives, all items will be informed with the latest values
 * 
 * bindingconf: ekey=
 * "ACTION|USERNAME|USERID|USERSTATUS|TERMINALID|TERMINALNAME|FINGERID|KEYID|INPUTID|MODE"
 * 
 * @author Paul Schlagitweit
 * @since 1.5.0
 */
public class EKeyGenericBindingProvider extends AbstractGenericBindingProvider
		implements EKeyBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ekey";
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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		EKeyBindingConfig config = new EKeyBindingConfig();
		
		if(bindingConfig==null)
			throw new BindingConfigParseException(
					"Your binding configuration is illegal!\n"
							+ "Possible values are: ACTION, USERNAME, USERID, TERMINALID, TERMINALNAME, "
							+ "FINGERID, KEYID, INPUTID, RELAISID, MODE\nExample: {ekey=\"ACTION\"}");
		
		config.itemType = item.getClass();

		// format the passed config parameter
		String value = bindingConfig.trim().toUpperCase();

		try { // try to convert the parameter to one of the predefined enum
				// types
			config.interestedIn = EKeyBindingConfig.InterestType.getType(value);
		} catch (Exception e) { // throw exception - parameter was illegal
			throw new BindingConfigParseException(
					"eKey does not know the configuration value "
							+ "'"
							+ value
							+ "' that you passed in the item binding configuration!\n"
							+ "Possible values are: ACTION, USERNAME, USERID, TERMINALID, TERMINALNAME, "
							+ "FINGERID, KEYID, INPUTID, RELAISID, MODE\nExample: {ekey=\"ACTION\"}");
		}

		addBindingConfig(item, config);
	}

	@Override
	public InterestType getItemInterest(String itemName) {
		EKeyBindingConfig config = (EKeyBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.interestedIn : null;
	}

	@Override
	public Class<? extends Item> getItemType(String itemName) {
		EKeyBindingConfig config = (EKeyBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}

}
