package org.openhab.persistence.dynamodb.internal;

public class DynamoDBTableNameResolver {

    private final String tablePrefix;

    public DynamoDBTableNameResolver(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String fromItem(DynamoDBItem<?> item) {
        final String[] tableName = new String[1];

        // Use the visitor pattern to deduce the table name
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

    /**
     * Construct DynamoDBTableNameResolver corresponding to DynamoDBItem class
     *
     * @param clazz
     * @return
     */
    public String fromClass(Class<? extends DynamoDBItem<?>> clazz) {
        DynamoDBItem<?> dummy;
        try {
            // Construct new instance of this class (assuming presense no-argument constructor)
            // in order to re-use fromItem(DynamoDBItem) constructor
            dummy = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Could not find suitable constructor for class %s", clazz));
        }
        return this.fromItem(dummy);
    }

}
