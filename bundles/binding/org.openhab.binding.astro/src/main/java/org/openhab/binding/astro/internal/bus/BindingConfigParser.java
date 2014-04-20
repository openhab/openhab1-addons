/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.bus;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Class to parse the key - value base config for an Astro item.
 * <p>Example:</p>
 * <pre>
 * Number   Azimuth        "Azimuth [%.2f]"         {astro="type=AZIMUTH"}
 * Number   Elevation      "Elevation [%.2f]"       {astro="type=ELEVATION"}
 * 
 * DateTime Sunrise_Time   "Sunrise [%1$tH:%1$tM]"  {astro="type=SUNRISE_TIME"}
 * DateTime Noon_Time      "Noon [%1$tH:%1$tM]"     {astro="type=NOON_TIME"}
 * DateTime Sunset_Time    "Sunset [%1$tH:%1$tM]"   {astro="type=SUNSET_TIME"}
 *
 * Switch   Sunrise_Event                           {astro="type=SUNRISE"}
 * Switch   Noon_Event                              {astro="type=NOON"}
 * Switch   Sunset_Event                            {astro="type=SUNSET"}
 * </pre>
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BindingConfigParser {

	/**
	 * Parses the bindingConfig of an item and returns a AstroBindingConfig.
	 */
	public AstroBindingConfig parse(Item item, String bindingConfig) throws BindingConfigParseException {
		bindingConfig = StringUtils.trimToEmpty(bindingConfig);
		bindingConfig = StringUtils.removeStart(bindingConfig, "{");
		bindingConfig = StringUtils.removeEnd(bindingConfig, "}");

		String[] entryParts = StringUtils.trimToEmpty(bindingConfig).split("[=]");
		if (entryParts.length != 2) {
			throw new BindingConfigParseException("A bindingConfig must have a key and a value");
		}

		String key = StringUtils.trim(entryParts[0]);
		if (!"type".equalsIgnoreCase(key)) {
			throw new BindingConfigParseException("Unknown key " + key);
		}
		String value = StringUtils.trim(entryParts[1]);
		value = StringUtils.removeStart(value, "\"");
		value = StringUtils.removeEnd(value, "\"");

		AstroType type = null;
		try {
			type = AstroType.valueOf(StringUtils.upperCase(value));
		} catch (Exception ex) {
			throw new BindingConfigParseException("Invalid binding: " + bindingConfig);
		}

		if (!item.getAcceptedDataTypes().contains(type.getAcceptedDataType())) {
			throw new BindingConfigParseException("Item " + item.getName() + " must support "
					+ type.getAcceptedDataType().getSimpleName() + " for binding " + value);
		}

		return new AstroBindingConfig(type);
	}
}
