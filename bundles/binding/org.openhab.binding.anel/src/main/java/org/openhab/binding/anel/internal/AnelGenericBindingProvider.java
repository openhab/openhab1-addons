/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.io.InvalidClassException;

import org.openhab.binding.anel.AnelBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author paphko
 * @since 1.6.0
 */
public class AnelGenericBindingProvider extends AbstractGenericBindingProvider implements AnelBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "anel";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
					+ item.getClass().getSimpleName()
					+ "', only Switch/String/NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		final String commandType = bindingConfig.trim();
		try {
			AnelCommandType.validateBinding(commandType, item.getClass());
			final AnelCommandType cmdType = AnelCommandType.getCommandType(commandType);

			// if command type was validated successfully, add binding config
			addBindingConfig(item, new AnelBindingConfig(item.getClass(), cmdType));
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("'" + commandType + "' is not a valid command type");
		} catch (InvalidClassException e) {
			throw new BindingConfigParseException("Not valid class for command type '" + commandType + "'");
		}
	}

	/**
	 * Internal class to represent an openHAB item binding.
	 */
	class AnelBindingConfig implements BindingConfig {
		public final Class<? extends Item> itemType;
		public final AnelCommandType commandType;

		protected AnelBindingConfig(Class<? extends Item> itemType, AnelCommandType cmdType) {
			this.itemType = itemType;
			this.commandType = cmdType;
		}

		@Override
		public String toString() {
			return "AnelBindingConfig [itemType=" + itemType + ", commandType=" + commandType + "]";
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		final AnelBindingConfig config = (AnelBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public AnelCommandType getCommandType(String itemName) {
		final AnelBindingConfig config = (AnelBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.commandType : null;
	}

}
