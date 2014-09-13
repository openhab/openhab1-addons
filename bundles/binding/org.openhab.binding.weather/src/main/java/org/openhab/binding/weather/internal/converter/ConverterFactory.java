/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.weather.internal.converter.property.DateConverter;
import org.openhab.binding.weather.internal.converter.property.DoubleConverter;
import org.openhab.binding.weather.internal.converter.property.FractionIntegerConverter;
import org.openhab.binding.weather.internal.converter.property.FullUtcDateConverter;
import org.openhab.binding.weather.internal.converter.property.IntegerConverter;
import org.openhab.binding.weather.internal.converter.property.MultiIdConverter;
import org.openhab.binding.weather.internal.converter.property.PercentIntegerConverter;
import org.openhab.binding.weather.internal.converter.property.SimpleDateConverter;
import org.openhab.binding.weather.internal.converter.property.StringConverter;
import org.openhab.binding.weather.internal.converter.property.ThreeHoursDoubleConverter;
import org.openhab.binding.weather.internal.converter.property.UnixDateConverter;
import org.openhab.binding.weather.internal.converter.property.UtcDateConverter;
import org.openhab.binding.weather.internal.converter.property.WindMpsConverter;

/**
 * A factory for creating converters.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ConverterFactory {
	private static Map<ConverterType, Converter<?>> converters = new HashMap<ConverterType, Converter<?>>();

	static {
		addConverter(new DoubleConverter());
		addConverter(new IntegerConverter());
		addConverter(new StringConverter());
		addConverter(new UnixDateConverter());
		addConverter(new PercentIntegerConverter());
		addConverter(new FractionIntegerConverter());
		addConverter(new UtcDateConverter());
		addConverter(new DateConverter());
		addConverter(new FullUtcDateConverter());
		addConverter(new SimpleDateConverter());
		addConverter(new MultiIdConverter());
		addConverter(new WindMpsConverter());
		addConverter(new ThreeHoursDoubleConverter());
	}

	/**
	 * Add the converter instance to the cache.
	 */
	private static void addConverter(Converter<?> converter) {
		converters.put(converter.getType(), converter);
	}

	/**
	 * Returns a converter specified by the type.
	 */
	public static Converter<?> getConverter(ConverterType type) {
		return converters.get(type);
	}
}
