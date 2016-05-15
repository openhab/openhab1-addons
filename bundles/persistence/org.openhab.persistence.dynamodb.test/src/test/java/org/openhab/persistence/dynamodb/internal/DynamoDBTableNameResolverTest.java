package org.openhab.persistence.dynamodb.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DynamoDBTableNameResolverTest {

    @Test
    public void testWithDynamoDBBigDecimalItem() {
        assertEquals("prefixbigdecimal",
                new DynamoDBTableNameResolver("prefix").fromItem(new DynamoDBBigDecimalItem()));
    }

    @Test
    public void testWithDynamoDBStringItem() {
        assertEquals("prefixstring", new DynamoDBTableNameResolver("prefix").fromItem(new DynamoDBStringItem()));
    }
}
