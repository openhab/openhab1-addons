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
 * Switch Person_A	"Person A"	{ mqttitude="<broker>:<topic>" }
 * Switch Person_B  "Person B"	{ mqttitude="mosquitto:/mqttitude/personB" }
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
