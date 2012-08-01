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
package org.openhab.io.gcal.internal;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.util.AuthenticationException;


/**
 * The {@link GCalConnector} knows how to connect to the {@link CalendarService}
 * to download- or to create new calendar entries.
 *  
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class GCalConnector {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(GCalConnector.class);
	
	
	/**
	 * Connects to Google-Calendar Service and returns the specified Calender-Feed
	 * <code>url</code>, <code>username</code> and <code>password</code> are taken
	 * from the corresponding config parameter in <code>openhab.cfg</code>. 
	 * 
	 * @return the corresponding Calendar-Feed or <code>null</code> if an error
	 * occurs
	 */
	public static CalendarEventFeed downloadEventFeed() {
		if (StringUtils.isBlank(GCalConfiguration.username) || StringUtils.isBlank(GCalConfiguration.password) || StringUtils.isBlank(GCalConfiguration.url)) {
			logger.warn("username, password and url must not be blank -> gcal calendar login aborted");
			return null;
		}
		
		// TODO: teichsta: there could be more than one calender url in openHAB.cfg
		// for now we accept this limitation if downloading just one feed ...
		CalendarEventFeed feed = downloadEventFeed(
			GCalConfiguration.url, GCalConfiguration.username, GCalConfiguration.password);
		if (feed != null) {
			checkIfFullCalendarFeed(feed.getEntries());
		}
		
		return feed;
	}
		
	/**
	 * Connects to Google-Calendar Service and returns the specified Calendar-Feed.
	 * 
	 * @param url the {@link URL} of the full Google Calendar-Feed
	 * @param username
	 * @param password
	 * 
	 * @return the corresponding Calendar-Feed or <code>null</code> if an error
	 * occurs. <i>Note:</i> We do only return events if their startTime lies between
	 * <code>now</code> and <code>now + 2 * refreshInterval</code> to reduce
	 * the amount of events to process.
	 */
	protected static CalendarEventFeed downloadEventFeed(String url, String username, String password) {
		try {
			URL feedUrl = new URL(url);
			
			CalendarService myService = new CalendarService("openHAB");
				myService.setUserCredentials(username, password);
			CalendarQuery myQuery = new CalendarQuery(feedUrl);
				myQuery.setMinimumStartTime(DateTime.now());
				myQuery.setMaximumStartTime(new DateTime(DateTime.now().getValue() + (2 * GCalConfiguration.refreshInterval)));
	
			return myService.getFeed(myQuery, CalendarEventFeed.class);
		}
		catch (AuthenticationException ae) {
			logger.error("authentication failed: {}", ae.getMessage());
		}
		catch (Exception e) {
			logger.error("downloading CalenerEventFeed throws exception: {}", e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Checks the first {@link CalendarEventEntry} of <code>entries</code> for
	 * completeness. If this first event is incomplete all other events will be
	 * incomplete as well.
	 * 
	 * @param entries the set to check 
	 */
	private static void checkIfFullCalendarFeed(List<CalendarEventEntry> entries) {
		if (entries != null && !entries.isEmpty()) {
			CalendarEventEntry referenceEvent = entries.get(0);
			if (referenceEvent.getIcalUID() == null || referenceEvent.getTimes().isEmpty()) {
				logger.warn("calender entries are incomplete - please make sure to use the full calendar feed");
			}
			
		}
	}
	
	/**
	 * Creates a new calendar entry.
	 * 
	 * @param event the event to create in the remote calendar identified by the
	 * full calendar feed configured in </code>openhab.cfg</code>
	 * @return the newly created entry
	 */
	public static CalendarEventEntry createCalendarEvent(CalendarEventEntry event) {
		try {
			CalendarService myService = new CalendarService("openHAB");
				myService.setUserCredentials(GCalConfiguration.username, GCalConfiguration.password);
			URL feedUrl = new URL(GCalConfiguration.url);
				
			return myService.insert(feedUrl, event);
		}
		catch (AuthenticationException ae) {
			logger.error("authentication failed: {}", ae.getMessage());
		}
		catch (Exception e) {
			logger.error("creating a new calendar entry throws an exception: {}", e.getMessage());
		}
		
		return null;
	}	
	
	
}
