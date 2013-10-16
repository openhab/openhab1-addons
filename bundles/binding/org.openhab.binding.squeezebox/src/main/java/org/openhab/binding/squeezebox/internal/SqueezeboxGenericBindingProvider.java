/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.squeezebox.SqueezeboxBindingProvider;
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
 * provides Squeezebox binding information from it. It registers as a 
 * {@link SqueezeboxBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * 
 * <ul>
 * 	<li><code>{ squeeze="STRING:player1:title" }</code> - shows the current title of the player named 'player1'</li>
 * 	<li><code>{ squeeze="ONOFF:player1:isMuted" }</code> - shows if  named 'player1' is muted or not</li>
 * 	<li><code>{ squeeze="ONOFF:player1:isPlaying" }</code> - shows if  named 'player1' is playing or not</li>
 * 	<li><code>{ squeeze="ONOFF:player1:isStopped" }</code> - shows if  named 'player1' is stopped or not</li>
 * 	<li><code>{ squeeze="ONOFF:player1:isPaused" }</code> - shows if  named 'player1' is paused or not</li>
 * 	<li><code>{ squeeze="ONOFF:player1:isPowered" }</code> - shows if  named 'player1' is powered on or off</li>
 * 	<li><code>{ squeeze="PERCENT:player1:volume" }</code> - shows the current volume of the player named 'player1'</li>
 * 	<li><code>{ squeeze="INCREASE:player1:volume_increase, DECREASE:player1:volume_decrease" }</code> - increases or decreases the volume of the player named 'player1'</li>
 * 	<li><code>{ squeeze="ON:player1:powerOn, OFF:player1:powerOff" }</code> - switch the player named 'player1' on or off</li>
 * 	<li><code>{ squeeze="ON:player1:mute, OFF:player1:unmute" }</code> - switch the mute state of player named 'player1'</li>
 * 	<li><code>{ squeeze="1:player1:http=somestream.net, 2:player1:file=/somemusic.mp3" }</code> - play the specified http stream or file on player named 'player1'</li>
 * 	<li><code>{ squeeze="ON:player1:play" }</code> - play the next item on playlist bound to player named 'player1'</li>
 * 	<li><code>{ squeeze="ON:player1:stop" }</code> - stop the current song on player named 'player1'</li>
 * 	<li><code>{ squeeze="ON:player1:stop" }</code> - pause the current song on player named 'player1'</li>
 * </ul>
 * 
 * @author Markus Wolters
 * @since 1.3.0
 */
public class SqueezeboxGenericBindingProvider extends AbstractGenericBindingProvider implements SqueezeboxBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "squeeze";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof StringItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, Dimmer-, String- and NumberItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		
		super.processBindingConfiguration(context, item, bindingConfig);
		
		SqueezeBindingConfig config = new SqueezeBindingConfig();
		parseBindingConfig(bindingConfig, config);
		addBindingConfig(item, config);		
	}
	
	protected void parseBindingConfig(String bindingConfigs, SqueezeBindingConfig config) throws BindingConfigParseException {
		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs, ",");

		String[] configParts = bindingConfig.split(":");
		if (configParts.length < 3) {
			throw new BindingConfigParseException("Squeezebox binding configuration must consist of three parts [config=" + configParts + "]");
		}

		String command = StringUtils.trim(configParts[0]);
		String playerId = StringUtils.trim(configParts[1]);
		String playerCommand = StringUtils.trim(configParts[2]);
		
		for (int i = 3; i < configParts.length; i++) {
			playerCommand += ":" + StringUtils.trim(configParts[i]);
		}
		
		// if there are more commands to parse do that recursively ...
		if (StringUtils.isNotBlank(bindingConfigTail)) {
			parseBindingConfig(bindingConfigTail, config);
		}
				
		config.put(command, playerId + ":" + playerCommand);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getPlayerCommand(String itemName, String command) {
		final String percent = "PERCENT";
		
		String retVal = null;
		
		SqueezeBindingConfig config = (SqueezeBindingConfig) bindingConfigs.get(itemName);
		if (config != null) {
			retVal = config.get(command);
			if ((retVal == null) && (config.containsKey(percent)) && (0 <= Byte.parseByte(command))) {
				retVal = config.get(percent) + "=" + command;
			}
		}
		
		return retVal;
	}
	
	public String getPlayerByItemnameAndCommand(String itemName, String command) {
		final String divider = ":";
		String retVal = "";
		
		if (null != bindingConfigs)
		{
			SqueezeBindingConfig config = (SqueezeBindingConfig) bindingConfigs.get(itemName);
			for (Map.Entry<String, String> entry : config.entrySet()) {
				if (true == entry.getValue().contains(command)) {
					retVal = entry.getValue().substring(0, entry.getValue().indexOf(divider));
					break;
				}
			}
		}
		
		return retVal;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String[] getItemNamesByPlayerAndPlayerCommand(String playerId, PlayerCommandTypeMapping playerCommand) {
		final String percent = "PERCENT";
		final String string = "STRING";
		final String onoff = "ONOFF";
		final String divider = ":";
		
		Set<String> itemNames = new HashSet<String>();
		for (String itemName : bindingConfigs.keySet()) {
			SqueezeBindingConfig squeezeConfig = (SqueezeBindingConfig) bindingConfigs.get(itemName);
			if (squeezeConfig.containsKey(percent) && PlayerCommandTypeMapping.VOLUME.equals(playerCommand) && squeezeConfig.get(percent).contains(playerId)) {
				itemNames.add(itemName);
			}
			else if (squeezeConfig.containsKey(string)) {
				String value = squeezeConfig.get(string);
				if ((value.substring(value.indexOf(divider) + 1).compareTo(playerCommand.command) == 0) && (value.contains(playerId))) {
					itemNames.add(itemName);					
				}
			}
			else if (squeezeConfig.containsKey(onoff)) {
				String value = squeezeConfig.get(onoff);
				if ((value.substring(value.indexOf(divider) + 1).compareTo(playerCommand.command) == 0) && (value.contains(playerId))) {
					itemNames.add(itemName);					
				}	
			}
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the Squeezebox binding 
	 * provider.
	 */
	static class SqueezeBindingConfig extends HashMap<String, String> implements BindingConfig {
        /** generated serialVersion UID */
		private static final long serialVersionUID = 6164971643530954095L;
	}
}
