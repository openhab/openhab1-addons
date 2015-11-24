package com.connectsdk.service.upnp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class DLNANotifyParser {
    private static final String ns = null;

    public JSONArray parse(InputStream in) throws XmlPullParserException, IOException, JSONException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readPropertySet(parser);
        } finally {
            in.close();
        }
    }

    private JSONArray readPropertySet(XmlPullParser parser) throws IOException, XmlPullParserException, JSONException {
        JSONArray propertyset = new JSONArray();

        parser.require(XmlPullParser.START_TAG, ns, "e:propertyset");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("e:property")) {
                propertyset.put(readProperty(parser));
            }
            else {
                skip(parser);
            }
        }
        return propertyset;
    }

    private JSONObject readProperty(XmlPullParser parser) throws IOException, XmlPullParserException, JSONException {
        JSONObject property = new JSONObject();

        parser.require(XmlPullParser.START_TAG, ns, "e:property");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("LastChange")) {
                String eventStr = readText(parser);

                JSONObject event;
                InputStream stream = null;

                try {
                    stream = new ByteArrayInputStream(eventStr.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }

                DLNAEventParser eventParser = new DLNAEventParser();

                event = eventParser.parse(stream);
                property.put("LastChange", event);
            }
            else {
                property = readPropertyData(name, parser);
            }
        }
        return property;
    }

    private JSONObject readPropertyData(String target, XmlPullParser parser) throws IOException, XmlPullParserException, JSONException {
        JSONObject data = new JSONObject();
        String value;

        parser.require(XmlPullParser.START_TAG, ns, target);

        value = readText(parser);
        data.put(target, value);

        parser.require(XmlPullParser.END_TAG, ns, target);

        return data;
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
