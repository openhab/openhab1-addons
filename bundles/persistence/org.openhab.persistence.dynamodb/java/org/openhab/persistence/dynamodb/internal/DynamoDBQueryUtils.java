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

import java.util.Collections;
import java.util.Date;

import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Operator;
import org.openhab.core.persistence.FilterCriteria.Ordering;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.collect.ImmutableMap;

public class DynamoDBQueryUtils {
    /**
     * Construct dynamodb query from filter
     *
     * @param filter
     * @return DynamoDBQueryExpression corresponding to the given FilterCriteria
     */
    public static DynamoDBQueryExpression<DynamoDBItem<?>> createQueryExpression(
            Class<? extends DynamoDBItem<?>> dtoClass, FilterCriteria filter) {
        DynamoDBItem<?> item = getDynamoDBHashKey(dtoClass, filter.getItemName());
        final DynamoDBQueryExpression<DynamoDBItem<?>> queryExpression = new DynamoDBQueryExpression<DynamoDBItem<?>>()
                .withHashKeyValues(item).withScanIndexForward(filter.getOrdering() == Ordering.ASCENDING)
                .withLimit(filter.getPageSize());
        maybeAddTimeFilter(queryExpression, filter);
        maybeAddStateFilter(filter, queryExpression);
        return queryExpression;
    }

    private static DynamoDBItem<?> getDynamoDBHashKey(Class<? extends DynamoDBItem<?>> dtoClass, String itemName) {
        DynamoDBItem<?> item;
        try {
            item = dtoClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        item.setName(itemName);
        return item;
    }

    private static void maybeAddStateFilter(FilterCriteria filter,
            final DynamoDBQueryExpression<DynamoDBItem<?>> queryExpression) {
        if (filter.getOperator() != null && filter.getState() != null) {
            // Convert filter's state to DynamoDBItem in order get suitable string representation for the state
            final DynamoDBItem<?> filterState = AbstractDynamoDBItem.fromState(filter.getItemName(), filter.getState(),
                    new Date());
            queryExpression.setFilterExpression(String.format("%s %s :opstate", DynamoDBItem.ATTRIBUTE_NAME_ITEMSTATE,
                    operatorAsString(filter.getOperator())));

            filterState.accept(new DynamoDBItemVisitor() {

                @Override
                public void visit(DynamoDBStringItem dynamoStringItem) {
                    queryExpression.setExpressionAttributeValues(
                            ImmutableMap.of(":opstate", new AttributeValue().withS(dynamoStringItem.getState())));
                }

                @Override
                public void visit(DynamoDBBigDecimalItem dynamoBigDecimalItem) {
                    queryExpression.setExpressionAttributeValues(ImmutableMap.of(":opstate",
                            new AttributeValue().withN(dynamoBigDecimalItem.getState().toPlainString())));
                }
            });

        }
    }

    private static Condition maybeAddTimeFilter(final DynamoDBQueryExpression<DynamoDBItem<?>> queryExpression,
            final FilterCriteria filter) {
        final Condition timeCondition = constructTimeCondition(filter);
        if (timeCondition != null) {
            queryExpression.setRangeKeyConditions(
                    Collections.singletonMap(DynamoDBItem.ATTRIBUTE_NAME_TIMEUTC, timeCondition));
        }
        return timeCondition;
    }

    private static Condition constructTimeCondition(FilterCriteria filter) {
        boolean hasBegin = filter.getBeginDate() != null;
        boolean hasEnd = filter.getEndDate() != null;

        final Condition timeCondition;
        if (!hasBegin && !hasEnd) {
            timeCondition = null;
        } else if (!hasBegin && hasEnd) {
            timeCondition = new Condition().withComparisonOperator(ComparisonOperator.LE).withAttributeValueList(
                    new AttributeValue().withS(AbstractDynamoDBItem.DATEFORMATTER.format(filter.getEndDate())));
        } else if (hasBegin && !hasEnd) {
            timeCondition = new Condition().withComparisonOperator(ComparisonOperator.GE).withAttributeValueList(
                    new AttributeValue().withS(AbstractDynamoDBItem.DATEFORMATTER.format(filter.getBeginDate())));
        } else {
            timeCondition = new Condition().withComparisonOperator(ComparisonOperator.BETWEEN).withAttributeValueList(
                    new AttributeValue().withS(AbstractDynamoDBItem.DATEFORMATTER.format(filter.getBeginDate())),
                    new AttributeValue().withS(AbstractDynamoDBItem.DATEFORMATTER.format(filter.getEndDate())));
        }
        return timeCondition;
    }

    /**
     * Convert op to string suitable for dynamodb filter expression
     *
     * @param op
     * @return string representation corresponding to the given the Operator
     */
    private static String operatorAsString(Operator op) {
        switch (op) {
            case EQ:
                return "=";
            case NEQ:
                return "<>";
            case LT:
                return "<";
            case LTE:
                return "<=";
            case GT:
                return ">";
            case GTE:
                return ">=";

            default:
                throw new IllegalStateException("Unknown operator " + op);
        }
    }
}
