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
package org.openhab.binding.milight.internal;


import org.openhab.binding.milight.MilightBindingProvider;
import org.openhab.binding.milight.internal.MilightBindingConfig.BindingType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format. It
 * registers as a {@link MilightBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{milight="livingroom;1"}</code> - Connects to bulb 1 and switches or dims the bulb without changing the color.
 * </li>
 * <li>
 * <code>{milight="livingroom;1;colorTemperature"} - Connects to bulb 1 and dims the bulbs color temperature without changing the brightness.</code>
 * </ul>
 * 
 * @author Hans-Joerg Merk
 * @author Kai Kreuzer
 * @since 1.3.0
 */

public class MilightGenericBindingProvider extends AbstractGenericBindingProvider implements MilightBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(MilightGenericBindingProvider.class);

	@Override
	public String getBindingType() {
		return "milight";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof ColorItem)) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems, DimmerItems and ColorItems are allowed - please check your *.items configuration");
		}

	}

	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		try {
			if (bindingConfig != null) {
				String[] configParts = bindingConfig.split(";");
				if (configParts.length > 3) {
					throw new BindingConfigParseException("milight binding configuration must not have more than three parts");
				}

				if (item instanceof ColorItem) {
					BindingConfig milightBindingConfig = (BindingConfig) new MilightBindingConfig(
							configParts[0], configParts[1], BindingType.rgb.name());
					addBindingConfig(item, milightBindingConfig);
				} else if (item instanceof DimmerItem || item instanceof SwitchItem) {
					BindingConfig milightBindingConfig = (BindingConfig) new MilightBindingConfig(
							configParts[0], configParts[1], configParts.length < 3 ? null : configParts[2]);
					addBindingConfig(item, milightBindingConfig);
				}

			} else {
				logger.warn("bindingConfig is NULL (item=" + item
						+ ") -> processing bindingConfig aborted!");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.warn("bindingConfig is invalid (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}

	}

	@Override
	public MilightBindingConfig getItemConfig(String itemName) {
		return (MilightBindingConfig) bindingConfigs.get(itemName);
	}

}
