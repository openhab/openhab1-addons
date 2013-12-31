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
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;

/**
 * Converts from {@link StopMoveType} command to a Z-Wave value.
 * @author Ben Jones
 * @since 1.4.0
 */
public class MultiLevelStopMoveCommandConverter extends
		ZWaveCommandConverter<StopMoveType, Integer> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer convert(Item item, StopMoveType command) {
		int value = ((PercentType)item.getStateAs(PercentType.class)).intValue();
		
		switch (command) {
			case MOVE:
				// if fully open then start closing, else open fully
				// TODO: is it ok to just 'move' in this way?
				if (value == 0x00)
					value = 0x63; 
				else
					value = 0x00;
				break;
			case STOP:
				// if stopping then just return the current value - i.e. no change
				// TODO: not sure if this will work?
				break;
		}
		
		return value;
	}

}