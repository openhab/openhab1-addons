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
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Baseclass for all number converters with common methods.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public abstract class AbstractNumberTypeConverter<T extends DecimalType> extends AbstractTypeConverter<T> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractNumberTypeConverter.class);

	/**
	 * Subclasses must implement this method to create a number type from the value.
	 */
	protected abstract T createType(BigDecimal value);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean toBoolean(T type, HmValueItem hmValueItem) {
		return new Boolean(toNumber(type, hmValueItem).doubleValue() == hmValueItem.getMaxValue().doubleValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Number toNumber(T type, HmValueItem hmValueItem) {
		if (hmValueItem.isIntegerValue()) {
			return type.intValue();
		}
		return round(type.doubleValue()).doubleValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String toString(T type, HmValueItem hmValueItem) {
		return toNumber(type, hmValueItem).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T fromBoolean(HmValueItem hmValueItem) {
		if (Boolean.TRUE.equals(hmValueItem.getValue())) {
			return createType(round(hmValueItem.getMaxValue().doubleValue()));
		}
		return createType(round(hmValueItem.getMinValue().doubleValue()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T fromNumber(HmValueItem hmValueItem) {
		Double number = ((Number) hmValueItem.getValue()).doubleValue();
		if (hmValueItem.isIntegerValue()) {
			return createType(new BigDecimal(number.intValue()));
		}
		return createType(round(number));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T fromString(HmValueItem hmValueItem) {
		return createTypeFromString(hmValueItem.getValue().toString());
	}

	private T createTypeFromString(String value) {
		try {
			return createType(new BigDecimal(value));
		} catch (NumberFormatException ex) {
			logger.warn("Can't convert string value '{}' to a number, returning 0", value);
			return createType(new BigDecimal(0));
		}
	}

}
