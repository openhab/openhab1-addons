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
package org.openhab.binding.onkyo.internal;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.onkyo.OnkyoBindingProvider;
import org.openhab.binding.onkyo.internal.eiscp.EiscpCommand;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * Onkyo device binding information from it.
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <code>
 * 	onkyo="&lt;openHAB-command>:&lt;device-id>:&lt;device-command>[,&lt;openHAB-command>:&lt;device-id>:&lt;device-command>][,...]"
 * </code>
 * <p>
 * where parts in brackets [] signify an optional information.
 * </p>
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>onkyo="ON:Livingroom:POWER_ON, OFF:Livingroom:POWER_OFF"</code></li>
 * <li><code>onkyo="UP:Livingroom:VOLUME_UP, DOWN:Livingroom:VOLUME_DOWN"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class OnkyoGenericBindingProvider extends AbstractGenericBindingProvider implements OnkyoBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "onkyo";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem 
				|| item instanceof NumberItem
				|| item instanceof DimmerItem
				|| item instanceof RollershutterItem 
				|| item instanceof StringItem)) {
			throw new BindingConfigParseException(
				"item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
				+ "', only SwitchItem, NumberItem, DimmerItem, RollershutterItem and StringItem are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		OnkyoBindingConfig config = new OnkyoBindingConfig();
		parseBindingConfig(bindingConfig, config);
		addBindingConfig(item, config);
	}
	
	protected void parseBindingConfig(String bindingConfigs,
			OnkyoBindingConfig config) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs, ",");

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length != 3) {
			throw new BindingConfigParseException(
				"Onkyo binding must contain three parts separated by ':'");
		}

		String command = StringUtils.trim(configParts[0]);
		String deviceId = StringUtils.trim(configParts[1]);
		String deviceCommand = StringUtils.trim(configParts[2]);

		try {
			EiscpCommand.valueOf(deviceCommand);
		} catch (Exception e) {
			throw new BindingConfigParseException("Unregonized command '" + deviceCommand + "'");
		}
		
		// if there are more commands to parse do that recursively ...
		if (StringUtils.isNotBlank(bindingConfigTail)) {
			parseBindingConfig(bindingConfigTail, config);
		}

		config.put(command, deviceId + ":" + deviceCommand);
	}
	
	@Override
	public String getDeviceCommand(String itemName, String command) {
		OnkyoBindingConfig config = (OnkyoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command) : null;
	}
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the Onkyo
	 * binding provider.
	 */
	static class OnkyoBindingConfig extends HashMap<String, String> implements BindingConfig {

		/** generated serialVersion UID */
		private static final long serialVersionUID = -8702006872563774395L;

	}
	
}
