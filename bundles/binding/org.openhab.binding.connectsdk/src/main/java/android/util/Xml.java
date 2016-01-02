package android.util;
public class Xml
{
public static enum Encoding
{
ISO_8859_1(),
US_ASCII(),
UTF_16(),
UTF_8();
}
Xml() { throw new RuntimeException("Stub!"); }
public static  void parse(java.lang.String xml, org.xml.sax.ContentHandler contentHandler) throws org.xml.sax.SAXException { throw new RuntimeException("Stub!"); }
public static  void parse(java.io.Reader in, org.xml.sax.ContentHandler contentHandler) throws java.io.IOException, org.xml.sax.SAXException { throw new RuntimeException("Stub!"); }
public static  void parse(java.io.InputStream in, android.util.Xml.Encoding encoding, org.xml.sax.ContentHandler contentHandler) throws java.io.IOException, org.xml.sax.SAXException { throw new RuntimeException("Stub!"); }
public static  org.xmlpull.v1.XmlPullParser newPullParser() { throw new RuntimeException("Stub!"); }
public static  org.xmlpull.v1.XmlSerializer newSerializer() { throw new RuntimeException("Stub!"); }
public static  android.util.Xml.Encoding findEncodingByName(java.lang.String encodingName) throws java.io.UnsupportedEncodingException { throw new RuntimeException("Stub!"); }
//public static  android.util.AttributeSet asAttributeSet(org.xmlpull.v1.XmlPullParser parser) { throw new RuntimeException("Stub!"); }
public static java.lang.String FEATURE_RELAXED;
}
