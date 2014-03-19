/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;

public class RollershutterTest extends BasicBindingTest {

    private static final ParameterKey PARAMETER_KEY = ParameterKey.LEVEL;
    private static final PercentType OPENHAB_BLINDS_UP = new PercentType(0);
    private static final PercentType OPENHAB_BLINDS_DOWN = new PercentType(100);
    private static final Double HM_BLINDS_UP = new Double(1);
    private static final Double HM_BLINDS_DOWN = new Double(0);

    @Before
    public void setupProvider() {
        provider.setItem(new RollershutterItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
        provider.setParameterAddress(HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, PARAMETER_KEY.name()));
        ccu.getPhysicalDevice(MOCK_PARAM_ADDRESS).getDeviceDescription().setType("HM-LC-Bl1-FM");
    }

    @Test
    public void allBindingsChanged100() {
        checkInitialValue(PARAMETER_KEY, HM_BLINDS_UP, OPENHAB_BLINDS_UP);
    }

    @Test
    public void allBindingsChanged0() {
        checkInitialValue(PARAMETER_KEY, HM_BLINDS_DOWN, OPENHAB_BLINDS_DOWN);
    }

    @Test
    public void receiveValueUpdate100() {
        checkValueReceived(PARAMETER_KEY, HM_BLINDS_UP, OPENHAB_BLINDS_UP);
    }

    @Test
    public void receiveValueUpdate0() {
        checkValueReceived(PARAMETER_KEY, HM_BLINDS_DOWN, OPENHAB_BLINDS_DOWN);
    }

    @Test
    public void receiveStateUP() {
        checkReceiveState(PARAMETER_KEY, UpDownType.UP, HM_BLINDS_UP);
    }

    @Test
    public void receiveStateDOWN() {
        checkReceiveState(PARAMETER_KEY, UpDownType.DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveState100() {
        checkReceiveState(PARAMETER_KEY, OPENHAB_BLINDS_UP, HM_BLINDS_UP);
    }

    @Test
    public void receiveState0() {
        checkReceiveState(PARAMETER_KEY, OPENHAB_BLINDS_DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveCommandUP() {
        checkReceiveCommand(PARAMETER_KEY, UpDownType.UP, HM_BLINDS_UP);
    }

    @Test
    public void receiveCommandDOWN() {
        checkReceiveCommand(PARAMETER_KEY, UpDownType.DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveCommandSTOP() {
        checkReceiveCommand(PARAMETER_KEY, ParameterKey.STOP, StopMoveType.STOP, Boolean.TRUE);
    }

    @Test
    public void receiveCommandMOVE() {
        checkReceiveCommand(PARAMETER_KEY, ParameterKey.STOP, StopMoveType.MOVE, Boolean.FALSE);
    }

    @Test
    public void receiveCommand0() {
        checkReceiveCommand(PARAMETER_KEY, OPENHAB_BLINDS_DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveCommand100() {
        checkReceiveCommand(PARAMETER_KEY, OPENHAB_BLINDS_UP, HM_BLINDS_UP);
    }

}
