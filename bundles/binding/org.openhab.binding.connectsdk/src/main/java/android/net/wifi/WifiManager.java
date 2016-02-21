package android.net.wifi;
public class WifiManager
{
public class WifiLock
{
WifiLock() { throw new RuntimeException("Stub!"); }
public  void acquire() { throw new RuntimeException("Stub!"); }
public  void release() { throw new RuntimeException("Stub!"); }
public  void setReferenceCounted(boolean refCounted) { throw new RuntimeException("Stub!"); }
public  boolean isHeld() { throw new RuntimeException("Stub!"); }
public  void setWorkSource(android.os.WorkSource ws) { throw new RuntimeException("Stub!"); }
public  java.lang.String toString() { throw new RuntimeException("Stub!"); }
protected  void finalize() throws java.lang.Throwable { throw new RuntimeException("Stub!"); }
}
public class MulticastLock
{
MulticastLock() { /*throw new RuntimeException("Stub!");*/ }
public  void acquire() { /*throw new RuntimeException("Stub!");*/}
public  void release() { /*throw new RuntimeException("Stub!");*/ }
public  void setReferenceCounted(boolean refCounted) { /*throw new RuntimeException("Stub!");*/ }
public  boolean isHeld() { return true; /*throw new RuntimeException("Stub!");*/ }
public  java.lang.String toString() { throw new RuntimeException("Stub!"); }
protected  void finalize() throws java.lang.Throwable { throw new RuntimeException("Stub!"); }
}
public WifiManager() { /*throw new RuntimeException("Stub!");*/ }
public  java.util.List<android.net.wifi.WifiConfiguration> getConfiguredNetworks() { throw new RuntimeException("Stub!"); }
public  int addNetwork(android.net.wifi.WifiConfiguration config) { throw new RuntimeException("Stub!"); }
public  int updateNetwork(android.net.wifi.WifiConfiguration config) { throw new RuntimeException("Stub!"); }
public  boolean removeNetwork(int netId) { throw new RuntimeException("Stub!"); }
public  boolean enableNetwork(int netId, boolean disableOthers) { throw new RuntimeException("Stub!"); }
public  boolean disableNetwork(int netId) { throw new RuntimeException("Stub!"); }
public  boolean disconnect() { throw new RuntimeException("Stub!"); }
public  boolean reconnect() { throw new RuntimeException("Stub!"); }
public  boolean reassociate() { throw new RuntimeException("Stub!"); }
public  boolean pingSupplicant() { throw new RuntimeException("Stub!"); }
public  boolean startScan() { throw new RuntimeException("Stub!"); }
public  android.net.wifi.WifiInfo getConnectionInfo() { return new WifiInfo(); /*throw new RuntimeException("Stub!"); */}
public  java.util.List<android.net.wifi.ScanResult> getScanResults() { throw new RuntimeException("Stub!"); }
public  boolean saveConfiguration() { throw new RuntimeException("Stub!"); }
public  android.net.DhcpInfo getDhcpInfo() { throw new RuntimeException("Stub!"); }
public  boolean setWifiEnabled(boolean enabled) { throw new RuntimeException("Stub!"); }
public  int getWifiState() { throw new RuntimeException("Stub!"); }
public  boolean isWifiEnabled() { throw new RuntimeException("Stub!"); }
public static  int calculateSignalLevel(int rssi, int numLevels) { throw new RuntimeException("Stub!"); }
public static  int compareSignalLevel(int rssiA, int rssiB) { throw new RuntimeException("Stub!"); }
public  android.net.wifi.WifiManager.WifiLock createWifiLock(int lockType, java.lang.String tag) { throw new RuntimeException("Stub!"); }
public  android.net.wifi.WifiManager.WifiLock createWifiLock(java.lang.String tag) { throw new RuntimeException("Stub!"); }
public  android.net.wifi.WifiManager.MulticastLock createMulticastLock(java.lang.String tag) { /*throw new RuntimeException("Stub!");*/ return new MulticastLock(); }
public static final int ERROR_AUTHENTICATING = 1;
public static final java.lang.String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
public static final java.lang.String EXTRA_WIFI_STATE = "wifi_state";
public static final java.lang.String EXTRA_PREVIOUS_WIFI_STATE = "previous_wifi_state";
public static final int WIFI_STATE_DISABLING = 0;
public static final int WIFI_STATE_DISABLED = 1;
public static final int WIFI_STATE_ENABLING = 2;
public static final int WIFI_STATE_ENABLED = 3;
public static final int WIFI_STATE_UNKNOWN = 4;
public static final java.lang.String SUPPLICANT_CONNECTION_CHANGE_ACTION = "android.net.wifi.supplicant.CONNECTION_CHANGE";
public static final java.lang.String EXTRA_SUPPLICANT_CONNECTED = "connected";
public static final java.lang.String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
public static final java.lang.String EXTRA_NETWORK_INFO = "networkInfo";
public static final java.lang.String EXTRA_BSSID = "bssid";
public static final java.lang.String EXTRA_WIFI_INFO = "wifiInfo";
public static final java.lang.String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
public static final java.lang.String EXTRA_NEW_STATE = "newState";
public static final java.lang.String EXTRA_SUPPLICANT_ERROR = "supplicantError";
public static final java.lang.String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
public static final java.lang.String RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED";
public static final java.lang.String EXTRA_NEW_RSSI = "newRssi";
public static final java.lang.String NETWORK_IDS_CHANGED_ACTION = "android.net.wifi.NETWORK_IDS_CHANGED";
public static final java.lang.String ACTION_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
public static final int WIFI_MODE_FULL = 1;
public static final int WIFI_MODE_SCAN_ONLY = 2;
public static final int WIFI_MODE_FULL_HIGH_PERF = 3;
}
