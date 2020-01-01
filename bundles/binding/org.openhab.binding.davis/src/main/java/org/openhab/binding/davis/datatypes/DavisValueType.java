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
package org.openhab.binding.davis.datatypes;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.davis.internal.DavisCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all valid datafields which could be processed by this binding
 *
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public enum DavisValueType {

    RX_COUNT_TOTAL {
        {
            key = "rx_count_total";
            data_type = DataTypeSplitString.class;
            command = DavisCommandType.RXCHECK;
            data_offset = 0;
            data_size = 0;
        }
    },

    RX_COUNT_MISSED {
        {
            key = "rx_count_missed";
            data_type = DataTypeSplitString.class;
            command = DavisCommandType.RXCHECK;
            data_offset = 1;
            data_size = 0;
        }
    },

    RX_COUNT_RESYNC {
        {
            key = "rx_count_resync";
            data_type = DataTypeSplitString.class;
            command = DavisCommandType.RXCHECK;
            data_offset = 2;
            data_size = 0;
        }
    },

    RX_COUNT_CONSECUTIVE {
        {
            key = "rx_count_consecutive";
            data_type = DataTypeSplitString.class;
            command = DavisCommandType.RXCHECK;
            data_offset = 3;
            data_size = 0;
        }
    },

    RX_COUNT_CRC {
        {
            key = "rx_count_crc";
            data_type = DataTypeSplitString.class;
            command = DavisCommandType.RXCHECK;
            data_offset = 4;
            data_size = 0;
        }
    },

    FIRMWARE_VERSION {
        {
            key = "firmware_version";
            data_type = DataTypeString.class;
            command = DavisCommandType.NVER;
            data_offset = 0;
            data_size = 0;
        }
    },

    FIRMWARE_DATE {
        {
            key = "firmware_date";
            data_type = DataTypeString.class;
            command = DavisCommandType.VER;
            data_offset = 0;
            data_size = 0;
        }
    },

    TIME {
        {
            key = "time";
            data_type = DataTypeTime.class;
            command = DavisCommandType.GETTIME;
            data_offset = 0;
            data_size = 0;
        }
    },

    RECEIVERS {
        {
            key = "receivers";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.RECEIVERS;
            data_offset = 0;
            data_size = 1;
        }
    },

    STATION_TYPE {
        {
            key = "station_type";
            data_type = DataTypeStationType.class;
            command = DavisCommandType.WRD;
            data_offset = 0;
            data_size = 1;
        }
    },

    BAR_TREND {
        {
            key = "bar_trend";
            data_type = DataTypeBarometricTrend.class;
            command = DavisCommandType.LOOP;
            data_offset = 3;
            data_size = 1;
        }
    },

    BAROMETER {
        {
            key = "barometer";
            data_type = DataTypeBarometer.class;
            command = DavisCommandType.LOOP;
            data_offset = 7;
            data_size = 2;
        }
    },

    INSIDE_TEMP {
        {
            key = "inside_temp";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.LOOP;
            data_offset = 9;
            data_size = 2;
        }
    },

    INSIDE_HUMIDITY {
        {
            key = "inside_humidity";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 11;
            data_size = 1;
        }
    },

    OUTSIDE_TEMP {
        {
            key = "outside_temp";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.LOOP;
            data_offset = 12;
            data_size = 2;
        }
    },

    WIND_SPEED {
        {
            key = "wind_speed";
            data_type = DataTypeWind.class;
            command = DavisCommandType.LOOP;
            data_offset = 14;
            data_size = 1;
        }
    },

    WIND_SPEED_10MIN_AVG {
        {
            key = "wind_speed_10min_avg";
            data_type = DataTypeWind.class;
            command = DavisCommandType.LOOP;
            data_offset = 15;
            data_size = 1;
        }
    },

    WIND_DIRECTION {
        {
            key = "wind_direction";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 16;
            data_size = 2;
        }
    },

    EXTRA_TEMPERATURE_1 {
        {
            key = "extra_temperature_1";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 18;
            data_size = 1;
        }
    },

    EXTRA_TEMPERATURE_2 {
        {
            key = "extra_temperature_2";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 19;
            data_size = 1;
        }
    },

    EXTRA_TEMPERATURE_3 {
        {
            key = "extra_temperature_3";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 20;
            data_size = 1;
        }
    },

    EXTRA_TEMPERATURE_4 {
        {
            key = "extra_temperature_4";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 21;
            data_size = 1;
        }
    },

    EXTRA_TEMPERATURE_5 {
        {
            key = "extra_temperature_5";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 22;
            data_size = 1;
        }
    },

    EXTRA_TEMPERATURE_6 {
        {
            key = "extra_temperature_6";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 23;
            data_size = 1;
        }
    },

    EXTRA_TEMPERATURE_7 {
        {
            key = "extra_temperature_7";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 24;
            data_size = 1;
        }
    },

    SOIL_TEMPERATURE_1 {
        {
            key = "soil_temperature_1";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 25;
            data_size = 1;
        }
    },

    SOIL_TEMPERATURE_2 {
        {
            key = "soil_temperature_2";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 26;
            data_size = 1;
        }
    },

    SOIL_TEMPERATURE_3 {
        {
            key = "soil_temperature_3";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 27;
            data_size = 1;
        }
    },

    SOIL_TEMPERATURE_4 {
        {
            key = "soil_temperature_4";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 28;
            data_size = 1;
        }
    },

    LEAF_TEMPERATURE_1 {
        {
            key = "leaf_temperature_1";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 29;
            data_size = 1;
        }
    },

    LEAF_TEMPERATURE_2 {
        {
            key = "leaf_temperature_2";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 30;
            data_size = 1;
        }
    },

    LEAF_TEMPERATURE_3 {
        {
            key = "leaf_temperature_3";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 31;
            data_size = 1;
        }
    },

    LEAF_TEMPERATURE_4 {
        {
            key = "leaf_temperature_4";
            data_type = DataTypeTemperatureExtra.class;
            command = DavisCommandType.LOOP;
            data_offset = 32;
            data_size = 1;
        }
    },

    OUTSIDE_HUMIDITY {
        {
            key = "outside_humidity";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 33;
            data_size = 1;
        }
    },

    EXTRA_HUMIDITY_1 {
        {
            key = "extra_humidity_1";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 34;
            data_size = 1;
        }
    },

    EXTRA_HUMIDITY_2 {
        {
            key = "extra_humidity_2";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 35;
            data_size = 1;
        }
    },

    EXTRA_HUMIDITY_3 {
        {
            key = "extra_humidity_3";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 36;
            data_size = 1;
        }
    },

    EXTRA_HUMIDITY_4 {
        {
            key = "extra_humidity_4";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 37;
            data_size = 1;
        }
    },

    EXTRA_HUMIDITY_5 {
        {
            key = "extra_humidity_5";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 38;
            data_size = 1;
        }
    },

    EXTRA_HUMIDITY_6 {
        {
            key = "extra_humidity_6";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 39;
            data_size = 1;
        }
    },

    EXTRA_HUMIDITY_7 {
        {
            key = "extra_humidity_7";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 40;
            data_size = 1;
        }
    },

    RAIN_RATE {
        {
            key = "rain_rate";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP;
            data_offset = 41;
            data_size = 2;
        }
    },

    UV {
        {
            key = "uv";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 43;
            data_size = 1;
        }
    },

    SOLAR_RADIATION {
        {
            key = "solar_radiation";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 44;
            data_size = 2;
        }
    },

    STORM_RAIN {
        {
            key = "storm_rain";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP;
            data_offset = 46;
            data_size = 2;
        }
    },

    DAY_RAIN {
        {
            key = "day_rain";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP;
            data_offset = 50;
            data_size = 2;
        }
    },

    MONTH_RAIN {
        {
            key = "month_rain";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP;
            data_offset = 52;
            data_size = 2;
        }
    },

    YEAR_RAIN {
        {
            key = "year_rain";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP;
            data_offset = 54;
            data_size = 2;
        }
    },

    SOIL_MOISTURE_1 {
        {
            key = "soil_moisture_1";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 62;
            data_size = 1;
        }
    },

    SOIL_MOISTURE_2 {
        {
            key = "soil_moisture_2";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 63;
            data_size = 1;
        }
    },

    SOIL_MOISTURE_3 {
        {
            key = "soil_moisture_3";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 64;
            data_size = 1;
        }
    },

    SOIL_MOISTURE_4 {
        {
            key = "soil_moisture_4";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 65;
            data_size = 1;
        }
    },

    LEAF_WETNES_1 {
        {
            key = "leaf_wetnes_1";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 66;
            data_size = 1;
        }
    },

    LEAF_WETNES_2 {
        {
            key = "leaf_wetnes_2";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 67;
            data_size = 1;
        }
    },

    LEAF_WETNES_3 {
        {
            key = "leaf_wetnes_3";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 68;
            data_size = 1;
        }
    },

    LEAF_WETNES_4 {
        {
            key = "leaf_wetnes_4";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 69;
            data_size = 1;
        }
    },

    TRANSMITTER_BATTERY_STATUS {
        {
            key = "transmitter_battery_status";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP;
            data_offset = 86;
            data_size = 1;
        }
    },

    CONSOLE_BATTERY_VOLTAGE {
        {
            key = "console_battery_voltage";
            data_type = DataTypeVoltage.class;
            command = DavisCommandType.LOOP;
            data_offset = 87;
            data_size = 2;
        }
    },

    WIND_SPEED_10MIN_AVG_HIRES {
        {
            key = "wind_speed_10min_avg_hires";
            data_type = DataTypeWindHiRes.class;
            command = DavisCommandType.LOOP2;
            data_offset = 18;
            data_size = 2;
        }
    },

    WIND_SPEED_2MIN_AVG_HIRES {
        {
            key = "wind_speed_2min_avg_hires";
            data_type = DataTypeWindHiRes.class;
            command = DavisCommandType.LOOP2;
            data_offset = 20;
            data_size = 2;
        }
    },

    WIND_SPEED_10MIN_GUST_HIRES {
        {
            key = "wind_speed_10min_gust_hires";
            data_type = DataTypeWindHiRes.class;
            command = DavisCommandType.LOOP2;
            data_offset = 22;
            data_size = 2;
        }
    },

    WIND_DIRECTION_10MIN_GUST {
        {
            key = "wind_direction_10min_gust";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.LOOP2;
            data_offset = 24;
            data_size = 2;
        }
    },

    DEW_POINT {
        {
            key = "dew_point";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.LOOP2;
            data_offset = 30;
            data_size = 2;
        }
    },

    HEAT_INDEX {
        {
            key = "heat_index";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.LOOP2;
            data_offset = 35;
            data_size = 2;
        }
    },

    WIND_CHILL {
        {
            key = "wind_chill";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.LOOP2;
            data_offset = 37;
            data_size = 2;
        }
    },

    THSW_INDEX {
        {
            key = "thsw_index";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.LOOP2;
            data_offset = 39;
            data_size = 2;
        }
    },

    LAST_15MIN_RAIN {
        {
            key = "last_15min_rain";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP2;
            data_offset = 52;
            data_size = 2;
        }
    },

    LAST_HOUR_RAIN {
        {
            key = "last_hour_rain";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP2;
            data_offset = 54;
            data_size = 2;
        }
    },

    LAST_24H_RAIN {
        {
            key = "last_24h_rain";
            data_type = DataTypeRain.class;
            command = DavisCommandType.LOOP2;
            data_offset = 58;
            data_size = 2;
        }

    },

    BAROMETER_DAY_LO {
        {
            key = "barometer_day_lo";
            data_type = DataTypeBarometer.class;
            command = DavisCommandType.HILOWS;
            data_offset = 0;
            data_size = 2;
        }
    },

    BAROMETER_DAY_HI {
        {
            key = "barometer_day_hi";
            data_type = DataTypeBarometer.class;
            command = DavisCommandType.HILOWS;
            data_offset = 2;
            data_size = 2;
        }
    },

    BAROMETER_MONTH_LO {
        {
            key = "barometer_month_lo";
            data_type = DataTypeBarometer.class;
            command = DavisCommandType.HILOWS;
            data_offset = 4;
            data_size = 2;
        }
    },

    BAROMETER_MONTH_HI {
        {
            key = "barometer_month_hi";
            data_type = DataTypeBarometer.class;
            command = DavisCommandType.HILOWS;
            data_offset = 6;
            data_size = 2;
        }
    },

    BAROMETER_YEAR_LO {
        {
            key = "barometer_year_lo";
            data_type = DataTypeBarometer.class;
            command = DavisCommandType.HILOWS;
            data_offset = 8;
            data_size = 2;
        }
    },

    BAROMETER_YEAR_HI {
        {
            key = "barometer_year_hi";
            data_type = DataTypeBarometer.class;
            command = DavisCommandType.HILOWS;
            data_offset = 10;
            data_size = 2;
        }
    },

    BAROMETER_DAY_LO_TIMEOFDAY {
        {
            key = "barometer_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 12;
            data_size = 2;
        }
    },

    BAROMETER_DAY_HI_TIMEOFDAY {
        {
            key = "barometer_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 14;
            data_size = 2;
        }
    },

    WIND_SPEED_DAY_HI {
        {
            key = "wind_speed_day_hi";
            data_type = DataTypeWind.class;
            command = DavisCommandType.HILOWS;
            data_offset = 16;
            data_size = 1;
        }
    },

    WIND_SPEED_DAY_HI_TIMEOFDAY {
        {
            key = "wind_speed_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 17;
            data_size = 2;
        }
    },

    WIND_SPEED_MONTH_HI {
        {
            key = "wind_speed_month_hi";
            data_type = DataTypeWind.class;
            command = DavisCommandType.HILOWS;
            data_offset = 19;
            data_size = 1;
        }
    },

    WIND_SPEED_YEAR_HI {
        {
            key = "wind_speed_year_hi";
            data_type = DataTypeWind.class;
            command = DavisCommandType.HILOWS;
            data_offset = 20;
            data_size = 1;
        }
    },

    INSIDE_TEMP_DAY_HI {
        {
            key = "inside_temp_day_hi";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 21;
            data_size = 2;
        }
    },

    INSIDE_TEMP_DAY_LO {
        {
            key = "inside_temp_day_lo";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 23;
            data_size = 2;
        }
    },

    INSIDE_TEMP_DAY_HI_TIMEOFDAY {
        {
            key = "inside_temp_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 25;
            data_size = 2;
        }
    },

    INSIDE_TEMP_DAY_LO_TIMEOFDAY {
        {
            key = "inside_temp_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 27;
            data_size = 2;
        }
    },

    INSIDE_TEMP_MONTH_LO {
        {
            key = "inside_temp_month_lo";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 29;
            data_size = 2;
        }
    },

    INSIDE_TEMP_MONTH_HI {
        {
            key = "inside_temp_month_hi";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 31;
            data_size = 2;
        }
    },

    INSIDE_TEMP_YEAR_LO {
        {
            key = "inside_temp_year_lo";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 33;
            data_size = 2;
        }
    },

    INSIDE_TEMP_YEAR_HI {
        {
            key = "inside_temp_year_hi";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 35;
            data_size = 2;
        }
    },

    INSIDE_HUMIDITY_DAY_HI {
        {
            key = "inside_humidity_day_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 37;
            data_size = 1;
        }
    },

    INSIDE_HUMIDITY_DAY_LO {
        {
            key = "inside_humidity_day_lo";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 38;
            data_size = 1;
        }
    },

    INSIDE_HUMIDITY_DAY_HI_TIMEOFDAY {
        {
            key = "inside_humidity_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 39;
            data_size = 2;
        }
    },

    INSIDE_HUMIDITY_DAY_LO_TIMEOFDAY {
        {
            key = "inside_humidity_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 41;
            data_size = 2;
        }
    },

    INSIDE_HUMIDITY_MONTH_HI {
        {
            key = "inside_humidity_month_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 43;
            data_size = 1;
        }
    },

    INSIDE_HUMIDITY_MONTH_LO {
        {
            key = "inside_humidity_month_lo";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 44;
            data_size = 1;
        }
    },

    INSIDE_HUMIDITY_YEAR_HI {
        {
            key = "inside_humidity_year_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 45;
            data_size = 1;
        }
    },

    INSIDE_HUMIDITY_YEAR_LO {
        {
            key = "inside_humidity_daily_lo";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 46;
            data_size = 1;
        }
    },

    OUTSIDE_TEMP_DAY_LO {
        {
            key = "outside_temp_day_lo";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 47;
            data_size = 2;
        }
    },

    OUTSIDE_TEMP_DAY_HI {
        {
            key = "outside_temp_day_hi";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 49;
            data_size = 2;
        }
    },

    OUTSIDE_TEMP_DAY_LO_TIMEOFDAY {
        {
            key = "outside_temp_day_lo_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 51;
            data_size = 2;
        }
    },

    OUTSIDE_TEMP_DAY_HI_TIMEOFDAY {
        {
            key = "outside_temp_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 53;
            data_size = 2;
        }
    },

    OUTSIDE_TEMP_MONTH_HI {
        {
            key = "outside_temp_month_hi";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 55;
            data_size = 2;
        }
    },

    OUTSIDE_TEMP_MONTH_LO {
        {
            key = "outside_temp_month_lo";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 57;
            data_size = 2;
        }
    },

    OUTSIDE_TEMP_YEAR_HI {
        {
            key = "outside_temp_year_hi";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 59;
            data_size = 2;
        }
    },

    OUTSIDE_TEMP_YEAR_LO {
        {
            key = "outside_temp_year_lo";
            data_type = DataTypeTemperature.class;
            command = DavisCommandType.HILOWS;
            data_offset = 61;
            data_size = 2;
        }
    },

    DEW_POINT_DAY_LO {
        {
            key = "dew_point_day_lo";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 63;
            data_size = 2;
        }
    },

    DEW_POINT_DAY_HI {
        {
            key = "dew_point_day_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 65;
            data_size = 2;
        }
    },

    DEW_POINT_DAY_LO_TIMEOFDAY {
        {
            key = "dew_point_day_lo_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 67;
            data_size = 2;
        }
    },

    DEW_POINT_DAY_HI_TIMEOFDAY {
        {
            key = "dew_point_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 69;
            data_size = 2;
        }
    },

    DEW_POINT_MONTH_HI {
        {
            key = "dew_point_month_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 71;
            data_size = 2;
        }
    },

    DEW_POINT_MONTH_LO {
        {
            key = "dew_point_month_lo";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 73;
            data_size = 2;
        }
    },

    DEW_POINT_YEAR_HI {
        {
            key = "dew_point_year_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 75;
            data_size = 2;
        }
    },

    DEW_POINT_YEAR_LO {
        {
            key = "dew_point_year_lo";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 77;
            data_size = 2;
        }
    },

    WIND_CHILL_DAY_LO {
        {
            key = "wind_chill_day_lo";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 79;
            data_size = 2;
        }
    },

    WIND_CHILL_DAY_LO_TIMEOFDAY {
        {
            key = "wind_chill_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 81;
            data_size = 2;
        }
    },

    WIND_CHILL_MONTH_LO {
        {
            key = "wind_chill_month_lo";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 83;
            data_size = 2;
        }
    },

    WIND_CHILL_YEAR_LO {
        {
            key = "wind_chill_year_lo";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 85;
            data_size = 2;
        }
    },

    HEAT_INDEX_DAY_HI {
        {
            key = "heat_index_day_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 87;
            data_size = 2;
        }
    },

    HEAT_INDEX_DAY_HI_TIMEOFDAY {
        {
            key = "heat_index_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 89;
            data_size = 2;
        }
    },

    HEAT_INDEX_MONTH_HI {
        {
            key = "heat_index_month_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 91;
            data_size = 2;
        }
    },

    HEAT_INDEX_YEAR_HI {
        {
            key = "heat_index_year_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 93;
            data_size = 2;
        }
    },

    THSW_INDEX_DAY_HI {
        {
            key = "thsw_index_day_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 95;
            data_size = 2;
        }
    },

    THSW_INDEX_DAY_HI_TIMEOFDAY {
        {
            key = "thsw_index_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 97;
            data_size = 2;
        }
    },

    THSW_INDEX_MONTH_HI {
        {
            key = "thsw_index_month_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 99;
            data_size = 2;
        }
    },

    THSW_INDEX_YEAR_HI {
        {
            key = "thsw_index_year_hi";
            data_type = DataTypeTemperatureF.class;
            command = DavisCommandType.HILOWS;
            data_offset = 101;
            data_size = 2;
        }
    },

    SOLAR_RADIATION_DAY_HI {
        {
            key = "solar_radiation_day_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 103;
            data_size = 2;
        }
    },

    SOLAR_RADIATION_DAY_LO_TIMEOFDAY {
        {
            key = "solar_radiation_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 105;
            data_size = 2;
        }
    },

    SOLAR_RADIATION_MONTH_HI {
        {
            key = "solar_radiation_month_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 107;
            data_size = 2;
        }
    },

    SOLAR_RADIATION_YEAR_HI {
        {
            key = "solar_radiation_year_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 109;
            data_size = 2;
        }
    },

    UV_DAY_HI {
        {
            key = "uv_day_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 111;
            data_size = 1;
        }
    },

    UV_DAY_HI_TIMEOFDAY {
        {
            key = "uv_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 112;
            data_size = 2;
        }
    },

    UV_MONTH_HI {
        {
            key = "uv_month_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 114;
            data_size = 1;
        }
    },

    UV_YEAR_HI {
        {
            key = "uv_year_hi";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 115;
            data_size = 1;
        }
    },

    RAIN_RATE_DAY_HI {
        {
            key = "rain_rate_day_hi";
            data_type = DataTypeRain.class;
            command = DavisCommandType.HILOWS;
            data_offset = 116;
            data_size = 2;
        }
    },

    RAIN_RATE_DAY_HI_TIMEOFDAY {
        {
            key = "rain_rate_day_hi_timeofday";
            data_type = DataTypeNumber.class;
            command = DavisCommandType.HILOWS;
            data_offset = 118;
            data_size = 2;
        }
    },

    RAIN_RATE_HOUR_HI {
        {
            key = "rain_rate_hour_hi";
            data_type = DataTypeRain.class;
            command = DavisCommandType.HILOWS;
            data_offset = 120;
            data_size = 2;
        }
    },

    RAIN_RATE_MONTH_HI {
        {
            key = "rain_rate_month_hi";
            data_type = DataTypeRain.class;
            command = DavisCommandType.HILOWS;
            data_offset = 122;
            data_size = 2;
        }
    },

    RAIN_RATE_YEAR_HI {
        {
            key = "rain_rate_year_hi";
            data_type = DataTypeRain.class;
            command = DavisCommandType.HILOWS;
            data_offset = 124;
            data_size = 2;
        }
    };

    Logger logger = LoggerFactory.getLogger(DavisValueType.class);
    String key;
    Class<? extends DavisDataType> data_type;
    DavisCommandType command;
    int data_offset;
    int data_size;

    /**
     * @return command key
     */
    public String getKey() {
        return key;
    }

    public DavisCommandType getCommand() {
        return command;
    }

    /**
     * @return data type for this command key
     */
    public DavisDataType getDataType() {
        try {
            return data_type.newInstance();
        } catch (Exception e) {
            logger.error("Creating new DataType went wrong", e);
        }
        return null;
    }

    public int getDataOffset() {
        return data_offset;
    }

    public int getDataSize() {
        return data_size;
    }

    /**
     * Get all commands which receive informations to update items.
     *
     * @return all DavisCommand's identified by keys
     */
    public static Collection<DavisCommand> getReadCommandsByKeys(List<String> keys) {

        Map<String, DavisCommand> commands = new HashMap<String, DavisCommand>();
        for (DavisValueType entry : values()) {
            if (!keys.contains(entry.key)) {
                continue;
            }
            if (entry.command == null) {
                continue;
            }

            DavisCommandType getCmd = entry.command;

            DavisCommand command = commands.get(getCmd);

            if (command == null) {
                command = new DavisCommand(entry.key, getCmd);
                commands.put(command.getRequestCmd(), command);
            } else {
                command.addKey(entry.key);
            }
        }

        return commands.values();
    }

    /**
     * Get all commands which receive informations to update items.
     *
     * @return all DavisValueTypes receiving data from a specific command
     */
    public static Set<DavisValueType> getValueTypesByCommandType(DavisCommandType commandType) {

        EnumSet<DavisValueType> valueTypes = EnumSet.noneOf(DavisValueType.class);
        for (DavisValueType entry : values()) {
            if (commandType.getCommand().equals(entry.command.getCommand())) {
                valueTypes.add(entry);
            }
        }
        return valueTypes;
    }

    /**
     * Get a specific command.
     *
     * @param key
     *            command key
     * @return DavisValueType identified by key
     */
    public static DavisValueType getValueTypeByKey(String key) {
        for (DavisValueType entry : values()) {
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

}
