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
import java.util.Date;

import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;

public abstract class AbstractDynamoItem<T> {

    protected String name;
    protected T state;
    protected Date time;

    public AbstractDynamoItem(String name, T state, Date time) {
        this.name = name;
        this.state = state;
        this.time = time;
    }

    public static AbstractDynamoItem<?> fromState(String name, State state, Date time) {
        if (state instanceof DecimalType) {
            return new DynamoBigDecimalItem(name, ((DecimalType) state).toBigDecimal(), time);
        } else if (state instanceof OnOffType) {
            return new DynamoIntegerItem(name, ((OnOffType) state) == OnOffType.ON ? 1 : 0, time);
        } else if (state instanceof OpenClosedType) {
            return new DynamoIntegerItem(name, ((OpenClosedType) state) == OpenClosedType.OPEN ? 1 : 0, time);
        } else if (state instanceof DateTimeType) {
            return new DynamoDateItem(name, ((DateTimeType) state).getCalendar().getTime(), time);
        } else {
            // HSBType, PointType and all others
            return new DynamoStringItem(name, state.toString(), time);
        }
    }

    public abstract String getName();

    public T getState() {
        return state;
    }

    public abstract Date getTime();

    public void setName(String name) {
        this.name = name;
    }

    public void setState(T state) {
        this.state = state;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public abstract void accept(DynamoItemVisitor visitor);

    @Override
    public String toString() {
        return DateFormat.getDateTimeInstance().format(time) + ": " + name + " -> " + state.toString();
    }

}
