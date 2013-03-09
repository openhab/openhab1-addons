/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.hue.internal;

import org.openhab.binding.hue.HueBindingProvider;
import org.openhab.binding.hue.internal.HueBindingConfig.BindingType;
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
 * registers as a {@link HueBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{hue="1"}</code> - Connects to bulb 1 and switches the bulb without
 * changing the color or brightness.</li>
 * <li>
 * <code>{hue="1;brightness"} - Connects to bulb 1 and dims the bulbs brightness without changing the color. The step size set to default (25).</code>
 * </li>
 * <li>
 * <code>{hue="1;brightness;30"} - Connects to bulb 1 and dims the bulbs brightness without changing the color. The step size is set to 30.</code>
 * </li>
 * <li>
 * <code>{hue="1;colorTemperature"} - Connects to bulb 1 and dims the bulbs color temperature without changing the brightness. The step size set to default (25).</code>
 * </li>
 * <li>
 * <code>{hue="1;colorTemperature;30"} - Connects to bulb 1 and dims the bulbs color temperature without changing the brightness. The step size is set to 30.</code>
 * </li>
 * </ul>
 * 
 * @author Roman Hartmann
 * @since 1.2.0
 */
public class HueGenericBindingProvider extends AbstractGenericBindingProvider
		implements HueBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(HueGenericBindingProvider.class);

	@Override
	public String getBindingType() {
		return "hue";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems, DimmerItems and ColorItems are allowed - please check your *.items configuration");
		}

	}

	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);

		try {

			if (bindingConfig != null) {

				String[] configParts = bindingConfig.split(";");

				if (item instanceof ColorItem) {
					BindingConfig hueBindingConfig = (BindingConfig) new HueBindingConfig(
							configParts[0], BindingType.rgb.name(), null);
					addBindingConfig(item, hueBindingConfig);
				} else if (item instanceof DimmerItem) {
					BindingConfig hueBindingConfig = (BindingConfig) new HueBindingConfig(
							configParts[0], configParts.length < 2 ? null
									: configParts[1],
							configParts.length < 3 ? null : configParts[2]);
					addBindingConfig(item, hueBindingConfig);
				} else if (item instanceof SwitchItem) {
					BindingConfig hueBindingConfig = (BindingConfig) new HueBindingConfig(
							configParts[0], BindingType.switching.name(), null);
					addBindingConfig(item, hueBindingConfig);
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
	public HueBindingConfig getItemConfig(String itemName) {
		return (HueBindingConfig) bindingConfigs.get(itemName);
	}

}
