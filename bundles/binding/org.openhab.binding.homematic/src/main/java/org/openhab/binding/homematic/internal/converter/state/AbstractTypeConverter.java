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
import java.math.RoundingMode;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Baseclass for all converters with common methods.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public abstract class AbstractTypeConverter<T extends State> implements Converter<T> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractTypeConverter.class);

	private static final String[] ROLLERSHUTTER_DEVICES = new String[] { "HM-LC-Bl1-FM", "HM-LC-Bl1PBU-FM",
			"HMW-LC-Bl1-DR" };

	/**
	 * Checks if the value to convert is a RollerShutter level value, these
	 * values must be inverted.
	 */
	protected boolean isRollerShutterLevelDatapoint(HmValueItem hmValueItem) {
		if (hmValueItem != null && hmValueItem instanceof HmDatapoint) {
			String device = ((HmDatapoint) hmValueItem).getChannel().getDevice().getType();
			if (ArrayUtils.contains(ROLLERSHUTTER_DEVICES, device) && "LEVEL".equalsIgnoreCase(hmValueItem.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Rounds a double value.
	 */
	protected BigDecimal round(Double number) {
		BigDecimal bd = new BigDecimal(number == null ? "0" : number.toString());
		String stringBd = bd.toPlainString();
		int scale = stringBd.length() - (stringBd.lastIndexOf('.') + 1);
		return bd.setScale(scale > 2 ? 6 : 2, RoundingMode.HALF_UP);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object convertToBinding(Type type, HmValueItem hmValueItem) {
		if (logger.isDebugEnabled()) {
			logger.debug("Converting type {} with value '{}' to ({}) value with {} for {}", type.getClass()
					.getSimpleName(), type.toString(), hmValueItem.getValue().getClass().getSimpleName(), this
					.getClass().getSimpleName(), hmValueItem);
		}

		if (type.getClass().isEnum() && !(this instanceof AbstractEnumTypeConverter)) {
			return commandToBinding((Command) type, hmValueItem);
		} else {
			if (hmValueItem.isBooleanValue()) {
				return toBoolean((T) type, hmValueItem);
			} else if (hmValueItem.isNumberValue()) {
				return toNumber((T) type, hmValueItem);
			} else {
				return toString((T) type, hmValueItem);
			}
		}
	}

	/**
	 * Converts a openHAB type to a Homematic boolean value.
	 */
	protected abstract Boolean toBoolean(T type, HmValueItem hmValueItem);

	/**
	 * Converts a openHAB type to a Homematic number value.
	 */
	protected abstract Number toNumber(T type, HmValueItem hmValueItem);

	/**
	 * Converts a openHAB type to a Homematic string value.
	 */
	protected abstract String toString(T type, HmValueItem hmValueItem);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T convertFromBinding(HmValueItem hmValueItem) {
		if (logger.isDebugEnabled()) {
			logger.debug("Converting ({}) value '{}' with {} for {}",
					hmValueItem.getValue().getClass().getSimpleName(), hmValueItem.getValue(), this.getClass()
							.getSimpleName(), hmValueItem.toString());
		}

		if (hmValueItem.isBooleanValue()) {
			return fromBoolean(hmValueItem);
		} else if (hmValueItem.isNumberValue()) {
			return fromNumber(hmValueItem);
		} else {
			return fromString(hmValueItem);
		}
	}

	/**
	 * Converts a boolean Homematic value to a openHAB State.
	 */
	protected abstract T fromBoolean(HmValueItem hmValueItem);

	/**
	 * Converts a number Homematic value to a openHAB State.
	 */
	protected abstract T fromNumber(HmValueItem hmValueItem);

	/**
	 * Converts a string Homematic value to a openHAB State.
	 */
	protected abstract T fromString(HmValueItem hmValueItem);

	/**
	 * Converts a openHAB command to a Homematic value, overridden by the
	 * different converters.
	 */
	public Object commandToBinding(Command command, HmValueItem hmValueItem) {
		throw new UnsupportedOperationException("Unsupported command " + command.getClass().getSimpleName() + " in "
				+ this.getClass().getSimpleName());
	}
}
