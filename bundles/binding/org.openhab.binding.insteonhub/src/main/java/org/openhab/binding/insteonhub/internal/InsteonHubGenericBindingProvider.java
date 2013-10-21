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
package org.openhab.binding.insteonhub.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.insteonhub.InsteonHubBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubGenericBindingProvider extends
		AbstractGenericBindingProvider implements InsteonHubBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "insteonhub";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		if (bindingConfig != null) {
			// parse configuration
			Map<String, String> props = parseProperties(bindingConfig);
			String hubid = props.get(InsteonHubBindingConfig.KEY_HUB_ID);
			String device = props.get(InsteonHubBindingConfig.KEY_DEVICE);
			String bindingType = props
					.get(InsteonHubBindingConfig.KEY_BINDING_TYPE);
			// create configuration
			InsteonHubBindingConfig config = new InsteonHubBindingConfig(hubid,
					device, bindingType, item.getName());
			// add binding configuration
			addBindingConfig(item, config);
		}
	}

	private static Map<String, String> parseProperties(String config) {
		Map<String, String> props = new HashMap<String, String>();
		String[] tokens = config.trim().split(",");
		for (String token : tokens) {
			token = token.trim();
			String[] confStatement = token.split("=");
			String key = confStatement[0];
			String value = confStatement[1];
			props.put(key, value);
		}
		return props;
	}

	@Override
	public InsteonHubBindingConfig getConfigForItem(String itemName) {
		return ((InsteonHubBindingConfig) bindingConfigs.get(itemName));
	}

	@Override
	public void getConfigsForHub(String hubId,
			Map<String, InsteonHubBindingConfig> configs) {
		for (BindingConfig config : bindingConfigs.values()) {
			InsteonHubBindingConfig insteonConfig = (InsteonHubBindingConfig) config;
			if (insteonConfig.getHubId().equals(hubId)) {
				configs.put(insteonConfig.getItemName(), insteonConfig);
			}
		}
	}

	@Override
	public InsteonHubBindingConfig getConfigForHubDevice(String hubId,
			String device) {
		for (BindingConfig config : bindingConfigs.values()) {
			InsteonHubBindingConfig insteonConfig = (InsteonHubBindingConfig) config;
			if (insteonConfig.getHubId().equals(hubId)
					&& insteonConfig.getDevice().equals(device)) {
				return insteonConfig;
			}
		}
		return null;
	}

}
