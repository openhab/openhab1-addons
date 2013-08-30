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
package org.openhab.binding.fritzaha.internal;

import org.openhab.binding.fritzaha.FritzahaBindingProvider;
import org.openhab.binding.fritzaha.internal.hardware.devices.FritzahaQueryscriptMeter;
import org.openhab.binding.fritzaha.internal.hardware.devices.FritzahaQueryscriptSwitch;
import org.openhab.binding.fritzaha.internal.hardware.devices.FritzahaWebserviceMeter;
import org.openhab.binding.fritzaha.internal.hardware.devices.FritzahaWebserviceSwitch;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaDevice;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaOutletMeter.MeterType;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaGenericBindingProvider extends AbstractGenericBindingProvider implements BindingConfigReader,
		FritzahaBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(FritzahaGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "fritzaha";
	}

	/**
	 * @{inheritDoc
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
					+ item.getClass().getSimpleName()
					+ "', only Switch- and NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		FritzahaDevice config = null;
		String[] configParts = bindingConfig.trim().split(",");
		if (configParts.length < 2) {
			throw new BindingConfigParseException("FritzAHA items must start with <hostID>,<deviceID/AIN>");
		}
		if (item instanceof SwitchItem) {
			if (configParts.length != 2) {
				throw new BindingConfigParseException("FritzAHA switches must be of format <hostID>,<deviceID/AIN>");
			}
			if (configParts[1].length() > 8) {
				config = new FritzahaWebserviceSwitch(configParts[0], configParts[1]);
			} else {
				config = new FritzahaQueryscriptSwitch(configParts[0], configParts[1]);
			}
		} else if (item instanceof NumberItem) {
			if (configParts.length != 3) {
				throw new BindingConfigParseException(
						"FritzAHA meters must be of format <hostID>,<deviceID/AIN>,<valueToMeasure>");
			} else if (configParts[1].length() > 8) {
				if ("power".equalsIgnoreCase(configParts[2])) {
					config = new FritzahaWebserviceMeter(configParts[0], configParts[1], MeterType.POWER);
				} else if ("energy".equalsIgnoreCase(configParts[2])) {
					config = new FritzahaWebserviceMeter(configParts[0], configParts[1], MeterType.ENERGY);
				} else {
					logger.warn("Could not configure item " + item + " - Unsupported meter type for webservice");
					return;
				}
			} else {
				if ("voltage".equalsIgnoreCase(configParts[2])) {
					config = new FritzahaQueryscriptMeter(configParts[0], configParts[1], MeterType.VOLTAGE);
				} else if ("current".equalsIgnoreCase(configParts[2])) {
					config = new FritzahaQueryscriptMeter(configParts[0], configParts[1], MeterType.CURRENT);
				} else if ("power".equalsIgnoreCase(configParts[2])) {
					config = new FritzahaQueryscriptMeter(configParts[0], configParts[1], MeterType.POWER);
				} else {
					logger.warn("Could not configure item " + item + " - Unsupported meter type for query script");
					return;
				}
			}
		} else {
			logger.warn("Could not configure item " + item + " - Unsupported item type");
		}
		if (config != null)
			addBindingConfig(item, config);
		else {
			logger.error("Could not configure item " + item + " - An error occurred");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public FritzahaDevice getDeviceConfig(String itemName) {
		FritzahaDevice config = (FritzahaDevice) bindingConfigs.get(itemName);
		return config;
	}
}
