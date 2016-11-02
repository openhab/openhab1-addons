/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeData {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeData.class);
    private static String filename = CalDavLoaderImpl.CACHE_PATH + "/runtime.properties";
    private Properties p;

    public static final String PROP_REFRESH_TOKEN = ".refresh_token";
    public static final String PROP_AUTHENTICATION_BEARER = ".authentication_bearer";
    public static final String PROP_CODE = ".code";

    private static RuntimeData instance;

    public static RuntimeData getInstance() {
        if (instance == null) {
            instance = new RuntimeData();
        }
        return instance;
    }

    public RuntimeData() {
        p = new Properties();
        try {
            File file = new File(filename);
            if (file.exists()) {
                p.load(new FileInputStream(file));
            }
        } catch (IOException e) {
            logger.error("error loading runtime data", e);
        }
    }

    public void store() {
        File file = new File(filename);
        try {
            p.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            logger.error(String.format("error storing runtime data to file '%s': %s", file.getAbsolutePath(),
                    e.getMessage()), e);
        }
    }

    public void setRefreshToken(String calendarId, String value) {
        this.p.put(calendarId + PROP_REFRESH_TOKEN, value);
        this.store();
    }

    public String getRefreshToken(String calendarId) {
        Object val = this.p.get(calendarId + PROP_REFRESH_TOKEN);
        if (val == null) {
            return null;
        }
        return val.toString();
    }

    public void setAuthenticationBearer(String calendarId, String value) {
        this.p.put(calendarId + PROP_AUTHENTICATION_BEARER, value);
        this.store();
    }

    public String getAuthenticationBearer(String calendarId) {
        Object val = this.p.get(calendarId + PROP_AUTHENTICATION_BEARER);
        if (val == null) {
            return null;
        }
        return val.toString();
    }
}
