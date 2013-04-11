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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

@Ignore("Switch Item not yet suppported")
public class WaterSensorSwitchItemTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new SwitchItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void allBindingsChangedNoWater() {
        String sensor = "0";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OnOffType.OFF);
    }

    @Test
    public void allBindingsChangedHumid() {
        String sensor = "1";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OnOffType.OFF);
    }

    @Test
    public void allBindingsChangedWater() {
        String sensor = "2";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OnOffType.ON);
    }

    @Test
    public void receiveSensorUpdateNoWater() {
        String sensor = "0";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OnOffType.OFF);
    }

    @Test
    public void receiveSensorUpdateHumid() {
        String sensor = "1";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OnOffType.OFF);
    }

    @Test
    public void receiveSensorUpdateWater() {
        String sensor = "2";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OnOffType.ON);
    }

}
