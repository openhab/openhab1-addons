package org.openhab.binding.homematic.internal.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.openhab.binding.homematic.internal.config.HomematicGenericBindingProvider.HomematicBindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

public class BindingConfigParserTest {

    @Test
    public void testParseConfigFullFilled() throws BindingConfigParseException {
        BindingConfigParser parser = new BindingConfigParser();
        HomematicBindingConfig config = new HomematicBindingConfig();
        parser.parse("{id=GEQ00112233, channel=1, parameter=LEVEL}", config);
        assertEquals("id", "GEQ00112233", config.id);
        assertEquals("channel", "1", config.channel);
        assertEquals("parameter", "LEVEL", config.parameter);
    }

    @Test
    public void testParseConfigIdFilled() throws BindingConfigParseException {
        BindingConfigParser parser = new BindingConfigParser();
        HomematicBindingConfig config = new HomematicBindingConfig();
        parser.parse("{id=GEQ00112233}", config);
        assertEquals("id", "GEQ00112233", config.id);
        assertNull("channel", config.channel);
        assertNull("parameter", config.parameter);
    }

    @Test
    public void testParseConfigIdAndParameterFilled() throws BindingConfigParseException {
        BindingConfigParser parser = new BindingConfigParser();
        HomematicBindingConfig config = new HomematicBindingConfig();
        parser.parse("{id=ADMIN, parameter=DUMP_UNCONFIGURED_DEVICES}", config);
        assertEquals("id", "ADMIN", config.id);
        assertEquals("parameter", "DUMP_UNCONFIGURED_DEVICES", config.parameter);
    }

}
