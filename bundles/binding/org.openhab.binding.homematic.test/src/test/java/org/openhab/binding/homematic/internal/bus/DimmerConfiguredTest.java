/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class DimmerConfiguredTest extends BasicBindingTest {

    private static final PercentType OPENHAB_DIMMER_OFF = new PercentType(0);
    private static final PercentType OPENHAB_DIMMER_FULL = new PercentType(100);
    private static final Double HM_DIMMER_FULL = new Double(1);
    private static final Double HM_DIMMER_OFF = new Double(0);

    @Before
    public void setupProvider() {
        provider.setItem(new DimmerItem(ITEM_NAME));
        ccu.getPhysicalDevice(MOCK_PARAM_ADDRESS).getDeviceDescription().setType("HM-LC-Dim2T-SM");
    }

    @Test
    public void receiveValueUpdate100() {
        checkValueReceived(ParameterKey.LEVEL, HM_DIMMER_FULL, OPENHAB_DIMMER_FULL);
    }

    @Test
    public void receiveValueUpdate0() {
        checkValueReceived(ParameterKey.LEVEL, HM_DIMMER_OFF, OPENHAB_DIMMER_OFF);
    }

    @Test
    @Ignore
    public void receiveValueUpdateWhileWorkingSupport() {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, ParameterKey.LEVEL.name());
        provider.setParameterAddress(paramAddress);
        values.put(ParameterKey.LEVEL.name(), HM_DIMMER_OFF);
        binding.event("dummie", MOCK_PARAM_ADDRESS, ParameterKey.LEVEL.name(), HM_DIMMER_OFF);
        assertEquals("Update State", OPENHAB_DIMMER_OFF, publisher.getUpdateState());
        binding.event("dummie", MOCK_PARAM_ADDRESS, ParameterKey.WORKING.name(), Boolean.TRUE);
        ccu.event("dummie", MOCK_PARAM_ADDRESS, ParameterKey.WORKING.name(), Boolean.TRUE);
        binding.event("dummie", MOCK_PARAM_ADDRESS, ParameterKey.LEVEL.name(), HM_DIMMER_FULL);
        assertEquals("Update State", OPENHAB_DIMMER_OFF, publisher.getUpdateState());
        binding.event("dummie", MOCK_PARAM_ADDRESS, ParameterKey.WORKING.name(), Boolean.FALSE);
        ccu.event("dummie", MOCK_PARAM_ADDRESS, ParameterKey.WORKING.name(), Boolean.FALSE);
        binding.event("dummie", MOCK_PARAM_ADDRESS, ParameterKey.LEVEL.name(), HM_DIMMER_FULL);
        assertEquals("Update State", OPENHAB_DIMMER_FULL, publisher.getUpdateState());
    }

    @Test
    public void receiveState100() {
        checkReceiveState(ParameterKey.LEVEL, OPENHAB_DIMMER_FULL, HM_DIMMER_FULL);
    }

    @Test
    public void receiveState0() {
        checkReceiveState(ParameterKey.LEVEL, OPENHAB_DIMMER_OFF, HM_DIMMER_OFF);
    }

    @Test
    public void receiveStateON() {
        checkReceiveState(ParameterKey.LEVEL, OnOffType.ON, HM_DIMMER_FULL);
    }

    @Test
    public void receiveStateOFF() {
        checkReceiveState(ParameterKey.LEVEL, OnOffType.OFF, HM_DIMMER_OFF);
    }

    @Test
    public void testLevelReceiveDimmerOnCommand() {
        checkReceiveCommand(ParameterKey.LEVEL, OnOffType.ON, HM_DIMMER_FULL);
    }

    @Test
    public void testLevelReceiveDimmerOffCommand() {
        checkReceiveCommand(ParameterKey.LEVEL, OnOffType.OFF, HM_DIMMER_OFF);
    }

    @Test
    public void receiveCommand0() {
        checkReceiveCommand(ParameterKey.LEVEL, OPENHAB_DIMMER_OFF, HM_DIMMER_OFF);
    }

    @Test
    public void receiveCommand100() {
        checkReceiveCommand(ParameterKey.LEVEL, OPENHAB_DIMMER_FULL, HM_DIMMER_FULL);
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommand() {
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.INCREASE, new Double(0.10));
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommandFromValue() {
        NumberItem item = new NumberItem(ITEM_NAME);
        item.setState(new PercentType(10));
        provider.setItem(item);
        binding.updateItemState(ITEM_NAME, item.getState());
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.INCREASE, new Double(0.20));
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommandFromMaxValue() {
        NumberItem item = new NumberItem(ITEM_NAME);
        item.setState(OPENHAB_DIMMER_FULL);
        provider.setItem(item);
        binding.updateItemState(ITEM_NAME, item.getState());
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.INCREASE, new Double(1));
    }

    @Test
    public void testLevelReceiveDimmerDecreaseCommand() {
        NumberItem item = new NumberItem(ITEM_NAME);
        item.setState(new PercentType(20));
        provider.setItem(item);
        binding.updateItemState(ITEM_NAME, item.getState());
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.DECREASE, new Double(0.10));
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommandFromNull() {
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.DECREASE, new Double(0));
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommandFromMinValue() {
        NumberItem item = new NumberItem(ITEM_NAME);
        item.setState(OPENHAB_DIMMER_OFF);
        provider.setItem(item);
        binding.updateItemState(ITEM_NAME, item.getState());
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.DECREASE, new Double(0));
    }

}
