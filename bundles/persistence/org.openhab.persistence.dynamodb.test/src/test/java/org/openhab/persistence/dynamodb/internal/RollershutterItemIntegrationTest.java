package org.openhab.persistence.dynamodb.internal;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

public class RollershutterItemIntegrationTest extends TwoItemIntegrationTest {

    private static final String name = "rollershutter";
    private static final PercentType state1 = PercentType.ZERO;
    private static final PercentType state2 = new PercentType("72.938289428989489389329834898929892439842399483498");
    private static final PercentType stateBetween = new PercentType(66); // no such that exists

    @BeforeClass
    public static void storeData() throws InterruptedException {
        RollershutterItem item = (RollershutterItem) items.get(name);
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

    /**
     * Use relaxed state comparison due to numerical rounding. See also DynamoDBBigDecimalItem.loseDigits
     */
    @Override
    protected void assertStateEquals(State expected, State actual) {
        BigDecimal expectedDecimal = ((DecimalType) expected).toBigDecimal();
        BigDecimal actualDecimal = ((DecimalType) expected).toBigDecimal();
        assertEquals(DynamoDBBigDecimalItem.loseDigits(expectedDecimal),
                DynamoDBBigDecimalItem.loseDigits(actualDecimal));
    }
}
