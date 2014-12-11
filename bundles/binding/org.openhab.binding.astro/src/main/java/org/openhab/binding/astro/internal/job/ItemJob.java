/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
