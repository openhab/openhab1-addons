/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
