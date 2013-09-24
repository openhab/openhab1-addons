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
package org.openhab.binding.tcpsimple.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.tcpsimple.TCPSimpleBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public class TCPSimpleGenericBindingProvider extends
		AbstractGenericBindingProvider implements TCPSimpleBindingProvider {
	static final Logger logger = LoggerFactory
			.getLogger(TCPSimpleGenericBindingProvider.class);

	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern BASE_CONFIG_PATTERN = Pattern
			.compile("([<|>|\\*]\\[.*?\\])*");

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern IN_BINDING_PATTERN = Pattern
			.compile("<\\[([0-9.a-zA-Z]+)\\]");

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern OUT_BINDING_PATTERN = Pattern
			.compile(">\\[([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+)\\]");

	/** {@link Pattern} which matches an Gen-Binding */
	private static final Pattern GEN_BINDING_PATTERN = Pattern
			.compile("\\*\\[([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+)\\]");

	/**
	 * Artificial command for the snmp-in configuration
	 */
	protected static final Command IN_BINDING_KEY = StringType.valueOf("IN_BINDING");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "tcpsimple";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			TCPSimpleBindingConfig newConfig = new TCPSimpleBindingConfig();
			Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);

			if (!matcher.matches()) {
				throw new BindingConfigParseException("bindingConfig '"
						+ bindingConfig
						+ "' doesn't contain a valid binding configuration");
			}
			matcher.reset();

			while (matcher.find()) {
				String bindingConfigPart = matcher.group(1);
				if (StringUtils.isNotBlank(bindingConfigPart)) {
					parseBindingConfig(newConfig, item, bindingConfigPart);
				}
			}

			addBindingConfig(item, newConfig);
		} else {
			logger.warn("bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}

	private void parseBindingConfig(TCPSimpleBindingConfig config, Item item,
			String bindingConfig) throws BindingConfigParseException {

		config.itemType = item.getClass();

		if (bindingConfig != null) {
			Matcher inMatcher = IN_BINDING_PATTERN.matcher(bindingConfig);
			Matcher outMatcher = OUT_BINDING_PATTERN.matcher(bindingConfig);
			Matcher genMatcher = GEN_BINDING_PATTERN.matcher(bindingConfig);

			if (!outMatcher.matches() && !inMatcher.matches() && !genMatcher.matches()) {
				throw new BindingConfigParseException(
						getBindingType()
								+ " binding configuration must consist of four [config="
								+ inMatcher + "] or five parts [config="
								+ outMatcher + "]");
			} else {
				TCPSimpleBindingConfigElement newElement = new TCPSimpleBindingConfigElement();
				if (outMatcher.matches()) {
					String commandAsString = outMatcher.group(1).toString();

					newElement.name = inMatcher.group(1).toString();
					// Only Integer commands accepted at this time.
					newElement.value = new Integer(Integer.parseInt(outMatcher
							.group(2).toString()));

					Command command = TypeParser.parseCommand(
							item.getAcceptedCommandTypes(), commandAsString);
					if (command == null) {
						logger.error(
								"SNMP can't resolve command {} for item {}",
								commandAsString, item);
					} else {
						config.put(command, newElement);
					}
				} else if (inMatcher.matches()) {
					newElement.name = inMatcher.group(1).toString();

					config.put(IN_BINDING_KEY, newElement);
				} else if (genMatcher.matches()) {
					config.connector = genMatcher.group(1).toString();
					config.addressName = genMatcher.group(2).toString();
					config.addressValue = genMatcher.group(3).toString();
				}
			}
		} else {
			return;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAddress(String itemName) {
		TCPSimpleBindingConfig config = (TCPSimpleBindingConfig) bindingConfigs.get(itemName);
		if(config == null)
			return null;
		return config.addressValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAddressName(String itemName) {
		TCPSimpleBindingConfig config = (TCPSimpleBindingConfig) bindingConfigs.get(itemName);
		if(config == null)
			return null;
		return config.addressName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getInBindingVariable(String itemName) {
		TCPSimpleBindingConfig config = (TCPSimpleBindingConfig) bindingConfigs.get(itemName);
		if(config == null)
			return null;
		return config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).name : null;
	}


	public List<String> getInBindingItemNamesForConnector(String connector) {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			TCPSimpleBindingConfig tcpConfig = (TCPSimpleBindingConfig) bindingConfigs.get(itemName);
			if (tcpConfig.containsKey(IN_BINDING_KEY) && tcpConfig.connector.equals(connector)) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		TCPSimpleBindingConfig config = (TCPSimpleBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	static class TCPSimpleBindingConfig extends
			HashMap<Command, TCPSimpleBindingConfigElement> implements
			BindingConfig {

		private static final long serialVersionUID = 4697146075427676116L;
		Class<? extends Item> itemType;
		String connector;
		String addressName;
		String addressValue;
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the TCP binding
	 * provider.
	 */
	static class TCPSimpleBindingConfigElement implements BindingConfig {
		String name;
		Integer value;
	}
}
