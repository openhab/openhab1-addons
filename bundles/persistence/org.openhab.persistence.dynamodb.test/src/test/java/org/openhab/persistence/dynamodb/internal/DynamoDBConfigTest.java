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
package org.openhab.persistence.dynamodb.internal;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.amazonaws.regions.Regions;
import com.google.common.collect.ImmutableMap;

/**
 *
 * @author Sami Salonen
 *
 */
public class DynamoDBConfigTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testEmpty() throws Exception {
        assertNull(DynamoDBConfig.fromConfig(new HashMap<String, Object>()));
    }

    @Test
    public void testInvalidRegion() throws Exception {
        assertNull(DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "foobie")));
    }

    @Test
    public void testRegionOnly() throws Exception {
        assertNull(DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1")));
    }

    @Test
    public void testRegionWithAccessKeys() throws Exception {
        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "accessKey", "access1", "secretKey", "secret1"));
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("access1", fromConfig.getCredentials().getAWSAccessKeyId());
        assertEquals("secret1", fromConfig.getCredentials().getAWSSecretKey());
        assertEquals("openhab-", fromConfig.getTablePrefix());
        assertEquals(true, fromConfig.isCreateTable());
        assertEquals(1, fromConfig.getReadCapacityUnits());
        assertEquals(1, fromConfig.getWriteCapacityUnits());
        assertEquals(1000L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(1000, fromConfig.getBufferSize());
    }

    @Test
    public void testRegionWithProfilesConfigFile() throws Exception {
        File credsFile = folder.newFile("creds");
        FileUtils.write(credsFile, "[fooprofile]\n" + "aws_access_key_id=testAccessKey\n"
                + "aws_secret_access_key=testSecretKey\n" + "aws_session_token=testSessionToken\n");

        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "profilesConfigFile", credsFile.getAbsolutePath(), "profile", "fooprofile"));
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("openhab-", fromConfig.getTablePrefix());
        assertEquals(true, fromConfig.isCreateTable());
        assertEquals(1, fromConfig.getReadCapacityUnits());
        assertEquals(1, fromConfig.getWriteCapacityUnits());
        assertEquals(1000L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(1000, fromConfig.getBufferSize());
    }

    @Test
    public void testNullConfiguration() throws Exception {
        assertNull(DynamoDBConfig.fromConfig(null));
    }

    @Test
    public void testEmptyConfiguration() throws Exception {
        assertNull(DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of()));
    }

    @Test
    public void testRegionWithInvalidProfilesConfigFile() throws Exception {
        File credsFile = folder.newFile("creds");
        FileUtils.write(credsFile, "[fooprofile]\n" + "aws_access_key_idINVALIDKEY=testAccessKey\n"
                + "aws_secret_access_key=testSecretKey\n" + "aws_session_token=testSessionToken\n");

        assertNull(DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "profilesConfigFile", credsFile.getAbsolutePath(), "profile", "fooprofile")));
    }

    @Test
    public void testRegionWithProfilesConfigFileMissingProfile() throws Exception {
        File credsFile = folder.newFile("creds");
        FileUtils.write(credsFile, "[fooprofile]\n" + "aws_access_key_id=testAccessKey\n"
                + "aws_secret_access_key=testSecretKey\n" + "aws_session_token=testSessionToken\n");

        assertNull(DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "profilesConfigFile", credsFile.getAbsolutePath())));
    }

    @Test
    public void testRegionWithAccessKeysWithPrefix() throws Exception {
        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "accessKey", "access1", "secretKey", "secret1", "tablePrefix", "foobie-"));
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("access1", fromConfig.getCredentials().getAWSAccessKeyId());
        assertEquals("secret1", fromConfig.getCredentials().getAWSSecretKey());
        assertEquals("foobie-", fromConfig.getTablePrefix());
        assertEquals(true, fromConfig.isCreateTable());
        assertEquals(1, fromConfig.getReadCapacityUnits());
        assertEquals(1, fromConfig.getWriteCapacityUnits());
        assertEquals(1000L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(1000, fromConfig.getBufferSize());
    }

    @Test
    public void testRegionWithAccessKeysWithPrefixWithCreateTable() throws Exception {
        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "accessKey", "access1", "secretKey", "secret1", "createTable", "false"));
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("access1", fromConfig.getCredentials().getAWSAccessKeyId());
        assertEquals("secret1", fromConfig.getCredentials().getAWSSecretKey());
        assertEquals("openhab-", fromConfig.getTablePrefix());
        assertEquals(false, fromConfig.isCreateTable());
        assertEquals(1, fromConfig.getReadCapacityUnits());
        assertEquals(1, fromConfig.getWriteCapacityUnits());
        assertEquals(1000L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(1000, fromConfig.getBufferSize());
    }

    @Test
    public void testRegionWithAccessKeysWithPrefixWithReadCapacityUnits() throws Exception {
        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "accessKey", "access1", "secretKey", "secret1", "readCapacityUnits", "5"));
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("access1", fromConfig.getCredentials().getAWSAccessKeyId());
        assertEquals("secret1", fromConfig.getCredentials().getAWSSecretKey());
        assertEquals("openhab-", fromConfig.getTablePrefix());
        assertEquals(true, fromConfig.isCreateTable());
        assertEquals(5, fromConfig.getReadCapacityUnits());
        assertEquals(1, fromConfig.getWriteCapacityUnits());
        assertEquals(1000L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(1000, fromConfig.getBufferSize());
    }

    @Test
    public void testRegionWithAccessKeysWithPrefixWithWriteCapacityUnits() throws Exception {
        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "accessKey", "access1", "secretKey", "secret1", "writeCapacityUnits", "5"));
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("access1", fromConfig.getCredentials().getAWSAccessKeyId());
        assertEquals("secret1", fromConfig.getCredentials().getAWSSecretKey());
        assertEquals("openhab-", fromConfig.getTablePrefix());
        assertEquals(true, fromConfig.isCreateTable());
        assertEquals(1, fromConfig.getReadCapacityUnits());
        assertEquals(5, fromConfig.getWriteCapacityUnits());
        assertEquals(1000L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(1000, fromConfig.getBufferSize());
    }

    @Test
    public void testRegionWithAccessKeysWithPrefixWithReadWriteCapacityUnits() throws Exception {
        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(ImmutableMap.<String, Object> of("region", "eu-west-1",
                "accessKey", "access1", "secretKey", "secret1", "readCapacityUnits", "3", "writeCapacityUnits", "5"));
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("access1", fromConfig.getCredentials().getAWSAccessKeyId());
        assertEquals("secret1", fromConfig.getCredentials().getAWSSecretKey());
        assertEquals("openhab-", fromConfig.getTablePrefix());
        assertEquals(true, fromConfig.isCreateTable());
        assertEquals(3, fromConfig.getReadCapacityUnits());
        assertEquals(5, fromConfig.getWriteCapacityUnits());
        assertEquals(1000L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(1000, fromConfig.getBufferSize());
    }

    @Test
    public void testRegionWithAccessKeysWithPrefixWithReadWriteCapacityUnitsWithBufferSettings() throws Exception {
        DynamoDBConfig fromConfig = DynamoDBConfig.fromConfig(
                ImmutableMap.<String, Object> builder().put("region", "eu-west-1").put("accessKey", "access1")
                        .put("secretKey", "secret1").put("readCapacityUnits", "3").put("writeCapacityUnits", "5")
                        .put("bufferCommitIntervalMillis", "501").put("bufferSize", "112").build());
        assertEquals(Regions.EU_WEST_1, fromConfig.getRegion());
        assertEquals("access1", fromConfig.getCredentials().getAWSAccessKeyId());
        assertEquals("secret1", fromConfig.getCredentials().getAWSSecretKey());
        assertEquals("openhab-", fromConfig.getTablePrefix());
        assertEquals(true, fromConfig.isCreateTable());
        assertEquals(3, fromConfig.getReadCapacityUnits());
        assertEquals(5, fromConfig.getWriteCapacityUnits());
        assertEquals(501L, fromConfig.getBufferCommitIntervalMillis());
        assertEquals(112, fromConfig.getBufferSize());
    }
}
