package org.openhab.binding.homematic.internal.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.core.library.types.PercentType;

public class InvertedDoublePercentageConverterTest {

    private InvertedDoublePercentageConverter underTest = new InvertedDoublePercentageConverter();

    @Test
    public void testConvertTo() {
        assertEquals(new PercentType(0), underTest.convertTo(1.0));
    }

    @Test
    public void testConvertFrom() {
        assertEquals((Double) 1.0, underTest.convertFrom(new PercentType(0)));
    }

}
