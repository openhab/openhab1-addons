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
