/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jointspace.internal;

import java.util.HashMap;

import org.openhab.binding.jointspace.JointSpaceBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.ColorItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


import org.apache.commons.lang.StringUtils;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author „Lenzebo“
 * @since 1.5.0
 */
public class JointSpaceGenericBindingProvider extends AbstractGenericBindingProvider implements JointSpaceBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "jointspace";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof ColorItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, Color- and DimmerItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		jointSpaceBindingConfig config = new jointSpaceBindingConfig();
		
		parseBindingConfig(bindingConfig, config);
		
		addBindingConfig(item, config);		
	}
	
	protected void parseBindingConfig(String bindingConfigs,
			jointSpaceBindingConfig config) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs, ",");

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length != 3) {
			throw new BindingConfigParseException(
					"JointSpace binding must contain two parts separated by ':', e.g. <type>:<command>:<button>");
		}

		
		String command = StringUtils.trim(configParts[0]);
		String device_id = StringUtils.trim(configParts[1]);
		String tvCommand = StringUtils.trim(configParts[2]);


		// if there are more commands to parse do that recursively ...
		if (StringUtils.isNotBlank(bindingConfigTail)) {
			parseBindingConfig(bindingConfigTail, config);
		}

		config.put(command, tvCommand);
	}
	
	@Override
	public String getTVCommand(String itemName, String command) {
		jointSpaceBindingConfig config = (jointSpaceBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command) : null;
	}
	
	
	class jointSpaceBindingConfig extends HashMap<String, String> implements BindingConfig {

		/**
		 * generated serial version uid
		 */
		private static final long serialVersionUID = -1723443134323559493L;
		// put member fields here which holds the parsed values
	}

	
}
