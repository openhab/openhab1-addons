/*
 * NetcastApplicationsParser
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

public class NetcastApplicationsParser extends DefaultHandler {
    public JSONArray applicationList;
    public JSONObject application;

    public String value;

    public final String DATA = "data";
    public final String AUID = "auid";
    public final String NAME = "name";
    public final String TYPE = "type";
    public final String CPID = "cpid";
    public final String ADULT = "adult";
    public final String ICON_NAME = "icon_name";

    public NetcastApplicationsParser() {
        applicationList = new JSONArray();
        value = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(DATA)) {
            application = new JSONObject();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (qName.equalsIgnoreCase(DATA)) {
                applicationList.put(application);
            }
            else if (qName.equalsIgnoreCase(AUID)) {
                application.put("id", value);
            }
            else if (qName.equalsIgnoreCase(NAME)) {
                application.put("title", value);
            }
            else if (qName.equalsIgnoreCase(TYPE)) {
                application.put(TYPE, value);
            }
            else if (qName.equalsIgnoreCase(CPID)) {
                application.put(CPID, value);
            }
            else if (qName.equalsIgnoreCase(ADULT)) {
                application.put(ADULT, value);
            }
            else if (qName.equalsIgnoreCase(ICON_NAME)) {
                application.put(ICON_NAME, value);
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

    public JSONArray getApplications() {
        return applicationList;
    }
}
