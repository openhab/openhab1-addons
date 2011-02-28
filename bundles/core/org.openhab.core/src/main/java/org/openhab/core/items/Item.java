/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * <p>This interface defines the core features of an openHAB item.</p>
 * <p>Item instances are used for all stateful services and are especially
 * important for the {@link ItemRegistry}.</p>
 * 
 * @author Kai Kreuzer
 *
 */
public interface Item {

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

	/**
	 * tells, whether the item state has changed. This flag is evaluated by
	 * automation rules and automatically set back to false afterwards.
	 * 
	 * @return true, if the item state has changed since the last rule evaluation
	 */
	public boolean hasChanged();
	
	/**
	 * tells, whether the item state was updated. This flag is evaluated by
	 * automation rules and automatically set back to false afterwards.
	 * The difference to <code>hasChanged()</code> is that this method returns
	 * true on any update event, while <code>hasChanged()</code> only returns
	 * true, if the new state is different than the previous one.
	 * 
	 * @return true, if the item state was updated since the last rule evaluation
	 */
	boolean wasUpdated();

	/**
	 * <p>This method provides a list of all data types that can be used to update the item state</p>
	 * <p>Imagine e.g. a dimmer device: It's status could be 0%, 10%, 50%, 100%, but also OFF or ON and
	 * maybe UNDEFINED. So the  accepted data types would be in this case {@link PercentType}, {@link OnOffType}
	 * and {@link UnDefType}</p>
	 * 
	 * @return a list of data types that can be used to update the item state
	 */
	public List<Class<? extends State>> getAcceptedDataTypes();
	
	
	/**
	 * <p>This method provides a list of all command types that can be used for this item</p>
	 * <p>Imagine e.g. a dimmer device: You could ask it to dim to 0%, 10%, 50%, 100%, but 
	 * also to turn OFF or ON. So the  accepted command types would be in this case {@link PercentType},
	 * {@link OnOffType}</p>
	 * 
	 * @return a list of all command types that can be used for this item
	 */
	public List<Class<? extends Command>> getAcceptedCommandTypes();

}