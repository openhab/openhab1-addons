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

import java.io.IOException;

import org.junit.Test;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.library.tel.items.CallItem;

/**
 * Test for AbstractDynamoDBItem.getDynamoItemClass
 *
 */
public class AbstractDynamoDBItemGetDynamoItemClass {

    @Test
    public void testCallItem() throws IOException {
        assertEquals(DynamoDBStringItem.class, AbstractDynamoDBItem.getDynamoItemClass(CallItem.class));

    }

    @Test
    public void testContactItem() throws IOException {
        assertEquals(DynamoDBBigDecimalItem.class, AbstractDynamoDBItem.getDynamoItemClass(ContactItem.class));
    }

    @Test
    public void testDateTimeItem() throws IOException {
        assertEquals(DynamoDBStringItem.class, AbstractDynamoDBItem.getDynamoItemClass(DateTimeItem.class));
    }

    @Test
    public void testStringItem() throws IOException {
        assertEquals(DynamoDBStringItem.class, AbstractDynamoDBItem.getDynamoItemClass(StringItem.class));
    }

    @Test
    public void testLocationItem() throws IOException {
        assertEquals(DynamoDBStringItem.class, AbstractDynamoDBItem.getDynamoItemClass(LocationItem.class));
    }

    @Test
    public void testNumberItem() throws IOException {
        assertEquals(DynamoDBBigDecimalItem.class, AbstractDynamoDBItem.getDynamoItemClass(NumberItem.class));
    }

    @Test
    public void testColorItem() throws IOException {
        assertEquals(DynamoDBStringItem.class, AbstractDynamoDBItem.getDynamoItemClass(ColorItem.class));
    }

    @Test
    public void testDimmerItem() throws IOException {
        assertEquals(DynamoDBBigDecimalItem.class, AbstractDynamoDBItem.getDynamoItemClass(DimmerItem.class));
    }

    @Test
    public void testRollershutterItem() throws IOException {
        assertEquals(DynamoDBBigDecimalItem.class, AbstractDynamoDBItem.getDynamoItemClass(RollershutterItem.class));
    }

    @Test
    public void testOnOffTypeWithSwitchItem() throws IOException {
        assertEquals(DynamoDBBigDecimalItem.class, AbstractDynamoDBItem.getDynamoItemClass(SwitchItem.class));
    }
}
