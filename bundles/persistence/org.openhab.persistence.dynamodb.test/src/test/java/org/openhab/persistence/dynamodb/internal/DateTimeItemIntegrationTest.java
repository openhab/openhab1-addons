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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.BeforeClass;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.types.State;

/**
 *
 * @author Sami Salonen
 *
 */
public class DateTimeItemIntegrationTest extends AbstractTwoItemIntegrationTest {

    private static final String name = "datetime";
    private static final Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private static final Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private static final Calendar calBetween = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    static {
        cal1.set(2016, 5, 15, 10, 00, 00);
        cal2.set(2016, 5, 15, 16, 00, 00);
        cal2.set(Calendar.MILLISECOND, 123);
        calBetween.set(2016, 5, 15, 14, 00, 00);
    }

    private static final DateTimeType state1 = new DateTimeType(cal1);
    private static final DateTimeType state2 = new DateTimeType(cal2);
    private static final DateTimeType stateBetween = new DateTimeType(calBetween);;

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
