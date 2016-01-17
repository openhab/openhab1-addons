/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;


/**
 * Context implementation that provides connect sdk information about its surrounding environment.
 * @author Sebastian Prehn
 * @since 1.8.0
 */
class ContextImpl extends Context {
	private final String CONFIG_PROPERTIES_BASE = "etc" + File.separator + "connect_sdk";
	private static final Logger logger = LoggerFactory.getLogger(ContextImpl.class);
    @Override
    public PackageManager getPackageManager() {
        
        return new PackageManager() {

            @Override
            public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
                PackageInfo packageInfo = new PackageInfo();
                packageInfo.applicationInfo = new ApplicationInfo();
                packageInfo.applicationInfo.dataDir =  (new File(CONFIG_PROPERTIES_BASE)).getAbsolutePath();
                logger.debug("Folder path for connect sdk devices: {}", packageInfo.applicationInfo.dataDir);
                return packageInfo;
            }

            @Override
            public String[] currentToCanonicalPackageNames(String[] names) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public String[] canonicalToCurrentPackageNames(String[] names) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public int[] getPackageGids(String packageName) throws PackageManager.NameNotFoundException {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
                throw new NameNotFoundException();
            }

            @Override
            public List<PackageInfo> getInstalledPackages(int flags) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public int checkPermission(String permName, String pkgName) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void removePermission(String name) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public int checkSignatures(String pkg1, String pkg2) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public int checkSignatures(int uid1, int uid2) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public String[] getPackagesForUid(int uid) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public String getNameForUid(int uid) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public List<ApplicationInfo> getInstalledApplications(int flags) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public String[] getSystemSharedLibraryNames() {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public boolean hasSystemFeature(String name) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public Drawable getApplicationIcon(String packageName) throws PackageManager.NameNotFoundException {
                throw new UnsupportedOperationException("Not supported yet."); // here we could provide the default opnehab logo as a BitmapDrawable
            }

            @Override
            public CharSequence getText(String packageName, int resid, ApplicationInfo appInfo) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public CharSequence getApplicationLabel(ApplicationInfo info) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void verifyPendingInstall(int id, int verificationCode) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void setInstallerPackageName(String targetPackage, String installerPackageName) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public String getInstallerPackageName(String packageName) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void addPackageToPreferred(String packageName) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void removePackageFromPreferred(String packageName) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public List<PackageInfo> getPreferredPackages(int flags) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void clearPackagePreferredActivities(String packageName) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void setApplicationEnabledSetting(String packageName, int newState, int flags) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public int getApplicationEnabledSetting(String packageName) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public boolean isSafeMode() {
                throw new UnsupportedOperationException("Not supported yet."); 
            }
            
        };
    }

    @Override
    public Context getApplicationContext() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setTheme(int i) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getPackageName() {
        return "org.openhab";
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getPackageResourcePath() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getPackageCodePath() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public FileInputStream openFileInput(String string) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public FileOutputStream openFileOutput(String string, int i) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean deleteFile(String string) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getFileStreamPath(String string) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getFilesDir() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getExternalFilesDir(String string) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getObbDir() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getCacheDir() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getExternalCacheDir() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String[] fileList() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getDir(String string, int i) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean deleteDatabase(String string) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getDatabasePath(String string) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String[] databaseList() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setWallpaper(InputStream in) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void clearWallpaper() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver br, IntentFilter i) {
        return null; //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver br) {
    	// NOP
    }

    @Override
    public Object getSystemService(String string) {
        if(Context.WIFI_SERVICE.equals(string)) {
            return new WifiManager();
        }
        if(Context.CONNECTIVITY_SERVICE.equals(string)) {
        	return new ConnectivityManager();
        }
//        if(Context.WINDOW_SERVICE.equals(string)) {
//            return new WindowManager() {
//
//                @Override
//                public Display getDefaultDisplay() {
//                   return new Display(); 
//                }
//
//                @Override
//                public void removeViewImmediate(View view) {
//                    throw new UnsupportedOperationException("Not supported yet."); 
//                }
//
//                @Override
//                public void addView(View view, ViewGroup.LayoutParams params) {
//                    throw new UnsupportedOperationException("Not supported yet."); 
//                }
//
//                @Override
//                public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
//                    throw new UnsupportedOperationException("Not supported yet."); 
//                }
//
//                @Override
//                public void removeView(View view) {
//                    throw new UnsupportedOperationException("Not supported yet."); 
//                }
//            };
//        }
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int checkPermission(String string, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int checkCallingPermission(String string) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int checkCallingOrSelfPermission(String string) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void enforcePermission(String string, int i, int i1, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void enforceCallingPermission(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void enforceCallingOrSelfPermission(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Context createPackageContext(String string, int i) throws PackageManager.NameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
