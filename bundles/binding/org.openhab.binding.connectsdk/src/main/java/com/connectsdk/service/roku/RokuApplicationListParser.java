/*
 * RokuApplicationListParser
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 26 Feb 2014
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

package com.connectsdk.service.roku;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.connectsdk.core.AppInfo;

public class RokuApplicationListParser extends DefaultHandler {
    public String value;

    public final String APP = "app";
    public final String ID = "id";

    public List<AppInfo> appList;
    public AppInfo appInfo;

    public RokuApplicationListParser() {
        value = null;
        appList = new ArrayList<AppInfo>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, final Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(APP)) {
            final int index = attributes.getIndex(ID);

            if ( index != -1 ) {
                appInfo = new AppInfo() {{
                    setId(attributes.getValue(index));
                }};
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(APP)) {
            appInfo.setName(value);
            appList.add(appInfo);
        }
        value = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        value = new String(ch, start, length);
    }

    public List<AppInfo> getApplicationList() {
        return appList;
    }
}
