/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.OnOffType;

/**
 * Converts from Z-Wave boolean value to a {@link OnOffType}
 * @author Ben Jones
 * @since 1.4.0
 */
public class BooleanOnOffTypeConverter extends
		ZWaveStateConverter<Boolean, OnOffType> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OnOffType convert(Boolean value) {
		return value ? OnOffType.ON : OnOffType.OFF;
	}

}
