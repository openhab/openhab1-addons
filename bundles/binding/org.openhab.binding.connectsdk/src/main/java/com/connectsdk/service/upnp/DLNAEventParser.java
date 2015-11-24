package com.connectsdk.service.upnp;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class DLNAEventParser {
    private static final String ns = null;

    public JSONObject parse(InputStream in) throws XmlPullParserException, IOException, JSONException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readEvent(parser);
        } finally {
            in.close();
        }
    }

    private JSONObject readEvent(XmlPullParser parser) throws IOException, XmlPullParserException, JSONException {
        JSONObject event = new JSONObject();

        JSONArray instanceIDs = new JSONArray();
        JSONArray queueIDs = new JSONArray();

        parser.require(XmlPullParser.START_TAG, ns, "Event");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("InstanceID")) {
                instanceIDs.put(readInstanceID(parser));
            }
            else if (name.equals("QueueID")) {
                queueIDs.put(readQueueID(parser));
            }
            else {
                skip(parser);
            }
        }

        if (instanceIDs.length() > 0)
            event.put("InstanceID", instanceIDs);
        if (queueIDs.length() > 0)
            event.put("QueueID", queueIDs);

        return event;
    }

    private JSONArray readInstanceID(XmlPullParser parser) throws IOException, XmlPullParserException, JSONException {
        JSONArray instanceIDs = new JSONArray();
        JSONObject data = new JSONObject();

        parser.require(XmlPullParser.START_TAG, ns, "InstanceID");
        data.put("value", parser.getAttributeValue(null, "val"));
        instanceIDs.put(data);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            instanceIDs.put(readEntry(name, parser));
        }

        return instanceIDs;
    }

    private JSONArray readQueueID(XmlPullParser parser) throws IOException, XmlPullParserException, JSONException {
        JSONArray queueIDs = new JSONArray();
        JSONObject data = new JSONObject();

        parser.require(XmlPullParser.START_TAG, ns, "QueueID");
        data.put("value", parser.getAttributeValue(null, "val"));
        queueIDs.put(data);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            queueIDs.put(readEntry(name, parser));
        }

        return queueIDs;
    }

    private JSONObject readEntry(String target, XmlPullParser parser) throws IOException, XmlPullParserException, JSONException {
        parser.require(XmlPullParser.START_TAG, ns, target);
        String value = parser.getAttributeValue(null, "val");
        String channel = parser.getAttributeValue(null, "channel");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, target);

        JSONObject data = new JSONObject();
        data.put(target, value);

        if (channel!=null)
            data.put("channel", channel);

        return data;
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
