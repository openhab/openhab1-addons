/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts between openHAB StringType and Homematic values.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class StringTypeConverter extends AbstractTypeConverter<StringType> {
	private static final Logger logger = LoggerFactory.getLogger(StringTypeConverter.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean toBoolean(StringType type, HmValueItem hmValueItem) {
		String result = toString(type, hmValueItem);
		return "true".equalsIgnoreCase(result) || !"0".equals(result) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Number toNumber(StringType type, HmValueItem hmValueItem) {
		String value = toString(type, hmValueItem);
		if (hmValueItem.isIntegerValue()) {
			return Integer.valueOf(value);
		}
		return round(Double.valueOf(value)).doubleValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String toString(StringType type, HmValueItem hmValueItem) {
		if (hmValueItem.hasValueList()) {
			int idx = hmValueItem.getValueListIndex(type.toString());
			if (idx != -1) {
				return String.valueOf(idx);
			} else {
				logger.warn("Cant' find value '{}' in valueList for item {}, converting plain value", type.toString(),
						hmValueItem);
			}
		}

		return type.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StringType fromBoolean(HmValueItem hmValueItem) {
		return fromString(hmValueItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StringType fromNumber(HmValueItem hmValueItem) {
		String valueListValue = getValueListValue(hmValueItem);
		if (valueListValue != null) {
			return new StringType(valueListValue);
		}

		Double value = Double.valueOf(hmValueItem.getValue().toString());
		if (hmValueItem.isIntegerValue()) {
			return new StringType(String.valueOf(value.intValue()));
		}
		return new StringType(String.valueOf(round(value).doubleValue()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StringType fromString(HmValueItem hmValueItem) {
		String valueListValue = getValueListValue(hmValueItem);
		if (valueListValue != null) {
			return new StringType(valueListValue);
		}

		return new StringType(hmValueItem.getValue().toString());
	}

	/**
	 * If the item is bound to a valueList variable, return the string
	 * representation.
	 */
	private String getValueListValue(HmValueItem hmValueItem) {
		if (hmValueItem.hasValueList()) {
			String valueListValue = hmValueItem.getValueListValue();
			if (valueListValue != null) {
				return valueListValue;
			} else {
				logger.warn("Cant' find value '{}' in valueList for item {}, converting plain value",
						hmValueItem.getValue(), hmValueItem);
			}
		}
		return null;
	}

}
