/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.items;

import java.util.List;

import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * Group functions are used by active group items to calculate a state for the group
 * out of the states of all its member items.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 *
 */
abstract public interface GroupFunction {

	/**
	 * Determines the current state of a group based on a list of items
	 * 
	 * @param items the items to calculate a group state for
	 * @return the calculated group state
	 */
	public State calculate(List<Item> items);
	
	/**
	 * Calculates the group state and returns it as a state of the requested type.
	 * 
	 * @param items the items to calculate a group state for
	 * @param stateClass the type in which the state should be returned
	 * @return the calculated group state of the requested type or null, if type is not supported
	 */
	public State getStateAs(List<Item> items, Class<? extends State> stateClass);

	/**
	 * This is the default group function that does nothing else than to check if all member items
	 * have the same state. If this is the case, this state is returned, otherwise UNDEF is returned.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.7.0
	 *
	 */
	static class Equality implements GroupFunction {

		/**
		 * @{inheritDoc
		 */
		public State calculate(List<Item> items) {
			if(items.size()>0) {
				State state = items.get(0).getState(); 
				for(int i=1; i<items.size(); i++) {
					if(!state.equals(items.get(i).getState())) {
						return UnDefType.UNDEF;
					}
				}
				return state;
			} else {
				return UnDefType.UNDEF;
			}
		}

		/**
		 * @{inheritDoc
		 */
		public State getStateAs(List<Item> items,
				Class<? extends State> stateClass) {
			State state = calculate(items);
			if(stateClass.isInstance(state)) {
				return state;
			} else {
				return null;
			}
		}
	}

}
