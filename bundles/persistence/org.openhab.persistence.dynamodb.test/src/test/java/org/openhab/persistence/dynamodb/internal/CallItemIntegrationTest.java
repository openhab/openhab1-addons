package org.openhab.persistence.dynamodb.internal;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.types.State;
import org.openhab.library.tel.items.CallItem;
import org.openhab.library.tel.types.CallType;

public class CallItemIntegrationTest extends AbstractTwoItemIntegrationTest {

    private static final String name = "call";
    // values are encoded as dest##orig, ordering goes wrt strings
    private static final CallType state1 = new CallType("orig1", "dest1");
    private static final CallType state2 = new CallType("orig1", "dest3");
    private static final CallType stateBetween = new CallType("orig2", "dest2");

    @BeforeClass
    public static void storeData() throws InterruptedException {
        CallItem item = (CallItem) items.get(name);
        item.setState(state1);
        beforeStore = new Date();
        Thread.sleep(10);
        service.store(item);
        afterStore1 = new Date();
        Thread.sleep(10);
        item.setState(state2);
        service.store(item);
        Thread.sleep(10);
        afterStore2 = new Date();

        logger.info("Created item between {} and {}", AbstractDynamoDBItem.DATEFORMATTER.format(beforeStore),
                AbstractDynamoDBItem.DATEFORMATTER.format(afterStore1));
    }

    @Override
    protected String getItemName() {
        return name;
    }

    @Override
    protected State getFirstItemState() {
        return state1;
    }

    @Override
    protected State getSecondItemState() {
        return state2;
    }

    @Override
    protected State getQueryItemStateBetween() {
        return stateBetween;
    }

    @Override
    protected void assertStateEquals(State expected, State actual) {
        // Since CallType.equals is broken, toString is used as workaround
        assertEquals(expected.toString(), actual.toString());
    }

}
