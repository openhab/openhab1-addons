package org.openhab.io.caldav.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.fortuna.ical4j.data.ParserException;

import org.openhab.io.caldav.CalDavEvent;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class EventDownloaderJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		try {
//			LOG.debug("loading events for config: "
//					+ config.getKey());
//			ConcurrentHashMap<String, CalDavEvent> map = eventCache.get(config.getKey());
//			List<String> oldMap = Collections.list(map != null ? map.keys() : Collections.enumeration(new ArrayList<String>()));
//			List<String> loadedCalendarIds = new ArrayList<String>();
//			loadEvents(config, loadedCalendarIds);
//			oldMap.removeAll(loadedCalendarIds);
//			// stop all events in oldMap
//			removeDeletedEvents(config.getKey(), oldMap);
//			printAllEvents();
//		} catch (IOException e) {
//			LOG.error("error while loading calendar entries: " + e.getMessage(), e);
//		} catch (ParserException e) {
//			LOG.error("error while loading calendar entries: " + e.getMessage(), e);
//		}
	}

}
