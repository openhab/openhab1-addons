/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter;

/**
 * Converter interface for converting between JSON properties and the weather
 * model properties.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface Converter<T> {

	/**
	 * Converts a string value to a target value.
	 */
	public T convert(String value) throws Exception;

	/**
	 * Returns the ConverterType.
	 */
	public ConverterType getType();

}
