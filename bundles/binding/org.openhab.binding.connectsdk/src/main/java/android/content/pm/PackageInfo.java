package android.content.pm;
public class PackageInfo
  implements android.os.Parcelable
{
public  PackageInfo() { /*throw new RuntimeException("Stub!"); */}
public  java.lang.String toString() { throw new RuntimeException("Stub!"); }
public  int describeContents() { throw new RuntimeException("Stub!"); }
public  void writeToParcel(android.os.Parcel dest, int parcelableFlags) { throw new RuntimeException("Stub!"); }
public java.lang.String packageName;
public int versionCode;
public java.lang.String versionName;
public java.lang.String sharedUserId;
public int sharedUserLabel;
public android.content.pm.ApplicationInfo applicationInfo;
public long firstInstallTime;
public long lastUpdateTime;
public int[] gids = null;
public android.content.pm.ActivityInfo[] activities = null;
public android.content.pm.ActivityInfo[] receivers = null;
public android.content.pm.ServiceInfo[] services = null;
public android.content.pm.ProviderInfo[] providers = null;
public android.content.pm.InstrumentationInfo[] instrumentation = null;
public android.content.pm.PermissionInfo[] permissions = null;
public java.lang.String[] requestedPermissions = null;
public int[] requestedPermissionsFlags = null;
public static final int REQUESTED_PERMISSION_REQUIRED = 1;
public static final int REQUESTED_PERMISSION_GRANTED = 2;
public android.content.pm.Signature[] signatures = null;
public android.content.pm.ConfigurationInfo[] configPreferences = null;
public android.content.pm.FeatureInfo[] reqFeatures = null;
public static final android.os.Parcelable.Creator<android.content.pm.PackageInfo> CREATOR;
static { CREATOR = null; }
}
