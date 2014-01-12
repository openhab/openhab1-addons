package org.openhab.binding.homematic.internal.converter.lookup;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.DeviceConfigParser;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.converter.state.InvertedDoublePercentageConverter;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.CCUMock;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.PercentType;

public class StateConverterLookupByConfiguredDevicesTest {

    private static final String NUMBER_ITEM_NAME = "NumberItem";
    private StateConverterLookupByConfiguredDevices converterLookup = new StateConverterLookupByConfiguredDevices();
    private HomematicParameterAddress numberItemAddress;

    @Before
    public void setUp() {
        CCUMock ccu = new CCUMock();
        ccu.getPhysicalDevice(null).getDeviceDescription().setType("HM-LC-Bl1-FM");
        converterLookup.setCcu(ccu);
        numberItemAddress = new HomematicParameterAddress("dimmer", "channel", ParameterKey.LEVEL.name());
    }

    @Test
    public void testGetStateToBindingValueConverter() {
        DimmerItem item = new DimmerItem(NUMBER_ITEM_NAME);
        List<ConfiguredDevice> configuredDevices = new ArrayList<ConfiguredDevice>();
        DeviceConfigParser parser = new DeviceConfigParser();
        ConfiguredDevice deviceConfig = parser.parseDeviceConfig("devices/example_device_config.xml");
        configuredDevices.add(deviceConfig);
        converterLookup.addConfiguredDevices(configuredDevices);
        StateConverterMap converter = converterLookup.getStateToBindingValueConverter(item, numberItemAddress);
        assertEquals(InvertedDoublePercentageConverter.class, converter.get(PercentType.class).getClass());
    }

    @Test
    public void testGetBindingValueToStateConverter() {
        DimmerItem item = new DimmerItem(NUMBER_ITEM_NAME);
        List<ConfiguredDevice> configuredDevices = new ArrayList<ConfiguredDevice>();
        DeviceConfigParser parser = new DeviceConfigParser();
        ConfiguredDevice deviceConfig = parser.parseDeviceConfig("devices/example_device_config.xml");
        configuredDevices.add(deviceConfig);
        converterLookup.addConfiguredDevices(configuredDevices);
        StateConverter<?, ?> converter = converterLookup.getBindingValueToStateConverter(item, numberItemAddress);
        assertEquals(InvertedDoublePercentageConverter.class, converter.getClass());
    }

}
