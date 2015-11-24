package android.content.pm;
public class ApplicationInfo
  extends android.content.pm.PackageItemInfo
  implements android.os.Parcelable
{
public static class DisplayNameComparator
  implements java.util.Comparator<android.content.pm.ApplicationInfo>
{
public  DisplayNameComparator(android.content.pm.PackageManager pm) { throw new RuntimeException("Stub!"); }
public final  int compare(android.content.pm.ApplicationInfo aa, android.content.pm.ApplicationInfo ab) { throw new RuntimeException("Stub!"); }
}
public  ApplicationInfo() { /*throw new RuntimeException("Stub!");*/ }
public  ApplicationInfo(android.content.pm.ApplicationInfo orig) { throw new RuntimeException("Stub!"); }
public  void dump(android.util.Printer pw, java.lang.String prefix) { throw new RuntimeException("Stub!"); }
public  java.lang.String toString() { throw new RuntimeException("Stub!"); }
public  int describeContents() { throw new RuntimeException("Stub!"); }
public  void writeToParcel(android.os.Parcel dest, int parcelableFlags) { throw new RuntimeException("Stub!"); }
public  java.lang.CharSequence loadDescription(android.content.pm.PackageManager pm) { throw new RuntimeException("Stub!"); }
public java.lang.String taskAffinity;
public java.lang.String permission;
public java.lang.String processName;
public java.lang.String className;
public int descriptionRes;
public int theme;
public java.lang.String manageSpaceActivityName;
public java.lang.String backupAgentName;
public int uiOptions;
public static final int FLAG_SYSTEM = 1;
public static final int FLAG_DEBUGGABLE = 2;
public static final int FLAG_HAS_CODE = 4;
public static final int FLAG_PERSISTENT = 8;
public static final int FLAG_FACTORY_TEST = 16;
public static final int FLAG_ALLOW_TASK_REPARENTING = 32;
public static final int FLAG_ALLOW_CLEAR_USER_DATA = 64;
public static final int FLAG_UPDATED_SYSTEM_APP = 128;
public static final int FLAG_TEST_ONLY = 256;
public static final int FLAG_SUPPORTS_SMALL_SCREENS = 512;
public static final int FLAG_SUPPORTS_NORMAL_SCREENS = 1024;
public static final int FLAG_SUPPORTS_LARGE_SCREENS = 2048;
public static final int FLAG_RESIZEABLE_FOR_SCREENS = 4096;
public static final int FLAG_SUPPORTS_SCREEN_DENSITIES = 8192;
public static final int FLAG_VM_SAFE_MODE = 16384;
public static final int FLAG_ALLOW_BACKUP = 32768;
public static final int FLAG_KILL_AFTER_RESTORE = 65536;
public static final int FLAG_RESTORE_ANY_VERSION = 131072;
public static final int FLAG_EXTERNAL_STORAGE = 262144;
public static final int FLAG_SUPPORTS_XLARGE_SCREENS = 524288;
public static final int FLAG_LARGE_HEAP = 1048576;
public static final int FLAG_STOPPED = 2097152;
public int flags;
public int requiresSmallestWidthDp;
public int compatibleWidthLimitDp;
public int largestWidthLimitDp;
public java.lang.String sourceDir;
public java.lang.String publicSourceDir;
public java.lang.String[] sharedLibraryFiles = null;
public java.lang.String dataDir;
public java.lang.String nativeLibraryDir;
public int uid;
public int targetSdkVersion;
public boolean enabled;
public static final android.os.Parcelable.Creator<android.content.pm.ApplicationInfo> CREATOR;
static { CREATOR = null; }
}

