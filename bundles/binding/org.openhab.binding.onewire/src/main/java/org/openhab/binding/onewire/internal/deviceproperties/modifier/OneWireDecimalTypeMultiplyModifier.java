/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#getModifierName()
     */
    @Override
    public String getModifierName() {
        return "multiply modifier for DecimalTypes";
    }

    @Override
    public DecimalType modifyDecimalType4Read(DecimalType pvModifyValue) {
        if (pvModifyValue == null) {
            return null;
        }

        BigDecimal lvResult = pvModifyValue.toBigDecimal();

        if (lvResult.equals(BigDecimal.ZERO)) {
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

        if (lvResult.equals(BigDecimal.ZERO)) {
            return DecimalType.ZERO;
        } else {
            lvResult = lvResult.divide(ivAdjustValue);
        }

        return new DecimalType(lvResult);
    }

}
