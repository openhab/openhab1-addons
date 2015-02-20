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


import java.util.Map;

@SuppressWarnings("unused")
abstract class BaseDevice  {
    private static final String TAG = BaseDevice.class.getSimpleName();

    private final String mDeviceID;
    private final String mLocale;
    private final String mSoftwareVersion;
    private final String mStructureID;
    private final String mName;
    private final String mNameLong;
    private final String mLastConnection;
    private final boolean mIsOnline;

    protected BaseDevice(BaseDeviceBuilder<?> builder) {
        mDeviceID = builder.mDeviceID;
        mLocale = builder.mLocale;
        mSoftwareVersion = builder.mSoftwareVersion;
        mStructureID = builder.mStructureID;
        mName = builder.mName;
        mNameLong = builder.mNameLong;
        mLastConnection = builder.mLastConnection;
        mIsOnline = builder.mIsOnline;
    }

    /**
     * Returns the unique identifier of this device.
     */
    public String getDeviceID() {
        return mDeviceID;
    }

    /**
     * Returns the locale for this device, if set.
     */
    public String getLocale() {
        return mLocale;
    }

    /**
     * Returns the current software version that this device
     * has installed.
     */
    public String getSoftwareVersion() {
        return mSoftwareVersion;
    }

    /**
     * Returns the unique identifier of the structure to which this
     * device is paired.
     * @see org.openhab.binding.nest.internal.api.model.Structure
     * @see Structure#getStructureID()
     */
    public String getStructureID() {
        return mStructureID;
    }

    /**
     * Returns an abbreviated version of the user's name for this device.
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns a verbose version of the user's name for this device.
     */
    public String getNameLong() {
        return mNameLong;
    }

    /**
     * Returns the timestamp (in ISO-8601 format) at which the device last connected to the
     * Nest service.
     */
    public String getLastConnection() {
        return mLastConnection;
    }

    protected void save(JSONObject json) throws JSONException {
        json.put(Keys.DEVICE_ID, mDeviceID);
        json.put(Keys.LOCALE, mLocale);
        json.put(Keys.SOFTWARE_VERSION, mSoftwareVersion);
        json.put(Keys.STRUCTURE_ID, mStructureID);
        json.put(Keys.LAST_CONNECTION, mLastConnection);
        json.put(Keys.IS_ONLINE, mIsOnline);
    }

    public JSONObject toJSON() {
        try {
            final JSONObject json = new JSONObject();
            save(json);
            return json;
        } catch (JSONException excep) {
            return null;
        }
    }
    
    static abstract class BaseDeviceBuilder<T extends BaseDevice> {
        private String mDeviceID;
        private String mLocale;
        private String mSoftwareVersion;
        private String mStructureID;
        private String mName;
        private String mNameLong;
        private String mLastConnection;
        private boolean mIsOnline;

        public T fromMap(Map<String, Object> map) {
            return fromJSON(new JSONObject(map));
        }

        public T fromJSON(JSONObject json) {
            setDeviceID(json.optString(Keys.DEVICE_ID));
            setLocale(json.optString(Keys.LOCALE));
            setSoftwareVersion(json.optString(Keys.SOFTWARE_VERSION));
            setStructureID(json.optString(Keys.STRUCTURE_ID));
            setName(json.optString(Keys.NAME));
            setNameLong(json.optString(Keys.NAME_LONG));
            setLastConnection(json.optString(Keys.LAST_CONNECTION));
            setOnline(json.optBoolean(Keys.IS_ONLINE));
            return build();
        }
        
        public BaseDeviceBuilder<T> setDeviceID(String deviceID) {
            mDeviceID = deviceID;
            return this;
        }

        public BaseDeviceBuilder<T> setLocale(String locale) {
            mLocale = locale;
            return this;
        }

        public BaseDeviceBuilder<T> setSoftwareVersion(String softwareVersion) {
            mSoftwareVersion = softwareVersion;
            return this;
        }

        public BaseDeviceBuilder<T> setStructureID(String structureID) {
            mStructureID = structureID;
            return this;
        }

        public BaseDeviceBuilder<T> setName(String name) {
            mName = name;
            return this;
        }

        public BaseDeviceBuilder<T> setNameLong(String nameLong) {
            mNameLong = nameLong;
            return this;
        }

        public BaseDeviceBuilder<T> setLastConnection(String lastConnection) {
            mLastConnection = lastConnection;
            return this;
        }

        public BaseDeviceBuilder<T> setOnline(boolean isOnline) {
            mIsOnline = isOnline;
            return this;
        }

        public abstract T build();
    }
}
