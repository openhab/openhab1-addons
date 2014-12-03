/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.utils;

import org.apache.commons.lang.StringUtils;

/**
 * This class assists in resolving nested property names.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class PropertyResolver {
	private static final String SEPERATOR = ".";

	/**
	 * Indicates whether or not the expression contains nested property
	 * expressions.
	 */
	public static boolean hasNested(String expression) {
		return StringUtils.contains(expression, SEPERATOR);
	}

	/**
	 * Extract the next property expression from the current expression.
	 */
	public static String first(String expression) {
		return StringUtils.substringBefore(expression, SEPERATOR);
	}

	/**
	 * Extract the last property expression from the current expression.
	 */
	public static String last(String expression) {
		return StringUtils.substringAfterLast(expression, SEPERATOR);
	}

	/**
	 * Remove the first property expresson from the current expression.
	 */
	public static String removeFirst(String expression) {
		return StringUtils.substringAfter(expression, SEPERATOR);
	}

	/**
	 * Remove the last property expresson from the current expression.
	 */
	public static String removeLast(String expression) {
		return StringUtils.substringBeforeLast(expression, SEPERATOR);
	}

	/**
	 * Adds a property expression.
	 */
	public static String add(String expression, String add) {
		String str1 = StringUtils.trimToNull(expression);
		String str2 = StringUtils.trimToNull(add);

		if (str1 == null && str2 == null) {
			return null;
		} else if (str1 == null && str2 != null) {
			return str2;
		} else if (str1 != null && str2 == null) {
			return str1;
		}
		return str1 + SEPERATOR + str2;
	}

}
