/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.util;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
/**
 * Methods to get the value from a property of an object.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class PropertyUtils {

	/**
	 * Returns the property value from the object instance, nested properties
	 * are possible. If the propertyName is for example rise.start, the methods
	 * getRise().getStart() are called.
	 */
	public static Object getPropertyValue(Object instance, String property) throws Exception {
		String[] properties = StringUtils.split(property, ".");
		return getPropertyValue(instance, properties, 0);
	}

	/**
	 * Returns true, if the specified instance has the specified property.
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
	 * Iterates through the nested properties and returns the getter value.
	 */
	@SuppressWarnings("all")
	private static Object getPropertyValue(Object instance, String[] properties, int nestedIndex) throws Exception {
		String propertyName = properties[nestedIndex];
		Method m = instance.getClass().getMethod(toGetterString(propertyName), null);
		Object result = m.invoke(instance, (Object[]) null);
		if (++nestedIndex < properties.length) {
			return getPropertyValue(result, properties, nestedIndex);
		}
		return result;
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
