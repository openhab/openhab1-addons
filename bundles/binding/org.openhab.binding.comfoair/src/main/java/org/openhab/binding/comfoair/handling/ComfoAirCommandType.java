/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.comfoair.handling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.comfoair.datatypes.ComfoAirDataType;
import org.openhab.binding.comfoair.datatypes.DataTypeBoolean;
import org.openhab.binding.comfoair.datatypes.DataTypeMessage;
import org.openhab.binding.comfoair.datatypes.DataTypeNumber;
import org.openhab.binding.comfoair.datatypes.DataTypeRPM;
import org.openhab.binding.comfoair.datatypes.DataTypeTemperature;
import org.openhab.binding.comfoair.datatypes.DataTypeVolt;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all valid commands which could be processed by this binding
 *
 * @author Holger Hees
 * @since 1.3.0
 */
public enum ComfoAirCommandType {

    /**
     * Below all valid commands to change or read parameters from ComfoAir
     *
     * @param key
     *            command name
     * @param data_tape
     *            data type (can be: DataTypeBoolean.class, DataTypeMessage.class,
     *            DataTypeNumber.class, DataTypeRPM.class,
     *            DataTypeTemperature.class, DataTypeVolt.class)
     * @param possible_values
     *            possible values for write command, if it can only take certain values
     * @param change_command
     *            byte number for ComfoAir write command
     * @param change_data_size
     *            size of bytes list for ComfoAir write command
     * @param change_data_pos
     *            position in bytes list to change
     * @param change_affected
     *            list of affected commands (can be empty)
     *            is mandatory for read-write command
     * @param read_command
     *            request byte number for ComfoAir read command
     * @param read_reply_command
     *            reply byte list size for ComfoAir read command (list of values only)
     * @param read_reply_data_pos
     *            list of byte positions in reply bytes list from ComfoAir
     * @param read_reply_data_bits
     *            byte value on read_reply_data_pos position to be considered by command (used with
     *            DataTypeBoolean.class data_type)
     */
    ACTIVATE {
        {
            key = "activate";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x03 };
            change_command = 0x9b;
            change_data_size = 1;
            change_data_pos = 0;
            change_affected = new String[] {};
            read_command = 0x9c;
            read_reply_command = 0x9c;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x03;
        }
    },

    MENU20_MODE {
        {
            key = "menu20_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x01;
        }
    },

    MENU21_MODE {
        {
            key = "menu21_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x02;
        }
    },

    MENU22_MODE {
        {
            key = "menu22_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x04;
        }
    },

    MENU23_MODE {
        {
            key = "menu23_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x08;
        }
    },

    MENU24_MODE {
        {
            key = "menu24_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x10;
        }
    },

    MENU25_MODE {
        {
            key = "menu25_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x20;
        }
    },

    MENU26_MODE {
        {
            key = "menu26_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x40;
        }
    },

    MENU27_MODE {
        {
            key = "menu27_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 6 };
            read_reply_data_bits = 0x80;
        }
    },

    MENU28_MODE {
        {
            key = "menu28_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 7 };
            read_reply_data_bits = 0x01;
        }
    },

    MENU29_MODE {
        {
            key = "menu29_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 7 };
            read_reply_data_bits = 0x02;
        }
    },

    BATHROOM_START_DELAY {
        {
            key = "bathroom_start_delay";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 0;
            change_affected = new String[] { "menu21_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    BATHROOM_END_DELAY {
        {
            key = "bathroom_end_delay";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 1;
            change_affected = new String[] { "menu22_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    L1_END_DELAY {
        {
            key = "L1_end_delay";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 2;
            change_affected = new String[] { "menu27_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    LEVEL3_DELAY {
        {
            key = "level3_delay";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 3;
            change_affected = new String[] { "menu23_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    FILTER_PERIOD {
        {
            key = "filter_period";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 4;
            change_affected = new String[] { "menu24_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 4 };
        }
    },

    RF_LOW_DELAY {
        {
            key = "RF_low_delay";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 5;
            change_affected = new String[] { "menu25_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 5 };
        }
    },

    RF_HIGH_DELAY {
        {
            key = "RF_high_delay";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 6;
            change_affected = new String[] { "menu26_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 6 };
        }
    },

    COOKERHOOD_DELAY {
        {
            key = "cookerhood_delay";
            data_type = DataTypeNumber.class;
            change_command = 0xcb;
            change_data_size = 8;
            change_data_pos = 7;
            change_affected = new String[] { "menu20_mode" };
            read_command = 0xc9;
            read_reply_command = 0xca;
            read_reply_data_pos = new int[] { 7 };
        }
    },

    FAN_OUT_0 {
        {
            key = "fan_out_0";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 0;
            change_affected = new String[] { "outgoing_fan", "fan_out_efficiency", "fan_out_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    FAN_OUT_1 {
        {
            key = "fan_out_1";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 1;
            change_affected = new String[] { "outgoing_fan", "fan_out_efficiency", "fan_out_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    FAN_OUT_2 {
        {
            key = "fan_out_2";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 2;
            change_affected = new String[] { "outgoing_fan", "fan_out_efficiency", "fan_out_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    FAN_OUT_3 {
        {
            key = "fan_out_3";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 6;
            change_affected = new String[] { "outgoing_fan", "fan_out_efficiency", "fan_out_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 10 };
        }
    },

    FAN_IN_0 {
        {
            key = "fan_in_0";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 3;
            change_affected = new String[] { "incomming_fan", "fan_in_efficiency", "fan_in_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    FAN_IN_1 {
        {
            key = "fan_in_1";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 4;
            change_affected = new String[] { "incomming_fan", "fan_in_efficiency", "fan_in_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 4 };
        }
    },

    FAN_IN_2 {
        {
            key = "fan_in_2";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 5;
            change_affected = new String[] { "incomming_fan", "fan_in_efficiency", "fan_in_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 5 };
        }
    },

    FAN_IN_3 {
        {
            key = "fan_in_3";
            data_type = DataTypeNumber.class;
            change_command = 0xcf;
            change_data_size = 9;
            change_data_pos = 7;
            change_affected = new String[] { "incomming_fan", "fan_in_efficiency", "fan_in_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 11 };
        }
    },

    FAN_IN_EFFICIENCY {
        {
            key = "fan_in_efficiency";
            data_type = DataTypeNumber.class;
            read_command = 0x0b;
            read_reply_command = 0x0c;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    FAN_OUT_EFFICIENCY {
        {
            key = "fan_out_efficiency";
            data_type = DataTypeNumber.class;
            read_command = 0x0b;
            read_reply_command = 0x0c;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    FAN_IN_SPEED {
        {
            key = "fan_in_speed";
            data_type = DataTypeRPM.class;
            read_command = 0x0b;
            read_reply_command = 0x0c;
            read_reply_data_pos = new int[] { 2, 3 };
        }
    },

    FAN_OUT_SPEED {
        {
            key = "fan_out_speed";
            data_type = DataTypeRPM.class;
            read_command = 0x0b;
            read_reply_command = 0x0c;
            read_reply_data_pos = new int[] { 4, 5 };
        }
    },

    FAN_LEVEL {
        {
            key = "fan_level";
            data_type = DataTypeNumber.class;
            possible_values = new int[] { 0x01, 0x02, 0x03, 0x04 };
            change_command = 0x99;
            change_data_size = 1;
            change_data_pos = 0;
            change_affected = new String[] { "auto_mode", "incomming_fan", "outgoing_fan", "fan_in_efficiency",
                    "fan_out_efficiency", "fan_in_speed", "fan_out_speed" };
            read_command = 0xcd;
            read_reply_command = 0xce;
            read_reply_data_pos = new int[] { 8 };
        }
    },

    INCOMMING_FAN {
        {
            key = "incomming_fan";
            data_type = DataTypeNumber.class;
            read_command = 0x0b;
            read_reply_command = 0x0c;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    OUTGOING_FAN {
        {
            key = "outgoing_fan";
            data_type = DataTypeNumber.class;
            read_command = 0x0b;
            read_reply_command = 0x0c;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    HEATER_TEMPERATUR {
        {
            key = "heater_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 7 };
        }
    },

    TARGET_TEMPERATUR {
        {
            key = "target_temperatur";
            data_type = DataTypeTemperature.class;
            change_command = 0xd3;
            change_data_size = 1;
            change_data_pos = 0;
            change_affected = new String[] { "bypass_mode" };
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    COOKERHOOD_TEMPERATUR {
        {
            key = "cookerhood_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 8 };
        }
    },

    OUTDOOR_INCOMMING_TEMPERATUR {
        {
            key = "outdoor_incomming_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    OUTDOOR_OUTGOING_TEMPERATUR {
        {
            key = "outdoor_outgoing_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 4 };
        }
    },

    INDOOR_INCOMMING_TEMPERATUR {
        {
            key = "indoor_incomming_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    INDOOR_OUTGOING_TEMPERATUR {
        {
            key = "indoor_outgoing_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    EWT_TEMPERATUR {
        {
            key = "ewt_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 6 };
        }
    },

    IS_T1_SENSOR {
        {
            key = "is_T1_sensor";
            data_type = DataTypeBoolean.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x01;
        }
    },

    IS_T2_SENSOR {
        {
            key = "is_T2_sensor";
            data_type = DataTypeBoolean.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x02;
        }
    },

    IS_T3_SENSOR {
        {
            key = "is_T3_sensor";
            data_type = DataTypeBoolean.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x04;
        }
    },

    IS_T4_SENSOR {
        {
            key = "is_T4_sensor";
            data_type = DataTypeBoolean.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x08;
        }
    },

    IS_EWT_SENSOR {
        {
            key = "is_EWT_sensor";
            data_type = DataTypeBoolean.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x10;
        }
    },

    IS_HEATER_SENSOR {
        {
            key = "is_heater_sensor";
            data_type = DataTypeBoolean.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x20;
        }
    },

    IS_COOKERHOOD_SENSOR {
        {
            key = "is_cookerhood_sensor";
            data_type = DataTypeBoolean.class;
            read_command = 0xd1;
            read_reply_command = 0xd2;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x40;
        }
    },

    IS_CHIMNEY {
        {
            key = "is_chimney";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 4;
            change_affected = new String[] { "out_fan_only", "in_fan_only" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 4 };
            read_reply_data_bits = 0x01;
        }
    },

    IS_PREHEATER {
        {
            key = "is_preheater";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x40 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 4;
            change_affected = new String[] { "outdoor_incomming_temperatur", "indoor_incomming_temperatur",
                    "preheater_frost_protect", "preheater_frost_time", "preheater_heating", "preheater_mode",
                    "preheater_option", "preheater_time", "preheater_valve" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 4 };
            read_reply_data_bits = 0x40;
        }
    },

    IS_COOKERHOOD {
        {
            key = "is_cookerhood";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x02 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 4;
            change_affected = new String[] { "cookerhood_delay", "cookerhood_mode", "cookerhood_speed",
                    "cookerhood_temperatur" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 4 };
            read_reply_data_bits = 0x02;
        }
    },

    IS_BYPASS {
        {
            key = "is_bypass";
            data_type = DataTypeNumber.class;
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 1;
            change_affected = new String[] { "indoor_incomming_temperatur", "outdoor_outgoing_temperatur" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    IS_HEATER {
        {
            key = "is_heater";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x04 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 4;
            change_affected = new String[] { "heater_target_temperatur", "heater_efficiency", "heater_mode",
                    "heater_power", "heater_temperatur" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 4 };
            read_reply_data_bits = 0x04;
        }
    },

    RECU_LEVEL {
        {
            key = "recu_level";
            data_type = DataTypeNumber.class;
            possible_values = new int[] { 0x00, 0x01, 0x02 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 3;
            change_affected = new String[] { "incomming_fan", "outgoing_fan", "fan_out_0", "fan_out_1", "fan_out_2",
                    "fan_out_3", "fan_in_0", "fan_in_1", "fan_in_2", "fan_in_3" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    RECU_TYPE {
        {
            key = "recu_type";
            data_type = DataTypeNumber.class;
            possible_values = new int[] { 0x01, 0x02 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 2;
            change_affected = new String[] { "incomming_fan", "outgoing_fan", "outdoor_incomming_temperatur",
                    "indoor_incomming_temperatur", "indoor_outgoing_temperatur", "outdoor_outgoing_temperatur" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    IS_ENTHALPY {
        {
            key = "is_enthalpy";
            data_type = DataTypeNumber.class;
            possible_values = new int[] { 0x00, 0x01, 0x02 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 6;
            change_affected = new String[] { "enthalpy_temperatur", "enthalpy_humidity", "enthalpy_level",
                    "enthalpy_mode", "enthalpy_time" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 9 };
        }
    },

    IS_EWT {
        {
            key = "is_ewt";
            data_type = DataTypeNumber.class;
            possible_values = new int[] { 0x00, 0x01, 0x02 };
            change_command = 0xd7;
            change_data_size = 8;
            change_data_pos = 7;
            change_affected = new String[] { "ewt_speed", "ewt_temperatur_low", "ewt_mode", "ewt_temperatur_high",
                    "ewt_temperatur" };
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 10 };
        }
    },

    EWT_SPEED {
        {
            key = "ewt_speed";
            data_type = DataTypeNumber.class;
            change_command = 0xed;
            change_data_size = 5;
            change_data_pos = 2;
            change_affected = new String[] { "ewt_mode", "ewt_temperatur" };
            read_command = 0xeb;
            read_reply_command = 0xec;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    EWT_TEMPERATUR_LOW {
        {
            key = "ewt_temperatur_low";
            data_type = DataTypeNumber.class;
            change_command = 0xed;
            change_data_size = 5;
            change_data_pos = 0;
            change_affected = new String[] { "ewt_mode" };
            read_command = 0xeb;
            read_reply_command = 0xec;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    EWT_TEMPERATUR_HIGH {
        {
            key = "ewt_temperatur_high";
            data_type = DataTypeNumber.class;
            change_command = 0xed;
            change_data_size = 5;
            change_data_pos = 1;
            change_affected = new String[] { "ewt_mode" };
            read_command = 0xeb;
            read_reply_command = 0xec;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    COOKERHOOD_SPEED {
        {
            key = "cookerhood_speed";
            data_type = DataTypeNumber.class;
            change_command = 0xed;
            change_data_size = 5;
            change_data_pos = 3;
            change_affected = new String[] { "cookerhood_mode", "cookerhood_temperatur" };
            read_command = 0xeb;
            read_reply_command = 0xec;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    HEATER_EFFICIENCY {
        {
            key = "heater_efficiency";
            data_type = DataTypeNumber.class;
            read_command = 0xeb;
            read_reply_command = 0xec;
            read_reply_data_pos = new int[] { 4 };
        }
    },

    HEATER_POWER {
        {
            key = "heater_power";
            data_type = DataTypeNumber.class;
            read_command = 0xeb;
            read_reply_command = 0xec;
            read_reply_data_pos = new int[] { 5 };
        }
    },

    HEATER_TARGET_TEMPERATUR {
        {
            key = "heater_target_temperatur";
            data_type = DataTypeNumber.class;
            change_command = 0xed;
            change_data_size = 5;
            change_data_pos = 4;
            change_affected = new String[] { "heater_mode", "heater_efficiency", "heater_temperatur" };
            read_command = 0xeb;
            read_reply_command = 0xec;
            read_reply_data_pos = new int[] { 6 };
        }
    },

    SOFTWARE_MAIN_VERSION {
        {
            key = "software_main_version";
            data_type = DataTypeNumber.class;
            read_command = 0x69;
            read_reply_command = 0x6a;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    SOFTWARE_MINOR_VERSION {
        {
            key = "software_minor_version";
            data_type = DataTypeNumber.class;
            read_command = 0x69;
            read_reply_command = 0x6a;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    SOFTWARE_BETA_VERSION {
        {
            key = "software_beta_version";
            data_type = DataTypeNumber.class;
            read_command = 0x69;
            read_reply_command = 0x6a;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    ERROR_MESSAGE {
        {
            key = "error_message";
            data_type = DataTypeMessage.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 0, 1, 9, 13 };
        }
    },

    ERRORA_CURRENT {
        {
            key = "errorA_current";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    ERRORA_LAST {
        {
            key = "errorA_last";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    ERRORA_PRELAST {
        {
            key = "errorA_prelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 4 };
        }
    },

    ERRORA_PREPRELAST {
        {
            key = "errorA_preprelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 6 };
        }
    },

    ERRORAHIGH_CURRENT {
        {
            key = "errorAhigh_current";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 13 };
        }
    },

    ERRORAHIGH_LAST {
        {
            key = "errorAhigh_last";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 14 };
        }
    },

    ERRORAHIGH_PRELAST {
        {
            key = "errorAhigh_prelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 15 };
        }
    },

    ERRORAHIGH_PREPRELAST {
        {
            key = "errorAhigh_preprelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 16 };
        }
    },

    ERRORE_CURRENT {
        {
            key = "errorE_current";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    ERRORE_LAST {
        {
            key = "errorE_last";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    ERRORE_PRELAST {
        {
            key = "errorE_prelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 5 };
        }
    },

    ERRORE_PREPRELAST {
        {
            key = "errorE_preprelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 7 };
        }
    },

    ERROREA_CURRENT {
        {
            key = "errorEA_current";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 9 };
        }
    },

    ERROREA_LAST {
        {
            key = "errorEA_last";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 10 };
        }
    },

    ERROREA_PRELAST {
        {
            key = "errorEA_prelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 11 };
        }
    },

    ERROREA_PREPRELAST {
        {
            key = "errorEA_preprelast";
            data_type = DataTypeNumber.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 12 };
        }
    },

    ERROR_RESET {
        {
            key = "error_reset";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0xdb;
            change_data_size = 4;
            change_data_pos = 0;
            change_affected = new String[] { "error_message" };
        }
    },

    FILTER_RUNNING {
        {
            key = "filter_running";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 15, 16 };
        }
    },

    FILTER_RESET {
        {
            key = "filter_reset";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0xdb;
            change_data_size = 4;
            change_data_pos = 3;
            change_affected = new String[] { "filter_running", "filter_error", "filter_error_intern",
                    "filter_error_extern" };
        }
    },

    FILTER_ERROR {
        {
            key = "filter_error";
            data_type = DataTypeBoolean.class;
            read_command = 0xd9;
            read_reply_command = 0xda;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x01;
        }
    },

    FILTER_ERROR_INTERN {
        {
            key = "filter_error_intern";
            data_type = DataTypeBoolean.class;
            read_command = 0x37;
            read_reply_command = 0x3c;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x40;
        }
    },

    FILTER_ERROR_EXTERN {
        {
            key = "filter_error_extern";
            data_type = DataTypeBoolean.class;
            read_command = 0x37;
            read_reply_command = 0x3c;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x80;
        }
    },

    RECU_RESET {
        {
            key = "recu_reset";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0xdb;
            change_data_size = 4;
            change_data_pos = 1;
        }
    },

    RECU_AUTOTEST {
        {
            key = "recu_autotest";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0xdb;
            change_data_size = 4;
            change_data_pos = 2;
        }
    },

    CHIMNEY_MODE {
        {
            key = "chimney_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x01;
        }
    },

    BYPASS_MODE {
        {
            key = "bypass_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x02;
        }
    },

    EWT_MODE {
        {
            key = "ewt_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x04;
        }
    },

    HEATER_MODE {
        {
            key = "heater_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x08;
        }
    },

    CONTROL_MODE {
        {
            key = "control_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x10;
        }
    },

    PREHEATER_MODE {
        {
            key = "preheater_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x20;
        }
    },

    COOKERHOOD_MODE {
        {
            key = "cookerhood_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 8 };
            read_reply_data_bits = 0x40;
        }
    },

    ENTHALPY_MODE {
        {
            key = "enthalpy_mode";
            data_type = DataTypeNumber.class;
            read_command = 0xd5;
            read_reply_command = 0xd6;
            read_reply_data_pos = new int[] { 9 };
        }
    },

    ENTHALPY_TEMPERATUR {
        {
            key = "enthalpy_temperatur";
            data_type = DataTypeTemperature.class;
            read_command = 0x97;
            read_reply_command = 0x98;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    ENTHALPY_HUMIDITY {
        {
            key = "enthalpy_humidity";
            data_type = DataTypeNumber.class;
            read_command = 0x97;
            read_reply_command = 0x98;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    ENTHALPY_LEVEL {
        {
            key = "enthalpy_level";
            data_type = DataTypeNumber.class;
            read_command = 0x97;
            read_reply_command = 0x98;
            read_reply_data_pos = new int[] { 4 };
        }
    },

    ENTHALPY_TIME {
        {
            key = "enthalpy_time";
            data_type = DataTypeNumber.class;
            read_command = 0x97;
            read_reply_command = 0x98;
            read_reply_data_pos = new int[] { 5 };
        }
    },

    PREHEATER_VALVE {
        {
            key = "preheater_valve";
            data_type = DataTypeNumber.class;
            read_command = 0xe1;
            read_reply_command = 0xe2;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    PREHEATER_FROST_PROTECT {
        {
            key = "preheater_frost_protect";
            data_type = DataTypeNumber.class;
            read_command = 0xe1;
            read_reply_command = 0xe2;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    PREHEATER_HEATING {
        {
            key = "preheater_heating";
            data_type = DataTypeNumber.class;
            read_command = 0xe1;
            read_reply_command = 0xe2;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    PREHEATER_FROST_TIME {
        {
            key = "preheater_frost_time";
            data_type = DataTypeNumber.class;
            read_command = 0xe1;
            read_reply_command = 0xe2;
            read_reply_data_pos = new int[] { 3, 4 };
        }
    },

    PREHEATER_OPTION {
        {
            key = "preheater_option";
            data_type = DataTypeNumber.class;
            read_command = 0xe1;
            read_reply_command = 0xe2;
            read_reply_data_pos = new int[] { 5 };
        }
    },

    FREEZE_MODE {
        {
            key = "freeze_mode";
            data_type = DataTypeBoolean.class;
            read_command = 0x35;
            read_reply_command = 0x3c;
            read_reply_data_pos = new int[] { 5 };
            read_reply_data_bits = 0x80;
        }
    },

    LEVEL0_TIME {
        {
            key = "level0_time";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 0, 1, 2 };
        }
    },

    LEVEL1_TIME {
        {
            key = "level1_time";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 3, 4, 5 };
        }
    },

    LEVEL2_TIME {
        {
            key = "level2_time";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 6, 7, 8 };
        }
    },

    LEVEL3_TIME {
        {
            key = "level3_time";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 17, 18, 19 };
        }
    },

    FREEZE_TIME {
        {
            key = "freeze_time";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 9, 10 };
        }
    },

    PREHEATER_TIME {
        {
            key = "preheater_time";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 11, 12 };
        }
    },

    BYPASS_TIME {
        {
            key = "bypass_time";
            data_type = DataTypeNumber.class;
            read_command = 0xdd;
            read_reply_command = 0xde;
            read_reply_data_pos = new int[] { 13, 14 };
        }
    },

    BYPASS_SEASON {
        {
            key = "bypass_season";
            data_type = DataTypeNumber.class;
            read_command = 0xdf;
            read_reply_command = 0xe0;
            read_reply_data_pos = new int[] { 6 };
        }
    },

    IS_ANALOG1 {
        {
            key = "is_analog1";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 0;
            change_affected = new String[] { "analog1_mode", "analog1_negative", "analog1_min", "analog1_max",
                    "analog1_value", "analog1_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x01;
        }
    },

    IS_ANALOG2 {
        {
            key = "is_analog2";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x02 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 0;
            change_affected = new String[] { "analog2_mode", "analog2_negative", "analog2_min", "analog2_max",
                    "analog2_value", "analog2_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x02;
        }
    },

    IS_ANALOG3 {
        {
            key = "is_analog3";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x04 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 0;
            change_affected = new String[] { "analog3_mode", "analog3_negative", "analog3_min", "analog3_max",
                    "analog3_value", "analog3_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x04;
        }
    },

    IS_ANALOG4 {
        {
            key = "is_analog4";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x08 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 0;
            change_affected = new String[] { "analog4_mode", "analog4_negative", "analog4_min", "analog4_max",
                    "analog4_value", "analog4_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x08;
        }
    },

    IS_RF {
        {
            key = "is_RF";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x10 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 0;
            change_affected = new String[] { "RF_mode", "RF_negative", "RF_min", "RF_max", "RF_value" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x10;
        }
    },

    ANALOG1_MODE {
        {
            key = "analog1_mode";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 1;
            change_affected = new String[] { "analog1_negative", "analog1_min", "analog1_max", "analog1_value",
                    "analog1_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x01;
        }
    },

    ANALOG2_MODE {
        {
            key = "analog2_mode";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x02 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 1;
            change_affected = new String[] { "analog2_negative", "analog2_min", "analog2_max", "analog2_value",
                    "analog2_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x02;
        }
    },

    ANALOG3_MODE {
        {
            key = "analog3_mode";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x04 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 1;
            change_affected = new String[] { "analog3_negative", "analog3_min", "analog3_max", "analog3_value",
                    "analog3_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x04;
        }
    },

    ANALOG4_MODE {
        {
            key = "analog4_mode";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x08 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 1;
            change_affected = new String[] { "analog4_negative", "analog4_min", "analog4_max", "analog4_value",
                    "analog4_volt" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x08;
        }
    },

    RF_MODE {
        {
            key = "RF_mode";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x10 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 1;
            change_affected = new String[] { "RF_negative", "RF_min", "RF_max", "RF_value" };
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x10;
        }
    },

    ANALOG1_NEGATIVE {
        {
            key = "analog1_negative";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x01 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 2;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 2 };
            read_reply_data_bits = 0x01;
        }
    },

    ANALOG2_NEGATIVE {
        {
            key = "analog2_negative";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x02 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 2;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 2 };
            read_reply_data_bits = 0x02;
        }
    },

    ANALOG3_NEGATIVE {
        {
            key = "analog3_negative";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x04 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 2;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 2 };
            read_reply_data_bits = 0x04;
        }
    },

    ANALOG4_NEGATIVE {
        {
            key = "analog4_negative";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x08 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 2;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 2 };
            read_reply_data_bits = 0x08;
        }
    },

    RF_NEGATIVE {
        {
            key = "RF_negative";
            data_type = DataTypeBoolean.class;
            possible_values = new int[] { 0x10 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 2;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 2 };
            read_reply_data_bits = 0x10;
        }
    },

    ANALOG1_MIN {
        {
            key = "analog1_min";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 3;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    ANALOG1_MAX {
        {
            key = "analog1_max";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 4;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 4 };
        }
    },

    ANALOG1_VALUE {
        {
            key = "analog1_value";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 5;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 5 };
        }
    },

    ANALOG2_MIN {
        {
            key = "analog2_min";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 6;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 6 };
        }
    },

    ANALOG2_MAX {
        {
            key = "analog2_max";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 7;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 7 };
        }
    },

    ANALOG2_VALUE {
        {
            key = "analog2_value";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 8;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 8 };
        }
    },

    ANALOG3_MIN {
        {
            key = "analog3_min";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 9;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 9 };
        }
    },

    ANALOG3_MAX {
        {
            key = "analog3_max";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 10;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 10 };
        }
    },

    ANALOG3_VALUE {
        {
            key = "analog3_value";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 11;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 11 };
        }
    },

    ANALOG4_MIN {
        {
            key = "analog4_min";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 12;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 12 };
        }
    },

    ANALOG4_MAX {
        {
            key = "analog4_max";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 13;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 13 };
        }
    },

    ANALOG4_VALUE {
        {
            key = "analog4_value";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 14;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 14 };
        }
    },

    RF_MIN {
        {
            key = "RF_min";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 15;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 15 };
        }
    },

    RF_MAX {
        {
            key = "RF_max";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 16;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 16 };
        }
    },

    RF_VALUE {
        {
            key = "RF_value";
            data_type = DataTypeNumber.class;
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 17;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 17 };
        }
    },

    ANALOG_MODE {
        {
            key = "analog_mode";
            data_type = DataTypeNumber.class;
            possible_values = new int[] { 0x00, 0x01 };
            change_command = 0x9f;
            change_data_size = 19;
            change_data_pos = 18;
            change_affected = new String[] {};
            read_command = 0x9d;
            read_reply_command = 0x9e;
            read_reply_data_pos = new int[] { 18 };
        }
    },

    ANALOG1_VOLT {
        {
            key = "analog1_volt";
            data_type = DataTypeVolt.class;
            read_command = 0x13;
            read_reply_command = 0x14;
            read_reply_data_pos = new int[] { 0 };
        }
    },

    ANALOG2_VOLT {
        {
            key = "analog2_volt";
            data_type = DataTypeVolt.class;
            read_command = 0x13;
            read_reply_command = 0x14;
            read_reply_data_pos = new int[] { 1 };
        }
    },

    ANALOG3_VOLT {
        {
            key = "analog3_volt";
            data_type = DataTypeVolt.class;
            read_command = 0x13;
            read_reply_command = 0x14;
            read_reply_data_pos = new int[] { 2 };
        }
    },

    ANALOG4_VOLT {
        {
            key = "analog4_volt";
            data_type = DataTypeVolt.class;
            read_command = 0x13;
            read_reply_command = 0x14;
            read_reply_data_pos = new int[] { 3 };
        }
    },

    IS_FAN_IN {
        {
            key = "is_fan_in";
            data_type = DataTypeBoolean.class;
            read_command = 0x37;
            read_reply_command = 0x3c;
            read_reply_data_pos = new int[] { 9 };
            read_reply_data_bits = 0x40;
        }
    },

    IS_FAN_OUT {
        {
            key = "is_fan_out";
            data_type = DataTypeBoolean.class;
            read_command = 0x37;
            read_reply_command = 0x3c;
            read_reply_data_pos = new int[] { 9 };
            read_reply_data_bits = 0x80;
        }
    },

    IS_L1_SWITCH {
        {
            key = "is_L1_switch";
            data_type = DataTypeBoolean.class;
            read_command = 0x03;
            read_reply_command = 0x04;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x01;
        }
    },

    IS_L2_SWITCH {
        {
            key = "is_L2_switch";
            data_type = DataTypeBoolean.class;
            read_command = 0x03;
            read_reply_command = 0x04;
            read_reply_data_pos = new int[] { 0 };
            read_reply_data_bits = 0x02;
        }
    },

    IS_BATHROOM_SWITCH {
        {
            key = "is_bathroom_switch";
            data_type = DataTypeBoolean.class;
            read_command = 0x03;
            read_reply_command = 0x04;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x01;
        }
    },

    IS_COOKERHOOD_SWITCH {
        {
            key = "is_cookerhood_switch";
            data_type = DataTypeBoolean.class;
            read_command = 0x03;
            read_reply_command = 0x04;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x02;
        }
    },

    IS_EXTERNAL_FILTER {
        {
            key = "is_external_filter";
            data_type = DataTypeBoolean.class;
            read_command = 0x03;
            read_reply_command = 0x04;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x04;
        }
    },

    IS_WTW {
        {
            key = "is_WTW";
            data_type = DataTypeBoolean.class;
            read_command = 0x03;
            read_reply_command = 0x04;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x08;
        }
    },

    IS_BATHROOM2_SWITCH {
        {
            key = "is_bathroom2_switch";
            data_type = DataTypeBoolean.class;
            read_command = 0x03;
            read_reply_command = 0x04;
            read_reply_data_pos = new int[] { 1 };
            read_reply_data_bits = 0x10;
        }
    };

    Logger logger = LoggerFactory.getLogger(ComfoAirCommandType.class);
    String key;
    Class<? extends ComfoAirDataType> data_type;

    /*
     * Possible values
     */
    int[] possible_values;

    /*
     * Cmd code to change properties on the comfoair.
     */
    int change_command;
    /*
     * The size of the data block.
     */
    int change_data_size;
    /*
     * The byte inside the data block which holds the crucial value.
     */
    int change_data_pos;
    /*
     * Affected commands which should be refreshed after a successful change
     * command call.
     */
    String[] change_affected;

    /*
     * Command for reading properties.
     */
    int read_command;

    /*
     * ACK Command which identifies the matching response.
     */
    int read_reply_command;

    /*
     * The byte position inside the response data.
     */
    int[] read_reply_data_pos;

    /*
     * Bit mask for boolean response properties to identify a true value.
     */
    int read_reply_data_bits;

    /**
     * @return command key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return data type for this command key
     */
    public ComfoAirDataType getDataType() {
        try {
            return data_type.newInstance();
        } catch (Exception e) {
            logger.debug("Creating new DataType went wrong ", e);
        }
        return null;
    }

    /**
     * @return possible byte values
     */
    public int[] getPossibleValues() {
        return possible_values;
    }

    /**
     * @return relevant byte position inside the response byte value array
     */
    public int getChangeDataPos() {
        return change_data_pos;
    }

    /**
     * @return generate a byte value sequence for the response stream
     */
    public int[] getChangeDataTemplate() {
        int[] template = new int[change_data_size];
        for (int i = 0; i < template.length; i++) {
            template[i] = 0x00;
        }
        return template;
    }

    /**
     * @return byte position inside the request byte value array
     */
    public int[] getGetReplyDataPos() {
        return read_reply_data_pos;
    }

    /**
     * @return bit mask for the response byte value
     */
    public int getGetReplyDataBits() {
        return read_reply_data_bits;
    }

    /**
     * Get a command to change properties on the comfoair.
     *
     * @param key
     *            command key
     * @param value
     *            new state
     * @return initialized ComfoAirCommand
     */
    public static ComfoAirCommand getChangeCommand(String key, State value) {
        ComfoAirCommandType commandType = ComfoAirCommandType.getCommandTypeByKey(key);

        if (commandType != null) {
            ComfoAirDataType dataType = commandType.getDataType();
            int[] data = dataType.convertFromState(value, commandType);
            int dataPossition = commandType.getChangeDataPos();
            int intValue = ((DecimalType) value).intValue();

            return new ComfoAirCommand(key, commandType.change_command, null, data, dataPossition, intValue);
        }

        return null;
    }

    /**
     * Get all commands which should be refreshed after a successful change
     * command.
     *
     * @param key
     *            command key
     * @param usedKeys
     * @return ComfoAirCommand's which should be updated after a modifying
     *         ComfoAirCommand named by key
     */
    public static Collection<ComfoAirCommand> getAffectedReadCommands(String key, Set<String> usedKeys) {

        Map<Integer, ComfoAirCommand> commands = new HashMap<Integer, ComfoAirCommand>();

        ComfoAirCommandType commandType = ComfoAirCommandType.getCommandTypeByKey(key);
        if (commandType.read_reply_command != 0) {
            Integer getCmd = commandType.read_command == 0 ? null : new Integer(commandType.read_command);
            Integer replyCmd = new Integer(commandType.read_reply_command);

            ComfoAirCommand command = new ComfoAirCommand(key, getCmd, replyCmd, null, null, null);
            commands.put(command.getReplyCmd(), command);
        }

        for (String affectedKey : commandType.change_affected) {
            // refresh affected event keys only when they are used
            if (!usedKeys.contains(affectedKey)) {
                continue;
            }

            ComfoAirCommandType affectedCommandType = ComfoAirCommandType.getCommandTypeByKey(affectedKey);

            Integer getCmd = affectedCommandType.read_command == 0 ? null
                    : new Integer(affectedCommandType.read_command);
            Integer replyCmd = new Integer(affectedCommandType.read_reply_command);

            ComfoAirCommand command = commands.get(replyCmd);

            if (command == null) {
                command = new ComfoAirCommand(affectedKey, getCmd, replyCmd, null, null, null);
                commands.put(command.getReplyCmd(), command);
            } else {
                command.addKey(affectedKey);
            }
        }

        return commands.values();
    }

    /**
     * Get all commands which receive informations to update items.
     *
     * @return all ComfoAirCommand's identified by keys
     */
    public static Collection<ComfoAirCommand> getReadCommandsByEventTypes(List<String> keys) {

        Map<Integer, ComfoAirCommand> commands = new HashMap<Integer, ComfoAirCommand>();
        for (ComfoAirCommandType entry : values()) {
            if (!keys.contains(entry.key)) {
                continue;
            }
            if (entry.read_reply_command == 0) {
                continue;
            }

            Integer getCmd = entry.read_command == 0 ? null : new Integer(entry.read_command);
            Integer replyCmd = new Integer(entry.read_reply_command);

            ComfoAirCommand command = commands.get(replyCmd);

            if (command == null) {
                command = new ComfoAirCommand(entry.key, getCmd, replyCmd, null, null, null);
                commands.put(command.getReplyCmd(), command);
            } else {
                command.addKey(entry.key);
            }
        }

        return commands.values();
    }

    /**
     * Get commandtypes which matches the replyCmd.
     *
     * @param replyCmd
     *            reply command byte value
     * @return ComfoAirCommandType identified by replyCmd
     */
    public static List<ComfoAirCommandType> getCommandTypesByReplyCmd(int replyCmd) {
        List<ComfoAirCommandType> commands = new ArrayList<ComfoAirCommandType>();
        for (ComfoAirCommandType entry : values()) {
            if (entry.read_reply_command != replyCmd) {
                continue;
            }
            commands.add(entry);
        }
        return commands;
    }

    /**
     * Get a specific command.
     *
     * @param key
     *            command key
     * @return ComfoAirCommandType identified by key
     */
    public static ComfoAirCommandType getCommandTypeByKey(String key) {
        for (ComfoAirCommandType entry : values()) {
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

}