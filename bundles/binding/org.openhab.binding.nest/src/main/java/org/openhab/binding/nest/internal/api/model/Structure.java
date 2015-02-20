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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.binding.nest.internal.api.model.Keys.ETA;
import org.openhab.binding.nest.internal.api.model.Keys.STRUCTURE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Structure {
	private static final Logger logger = LoggerFactory.getLogger(Structure.class);
    private static final String TAG = Structure.class.getSimpleName();

    private final String mStructureID;
    private final List<String> mThermostatIDs;
    private final List<String> mSmokeCOAlarms;
    private final String mName;
    private final String mCountryCode;
    private final String mPeakPeriodStartTime;
    private final String mPeakPeriodEndTime;
    private final String mTimeZone;
    private final AwayState mAwayState;
    private final ETA mETA;

    private Structure(Builder builder) {
        mStructureID = builder.mStructureID;
        mThermostatIDs = builder.mThermostatIDs;
        mSmokeCOAlarms = builder.mSmokeCOAlarms;
        mName = builder.mName;
        mCountryCode = builder.mCountryCode;
        mPeakPeriodStartTime = builder.mPeakPeriodStartTime;
        mPeakPeriodEndTime = builder.mPeakPeriodEndTime;
        mTimeZone = builder.mTimeZone;
        mAwayState = builder.mAwayState;
        mETA = builder.mETA;
    }

    public static class Builder {
        private String mStructureID;
        private List<String> mThermostatIDs = new ArrayList<>();
        private List<String> mSmokeCOAlarms = new ArrayList<>();
        private String mName;
        private String mCountryCode;
        private String mPeakPeriodStartTime;
        private String mPeakPeriodEndTime;
        private String mTimeZone;
        private AwayState mAwayState;
        private ETA mETA;

        public Structure fromJSON(JSONObject json) {
            setStructureID(json.optString(Keys.STRUCTURE_ID));
            setThermostatIDs(convertToList(json.optJSONArray(Keys.STRUCTURE.THERMOSTATS)));
            setSmokeCOAlarms(convertToList(json.optJSONArray(Keys.STRUCTURE.SMOKE_CO_ALARMS)));
            setAwayState(json.optString(Keys.STRUCTURE.AWAY));
            setName(json.optString(Keys.STRUCTURE.NAME));
            setCountryCode(json.optString(Keys.STRUCTURE.COUNTRY_CODE));
            setPeakPeriodStartTime(json.optString(Keys.STRUCTURE.PEAK_PERIOD_START_TIME));
            setPeakPeriodEndTime(json.optString(Keys.STRUCTURE.PEAK_PERIOD_END_TIME));
            setTimeZone(json.optString(Keys.STRUCTURE.TIME_ZONE));
            setETA(new ETA.Builder().fromJSON(json.optJSONObject(Keys.STRUCTURE.ETA)));
            return new Structure(this);
        }

        public Structure fromMap(Map<String, Object> map) {
            return fromJSON(new JSONObject(map));
        }

        public Builder setStructureID(String structureID) {
            mStructureID = structureID;
            return this;
        }

        public Builder setThermostatIDs(List<String> thermostats) {
            mThermostatIDs = thermostats;
            return this;
        }

        public Builder setSmokeCOAlarms(List<String> alarms) {
            mSmokeCOAlarms = alarms;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setCountryCode(String countryCode) {
            mCountryCode = countryCode;
            return this;
        }

        public Builder setPeakPeriodStartTime(String peakPeriodStartTime) {
            mPeakPeriodStartTime = peakPeriodStartTime;
            return this;
        }

        public Builder setPeakPeriodEndTime(String peakPeriodEndTime) {
            mPeakPeriodEndTime = peakPeriodEndTime;
            return this;
        }

        public Builder setTimeZone(String timeZone) {
            mTimeZone = timeZone;
            return this;
        }

        public Builder setAwayState(String away) {
            mAwayState = AwayState.from(away);
            return this;
        }

        public Builder setAwayState(AwayState away) {
            mAwayState = away;
            return this;
        }

        public Builder setETA(ETA eta) {
            mETA = eta;
            return this;
        }

        public Structure build() {
            return new Structure(this);
        }
    }

    private static List<String> convertToList(JSONArray array) {
        List<String> list = new ArrayList<>();
        try {
            if(array == null) {
                return list;
            }
            for(int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        }
        catch (JSONException excep) {
            logger.error(TAG, "Unable to parse array from json: " + array.toString());
        }
        return list;

    }

    /** Returns the unique identifier for this structure. */
    public String getStructureID() {
        return mStructureID;
    }

    /** Returns an unsorted list of all thermostat IDs that
     *  are paired with this structure. */
    public List<String> getThermostatIDs() {
        return new ArrayList<>(mThermostatIDs);
    }

    /** Returns an unsorted list of all Nest Protect IDs that
     *  are paired with this structure. */
    public List<String> getSmokeCOAlarms() {
        return new ArrayList<>(mSmokeCOAlarms);
    }

    /**
     * Gets the current occupancy state of the structure (Home,
     * Away, or Auto Away)
     * @see org.openhab.binding.nest.internal.api.model.Structure.AwayState */
    public AwayState getAwayState() {
        return mAwayState;
    }

    /** Returns the users's name for this structure */
    public String getName() {
        return mName;
    }

    /**
     * Returns the country code for this structure is located in,
     * if it has been set. Returns an empty string otherwise.
     */
    public String getCountryCode() {
        return mCountryCode;
    }

    /**
     * Get the time (in ISO-8601 format) at which a Rush Hour Rewards
     * event will begin adjusting enrolled thermostats. Returns an empty string
     * if no event is active.
     */
    public String getPeakPeriodStartTime() {
        return mPeakPeriodStartTime;
    }

    /**
     * Get the time (in ISO-8601 format) at which a Rush Hour Rewards
     * event will stop adjusting enrolled thermostats. Returns an empty string
     * if no event is active.
     */
    public String getPeakPeriodEndTime() {
        return mPeakPeriodEndTime;
    }

    /**
     * Returns the Olson code (e.g. "America/Los_Angeles") of the
     * structure's location, if set. Returns an empty string otherwise.
     */
    public String getTimeZone() {
        return mTimeZone;
    }

    /**
     * Returns the ETA window for this structure, if one exists.
     * @see org.openhab.binding.nest.internal.api.model.Structure.ETA
     */
    public ETA getETA() {
        return mETA;
    }

    /**
     * Returns the number of thermostats paired with this structure
     */
    public int getThermostatCount() {
        return mThermostatIDs.size();
    }

    /**
     * Returns the number of Nest Protect Smoke & CO Detectors paired
     * with this structure.
     */
    public int getSmokeCOAlarmCount() {
        return mSmokeCOAlarms.size();
    }

    /**
     * Returns the contents of this structure, written to a JSONObject.
     */
    public JSONObject toJSON() {
        final JSONObject json = new JSONObject();

        try {
            json.put(Keys.STRUCTURE_ID, mStructureID);
            json.put(Keys.STRUCTURE.THERMOSTATS, new JSONArray(mThermostatIDs));
            json.put(Keys.STRUCTURE.SMOKE_CO_ALARMS, new JSONArray(mSmokeCOAlarms));
            json.put(Keys.STRUCTURE.AWAY, mAwayState);
            json.put(Keys.STRUCTURE.NAME, mName);
            json.put(Keys.STRUCTURE.COUNTRY_CODE, mCountryCode);
            json.put(Keys.STRUCTURE.PEAK_PERIOD_START_TIME, mPeakPeriodStartTime);
            json.put(Keys.STRUCTURE.PEAK_PERIOD_END_TIME, mPeakPeriodEndTime);
            json.put(Keys.STRUCTURE.TIME_ZONE, mTimeZone);
            json.put(Keys.STRUCTURE.ETA, mETA == null ? null : mETA.toJSON());
            return json;
        } catch (JSONException excep) {
            // This will never happen, as JSONException is only thrown for
            // null keys.
            logger.warn(TAG, "Unable to generate JSON object for Structure");
            return json;
        }
    }

    public static enum AwayState {
        /** State when the user has explicitly set the structure
         *  to "Away" state. */
        AWAY("away"),

        /** State when Nest has detected that the structure is not
         *  occupied. */
        AUTO_AWAY("auto-away"),

        /** State when the user is at home */
        HOME("home"),

        /** State for when the home/away status is unknown */
        UNKNOWN("unknown");

        private static final AwayState[] sValues = values();
        private final String mKey;

        AwayState(String key) {
            mKey = key;
        }

        public String getKey() {
            return mKey;
        }

        public static AwayState from(String key) {
            for (AwayState state : sValues) {
                if (state.mKey.equals(key)) {
                    return state;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Class that represents an estimated time of arrival (ETA) event for
     * a structure, including an identified, and timestamps for the estimated
     * beginning and end of the arrival window.
     */
    public static class ETA {

        private final static String TAG = ETA.class.getSimpleName();

        private final String mTripID;
        private final String mEstimatedArrivalWindowBegin;
        private final String mEstimatedArrivalWindowEnd;

        private ETA(Builder builder) {
            mTripID = builder.mTripID;
            mEstimatedArrivalWindowBegin = builder.mEstimatedArrivalWindowBegin;
            mEstimatedArrivalWindowEnd = builder.mEstimatedArrivalWindowEnd;
        }

        /**
         * Returns the identifier for this ETA event.
         */
        public String getTripID() {
            return mTripID;
        }

        /**
         * Gets the beginning of the estimated time window (in ISO-8601 format) at which
         * the user is expected to return to the structure. Returns an empty string
         * or null if not set.
         * @see #getEstimatedArrivalWindowEnd()
         */
        public String getEstimatedArrivalWindowBegin() {
            return mEstimatedArrivalWindowBegin;
        }

        /**
         * Gets the end of the estimated time window (in ISO-8601 format) at which
         * the user is expected to return to the structure. Returns an empty string
         * or null if not set.
         * @see #getEstimatedArrivalWindowBegin()
         */
        public String getEstimatedArrivalWindowEnd() {
            return mEstimatedArrivalWindowEnd;
        }

        HashMap<String, Object> toMap() {
            final HashMap<String, Object> etaMap = new HashMap<>();
            etaMap.put(Keys.ETA.TRIP_ID, mTripID);
            etaMap.put(Keys.ETA.ESTIMATED_ARRIVAL_WINDOW_BEGIN, mEstimatedArrivalWindowBegin);
            etaMap.put(Keys.ETA.ESTIMATED_ARRIVAL_WINDOW_END, mEstimatedArrivalWindowEnd);
            return etaMap;
        }

        public JSONObject toJSON() {
            final JSONObject json = new JSONObject();

            try {
                json.put(Keys.ETA.TRIP_ID, mTripID);
                json.put(Keys.ETA.ESTIMATED_ARRIVAL_WINDOW_BEGIN, mEstimatedArrivalWindowBegin);
                json.put(Keys.ETA.ESTIMATED_ARRIVAL_WINDOW_END, mEstimatedArrivalWindowEnd);
            } catch (JSONException excep) {
                // This will never happen, as a JSONException is only thrown for
                // null keys.
            	logger.error(TAG, "Unable to generate JSON object for ETA", excep);
            }

            return json;
        }

        public static class Builder {
            private String mTripID;
            private String mEstimatedArrivalWindowBegin;
            private String mEstimatedArrivalWindowEnd;

            public Builder setTripID(String tripID) {
                mTripID = tripID;
                return this;
            }

            public Builder setEstimatedArrivalWindowBegin(String windowBegin) {
                mEstimatedArrivalWindowBegin = windowBegin;
                return this;
            }

            public Builder setEstimatedArrivalWindowEnd(String windowEnd) {
                mEstimatedArrivalWindowEnd = windowEnd;
                return this;
            }
            public Builder setEstimatedArrivalWindowBegin(long utc, TimeZone timeZone) {
                mEstimatedArrivalWindowBegin = convertToISO8601(utc, timeZone);
                return this;
            }

            public Builder setEstimatedArrivalWindowEnd(long utc, TimeZone timeZone) {
                mEstimatedArrivalWindowEnd = convertToISO8601(utc, timeZone);
                return this;
            }

            public ETA fromJSON(JSONObject json){
                if(json == null) {
                    return null;
                }
                mTripID = json.optString(Keys.ETA.TRIP_ID);
                mEstimatedArrivalWindowBegin = json.optString(Keys.ETA.ESTIMATED_ARRIVAL_WINDOW_BEGIN);
                mEstimatedArrivalWindowEnd = json.optString(Keys.ETA.ESTIMATED_ARRIVAL_WINDOW_END);
                return new ETA(this);
            }

            public ETA fromMap(HashMap<String, Object> etaMap) {
                return fromJSON(new JSONObject(etaMap));
            }

            public ETA build() {
                return new ETA(this);
            }

            private String convertToISO8601(long utcMillis, TimeZone timeZone) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
                Date date = new Date();
                date.setTime(utcMillis);
                return df.format(date);
            }

        }

        @Override
        public String toString() {
            return "ETA{" +
                    "mTripID='" + mTripID + '\'' +
                    ", mEstimatedArrivalWindowBegin='" + mEstimatedArrivalWindowBegin + '\'' +
                    ", mEstimatedArrivalWindowEnd='" + mEstimatedArrivalWindowEnd + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Structure{" +
                "mStructureID='" + mStructureID + '\'' +
                ", mThermostatIDs=" + mThermostatIDs +
                ", mSmokeCOAlarms=" + mSmokeCOAlarms +
                ", mAwayState='" + mAwayState + '\'' +
                ", mName='" + mName + '\'' +
                ", mCountryCode='" + mCountryCode + '\'' +
                ", mPeakPeriodStartTime='" + mPeakPeriodStartTime + '\'' +
                ", mPeakPeriodEndTime='" + mPeakPeriodEndTime + '\'' +
                ", mTimeZone='" + mTimeZone + '\'' +
                ", mETA=" + mETA +
                '}';
    }
}
