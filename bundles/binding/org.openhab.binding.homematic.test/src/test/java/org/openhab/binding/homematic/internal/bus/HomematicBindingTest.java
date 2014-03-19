/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import org.junit.Test;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;

public class HomematicBindingTest extends BasicBindingTest {

    @Test
    public void testBindingChangedKnownParameterKey() {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, SHORT_PRESS_PARAMETER_KEY);
        provider.setParameterAddress(paramAddress);
        values.put(SHORT_PRESS_PARAMETER_KEY, Boolean.TRUE);
        binding.bindingChanged(provider, ITEM_NAME);
    }
    @Test
    public void testBindingChangedKnownParameterKeyWithoutMapper() {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, SHORT_PRESS_PARAMETER_KEY);
        provider.setParameterAddress(paramAddress);
        values.put(SHORT_PRESS_PARAMETER_KEY, Boolean.TRUE);
        binding.bindingChanged(provider, ITEM_NAME);
    }
    @Test
    public void testBindingChangedUnknownParameter() {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, "unknown");
        provider.setParameterAddress(paramAddress);
        values.put(SHORT_PRESS_PARAMETER_KEY, Boolean.TRUE);
        binding.bindingChanged(provider, ITEM_NAME);
    }

}
