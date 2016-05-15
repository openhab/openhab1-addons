package org.openhab.persistence.dynamodb.internal;

import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

public class DimmerItemIntegrationTest extends AbstractTwoItemIntegrationTest {

    private static final String name = "dimmer";
    private static final PercentType state1 = new PercentType(66);
    private static final PercentType state2 = new PercentType(68);
    private static final PercentType stateBetween = new PercentType(67);

    @BeforeClass
    public static void storeData() throws InterruptedException {
        DimmerItem item = (DimmerItem) items.get(name);

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
