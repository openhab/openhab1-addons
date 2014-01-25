package org.openhab.binding.homematic.internal.converter.lookup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.DeviceConfigParser;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.converter.state.BooleanOnOffConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerDecimalConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerPercentConverter;
import org.openhab.binding.homematic.internal.converter.state.InvertedDoublePercentageConverter;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.CCUMock;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class ConverterLookupTest {

    private static final String SWITCH_ITEM_NAME = "SwitchItem";
    private static final String NUMBER_ITEM_NAME = "NumberItem";
    private ConverterLookup converterLookup;
    private HomematicParameterAddress numberItemAddress;
    private HomematicParameterAddress switchItemAddress;
    private StateConverterLookupByParameterId parameterIdToStateConverterMap;
    private StateConverterLookupByCustomConverter stateConverterLookupByCustomConverter;
    private StateConverterLookupByConfiguredDevices stateConverterLookupByConfiguredDevices;

    @Test
    public void testGetBindingValueToStateConverter() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterLookup.configureItem(new NumberItem(NUMBER_ITEM_NAME), numberItemAddress);
        StateConverter<?, ?> toStateConverter = converterLookup.getBindingValueToStateConverter(NUMBER_ITEM_NAME);
        assertNotNull(toStateConverter);
        assertEquals(IntegerDecimalConverter.class, toStateConverter.getClass());
    }

    @Test
    public void testGetBindingValueToStateConverterForDifferentKey() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.BRIGHTNESS.name(), OnOffType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterLookup.configureItem(new SwitchItem(SWITCH_ITEM_NAME), switchItemAddress);
        StateConverter<?, ?> toStateConverter = converterLookup.getBindingValueToStateConverter(SWITCH_ITEM_NAME);
        assertNotNull(toStateConverter);
        assertEquals(BooleanOnOffConverter.class, toStateConverter.getClass());
    }

    @Test
    public void testGetBindingValueToStateConverterForDifferentState() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterLookup.configureItem(new NumberItem(NUMBER_ITEM_NAME), numberItemAddress);
        converterLookup.configureItem(new SwitchItem(SWITCH_ITEM_NAME), switchItemAddress);
        StateConverter<?, ?> toStateConverterNumberItem = converterLookup.getBindingValueToStateConverter(NUMBER_ITEM_NAME);
        assertEquals(IntegerDecimalConverter.class, toStateConverterNumberItem.getClass());
        StateConverter<?, ?> toStateConverterSwitchItem = converterLookup.getBindingValueToStateConverter(SWITCH_ITEM_NAME);
        assertEquals(BooleanOnOffConverter.class, toStateConverterSwitchItem.getClass());
    }

    @Test
    public void testGetBindingValueToStateConverterWithCustomConverter() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        stateConverterLookupByCustomConverter.addCustomConverter(NUMBER_ITEM_NAME, IntegerPercentConverter.class);
        converterLookup.configureItem(new NumberItem(NUMBER_ITEM_NAME), numberItemAddress);
        StateConverter<?, ?> toStateConverterNumberItem = converterLookup.getBindingValueToStateConverter(NUMBER_ITEM_NAME);
        assertEquals(IntegerPercentConverter.class, toStateConverterNumberItem.getClass());
    }

    @Test
    public void testGetStateToBindingValueConverter() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterLookup.configureItem(new NumberItem(NUMBER_ITEM_NAME), numberItemAddress);
        StateConverter<?, ?> toStateConverter = converterLookup.getStateToBindingValueConverter(NUMBER_ITEM_NAME, DecimalType.class);
        assertNotNull("Converter may not be null", toStateConverter);
        assertEquals(IntegerDecimalConverter.class, toStateConverter.getClass());
    }

    @Test
    public void testGetStateToBindingValueConverterForDifferentState() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterLookup.configureItem(new NumberItem(NUMBER_ITEM_NAME), numberItemAddress);
        StateConverter<?, ?> toStateConverterNumberItem = converterLookup.getStateToBindingValueConverter(NUMBER_ITEM_NAME,
                DecimalType.class);
        assertEquals(IntegerDecimalConverter.class, toStateConverterNumberItem.getClass());
        StateConverter<?, ?> toStateConverterSwitchItem = converterLookup
                .getStateToBindingValueConverter(NUMBER_ITEM_NAME, OnOffType.class);
        assertEquals(BooleanOnOffConverter.class, toStateConverterSwitchItem.getClass());
    }

    @Test
    public void testGetStateToBindingValueConverterWithCustomConverter() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        stateConverterLookupByCustomConverter.addCustomConverter(NUMBER_ITEM_NAME, IntegerPercentConverter.class);
        converterLookup.configureItem(new NumberItem(NUMBER_ITEM_NAME), numberItemAddress);
        StateConverter<?, ?> toStateConverterNumberItem = converterLookup.getStateToBindingValueConverter(NUMBER_ITEM_NAME,
                DecimalType.class);
        assertEquals(IntegerPercentConverter.class, toStateConverterNumberItem.getClass());
    }

    @Test
    public void testGetStateToBindingValueConverterWithConfiguredDevices() {
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        parameterIdToStateConverterMap.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        List<ConfiguredDevice> configuredDevices = new ArrayList<ConfiguredDevice>();
        DeviceConfigParser parser = new DeviceConfigParser();
        ConfiguredDevice deviceConfig = parser.parseDeviceConfig("devices/example_device_config.xml");
        configuredDevices.add(deviceConfig);
        stateConverterLookupByConfiguredDevices.addConfiguredDevices(configuredDevices);
        DimmerItem item = new DimmerItem(NUMBER_ITEM_NAME);
        converterLookup.configureItem(item, new HomematicParameterAddress("dimmer", "channel", ParameterKey.LEVEL.name()));
        StateConverter<?, ?> toStateConverterNumberItem = converterLookup.getStateToBindingValueConverter(NUMBER_ITEM_NAME,
                PercentType.class);
        assertEquals(InvertedDoublePercentageConverter.class, toStateConverterNumberItem.getClass());
    }

    @Before
    public void setUp() {
        converterLookup = new ConverterLookup();
        parameterIdToStateConverterMap = new StateConverterLookupByParameterId();
        stateConverterLookupByCustomConverter = new StateConverterLookupByCustomConverter();
        stateConverterLookupByConfiguredDevices = new StateConverterLookupByConfiguredDevices();
        CCUMock ccu = new CCUMock();
        ccu.getPhysicalDevice(null).getDeviceDescription().setType("HM-LC-Bl1-FM");
        stateConverterLookupByConfiguredDevices.setCcu(ccu);
        converterLookup.setConverterLookupByParameterId(parameterIdToStateConverterMap);
        converterLookup.setConverterLookupByCustomConverter(stateConverterLookupByCustomConverter);
        converterLookup.setConverterLookupByConfiguredDevices(stateConverterLookupByConfiguredDevices);
        numberItemAddress = new HomematicParameterAddress("dimmer", "channel", ParameterKey.STATE.name());
        switchItemAddress = new HomematicParameterAddress("button", "channel", ParameterKey.STATE.name());
    }

}
