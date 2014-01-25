/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqttitude.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Describes the item binding configuration for a Mqttitude phone/user
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class MqttitudeItemConfig implements BindingConfig {	
	private final String itemName;
	private final String broker;
	private final String topic;
	private final String region;

	public MqttitudeItemConfig(String itemName, String bindingConfig) throws BindingConfigParseException {
		
		String[] config = bindingConfig.split(":");
		
		if (config.length < 2)
			throw new BindingConfigParseException("Invalid Mqttitude binding configuration '" + bindingConfig + "' for item " + itemName + ". Expecting '<broker>:<topic>[:<region>]'.");
		
		this.itemName = itemName;
		this.broker = StringUtils.trim(config[0]);
		this.topic = StringUtils.trim(config[1]);
		this.region = config.length > 2 ? StringUtils.trim(config[2]) : null;
	}

	public String getItemName() {
		return itemName;
	}
	
	public String getBroker() {
		return broker;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public String getRegion() {
		return region;
	}
}
