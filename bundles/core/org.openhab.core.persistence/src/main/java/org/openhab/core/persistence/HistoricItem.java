/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence;

import java.util.Date;

import org.openhab.core.types.State;

/**
 * This interface is used by persistence services to represent an item
 * with a certain state at a given point in time.
 * 
 * <p>Note that this interface does not extend {@link Item} as the persistence
 * services could not provide an implementation that correctly implement
 * getAcceptedXTypes() and getGroupNames().</p>
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public interface HistoricItem {

	/**
	 * returns the timestamp of the persisted item
	 * 
	 * @return the timestamp of the item
	 */
	Date getTimestamp();

	/**
	 * returns the current state of the item
	 * 
	 * @return the current state
	 */
	public State getState();

	/**
	 * returns the name of the item
	 * 
	 * @return the name of the item
	 */
	public String getName();

}
