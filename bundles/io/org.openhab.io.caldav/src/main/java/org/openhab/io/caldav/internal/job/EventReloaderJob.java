package org.openhab.io.caldav.internal.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.EventNotifier;
import org.openhab.io.caldav.internal.CalDavLoaderImpl;
import org.openhab.io.caldav.internal.EventStorage;
import org.openhab.io.caldav.internal.EventStorage.CalendarRuntime;
import org.openhab.io.caldav.internal.EventStorage.EventContainer;
import org.openhab.io.caldav.internal.Util;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventReloaderJob implements Job {
	public static final String KEY_CONFIG = "config";
	private static final Logger log = LoggerFactory
			.getLogger(EventReloaderJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			final String config = context.getJobDetail().getJobDataMap().getString(KEY_CONFIG);
			
			CalendarRuntime eventRuntime = EventStorage.getInstance().getEventCache().get(config);
		
			log.debug("loading events for config: " + config);
			List<String> oldEventIds = new ArrayList<String>();
			for (EventContainer eventContainer : eventRuntime.getEventMap()
					.values()) {
				oldEventIds.add(eventContainer.getFilename());
			}
			CalDavLoaderImpl.INSTANCE.loadEvents(eventRuntime, oldEventIds);
			// stop all events in oldMap
			removeDeletedEvents(config, oldEventIds);

			for (EventNotifier notifier : CalDavLoaderImpl.INSTANCE.getEventListenerList()) {
				try {
					notifier.calendarReloaded(config);
				} catch (Exception e) {
					log.error("error while invoking listener", e);
				}
			}

			printAllEvents();
		} catch (Exception e) {
			log.error("error while loading calendar entries: " + e.getMessage(), e);
			throw new JobExecutionException("error while loading calendar entries", e, false);
		}
	}
	
	private synchronized void removeDeletedEvents(String calendarKey, List<String> oldMap) {
		final CalendarRuntime eventRuntime = EventStorage.getInstance().getEventCache().get(calendarKey);
		
		for (String filename : oldMap) {
			EventContainer eventContainer = eventRuntime.getEventContainerByFilename(filename);
			if (eventContainer == null) {
				log.error("cannot find event container for filename: {}", filename);
				continue;
			}
			
			// cancel old jobs
			for (String jobId : eventContainer.getTimerMap()) {
				try {
					CalDavLoaderImpl.INSTANCE.getScheduler().deleteJob(JobKey.jobKey(jobId));
				} catch (SchedulerException e) {
					log.error("cannot delete job '{}'", jobId);
				}
			}
			eventContainer.getTimerMap().clear();
			
			for (EventNotifier notifier : CalDavLoaderImpl.INSTANCE.getEventListenerList()) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					try {
						notifier.eventRemoved(event);
					} catch (Exception e) {
						log.error("error while invoking listener", e);
					}
				}
			}
			
			ConcurrentHashMap<String,EventContainer> eventContainerMap = eventRuntime.getEventMap();
			if (eventContainer != null) {
				this.removeFromDisk(eventContainer);
				
				log.debug("remove deleted event: {}", eventContainer.getEventId());
				eventContainerMap.remove(eventContainer.getEventId());
			}
		}
	}
	
	private void removeFromDisk(EventContainer eventContainer) {
		Util.getCacheFile(eventContainer.getCalendarId(), eventContainer.getFilename()).delete();
	}

	private synchronized void printAllEvents() {
		for (CalendarRuntime eventRuntime : EventStorage.getInstance().getEventCache().values()) {
			log.trace("------------ list " + eventRuntime.getEventMap().size() + " -------------");
			for (EventContainer eventContainer : eventRuntime.getEventMap().values()) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					log.trace(event.getShortName());
				}
			}
			log.trace("------------ list end ---------");
		}
	}
}
