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

import org.enocean.java.address.EnoceanId;
import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.common.EEPId;
import org.enocean.java.common.Parameter;
import org.enocean.java.common.values.ButtonState;
import org.junit.Before;
import org.junit.Test;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;

public class RockerSwitchInRollershutterProfileTest extends BasicBindingTest {

    private static final int LONG_PRESS_DELAY = 300 + 10;

    @Before
    public void setUpDefaultDevice() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID));
        provider.setParameterAddress(parameterAddress);
        provider.setItem(new RollershutterItem("dummie"));
        provider.setEep(EEPId.EEP_F6_02_01);
        binding.addBindingProvider(provider);
    }

    @Test
    public void openShutterOnShortButtonPressUp() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", UpDownType.UP, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void closeShutterOnShortButtonPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", UpDownType.DOWN, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void openShutterDuringLongButtonPressUp() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", UpDownType.UP, publisher.popLastCommand());
        waitFor(LONG_PRESS_DELAY);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void closeShutterDuringLongButtonPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", UpDownType.DOWN, publisher.popLastCommand());
        waitFor(LONG_PRESS_DELAY);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void stopShutterMovingUpOnShortPressUp() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        publisher.popLastCommand();
        waitFor(100);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void stopShutterMovingDownOnShortPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        publisher.popLastCommand();
        waitFor(100);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void stopShutterMovingDownOnShortPressUp() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        publisher.popLastCommand();
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("No new state expected", null, publisher.popLastCommand());
        waitFor(100);
        valueParameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void stopShutterMovingUpOnShortPressDown() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        publisher.popLastCommand();
        waitFor(100);
        valueParameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.I);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    @Test
    public void stopShutterMovingAndStartAgain() {
        EnoceanParameterAddress valueParameterAddress = new EnoceanParameterAddress(
                EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID), Parameter.O);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", UpDownType.UP, publisher.popLastCommand());
        waitFor(100);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        waitFor(100);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", UpDownType.UP, publisher.popLastCommand());
        waitFor(100);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", StopMoveType.STOP, publisher.popLastCommand());
        waitFor(100);
        binding.valueChanged(valueParameterAddress, ButtonState.PRESSED);
        binding.valueChanged(valueParameterAddress, ButtonState.RELEASED);
        assertEquals("Update State", UpDownType.UP, publisher.popLastCommand());
        assertEquals("No new state expected", null, publisher.popLastCommand());
    }

    private void waitFor(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
