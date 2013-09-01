/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.tinkerforge.internal;

import org.openhab.binding.tinkerforge.TinkerforgeBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * tinkerforge binding information from it. It registers as a
 * {@link TinkerforgeBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{ tinkerforge="uid=etd" }</code></li>
 * <li><code>{ tinkerforge="uid=etd, subid=temperature"}</code></li>
 * <li><code>{ tinkerforge="name=relay_coffee_machine" }</code></li>
 * </ul>
 * 
 * @author Theo Weiss
 * @since 1.3.0
 */
public class TinkerforgeGenericBindingProvider extends
		AbstractGenericBindingProvider implements TinkerforgeBindingProvider {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(TinkerforgeGenericBindingProvider.class);

	private enum ConfigKey {
		uid, subid, name
	}
	

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "tinkerforge";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// all itemTypes are allowed for now ...
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		
		super.processBindingConfiguration(context, item, bindingConfig);
		// parse bindingconfig here ...
		if (bindingConfig == null) {
			logger.error("got bindingConfig null for item: {}", item.getName());
		} else {
			TinkerforgeBindingConfig config = new TinkerforgeBindingConfig();
			String[] tokens = bindingConfig.trim().split(",");
			for (String token : tokens) {
				token = token.trim();
				logger.debug("token: {}", token);
				String[] confStatement = token.split("=");
				if (confStatement.length != 2) {
					throw new BindingConfigParseException(
							"TinkerforgeGenericBindingProvider:processBindingConfiguration: invalid format, the entry must consist of key=value pair, but value was found."
									+ token);
				} else {
					String key = confStatement[0];
					String value = confStatement[1];
					if (key.equals(ConfigKey.uid.name())) {
						config.setUid(value);
					} else if (key.equals(ConfigKey.subid.name())) {
						config.setSubId(value);
					} else if (key.equals(ConfigKey.name.name())) {
						config.setName(value);
					} else {
						throw new BindingConfigParseException(
								"unknown configuration key: " + key);
					}
				}
			}
			addBindingConfig(item, config);
		}
	}

	@Override
	public String getUid(String itemName) {
		TinkerforgeBindingConfig config = (TinkerforgeBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getUid() : null;
	}

	@Override
	public String getSubId(String itemName) {
		TinkerforgeBindingConfig config = (TinkerforgeBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getSubId() : null;
	}

	@Override
	public Item getItem(String itemName) {
		TinkerforgeBindingConfig config = (TinkerforgeBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getItem() : null;
	}

	@Override
	public String getName(String itemName) {
		TinkerforgeBindingConfig config = (TinkerforgeBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getName() : null;
	}

	/**
	 * This class represents the configuration of an Item that is binded to a
	 * tinkerforge device. It can hold the following information:
	 * 
	 * <ul>
	 * <li>The uid of the tinkerforge device.</li>
	 * <li>The subid of the tinkerforge device.</li>
	 * <li>The symbolic name of the tinkerforge device.</li>
	 * </ul>
	 * 
	 * The configuration must either declare a uid and subid (if it is a
	 * tinkerforge subdevice) or a symbolic device name. The symbolic name must
	 * refer to a name, which has to be configured in the openhab.cfg file.
	 * 
	 * @author Theo Weiss
	 * @since 1.3.0
	 */
	class TinkerforgeBindingConfig implements BindingConfig {
		private String uid;
		private String subId;
		private String name;
		private Item item;

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}

		public String getUid() {
			return uid;
		}

		public String getSubId() {
			return subId;
		}

		public void setSubId(String subId) {
			this.subId = subId;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
