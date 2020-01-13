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

import org.junit.BeforeClass;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;

/**
 * 
 * @author Sami Salonen
 *
 */
public class ContactItemIntegrationTest extends AbstractTwoItemIntegrationTest {

    private static final String name = "contact";
    private static final OpenClosedType state1 = OpenClosedType.CLOSED;
    private static final OpenClosedType state2 = OpenClosedType.OPEN;
    // There is no OpenClosedType state value between CLOSED and OPEN.
    // Omit extended query tests AbstractTwoItemIntegrationTest by setting stateBetween to null.
    private static final OnOffType stateBetween = null;

    @BeforeClass
    public static void storeData() throws InterruptedException {
        ContactItem item = (ContactItem) items.get(name);
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
