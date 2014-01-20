/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.StringType;

/**
 * Converts from a {@link String} to a {@link StringType}
 * @author Ben Jones
 * @since 1.4.0
 */
public class StringStringTypeConverter extends
		ZWaveStateConverter<String, StringType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StringType convert(String value) {
		return new StringType(value);
	}

}
