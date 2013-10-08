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
package org.openhab.binding.nikobus.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.nikobus.NikobusBindingProvider;
import org.openhab.binding.nikobus.internal.config.AbstractNikobusItemConfig;
import org.openhab.binding.nikobus.internal.config.Button;
import org.openhab.binding.nikobus.internal.config.ModuleChannelGroup;
import org.openhab.binding.nikobus.internal.core.NikobusModule;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the nikobus item binding configuration.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusGenericBindingProvider extends
		AbstractGenericBindingProvider implements NikobusBindingProvider {
	
	private static final Logger log = LoggerFactory.getLogger(NikobusGenericBindingProvider.class);

	private static final String BUTTON_CONFIG_PATTERN = "^#N([A-Z0-9]){6}(:SHORT|:LONG)?"
			+ "(\\[([A-Z0-9]){4}-[12](,[A-Z0-9]{4}-[12])*\\])?" + "$";

	private static final String MODULE_CHANNEL_PATTERN = "^([A-Z0-9]){4}:([1-9]|1[0-2])$";

	private List<NikobusModule> allModules = new ArrayList<NikobusModule>();

	private Map<String, NikobusModule> modules = new HashMap<String, NikobusModule>();

	@Override
	public String getBindingType() {
		return "nikobus";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("Item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', which is not supported by the Nikobus Binding.");
		}
	}

	@Override
	public AbstractNikobusItemConfig getItemConfig(String itemName) {
		return (AbstractNikobusItemConfig) bindingConfigs.get(itemName);
	}

	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);
		String config = (bindingConfig == null) ? "" : bindingConfig.replaceAll(" ", "").toUpperCase();
		log.trace("Binding item: {} with configuration {}", item.getName(), config);

		final AbstractNikobusItemConfig itemBinding = parseItem(item, config);

		addBindingConfig(item, itemBinding);
	}

	/**
	 * Parse an item from the provided configuration string.
	 * 
	 * @param item.getName()
	 *            item name
	 * @param config
	 *            string to parse
	 * @return parsed item
	 * @throws BindingConfigParseException
	 *             if no item could be created
	 */
	private AbstractNikobusItemConfig parseItem(Item item, String config) throws BindingConfigParseException {

		if (config == null || config.trim().length() == 0) {
			throw new BindingConfigParseException("Invalid config for item " + item.getName());
		}

		if (config.matches(BUTTON_CONFIG_PATTERN)) {
			return new Button(item.getName(), config);
		}

		if (config.matches(MODULE_CHANNEL_PATTERN)) {
			String address = config.split(":")[0];
			int channelNum = Integer.parseInt(config.split(":")[1]);
			int group = channelNum > 6 ? 2 : 1;
			String moduleKey = address + "-" + group;
			NikobusModule module = getModule(moduleKey);
			if (module == null) {
				log.trace("Creating channel group {}", moduleKey);
				module = new ModuleChannelGroup(address, group);
				allModules.add(module);
				modules.put(moduleKey, module);
			}
			return ((ModuleChannelGroup) module).addChannel(item.getName(), channelNum, item.getAcceptedCommandTypes());
		}

		throw new BindingConfigParseException("Could not determine item type from config: " + config);
	}

	@Override
	public NikobusModule getModule(String name) {
		return modules.get(name);
	}

	@Override
	public List<NikobusModule> getAllModules() {
		return allModules;
	}
	
}
