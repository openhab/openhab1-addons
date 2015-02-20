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
package org.openhab.binding.nest.internal.api.listeners;

import org.openhab.binding.nest.internal.api.model.SmokeCOAlarm;
import org.openhab.binding.nest.internal.api.model.Structure;
import org.openhab.binding.nest.internal.api.model.Thermostat;

/**
 * This class is used to build a collection of data listeners for updates
 * to user data (devices and structure details).
 */
public final class Listener {
    private final ThermostatListener mThermostatListener;
    private final StructureListener mStructureListener;
    private final SmokeCOAlarmListener mSmokeCOAlarmListener;

    private Listener(Builder builder) {
        mThermostatListener = builder.mThermostatListener;
        mStructureListener = builder.mStructureListener;
        mSmokeCOAlarmListener = builder.mSmokeCOAlarmListener;
    }

    public ThermostatListener getThermostatListener() {
        return mThermostatListener;
    }

    public StructureListener getStructureListener() {
        return mStructureListener;
    }

    public SmokeCOAlarmListener getSmokeCOAlarmListener() {
        return mSmokeCOAlarmListener;
    }

    public static class Builder {
        private ThermostatListener mThermostatListener = null;
        private StructureListener mStructureListener = null;
        private SmokeCOAlarmListener mSmokeCOAlarmListener = null;

        public Builder setThermostatListener(ThermostatListener listener) {
            mThermostatListener = listener;
            return this;
        }

        public Builder setStructureListener(StructureListener listener) {
            mStructureListener = listener;
            return this;
        }

        public Builder setSmokeCOAlarmListener(SmokeCOAlarmListener listener) {
            mSmokeCOAlarmListener = listener;
            return this;
        }

        public Listener build() {
            return new Listener(this);
        }
    }

    public interface ThermostatListener {
        /**
         * Called when updated data is retrieved for a Thermostat.
         * @param thermostat the new data for the thermostat (guaranteed
         *                   to be non-null)
         */
        void onThermostatUpdated(Thermostat thermostat);
    }

    public interface StructureListener {
        /**
         * Called when updated data is retrieved for a user's structure.
         * @param structure the new data for the structure (guaranteed
         *                  to be non-null)
         */
        void onStructureUpdated(Structure structure);
    }

    public interface SmokeCOAlarmListener {
        /**
         * Called when updated data is retrieved for a Nest Protect.
         * @param smokeCOAlarm the new data for the Nest Protect (guaranteed
         *                     to be non-null)
         */
        void onSmokeCOAlarmUpdated(SmokeCOAlarm smokeCOAlarm);
    }
}