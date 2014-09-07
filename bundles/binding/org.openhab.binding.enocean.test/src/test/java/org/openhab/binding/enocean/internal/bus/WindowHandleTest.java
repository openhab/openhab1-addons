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
import org.opencean.core.common.EEPId;
import org.opencean.core.common.Parameter;
import org.opencean.core.common.values.WindowHandleState;
import org.junit.Before;
import org.junit.Test;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;

/**
 * Testcases to test the transformation of window handle enocean-messages to window states
 * 
 * @author Holger Ploch (contact@holger-ploch.de)
 * 
 */
public class WindowHandleTest extends BasicBindingTest {

    @Before
    public void setUpDefaultDevice() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID));
        provider.setParameterAddress(parameterAddress);
        provider.setItem(new StringItem("dummie"));
        provider.setEep(EEPId.EEP_F6_10_00);
        binding.addBindingProvider(provider);
    }
	
	
    @Test
    public void testReceiveWindowClosed() {
        binding.valueChanged(parameterAddress, WindowHandleState.DOWN);
        assertEquals("Update Window State", new StringType("CLOSED").toString(), publisher.popLastCommand().toString());
    }
    
    @Test
    public void testReceiveWindowOpened() {
        binding.valueChanged(parameterAddress, WindowHandleState.MIDDLE);
        assertEquals("Update Window State", new StringType("OPEN").toString(), publisher.popLastCommand().toString());
    }
    
    @Test
    public void testReceiveWindowAjar() {
        binding.valueChanged(parameterAddress, WindowHandleState.UP);
        assertEquals("Update Window State", new StringType("AJAR").toString(), publisher.popLastCommand().toString());
    }
    
    
}
