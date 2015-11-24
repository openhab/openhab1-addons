package android.net;
public class NetworkInfo
  implements android.os.Parcelable
{
public static enum State
{
CONNECTED(),
CONNECTING(),
DISCONNECTED(),
DISCONNECTING(),
SUSPENDED(),
UNKNOWN();
}
public static enum DetailedState
{
AUTHENTICATING(),
BLOCKED(),
CONNECTED(),
CONNECTING(),
DISCONNECTED(),
DISCONNECTING(),
FAILED(),
IDLE(),
OBTAINING_IPADDR(),
SCANNING(),
SUSPENDED(),
VERIFYING_POOR_LINK();
}
NetworkInfo() { /* throw new RuntimeException("Stub!"); */}
public  int getType() { throw new RuntimeException("Stub!"); }
public  int getSubtype() { throw new RuntimeException("Stub!"); }
public  java.lang.String getTypeName() { throw new RuntimeException("Stub!"); }
public  java.lang.String getSubtypeName() { throw new RuntimeException("Stub!"); }
public  boolean isConnectedOrConnecting() { throw new RuntimeException("Stub!"); }
public  boolean isConnected() { return true; /*throw new RuntimeException("Stub!");*/ }
public  boolean isAvailable() { throw new RuntimeException("Stub!"); }
public  boolean isFailover() { throw new RuntimeException("Stub!"); }
public  boolean isRoaming() { throw new RuntimeException("Stub!"); }
public  android.net.NetworkInfo.State getState() { throw new RuntimeException("Stub!"); }
public  android.net.NetworkInfo.DetailedState getDetailedState() { throw new RuntimeException("Stub!"); }
public  java.lang.String getReason() { throw new RuntimeException("Stub!"); }
public  java.lang.String getExtraInfo() { throw new RuntimeException("Stub!"); }
public  java.lang.String toString() { throw new RuntimeException("Stub!"); }
public  int describeContents() { throw new RuntimeException("Stub!"); }
public  void writeToParcel(android.os.Parcel dest, int flags) { throw new RuntimeException("Stub!"); }
}
