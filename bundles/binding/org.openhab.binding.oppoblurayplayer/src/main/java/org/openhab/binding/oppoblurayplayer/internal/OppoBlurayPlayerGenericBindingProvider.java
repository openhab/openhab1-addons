/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.oppoblurayplayer.OppoBlurayPlayerBindingProvider;
import org.openhab.binding.oppoblurayplayer.internal.core.OppoBlurayPlayerNoMatchingItemException;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * <p>
 * This class can parse information from the generic binding format and provides
 * Oppo Bluray player device binding information from it.
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>oppoblurayplayer="*hometheater:Power"</code></li>
 * <li><code>oppoblurayplayer="*hometheatre:PlayBackStatus"</code></li>
 * <li><code>oppoblurayplayer="*hometheatre:VolumeLevel"</code></li>
 * <li><code>oppoblurayplayer="*hometheatre:TrayPosition"</code></li>
 * <li><code>oppoblurayplayer="*hometheatre:Mute"</code></li>
 * <li><code>oppoblurayplayer="*hometheatre:VerboseMode"</code></li>
 * <li><code>oppoblurayplayer="hometheatre:DiscType"</code></li>
 * <li><code>oppoblurayplayer="hometheatre:HdmiResolution"</code></li>
 * <li><code>oppoblurayplayer="*hometheater:Source"</code></li>
 * </ul>
 * 
 * @author  (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerGenericBindingProvider extends AbstractGenericBindingProvider implements OppoBlurayPlayerBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "oppoblurayplayer";
	}

	private static final Logger logger = LoggerFactory.getLogger(OppoBlurayPlayerGenericBindingProvider.class);


	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItem, NumberItem and StringItem are allowed - please check your *.items configuration");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		OppoBlurayPlayerBindingConfig config = new OppoBlurayPlayerBindingConfig();

		String[] configParts = bindingConfig.trim().split(":");

		config.inBinding = true;
		config.outBinding = true;
		config.itemType = item.getClass();
		config.itemName = item.getName();

			
		if (configParts.length != 2) {
			throw new BindingConfigParseException(
					"Oppo Bluray player bi-directional binding must contain 2 parts separated by ':'");
		}

		config.deviceID = configParts[0].trim();

		config.commandType = getCommandTypeFromString(configParts[1].trim(), item);

		addBindingConfig(item, config);
		logger.debug("Binding "+ getBindingType()+" has item "+item+" with config {}", config.toString());
	}

	private OppoBlurayPlayerCommandType getCommandTypeFromString(String commandTypeString, Item item) throws BindingConfigParseException {
		
		OppoBlurayPlayerCommandType commandType = null;
		
		try {
			OppoBlurayPlayerCommandType.validateBinding(commandTypeString, item.getClass());

			commandType = OppoBlurayPlayerCommandType.getCommandType(commandTypeString);

		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("Invalid command type '"
					+ commandTypeString + "'!");

		} catch (InvalidClassException e) {
			throw new BindingConfigParseException(
					"Invalid item type for command type '" + commandTypeString
							+ "'!");

		}
		
		return commandType;
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		OppoBlurayPlayerBindingConfig config = (OppoBlurayPlayerBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public String getDeviceId(String itemName) {
		OppoBlurayPlayerBindingConfig config = (OppoBlurayPlayerBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.deviceID : null;
	}

	@Override
	public OppoBlurayPlayerCommandType getCommandType(String itemName) {
		OppoBlurayPlayerBindingConfig config = (OppoBlurayPlayerBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.commandType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInBinding(String itemName) {
		OppoBlurayPlayerBindingConfig config = (OppoBlurayPlayerBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.inBinding: null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOutBinding(String itemName) {
		OppoBlurayPlayerBindingConfig config = (OppoBlurayPlayerBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.outBinding: null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			OppoBlurayPlayerBindingConfig config = (OppoBlurayPlayerBindingConfig) bindingConfigs.get(itemName);
			if (config.inBinding == true) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}
	
	@Override
	public String findFirstMatchingBindingItemName(String playerName, OppoBlurayPlayerCommand command) {
		for (Entry<String, BindingConfig> config : bindingConfigs.entrySet()){
			BindingConfig bindingConfig = config.getValue();
			if (bindingConfig instanceof OppoBlurayPlayerBindingConfig){
				if (((OppoBlurayPlayerBindingConfig) bindingConfig).commandType.equals(command.getOppoBlurayPlayerCommandType())){
					return config.getKey();
				}
			}
		}
		return null;
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the Oppo Bluray Player
	 * binding provider.
	 */
	static class OppoBlurayPlayerBindingConfig implements BindingConfig {

		public Class<? extends Item> itemType = null;
		public String deviceID = null;
		public String itemName = null;
		public OppoBlurayPlayerCommandType commandType = null;
		public boolean inBinding = true;
		public boolean outBinding = true;
		
		@Override
		public String toString() {
			return "ExecBindingConfigElement ["
					+ ", itemType=" + itemType
					+ ", itemName=" + itemName
					+ ", deviceID=" + deviceID
					+ ", commandType=" + commandType
					+ ", inBinding=" + inBinding + "]"
					+ ", outBinding=" + outBinding + "]";
		}

	}

	
}
