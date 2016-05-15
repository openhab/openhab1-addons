package org.openhab.persistence.dynamodb.internal;

import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.types.State;

public class LocationItemIntegrationTest extends AbstractTwoItemIntegrationTest {

    private static final String name = "location";
    // values are encoded as lat,lon[,alt] , ordering goes wrt strings
    private static final PointType state1 = new PointType(
            new DecimalType("60.012033100120453345435345345345346365434630300230230032020393149"), new DecimalType(30.),
            new DecimalType(3.0));
    private static final PointType state2 = new PointType(new DecimalType(61.0), new DecimalType(30.));
    private static final PointType stateBetween = new PointType(new DecimalType(60.5), new DecimalType(30.));

    @BeforeClass
    public static void storeData() throws InterruptedException {
        LocationItem item = (LocationItem) items.get(name);
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
