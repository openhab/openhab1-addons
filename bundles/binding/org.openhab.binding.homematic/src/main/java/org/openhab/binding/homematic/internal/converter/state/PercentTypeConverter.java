/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import java.math.BigDecimal;

import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;

/**
 * PercentTypeConverter which supports commands and calculates the percentage
 * value from the min and max metadata values.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class PercentTypeConverter extends AbstractNumberTypeConverter<PercentType> {

	/**
	 * Converts different openHAB commands to a Homematic object.
	 */
	@Override
	public Object commandToBinding(Command command, HmValueItem hmValueItem) {
		if (command.getClass() == IncreaseDecreaseType.class) {
			PercentType type = convertFromBinding(hmValueItem);

			int percent = type.intValue();
			percent += command.equals(IncreaseDecreaseType.INCREASE) ? 10 : -10;
			percent = (percent / 10) * 10;
			percent = Math.min(100, percent);
			percent = Math.max(0, percent);
			return convertToBinding(new PercentType(percent), hmValueItem);
		} else if (command.getClass() == OnOffType.class) {
			PercentType type = new PercentType(command.equals(OnOffType.ON) ? 100 : 0);
			return convertToBinding(type, hmValueItem);
		} else if (command.getClass() == UpDownType.class) {
			int result = command.equals(UpDownType.UP) ? 100 : 0;
			if (isRollerShutterLevelDatapoint(hmValueItem)) {
				result = command.equals(UpDownType.UP) ? 0 : 100;
			}
			return convertToBinding(new PercentType(result), hmValueItem);
		} else {
			return super.commandToBinding(command, hmValueItem);
		}
	}

	/**
	 * Creates a PercentType from the value.
	 */
	protected PercentType createType(BigDecimal value) {
		return new PercentType(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Number toNumber(PercentType type, HmValueItem hmValueItem) {
		Double number = (type.doubleValue() / 100) * hmValueItem.getMaxValue().doubleValue();

		if (isRollerShutterLevelDatapoint(hmValueItem)) {
			number = hmValueItem.getMaxValue().doubleValue() - number;
		}
		if (hmValueItem.isIntegerValue()) {
			return number.intValue();
		}
		return round(number).doubleValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PercentType fromBoolean(HmValueItem hmValueItem) {
		return Boolean.TRUE.equals(hmValueItem.getValue()) ? new PercentType(100) : new PercentType(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PercentType fromNumber(HmValueItem hmValueItem) {
		Double number = ((Number) hmValueItem.getValue()).doubleValue();
		int percent = (int) ((100 / hmValueItem.getMaxValue().doubleValue()) * number);

		if (isRollerShutterLevelDatapoint(hmValueItem)) {
			percent = 100 - percent;
		}
		return new PercentType(percent);
	}

}
