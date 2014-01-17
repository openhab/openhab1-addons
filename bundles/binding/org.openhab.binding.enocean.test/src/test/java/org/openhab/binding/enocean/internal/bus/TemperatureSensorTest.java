/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.bus;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.enocean.java.address.EnoceanId;
import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.common.Parameter;
import org.enocean.java.common.values.NumberWithUnit;
import org.enocean.java.common.values.Unit;
import org.junit.Test;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class TemperatureSensorTest extends BasicBindingTest {

    @Test
    public void testReceiveTempertureUpdate() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.TEMPERATURE);
        provider.setParameterAddress(parameterAddress);
        binding.addBindingProvider(provider);
        provider.setItem(new NumberItem("dummie"));
        BigDecimal temperature = new BigDecimal("20.3");
        binding.valueChanged(parameterAddress, new NumberWithUnit(Unit.DEGREE_CELSIUS, temperature));
        assertEquals("Update State", new DecimalType(temperature), publisher.getUpdateState());
    }

}
