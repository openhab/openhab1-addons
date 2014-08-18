/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.bus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.binding.astro.internal.model.Planet;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.util.ItemIterator;
import org.openhab.binding.astro.internal.util.ItemIterator.ItemIteratorCallback;
import org.openhab.binding.astro.internal.util.PropertyUtils;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publishes object properties to items.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class PlanetPublisher {
	private static final Logger logger = LoggerFactory.getLogger(PlanetPublisher.class);
	private AstroContext context = AstroContext.getInstance();
	private Map<String, Object> itemCache = new HashMap<String, Object>();

	private static PlanetPublisher instance = null;

	private PlanetPublisher() {
	}

	/**
	 * Returns the singleton instance of PlanetPublisher.
	 */
	public static PlanetPublisher getInstance() {
		if (instance == null) {
			instance = new PlanetPublisher();
		}
		return instance;
	}

	/**
	 * Clears the item cache.
	 */
	public void clear() {
		itemCache.clear();
	}

	/**
	 * Republish the state of the item.
	 */
	public void republishItem(String itemName) {
		AstroBindingConfig bindingConfig = null;
		for (AstroBindingProvider provider : context.getProviders()) {
			if (bindingConfig == null) {
				bindingConfig = provider.getBindingFor(itemName);
			}
		}
		if (bindingConfig == null) {
			logger.warn("Astro binding for item {} not found", itemName);
		} else {
			itemCache.remove(itemName);
			publish(bindingConfig.getPlanetName());
		}
	}

	/**
	 * Iterates through all items and publishes the states.
	 */
	public void publish(final PlanetName planetName) {
		final Planet planet = context.getPlanet(planetName);
		new ItemIterator().iterate(new ItemIteratorCallback() {

			@Override
			public void next(AstroBindingConfig bindingConfig, Item item) {
				if (planetName == bindingConfig.getPlanetName()) {
					try {
						Object value = PropertyUtils.getPropertyValue(planet, bindingConfig.getPlanetProperty());
						if (!equalsCachedValue(value, item)) {
							publishValue(item, value, bindingConfig);
							itemCache.put(item.getName(), value);
						}
					} catch (Exception ex) {
						logger.warn(ex.getMessage(), ex);
					}
				}
			}
		});
	}

	/**
	 * Returns true, if the cached value is equal to the new value.
	 */
	private boolean equalsCachedValue(Object value, Item item) {
		int cachedValueHashCode = ObjectUtils.hashCode(itemCache.get(item.getName()));
		int valueHashCode = ObjectUtils.hashCode(value);
		return cachedValueHashCode == valueHashCode;
	}

	/**
	 * Publishes the item with the value, if the item is a OnOffType and the
	 * value is a calendar, a job is scheduled.
	 */
	private void publishValue(Item item, Object value, AstroBindingConfig bindingConfig) {
		if (value == null) {
			context.getEventPublisher().postUpdate(item.getName(), UnDefType.UNDEF);
		} else if (value instanceof Calendar) {
			Calendar calendar = (Calendar) value;
			if (bindingConfig.getOffset() != 0) {
				calendar = (Calendar) calendar.clone();
				calendar.add(Calendar.MINUTE, bindingConfig.getOffset());
			}

			if (item.getAcceptedDataTypes().contains(DateTimeType.class)) {
				context.getEventPublisher().postUpdate(item.getName(), new DateTimeType(calendar));
			} else if (item.getAcceptedCommandTypes().contains(OnOffType.class)) {
				context.getJobScheduler().scheduleItem(calendar, item.getName());
			} else {
				logger.warn("Unsupported type for item {}, only DateTimeType and OnOffType supported!", item.getName());
			}
		} else if (value instanceof Number) {
			if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				BigDecimal decimalValue = new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP);
				context.getEventPublisher().postUpdate(item.getName(), new DecimalType(decimalValue));
			} else if (value instanceof Long && item.getAcceptedDataTypes().contains(StringType.class)
					&& "duration".equals(bindingConfig.getProperty())) {
				// special case, transforming duration to minute:second string
				context.getEventPublisher().postUpdate(item.getName(), new StringType(durationToString((Long) value)));
			} else {
				logger.warn("Unsupported type for item {}, only DecimalType supported!", item.getName());
			}
		} else if (value instanceof String || value instanceof Enum) {
			if (item.getAcceptedDataTypes().contains(StringType.class)) {
				if (value instanceof Enum) {
					String enumValue = WordUtils.capitalizeFully(StringUtils.replace(value.toString(), "_", " "));
					context.getEventPublisher().postUpdate(item.getName(), new StringType(enumValue));
				} else {
					context.getEventPublisher().postUpdate(item.getName(), new StringType(value.toString()));
				}
			} else {
				logger.warn("Unsupported type for item {}, only String supported!", item.getName());
			}

		} else {
			logger.warn("Unsupported value type {}", value.getClass().getSimpleName());
		}
	}

	/**
	 * Returns the minute:second string for the minutes number.
	 */
	private String durationToString(long minutes) {
		long hours = minutes / 60;
		minutes -= hours * 60;

		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);

		writer.printf("%02d:%02d", hours, minutes);

		writer.flush();
		return sw.getBuffer().toString();
	}

}
