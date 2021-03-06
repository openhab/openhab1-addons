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

import org.junit.Before;
import org.junit.Test;
import org.opencean.core.address.EnoceanId;
import org.opencean.core.address.EnoceanParameterAddress;
import org.opencean.core.common.EEPId;
import org.opencean.core.common.Parameter;
import org.opencean.core.common.values.ButtonState;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class RockerSwitchInSwitchOnOffProfileTest extends BasicBindingTest {

    private static final String CHANNEL = "B";

    @Before
    public void setUpDefaultDevice() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID),
                CHANNEL, (String) null);
        provider.setParameterAddress(parameterAddress);
        provider.setItem(new SwitchItem("dummie"));
        provider.setEep(EEPId.EEP_F6_02_01);
        binding.addBindingProvider(provider);
    }

    @Test
    public void switchOnForButtonPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), CHANNEL, Parameter.I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", OnOffType.ON, publisher.popLastCommand());
    }

    @Test
    public void switchOffForButtonPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), CHANNEL, Parameter.O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", OnOffType.OFF, publisher.popLastCommand());
    }

}
