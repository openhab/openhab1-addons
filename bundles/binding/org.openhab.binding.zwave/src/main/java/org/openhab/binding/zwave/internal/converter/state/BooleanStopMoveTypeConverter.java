/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.StopMoveType;

/**
 * Converts from Z-Wave boolean value to a {@link StopMoveType}
 * @author Ben Jones
 * @since 1.4.0
 */
public class BooleanOnOffTypeConverter extends
		ZWaveStateConverter<Boolean, StopMoveType> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StopMoveType convert(Boolean value) {
		return value ? StopMoveType.MOVE : StopMoveType.STOP;
	}

}
