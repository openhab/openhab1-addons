/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dynamodb.internal;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.library.tel.items.CallItem;
import org.openhab.library.tel.types.CallType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDynamoDBItem<T> implements DynamoDBItem<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDynamoDBItem.class);
    public static final SimpleDateFormat DATEFORMATTER = new SimpleDateFormat(DATE_FORMAT);

    static {
        DATEFORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private static final String UNDEFINED_PLACEHOLDER = "<org.openhab.core.types.UnDefType.UNDEF>";

    static final Map<Class<? extends Item>, Class<? extends DynamoDBItem<?>>> itemClassToDynamoItemClass = new HashMap<Class<? extends Item>, Class<? extends DynamoDBItem<?>>>();

    static {
        itemClassToDynamoItemClass.put(CallItem.class, DynamoDBStringItem.class);
        itemClassToDynamoItemClass.put(ContactItem.class, DynamoDBBigDecimalItem.class);
        itemClassToDynamoItemClass.put(DateTimeItem.class, DynamoDBStringItem.class);
        itemClassToDynamoItemClass.put(LocationItem.class, DynamoDBStringItem.class);
        itemClassToDynamoItemClass.put(NumberItem.class, DynamoDBBigDecimalItem.class);
        itemClassToDynamoItemClass.put(RollershutterItem.class, DynamoDBBigDecimalItem.class);
        itemClassToDynamoItemClass.put(StringItem.class, DynamoDBStringItem.class);
        itemClassToDynamoItemClass.put(SwitchItem.class, DynamoDBBigDecimalItem.class);
        itemClassToDynamoItemClass.put(DimmerItem.class, DynamoDBBigDecimalItem.class); // inherited from SwitchItem (!)
        itemClassToDynamoItemClass.put(ColorItem.class, DynamoDBStringItem.class); // inherited from DimmerItem
    }

    public static final Class<? extends DynamoDBItem<?>> getDynamoItemClass(Class<? extends Item> itemClass)
            throws NullPointerException {
        Class<? extends DynamoDBItem<?>> dtoclass = itemClassToDynamoItemClass.get(itemClass);
        if (dtoclass == null) {
            throw new NullPointerException(String.format("Unknown item class %s", itemClass));
        }
        return dtoclass;
    }

    protected String name;
    protected T state;
    protected Date time;

    public AbstractDynamoDBItem(String name, T state, Date time) {
        this.name = name;
        this.state = state;
        this.time = time;
    }

    public static DynamoDBItem<?> fromState(String name, State state, Date time) {
        if (state instanceof DecimalType && !(state instanceof HSBType)) {
            // also covers PercentType which is inherited from DecimalType
            return new DynamoDBBigDecimalItem(name, ((DecimalType) state).toBigDecimal(), time);
        } else if (state instanceof OnOffType) {
            return new DynamoDBBigDecimalItem(name,
                    ((OnOffType) state) == OnOffType.ON ? BigDecimal.ONE : BigDecimal.ZERO, time);
        } else if (state instanceof OpenClosedType) {
            return new DynamoDBBigDecimalItem(name,
                    ((OpenClosedType) state) == OpenClosedType.OPEN ? BigDecimal.ONE : BigDecimal.ZERO, time);
        } else if (state instanceof UpDownType) {
            return new DynamoDBBigDecimalItem(name,
                    ((UpDownType) state) == UpDownType.UP ? BigDecimal.ONE : BigDecimal.ZERO, time);
        } else if (state instanceof DateTimeType) {
            return new DynamoDBStringItem(name, DATEFORMATTER.format(((DateTimeType) state).getCalendar().getTime()),
                    time);
        } else if (state instanceof UnDefType) {
            return new DynamoDBStringItem(name, UNDEFINED_PLACEHOLDER, time);
        } else if (state instanceof CallType) {
            // CallType.format method instead of toString since that matches the format expected by the String
            // constructor
            return new DynamoDBStringItem(name, state.format("%s##%s"), time);
        } else {
            // HSBType, PointType and StringType
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
                if (item instanceof ColorItem) {
                    state[0] = new HSBType(dynamoStringItem.getState());
                } else if (item instanceof LocationItem) {
                    state[0] = new PointType(dynamoStringItem.getState());
                } else if (item instanceof DateTimeItem) {
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(DATEFORMATTER.parse(dynamoStringItem.getState()));
                    } catch (ParseException e) {
                        LOGGER.error("Failed to parse {} as date. Outputting UNDEF instead",
                                dynamoStringItem.getState());
                        state[0] = UnDefType.UNDEF;
                    }
                    state[0] = new DateTimeType(cal);
                } else if (dynamoStringItem.getState().equals(UNDEFINED_PLACEHOLDER)) {
                    state[0] = UnDefType.UNDEF;
                } else if (item instanceof CallItem) {
                    String parts = dynamoStringItem.getState();
                    String[] strings = parts.split("##");
                    String dest = strings[0];
                    String orig = strings[1];
                    state[0] = new CallType(orig, dest);
                } else {
                    state[0] = new StringType(dynamoStringItem.getState());
                }
            }

            @Override
            public void visit(DynamoDBBigDecimalItem dynamoBigDecimalItem) {
                if (item instanceof NumberItem) {
                    state[0] = new DecimalType(dynamoBigDecimalItem.getState());
                } else if (item instanceof DimmerItem) {
                    state[0] = new PercentType(dynamoBigDecimalItem.getState());
                } else if (item instanceof SwitchItem) {
                    state[0] = dynamoBigDecimalItem.getState().compareTo(BigDecimal.ONE) == 0 ? OnOffType.ON
                            : OnOffType.OFF;
                } else if (item instanceof ContactItem) {
                    state[0] = dynamoBigDecimalItem.getState().compareTo(BigDecimal.ONE) == 0 ? OpenClosedType.OPEN
                            : OpenClosedType.CLOSED;
                } else if (item instanceof RollershutterItem) {
                    state[0] = new PercentType(dynamoBigDecimalItem.getState());
                } else {
                    LOGGER.warn("Not sure how to convert big decimal item {} to type {}. Using StringType as fallback",
                            dynamoBigDecimalItem.getName(), item.getClass());
                    state[0] = new StringType(dynamoBigDecimalItem.getState().toString());
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
