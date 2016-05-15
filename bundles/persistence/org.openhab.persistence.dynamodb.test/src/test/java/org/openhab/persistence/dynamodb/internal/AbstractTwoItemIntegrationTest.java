package org.openhab.persistence.dynamodb.internal;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Iterator;

import org.junit.Assume;
import org.junit.Test;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Operator;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * See DimmerItemIntegrationTest for example how to use this base class.
 *
 * Implementor need to store two items. The first one needs to be smaller than the second. Protected static timestamps
 * in this class needs to be updated
 *
 *
 */
public abstract class AbstractTwoItemIntegrationTest extends BaseIntegrationTest {

    protected static final Logger logger = LoggerFactory.getLogger(DynamoDBPersistenceService.class);
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
     * State that is between the first and second
     *
     * @return
     */
    protected abstract State getQueryItemStateBetween();

    protected void assertStateEquals(State expected, State actual) {
        assertEquals(expected, actual);
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
    public void testQueryDimmerUsingName() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryDimmerUsingNameAndStart() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryDimmerUsingNameAndStartNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setItemName(getItemName());
        criteria.setBeginDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testQueryDimmerUsingNameAndEnd() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryDimmerUsingNameAndEndNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setItemName(getItemName());
        criteria.setEndDate(beforeStore);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testQueryDimmerUsingNameAndStartAndEnd() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.ASCENDING);
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, true);
    }

    @Test
    public void testQueryDimmerUsingNameAndStartAndEndDesc() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setOrdering(Ordering.DESCENDING);
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(afterStore2);
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertIterableContainsItems(iterable, false);
    }

    @Test
    public void testQueryDimmerUsingNameAndStartAndEndWithNEQOperator() {
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
    public void testQueryDimmerUsingNameAndStartAndEndWithEQOperator() {
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
    public void testQueryDimmerUsingNameAndStartAndEndWithLTOperator() {
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
    public void testQueryDimmerUsingNameAndStartAndEndWithLTOperatorNoMatch() {
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
    public void testQueryDimmerUsingNameAndStartAndEndWithLTEOperator() {
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
    public void testQueryDimmerUsingNameAndStartAndEndWithGTOperator() {
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
    public void testQueryDimmerUsingNameAndStartAndEndWithGTOperatorNoMatch() {
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
    public void testQueryDimmerUsingNameAndStartAndEndWithGTEOperator() {
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
    public void testQueryDimmerUsingNameAndStartAndEndFirst() {
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
    public void testQueryDimmerUsingNameAndStartAndEndNoMatch() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setItemName(getItemName());
        criteria.setBeginDate(beforeStore);
        criteria.setEndDate(beforeStore); // sic
        Iterable<HistoricItem> iterable = BaseIntegrationTest.service.query(criteria);
        assertFalse(iterable.iterator().hasNext());
    }
}
