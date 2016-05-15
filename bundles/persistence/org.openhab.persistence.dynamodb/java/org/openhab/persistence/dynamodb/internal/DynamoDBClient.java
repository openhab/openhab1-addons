package org.openhab.persistence.dynamodb.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/***
 * Shallow wrapper for Dynamo DB wrappers
 *
 */
public class DynamoDBClient {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBClient.class);
    private DynamoDB dynamo;
    private AmazonDynamoDBClient client;

    public DynamoDBClient(AWSCredentials credentials, Region region) {
        client = new AmazonDynamoDBClient(credentials);
        client.setRegion(region);
        dynamo = new DynamoDB(client);
    }

    public DynamoDBClient(DynamoDBConfig clientConfig) {
        this(clientConfig.getCredentials(), clientConfig.getRegion());
    }

    public AmazonDynamoDBClient getDynamoClient() {
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
        } catch (AmazonClientException e) {
            logger.error("Got internal server error when trying to list tables: {}", e);
            return false;
        }
        return true;
    }
}
