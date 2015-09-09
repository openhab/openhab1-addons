/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.model;

import java.text.DateFormat;
import java.util.Date;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

/**
 * Represents the data on the part of OpenHab.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsItem implements HistoricItem {

	final private String name;
	final private State state;
	final private Date timestamp;

	public DbmsItem(String name, State state, Date timestamp) {
		this.name = name;
		this.state = state;
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public State getState() {
		return state;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance().format(timestamp) + ": " + name + " -> " + state.toString();
	}

}