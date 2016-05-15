package org.openhab.persistence.dynamodb.internal;

import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;

public class ContactItemIntegrationTest extends TwoItemIntegrationTest {

    private static final String name = "contact";
    private static final OpenClosedType state1 = OpenClosedType.CLOSED;
    private static final OpenClosedType state2 = OpenClosedType.OPEN;
    private static final OnOffType stateBetween = null; // no such that exists

    @BeforeClass
    public static void storeData() throws InterruptedException {
        ContactItem item = (ContactItem) items.get(name);
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
