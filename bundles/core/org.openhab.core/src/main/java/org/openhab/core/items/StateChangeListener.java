/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.items;

import org.openhab.core.types.State;

/**
 * <p>This interface must be implemented by all classes that want to be notified
 * about changes in the state of an item.</p>
 * <p>The {@link GenericItem} class provides the possibility to register such
 * listeners.</p>
 * 
 * @author Kai Kreuzer
 *
 */
public interface StateChangeListener {
	
	/**
	 * This method is called, if a state has changed.
	 * 
	 * @param item the item whose state has changed
	 * @param oldState the previous state
	 * @param newState the new state
	 */
	public void stateChanged(Item item, State oldState, State newState);

	/**
	 * This method is called, if a state was updated, but has not changed
	 * 
	 * @param item the item whose state was updated
	 * @param state the current state, same before and after the update
	 */
	public void stateUpdated(Item item, State state);

}
