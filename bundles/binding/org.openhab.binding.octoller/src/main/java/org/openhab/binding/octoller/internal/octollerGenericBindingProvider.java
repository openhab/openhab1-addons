/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 

package org.openhab.binding.octoller.internal;

import org.openhab.binding.octoller.octollerBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * For openHAB binding for octoller (www.octoller.com)
 *  
 * @author JPlenert
 * @since 1.5.1
 */
public class octollerGenericBindingProvider extends AbstractGenericBindingProvider implements octollerBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "octoller";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		String[] configParts = bindingConfig.trim().split("#");
		if (configParts.length != 2)
			throw new BindingConfigParseException("Two parameters needed (Host#Block)");
		
		// Right now a gateway must be given!
		String[] hostParts = configParts[0].trim().split("@");
		if (hostParts.length != 2)
			throw new BindingConfigParseException("Host paramter must contain device and gateway (deviceHost@gatewayHost)");
		
		octollerBindingConfig config = new octollerBindingConfig();
		config.DeviceHost = hostParts[0].trim();
		config.GatewayHost = hostParts[1].trim();
		// Check for BlockName
		if (configParts[1].length() > 2 && configParts[1].startsWith("'") && configParts[1].endsWith("'"))
			config.BlockName = configParts[1].substring(1, configParts[1].length()-1).trim();
		else
		{
			try
			{
				config.BlockID = Integer.parseInt(configParts[1]);
			}
			catch(Exception ex)
			{
				throw new BindingConfigParseException("Block parameter must be nummeric or the block name in single quotation marks");
			}
			
		}
		
		addBindingConfig(item, config);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public octollerBindingConfig getConfig(String itemName) {
		return (octollerBindingConfig) bindingConfigs.get(itemName);
	}	
}
