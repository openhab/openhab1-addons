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

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Iterator;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Operator;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

/**
 * This is abstract class helping with integration testing the persistence service. Different kind of queries are tested
 * against actual dynamo db database.
 *
 *
 * Inheritor of this base class needs to store two states of one item in a static method annotated with @BeforeClass.
 * This
 * static
 * class should update the private static fields
 * beforeStore (date before storing anything), afterStore1 (after storing first item, but before storing second item),
 * afterStore2 (after storing second item). The item name must correspond to getItemName. The first state needs to be
 * smaller than the second state.
 *
 * To have more comprehensive tests, the inheritor class can define getQueryItemStateBetween to provide a value between
 * the two states. Null can be used to omit the additional tests.
 *
 *
 * See DimmerItemIntegrationTest for example how to use this base class.
 *
 * @author Sami Salonen
 *
 */
public abstract class AbstractTwoItemIntegrationTest extends BaseIntegrationTest {

    protected static Date beforeStore;
    protected static Date afterStore1;
    protected static Date afterStore2;

    protected abstract String getItemName();

    /**
     * State of the time item stored first, should be smaller than the second value
     *
     * @return
     */
    protected abstract State getFirstItemState();

    /**
     * State of the time item stored second, should be larger than the first value
     *
     * @return
     */
    protected abstract State getSecondItemState();

    /**
     * State that is between the first and second. Use null to omit extended tests using this value.
     *
     * @return
     */
    protected abstract State getQueryItemStateBetween();

    protected void assertStateEquals(State expected, State actual) {
        assertEquals(expected, actual);
    }

    @BeforeClass
    public static void checkService() throws InterruptedException {
        String msg = "DynamoDB integration tests will be skipped. Did you specify AWS credentials for testing? "
                + "See BaseIntegrationTest for more details";
        if (service == null) {
            System.out.println(msg);
        }
        Assume.assumeTrue(msg, service != null);
    }

    /**
     * Asserts that iterable contains correct items and nothing else
     *
     */
    private void assertIterableContainsItems(Iterable<HistoricItem> iterable, boolean ascending) {
        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        HistoricItem actual2 = iterator.next();
        assertFalse(iterator.hasNext());

        for (HistoricItem actual : new HistoricItem[] { actual1, actual2 }) {
            assertEquals(getItemName(), actual.getName());
        }
        HistoricItem storedFirst;
        HistoricItem storedSecond;
        if (ascending) {
            storedFirst = actual1;
            storedSecond = actual2;
        } else {
            storedFirst = actual2;
            storedSecond = actual1;
        }

        assertStateEquals(getFirstItemState(), storedFirst.getState());
        assertTrue(storedFirst.getTimestamp().before(afterStore1));
        assertTrue(storedFirst.getTimestamp().after(beforeStore));

        assertStateEquals(getSecondItemState(), storedSecond.getState());
        assertTrue(storedSecond.getTimestamp().before(afterStore2));
        assertTrue(storedSecond.getTimestamp().after(afterStore1));
    }

    @Test
    public void testQueryUsingName() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryUsingNameAndStart() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryUsingNameAndStartNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setItemName(getItemName());
        criteria.setBeginDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testQueryUsingNameAndEnd() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryUsingNameAndEndNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setItemName(getItemName());
        criteria.setEndDate(beforeStore);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testQueryUsingNameAndStartAndEnd() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryUsingNameAndStartAndEndDesc() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.DESCENDING);
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, false);
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithNEQOperator() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.NEQ);
        criteria.setState(getSecondItemState());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        assertFalse(iterator.hasNext());
        assertStateEquals(getFirstItemState(), actual1.getState());
        assertTrue(actual1.getTimestamp().before(afterStore1));
        assertTrue(actual1.getTimestamp().after(beforeStore));
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithEQOperator() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.EQ);
        criteria.setState(getFirstItemState());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        assertFalse(iterator.hasNext());
        assertStateEquals(getFirstItemState(), actual1.getState());
        assertTrue(actual1.getTimestamp().before(afterStore1));
        assertTrue(actual1.getTimestamp().after(beforeStore));
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithLTOperator() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.LT);
        criteria.setState(getSecondItemState());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        assertFalse(iterator.hasNext());
        assertStateEquals(getFirstItemState(), actual1.getState());
        assertTrue(actual1.getTimestamp().before(afterStore1));
        assertTrue(actual1.getTimestamp().after(beforeStore));
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithLTOperatorNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.LT);
        criteria.setState(getFirstItemState());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithLTEOperator() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.LTE);
        criteria.setState(getFirstItemState());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        assertFalse(iterator.hasNext());
        assertStateEquals(getFirstItemState(), actual1.getState());
        assertTrue(actual1.getTimestamp().before(afterStore1));
        assertTrue(actual1.getTimestamp().after(beforeStore));
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithGTOperator() {
        // Skip for subclasses which have null "state between"
        Assume.assumeTrue(getQueryItemStateBetween() != null);

        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.GT);
        criteria.setState(getQueryItemStateBetween());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        assertFalse(iterator.hasNext());
        assertStateEquals(getSecondItemState(), actual1.getState());
        assertTrue(actual1.getTimestamp().before(afterStore2));
        assertTrue(actual1.getTimestamp().after(afterStore1));
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithGTOperatorNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.GT);
        criteria.setState(getSecondItemState());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testQueryUsingNameAndStartAndEndWithGTEOperator() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOperator(Operator.GTE);
        criteria.setState(getSecondItemState());
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        assertFalse(iterator.hasNext());
        assertStateEquals(getSecondItemState(), actual1.getState());
        assertTrue(actual1.getTimestamp().before(afterStore2));
        assertTrue(actual1.getTimestamp().after(afterStore1));
    }

    @Test
    public void testQueryUsingNameAndStartAndEndFirst() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore1);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);

        Iterator<HistoricItem> iterator = iterable.iterator();
        HistoricItem actual1 = iterator.next();
        assertFalse(iterator.hasNext());
        assertStateEquals(getFirstItemState(), actual1.getState());
        assertTrue(actual1.getTimestamp().before(afterStore1));
        assertTrue(actual1.getTimestamp().after(beforeStore));
    }

    @Test
    public void testQueryUsingNameAndStartAndEndNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(beforeStore); // sic
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertFalse(iterable.iterator().hasNext());
    }
}
