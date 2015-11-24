package android.os;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Environment
{
    final static File tempDir;
    static {
        File dir;
        try {
            dir = Files.createTempDirectory("connectsdk").toFile();
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
            dir = null;
        }
        tempDir = dir;
    }
public  Environment() {  }
public static  java.io.File getRootDirectory() { throw new RuntimeException("Stub!"); }
public static  java.io.File getDataDirectory() { throw new RuntimeException("Stub!"); }
public static  java.io.File getExternalStorageDirectory() { return tempDir; }
public static  java.io.File getExternalStoragePublicDirectory(java.lang.String type) { throw new RuntimeException("Stub!"); }
public static  java.io.File getDownloadCacheDirectory() { throw new RuntimeException("Stub!"); }
public static  java.lang.String getExternalStorageState() { return Environment.MEDIA_MOUNTED; /*throw new RuntimeException("Stub!");*/ }
public static  boolean isExternalStorageRemovable() { throw new RuntimeException("Stub!"); }
public static  boolean isExternalStorageEmulated() { throw new RuntimeException("Stub!"); }
public static java.lang.String DIRECTORY_MUSIC;
public static java.lang.String DIRECTORY_PODCASTS;
public static java.lang.String DIRECTORY_RINGTONES;
public static java.lang.String DIRECTORY_ALARMS;
public static java.lang.String DIRECTORY_NOTIFICATIONS;
public static java.lang.String DIRECTORY_PICTURES;
public static java.lang.String DIRECTORY_MOVIES;
public static java.lang.String DIRECTORY_DOWNLOADS;
public static java.lang.String DIRECTORY_DCIM;
public static final java.lang.String MEDIA_REMOVED = "removed";
public static final java.lang.String MEDIA_UNMOUNTED = "unmounted";
public static final java.lang.String MEDIA_CHECKING = "checking";
public static final java.lang.String MEDIA_NOFS = "nofs";
public static final java.lang.String MEDIA_MOUNTED = "mounted";
public static final java.lang.String MEDIA_MOUNTED_READ_ONLY = "mounted_ro";
public static final java.lang.String MEDIA_SHARED = "shared";
public static final java.lang.String MEDIA_BAD_REMOVAL = "bad_removal";
public static final java.lang.String MEDIA_UNMOUNTABLE = "unmountable";
}
