/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dynamodb.internal;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.regions.Regions;

/**
 * Configuration for DynamoDB connections
 *
 * @author Sami Salonen
 */
public class DynamoDBConfig {
    public static final String DEFAULT_TABLE_PREFIX = "openhab-";
    public static final boolean DEFAULT_CREATE_TABLE_ON_DEMAND = true;
    public static final long DEFAULT_READ_CAPACITY_UNITS = 1;
    public static final long DEFAULT_WRITE_CAPACITY_UNITS = 1;

    private static final Logger logger = LoggerFactory.getLogger(DynamoDBConfig.class);

    private String tablePrefix = DEFAULT_TABLE_PREFIX;
    private Regions region;
    private AWSCredentials credentials;
    private boolean createTable = DEFAULT_CREATE_TABLE_ON_DEMAND;
    private long readCapacityUnits = DEFAULT_READ_CAPACITY_UNITS;
    private long writeCapacityUnits = DEFAULT_WRITE_CAPACITY_UNITS;

    /**
     *
     * @param config persistence service configuration
     * @return DynamoDB configuration. Returns null in case of configuration errors
     */
    public static DynamoDBConfig fromConfig(Map<String, Object> config) {
        if (config == null || config.isEmpty()) {
            logger.error("Configuration not provided! At least AWS region and credentials must be provided.");
            return null;
        }

        try {
            String regionName = (String) config.get("region");
            if (isBlank(regionName)) {
                invalidRegionLogHelp(regionName);
                return null;
            }
            final Regions region;
            try {
                region = Regions.fromName(regionName);
            } catch (IllegalArgumentException e) {
                invalidRegionLogHelp(regionName);
                return null;
            }

            AWSCredentials credentials;
            String accessKey = (String) config.get("accessKey");
            String secretKey = (String) config.get("secretKey");
            if (!isBlank(accessKey) && !isBlank(secretKey)) {
                logger.debug("accessKey and secretKey specified. Using those.");
                credentials = new BasicAWSCredentials(accessKey, secretKey);
            } else {
                logger.debug("accessKey and/or secretKey blank. Checking profilesConfigFile and profile.");
                String profilesConfigFile = (String) config.get("profilesConfigFile");
                String profile = (String) config.get("profile");
                if (isBlank(profilesConfigFile) || isBlank(profile)) {
                    logger.error("Specify either 1) accessKey and secretKey; or 2) profilesConfigFile and "
                            + "profile for providing AWS credentials");
                    return null;
                }
                credentials = new ProfilesConfigFile(profilesConfigFile).getCredentials(profile);
            }

            String table = (String) config.get("tablePrefix");
            if (isBlank(table)) {
                logger.debug("Using default table name {}", DEFAULT_TABLE_PREFIX);
                table = DEFAULT_TABLE_PREFIX;
            }

            final boolean createTable;
            String createTableParam = (String) config.get("createTable");
            if (isBlank(createTableParam)) {
                logger.debug("Creating table on demand: {}", DEFAULT_CREATE_TABLE_ON_DEMAND);
                createTable = DEFAULT_CREATE_TABLE_ON_DEMAND;
            } else {
                createTable = Boolean.parseBoolean(createTableParam);
            }

            final long readCapacityUnits;
            String readCapacityUnitsParam = (String) config.get("readCapacityUnits");
            if (isBlank(readCapacityUnitsParam)) {
                logger.debug("Read capacity units: {}", DEFAULT_READ_CAPACITY_UNITS);
                readCapacityUnits = DEFAULT_READ_CAPACITY_UNITS;
            } else {
                readCapacityUnits = Long.parseLong(readCapacityUnitsParam);
            }

            final long writeCapacityUnits;
            String writeCapacityUnitsParam = (String) config.get("writeCapacityUnits");
            if (isBlank(writeCapacityUnitsParam)) {
                logger.debug("Write capacity units: {}", DEFAULT_WRITE_CAPACITY_UNITS);
                writeCapacityUnits = DEFAULT_WRITE_CAPACITY_UNITS;
            } else {
                writeCapacityUnits = Long.parseLong(writeCapacityUnitsParam);
            }

            return new DynamoDBConfig(region, credentials, table, createTable, readCapacityUnits, writeCapacityUnits);
        } catch (Exception e) {
            logger.error("Error with configuration", e);
            return null;
        }
    }

    public DynamoDBConfig(Regions region, AWSCredentials credentials, String table, boolean createTable,
            long readCapacityUnits, long writeCapacityUnits) {
        this.region = region;
        this.credentials = credentials;
        this.tablePrefix = table;
        this.createTable = createTable;
        this.readCapacityUnits = readCapacityUnits;
        this.writeCapacityUnits = writeCapacityUnits;
    }

    public AWSCredentials getCredentials() {
        return credentials;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public Regions getRegion() {
        return region;
    }

    public boolean isCreateTable() {
        return createTable;
    }

    public long getReadCapacityUnits() {
        return readCapacityUnits;
    }

    public long getWriteCapacityUnits() {
        return writeCapacityUnits;
    }

    private static void invalidRegionLogHelp(String region) {
        logger.error("Specify valid AWS region to use, got {}. Valid values include: {}", region,
                StringUtils.join(Regions.values(), ','));
    }
}
