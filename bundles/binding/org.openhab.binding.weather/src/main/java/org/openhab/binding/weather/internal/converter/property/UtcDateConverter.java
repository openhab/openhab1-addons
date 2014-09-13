/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Date converter with a UTC pattern.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class UtcDateConverter extends AbstractDateConverter {
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm a";

	public UtcDateConverter() {
		super(DATE_PATTERN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConverterType getType() {
		return ConverterType.UTC_DATE;
	}

}
