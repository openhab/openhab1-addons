/*
 * PListParser
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 18 Apr 2014
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

package com.connectsdk.service.airplay;

import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public class PListParser {
    private static final String ns = null;
    public static final String TAG_DATA = "data";
    public static final String TAG_INTEGER = "integer";
    public static final String TAG_STRING = "string";
    public static final String TAG_DATE = "date";
    public static final String TAG_REAL = "real";
    public static final String TAG_ARRAY = "array";
    public static final String TAG_DICT = "dict";
    public static final String TAG_TRUE = "true";
    public static final String TAG_FALSE = "false";
    public static final String TAG_KEY = "key";
    public static final String TAG_PLIST = "plist";

    public JSONObject parse(String text) throws XmlPullParserException, IOException,
            JSONException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        Reader stream = new StringReader(text);
        parser.setInput(stream);
        parser.nextTag();
        return readPlist(parser);
    }

    public JSONObject parse(InputStream in) throws XmlPullParserException, IOException,
            JSONException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readPlist(parser);
        } finally {
            in.close();
        }
    }

    private JSONObject readPlist(XmlPullParser parser) throws XmlPullParserException, IOException,
            JSONException {
        JSONObject plist = null;

        parser.require(XmlPullParser.START_TAG, ns, TAG_PLIST);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals(TAG_DICT)) {
                plist = readDict(parser);
            }
        }  

        return plist;
    }

    public JSONObject readDict(XmlPullParser parser) throws IOException, XmlPullParserException,
            JSONException {
        JSONObject plist = new JSONObject();

        parser.require(XmlPullParser.START_TAG, ns, TAG_DICT);

        String key = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals(TAG_KEY)) {
                key = readKey(parser);
            }
            else if (key != null) {
                if (name.equals(TAG_DATA)) {
                    plist.put(key, readData(parser));
                }
                else if (name.equals(TAG_INTEGER)) {
                    plist.put(key, readInteger(parser));
                }
                else if (name.equals(TAG_STRING)) {
                    plist.put(key, readString(parser));
                }
                else if (name.equals(TAG_DATE)) {
                    plist.put(key, readDate(parser));
                }
                else if (name.equals(TAG_REAL)) {
                    plist.put(key, readReal(parser));
                }
                else if (name.equals(TAG_ARRAY)) {
                    plist.put(key, readArray(parser));
                }
                else if (name.equals(TAG_DICT)) {
                    plist.put(key, readDict(parser));
                }
                else if (name.equals(TAG_TRUE) || name.equals(TAG_FALSE)) {
                    plist.put(key, Boolean.valueOf(name));
                    skip(parser);
                }

                key = null;
            }
        }

        return plist;
    }

    private JSONArray readArray(XmlPullParser parser) throws IOException, XmlPullParserException,
            JSONException {
        JSONArray plist = new JSONArray();
        parser.require(XmlPullParser.START_TAG, ns, TAG_ARRAY);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_DICT)) {
                plist.put(readDict(parser));
            }
        }
        return plist;
    }

    private String readKey(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_KEY);
        String key = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_KEY);
        return key;
    }

    private String readData(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_DATA);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_DATA);
        return value;
    }

    private int readInteger(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_INTEGER);
        int value = Integer.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, TAG_INTEGER);
        return value;
    }

    private double readReal(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_REAL);
        double value = Double.valueOf(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, TAG_REAL);
        return value;
    }

    private String readString(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_STRING);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_STRING);
        return value;
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_DATE);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_DATE);
        return value;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }
}
