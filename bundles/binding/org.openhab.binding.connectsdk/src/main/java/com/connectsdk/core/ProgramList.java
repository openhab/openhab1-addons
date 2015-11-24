/*
 * ProgramList
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgramList implements JSONSerializable {
    ChannelInfo channel;
    JSONArray programList;

    public ProgramList(ChannelInfo channel, JSONArray programList) {
        this.channel = channel;
        this.programList = programList;
    }

    public ChannelInfo getChannel() {
        return channel;
    }

    public JSONArray getProgramList() {
        return programList;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("channel", channel != null ? channel.toString() : null);
        obj.put("programList", programList != null ? programList.toString() : null);

        return obj;
    }
}