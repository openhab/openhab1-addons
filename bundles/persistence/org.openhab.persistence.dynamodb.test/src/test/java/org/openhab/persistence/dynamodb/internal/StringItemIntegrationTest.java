package org.openhab.persistence.dynamodb.internal;

import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

public class StringItemIntegrationTest extends TwoItemIntegrationTest {

    private static final String name = "string";
    private static final StringType state1 = new StringType("b001");
    private static final StringType state2 = new StringType("c002");
    private static final StringType stateBetween = new StringType("b001");

    @BeforeClass
    public static void storeData() throws InterruptedException {
        StringItem item = (StringItem) items.get(name);
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

}
