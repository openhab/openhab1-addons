/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
