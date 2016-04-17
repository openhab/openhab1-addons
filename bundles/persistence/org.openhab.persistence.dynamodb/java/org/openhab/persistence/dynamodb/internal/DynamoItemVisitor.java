package org.openhab.persistence.dynamodb.internal;

public interface DynamoItemVisitor {

    public void visit(DynamoDateItem item);

    public void visit(DynamoBigDecimalItem dynamoBigDecimalItem);

    public void visit(DynamoIntegerItem dynamoIntegerItem);

    public void visit(DynamoStringItem dynamoStringItem);
}