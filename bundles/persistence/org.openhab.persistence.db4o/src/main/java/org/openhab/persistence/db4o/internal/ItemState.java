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

package org.openhab.persistence.db4o.internal;

import java.text.DateFormat;
import java.util.Date;

import org.openhab.core.types.State;

import com.db4o.config.Configuration;

/**
 * This is a Java bean used to persist item states with timestamps in the database.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class ItemState {

	private String itemName;
	private State state;
	private Long timeStamp;
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		Date t = new Date(timeStamp);
		return DateFormat.getDateTimeInstance().format(t) + ": " + itemName + " -> "+ state.toString();
	}

	static /* default */ void configure(Configuration config) {
		config.objectClass(ItemState.class).objectField("itemName").indexed(true);
		config.objectClass(ItemState.class).objectField("timeStamp").indexed(true);

		config.objectClass(ItemState.class).cascadeOnUpdate(false);
		config.objectClass(ItemState.class).cascadeOnDelete(true);
}
}
