/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.logging.internal;

import java.util.Date;

import org.openhab.core.items.Item;

import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * This is a logback event that takes an openHAB item
 * as a constructor parameter.
 * The provided item is used to define the logged information.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class ItemLoggingEvent extends LoggingEvent {

	public ItemLoggingEvent(Item item) {
		super();
		setLoggerName(item.getName());
		setMessage(item.getState().toString());
		setTimeStamp((new Date()).getTime());
	}
	
	@Override
	public String getLoggerName() {
		return super.getLoggerName();
	}
}
