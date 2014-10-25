/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.samsungac.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.samsungac.SamsungAcBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stein Tore Tøsse
 * @since 1.6.0
 */
public class SamsungAcGenericBindingProvider extends
		AbstractGenericBindingProvider implements SamsungAcBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(SamsungAcGenericBindingProvider.class);

	private static final Pattern CONFIG_PATTERN = Pattern
			.compile("\\[(.*)\\|(.*)\\]");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "samsungac";
	}

	/**
	 * @{inheritDoc
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem) && !(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', but only Number and Switchs items are allowed.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);
		SamsungAcBindingConfig config = parseBindingConfig(item, bindingConfig);
		addBindingConfig(item, config);
	}

	private SamsungAcBindingConfig parseBindingConfig(Item item,
			String bindingConfig) throws BindingConfigParseException {
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

		if (!matcher.matches())
			throw new BindingConfigParseException("Config for item '"
					+ item.getName() + "' could not be parsed.");

		String acInstance = matcher.group(1);
		CommandEnum property = CommandEnum.valueOf(matcher.group(2));

		return new SamsungAcBindingConfig(acInstance, item.getName(), property);
	}

	public BindingConfig getItem(CommandEnum property) {
		for (BindingConfig config : bindingConfigs.values()) {
			SamsungAcBindingConfig con = (SamsungAcBindingConfig) config;
			if (property.equals(con.getProperty()))
				return con;
		}
		return null;
	}

	public String getItemName(CommandEnum property) {
		SamsungAcBindingConfig con = (SamsungAcBindingConfig) getItem(property);
		if (con != null && property.equals(con.getProperty()))
			return con.getItemName();
		return null;
	}

	class SamsungAcBindingConfig implements BindingConfig {
		private String acInstance;
		private String itemName;
		private CommandEnum property;

		public SamsungAcBindingConfig(String acInstance, CommandEnum property) {
			this.acInstance = acInstance;
			this.property = property;
		}

		public SamsungAcBindingConfig(String acInstance, String itemName,
				CommandEnum property) {
			this.acInstance = acInstance;
			this.property = property;
			this.itemName = itemName;
		}

		public String getSamsungAcInstance() {
			return acInstance;
		}

		public CommandEnum getProperty() {
			return property;
		}

		public String getItemName() {
			return itemName;
		}

		public String toString() {
			return " acInstance:" + acInstance + " itemName:" + itemName
					+ " property:" + property;
		}
	}

	public String getAirConditionerInstance(String itemname) {
		SamsungAcBindingConfig bindingConfig = (SamsungAcBindingConfig) bindingConfigs
				.get(itemname);
		return bindingConfig.getSamsungAcInstance();
	}

	public CommandEnum getProperty(String itemname) {
		SamsungAcBindingConfig bindingConfig = (SamsungAcBindingConfig) bindingConfigs
				.get(itemname);
		return bindingConfig.getProperty();
	}
}
