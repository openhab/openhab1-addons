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
package org.openhab.binding.piface.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.piface.PifaceBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class can parse information from the generic binding format and 
 * provides Piface binding information from it. It registers as a 
 * {@link PifaceBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ piface="piface-id:OUT:1" }</code></li>
 * 	<li><code>{ piface="piface-id:IN:6" }</code></li>
 * </ul>
 * 
 * @author Ben Jones
 * 
 * @since 1.3.0
 */
public class PifaceGenericBindingProvider extends AbstractGenericBindingProvider implements PifaceBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(PifaceGenericBindingProvider.class);
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "piface";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem) && !(item instanceof ContactItem)) {
			throw new BindingConfigParseException("Item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch and Contact items are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			PifaceBindingConfig pin = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, pin);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> process bindingConfig aborted!");
		}
	}
	
	protected PifaceBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		String[] parts = bindingConfig.split(":");
		if (parts.length < 2) {
			throw new BindingConfigParseException("Item '" + item.getName() + "' has an invalid binding config - expecting at least 2 tokens (<id>:<type>[:<pin>]) but found " + bindingConfig);
		}
		
		String pifaceId = parts[0];
		PifaceBindingConfig.BindingType bindingType = PifaceBindingConfig.BindingType.parse(parts[1]);
		
		Integer pinNumber = 0;
		if (!bindingType.equals(PifaceBindingConfig.BindingType.WATCHDOG)) {
			if (parts.length != 3) {
				throw new BindingConfigParseException("Item '" + item.getName() + "' has an invalid binding config - expecting at 3 tokens (<id>:IN|OUT:<pin>) but found " + bindingConfig);
			}
			
			pinNumber = Integer.parseInt(parts[2]);
		}
		
		return new PifaceBindingConfig(pifaceId, bindingType, pinNumber, item.getClass());
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		PifaceBindingConfig pin = (PifaceBindingConfig) bindingConfigs.get(itemName);
		return pin != null ? pin.getItemType() : null;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public PifaceBindingConfig getPifaceBindingConfig(String itemName) {
		return (PifaceBindingConfig) bindingConfigs.get(itemName);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getItemNames(String pifaceId, PifaceBindingConfig.BindingType bindingType, int pinNumber) {
		List<String> itemNames = new ArrayList<String>();
		for (String itemName : getItemNames()) {
			PifaceBindingConfig bindingConfig = (PifaceBindingConfig) bindingConfigs.get(itemName);
			if (bindingConfig.getPifaceId().equals(pifaceId) && bindingConfig.getBindingType() == bindingType && bindingConfig.getPinNumber() == pinNumber) {
				itemNames.add(itemName);
			}
		}
		return itemNames;
	}
}
