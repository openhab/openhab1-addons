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
package org.openhab.binding.systeminfo.internal;

import org.openhab.binding.systeminfo.SysteminfoBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class SysteminfoGenericBindingProvider extends
		AbstractGenericBindingProvider implements SysteminfoBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "systeminfo";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
					+ "', only NumberItem and StringItem are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		SysteminfoBindingConfig config = new SysteminfoBindingConfig();

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length < 2 && configParts.length > 3) {
			throw new BindingConfigParseException("Systeminf binding must contain 2-3 parts separated by ':'");
		}

		String commandType = configParts[0].trim();
		
		try {
			config.commandType = SysteminfoCommandType.getCommandType(commandType);	
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("'" + commandType + "' is not a valid command type" );
		}
		
		try {
			config.refreshInterval = Integer.valueOf(configParts[1]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException("'" + configParts[1] + "' is not a valid refresh interval" );
		}

		if (configParts.length > 2) {
			config.target = configParts[2].trim();
		}

		addBindingConfig(item, config);
	}

	@Override
	public SysteminfoCommandType getCommandType(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.commandType : null;
	}

	@Override
	public Class<? extends Item> getItemType(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public int getRefreshInterval(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.refreshInterval : 0;
	}

	@Override
	public String getTarget(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.target : null;
	}
	
	
	class SysteminfoBindingConfig implements BindingConfig {

		public Class<? extends Item> itemType = null;
		public SysteminfoCommandType commandType;
		public int refreshInterval = 0;
		public String target = null;

		@Override
		public String toString() {
			return "SysteminfoBindingConfigElement [" + ", itemType=" + itemType
					+ ", commandType=" + commandType + ", refreshInterval="
					+ refreshInterval + ", target=" + target + "]";
		}

	}
	
}
