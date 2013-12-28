package org.openhab.binding.homematic.internal.converter;

import java.util.List;

import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.DeviceConfigLocator;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;

public class ConverterFactoryBuilder {

    public ConverterFactory build() {
        ConverterFactory converterFactory = new ConverterFactory();
        converterFactory.addStateConverter(ParameterKey.INSTALL_TEST.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.BRIGHTNESS.name(), PercentType.class, BrightnessConverter.class);
        converterFactory.addStateConverter(ParameterKey.BRIGHTNESS.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterFactory.addStateConverter(ParameterKey.PRESS_SHORT.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.PRESS_LONG.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.PRESS_LONG_RELEASE.name(), OnOffType.class, NegativeBooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.PRESS_CONT.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.HUMIDITY.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterFactory.addStateConverter(ParameterKey.HUMIDITY.name(), PercentType.class, IntegerPercentConverter.class);

        converterFactory.addStateConverter(ParameterKey.LEVEL.name(), PercentType.class, DoublePercentageConverter.class);
        converterFactory.addStateConverter(ParameterKey.LEVEL.name(), UpDownType.class, DoubleUpDownConverter.class);
        converterFactory.addStateConverter(ParameterKey.LEVEL.name(), OnOffType.class, DoubleOnOffConverter.class);
        converterFactory.addCommandConverter(ParameterKey.LEVEL.name(), OnOffType.class, OnOffPercentageCommandConverter.class);
        converterFactory.addCommandConverter(ParameterKey.LEVEL.name(), IncreaseDecreaseType.class,
                IncreaseDecreasePercentageCommandConverter.class);
        // Roller shutter: convert Stop to Off and Off to FALSE. Set this at the
        // STOP parameter
        converterFactory.addStateConverter(ParameterKey.STOP.name(), OnOffType.class, NegativeBooleanOnOffConverter.class);
        converterFactory.addCommandConverter(ParameterKey.LEVEL.name(), StopMoveType.class, StopMoveBooleanCommandConverter.class);

        converterFactory.addStateConverter(ParameterKey.MOTION.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterFactory.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.STATE.name(), OpenClosedType.class, BooleanOpenCloseConverter.class);

        converterFactory.addStateConverter(ParameterKey.TEMPERATURE.name(), DecimalType.class, TemperatureConverter.class);
        converterFactory.addStateConverter(ParameterKey.SETPOINT.name(), DecimalType.class, TemperatureConverter.class);
        converterFactory.addStateConverter(ParameterKey.MODE_TEMPERATUR_VALVE.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterFactory.addStateConverter(ParameterKey.VALVE_STATE.name(), PercentType.class, IntegerPercentConverter.class);
        converterFactory.addStateConverter(ParameterKey.VALVE_STATE.name(), OnOffType.class, IntegerPercentageOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.VALVE_STATE.name(), OpenClosedType.class,
                IntegerPercentageOpenClosedConverter.class);

        converterFactory.addStateConverter(ParameterKey.ERROR.name(), OnOffType.class, IntegerOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.ERROR.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterFactory.addStateConverter(ParameterKey.UNREACH.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.LOWBAT.name(), OnOffType.class, BooleanOnOffConverter.class);

        DeviceConfigLocator locator = new DeviceConfigLocator("HM-CC-RT-DN.xml", "HM-LC-Dim1L-Pl.xml", "HM-LC-BI1PBU-FM.xml",
                "HM-LC-Bl1-FM.xml", "HM-LC-Dim2L-SM.xml", "HM-LC-Dim2L-CV.xml", "HM-LC-Dim1L-CV.xml", "HM-LC-Dim1T-Pl.xml",
                "HM-LC-Dim1T-CV.xml", "HM-LC-Dim2T-SM.xml", "HM-PB-4DIS-WM.xml", "HM-Sec-SD.xml", "HM-Sec-SC.xml", "HM-Sec-RHS.xml");
        List<ConfiguredDevice> configuredDevices = locator.findAll();
        converterFactory.addConfiguredDevices(configuredDevices);
        return converterFactory;
    }

}
