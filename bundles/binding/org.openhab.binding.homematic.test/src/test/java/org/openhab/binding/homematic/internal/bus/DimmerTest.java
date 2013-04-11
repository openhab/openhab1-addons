/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.homematic.internal.bus;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.ParameterAddress;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class DimmerTest extends BasicBindingTest {

    private static final PercentType OPENHAB_DIMMER_OFF = new PercentType(0);
    private static final PercentType OPENHAB_DIMMER_FULL = new PercentType(100);
    private static final Double HM_DIMMER_FULL = new Double(1);
    private static final Double HM_DIMMER_OFF = new Double(0);

    @Before
    public void setupProvider() {
        provider.setItem(new DimmerItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
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
        ParameterAddress paramAddress = ParameterAddress.from(MOCK_PARAM_ADDRESS, ParameterKey.LEVEL.name());
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
        NumberItem item = new NumberItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME);
        item.setState(new PercentType(10));
        provider.setItem(item);
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.INCREASE, new Double(0.20));
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommandFromMaxValue() {
        NumberItem item = new NumberItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME);
        item.setState(OPENHAB_DIMMER_FULL);
        provider.setItem(item);
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.INCREASE, new Double(1));
    }

    @Test
    public void testLevelReceiveDimmerDecreaseCommand() {
        NumberItem item = new NumberItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME);
        item.setState(new PercentType(20));
        provider.setItem(item);
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.DECREASE, new Double(0.10));
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommandFromNull() {
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.DECREASE, new Double(0));
    }

    @Test
    public void testLevelReceiveDimmerIncreaseCommandFromMinValue() {
        NumberItem item = new NumberItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME);
        item.setState(OPENHAB_DIMMER_OFF);
        provider.setItem(item);
        checkReceiveCommand(ParameterKey.LEVEL, IncreaseDecreaseType.DECREASE, new Double(0));
    }

}
