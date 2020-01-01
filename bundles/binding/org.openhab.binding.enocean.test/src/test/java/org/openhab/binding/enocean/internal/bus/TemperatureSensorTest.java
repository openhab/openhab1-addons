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
package org.openhab.binding.enocean.internal.bus;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.opencean.core.address.EnoceanId;
import org.opencean.core.address.EnoceanParameterAddress;
import org.opencean.core.common.Parameter;
import org.opencean.core.common.values.NumberWithUnit;
import org.opencean.core.common.values.Unit;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class TemperatureSensorTest extends BasicBindingTest {

    @Test
    public void testReceiveTempertureUpdate() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID),
                Parameter.TEMPERATURE);
        provider.setParameterAddress(parameterAddress);
        binding.addBindingProvider(provider);
        provider.setItem(new NumberItem("dummie"));
        BigDecimal temperature = new BigDecimal("20.3");
        binding.valueChanged(parameterAddress, new NumberWithUnit(Unit.DEGREE_CELSIUS, temperature));
        assertEquals("Update State", new DecimalType(temperature), publisher.getUpdateState());
    }

}
