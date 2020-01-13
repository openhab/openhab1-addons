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

/**
 * The DynamoDBTableNameResolver resolves DynamoDB table name for a given item.
 *
 * @author Sami Salonen
 *
 */
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
