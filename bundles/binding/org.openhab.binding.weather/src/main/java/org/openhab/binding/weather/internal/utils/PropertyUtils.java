/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.openhab.binding.weather.internal.model.Weather;

/**
 * Methods to get the value from a property or a getter method of an object.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class PropertyUtils {
	private static final String WEATHER_PROPERTY = Weather.class.getSimpleName().toLowerCase() + ".";

	/**
	 * Returns the object of the (nested) property.
	 */
	public static Object getNestedObject(Object instance, String propertyName) throws IllegalAccessException {
		if (PropertyUtils.isWeatherProperty(propertyName)) {
			return instance;
		}

		while (PropertyResolver.hasNested(propertyName)) {
			instance = FieldUtils.readField(instance, PropertyResolver.first(propertyName), true);
			propertyName = PropertyResolver.removeFirst(propertyName);
		}
		return instance;
	}

	/**
	 * Returns true, if the property is a property from the weather object.
	 */
	public static boolean isWeatherProperty(String property) {
		return StringUtils.startsWith(property, WEATHER_PROPERTY);
	}

	/**
	 * Returns true, if the specified instance has the specified getter method.
	 */
	public static boolean hasProperty(Object instance, String property) {
		try {
			getPropertyValue(instance, property);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Returns the type name of the property of the instance.
	 */
	public static String getPropertyTypeName(Object instance, String property) throws IllegalAccessException {
		Object object = getNestedObject(instance, property);
		Field field = FieldUtils.getField(object.getClass(), PropertyResolver.last(property), true);
		return field.getType().getCanonicalName();
	}

	/**
	 * Returns the getter value from the object instance, nested properties are
	 * possible. If the propertyName is for example temperature.current, the
	 * methods getTemperature().getCurrent() are called.
	 */
	public static Object getPropertyValue(Object instance, String property) throws Exception {
		Object object = getNestedObject(instance, property);
		String getMethod = toGetterString(PropertyResolver.last(property));
		return MethodUtils.invokeMethod(object, getMethod, null);
	}

	/**
	 * Converts the string to a getter property.
	 */
	private static String toGetterString(String str) {
		StringBuilder sb = new StringBuilder();
		sb.append("get");
		sb.append(Character.toTitleCase(str.charAt(0)));
		sb.append(str.substring(1));
		return sb.toString();
	}

}
