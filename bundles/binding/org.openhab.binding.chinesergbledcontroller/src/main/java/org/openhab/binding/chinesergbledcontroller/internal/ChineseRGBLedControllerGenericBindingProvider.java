/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.chinesergbledcontroller.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openhab.binding.chinesergbledcontroller.ChineseRGBLedControllerBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Konstantin Polihronov
 * @since 1.8.0
 */
public class ChineseRGBLedControllerGenericBindingProvider extends AbstractGenericBindingProvider implements ChineseRGBLedControllerBindingProvider {
	private static final Logger logger = 
			LoggerFactory.getLogger(ChineseRGBLedControllerBinding.class);
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "chinesergbledcontroller";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof ColorItem )) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @throws  
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
	
		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length > 2) {
			throw new BindingConfigParseException("ChineseRGBLedController configuration can contain only one configuration part at max"+configParts);
		}
		ChineseRGBLedControllerBindingConfig config = new ChineseRGBLedControllerBindingConfig();
		
//		item.getName();
		config.item=item;
		if(configParts[0].equals("ip")) {
			try {
				config.IPAddress = InetAddress.getByName(configParts[1]);
				logger.debug("IP address of "+item.getName()+" is "+config.IPAddress.getHostAddress());
			} catch (UnknownHostException e) {
				logger.error("Could not get configuration of "+item+".\n"+e.getMessage());
			}
		} else { 
			logger.error("Wrong item configuration for[{}]. Syntax is ip:<ipaddress of controller>",item);
		}
		addBindingConfig(item, config);		
	}
	
	/**
	 * This returns the IP address of our item
	 * @param itemName
	 * @return
	 */
	public InetAddress getIPAddress(String itemName) {
			ChineseRGBLedControllerBindingConfig config = (ChineseRGBLedControllerBindingConfig) bindingConfigs.get(itemName);
			return config != null ? config.getIPAddress() : null;
	}
	
	public Item getItem(String itemName) {
			ChineseRGBLedControllerBindingConfig config = (ChineseRGBLedControllerBindingConfig) bindingConfigs.get(itemName);
			return config != null ? config.getItem() : null;	
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Konstantin Polihronov
	 * @since 1.8.0
	 */
	private class ChineseRGBLedControllerBindingConfig implements BindingConfig {
		 InetAddress IPAddress;   // the IP of the LED controller 
		 Item item;
		 //TODO optional add port or something else if necessary

		public Item getItem() {
			return item;
		}

		public InetAddress getIPAddress() {
			return IPAddress;
		}
	}
	
	
}
