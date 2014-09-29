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
import org.openhab.core.library.types.PercentType;

/**
 * Converts from {@link PercentType} command to a Z-Wave value.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class MultiLevelPercentCommandConverter extends
		ZWaveCommandConverter<PercentType, Integer> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer convert(Item item, PercentType command) {
		if (command.intValue() <= 0)
			return 0x00;
		if (command.intValue() > 0 && command.intValue() < 0x63)
			return command.intValue();
		else 
			return 0x63;
	}
}