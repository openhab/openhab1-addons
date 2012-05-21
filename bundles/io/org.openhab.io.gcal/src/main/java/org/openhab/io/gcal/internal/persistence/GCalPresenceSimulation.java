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
package org.openhab.io.gcal.internal.persistence;

import org.joda.time.DateTime;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.io.gcal.internal.GCalConfiguration;
import org.openhab.io.gcal.internal.GCalConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;


/**
 * This implementation of the {@link PersistenceService} provides Presence 
 * Simulation features based on the Google Calendar Service.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class GCalPresenceSimulation implements PersistenceService {

	private static final Logger logger =
		LoggerFactory.getLogger(GCalPresenceSimulation.class);
	
	private final static String COMMAND =
		"> if (PresenceSimulation.state == ON) sendCommand(%s,%s)";
	
	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "presencesimulation";
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		store(item, item.getName());
	}

	/**
	 * Creates a new Google Calendar Entry for each <code>item</code> being 
	 * stored. The entrys' title will either be the items name or 
	 * <code>alias</code> if this it is <code>!= null</code>.
	 * 
	 * The new Calendar Entry will contain a single command to be executed wich
	 * looks like<br>
	 * <p><code>send &lt;item.name&gt; &lt;item.state&gt;</code></p>
	 * 
	 * @param item the item which state should be persisted.
	 * @param alias the alias under which the item should be persisted.
	 */
	public void store(Item item, String alias) {
		if (GCalConfiguration.isInitialized()) {
			String newAlias = alias != null ? alias : item.getName();
			
			CalendarEventEntry myEntry = new CalendarEventEntry();
				myEntry.setTitle(new PlainTextConstruct("[PresenceSimulation] " + newAlias));
				myEntry.setContent(new PlainTextConstruct(
					String.format(COMMAND, item.getName(), item.getState().toString())));

			DateTime nowPlusOffset = new DateTime().plusDays(GCalConfiguration.offset);
			
			com.google.gdata.data.DateTime time = 
				com.google.gdata.data.DateTime.parseDateTime(nowPlusOffset.toString()); 
			When eventTimes = new When();
				eventTimes.setStartTime(time);
				eventTimes.setEndTime(time);
			myEntry.addTime(eventTimes);
			
			CalendarEventEntry createdEvent = 
					GCalConnector.createCalendarEvent(myEntry);
			logger.debug("succesfully created new calendar event (title='{}', date='{}')",
					createdEvent.getTitle().getPlainText(), 
					createdEvent.getTimes().get(0).getStartTime());
		}
	}
	
	
}
