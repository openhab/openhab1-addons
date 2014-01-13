package org.openhab.binding.homematic.internal.converter.lookup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.converter.state.BooleanOnOffConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerDecimalConverter;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class StateConverterLookupByParameterIdTest {

    private static final String SWITCH_ITEM_NAME = "SwitchItem";
    private static final String NUMBER_ITEM_NAME = "NumberItem";
    private static final String DIMMER_ITEM_NAME = "DimmerItem";

    private StateConverterLookupByParameterId parameterIdToStateConverterMap;
    private HomematicParameterAddress numberItemAddress;
    private HomematicParameterAddress switchItemAddress;

    @Before
    public void setUp() {
        parameterIdToStateConverterMap = new StateConverterLookupByParameterId();
        numberItemAddress = new HomematicParameterAddress("dimmer", "channel", ParameterKey.STATE.name());
        switchItemAddress = new HomematicParameterAddress("button", "channel", ParameterKey.STATE.name());
    }

    @Test
    public void testGetToStateConverter() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        StateConverter<?, ?> toStateConverter = parameterIdToStateConverterMap.getBindingValueToStateConverter(new NumberItem(NUMBER_ITEM_NAME),
                numberItemAddress);
        assertNotNull(toStateConverter);
        assertEquals(IntegerDecimalConverter.class, toStateConverter.getClass());
    }

    @Test
    public void testGetToStateConverterForDifferentKey() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.BRIGHTNESS.name(), OnOffType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        StateConverter<?, ?> toStateConverter = parameterIdToStateConverterMap.getBindingValueToStateConverter(new SwitchItem(SWITCH_ITEM_NAME),
                switchItemAddress);
        assertNotNull(toStateConverter);
        assertEquals(BooleanOnOffConverter.class, toStateConverter.getClass());
    }

    @Test
    public void testGetToStateConverterForDifferentState() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        StateConverter<?, ?> toStateConverterNumberItem = parameterIdToStateConverterMap.getBindingValueToStateConverter(
                new NumberItem(NUMBER_ITEM_NAME), numberItemAddress);
        assertEquals(IntegerDecimalConverter.class, toStateConverterNumberItem.getClass());
        StateConverter<?, ?> toStateConverterSwitchItem = parameterIdToStateConverterMap.getBindingValueToStateConverter(
                new SwitchItem(SWITCH_ITEM_NAME), switchItemAddress);
        assertEquals(BooleanOnOffConverter.class, toStateConverterSwitchItem.getClass());
    }

    @Test
    public void testGetToStateConverterWithMultipleMatches() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), PercentType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        StateConverter<?, ?> toStateConverterNumberItem = parameterIdToStateConverterMap.getBindingValueToStateConverter(
                new DimmerItem(DIMMER_ITEM_NAME), numberItemAddress);
        assertEquals(IntegerDecimalConverter.class, toStateConverterNumberItem.getClass());
    }

    @Test
    public void testGetToStateConverterWithMultipleMatchesInvertedOrder() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), PercentType.class, IntegerDecimalConverter.class);
        StateConverter<?, ?> toStateConverterNumberItem = parameterIdToStateConverterMap.getBindingValueToStateConverter(
                new DimmerItem(DIMMER_ITEM_NAME), numberItemAddress);
        assertEquals(IntegerDecimalConverter.class, toStateConverterNumberItem.getClass());
    }

    @Test
    public void testAddStateConverter() {
        StateConverterLookupByParameterId converterMap = new StateConverterLookupByParameterId();
        converterMap.addStateConverter(ParameterKey.INSTALL_TEST.name(), OnOffType.class, BooleanOnOffConverter.class);
        StateConverterMap stateConverterMap = converterMap.get(ParameterKey.INSTALL_TEST.name());
        assertNotNull(stateConverterMap);
        assertEquals("Size of stateConverterMap", 1, stateConverterMap.size());
        assertEquals("stateConverterMap key", OnOffType.class, stateConverterMap.keySet().iterator().next());
    }

}
