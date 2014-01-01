/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.command;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.StopMoveType;

/**
 * Converts from {@link StopMoveType} command to a Z-Wave value.
 * @author Ben Jones
 * @since 1.4.0
 */
public class BinaryStopMoveCommandConverter extends
		ZWaveCommandConverter<StopMoveType, Integer> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer convert(Item item, StopMoveType command) {
		return command == StopMoveType.MOVE ? 0xFF : 0x00;
	}
}