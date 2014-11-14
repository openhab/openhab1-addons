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

import org.opencean.core.address.EnoceanId;
import org.opencean.core.address.EnoceanParameterAddress;
import org.opencean.core.common.Parameter;
import org.opencean.core.common.values.ButtonState;
import org.junit.Test;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class RockerSwitchTest extends BasicBindingTest {

    @Test
    public void testReceiveButtonPress() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.I);
        provider.setParameterAddress(parameterAddress);
        binding.addBindingProvider(provider);

        provider.setItem(new SwitchItem("dummie"));
        binding.valueChanged(parameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", OnOffType.ON, publisher.getUpdateState());
    }
}
