package org.openhab.persistence.dynamodb.internal;

import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.types.State;

public class DateTimeItemIntegrationTest extends TwoItemIntegrationTest {

    private static final String name = "datetime";
    private static final DateTimeType state1 = new DateTimeType("2016-05-15T10:00:00Z");
    private static final DateTimeType state2 = new DateTimeType("2016-05-16T10:00:00.123Z");
    private static final DateTimeType stateBetween = new DateTimeType("2016-05-15T14:00:00Z");;

    @BeforeClass
    public static void storeData() throws InterruptedException {
        DateTimeItem item = (DateTimeItem) items.get(name);

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
