package org.openhab.io.caldav.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.io.caldav.CalDavEvent;

public final class EventStorage {
	private static EventStorage instance;
	
	public static EventStorage getInstance() {
		if (instance == null) {
			instance = new EventStorage();
		}
		return instance;
	}
	
	private EventStorage() { }
	
	private ConcurrentHashMap<String, CalendarRuntime> eventCache = new ConcurrentHashMap<String, CalendarRuntime>();

	public ConcurrentHashMap<String, CalendarRuntime> getEventCache() {
		return eventCache;
	}
	
	/**
	 * Containing all events for a specific calendar and the config for the calendar.
	 * @author Robert
	 *
	 */
	public static class CalendarRuntime {
		private final ConcurrentHashMap<String, EventContainer> eventMap = new ConcurrentHashMap<String, EventContainer>();
		
		private CalDavConfig config;
		
		public EventContainer getEventContainerByFilename(String filename) {
			for (EventContainer eventContainer : eventMap.values()) {
				if (eventContainer.getFilename().equals(filename)) {
					return eventContainer;
				}
			}
			return null;
		}
		
		public ConcurrentHashMap<String, EventContainer> getEventMap() {
			return eventMap;
		}

		public CalDavConfig getConfig() {
			return config;
		}

		public void setConfig(CalDavConfig config) {
			this.config = config;
		}
	}
	
	/**
	 * A container for a event.
	 * Each event can have multiple occurrences.
	 * @author Robert Delbrück
	 *
	 */
	public static class EventContainer {
		private String calendarId;
		private String eventId;
		private org.joda.time.DateTime lastChanged;
		private String filename;
		private boolean historicEvent;
		private org.joda.time.DateTime calculatedUntil;
		
		private List<CalDavEvent> eventList = new ArrayList<CalDavEvent>();
		private final List<String> timerMap = new ArrayList<String>();
		
		public EventContainer() {
			super();
		}

		public EventContainer(String calendarId) {
			super();
			this.calendarId = calendarId;
		}

		public List<CalDavEvent> getEventList() {
			return eventList;
		}

		public void setEventList(List<CalDavEvent> eventList) {
			this.eventList = eventList;
		}

		public List<String> getTimerMap() {
			return timerMap;
		}
		
		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public String getCalendarId() {
			return calendarId;
		}

		public void setCalendarId(String calendarId) {
			this.calendarId = calendarId;
		}

		public org.joda.time.DateTime getLastChanged() {
			return lastChanged;
		}

		public void setLastChanged(org.joda.time.DateTime lastChanged) {
			this.lastChanged = lastChanged;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public boolean isHistoricEvent() {
			return historicEvent;
		}

		public void setHistoricEvent(boolean historicEvent) {
			this.historicEvent = historicEvent;
		}

		public org.joda.time.DateTime getCalculatedUntil() {
			return calculatedUntil;
		}

		public void setCalculatedUntil(org.joda.time.DateTime calculatedUntil) {
			this.calculatedUntil = calculatedUntil;
		}
	}
}
