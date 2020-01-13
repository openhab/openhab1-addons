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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

/**
 * DynamoDBItem for items that can be serialized as DynamoDB string
 *
 * @author Sami Salonen
 */
@DynamoDBDocument
public class DynamoDBStringItem extends AbstractDynamoDBItem<String> {

    public DynamoDBStringItem() {
        this(null, null, null);
    }

    public DynamoDBStringItem(String name, String state, Date time) {
        super(name, state, time);
    }

    @DynamoDBAttribute(attributeName = DynamoDBItem.ATTRIBUTE_NAME_ITEMSTATE)
    @Override
    public String getState() {
        return state;
    }

    @DynamoDBHashKey(attributeName = DynamoDBItem.ATTRIBUTE_NAME_ITEMNAME)
    @Override
    public String getName() {
        return name;
    }

    @Override
    @DynamoDBRangeKey(attributeName = ATTRIBUTE_NAME_TIMEUTC)
    public Date getTime() {
        return time;
    }

    @Override
    public void accept(org.openhab.persistence.dynamodb.internal.DynamoDBItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void setTime(Date time) {
        this.time = time;
    }
}
