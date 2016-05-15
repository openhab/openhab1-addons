package org.openhab.persistence.dynamodb.internal;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.BeforeClass;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

public class ColorItemIntegrationTest extends AbstractTwoItemIntegrationTest {

    private static HSBType color(double hue, int saturation, int brightness) {
        return new HSBType(new DecimalType(hue), new PercentType(saturation), new PercentType(brightness));
    }

    private static HSBType color(String hue, int saturation, int brightness) {
        return new HSBType(new DecimalType(new BigDecimal(hue)), new PercentType(saturation),
                new PercentType(brightness));
    }

    private static final String name = "color";
    // values are encoded as <hue>,<saturation>,<brightness>, ordering goes wrt strings
    private static final HSBType state1 = color("3.1493842988948932984298384892384823984923849238492839483294893", 50,
            50);
    private static final HSBType state2 = color(75, 100, 90);
    private static final HSBType stateBetween = color(60, 50, 50);

    @BeforeClass
    public static void storeData() throws InterruptedException {
        ColorItem item = (ColorItem) items.get(name);
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
