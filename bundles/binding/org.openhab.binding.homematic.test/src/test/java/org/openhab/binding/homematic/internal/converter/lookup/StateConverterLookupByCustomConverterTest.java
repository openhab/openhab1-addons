package org.openhab.binding.homematic.internal.converter.lookup;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.binding.homematic.internal.converter.state.IntegerDecimalConverter;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.library.items.NumberItem;

public class StateConverterLookupByCustomConverterTest {

    private static final String NUMBER_ITEM_NAME = "NumberItem";

    private StateConverterLookupByCustomConverter converterLookup = new StateConverterLookupByCustomConverter();

    @Test
    public void testGetStateToBindingValueConverter() {
        NumberItem item = new NumberItem(NUMBER_ITEM_NAME);
        converterLookup.addCustomConverter(NUMBER_ITEM_NAME, IntegerDecimalConverter.class);
        StateConverterMap converter = converterLookup.getStateToBindingValueConverter(item, null);
        assertEquals(IntegerDecimalConverter.class, converter.get("bla").getClass());
    }

    @Test
    public void testGetBindingValueToStateConverter() {
        NumberItem item = new NumberItem(NUMBER_ITEM_NAME);
        converterLookup.addCustomConverter(NUMBER_ITEM_NAME, IntegerDecimalConverter.class);
        StateConverter<?, ?> converter = converterLookup.getBindingValueToStateConverter(item, null);
        assertEquals(IntegerDecimalConverter.class, converter.getClass());
    }

}
