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
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.types.OpenClosedType;

public class RHSSensorContactItemTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new ContactItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
        provider.setParameterAddress(HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, ParameterKey.STATE.name()));
        ccu.getPhysicalDevice(MOCK_PARAM_ADDRESS).getDeviceDescription().setType("HM-Sec-RHS");
    }

    @Test
    public void allBindingsChangedClosed() {
        String sensor = "0";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OpenClosedType.CLOSED);
    }

    @Test
    public void allBindingsChangedTilted() {
        String sensor = "1";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }

    @Test
    public void allBindingsChangedOpen() {
        String sensor = "2";
        checkInitialValue(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }

    @Test
    public void receiveSensorUpdateClosed() {
        String sensor = "0";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OpenClosedType.CLOSED);
    }

    @Test
    public void receiveSensorUpdateTilted() {
        String sensor = "1";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }

    @Test
    public void receiveSensorUpdateOpen() {
        String sensor = "2";
        checkValueReceived(ParameterKey.STATE, new Integer(sensor), OpenClosedType.OPEN);
    }
}
