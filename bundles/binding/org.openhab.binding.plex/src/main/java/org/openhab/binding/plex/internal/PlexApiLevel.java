/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import static org.apache.commons.lang.StringUtils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This enum keeps track of changes within the Plex API. These are not "official" API levels used by Plex.
 * They are however necessary for supporting different versions of the Plex Media Server within this binding.
 *
 * @author Jeroen Idserda
 * @since 1.9.0
 */
public enum PlexApiLevel {

    v1("0.0"),
    v2("1.3.2.3112");

    private String fromVersion;

    private PlexApiLevel(String fromVersion) {
        this.fromVersion = fromVersion;
    }

    /**
     * Gets the latest (most recent) api level
     *
     * @return Latest api level
     */
    public static PlexApiLevel getLatest() {
        return values()[values().length - 1];
    }

    /**
     * Get the API level for a certain version of the Plex Media Server.
     *
     * @param version Version of the Plex Media Server
     * @return The appropriate API level for this version
     */
    public static PlexApiLevel getApiLevel(String version) {
        if (isNotBlank(version)) {
            String[] versionWithBuildnumber = version.split("-");
            String versionOnly = versionWithBuildnumber[0];

            if (isNotBlank(versionOnly) && isNumeric(versionOnly.replaceAll("\\.", ""))) {
                List<PlexApiLevel> levels = Arrays.asList(values());
                Collections.reverse(levels);

                for (PlexApiLevel level : levels) {
                    if (level.isEqualOrBeforeVersion(versionOnly)) {
                        return level;
                    }
                }
            }
        }

        // Assume latest version for unprocessable version numbers
        return getLatest();
    }

    private boolean isEqualOrBeforeVersion(String version) {
        String[] v1 = fromVersion.split("\\.");
        String[] v2 = version.split("\\.");

        int length = Math.min(v1.length, v2.length);
        for (int i = 0; i < length; i++) {
            int result = new Integer(v1[i]).compareTo(Integer.parseInt(v2[i]));
            if (result != 0) {
                return result <= 0;
            }
        }

        return Integer.compare(v1.length, v2.length) <= 0;
    }

}
