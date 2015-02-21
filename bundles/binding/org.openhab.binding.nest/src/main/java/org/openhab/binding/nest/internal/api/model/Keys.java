/**
 * Copyright 2014 Nest Labs Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software * distributed under
 * the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openhab.binding.nest.internal.api.model;

public class Keys {
    public static final String DEVICES = "devices";
    public static final String THERMOSTATS = "thermostats";
    public static final String STRUCTURES = "structures";
    public static final String SMOKE_CO_ALARMS = "smoke_co_alarms";
    public static final String DEVICE_ID = "device_id";
    public static final String LOCALE = "locale";
    public static final String SOFTWARE_VERSION = "software_version";
    public static final String STRUCTURE_ID = "structure_id";
    public static final String NAME = "name";
    public static final String NAME_LONG = "name_long";
    public static final String LAST_CONNECTION = "last_connection";
    public static final String IS_ONLINE = "is_online";

    public static class ACCESS_TOKEN {
    	public static final String TOKEN = "access_token";
        public static final String EXPIRES_IN = "expires_in";
    }

    public static class THERMOSTAT {
    	public static final String CAN_COOL = "can_cool";
        public static final String CAN_HEAT = "can_heat";
        public static final String IS_USING_EMERGENCY_HEAT = "is_using_emergency_heat";
        public static final String HAS_FAN = "has_fan";
        public static final String FAN_TIMER_ACTIVE = "fan_timer_active";
        public static final String FAN_TIMER_TIMEOUT = "fan_timer_timeout";
        public static final String HAS_LEAF = "has_leaf";
        public static final String TEMP_SCALE = "temperature_scale";
        public static final String TARGET_TEMP_F = "target_temperature_f";
        public static final String TARGET_TEMP_C = "target_temperature_c";
        public static final String TARGET_TEMP_HIGH_F = "target_temperature_high_f";
        public static final String TARGET_TEMP_HIGH_C = "target_temperature_high_c";
        public static final String TARGET_TEMP_LOW_F = "target_temperature_low_f";
        public static final String TARGET_TEMP_LOW_C = "target_temperature_low_c";
        public static final String AWAY_TEMP_HIGH_F = "away_temperature_high_f";
        public static final String AWAY_TEMP_HIGH_C = "away_temperature_high_c";
        public static final String AWAY_TEMP_LOW_F = "away_temperature_low_f";
        public static final String AWAY_TEMP_LOW_C = "away_temperature_low_c";
        public static final String HVAC_MODE = "hvac_mode";
        public static final String AMBIENT_TEMP_F = "ambient_temperature_f";
        public static final String AMBIENT_TEMP_C = "ambient_temperature_c";
    }

    public static class SMOKE_CO_ALARM {
    	public static final String BATTERY_HEALTH = "battery_health";
        public static final String CO_ALARM_STATE = "co_alarm_state";
        public static final String SMOKE_ALARM_STATE = "smoke_alarm_state";
        public static final String UI_COLOR_STATE = "ui_color_state";
        public static final String LAST_MANUAL_TEST_TIME = "last_manual_test_time";
    }

    public static class STRUCTURE {
    	public static final String THERMOSTATS = "thermostats";
    	public static final String SMOKE_CO_ALARMS = "smoke_co_alarms";
    	public static final String AWAY = "away";
    	public static final String NAME = "name";
    	public static final String COUNTRY_CODE = "country_code";
    	public static final String PEAK_PERIOD_START_TIME = "peak_period_start_time";
    	public static final String PEAK_PERIOD_END_TIME = "peak_period_end_time";
    	public static final String TIME_ZONE = "time_zone";
    	public static final String ETA = "eta";
    }

    public static class ETA {
    	public static final String TRIP_ID = "trip_id";
    	public static final String ESTIMATED_ARRIVAL_WINDOW_BEGIN = "estimated_arrival_window_begin";
    	public static final String ESTIMATED_ARRIVAL_WINDOW_END = "estimated_arrival_window_end";
    }
}
