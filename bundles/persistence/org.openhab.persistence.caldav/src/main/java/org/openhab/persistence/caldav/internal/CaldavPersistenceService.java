/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.caldav.internal;

import static org.openhab.persistence.caldav.internal.CaldavConfiguration.calendarId;
import static org.openhab.persistence.caldav.internal.CaldavConfiguration.duration;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a {@link PersistenceService} implementation using calDAV.
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 */
public class CaldavPersistenceService implements QueryablePersistenceService {

	private static final Logger logger = LoggerFactory.getLogger(CaldavPersistenceService.class);
	
	private static final String SERVICE_NAME = "caldav";
	
	private CalDavLoader calDavLoader;
	
	public String getName() {
		return SERVICE_NAME;
	}
	
	public void setCalDavLoader(CalDavLoader calDavLoader) {
		this.calDavLoader = calDavLoader;
	}
	
	public void unsetCalDavLoader() {
		this.calDavLoader = null;
	}
	
	public void activate() {
	}

	public void deactivate() {
	}
	

	public void store(Item item) {
		store(item, null);
	}

	public void store(Item item, String alias) {
		if(alias==null) alias = item.getName();
		
		CalDavEvent event = new CalDavEvent();
		event.setName(alias);
		event.setContent("BEGIN:" + alias + ":" + item.getState());
		DateTime now = DateTime.now();
		event.setStart(now);
		event.setEnd(now.plusMinutes(duration));
		event.setCalendarId(calendarId);
		this.calDavLoader.addEvent(event);
	}

	public Iterable<HistoricItem> query(FilterCriteria filter) {
		List<CalDavEvent> events = calDavLoader.getEvents(calendarId);
		
		
		return Collections.emptyList();
	}
}
