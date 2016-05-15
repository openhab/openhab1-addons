package org.openhab.persistence.dynamodb.internal;

public class DynamoDBTableNameResolver {

    private final String tablePrefix;

    public DynamoDBTableNameResolver(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String fromItem(DynamoDBItem<?> item) {
        final String[] tableName = new String[1];

        item.accept(new DynamoDBItemVisitor() {

            @Override
            public void visit(DynamoDBBigDecimalItem dynamoBigDecimalItem) {
                tableName[0] = tablePrefix + "bigdecimal";
            }

            @Override
            public void visit(DynamoDBStringItem dynamoStringItem) {
                tableName[0] = tablePrefix + "string";
            }
        });
        return tableName[0];
    }

    public String fromClass(Class<? extends DynamoDBItem<?>> clazz) {
        DynamoDBItem<?> dummy;
        try {
            dummy = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Could not find suitable constructor for class %s", clazz));
        }
        return this.fromItem(dummy);
    }

}
