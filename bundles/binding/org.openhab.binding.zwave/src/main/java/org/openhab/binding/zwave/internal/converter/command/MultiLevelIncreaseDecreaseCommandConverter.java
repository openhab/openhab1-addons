/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.command;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;

/**
 * Converts from {@link IncreaseDecreaseType} command to a Z-Wave value.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class MultiLevelIncreaseDecreaseCommandConverter extends
		ZWaveCommandConverter<IncreaseDecreaseType, Integer> {

	private static final int DIMMER_STEP_SIZE = 5;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer convert(Item item, IncreaseDecreaseType command) {
		int value = ((PercentType)item.getStateAs(PercentType.class)).intValue();
		
		switch (command) {
			case DECREASE:
				if (value >= 0x63)
					value = ((((int)(0x63 / DIMMER_STEP_SIZE)) + 1) * DIMMER_STEP_SIZE) - DIMMER_STEP_SIZE; 
				else if (value > 0x00)
					value -= DIMMER_STEP_SIZE;
				else
					value = 0x00;
				break;
			case INCREASE:
				value += DIMMER_STEP_SIZE;
				
				if (value >= 0x63)
					value = 0x63;
				break;
		}
		
		return value;
	}

}