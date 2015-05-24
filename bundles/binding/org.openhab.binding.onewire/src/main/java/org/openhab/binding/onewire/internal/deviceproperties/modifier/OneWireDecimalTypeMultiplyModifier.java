/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties.modifier;

import java.math.BigDecimal;

import org.openhab.core.library.types.DecimalType;

/**
 * The MultiplyModifier multiplies a given value with the read-value on read.
 * On write, value to write is divided by given value.
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireDecimalTypeMultiplyModifier extends AbstractOneWireDecimalTypeModifier {

	private BigDecimal ivAdjustValue;

	/**
	 * @param pvAdjustValue
	 */
	public OneWireDecimalTypeMultiplyModifier(BigDecimal pvAdjustValue) {
		super();
		this.ivAdjustValue = pvAdjustValue;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#getModifierName()
	 */
	public String getModifierName() {
		return "multiply modifier for DecimalTypes";
	}

	@Override
	public DecimalType modifyDecimalType4Read(DecimalType pvModifyValue) {
		if (pvModifyValue == null) {
			return null;
		}

		BigDecimal lvResult = pvModifyValue.toBigDecimal();

		if (pvModifyValue.equals(BigDecimal.ZERO)) {
			return DecimalType.ZERO;
		} else if (lvResult.equals(BigDecimal.ZERO)) {
			return DecimalType.ZERO;
		} else {
			lvResult = lvResult.multiply(ivAdjustValue);
		}

		return new DecimalType(lvResult);
	}

	@Override
	public DecimalType modifyDecimalType4Write(DecimalType pvModifyValue) {
		if (pvModifyValue == null) {
			return null;
		}

		BigDecimal lvResult = pvModifyValue.toBigDecimal();

		if (pvModifyValue.equals(BigDecimal.ZERO)) {
			return DecimalType.ZERO;
		} else if (lvResult.equals(BigDecimal.ZERO)) {
			return DecimalType.ZERO;
		} else {
			lvResult = lvResult.divide(ivAdjustValue);
		}

		return new DecimalType(lvResult);
	}

}
