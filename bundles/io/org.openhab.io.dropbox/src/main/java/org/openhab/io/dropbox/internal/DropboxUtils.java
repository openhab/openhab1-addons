/**
 * Copyright (c) 2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.dropbox.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Carman - split from original DropboxSynchronizer
 * @since 1.11.0
 */
public class DropboxUtils {
    private static Logger logger = LoggerFactory.getLogger(DropboxUtils.class);

    public static String readFile(File file) {
        String content = null;
        if (file.exists()) {
            try {
                List<String> lines = FileUtils.readLines(file);
                if (lines.size() > 0) {
                    content = lines.get(0);
                }
            } catch (IOException ioe) {
                logger.debug("Handling of cursor file threw an Exception", ioe);
            }
        }
        return content;
    }

    public static void writeLocalFile(File file, String content) {
        try {
            FileUtils.writeStringToFile(file, content);
            logger.debug("Created file '{}' with content '{}'", file.getAbsolutePath(), content);
        } catch (IOException e) {
            logger.error("Couldn't write to file '{}'.", file.getPath(), e);
        }
    }

    public static String getUserDbxDataFolder() {
        String progArg = System.getProperty("smarthome.userdata");
        if (progArg != null) {
            return progArg + File.separator + "dropbox";
        }

        return ".";
    }

    public static String getConfigDirFolder() {
        String smartHomeProgArg = System.getProperty("smarthome.configdir");
        String openHABProgArg = System.getProperty("openhab.configdir");
        if (smartHomeProgArg != null) {
            return smartHomeProgArg;
        }

        if (openHABProgArg != null) {
            return openHABProgArg;
        }

        return ".";
    }

    public static boolean isWindows() {
        return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
    }
}
