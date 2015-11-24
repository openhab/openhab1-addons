/*
 * NetcastAppNumberParser
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

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetcastAppNumberParser extends DefaultHandler {
    public String value;

    public final String TYPE = "type";
    public final String NUMBER = "number";

    int count;

    public NetcastAppNumberParser() {
        value = null;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(TYPE)) {
        }
        else if (qName.equalsIgnoreCase(NUMBER)) {
            count = Integer.parseInt(value);
        }
        value = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        value = new String(ch, start, length);
    }

    public int getApplicationNumber() {
        return count;
    }
}
