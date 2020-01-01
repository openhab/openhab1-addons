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

import java.util.Date;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.HistoricItem;

/**
 * Represents openHAB Item serialized in a suitable format for the database
 *
 * @param <T> Type of the state as accepted by the AWS SDK.
 *
 * @author Sami Salonen
 */
public interface DynamoDBItem<T> {

    static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    static final String ATTRIBUTE_NAME_TIMEUTC = "timeutc";

    static final String ATTRIBUTE_NAME_ITEMNAME = "itemname";

    static final String ATTRIBUTE_NAME_ITEMSTATE = "itemstate";

    /**
     * Convert this AbstractDynamoItem as HistoricItem.
     *
     * @param item Item representing this item. Used to determine item type.
     * @return HistoricItem representing this DynamoDBItem.
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