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

import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.binding.nest.internal.api.model.BaseDevice.BaseDeviceBuilder;
import org.openhab.binding.nest.internal.api.model.Keys.THERMOSTAT;


@SuppressWarnings("unused")
public final class Thermostat extends BaseDevice {
    private final boolean mCanCool;
    private final boolean mCanHeat;
    private final boolean mIsUsingEmergencyHeat;
    private final boolean mHasFan;
    private final String mFanTimerTimeout;
    private final boolean mHasLeaf;
    private final String mTemperatureScale;
    private final long mAwayTemperatureHighF;
    private final double mAwayTemperatureHighC;
    private final long mAwayTemperatureLowF;
    private final double mAwayTemperatureLowC;
    private final long mAmbientTemperatureF;
    private final double mAmbientTemperatureC;
    private final boolean mFanTimerActive;
    private final long mTargetTemperatureF;
    private final double mTargetTemperatureC;
    private final long mTargetTemperatureHighF;
    private final double mTargetTemperatureHighC;
    private final long mTargetTemperatureLowF;
    private final double mTargetTemperatureLowC;
    private final HVACMode mHVACmode;

    private Thermostat(Builder builder) {
        super(builder);
        mCanCool = builder.mCanCool;
        mCanHeat = builder.mCanHeat;
        mIsUsingEmergencyHeat = builder.mIsUsingEmergencyHeat;
        mHasFan = builder.mHasFan;
        mFanTimerTimeout = builder.mFanTimerTimeout;
        mHasLeaf = builder.mHasLeaf;
        mTemperatureScale = builder.mTemperatureScale;
        mAwayTemperatureHighF = builder.mAwayTemperatureHighF;
        mAwayTemperatureHighC = builder.mAwayTemperatureHighC;
        mAwayTemperatureLowF = builder.mAwayTemperatureLowF;
        mAwayTemperatureLowC = builder.mAwayTemperatureLowC;
        mAmbientTemperatureF = builder.mAmbientTemperatureF;
        mAmbientTemperatureC = builder.mAmbientTemperatureC;
        mFanTimerActive = builder.mFanTimerActive;
        mTargetTemperatureF = builder.mTargetTemperatureF;
        mTargetTemperatureC = builder.mTargetTemperatureC;
        mTargetTemperatureHighF = builder.mTargetTemperatureHighF;
        mTargetTemperatureHighC = builder.mTargetTemperatureHighC;
        mTargetTemperatureLowF = builder.mTargetTemperatureLowF;
        mTargetTemperatureLowC = builder.mTargetTemperatureLowC;
        mHVACmode = builder.mHVACmode;
    }

    public static class Builder extends BaseDeviceBuilder<Thermostat> {
        private boolean mCanCool;
        private boolean mCanHeat;
        private boolean mIsUsingEmergencyHeat;
        private boolean mHasFan;
        private boolean mFanTimerActive;
        private String mFanTimerTimeout;
        private boolean mHasLeaf;
        private String mTemperatureScale;
        private long mTargetTemperatureF;
        private double mTargetTemperatureC;
        private long mTargetTemperatureHighF;
        private double mTargetTemperatureHighC;
        private long mTargetTemperatureLowF;
        private double mTargetTemperatureLowC;
        private long mAwayTemperatureHighF;
        private double mAwayTemperatureHighC;
        private long mAwayTemperatureLowF;
        private double mAwayTemperatureLowC;
        private HVACMode mHVACmode;
        private long mAmbientTemperatureF;
        private double mAmbientTemperatureC;

        public Thermostat fromJSON(JSONObject json) {
            super.fromJSON(json);
            setCanCool(json.optBoolean(Keys.THERMOSTAT.CAN_COOL));
            setCanHeat(json.optBoolean(Keys.THERMOSTAT.CAN_HEAT));
            setUsingEmergencyHeat(json.optBoolean(Keys.THERMOSTAT.IS_USING_EMERGENCY_HEAT));
            setHasFan(json.optBoolean(Keys.THERMOSTAT.HAS_FAN));
            setFanTimerTimeout(json.optString(Keys.THERMOSTAT.FAN_TIMER_TIMEOUT));
            setHasLeaf(json.optBoolean(Keys.THERMOSTAT.HAS_LEAF));
            setTemperatureScale(json.optString(Keys.THERMOSTAT.TEMP_SCALE));
            setAwayTemperatureHighF(json.optLong(Keys.THERMOSTAT.AWAY_TEMP_HIGH_F));
            setAwayTemperatureHighC(json.optDouble(Keys.THERMOSTAT.AWAY_TEMP_HIGH_C));
            setAwayTemperatureLowF(json.optLong(Keys.THERMOSTAT.AWAY_TEMP_LOW_F));
            setAwayTemperatureLowC(json.optDouble(Keys.THERMOSTAT.AWAY_TEMP_LOW_C));
            setAmbientTemperatureF(json.optLong(Keys.THERMOSTAT.AMBIENT_TEMP_F));
            setAmbientTemperatureC(json.optDouble(Keys.THERMOSTAT.AMBIENT_TEMP_C));
            setFanTimerActive(json.optBoolean(Keys.THERMOSTAT.FAN_TIMER_ACTIVE));
            setTargetTemperatureF(json.optLong(Keys.THERMOSTAT.TARGET_TEMP_F));
            setTargetTemperatureC(json.optDouble(Keys.THERMOSTAT.TARGET_TEMP_C));
            setTargetTemperatureHighF(json.optLong(Keys.THERMOSTAT.TARGET_TEMP_HIGH_F));
            setTargetTemperatureHighC(json.optDouble(Keys.THERMOSTAT.TARGET_TEMP_HIGH_C));
            setTargetTemperatureLowF(json.optLong(Keys.THERMOSTAT.TARGET_TEMP_LOW_F));
            setTargetTemperatureLowC(json.optDouble(Keys.THERMOSTAT.TARGET_TEMP_LOW_C));
            setHVACmode(json.optString(Keys.THERMOSTAT.HVAC_MODE));
            return build();
        }

        public Builder setCanCool(boolean canCool) {
            mCanCool = canCool;
            return this;
        }

        public Builder setCanHeat(boolean canHeat) {
            mCanHeat = canHeat;
            return this;
        }

        public Builder setUsingEmergencyHeat(boolean isUsingEmergencyHeat) {
            mIsUsingEmergencyHeat = isUsingEmergencyHeat;
            return this;
        }

        public Builder setHasFan(boolean hasFan) {
            mHasFan = hasFan;
            return this;
        }

        public Builder setFanTimerActive(boolean fanTimerActive) {
            mFanTimerActive = fanTimerActive;
            return this;
        }

        public Builder setFanTimerTimeout(String fanTimerTimeout) {
            mFanTimerTimeout = fanTimerTimeout;
            return this;
        }

        public Builder setHasLeaf(boolean hasLeaf) {
            mHasLeaf = hasLeaf;
            return this;
        }

        public Builder setTemperatureScale(String temperatureScale) {
            mTemperatureScale = temperatureScale;
            return this;
        }

        public Builder setTargetTemperatureF(long targetTemperatureF) {
            mTargetTemperatureF = targetTemperatureF;
            return this;
        }

        public Builder setTargetTemperatureC(double targetTemperatureC) {
            mTargetTemperatureC = targetTemperatureC;
            return this;
        }

        public Builder setTargetTemperatureHighF(long targetTemperatureHighF) {
            mTargetTemperatureHighF = targetTemperatureHighF;
            return this;
        }

        public Builder setTargetTemperatureHighC(double targetTemperatureHighC) {
            mTargetTemperatureHighC = targetTemperatureHighC;
            return this;
        }

        public Builder setTargetTemperatureLowF(long targetTemperatureLowF) {
            mTargetTemperatureLowF = targetTemperatureLowF;
            return this;
        }

        public Builder setTargetTemperatureLowC(double targetTemperatureLowC) {
            mTargetTemperatureLowC = targetTemperatureLowC;
            return this;
        }

        public Builder setAwayTemperatureHighF(long awayTemperatureHighF) {
            mAwayTemperatureHighF = awayTemperatureHighF;
            return this;
        }

        public Builder setAwayTemperatureHighC(double awayTemperatureHighC) {
            mAwayTemperatureHighC = awayTemperatureHighC;
            return this;
        }

        public Builder setAwayTemperatureLowF(long awayTemperatureLowF) {
            mAwayTemperatureLowF = awayTemperatureLowF;
            return this;
        }

        public Builder setAwayTemperatureLowC(double awayTemperatureLowC) {
            mAwayTemperatureLowC = awayTemperatureLowC;
            return this;
        }

        public Builder setHVACmode(String mode) {
            mHVACmode = HVACMode.from(mode);
            return this;
        }

        public Builder setHVACmode(HVACMode HVACmode) {
            mHVACmode = HVACmode;
            return this;
        }

        public Builder setAmbientTemperatureF(long ambientTemperatureF) {
            mAmbientTemperatureF = ambientTemperatureF;
            return this;
        }

        public Builder setAmbientTemperatureC(double ambientTemperatureC) {
            mAmbientTemperatureC = ambientTemperatureC;
            return this;
        }

        public Thermostat build() {
            return new Thermostat(this);
        }
    }

    @Override
    protected void save(JSONObject json) throws JSONException {
        super.save(json);
        json.put(Keys.THERMOSTAT.CAN_COOL, mCanCool);
        json.put(Keys.THERMOSTAT.CAN_HEAT, mCanHeat);
        json.put(Keys.THERMOSTAT.IS_USING_EMERGENCY_HEAT, mIsUsingEmergencyHeat);
        json.put(Keys.THERMOSTAT.HAS_FAN, mHasFan);
        json.put(Keys.THERMOSTAT.FAN_TIMER_ACTIVE, mFanTimerActive);
        json.put(Keys.THERMOSTAT.FAN_TIMER_TIMEOUT, mFanTimerTimeout);
        json.put(Keys.THERMOSTAT.HAS_LEAF, mHasLeaf);
        json.put(Keys.THERMOSTAT.TEMP_SCALE, mTemperatureScale);
        json.put(Keys.THERMOSTAT.TARGET_TEMP_F, mTargetTemperatureF);
        json.put(Keys.THERMOSTAT.TARGET_TEMP_C, mTargetTemperatureC);
        json.put(Keys.THERMOSTAT.TARGET_TEMP_HIGH_F, mTargetTemperatureHighF);
        json.put(Keys.THERMOSTAT.TARGET_TEMP_HIGH_C, mTargetTemperatureHighC);
        json.put(Keys.THERMOSTAT.TARGET_TEMP_LOW_F, mTargetTemperatureLowF);
        json.put(Keys.THERMOSTAT.TARGET_TEMP_LOW_C, mTargetTemperatureLowC);
        json.put(Keys.THERMOSTAT.AWAY_TEMP_HIGH_F, mAwayTemperatureHighF);
        json.put(Keys.THERMOSTAT.AWAY_TEMP_HIGH_C, mAwayTemperatureHighC);
        json.put(Keys.THERMOSTAT.AWAY_TEMP_LOW_F, mAwayTemperatureLowF);
        json.put(Keys.THERMOSTAT.AWAY_TEMP_LOW_C, mAwayTemperatureLowC);
        json.put(Keys.THERMOSTAT.HVAC_MODE, mHVACmode.getKey());
        json.put(Keys.THERMOSTAT.AMBIENT_TEMP_F, mAmbientTemperatureF);
        json.put(Keys.THERMOSTAT.AMBIENT_TEMP_C, mAmbientTemperatureC);
    }

    /**
     * Returns true if this thermostat is connected to a cooling system
     */
    public boolean canCool() {
        return mCanCool;
    }

    /**
     * Returns true if this thermostat is connected to a heating system
     */
    public boolean canHeat() {
        return mCanHeat;
    }

    /**
     * Returns true if this thermostat is currently operating using the
     * emergency heating system
     */
    public boolean isUsingEmergencyHeat() {
        return mIsUsingEmergencyHeat;
    }

    /**
     * Returns true if this thermostat has a connected fan
     */
    public boolean hasFan() {
        return mHasFan;
    }

    /**
     * If the fan is running on a timer, this provides the timestamp
     * (in ISO-8601 format) at which the fan will stop running.
     * @see #hasFan()
     * @see #isFanTimerActive()
     */
    public String getFanTimerTimeout() {
        return mFanTimerTimeout;
    }

    /**
     * Returns true if the thermostat is currently displaying the leaf indicator
     */
    public boolean hasLeaf() {
        return mHasLeaf;
    }

    /**
     * Returns the temperature scale: one of "C" (Celsius) or "F" (Fahrenheit)
     * that this thermostat should display temperatures in.
     */
    public String getTemperatureScale() {
        return mTemperatureScale;
    }

    /**
     * Returns the temperature (in Fahrenheit) at which the cooling
     * system will engage when in "Away" state.
     */
    public long getAwayTemperatureHighF() {
        return mAwayTemperatureHighF;
    }

    /**
     * Returns the temperature (in Celsius) at which the cooling
     * system will engage when in "Away" state.
     */
    public double getAwayTemperatureHighC() {
        return mAwayTemperatureHighC;
    }

    /**
     * Returns the temperature (in Fahrenheit) at which the heating
     * system will engage when in "Away" state.
     */
    public long getAwayTemperatureLowF() {
        return mAwayTemperatureLowF;
    }

    /**
     * Returns the temperature (in Celsius) at which the heating
     * system will engage when in "Away" state.
     */
    public double getAwayTemperatureLowC() {
        return mAwayTemperatureLowC;
    }

    /**
     * Returns the current ambient temperature in the structure
     * in Fahrenheit.
     */
    public long getAmbientTemperatureF() {
        return mAmbientTemperatureF;
    }

    /**
     * Returns the current ambient temperature in the structure
     * in Celsius.
     */
    public double getAmbientTemperatureC() {
        return mAmbientTemperatureC;
    }

    /**
     * Returns true if the fan is currently running on a timer
     * @see #getFanTimerTimeout()
     * @see #hasFan()
     */
    public boolean isFanTimerActive() {
        return mFanTimerActive;
    }

    /**
     * Returns the target temperature of the thermostat in Fahrenheit.
     * Note that this is only applicable when in Heat or Cool mode,
     * not "Heat and Cool" mode.
     *
     * @see org.openhab.binding.nest.internal.api.model.Thermostat.HVACMode
     */
    public long getTargetTemperatureF() {
        return mTargetTemperatureF;
    }

    /**
     * Returns the target temperature of the thermostat in Celsius.
     * Note that this is only applicable when in Heat or Cool mode,
     * not "Heat and Cool" mode.
     *
     * @see org.openhab.binding.nest.internal.api.model.Thermostat.HVACMode
     */
    public double getTargetTemperatureC() {
        return mTargetTemperatureC;
    }

    /**
     * Returns the target temperature of the cooling system in Fahrenheit
     * when in "Heat and Cool" mode.
     *
     * @see org.openhab.binding.nest.internal.api.model.Thermostat.HVACMode
     */
    public long getTargetTemperatureHighF() {
        return mTargetTemperatureHighF;
    }

    /**
     * Returns the target temperature of the cooling system in Celsius
     * when in "Heat and Cool" mode.
     *
     * @see org.openhab.binding.nest.internal.api.model.Thermostat.HVACMode
     */
    public double getTargetTemperatureHighC() {
        return mTargetTemperatureHighC;
    }

    /**
     * Returns the target temperature of the heating system in Celsius
     * when in "Heat and Cool" mode.
     *
     * @see org.openhab.binding.nest.internal.api.model.Thermostat.HVACMode
     */
    public long getTargetTemperatureLowF() {
        return mTargetTemperatureLowF;
    }

    /**
     * Returns the target temperature of the heating system in Fahrenheit
     * when in "Heat and Cool" mode.
     *
     * @see org.openhab.binding.nest.internal.api.model.Thermostat.HVACMode
     */
    public double getTargetTemperatureLowC() {
        return mTargetTemperatureLowC;
    }

    /**
     * Returns the current operating mode of the thermostat.
     * @see org.openhab.binding.nest.internal.api.model.Thermostat.HVACMode
     */
    public HVACMode getHVACmode() {
        return mHVACmode;
    }

    public static enum HVACMode {
        HEAT("heat"),
        COOL("cool"),
        HEAT_AND_COOL("heat-cool"),
        OFF("off"),
        UNKNOWN("unknown");

        private final String mKey;

        HVACMode(String key) {
            mKey = key;
        }

        public String getKey() {
            return mKey;
        }

        public static HVACMode from(String key) {
            for (HVACMode mode : values()) {
                if (mode.mKey.equalsIgnoreCase(key)) {
                    return mode;
                }
            }
            return UNKNOWN;
        }
    }
}
