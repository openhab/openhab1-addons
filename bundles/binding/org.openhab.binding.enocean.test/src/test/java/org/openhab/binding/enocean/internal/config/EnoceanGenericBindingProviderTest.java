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
package org.openhab.binding.enocean.internal.config;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.enocean.java.address.EnoceanId;
import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.eep.EEPId;
import org.junit.Test;
import org.openhab.core.items.GenericItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;

public class EnoceanGenericBindingProviderTest {

    @Test
    public void testGetParameterAddress() throws BindingConfigParseException {
        EnoceanGenericBindingProvider provider = new EnoceanGenericBindingProvider();
        provider.processBindingConfiguration("enocean", new TestItem("item"),
                "{id=00:8B:62:43, eep=F6:02:01, channel=B, parameter=I_PRESSED}");
        assertEquals(new EnoceanParameterAddress(EnoceanId.fromString("00:8B:62:43"), "B", "I_PRESSED"),
                provider.getParameterAddress("item"));
    }

    @Test
    public void testGetEEP() throws BindingConfigParseException {
        EnoceanGenericBindingProvider provider = new EnoceanGenericBindingProvider();
        provider.processBindingConfiguration("enocean", new TestItem("item"),
                "{id=00:8B:62:43, eep=F6:02:01, channel=B, parameter=I_PRESSED}");
        assertEquals(new EEPId("F6:02:01"), provider.getEEP("item"));
    }

    class TestItem extends GenericItem {

        public TestItem(String name) {
            super(name);
        }

        public List<Class<? extends State>> getAcceptedDataTypes() {
            // TODO Auto-generated method stub
            return null;
        }

        public List<Class<? extends Command>> getAcceptedCommandTypes() {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
