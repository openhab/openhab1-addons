package org.openhab.persistence.dynamodb.internal;

import java.text.DateFormat;
import java.util.Date;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

public class DynamoHistoricItem implements HistoricItem {
    final private String name;
    final private State state;
    final private Date timestamp;

    public DynamoHistoricItem(String name, State state, Date timestamp) {
        this.name = name;
        this.state = state;
        this.timestamp = timestamp;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return DateFormat.getDateTimeInstance().format(timestamp) + ": " + name + " -> " + state.toString();
    }
}
