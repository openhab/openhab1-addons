/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;

@Ignore
public class RollershutterTest extends BasicBindingTest {

    private static final PercentType OPENHAB_BLINDS_UP = new PercentType(0);
    private static final PercentType OPENHAB_BLINDS_DOWN = new PercentType(100);
    private static final Double HM_BLINDS_UP = new Double(1);
    private static final Double HM_BLINDS_DOWN = new Double(0);

    @Before
    public void setupProvider() {
        provider.setItem(new RollershutterItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void allBindingsChanged100() {
        checkInitialValue(ParameterKey.LEVEL, HM_BLINDS_UP, OPENHAB_BLINDS_UP);
    }

    @Test
    public void allBindingsChanged0() {
        checkInitialValue(ParameterKey.LEVEL, HM_BLINDS_DOWN, OPENHAB_BLINDS_DOWN);
    }

    @Test
    public void receiveValueUpdate100() {
        checkValueReceived(ParameterKey.LEVEL, HM_BLINDS_UP, OPENHAB_BLINDS_UP);
    }

    @Test
    public void receiveValueUpdate0() {
        checkValueReceived(ParameterKey.LEVEL, HM_BLINDS_DOWN, OPENHAB_BLINDS_DOWN);
    }

    @Test
    public void receiveStateUP() {
        checkReceiveState(ParameterKey.LEVEL, UpDownType.UP, HM_BLINDS_UP);
    }

    @Test
    public void receiveStateDOWN() {
        checkReceiveState(ParameterKey.LEVEL, UpDownType.DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveState100() {
        checkReceiveState(ParameterKey.LEVEL, OPENHAB_BLINDS_UP, HM_BLINDS_UP);
    }

    @Test
    public void receiveState0() {
        checkReceiveState(ParameterKey.LEVEL, OPENHAB_BLINDS_DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveCommandUP() {
        checkReceiveCommand(ParameterKey.LEVEL, UpDownType.UP, HM_BLINDS_UP);
    }

    @Test
    public void receiveCommandDOWN() {
        checkReceiveCommand(ParameterKey.LEVEL, UpDownType.DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveCommandSTOP() {
        checkReceiveCommand(ParameterKey.LEVEL, ParameterKey.STOP, StopMoveType.STOP, Boolean.TRUE);
    }

    @Test
    public void receiveCommandMOVE() {
        checkReceiveCommand(ParameterKey.LEVEL, ParameterKey.STOP, StopMoveType.MOVE, Boolean.FALSE);
    }

    @Test
    public void receiveCommand0() {
        checkReceiveCommand(ParameterKey.LEVEL, OPENHAB_BLINDS_DOWN, HM_BLINDS_DOWN);
    }

    @Test
    public void receiveCommand100() {
        checkReceiveCommand(ParameterKey.LEVEL, OPENHAB_BLINDS_UP, HM_BLINDS_UP);
    }

}
