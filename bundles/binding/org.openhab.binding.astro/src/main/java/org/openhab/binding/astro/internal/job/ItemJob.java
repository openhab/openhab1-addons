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
package org.openhab.binding.astro.internal.job;

import org.openhab.core.library.types.OnOffType;
import org.quartz.JobDataMap;

/**
 * Publishes the item state ON/OFF for scheduled events.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ItemJob extends AbstractBaseJob {

    @Override
    protected void executeJob(JobDataMap jobDataMap) {
        String itemName = jobDataMap.getString("itemName");
        context.getEventPublisher().postUpdate(itemName, OnOffType.ON);
        context.getEventPublisher().postUpdate(itemName, OnOffType.OFF);
    }

}
