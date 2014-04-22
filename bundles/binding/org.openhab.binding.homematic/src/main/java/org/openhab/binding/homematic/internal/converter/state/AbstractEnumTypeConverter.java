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
import org.openhab.core.types.State;
/**
 * Baseclass for all Enum converters with common methods.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public abstract class AbstractEnumTypeConverter<T extends State> extends AbstractTypeConverter<T> {

	/**
	 * Subclasses must implement this method to create the 'false' type of an enum.
	 */
	protected abstract T getFalseType();

	/**
	 * Subclasses must implement this method to create the 'true' type of an enum.
	 */
	protected abstract T getTrueType();

	/**
	 * Subclasses must implement this method if the enum must be inverted.
	 */
	protected abstract boolean isInvert(HmValueItem hmValueItem);

	/**
	 * Checks if the name of the item is SENSOR, value must be inverted for some types.
	 */
	protected boolean isSensorValueName(HmValueItem hmValueItem) {
		return hmValueItem.getName().equals("SENSOR");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean toBoolean(T type, HmValueItem hmValueItem) {
		if (isInvert(hmValueItem)) {
			return type.equals(getFalseType()) ? Boolean.TRUE : Boolean.FALSE;
		}
		return type.equals(getFalseType()) ? Boolean.FALSE : Boolean.TRUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Number toNumber(T type, HmValueItem hmValueItem) {
		if (isInvert(hmValueItem)) {
			return type.equals(getFalseType()) ? hmValueItem.getMaxValue() : hmValueItem.getMinValue();
		}
		return type.equals(getFalseType()) ? hmValueItem.getMinValue() : hmValueItem.getMaxValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String toString(T type, HmValueItem hmValueItem) {
		if (isInvert(hmValueItem)) {
			return type.toString().equalsIgnoreCase(getFalseType().toString()) ? getTrueType().toString()
					: getFalseType().toString();
		}
		return type.toString().equalsIgnoreCase(getFalseType().toString()) ? getFalseType().toString() : getTrueType()
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T fromBoolean(HmValueItem hmValueItem) {
		return createEnumType(hmValueItem, !(Boolean) hmValueItem.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T fromNumber(HmValueItem hmValueItem) {
		double number = ((Number) hmValueItem.getValue()).doubleValue();
		if (isRollerShutterLevelDatapoint(hmValueItem) && this instanceof OnOffTypeConverter) {
			return createEnumType(hmValueItem, number == hmValueItem.getMaxValue().doubleValue());
		}
		return createEnumType(hmValueItem, number == hmValueItem.getMinValue().doubleValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T fromString(HmValueItem hmValueItem) {
		return createEnumType(hmValueItem, getFalseType().toString()
				.equalsIgnoreCase(hmValueItem.getValue().toString()));
	}

	private T createEnumType(HmValueItem hmValueItem, boolean isFalse) {
		if (isInvert(hmValueItem)) {
			isFalse = !isFalse;
		}
		return isFalse ? getFalseType() : getTrueType();
	}
}
