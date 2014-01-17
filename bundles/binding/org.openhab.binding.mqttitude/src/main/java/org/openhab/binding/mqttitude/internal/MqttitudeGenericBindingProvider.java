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
import org.openhab.binding.mqttitude.MqttitudeBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration. A valid
 * items binding configuration file will look like the following:
 * 
 * <pre>
 * Switch Person_A	"Person A"	{ mqttitude="<broker>:<topic>[:<region>]" }
 * Switch Person_B  "Person B"	{ mqttitude="mosquitto:/mqttitude/personB:home" }
 * </pre>
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class MqttitudeGenericBindingProvider extends AbstractGenericBindingProvider implements MqttitudeBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "mqttitude";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

        if (StringUtils.isEmpty(bindingConfig))
            throw new BindingConfigParseException("Null config for " + item.getName() + " - expecting an Mqttitude topic");
		
		addBindingConfig(item, new MqttitudeItemConfig(item.getName(), bindingConfig));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public MqttitudeItemConfig getItemConfig(String itemName) {
		return (MqttitudeItemConfig) bindingConfigs.get(itemName);
	}
}
