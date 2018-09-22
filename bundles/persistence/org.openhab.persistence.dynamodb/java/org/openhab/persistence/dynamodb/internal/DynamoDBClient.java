/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dynamodb.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/**
 * Shallow wrapper for Dynamo DB wrappers
 *
 * @author Sami Salonen
 */
public class DynamoDBClient {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBClient.class);
    private DynamoDB dynamo;
    private AmazonDynamoDB client;

    public DynamoDBClient(AWSCredentials credentials, Regions region) {
        client = AmazonDynamoDBClientBuilder.standard().withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        dynamo = new DynamoDB(client);
    }

    public DynamoDBClient(DynamoDBConfig clientConfig) {
        this(clientConfig.getCredentials(), clientConfig.getRegion());
    }

    public AmazonDynamoDB getDynamoClient() {
        return client;
    }

    public DynamoDB getDynamoDB() {
        return dynamo;
    }

    public void shutdown() {
        dynamo.shutdown();
    }

    public boolean checkConnection() {
        try {
            dynamo.listTables(1).firstPage();
        } catch (Exception e) {
            logger.error("Got internal server error when trying to list tables: {}", e.getMessage());
            return false;
        }
        return true;
    }
}
