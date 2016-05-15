package org.openhab.persistence.dynamodb.internal;

import java.util.Date;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.HistoricItem;

public interface DynamoDBItem<T> {

    static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    String ATTRIBUTE_NAME_TIMEUTC = "timeutc";

    String ATTRIBUTE_NAME_ITEMNAME = "itemname";

    /**
     * Convert this AbstractDynamoItem as HistoricItem.
     *
     * @param item Item representing this item. Used to determine item type.
     * @return
     */
    HistoricItem asHistoricItem(Item item);

    String getName();

    T getState();

    Date getTime();

    void setName(String name);

    void setState(T state);

    void setTime(Date time);

    void accept(DynamoDBItemVisitor visitor);

}