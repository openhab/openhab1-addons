/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pulseaudio.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.pulseaudio.PulseaudioBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>This class can parse information from the generic binding format and 
 * provides Pulseaudio binding information from it. It registers as a 
 * {@link PulseaudioBindingProvider} service as well.</p>
 * 
 * <p>The syntax of the binding configuration strings accepted is the following:<p>
 * <p><code>
 * 	pulseaudio="&lt;sink-name&gt;"
 * </code></p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>pulseaudio="alsa_output.pci-0000_00_1b.0.analog-stereo"</code></li>
 * </ul>
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class PulseaudioGenericBindingProvider extends AbstractGenericBindingProvider implements PulseaudioBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "pulseaudio";
	}

	/**
	 * {@inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if ((item instanceof NumberItem) || (item instanceof StringItem) || (item instanceof SwitchItem) || (item instanceof DimmerItem)) {
			return;
		}
		throw new BindingConfigParseException("item '" + item.getName()
				+ "' is of type '" + item.getClass().getSimpleName()
				+ "', which is not supported by the pulseaudio binding - please check your *.items configuration");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		PulseaudioBindingConfig config = new PulseaudioBindingConfig();
		String[] configParts = bindingConfig.split(":");
		if (item instanceof StringItem) {
			if (configParts.length != 1 && configParts.length!=3) {
				throw new BindingConfigParseException(
						"Pulseaudio binding configuration for StringItems must consist of one or three part [config="
								+ configParts + "]");
			}
			config.serverId = StringUtils.trim(configParts[0]);
			config.itemType = item.getClass();
										
			addBindingConfig(item, config);
			return;
		}
		else if (configParts.length < 2 || configParts.length>3) {
			throw new BindingConfigParseException(
					"Pulseaudio binding configuration must consist of two or three parts [config="
							+ configParts + "]");
		}

		config.serverId = StringUtils.trim(configParts[0]);
		config.itemName = StringUtils.trim(configParts[1]);
		if (configParts.length==3) {
			config.command = StringUtils.trim(configParts[2]);
		}
		config.itemType = item.getClass();
									
		addBindingConfig(item, config);
	}
		
	
	/**
	 * {@inheritDoc}
	 */
	public String getItemName(String itemName) {
		PulseaudioBindingConfig config = (PulseaudioBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemName : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getServerId(String itemName) {
		PulseaudioBindingConfig config = (PulseaudioBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.serverId : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Item> getItemType(String itemName) {
		PulseaudioBindingConfig config = (PulseaudioBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getCommand(String itemName) {
		PulseaudioBindingConfig config = (PulseaudioBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.command : null;
	}
	
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the Pulseaudio binding 
	 * provider.
	 * 
	 * @author Tobias Bräutigam
	 */
	static private class PulseaudioBindingConfig implements BindingConfig {
		
		public String serverId;
		public String itemName;
		public String command;
		public Class<? extends Item> itemType;
		
	}
	
}
