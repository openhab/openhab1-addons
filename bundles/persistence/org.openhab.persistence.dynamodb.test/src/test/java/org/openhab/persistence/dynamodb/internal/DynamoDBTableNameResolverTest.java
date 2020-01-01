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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author Sami Salonen
 *
 */
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
