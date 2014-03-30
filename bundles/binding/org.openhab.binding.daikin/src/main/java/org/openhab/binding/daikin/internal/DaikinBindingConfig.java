/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.daikin.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * The binding configuration for a Daikin controller item.
 *  
 * @author Ben Jones
 * @since 1.5.0
 */
public class DaikinBindingConfig implements BindingConfig {
	private String itemName;
	private String id;
	private DaikinCommandType commandType;
	
	public DaikinBindingConfig(String itemName, String bindingConfig) throws BindingConfigParseException {
		String[] config = bindingConfig.split(":");
		if (config.length != 2)
			throw new BindingConfigParseException("Invalid Daikin binding configuration '" + bindingConfig + "' for item " + itemName + ". Expecting '<id>:<command>'.");
		
		this.itemName = itemName;
		this.id = StringUtils.trim(config[0]);
		this.commandType = DaikinCommandType.fromString(config[1]);
	}

	public String getItemName() {
		return itemName;
	}

	public String getId() {
		return id;
	}

	public DaikinCommandType getCommandType() {
		return commandType;
	}
}
