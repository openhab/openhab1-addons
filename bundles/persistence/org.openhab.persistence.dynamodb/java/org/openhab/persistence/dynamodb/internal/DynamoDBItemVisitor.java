package org.openhab.persistence.dynamodb.internal;

/**
 * Visitor for DynamoDBItem
 *
 */
public interface DynamoDBItemVisitor {

    public void visit(DynamoDBBigDecimalItem dynamoBigDecimalItem);

    public void visit(DynamoDBStringItem dynamoStringItem);
}