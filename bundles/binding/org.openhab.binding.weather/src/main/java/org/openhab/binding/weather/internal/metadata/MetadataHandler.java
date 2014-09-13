/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.annotation.Forecast;
import org.openhab.binding.weather.internal.annotation.ForecastMappings;
import org.openhab.binding.weather.internal.annotation.Provider;
import org.openhab.binding.weather.internal.annotation.ProviderMappings;
import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.ConverterFactory;
import org.openhab.binding.weather.internal.converter.ConverterType;
import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.utils.PropertyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scans the model package and extracts mapping infos from the annotations.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class MetadataHandler {
	private static final Logger logger = LoggerFactory.getLogger(MetadataHandler.class);

	private static final String PACKAGE_TO_SCAN = StringUtils.substringBeforeLast(Weather.class.getName(), ".");
	private Map<ProviderName, Map<String, ProviderMappingInfo>> providerMappings = new HashMap<ProviderName, Map<String, ProviderMappingInfo>>();
	private Map<ProviderName, List<String>> forecastMappings = new HashMap<ProviderName, List<String>>();

	private static MetadataHandler instance;

	private MetadataHandler() {
	}

	/**
	 * Returns the singleton instance of the MetadataHandler.
	 */
	public static MetadataHandler getInstance() {
		if (instance == null) {
			instance = new MetadataHandler();
		}
		return instance;
	}

	/**
	 * Scans the class and generates metadata.
	 */
	public void generate(Class<?> clazz) throws IllegalAccessException {
		if (clazz == null) {
			return;
		}

		for (Field field : clazz.getDeclaredFields()) {
			if (field.getType().getName().startsWith(PACKAGE_TO_SCAN) && !field.isEnumConstant()) {
				generate(field.getType());
			} else {
				for (Annotation annotation : field.getAnnotations()) {
					if (annotation.annotationType().equals(ProviderMappings.class)) {
						ProviderMappings providerAnnotations = (ProviderMappings) annotation;
						for (Provider provider : providerAnnotations.value()) {
							Map<String, ProviderMappingInfo> mappings = providerMappings.get(provider.name());

							if (mappings == null) {
								mappings = new HashMap<String, ProviderMappingInfo>();
								providerMappings.put(provider.name(), mappings);
							}

							Converter<?> converter = (Converter<?>) getConverter(field, provider.converter());
							String target = clazz.getSimpleName().toLowerCase() + "." + field.getName();
							ProviderMappingInfo pm = new ProviderMappingInfo(provider.property(), target, converter);
							mappings.put(pm.getSource(), pm);
							logger.trace("Added provider mapping {}: {}", provider.name(), pm);
						}
					} else if (annotation.annotationType().equals(ForecastMappings.class)) {
						ForecastMappings forecastsAnnotations = (ForecastMappings) annotation;
						for (Forecast forecast : forecastsAnnotations.value()) {
							List<String> forecastProperties = forecastMappings.get(forecast.provider());
							if (forecastProperties == null) {
								forecastProperties = new ArrayList<String>();
								forecastMappings.put(forecast.provider(), forecastProperties);
							}
							forecastProperties.add(forecast.property());
							logger.trace("Added forecast mapping {}: {}", forecast.provider(), forecast.property());
						}
					}
				}
			}
		}
	}

	/**
	 * Autodetects a converter or returns a specified instance.
	 */
	private Converter<?> getConverter(Field field, ConverterType type) {
		if (type == ConverterType.AUTO) {
			String fieldType = field.getType().getSimpleName().toUpperCase();
			return ConverterFactory.getConverter(ConverterType.valueOf(fieldType));
		} else if (type == ConverterType.NONE) {
			return null;
		} else {
			return ConverterFactory.getConverter(type);
		}
	}

	/**
	 * Returns the MappingInfo for the specified provider and property.
	 */
	public ProviderMappingInfo getProviderMappingInfo(ProviderName provider, String property) {
		Map<String, ProviderMappingInfo> mapping = providerMappings.get(provider);
		if (mapping == null) {
			return null;
		}

		ProviderMappingInfo provMapping = mapping.get(property);

		String nestedProperty = property;
		while (provMapping == null && PropertyResolver.hasNested(nestedProperty)) {
			nestedProperty = PropertyResolver.removeFirst(nestedProperty);
			provMapping = mapping.get(nestedProperty);
		}

		return provMapping;
	}

	/**
	 * Returns true, if the property is a forecast property.
	 */
	public boolean isForecast(ProviderName provider, String property) {
		if (property == null || !forecastMappings.containsKey(provider)) {
			return false;
		}
		return forecastMappings.get(provider).contains(property);
	}
}
