/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dynamodb.internal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDynamoDBItem<T> implements DynamoDBItem<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDynamoDBItem.class);
    public static final SimpleDateFormat DATEFORMATTER = new SimpleDateFormat(DATE_FORMAT);

    static {
        DATEFORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    protected static final String ATTRIBUTE_NAME_ITEMNAME = "itemname";
    protected static final String ATTRIBUTE_NAME_ITEMSTATE = "itemstate";

    protected String name;
    protected T state;
    protected Date time;

    public AbstractDynamoDBItem(String name, T state, Date time) {
        this.name = name;
        this.state = state;
        this.time = time;
    }

    public static DynamoDBItem<?> fromState(String name, State state, Date time) {
        if (state instanceof DecimalType) {
            return new DynamoDBBigDecimalItem(name, ((DecimalType) state).toBigDecimal(), time);
        } else if (state instanceof OnOffType) {
            return new DynamoDBIntegerItem(name, ((OnOffType) state) == OnOffType.ON ? 1 : 0, time);
        } else if (state instanceof OpenClosedType) {
            return new DynamoDBIntegerItem(name, ((OpenClosedType) state) == OpenClosedType.OPEN ? 1 : 0, time);
        } else if (state instanceof DateTimeType) {
            return new DynamoDBStringItem(name, DATEFORMATTER.format(((DateTimeType) state).getCalendar().getTime()),
                    time);
        } else {
            // HSBType, PointType and all others
            return new DynamoDBStringItem(name, state.toString(), time);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#asHistoricItem(org.openhab.core.items.Item)
     */
    @Override
    public HistoricItem asHistoricItem(final Item item) {
        final State[] state = new State[1];
        accept(new DynamoDBItemVisitor() {

            @Override
            public void visit(DynamoDBStringItem dynamoStringItem) {
                if (item instanceof HSBType) {
                    state[0] = new HSBType(dynamoStringItem.getState());
                } else if (item instanceof PointType) {
                    state[0] = new PointType(dynamoStringItem.getState());
                } else if (item instanceof DateTimeItem) {
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(DATEFORMATTER.parse(dynamoStringItem.getState()));
                    } catch (ParseException e) {
                        logger.error("Failed to parse {} as date. Outputting string item instead",
                                dynamoStringItem.getState());
                        state[0] = new StringType(dynamoStringItem.getState());
                    }
                    state[0] = new DateTimeType(cal);
                } else {
                    state[0] = new StringType(dynamoStringItem.getState());
                }
            }

            @Override
            public void visit(DynamoDBIntegerItem dynamoIntegerItem) {
                if (item instanceof DimmerItem) {
                    state[0] = new PercentType(dynamoIntegerItem.getState());
                } else if (item instanceof SwitchItem) {
                    state[0] = dynamoIntegerItem.getState() == 1 ? OnOffType.ON : OnOffType.OFF;
                } else if (item instanceof ContactItem) {
                    state[0] = dynamoIntegerItem.getState() == 1 ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
                } else if (item instanceof RollershutterItem) {
                    state[0] = new PercentType(dynamoIntegerItem.getState());
                } else {
                    logger.warn("Not sure how to convert integer item {} to type {}. Using StringType as fallback",
                            dynamoIntegerItem.getName(), item.getClass());
                    state[0] = new StringType(dynamoIntegerItem.getState().toString());
                }
            }

            @Override
            public void visit(DynamoDBBigDecimalItem dynamoBigDecimalItem) {
                if (item instanceof NumberItem) {
                    state[0] = new DecimalType(dynamoBigDecimalItem.getState());
                }
            }
        });
        return new DynamoDBHistoricItem(getName(), state[0], getTime());
    }

    // getter and setter must be defined in the child class in order to have it working with AWS SDK

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#getName()
     */
    @Override
    public abstract String getName();

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#getState()
     */
    @Override
    public abstract T getState();

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#getTime()
     */
    @Override
    public abstract Date getTime();

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#setName(java.lang.String)
     */
    @Override
    public abstract void setName(String name);

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#setState(T)
     */
    @Override
    public abstract void setState(T state);

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#setTime(java.util.Date)
     */
    @Override
    public abstract void setTime(Date time);

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.persistence.dynamodb.internal.DynamoItem#accept(org.openhab.persistence.dynamodb.internal.
     * DynamoItemVisitor)
     */
    @Override
    public abstract void accept(DynamoDBItemVisitor visitor);

    @Override
    public String toString() {
        return DateFormat.getDateTimeInstance().format(time) + ": " + name + " -> " + state.toString();
    }

}
