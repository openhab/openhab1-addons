/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.device;

import java.util.HashMap;
import java.util.Map;

import eu.aleon.aleoncean.values.OpenClosed;
import eu.aleon.aleoncean.values.RockerSwitchAction;
import eu.aleon.aleoncean.values.WindowHandlePosition;

/**
 * Collection of device parameters.
 *
 * To optimize for access speed and memory footprint, we use static objects.
 * For every parameter you add, you have to extend the "infos" member of the "Infos" class.
 *
 * - BUTTON_DIM_A/B: RockerSwitchAction
 * - ENERGY_WS: long, power value, unit watt seconds
 * - HUMIDITY_PERCENT: double, humidity, unit percent
 * - ILLUMINATION_LUX: double, illumination, unit lux
 * - MOTION: boolean, motion
 * - OCCUPANCY_BUTTON: boolean, true: pressed, false: released
 * - POSITION_PERCENT: integer, a position value, unit percent
 * - POWER_W: long, power value, unit watt
 * - SETPOINT_POSITION_PERCENT: integer, a set point position value, unit percent
 * - SETPOINT_TEMPERATURE_CELSIUS: double, a set point temperature value, unit celsius
 * - SUPPLY_VOLTAGE_V: double, unit volt
 * - SWITCH: boolean, true: on, false: off
 * - TEMPERATURE_CELSIUS: double, temperature, unit degree Celsius
 * - TEMPERATURE_CONTROL_ENABLE: boolean, flag if a temperature control algorithm is enabled
 * - TEMPERATURE_CONTROL_CUR_TEMP: double, the current temperature used for a temperature control algorithm
 * - WINDOW_HANDLE_POSITION: WindowHandlePosition
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public enum DeviceParameter {

    BUTTON_DIM_A("BUTTON_DIM_A", RockerSwitchAction.class),
    BUTTON_DIM_B("BUTTON_DIM_B", RockerSwitchAction.class),
    ENERGY_WS("ENERGY_WS", Double.class),
    HUMIDITY_PERCENT("HUMIDITY_PERCENT", Double.class),
    ILLUMINATION_LUX("ILLUMINATION_LUX", Double.class),
    MOTION("MOTION", Boolean.class),
    OCCUPANCY_BUTTON("OCCUPANCY_BUTTON", Boolean.class),
    OPEN_CLOSED(OpenClosed.class),
    POSITION_PERCENT(Integer.class),
    POWER_W(Double.class),
    SETPOINT_POSITION_PERCENT(Integer.class),
    SETPOINT_TEMPERATURE_CELSIUS(Double.class),
    SUPPLY_VOLTAGE_V("SUPPLY_VOLTAGE_V", Double.class),
    SWITCH("SWITCH", Boolean.class),
    TEMPERATURE_CELSIUS("TEMPERATURE_CELSIUS", Double.class),
    TEMPERATURE_CONTROL_ENABLE(Boolean.class),
    TEMPERATURE_CONTROL_CUR_TEMP(Double.class),
    WINDOW_HANDLE_POSITION("WINDOW_HANDLE_POSITION", WindowHandlePosition.class),

    /* temporary workaround to force sending configuration to device */
    TMP_SEND_CONFIGURATION(Boolean.class),

    TMP_RECV_SERVICE_ON(Boolean.class),
    TMP_RECV_ENERGY_INPUT_ENABLED(Boolean.class),
    TMP_RECV_ENERGY_STORAGE_SUFFICIENT(Boolean.class),
    TMP_RECV_CHANGE_BATTERY(Boolean.class),
    TMP_RECV_COVER_OPEN(Boolean.class),
    TMP_RECV_TEMPERATURE_SENSOR_FAILURE(Boolean.class),
    TMP_RECV_WINDOW_OPEN(Boolean.class),
    TMP_RECV_ACTUATOR_OBSTRUCTED(Boolean.class),
    TMP_SEND_RUN_INIT_SEQUENCE(Boolean.class),
    TMP_SEND_LIFT_SET(Boolean.class),
    TMP_SEND_VALVE_OPEN(Boolean.class),
    TMP_SEND_VALVE_CLOSED(Boolean.class),
    TMP_SEND_REDUCED_ENERGY_CONSUMPTION(Boolean.class);

    private final Class<?> clazz;
    private final String userName;

    private DeviceParameter(final Class<?> clazz) {
        this(null, clazz);
    }

    private DeviceParameter(final String userName, final Class<?> clazz) {
        if (userName == null) {
            this.userName = name();
        } else {
            this.userName = userName;
        }
        this.clazz = clazz;
    }

    private Class<?> getSupportedClass() {
        return clazz;
    }

    private String getUserName() {
        return userName;
    }

    private static final Map<String, DeviceParameter> NAME_TO_PARAMETER_MAPPING;

    static {
        NAME_TO_PARAMETER_MAPPING = new HashMap<>();
        for (final DeviceParameter param : DeviceParameter.values()) {
            NAME_TO_PARAMETER_MAPPING.put(param.getUserName(), param);
        }
    }

    /**
     * Get device parameter by the correspondent string representation.
     *
     * @param parameter The string that identifies the parameter.
     * @return Return a device parameter if the string is known, null if it is unknown.
     */
    public static DeviceParameter fromString(final String parameter) {
        return NAME_TO_PARAMETER_MAPPING.get(parameter);
    }

    public static Class<?> getSupportedClass(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        return parameter.getSupportedClass();
    }

}
