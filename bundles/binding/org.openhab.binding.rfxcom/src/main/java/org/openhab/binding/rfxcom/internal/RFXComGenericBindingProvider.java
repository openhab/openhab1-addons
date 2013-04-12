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
package org.openhab.binding.rfxcom.internal;

import java.io.InvalidClassException;

import org.openhab.binding.rfxcom.RFXComBindingProvider;
import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;
import org.openhab.binding.rfxcom.internal.messages.RFXComMessageUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * RFXCOM device binding information from it.
 * </p>
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <p>
 * <code>
 * in:  rfxcom:"&lt;DeviceId:ValueSelector"
 * <p>
 * out: rfxcom:"&gt;DeviceId:PacketType.SubType:ValueSelector"
 * </code>
 * <p>
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>rfxcom="<2264:Temperature"</code></li>
 * <li><code>rfxcom="<2264:Humidity"</code></li>
 * <li><code>rfxcom="<635602.1:Command"</code></li>
 * <li><code>rfxcom="<635602.2:Command"</code></li>
 * <li><code>rfxcom">635602.1:LIGHTING2.AC:Command"</code></li>
 * </ul>
 * 
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComGenericBindingProvider extends
		AbstractGenericBindingProvider implements RFXComBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "rfxcom";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		RFXComBindingConfig config = new RFXComBindingConfig();

		String valueSelectorString = null;
		
		if (bindingConfig.startsWith("<")) {

			String[] configParts = bindingConfig.trim().split(":");

			if (configParts.length != 2) {
				throw new BindingConfigParseException(
						"RFXCOM binding must contain two parts separated by ':'");
			}

			config.id = configParts[0].trim().replace("<", "");
			config.inBinding = true;

			valueSelectorString = configParts[1].trim();
			

		} else if (bindingConfig.startsWith(">")) {
			String[] configParts = bindingConfig.trim().split(":");

			config.id = configParts[0].trim().replace(">", "");
			config.inBinding = false;

			String[] types = configParts[1].trim().split("\\.");

			if (types.length != 2) {
				throw new BindingConfigParseException(
						"RFXCOM out binding second field should contain 2 parts separated by '.'");
			}

			try {
				config.packetType = RFXComMessageUtils.convertPacketType(types[0]
						.trim());

			} catch (Exception e) {
				throw new BindingConfigParseException("Invalid packet type '"
						+ types[0] + "'!");
			}

			try {
				config.subType = RFXComMessageUtils.convertSubType(config.packetType,
						types[1].trim());

			} catch (Exception e) {
				throw new BindingConfigParseException("Invalid sub type '"
						+ types[1] + "'!");
			}

			valueSelectorString = configParts[2].trim();

		} else {
			throw new BindingConfigParseException(
					"RFXCOM binding should start < or > character!");
		}
		
		try {

			RFXComValueSelector.validateBinding(valueSelectorString,
					item.getClass());

			config.valueSelector = RFXComValueSelector
					.getValueSelector(valueSelectorString);

		} catch (IllegalArgumentException e1) {
			throw new BindingConfigParseException(
					"Invalid value selector '" + valueSelectorString + "'!");

		} catch (InvalidClassException e1) {
			throw new BindingConfigParseException(
					"Invalid item type for value selector '"
							+ valueSelectorString + "'!");

		}

		addBindingConfig(item, config);
	}

	class RFXComBindingConfig implements BindingConfig {
		String id;
		RFXComValueSelector valueSelector;
		boolean inBinding;
		PacketType packetType;
		Object subType;

	}

	@Override
	public String getId(String itemName) {
		RFXComBindingConfig config = (RFXComBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.id : null;
	}

	@Override
	public RFXComValueSelector getValueSelector(String itemName) {
		RFXComBindingConfig config = (RFXComBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.valueSelector : null;
	}

	@Override
	public boolean isInBinding(String itemName) {
		RFXComBindingConfig config = (RFXComBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.inBinding : null;
	}

	@Override
	public PacketType getPacketType(String itemName) {
		RFXComBindingConfig config = (RFXComBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.packetType : null;
	}

	@Override
	public Object getSubType(String itemName) {
		RFXComBindingConfig config = (RFXComBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.subType : null;
	}

}
