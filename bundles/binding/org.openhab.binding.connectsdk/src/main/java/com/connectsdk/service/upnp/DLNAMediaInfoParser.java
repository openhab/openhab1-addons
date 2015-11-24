package com.connectsdk.service.upnp;

import android.util.Xml;

import com.connectsdk.core.ImageInfo;
import com.connectsdk.core.MediaInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

public class DLNAMediaInfoParser {

    private static final String APOS = "&amp;apos;";
    private static final String LT = "&lt;";
    private static final String GT = "&gt;";
    private static final String TITLE = "dc:title";
    private static final String CREATOR = "dc:creator";
    private static final String ARTIST = "r:albumArtist";
    private static final String THUMBNAIL = "upnp:albumArtURI";
    private static final String ALBUM = "upnp:album";
    private static final String GENRE = "upnp:genre";
    private static final String RADIOTITLE = "r:streamContent";

    private static String getData(String str, String data) {
        if (str.contains(toEndTag(data))) {
            int startInd = str.indexOf(toStartTag(data))
                    + toStartTag(data).length();
            int endInd = str.indexOf(toEndTag(data));
            return (toString(str.substring(startInd, endInd)));
        }

        if (str.contains(LT)) return "";

        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(str));

            int eventType = parser.nextTag();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals(data)) {
                        eventType = parser.next();
                        if (eventType == XmlPullParser.TEXT)
                            return parser.getText();
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static MediaInfo getMediaInfo(String str) {

        String url = DLNAMediaInfoParser.getURL(str);
        String title = DLNAMediaInfoParser.getTitle(str);
        String mimeType = DLNAMediaInfoParser.getMimeType(str);
        String description =  DLNAMediaInfoParser.getArtist(str) + "\n" + 
                DLNAMediaInfoParser.getAlbum(str);
        String iconUrl = DLNAMediaInfoParser.getThumbnail(str);

        ArrayList<ImageInfo> list = new ArrayList<ImageInfo>();
        list.add(new ImageInfo(iconUrl));
        return new MediaInfo(url, mimeType, title, description, list);
    }

    public static MediaInfo getMediaInfo(String str, String baseUrl) {
        String url = DLNAMediaInfoParser.getURL(str);
        String title = DLNAMediaInfoParser.getTitle(str);
        String mimeType = DLNAMediaInfoParser.getMimeType(str);
        String description =  DLNAMediaInfoParser.getArtist(str) + "\n" +
                DLNAMediaInfoParser.getAlbum(str);
        String iconUrl = DLNAMediaInfoParser.getThumbnail(str);

        try {
            new URL(iconUrl).openConnection().connect();
        } catch (Exception e) {
            iconUrl = baseUrl + iconUrl;
        }

        ArrayList<ImageInfo> list = new ArrayList<ImageInfo>();
        list.add(new ImageInfo(iconUrl));
        return new MediaInfo(url, mimeType, title, description, list);
    }

    public static String getTitle(String str) {
        if (!getData(str, RADIOTITLE).equals("")) return getData(str, RADIOTITLE);
        return getData(str, TITLE);
    }

    public static String getArtist(String str) {
        return getData(str, CREATOR);
    }

    public static String getAlbum(String str) {
        return getData(str, ALBUM);
    }

    public static String getGenre(String str) {
        return getData(str, GENRE);
    }

    @SuppressWarnings("deprecation")
    public static String getThumbnail(String str) {
        String res = getData(str, THUMBNAIL);
        res = java.net.URLDecoder.decode(res);
        return res;
    }

    public static String getMimeType(String str) {
        if (str.contains("protocolInfo")) {
            int startInd = str.indexOf("*:") + 2;
            int endInd = str.substring(startInd).indexOf(":") + startInd;
            return str.substring(startInd, endInd);
        }
        return "";
    }

    @SuppressWarnings("deprecation")
    public static String getURL(String str) {
        if (str.contains(LT)){
            if (str.contains(toEndTag("res"))) {
                int startInd = str.substring(str.indexOf(LT + "res")).indexOf(GT)
                        + str.indexOf(LT + "res") + GT.length();
                int endInd = str.indexOf(toEndTag("res"));
                return java.net.URLDecoder.decode(str.substring(startInd, endInd));
            }
            return "";
        }
        else return getData(str, "res");
    }

    private static String toStartTag(String str) {
        return (LT + str + GT);
    }

    private static String toEndTag(String str) {
        return toStartTag("/" + str);
    }

    private static String toString(String text) {
        StringBuilder sb = new StringBuilder();
        if (text.contains(APOS)) {
            sb.append(text.substring(0, text.indexOf(APOS)));
            sb.append("'");
            sb.append(text.substring(text.indexOf(APOS) + APOS.length()));
        } else
            return text;

        return sb.toString();
    }

}
