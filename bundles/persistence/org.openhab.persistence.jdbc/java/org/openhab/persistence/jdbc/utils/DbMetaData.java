/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.persistence.jdbc.utils;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.knowm.yank.Yank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author Helmut Lehmeyer
 * @since 1.9.0
 */
public class DbMetaData {

    private static final Logger logger = LoggerFactory.getLogger(DbMetaData.class);

    private int dbMajorVersion;
    private int dbMinorVersion;
    private int driverMajorVersion;
    private int driverMinorVersion;
    private String dbProductName;
    private String dbProductVersion;

    public DbMetaData() {
        HikariDataSource h = Yank.getDefaultConnectionPool();
        // HikariDataSource h = Yank.getDataSource();

        DatabaseMetaData meta;
        try {
            meta = h.getConnection().getMetaData();

            // Oracle (and some other vendors) do not support
            // some the following methods; therefore, we need
            // to use try-catch block.
            try {
                dbMajorVersion = meta.getDatabaseMajorVersion();
                logger.debug("dbMajorVersion = '{}'", dbMajorVersion);
            } catch (Exception e) {
                logger.error("Asking for 'dbMajorVersion' is a unsupported: '{}'", e.getMessage());
            }

            try {
                dbMinorVersion = meta.getDatabaseMinorVersion();
                logger.debug("dbMinorVersion = '{}'", dbMinorVersion);
            } catch (Exception e) {
                logger.error("Asking for 'dbMajorVersion' is a unsupported: '{}'", e.getMessage());
            }

            driverMajorVersion = meta.getDriverMajorVersion();
            logger.debug("driverMajorVersion = '{}'", driverMajorVersion);

            driverMinorVersion = meta.getDriverMinorVersion();
            logger.debug("driverMinorVersion = '{}'", driverMinorVersion);

            dbProductName = meta.getDatabaseProductName();
            logger.debug("dbProductName = '{}'", dbProductName);

            dbProductVersion = meta.getDatabaseProductVersion();
            logger.debug("dbProductVersion = '{}'", dbProductVersion);

        } catch (SQLException e1) {
            logger.error("Asking for 'dbMajorVersion' seems to be unsupported: '{}'", e1.getMessage());
        }

    }

    public int getDbMajorVersion() {
        return dbMajorVersion;
    }

    public int getDbMinorVersion() {
        return dbMinorVersion;
    }

    public boolean isDbVersionGreater(int major, int minor) {
        if (dbMajorVersion > major) {
            return true;
        } else if (dbMajorVersion == major) {
            if (dbMinorVersion > minor) {
                return true;
            }
        }
        return false;
    }

    public int getDriverMajorVersion() {
        return driverMajorVersion;
    }

    public int getDriverMinorVersion() {
        return driverMinorVersion;
    }

    public boolean isDriverVersionGreater(int major, int minor) {
        if (major > driverMajorVersion) {
            return true;
        } else if (major == driverMajorVersion) {
            if (minor > driverMinorVersion) {
                return true;
            }
        }
        return false;
    }

    public String getDbProductName() {
        return dbProductName;
    }

    public String getDbProductVersion() {
        return dbProductVersion;
    }

}
