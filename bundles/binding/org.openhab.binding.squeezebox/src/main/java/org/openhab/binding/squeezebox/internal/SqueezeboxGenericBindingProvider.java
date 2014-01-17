/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.squeezebox.SqueezeboxBindingConfig;
import org.openhab.binding.squeezebox.SqueezeboxBindingProvider;
import org.openhab.core.items.Item;
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
 * 	<li><code>{ squeeze="player1:title" }</code> - shows the currently playing title for the player named 'player1'</li>
 * 	<li><code>{ squeeze="player1:artist" }</code> - shows the currently playing artist for the player named 'player1'</li>
 * 	<li><code>{ squeeze="player1:power" }</code> - switch the player named 'player1' on or off</li>
 * 	<li><code>{ squeeze="player1:volume" }</code> - adjusts the volume of the player named 'player1'</li>
 * 	<li><code>{ squeeze="player1:mute" }</code> - switch the mute state of player named 'player1'</li>
 * 	<li><code>{ squeeze="player1:play" }</code> - play the next item on playlist bound to player named 'player1'</li>
 * 	<li><code>{ squeeze="player1:pause" }</code> - pause the current song on player named 'player1'</li>
 * 	<li><code>{ squeeze="player1:stop" }</code> - stop the current song on player named 'player1'</li>
 * 	<li><code>{ squeeze="player1:sync:player2" }</code> - synchronise 'player2' with 'player1'</li>
 * </ul>
 * 
 * @author Markus Wolters
 * @author Ben Jones
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
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		SqueezeboxBindingConfig config = parseBindingConfig(bindingConfig);
		addBindingConfig(item, config);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SqueezeboxBindingConfig getSqueezeboxBindingConfig(String itemName) {
		return (SqueezeboxBindingConfig) this.bindingConfigs.get(itemName);
	}		

	private SqueezeboxBindingConfig parseBindingConfig(String bindingConfig) throws BindingConfigParseException {
		String[] configParts = bindingConfig.split(":");
		
		if (configParts.length < 2)
			throw new BindingConfigParseException("Squeezebox binding configuration must consist of two parts [config=" + configParts + "]");

		String playerId = StringUtils.trim(configParts[0]);

		String command = StringUtils.trim(configParts[1]);
		CommandType commandType = CommandType.fromString(command);
		
		String extra = null;		
		if (configParts.length > 2)
			extra = configParts[2];

		return new SqueezeboxBindingConfig(playerId, commandType, extra);
	}
}
