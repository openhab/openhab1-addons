/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
