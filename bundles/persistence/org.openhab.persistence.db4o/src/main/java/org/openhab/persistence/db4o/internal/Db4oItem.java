/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.db4o.internal;

import java.text.DateFormat;
import java.util.Date;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

import com.db4o.config.Configuration;

/**
 * This is a Java bean used to persist item states with timestamps in the database.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class Db4oItem implements HistoricItem {

	private String name;
	private State state;
	private Date timestamp;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance().format(timestamp) + ": " + name + " -> "+ state.toString();
	}

	static /* default */ void configure(Configuration config) {
		config.objectClass(Db4oItem.class).objectField("name").indexed(true);
		config.objectClass(Db4oItem.class).objectField("timestamp").indexed(true);

		config.objectClass(Db4oItem.class).cascadeOnUpdate(false);
		config.objectClass(Db4oItem.class).cascadeOnDelete(true);
	}

}
