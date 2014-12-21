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
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.util.PropertyUtils;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to parse the key - value base config for an Astro item.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * Number   Azimuth        "Azimuth [%.2f]"     {astro="planet=sun, type=position, property=azimuth"}
 * Number   Elevation      "Elevation [%.2f]"   {astro="planet=sun, type=position, property=elevation"} 
 * 
 * DateTime Sunrise_Time   "Sunrise [%1$tH:%1$tM]"  {astro="planet=sun, type=rise, property=start"}
 * DateTime Noon_Time      "Noon [%1$tH:%1$tM]"     {astro="planet=sun, type=noon, property=start"}
 * DateTime Sunset_Time    "Sunset [%1$tH:%1$tM]"   {astro="planet=sun, type=set, property=end"}
 * 
 * Switch   Sunrise_Event                           {astro="planet=sun, type=rise, property=start"}
 * Switch   Sunrise_Event_Delayed                   {astro="planet=sun, type=rise, property=start, offset=10"}
 * Switch   Noon_Event                              {astro="planet=sun, type=noon, property=start"}
 * Switch   Sunset_Event                            {astro="planet=sun, type=set, property=end"}
 *
 * Number   Sunset_Duration                         {astro="planet=sun, type=set, property=duration"}
 * String   Sunset_Duration_Str                     {astro="planet=sun, type=set, property=duration"}
 * </pre>
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BindingConfigParser {
	private static final Logger logger = LoggerFactory.getLogger(BindingConfigParser.class);
	protected AstroContext context = AstroContext.getInstance();

	/**
	 * Parses the bindingConfig of an item and returns a AstroBindingConfig.
	 */
	public AstroBindingConfig parse(Item item, String bindingConfig) throws BindingConfigParseException {
		bindingConfig = StringUtils.trimToEmpty(bindingConfig);
		bindingConfig = StringUtils.removeStart(bindingConfig, "{");
		bindingConfig = StringUtils.removeEnd(bindingConfig, "}");
		String[] entries = bindingConfig.split("[,]");
		AstroBindingConfigHelper helper = new AstroBindingConfigHelper();

		for (String entry : entries) {
			String[] entryParts = StringUtils.trimToEmpty(entry).split("[=]");
			if (entryParts.length != 2) {
				throw new BindingConfigParseException("A bindingConfig must have a key and a value");
			}
			String key = StringUtils.trim(entryParts[0]);

			String value = StringUtils.trim(entryParts[1]);
			value = StringUtils.removeStart(value, "\"");
			value = StringUtils.removeEnd(value, "\"");

			try {
				if ("offset".equalsIgnoreCase(key)) {
					helper.getClass().getDeclaredField(key).set(helper, Integer.valueOf(value.toString()));
				} else {
					helper.getClass().getDeclaredField(key).set(helper, value);
				}
			} catch (Exception e) {
				throw new BindingConfigParseException("Could not set value " + value + " for attribute " + key);
			}
		}

		if (helper.isOldStyle()) {
			logger.warn(
					"Old Astro binding style for item {}, please see Wiki page for new style: https://github.com/openhab/openhab/wiki/Astro-binding",
					item.getName());
			return getOldAstroBindingConfig(helper);
		}

		if (!helper.isValid()) {
			throw new BindingConfigParseException("Invalid binding: " + bindingConfig);
		}

		PlanetName planetName = getPlanetName(helper);
		if (planetName == null) {
			throw new BindingConfigParseException("Invalid binding, unknown planet: " + bindingConfig);
		}

		AstroBindingConfig astroConfig = new AstroBindingConfig(planetName, helper.type, helper.property, helper.offset);

		if (!PropertyUtils.hasProperty(context.getPlanet(astroConfig.getPlanetName()), astroConfig.getPlanetProperty())) {
			throw new BindingConfigParseException("Invalid binding, unknown type or property: " + bindingConfig);
		}
		return astroConfig;
	}

	/**
	 * Parses the planet name.
	 */
	private PlanetName getPlanetName(AstroBindingConfigHelper helper) {
		try {
			return PlanetName.valueOf(StringUtils.upperCase(helper.planet));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Returns the AstroBindingConfig for old binding style.
	 */
	private AstroBindingConfig getOldAstroBindingConfig(AstroBindingConfigHelper helper) {
		String type = helper.type.toUpperCase();
		if ("AZIMUTH".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "position", "azimuth");
		} else if ("ELEVATION".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "position", "elevation");
		} else if ("SUNRISE".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "rise", "start");
		} else if ("NOON".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "noon", "start");
		} else if ("SUNSET".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "set", "end");
		} else if ("SUNRISE_TIME".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "rise", "start");
		} else if ("NOON_TIME".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "noon", "start");
		} else if ("SUNSET_TIME".equals(type)) {
			return new AstroBindingConfig(PlanetName.SUN, "set", "end");
		}

		return null;
	}

	/**
	 * Helper class for parsing the bindingConfig.
	 */
	private class AstroBindingConfigHelper {
		public String planet;
		public String type;
		public String property;
		public int offset = 0;

		protected boolean isValid() {
			return StringUtils.isNotBlank(planet) && StringUtils.isNotBlank(type) && StringUtils.isNotBlank(property);
		}

		protected boolean isOldStyle() {
			return StringUtils.isNotBlank(type) && StringUtils.isBlank(planet) && StringUtils.isBlank(property);
		}
	}

}
