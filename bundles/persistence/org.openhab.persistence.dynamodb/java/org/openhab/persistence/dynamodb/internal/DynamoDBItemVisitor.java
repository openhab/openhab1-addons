package org.openhab.persistence.dynamodb.internal;

public interface DynamoDBItemVisitor {

    public void visit(DynamoDBBigDecimalItem dynamoBigDecimalItem);

    public void visit(DynamoDBIntegerItem dynamoIntegerItem);

    public void visit(DynamoDBStringItem dynamoStringItem);
}