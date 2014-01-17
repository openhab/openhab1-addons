/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import org.openhab.binding.homematic.internal.converter.command.IncreaseDecreasePercentageCommandConverter;
import org.openhab.binding.homematic.internal.converter.command.OnOffPercentageCommandConverter;
import org.openhab.binding.homematic.internal.converter.command.StopMoveBooleanCommandConverter;
import org.openhab.binding.homematic.internal.converter.state.BooleanOnOffConverter;
import org.openhab.binding.homematic.internal.converter.state.BooleanOpenCloseConverter;
import org.openhab.binding.homematic.internal.converter.state.BrightnessConverter;
import org.openhab.binding.homematic.internal.converter.state.DoubleOnOffConverter;
import org.openhab.binding.homematic.internal.converter.state.DoublePercentageConverter;
import org.openhab.binding.homematic.internal.converter.state.DoubleUpDownConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerDecimalConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerOnOffConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerPercentConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerPercentageOnOffConverter;
import org.openhab.binding.homematic.internal.converter.state.IntegerPercentageOpenClosedConverter;
import org.openhab.binding.homematic.internal.converter.state.NegativeBooleanOnOffConverter;
import org.openhab.binding.homematic.internal.converter.state.TemperatureConverter;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;

/**
 * StateConverterLookupByParameterIdConfigurer.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class StateConverterLookupByParameterIdConfigurer {

    public void configure(StateConverterLookupByParameterId converterLookup) {
        converterLookup.addStateConverter(ParameterKey.INSTALL_TEST.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterLookup.addStateConverter(ParameterKey.BRIGHTNESS.name(), PercentType.class, BrightnessConverter.class);
        converterLookup.addStateConverter(ParameterKey.BRIGHTNESS.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterLookup.addStateConverter(ParameterKey.PRESS_SHORT.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterLookup.addStateConverter(ParameterKey.PRESS_LONG.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterLookup.addStateConverter(ParameterKey.PRESS_LONG_RELEASE.name(), OnOffType.class, NegativeBooleanOnOffConverter.class);
        converterLookup.addStateConverter(ParameterKey.PRESS_CONT.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterLookup.addStateConverter(ParameterKey.HUMIDITY.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterLookup.addStateConverter(ParameterKey.HUMIDITY.name(), PercentType.class, IntegerPercentConverter.class);

        converterLookup.addStateConverter(ParameterKey.LEVEL.name(), PercentType.class, DoublePercentageConverter.class);
        converterLookup.addStateConverter(ParameterKey.LEVEL.name(), UpDownType.class, DoubleUpDownConverter.class);
        converterLookup.addStateConverter(ParameterKey.LEVEL.name(), OnOffType.class, DoubleOnOffConverter.class);
        // Roller shutter: convert Stop to Off and Off to FALSE. Set this at the STOP parameter
        converterLookup.addStateConverter(ParameterKey.STOP.name(), OnOffType.class, NegativeBooleanOnOffConverter.class);

        converterLookup.addStateConverter(ParameterKey.MOTION.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterLookup.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterLookup.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterLookup.addStateConverter(ParameterKey.STATE.name(), OpenClosedType.class, BooleanOpenCloseConverter.class);

        converterLookup.addStateConverter(ParameterKey.TEMPERATURE.name(), DecimalType.class, TemperatureConverter.class);
        converterLookup.addStateConverter(ParameterKey.SETPOINT.name(), DecimalType.class, TemperatureConverter.class);
        converterLookup.addStateConverter(ParameterKey.MODE_TEMPERATUR_VALVE.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterLookup.addStateConverter(ParameterKey.VALVE_STATE.name(), PercentType.class, IntegerPercentConverter.class);
        converterLookup.addStateConverter(ParameterKey.VALVE_STATE.name(), OnOffType.class, IntegerPercentageOnOffConverter.class);
        converterLookup
                .addStateConverter(ParameterKey.VALVE_STATE.name(), OpenClosedType.class, IntegerPercentageOpenClosedConverter.class);

        converterLookup.addStateConverter(ParameterKey.ERROR.name(), OnOffType.class, IntegerOnOffConverter.class);
        converterLookup.addStateConverter(ParameterKey.ERROR.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterLookup.addStateConverter(ParameterKey.UNREACH.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterLookup.addStateConverter(ParameterKey.LOWBAT.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterLookup.addCommandConverter(ParameterKey.LEVEL.name(), OnOffType.class, OnOffPercentageCommandConverter.class);
        converterLookup.addCommandConverter(ParameterKey.LEVEL.name(), IncreaseDecreaseType.class,
                IncreaseDecreasePercentageCommandConverter.class);
        converterLookup.addCommandConverter(ParameterKey.LEVEL.name(), StopMoveType.class, StopMoveBooleanCommandConverter.class);

    }

}
