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
package org.openhab.binding.fritzbox.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.fritzbox.FritzboxBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.library.tel.items.CallItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides FritzBox binding information from it. It registers as a 
 * {@link FritzboxBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ fritzbox="inbound" }</code> - receives status updates on incoming calls</li>
 * 	<li><code>{ fritzbox="outbound" }</code> - receives status updates on outgoing calls</li>
 * 	<li><code>{ fritzbox="active" }</code> - receives status updates on active calls</li>
 * </ul>
 * These binding configurations can be used on either SwitchItems or StringItems. For SwitchItems,
 * it will obviously receive ON at the beginning and OFF at the end. StringItems will be filled
 * with the external phone number or an empty string.
 * </p>
 * 
 * @author Kai Kreuzer
 * 
 * @since 0.7.0
 */
public class FritzboxGenericBindingProvider extends AbstractGenericBindingProvider implements FritzboxBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(FritzboxGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "fritzbox";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof CallItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch- and CallItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			FritzboxBindingConfig config = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}
	
	/**
	 * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException if bindingConfig is no valid binding type
	 */
	protected FritzboxBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		if(ArrayUtils.contains(FritzboxBindingProvider.TYPES, bindingConfig)) {
			return new FritzboxBindingConfig(item.getClass(), bindingConfig);
		} else {
			throw new BindingConfigParseException("'" + bindingConfig + "' is no valid binding type");
		}
	}

	public Class<? extends Item> getItemType(String itemName) {
		FritzboxBindingConfig config = (FritzboxBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getItemType() : null;
	}

	public String getType(String itemName) {
		FritzboxBindingConfig config = (FritzboxBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getType() : null;
	}

	public String[] getItemNamesForType(String eventType) {
		Set<String> itemNames = new HashSet<String>();
		for(Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			FritzboxBindingConfig fbConfig = (FritzboxBindingConfig) entry.getValue();
			if(fbConfig.getType().equals(eventType)) {
				itemNames.add(entry.getKey());
			}
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}

	static class FritzboxBindingConfig implements BindingConfig {
		
		final private Class<? extends Item> itemType;
		final private String type;
		
		public FritzboxBindingConfig(Class<? extends Item> itemType, String type) {
			this.itemType = itemType;
			this.type = type;
		}
		
		public Class<? extends Item> getItemType() {
			return itemType;
		}

		public String getType() {
			return type;
		}		
	}

}
