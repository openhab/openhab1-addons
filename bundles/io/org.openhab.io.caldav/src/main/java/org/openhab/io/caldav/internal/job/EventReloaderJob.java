package org.openhab.io.caldav.internal.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.EventNotifier;
import org.openhab.io.caldav.internal.CalDavConfig;
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

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;

public class EventReloaderJob implements Job {
	public static final String KEY_CONFIG = "config";
	private static final Logger log = LoggerFactory
			.getLogger(EventReloaderJob.class);

	private static Map<String, Boolean> cachedEventsLoaded = new ConcurrentHashMap<String, Boolean>();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		final String config = context.getJobDetail().getJobDataMap()
				.getString(KEY_CONFIG);
		CalendarRuntime eventRuntime = EventStorage.getInstance()
				.getEventCache().get(config);
		
		// reload cached events (if necessary)
		if (!cachedEventsLoaded.containsKey(config)) {
			try {
				log.debug("reload cached events for config: {}", eventRuntime
						.getConfig().getKey());
				for (File fileCalendarKeys : new File(
						CalDavLoaderImpl.CACHE_PATH).listFiles()) {
					if (!eventRuntime
							.getConfig()
							.getKey()
							.equals(Util.getFilename(fileCalendarKeys.getName()))) {
						continue;
					}
					final Collection<File> icsFiles = FileUtils.listFiles(
							fileCalendarKeys, new String[] { "ics" }, false);
					for (File icsFile : icsFiles) {
						try {
							FileInputStream fis = new FileInputStream(icsFile);
							loadEvents(
											Util.getFilename(icsFile
													.getAbsolutePath()),
											new org.joda.time.DateTime(icsFile
													.lastModified()), fis,
											eventRuntime.getConfig(),
											new ArrayList<String>(), true);
						} catch (IOException e) {
							log.error(
									"cannot load events for file: " + icsFile,
									e);
						} catch (ParserException e) {
							log.error(
									"cannot load events for file: " + icsFile,
									e);
						}
					}
					break;
				}
			} catch (Throwable e) {
				log.error("cannot load events", e);
			} finally {
				cachedEventsLoaded.put(config, true);
			}
		}

		try {
			log.debug("loading events for config: " + config);
			List<String> oldEventIds = new ArrayList<String>();
			for (EventContainer eventContainer : eventRuntime.getEventMap()
					.values()) {
				oldEventIds.add(eventContainer.getFilename());
			}
			loadEvents(eventRuntime, oldEventIds);
			// stop all events in oldMap
			removeDeletedEvents(config, oldEventIds);

			for (EventNotifier notifier : CalDavLoaderImpl.instance
					.getEventListenerList()) {
				try {
					notifier.calendarReloaded(config);
				} catch (Exception e) {
					log.error("error while invoking listener", e);
				}
			}

			//printAllEvents();
		} catch (Exception e) {
			log.error(
					"error while loading calendar entries: " + e.getMessage(),
					e);
			throw new JobExecutionException(
					"error while loading calendar entries", e, false);
		}
	}

	private synchronized void removeDeletedEvents(String calendarKey,
			List<String> oldMap) {
		final CalendarRuntime eventRuntime = EventStorage.getInstance()
				.getEventCache().get(calendarKey);

		for (String filename : oldMap) {
			EventContainer eventContainer = eventRuntime
					.getEventContainerByFilename(filename);
			if (eventContainer == null) {
				log.error("cannot find event container for filename: {}",
						filename);
				continue;
			}

			// cancel old jobs
			for (String jobId : eventContainer.getTimerMap()) {
				try {
					String group;
					if (jobId.startsWith(CalDavLoaderImpl.JOB_NAME_EVENT_START)) {
						group = CalDavLoaderImpl.JOB_NAME_EVENT_START;
					} else if (jobId.startsWith(CalDavLoaderImpl.JOB_NAME_EVENT_END)) {
						group = CalDavLoaderImpl.JOB_NAME_EVENT_END;
					} else {
						throw new SchedulerException("unknown job id: " + jobId);
					}
					boolean deleteJob = CalDavLoaderImpl.instance
							.getScheduler().deleteJob(JobKey.jobKey(jobId, group));
					log.debug("old job ({}) deleted? {}", jobId, deleteJob);
				} catch (SchedulerException e) {
					log.error("cannot delete job '{}'", jobId);
				}
			}
			eventContainer.getTimerMap().clear();

			for (EventNotifier notifier : CalDavLoaderImpl.instance
					.getEventListenerList()) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					try {
						notifier.eventRemoved(event);
					} catch (Exception e) {
						log.error("error while invoking listener", e);
					}
				}
			}

			ConcurrentHashMap<String, EventContainer> eventContainerMap = eventRuntime
					.getEventMap();
			if (eventContainer != null) {
				this.removeFromDisk(eventContainer);

				log.debug("remove deleted event: {}",
						eventContainer.getEventId());
				eventContainerMap.remove(eventContainer.getEventId());
			}
		}
	}

	private void removeFromDisk(EventContainer eventContainer) {
		Util.getCacheFile(eventContainer.getCalendarId(),
				eventContainer.getFilename()).delete();
	}

	/**
	 * all events which are available must be removed from the oldEventIds list
	 * 
	 * @param calendarRuntime
	 * @param oldEventIds
	 * @throws IOException
	 * @throws ParserException
	 */
	public synchronized void loadEvents(final CalendarRuntime calendarRuntime,
			final List<String> oldEventIds) throws IOException, ParserException {
		CalDavConfig config = calendarRuntime.getConfig();

		Sardine sardine = Util.getConnection(config);

		List<DavResource> list = sardine.list(config.getUrl(), 1, false);

		for (DavResource resource : list) {
			if (resource.isDirectory()) {
				continue;
			}

			final String filename = Util.getFilename(resource.getName());
			oldEventIds.remove(filename);

			// must not be loaded
			EventContainer eventContainer = calendarRuntime
					.getEventContainerByFilename(filename);
			final org.joda.time.DateTime lastResourceChangeFS = new org.joda.time.DateTime(
					resource.getModified());

			log.trace("eventContainer found: {}", eventContainer != null);
			log.trace("last resource modification: {}", lastResourceChangeFS);
			log.trace("last change of already loaded event: {}",
					eventContainer != null ? eventContainer.getLastChanged()
							: null);
			if (config.isLastModifiedFileTimeStampValid()) {
				if (eventContainer != null
						&& !lastResourceChangeFS.isAfter(eventContainer
								.getLastChanged())) {
					// check if some timers or single (from repeating events) have
					// to be created
					if (eventContainer.getCalculatedUntil().isAfter(
							org.joda.time.DateTime.now().plusMinutes(
									config.getReloadMinutes()))) {
						// the event is calculated as long as the next reload
						// interval can handle this
						log.trace("skipping resource {}, not changed (calculated until: {})",
								resource.getName(), eventContainer.getCalculatedUntil());
						continue;
					}
	
					if (eventContainer.isHistoricEvent()) {
						// no more upcoming events, do nothing
						log.trace("skipping resource {}, not changed (historic)",
								resource.getName());
						continue;
					}
	
					File icsFile = Util.getCacheFile(config.getKey(), filename);
					if (icsFile != null && icsFile.exists()) {
						FileInputStream fis = new FileInputStream(icsFile);
						this.loadEvents(filename, lastResourceChangeFS, fis, config,
								oldEventIds, false);
						fis.close();
						continue;
					}
				}
			}

			log.debug("loading resource: {}", resource);

			URL url = new URL(config.getUrl());
			url = new URL(url.getProtocol(), url.getHost(), url.getPort(),
					resource.getPath());

			InputStream inputStream = sardine.get(url.toString().replaceAll(
					" ", "%20"));

			try {
				this.loadEvents(filename, lastResourceChangeFS, inputStream, config,
						oldEventIds, false);
			} catch (ParserException e) {
				log.error("cannot load calendar entry: " + filename, e);
			}
		}
	}

	public void loadEvents(String filename,
			org.joda.time.DateTime lastResourceChangeFS, final InputStream inputStream,
			final CalDavConfig config, final List<String> oldEventIds,
			boolean readFromFile) throws IOException, ParserException {
		CalendarBuilder builder = new CalendarBuilder();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(inputStream), 50);
		
		final UnfoldingReader uin = new UnfoldingReader(in, 50, true);
		Calendar calendar = builder.build(uin);
		uin.close();
		//log.trace("calendar: {}", calendar);

		EventContainer eventContainer = new EventContainer(config.getKey());
		eventContainer.setFilename(filename);
		eventContainer.setLastChanged(lastResourceChangeFS);

		org.joda.time.DateTime loadFrom = org.joda.time.DateTime.now()
				.minusMinutes(config.getHistoricLoadMinutes());
		org.joda.time.DateTime loadTo = org.joda.time.DateTime.now()
				.plusMinutes(config.getPreloadMinutes());

		final ComponentList<CalendarComponent> vEventComponents = calendar
				.getComponents(Component.VEVENT);
		if (vEventComponents.size() == 0) {
			// no events inside
			if (!readFromFile) {
				Util.storeToDisk(config.getKey(), filename, calendar);
			}
			return;
		}
		org.joda.time.DateTime lastModifedVEventOverAll = null;
		for (CalendarComponent comp : vEventComponents) {
			VEvent vEvent = (VEvent) comp;
			log.trace("loading event: " + vEvent.getUid().getValue() + ":"
					+ vEvent.getSummary().getValue());
			// fallback, because 'LastModified' in VEvent is optional
			org.joda.time.DateTime lastModifedVEvent = lastResourceChangeFS;
			if (vEvent.getLastModified() != null) {
				lastModifedVEvent = new org.joda.time.DateTime(vEvent.getLastModified().getDateTime());
			}
			
			if (!config.isLastModifiedFileTimeStampValid()) {
				if (lastModifedVEventOverAll == null || lastModifedVEvent.isAfter(lastModifedVEventOverAll)) {
					lastModifedVEventOverAll = lastModifedVEvent;
				}
				if (eventContainer != null
						&& !lastModifedVEvent.isBefore(eventContainer
								.getLastChanged())) {
					// check if some timers or single (from repeating events) have
					// to be created
					if (eventContainer.getCalculatedUntil() != null 
							&& vEventComponents.size() == 1
							&& eventContainer.getCalculatedUntil().isAfter(
							org.joda.time.DateTime.now().plusMinutes(
									config.getReloadMinutes()))) {
						// the event is calculated as long as the next reload
						// interval can handle this
						log.trace("skipping resource processing {}, not changed",
								filename);
						continue;
					}
	
					if (eventContainer.isHistoricEvent()) {
						// no more upcoming events, do nothing
						log.trace("skipping resource processing {}, not changed",
								filename);
						continue;
					}
				}
			}
			
			Period period = new Period(
					new DateTime(loadFrom.toDate()), new DateTime(loadTo
							.toDate()));
			PeriodList periods = vEvent.calculateRecurrenceSet(period);
			periods = periods.normalise();

			String eventId = vEvent.getUid().getValue();
			final String eventName = vEvent.getSummary().getValue();

			// no more upcoming events
			if (periods.size() > 0) {
				if (vEvent.getConsumedTime(
						new net.fortuna.ical4j.model.Date(),
						new net.fortuna.ical4j.model.Date(
								org.joda.time.DateTime.now().plusYears(10)
										.getMillis())).size() == 0) {
					log.trace("event will never be occur (historic): {}",
							eventName);
					eventContainer.setHistoricEvent(true);
				}
			}

			// expecting this is for every vEvent inside a calendar equals
			eventContainer.setEventId(eventId);

			eventContainer.setCalculatedUntil(loadTo);

			for (Period p : periods) {
				org.joda.time.DateTime start = getDateTime("start", p.getStart(), p.getRangeStart());
				org.joda.time.DateTime end = getDateTime("end", p.getEnd(), p.getRangeEnd());

				CalDavEvent event = new CalDavEvent(eventName, vEvent.getUid()
						.getValue(), config.getKey(), start, end);
				event.setLastChanged(lastModifedVEvent);
				if (vEvent.getLocation() != null) {
					event.setLocation(vEvent.getLocation().getValue());
				}
				if (vEvent.getDescription() != null) {
					event.setContent(vEvent.getDescription().getValue());
				}
				event.setFilename(filename);
				log.trace("adding event: " + event.getShortName());
				eventContainer.getEventList().add(event);

			}
		}
		if (lastModifedVEventOverAll != null && !config.isLastModifiedFileTimeStampValid()) {
			eventContainer.setLastChanged(lastModifedVEventOverAll);
		}
//		if (!eventContainer.getEventList().isEmpty()) {
			CalDavLoaderImpl.instance.addEventToMap(eventContainer, true);
			if (!readFromFile) {
				Util.storeToDisk(config.getKey(), filename, calendar);
			}
//		}
	}
	
	private org.joda.time.DateTime getDateTime(String dateType, DateTime date, Date rangeDate) {
		org.joda.time.DateTime start;
		if (date.getTimeZone() == null) {
			if (date.isUtc()) {
				log.trace("{} is without timezone, but UTC", dateType);
				start = new org.joda.time.DateTime(rangeDate,
						DateTimeZone.UTC).toLocalDateTime().toDateTime(
								CalDavLoaderImpl.defaultTimeZone);
			} else {
				log.trace("{} is without timezone, not UTC", dateType);
				start = new LocalDateTime(rangeDate)
						.toDateTime();
			}
		} else if (DateTimeZone.getAvailableIDs().contains(
				date.getTimeZone().getID())) {
			log.trace("{} is with known timezone: {}", dateType, date
					.getTimeZone().getID());
			start = new org.joda.time.DateTime(rangeDate,
					DateTimeZone.forID(date.getTimeZone()
							.getID()));
		} else {
			// unknown timezone
			log.trace("{} is with unknown timezone: {}", dateType, date.getTimeZone().getID());
			start = new org.joda.time.DateTime(rangeDate,
					CalDavLoaderImpl.defaultTimeZone);
		}
		return start;
	}
}
