/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.config.binding.ValueBindingConfig;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for creating converters which translates between Homematic values
 * and openHAB States/Commands.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class ConverterFactory {
	private static final Logger logger = LoggerFactory.getLogger(ConverterFactory.class);

	public static final String CONVERTER_PACKAGE = "org.openhab.binding.homematic.internal.converter.state.";

	private TypeComparator typeComparator = new TypeComparator();
	private Map<String, Converter<?>> converterCache = new HashMap<String, Converter<?>>();

	/**
	 * Returns the converter for an item or a custom converter if specified in
	 * the binding.
	 */
	public Converter<? extends State> createConverter(Item item, HomematicBindingConfig bindingConfig) {
		if (bindingConfig != null && bindingConfig instanceof ValueBindingConfig
				&& ((ValueBindingConfig) bindingConfig).getConverter() != null) {
			logger.debug("Using custom converter for {}", bindingConfig);
			return ((ValueBindingConfig) bindingConfig).getConverter();
		}

		List<Class<? extends State>> acceptedTypes = item.getAcceptedDataTypes();
		Collections.sort(acceptedTypes, typeComparator);

		for (Class<? extends State> clazz : acceptedTypes) {
			String converterName = clazz.getSimpleName() + "Converter";

			Converter<?> converter = converterCache.get(converterName);
			if (converter == null) {
				try {
					converter = (Converter<?>) Class.forName(CONVERTER_PACKAGE + converterName).newInstance();
					converterCache.put(converterName, converter);
				} catch (Exception e) {
					// ignore
				}
			}

			if (converter != null) {
				return converter;
			}
		}
		logger.warn("Can't find converter for {}, value is not published!", bindingConfig);
		return null;
	}

}
