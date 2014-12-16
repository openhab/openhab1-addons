/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Extracts the last ID from a multi ID field.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class MultiIdConverter implements Converter<String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convert(String value) throws Exception {
		return StringUtils.substringAfterLast(value, ":");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.MULTI_ID;
	}

}
