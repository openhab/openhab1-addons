/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_presence.internal;


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.caldav_presence.CalDavPresenceBindingProvider;

import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.EventNotifier;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Robert Delbrück
 * @since 1.6.1
 */
public class CalDavPresenceBinding extends AbstractBinding<CalDavPresenceBindingProvider> implements ManagedService, EventNotifier {

	private static final String PARAM_HOME_IDENTIFIERS = "homeIdentifiers";

	private static final String PARAM_USED_CALENDARS = "usedCalendars";

	private static final Logger logger = 
		LoggerFactory.getLogger(CalDavPresenceBinding.class);
	
	private CalDavLoader calDavLoader;
	
	private List<String> calendars = new ArrayList<String>();
	private List<String> homeIdentifier = new ArrayList<String>();
	
	/**
	 * key=calendarId
	 */
	private Map<String, List<CalDavEvent>> activeEvents = new HashMap<String, List<CalDavEvent>>();
	
	public CalDavPresenceBinding() {
	}
	
	public void setCalDavLoader(CalDavLoader calDavLoader) {
		this.calDavLoader = calDavLoader;
		this.calDavLoader.addListener(this);
	}
	
	public void unsetCalDavLoader() {
		this.calDavLoader.removeListener(this);
		this.calDavLoader = null;
	}
	
	public void activate() {
		
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		if (properties == null) {
			logger.warn("no configuration found");
		} else {
			String usedCalendars = (String) properties.get(PARAM_USED_CALENDARS);
			if (usedCalendars != null) {
				for (String cal : usedCalendars.split(",")) {
					this.calendars.add(cal.trim());
				}
			}
			String homeIdentifiers = (String) properties.get(PARAM_HOME_IDENTIFIERS);
			if (homeIdentifiers != null) {
				for (String homeIdent : homeIdentifiers.split(",")) {
					this.homeIdentifier.add(homeIdent.trim().toLowerCase());
				}
			}
		}
	}

	@Override
	public void eventRemoved(CalDavEvent event) { }

	@Override
	public void eventLoaded(CalDavEvent event) { }

	@Override
	public void eventBegins(CalDavEvent event) {
		if (!calendars.contains(event.getCalendarId())) {
			return;
		}
		
		if (!this.activeEvents.containsKey(event.getCalendarId())) {
			this.activeEvents.put(event.getCalendarId(), new ArrayList<CalDavEvent>());
		}
		List<CalDavEvent> activeEventList = this.activeEvents.get(event.getCalendarId());
		activeEventList.add(event);
		
		doAction(event, OnOffType.ON);
	}

	@Override
	public void eventEnds(CalDavEvent event) {
		if (!calendars.contains(event.getCalendarId())) {
			return;
		}
		
		if (!this.activeEvents.containsKey(event.getCalendarId())) {
			this.activeEvents.put(event.getCalendarId(), new ArrayList<CalDavEvent>());
		}
		List<CalDavEvent> activeEventList = this.activeEvents.get(event.getCalendarId());
		activeEventList.remove(event);
		
		if (activeEventList.size() > 0) {
			logger.info("there are other active events for this calendar, do not set to OFF");
			return;
		}
		
		doAction(event, OnOffType.OFF);
	}
	
	private boolean homeIdentifierMatch(String location) {
		if (location == null) {
			return false;
		}
		
		boolean match = this.homeIdentifier.contains(location.toLowerCase());
		if (match) {
			return true;
		}
		
		for (String homeIdentifier : this.homeIdentifier) {
			if (location.contains(homeIdentifier)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void doAction(CalDavEvent event, OnOffType state) {
		if (!calendars.contains(event.getCalendarId())) {
			return;
		}
		
		if (this.homeIdentifierMatch(event.getLocation())) {
			logger.debug("event '{}' is @ home, do not change presence", event);
			return;
		}
		
		CalDavPresenceBindingProvider bindingProvider = null;
		for (CalDavPresenceBindingProvider bindingProvider_ : this.providers) {
			bindingProvider = bindingProvider_;
		}
		if (bindingProvider == null) {
			logger.error("no binding provider found");
			return;
		}
		
		for (String item : bindingProvider.getItemNames()) {
			CalDavPresenceConfig config = bindingProvider.getConfig(item);
			if (event.getCalendarId().equals(config.getId())) {
				eventPublisher.postUpdate(item, state);
			}
		}
	}

}
