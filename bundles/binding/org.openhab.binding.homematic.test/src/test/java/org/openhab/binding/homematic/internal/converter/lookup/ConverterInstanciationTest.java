package org.openhab.binding.homematic.internal.converter.lookup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openhab.binding.homematic.internal.converter.lookup.ConverterInstanciation;
import org.openhab.binding.homematic.internal.converter.state.TemperatureConverter;

public class ConverterInstanciationTest {

    @Test
    public void testInstantiate() {
        assertNotNull(ConverterInstanciation.instantiate(TemperatureConverter.class));
    }

    @Test
    public void testInstantiateRightClass() {
        assertEquals(TemperatureConverter.class, ConverterInstanciation.instantiate(TemperatureConverter.class).getClass());
    }

}
