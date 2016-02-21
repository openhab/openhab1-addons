package android.content;
public class IntentFilter
  implements android.os.Parcelable
{
public static class MalformedMimeTypeException
  extends android.util.AndroidException
{
public  MalformedMimeTypeException() { throw new RuntimeException("Stub!"); }
public  MalformedMimeTypeException(java.lang.String name) { throw new RuntimeException("Stub!"); }
}
public static final class AuthorityEntry
{
public  AuthorityEntry(java.lang.String host, java.lang.String port) { throw new RuntimeException("Stub!"); }
public  java.lang.String getHost() { throw new RuntimeException("Stub!"); }
public  int getPort() { throw new RuntimeException("Stub!"); }
public  int match(android.net.Uri data) { throw new RuntimeException("Stub!"); }
}
public  IntentFilter() { /*throw new RuntimeException("Stub!"); */}
public  IntentFilter(java.lang.String action) { throw new RuntimeException("Stub!"); }
public  IntentFilter(java.lang.String action, java.lang.String dataType) throws android.content.IntentFilter.MalformedMimeTypeException { throw new RuntimeException("Stub!"); }
public  IntentFilter(android.content.IntentFilter o) { throw new RuntimeException("Stub!"); }
public static  android.content.IntentFilter create(java.lang.String action, java.lang.String dataType) { throw new RuntimeException("Stub!"); }
public final  void setPriority(int priority) { throw new RuntimeException("Stub!"); }
public final  int getPriority() { throw new RuntimeException("Stub!"); }
public final  void addAction(java.lang.String action) { /*throw new RuntimeException("Stub!");*/ }
public final  int countActions() { throw new RuntimeException("Stub!"); }
public final  java.lang.String getAction(int index) { throw new RuntimeException("Stub!"); }
public final  boolean hasAction(java.lang.String action) { throw new RuntimeException("Stub!"); }
public final  boolean matchAction(java.lang.String action) { throw new RuntimeException("Stub!"); }
public final  java.util.Iterator<java.lang.String> actionsIterator() { throw new RuntimeException("Stub!"); }
public final  void addDataType(java.lang.String type) throws android.content.IntentFilter.MalformedMimeTypeException { throw new RuntimeException("Stub!"); }
public final  boolean hasDataType(java.lang.String type) { throw new RuntimeException("Stub!"); }
public final  int countDataTypes() { throw new RuntimeException("Stub!"); }
public final  java.lang.String getDataType(int index) { throw new RuntimeException("Stub!"); }
public final  java.util.Iterator<java.lang.String> typesIterator() { throw new RuntimeException("Stub!"); }
public final  void addDataScheme(java.lang.String scheme) { throw new RuntimeException("Stub!"); }
public final  int countDataSchemes() { throw new RuntimeException("Stub!"); }
public final  java.lang.String getDataScheme(int index) { throw new RuntimeException("Stub!"); }
public final  boolean hasDataScheme(java.lang.String scheme) { throw new RuntimeException("Stub!"); }
public final  java.util.Iterator<java.lang.String> schemesIterator() { throw new RuntimeException("Stub!"); }
public final  void addDataAuthority(java.lang.String host, java.lang.String port) { throw new RuntimeException("Stub!"); }
public final  int countDataAuthorities() { throw new RuntimeException("Stub!"); }
public final  android.content.IntentFilter.AuthorityEntry getDataAuthority(int index) { throw new RuntimeException("Stub!"); }
public final  boolean hasDataAuthority(android.net.Uri data) { throw new RuntimeException("Stub!"); }
public final  java.util.Iterator<android.content.IntentFilter.AuthorityEntry> authoritiesIterator() { throw new RuntimeException("Stub!"); }
public final  void addDataPath(java.lang.String path, int type) { throw new RuntimeException("Stub!"); }
public final  int countDataPaths() { throw new RuntimeException("Stub!"); }
public final  android.os.PatternMatcher getDataPath(int index) { throw new RuntimeException("Stub!"); }
public final  boolean hasDataPath(java.lang.String data) { throw new RuntimeException("Stub!"); }
public final  java.util.Iterator<android.os.PatternMatcher> pathsIterator() { throw new RuntimeException("Stub!"); }
public final  int matchDataAuthority(android.net.Uri data) { throw new RuntimeException("Stub!"); }
public final  int matchData(java.lang.String type, java.lang.String scheme, android.net.Uri data) { throw new RuntimeException("Stub!"); }
public final  void addCategory(java.lang.String category) { throw new RuntimeException("Stub!"); }
public final  int countCategories() { throw new RuntimeException("Stub!"); }
public final  java.lang.String getCategory(int index) { throw new RuntimeException("Stub!"); }
public final  boolean hasCategory(java.lang.String category) { throw new RuntimeException("Stub!"); }
public final  java.util.Iterator<java.lang.String> categoriesIterator() { throw new RuntimeException("Stub!"); }
public final  java.lang.String matchCategories(java.util.Set<java.lang.String> categories) { throw new RuntimeException("Stub!"); }
public final  int match(android.content.ContentResolver resolver, android.content.Intent intent, boolean resolve, java.lang.String logTag) { throw new RuntimeException("Stub!"); }
public final  int match(java.lang.String action, java.lang.String type, java.lang.String scheme, android.net.Uri data, java.util.Set<java.lang.String> categories, java.lang.String logTag) { throw new RuntimeException("Stub!"); }
public  void writeToXml(org.xmlpull.v1.XmlSerializer serializer) throws java.io.IOException { throw new RuntimeException("Stub!"); }
public  void readFromXml(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException { throw new RuntimeException("Stub!"); }
public  void dump(android.util.Printer du, java.lang.String prefix) { throw new RuntimeException("Stub!"); }
public final  int describeContents() { throw new RuntimeException("Stub!"); }
public final  void writeToParcel(android.os.Parcel dest, int flags) { throw new RuntimeException("Stub!"); }
public static final int SYSTEM_HIGH_PRIORITY = 1000;
public static final int SYSTEM_LOW_PRIORITY = -1000;
public static final int MATCH_CATEGORY_MASK = 268369920;
public static final int MATCH_ADJUSTMENT_MASK = 65535;
public static final int MATCH_ADJUSTMENT_NORMAL = 32768;
public static final int MATCH_CATEGORY_EMPTY = 1048576;
public static final int MATCH_CATEGORY_SCHEME = 2097152;
public static final int MATCH_CATEGORY_HOST = 3145728;
public static final int MATCH_CATEGORY_PORT = 4194304;
public static final int MATCH_CATEGORY_PATH = 5242880;
public static final int MATCH_CATEGORY_TYPE = 6291456;
public static final int NO_MATCH_TYPE = -1;
public static final int NO_MATCH_DATA = -2;
public static final int NO_MATCH_ACTION = -3;
public static final int NO_MATCH_CATEGORY = -4;
public static final android.os.Parcelable.Creator<android.content.IntentFilter> CREATOR;
static { CREATOR = null; }
}
