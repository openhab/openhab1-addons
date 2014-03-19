/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.drools.event;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openhab.core.items.Item;

/**
 * This is an abstract class that should be extended by all event classes that are used as facts in the rules.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 *
 */
abstract public class RuleEvent {

	protected String itemName;
	protected Item item;
	protected Calendar timestamp;

	public RuleEvent(Item item) {
		this.itemName = item.getName();
		this.item = item;
		this.timestamp = GregorianCalendar.getInstance();
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

}
