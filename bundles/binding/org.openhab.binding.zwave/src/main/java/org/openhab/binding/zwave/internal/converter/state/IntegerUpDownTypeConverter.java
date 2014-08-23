/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.UpDownType;

/**
 * Converts from a {@link Integer} to a {@link UpDownType} 
 * @author Ben Jones
 * @since 1.4.0
 */
public class IntegerUpDownTypeConverter extends
		ZWaveStateConverter<Integer, UpDownType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UpDownType convert(Integer value) {
		return value != 0x00 ? UpDownType.DOWN : UpDownType.UP;
	}
}
