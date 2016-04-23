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
import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

@DynamoDBDocument
public class DynamoBigDecimalItem extends AbstractDynamoItem<BigDecimal> {

    public DynamoBigDecimalItem() {
        this(null, null, null);
    }

    public DynamoBigDecimalItem(String name, BigDecimal state, Date time) {
        super(name, state, time);
    }

    @DynamoDBAttribute(attributeName = "itemstate")
    @Override
    public BigDecimal getState() {
        return super.getState();
    }

    @DynamoDBHashKey(attributeName = "itemname")
    @Override
    public String getName() {
        return name;
    }

    @Override
    @DynamoDBRangeKey(attributeName = "timeutc")
    public Date getTime() {
        return time;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setState(BigDecimal state) {
        this.state = state;
    }

    @Override
    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public void accept(org.openhab.persistence.dynamodb.internal.DynamoItemVisitor visitor) {
        visitor.visit(this);
    }
}
