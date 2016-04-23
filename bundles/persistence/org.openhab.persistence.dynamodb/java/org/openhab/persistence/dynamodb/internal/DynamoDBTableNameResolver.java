package org.openhab.persistence.dynamodb.internal;

public class DynamoDBTableNameResolver {

    private final String tablePrefix;

    public DynamoDBTableNameResolver(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String fromItem(DynamoItem<?> item) {
        final String[] tableName = new String[1];

        item.accept(new DynamoItemVisitor() {

            @Override
            public void visit(DynamoBigDecimalItem dynamoBigDecimalItem) {
                tableName[0] = tablePrefix + "bigdecimal";
            }

            @Override
            public void visit(DynamoIntegerItem dynamoIntegerItem) {
                tableName[0] = tablePrefix + "integer";
            }

            @Override
            public void visit(DynamoStringItem dynamoStringItem) {
                tableName[0] = tablePrefix + "string";
            }
        });
        return tableName[0];
    }

}
