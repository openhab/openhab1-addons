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
 * The AddModifier adds a given value to a read-value on read.
 * On write, the given value is subtracted of the value to write.
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireDecimalTypeAddModifier extends AbstractOneWireDecimalTypeModifier {

	private BigDecimal ivAdjustValue;

	/**
	 * @param pvAdjustValue
	 */
	public OneWireDecimalTypeAddModifier(BigDecimal pvAdjustValue) {
		super();
		this.ivAdjustValue = pvAdjustValue;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#getModifierName()
	 */
	public String getModifierName() {
		return "add modifier for DecimalTypes";
	}

	@Override
	public DecimalType modifyDecimalType4Read(DecimalType pvDecimalTypeValue) {
		if (pvDecimalTypeValue == null) {
			return null;
		}

		BigDecimal lvResult = pvDecimalTypeValue.toBigDecimal().add(ivAdjustValue);

		return new DecimalType(lvResult);
	}

	@Override
	public DecimalType modifyDecimalType4Write(DecimalType pvDecimalTypeValue) {
		if (pvDecimalTypeValue == null) {
			return null;
		}

		BigDecimal lvResult = pvDecimalTypeValue.toBigDecimal().subtract(ivAdjustValue);

		return new DecimalType(lvResult);
	}

}
