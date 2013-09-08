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
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.openhab.binding.enocean.internal.config.EnoceanGenericBindingProvider.EnoceanBindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

public class BindingConfigParserTest {

    @Test
    public void testParseConfigFullFilled() throws BindingConfigParseException {
        BindingConfigParser<EnoceanBindingConfig> parser = new BindingConfigParser<EnoceanBindingConfig>();
        EnoceanBindingConfig config = new EnoceanGenericBindingProvider().new EnoceanBindingConfig();
        parser.parse("{id=00:8B:62:43, eep=F6:02:01, channel=B, parameter=I_PRESSED}", config);
        assertEquals("id", "00:8B:62:43", config.id);
        assertEquals("eep", "F6:02:01", config.eep);
        assertEquals("channel", "B", config.channel);
        assertEquals("parameter", "I_PRESSED", config.parameter);
    }

    @Test
    public void testParseConfigIdFilled() throws BindingConfigParseException {
        BindingConfigParser<EnoceanBindingConfig> parser = new BindingConfigParser<EnoceanBindingConfig>();
        EnoceanBindingConfig config = new EnoceanGenericBindingProvider().new EnoceanBindingConfig();
        parser.parse("{id=00:8B:62:43}", config);
        assertEquals("id", "00:8B:62:43", config.id);
        assertNull("eep", config.eep);
        assertNull("channel", config.channel);
        assertNull("parameter", config.parameter);
    }

    @Test
    public void testParseConfigIdAndParameterFilled() throws BindingConfigParseException {
        BindingConfigParser<EnoceanBindingConfig> parser = new BindingConfigParser<EnoceanBindingConfig>();
        EnoceanBindingConfig config = new EnoceanGenericBindingProvider().new EnoceanBindingConfig();
        parser.parse("{id=00:8B:62:43, parameter=I_PRESSED}", config);
        assertEquals("id", "00:8B:62:43", config.id);
        assertEquals("parameter", "I_PRESSED", config.parameter);
    }

}
