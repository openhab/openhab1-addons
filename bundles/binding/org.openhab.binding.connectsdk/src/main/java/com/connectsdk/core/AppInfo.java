/*
 * AppInfo
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 19 Jan 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.core;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Normalized reference object for information about a DeviceService's app. This
 * object will, in most cases, be used to launch apps.
 * 
 * In some cases, all that is needed to launch an app is the app id.
 */
public class AppInfo implements JSONSerializable {
    // @cond INTERNAL
    String id;
    String name;
    JSONObject raw;

    // @endcond

    /**
     * Default constructor method.
     */
    public AppInfo() {
    }

    /**
     * Default constructor method.
     * 
     * @param id
     *            App id to launch
     */
    public AppInfo(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of the app on the first screen device. Format is different
     * depending on the platform. (ex. youtube.leanback.v4, 0000001134, netflix,
     * etc).
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the app on the first screen device. Format is different
     * depending on the platform. (ex. youtube.leanback.v4, 0000001134, netflix,
     * etc).
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the user-friendly name of the app (ex. YouTube, Browser, Netflix,
     * etc).
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user-friendly name of the app (ex. YouTube, Browser, Netflix,
     * etc).
     */
    public void setName(String name) {
        this.name = name.trim();
    }

    /** Gets the raw data from the first screen device about the app. */
    public JSONObject getRawData() {
        return raw;
    }

    /** Sets the raw data from the first screen device about the app. */
    public void setRawData(JSONObject data) {
        raw = data;
    }

    // @cond INTERNAL
    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("name", name);
        obj.put("id", id);

        return obj;
    }

    // @endcond

    /**
     * Compares two AppInfo objects.
     * 
     * @param o
     *            Other AppInfo object to compare.
     * 
     * @return true if both AppInfo id values are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof AppInfo) {
            AppInfo ai = (AppInfo) o;
            return this.id.equals(ai.id);
        }
        return super.equals(o);
    }
}
