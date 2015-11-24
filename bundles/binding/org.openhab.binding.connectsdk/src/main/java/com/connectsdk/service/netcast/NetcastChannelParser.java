/*
 * NetcastChannelParser
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

package com.connectsdk.service.netcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.connectsdk.core.ChannelInfo;

public class NetcastChannelParser extends DefaultHandler {
    public JSONArray channelArray;
    public JSONObject channel;

    public String value;

    public final String CHANNEL_TYPE = "chtype";
    public final String MAJOR = "major";
    public final String MINOR = "minor";
    public final String DISPLAY_MAJOR = "displayMajor";
    public final String DISPLAY_MINOR = "displayMinor";
    public final String SOURCE_INDEX = "sourceIndex";
    public final String PHYSICAL_NUM = "physicalNum";
    public final String CHANNEL_NAME = "chname";
    public final String PROGRAM_NAME = "progName";
    public final String AUDIO_CHANNEL = "audioCh";
    public final String INPUT_SOURCE_NAME = "inputSourceName";
    public final String INPUT_SOURCE_TYPE = "inputSourceType";
    public final String LABEL_NAME = "labelName";
    public final String INPUT_SOURCE_INDEX = "inputSourceIdx";

    public NetcastChannelParser() {
        channelArray = new JSONArray();
        value = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("data")) {
            channel = new JSONObject();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (qName.equalsIgnoreCase("data")) {
                channelArray.put(channel);
            }
            else if (qName.equalsIgnoreCase(CHANNEL_TYPE)) {
                channel.put("channelModeName", value);
            }
            else if (qName.equalsIgnoreCase(MAJOR)) {
                channel.put("majorNumber", Integer.parseInt(value));
            }
            else if (qName.equalsIgnoreCase(DISPLAY_MAJOR)) {
                channel.put("displayMajorNumber", Integer.parseInt(value));
            }
            else if (qName.equalsIgnoreCase(MINOR)) {
                channel.put("minorNumber", Integer.parseInt(value));
            }
            else if (qName.equalsIgnoreCase(DISPLAY_MINOR)) {
                channel.put("displayMinorNumber", Integer.parseInt(value));
            }
            else if (qName.equalsIgnoreCase(SOURCE_INDEX)) {
                channel.put("sourceIndex", value);
            }
            else if (qName.equalsIgnoreCase(PHYSICAL_NUM)) {
                channel.put("physicalNumber", Integer.parseInt(value));
            }
            else if (qName.equalsIgnoreCase(CHANNEL_NAME)) {
                channel.put("channelName", value);
            }
            else if (qName.equalsIgnoreCase(PROGRAM_NAME)) {
                channel.put("programName", value);
            }
            else if (qName.equalsIgnoreCase(AUDIO_CHANNEL)) {
                channel.put("audioCh", value);
            }
            else if (qName.equalsIgnoreCase(INPUT_SOURCE_NAME)) {
                channel.put("inputSourceName", value);
            }
            else if (qName.equalsIgnoreCase(INPUT_SOURCE_TYPE)) {
                channel.put("inputSourceType", value);
            }
            else if (qName.equalsIgnoreCase(LABEL_NAME)) {
                channel.put("labelName", value);
            }
            else if (qName.equalsIgnoreCase(INPUT_SOURCE_INDEX)) {
                channel.put("inputSourceIndex", value);
            }
            value = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        value = new String(ch, start, length);
    }

    public JSONArray getJSONChannelArray() {
        return channelArray;
    }

    public static ChannelInfo parseRawChannelData(JSONObject channelRawData) {
        String channelName = null;
        String channelId = null;
        String channelNumber = null;
        int minorNumber = 0;
        int majorNumber = 0;

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setRawData(channelRawData);

        try {
            if (!channelRawData.isNull("channelName")) 
                channelName = (String) channelRawData.get("channelName");

            if (!channelRawData.isNull("channelId")) 
                channelId = (String) channelRawData.get("channelId");

            if (!channelRawData.isNull("majorNumber"))
                majorNumber = (Integer) channelRawData.get("majorNumber");

            if (!channelRawData.isNull("minorNumber"))
                minorNumber = (Integer) channelRawData.get("minorNumber");

            if (!channelRawData.isNull("channelNumber")) 
                channelNumber = (String) channelRawData.get("channelNumber");
            else {
                channelNumber = String.format(String.valueOf(majorNumber) + "-" + String.valueOf(minorNumber));
            }

            channelInfo.setName(channelName);
            channelInfo.setId(channelId);
            channelInfo.setNumber(channelNumber);
            channelInfo.setMajorNumber(majorNumber);
            channelInfo.setMinorNumber(minorNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return channelInfo;
    }
}
