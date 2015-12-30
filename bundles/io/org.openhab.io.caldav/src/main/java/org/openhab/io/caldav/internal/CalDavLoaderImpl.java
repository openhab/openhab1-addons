/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

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
import net.fortuna.ical4j.util.CompatibilityHints;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.CalDavQuery;
import org.openhab.io.caldav.EventNotifier;
import org.openhab.io.caldav.internal.EventStorage.CalendarRuntime;
import org.openhab.io.caldav.internal.EventStorage.EventContainer;
import org.openhab.io.caldav.internal.job.EventJob;
import org.openhab.io.caldav.internal.job.EventJob.EventTrigger;
import org.openhab.io.caldav.internal.job.EventReloaderJob;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;

/**
 * Loads all events from the configured calDAV servers. This is done with an
 * interval. All interesting events are hold in memory.
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 * 
 */
public class CalDavLoaderImpl extends AbstractActiveService implements
		ManagedService, CalDavLoader {
	private static final String JOB_NAME_EVENT_RELOADER = "event-reloader";
	private static final String JOB_NAME_EVENT_START = "event-start";
	private static final String JOB_NAME_EVENT_END = "event-end";
	private static final String PROP_RELOAD_INTERVAL = "reloadInterval";
	private static final String PROP_PRELOAD_TIME = "preloadTime";
	private static final String PROP_HISTORIC_LOAD_TIME = "historicLoadTime";
	private static final String PROP_URL = "url";
	private static final String PROP_PASSWORD = "password";
	private static final String PROP_USERNAME = "username";
	private static final String PROP_TIMEZONE = "timeZone";
	private static final String PROP_DISABLE_CERTIFICATE_VERIFICATION = "disableCertificateVerification";
	private DateTimeZone defaultTimeZone = DateTimeZone.getDefault();

	private static final Logger log = LoggerFactory
			.getLogger(CalDavLoaderImpl.class);
	public static final String CACHE_PATH = "etc/caldav";

	private ScheduledExecutorService execService;
	private List<EventNotifier> eventListenerList = new ArrayList<EventNotifier>();
	private final Scheduler scheduler;

	public static CalDavLoaderImpl INSTANCE;

	public CalDavLoaderImpl() {
		if (INSTANCE != null) {
			throw new IllegalStateException(
					"something went wrong, the loader service should be singleton");
		}
		INSTANCE = this;

		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.clear();
		} catch (SchedulerException e) {
			log.error("cannot get job-scheduler", e);
			throw new IllegalStateException("cannot get job-scheduler", e);
		}
		
	}

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			CompatibilityHints.setHintEnabled(
					CompatibilityHints.KEY_RELAXED_PARSING, true);

			// just temporary
			Map<String, CalDavConfig> configMap = new HashMap<String, CalDavConfig>();

			Enumeration<String> iter = config.keys();
			while (iter.hasMoreElements()) {
				String key = iter.nextElement();
				log.trace("configuration parameter: " + key);
				if (key.equals("service.pid")) {
					continue;
				} else if (key.equals(PROP_TIMEZONE)) {
					log.debug("overriding default timezone {} with {}",
							defaultTimeZone, config.get(key));
					defaultTimeZone = DateTimeZone.forID(config.get(key) + "");
					if (defaultTimeZone == null) {
						throw new ConfigurationException(PROP_TIMEZONE,
								"invalid timezone value: " + config.get(key));
					}
					log.debug("found timeZone: {}", defaultTimeZone);
					continue;
				}
				String[] keys = key.split(":");
				if (keys.length != 2) {
					throw new ConfigurationException(key, "unknown identifier");
				}
				String id = keys[0];
				String paramKey = keys[1];
				CalDavConfig calDavConfig = configMap.get(id);
				if (calDavConfig == null) {
					calDavConfig = new CalDavConfig();
					configMap.put(id, calDavConfig);
				}
				String value = config.get(key) + "";

				calDavConfig.setKey(id);
				if (paramKey.equals(PROP_USERNAME)) {
					calDavConfig.setUsername(value);
				} else if (paramKey.equals(PROP_PASSWORD)) {
					calDavConfig.setPassword(value);
				} else if (paramKey.equals(PROP_URL)) {
					calDavConfig.setUrl(value);
				} else if (paramKey.equals(PROP_RELOAD_INTERVAL)) {
					calDavConfig.setReloadMinutes(Integer.parseInt(value));
				} else if (paramKey.equals(PROP_PRELOAD_TIME)) {
					calDavConfig.setPreloadMinutes(Integer.parseInt(value));
				} else if (paramKey.equals(PROP_HISTORIC_LOAD_TIME)) {
					calDavConfig
							.setHistoricLoadMinutes(Integer.parseInt(value));
				} else if (paramKey
						.equals(PROP_DISABLE_CERTIFICATE_VERIFICATION)) {
					calDavConfig.setDisableCertificateVerification(BooleanUtils
							.toBoolean(value));
				}
			}

			// verify if all required parameters are set
			for (String id : configMap.keySet()) {
				if (configMap.get(id).getUrl() == null) {
					throw new ConfigurationException(PROP_URL, PROP_URL
							+ " must be set");
				}
				if (configMap.get(id).getUsername() == null) {
					throw new ConfigurationException(PROP_USERNAME,
							PROP_USERNAME + " must be set");
				}
				if (configMap.get(id).getPassword() == null) {
					throw new ConfigurationException(PROP_PASSWORD,
							PROP_PASSWORD + " must be set");
				}
				log.trace("config for id '{}': {}", id, configMap.get(id));
			}

			// initialize event cache
			for (CalDavConfig calDavConfig : configMap.values()) {
				final CalendarRuntime eventRuntime = new CalendarRuntime();
				eventRuntime.setConfig(calDavConfig);
				File cachePath = Util.getCachePath(calDavConfig.getKey());
				if (!cachePath.exists() && !cachePath.mkdirs()) {
					log.error("cannot create directory (" + CACHE_PATH
							+ ") for calendar caching (missing rights?)");
					continue;
				}
				EventStorage.getInstance().getEventCache()
						.put(calDavConfig.getKey(), eventRuntime);
			}

			setProperlyConfigured(true);
			this.startLoading();
		}
	}

	public List<EventNotifier> getEventListenerList() {
		return eventListenerList;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void addListener(EventNotifier notifier) {
		this.eventListenerList.add(notifier);

		// notify for missing changes
		for (CalendarRuntime calendarRuntime : EventStorage.getInstance()
				.getEventCache().values()) {
			for (EventContainer eventContainer : calendarRuntime.getEventMap()
					.values()) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					notifier.eventLoaded(event);
				}
			}
		}

	}

	public void removeListener(EventNotifier notifier) {
		this.eventListenerList.remove(notifier);
	}

	private synchronized void addEventToMap(EventContainer eventContainer,
			boolean createTimer) {
		CalendarRuntime calendarRuntime = EventStorage.getInstance()
				.getEventCache().get(eventContainer.getCalendarId());

		ConcurrentHashMap<String, EventContainer> eventContainerMap = calendarRuntime
				.getEventMap();

		if (eventContainerMap.containsKey(eventContainer.getEventId())) {
			EventContainer eventContainerOld = eventContainerMap
					.get(eventContainer.getEventId());
			// event is already in map
			if (eventContainer.getLastChanged().isAfter(
					eventContainerOld.getLastChanged())) {
				log.debug("event is already in event map and newer -> delete the old one, reschedule timer");
				// cancel old jobs
				for (String timerKey : eventContainerOld.getTimerMap()) {
					try {
						this.scheduler.deleteJob(JobKey.jobKey(timerKey));
					} catch (SchedulerException e) {
						log.error("cannot cancel event with job-id: "
								+ timerKey, e);
					}
				}
				eventContainerOld.getTimerMap().clear();

				// override event
				eventContainerMap.put(eventContainer.getEventId(),
						eventContainer);

				for (EventNotifier notifier : eventListenerList) {
					for (CalDavEvent event : eventContainerOld.getEventList()) {
						log.trace("notify listener... {}", notifier);
						try {
							notifier.eventRemoved(event);
						} catch (Exception e) {
							log.error("error while invoking listener", e);
						}
					}
				}
				for (EventNotifier notifier : eventListenerList) {
					for (CalDavEvent event : eventContainer.getEventList()) {
						log.trace("notify listener... {}", notifier);
						try {
							notifier.eventLoaded(event);
						} catch (Exception e) {
							log.error("error while invoking listener", e);
						}
					}
				}

				if (createTimer) {
					int index = 0;
					for (CalDavEvent event : eventContainer.getEventList()) {
						if (event.getEnd().isAfterNow()) {
							try {
								createJob(eventContainer, event, index);
							} catch (SchedulerException e) {
								log.error("cannot create jobs for event: "
										+ event.getShortName());
							}
						}
						index++;
					}
				}
			} else {
				// event is already in map and not updated, ignoring
			}
		} else {
			// event is new
			eventContainerMap.put(eventContainer.getEventId(), eventContainer);
			log.trace("listeners for events: {}", eventListenerList.size());
			for (EventNotifier notifier : eventListenerList) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					log.trace("notify listener... {}", notifier);
					try {
						notifier.eventLoaded(event);
					} catch (Exception e) {
						log.error("error while invoking listener", e);
					}
				}
			}
			if (createTimer) {
				int index = 0;
				for (CalDavEvent event : eventContainer.getEventList()) {
					if (event.getEnd().isAfterNow()) {
						try {
							createJob(eventContainer, event, index);
						} catch (SchedulerException e) {
							log.error("cannot create jobs for event: "
									+ event.getShortName());
						}
					}
					index++;
				}
			}
		}
	}

	private synchronized void createJob(final EventContainer eventContainer,
			final CalDavEvent event, final int index) throws SchedulerException {
		final String triggerStart = JOB_NAME_EVENT_START + "-"
				+ event.getShortName();
		Date startDate = event.getStart().toDate();
		JobDetail jobStart = JobBuilder
				.newJob()
				.ofType(EventJob.class)
				.usingJobData(EventJob.KEY_CONFIG,
						eventContainer.getCalendarId())
				.usingJobData(EventJob.KEY_EVENT, eventContainer.getEventId())
				.usingJobData(EventJob.KEY_REC_INDEX, index)
				.usingJobData(EventJob.KEY_EVENT_TRIGGER,
						EventTrigger.BEGIN.name()).build();
		Trigger jobTriggerStart = TriggerBuilder.newTrigger()
				.withIdentity(triggerStart, JOB_NAME_EVENT_START)
				.startAt(startDate).build();
		this.scheduler.scheduleJob(jobStart, jobTriggerStart);

		eventContainer.getTimerMap().add(triggerStart);
		log.debug("begin timer scheduled for event '{}' @ {}",
				event.getShortName(), startDate);

		final String triggerEnd = JOB_NAME_EVENT_END + "-"
				+ event.getShortName();
		Date endDate = event.getEnd().toDate();
		JobDetail jobEnd = JobBuilder
				.newJob()
				.ofType(EventJob.class)
				.usingJobData(EventJob.KEY_CONFIG,
						eventContainer.getCalendarId())
				.usingJobData(EventJob.KEY_EVENT, eventContainer.getEventId())
				.usingJobData(EventJob.KEY_REC_INDEX, index)
				.usingJobData(EventJob.KEY_EVENT_TRIGGER,
						EventTrigger.END.name()).build();
		Trigger jobTriggerEnd = TriggerBuilder.newTrigger()
				.withIdentity(triggerEnd, JOB_NAME_EVENT_END).startAt(endDate)
				.build();
		this.scheduler.scheduleJob(jobEnd, jobTriggerEnd);
		eventContainer.getTimerMap().add(triggerEnd);
		log.debug("end timer scheduled for event '{}' @ {}",
				event.getShortName(), endDate);
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
			final org.joda.time.DateTime lastResourceChange = new org.joda.time.DateTime(
					resource.getModified());

			log.trace("eventContainer found: {}", eventContainer != null);
			log.trace("last resource modification: {}", lastResourceChange);
			log.trace("last change of already loaded event: {}",
					eventContainer != null ? eventContainer.getLastChanged()
							: null);
			if (eventContainer != null
					&& !lastResourceChange.isAfter(eventContainer
							.getLastChanged())) {
				// check if some timers or single (from repeating events) have
				// to be created
				if (eventContainer.getCalculatedUntil().isAfter(
						org.joda.time.DateTime.now().plusMinutes(
								config.getReloadMinutes()))) {
					// the event is calculated as long as the next reload
					// interval can handle this
					log.trace("skipping resource {}, not changed",
							resource.getName());
					continue;
				}

				if (eventContainer.isHistoricEvent()) {
					// no more upcoming events, do nothing
					log.trace("skipping resource {}, not changed",
							resource.getName());
					continue;
				}

				File icsFile = Util.getCacheFile(config.getKey(), filename);
				if (icsFile != null && icsFile.exists()) {
					FileInputStream fis = new FileInputStream(icsFile);
					this.loadEvents(filename, lastResourceChange, fis, config,
							oldEventIds, false);
					fis.close();
					continue;
				}
			}

			log.debug("loading resource: {}", resource);

			URL url = new URL(config.getUrl());
			url = new URL(url.getProtocol(), url.getHost(), url.getPort(),
					resource.getPath());

			InputStream inputStream = sardine.get(url.toString().replaceAll(
					" ", "%20"));

			try {
				this.loadEvents(filename, lastResourceChange, inputStream, config,
						oldEventIds, false);
			} catch (ParserException e) {
				log.error("cannot load calendar entry: " + filename, e);
			}
		}
	}

	private void loadEvents(String filename,
			org.joda.time.DateTime lastChanged, final InputStream inputStream,
			final CalDavConfig config, final List<String> oldEventIds,
			boolean readFromFile) throws IOException, ParserException {
		CalendarBuilder builder = new CalendarBuilder();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(inputStream), 50);
//		String calContent = IOUtils.toString(in);
//		calContent = calContent.replace("\r", "\r\n");
//		log.info(calContent);
//		in.close();
		
		final UnfoldingReader uin = new UnfoldingReader(in, 50, true);
		Calendar calendar = builder.build(uin);
		uin.close();
		log.trace("calendar: {}", calendar);

		EventContainer eventContainer = new EventContainer(config.getKey());
		eventContainer.setFilename(filename);
		eventContainer.setLastChanged(lastChanged);

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
		for (CalendarComponent comp : vEventComponents) {
			VEvent vEvent = (VEvent) comp;
			log.trace("loading event: " + vEvent.getUid().getValue() + ":"
					+ vEvent.getSummary().getValue());

			PeriodList periods = vEvent.calculateRecurrenceSet(new Period(
					new DateTime(loadFrom.toDate()), new DateTime(loadTo
							.toDate())));

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
				org.joda.time.DateTime start = null;
				org.joda.time.DateTime end = null;

				if (p.getStart().getTimeZone() == null) {
					if (p.getStart().isUtc()) {
						log.trace("start is without timezone, but UTC");
						start = new org.joda.time.DateTime(p.getRangeStart(),
								DateTimeZone.UTC).toLocalDateTime().toDateTime(
								defaultTimeZone);
					} else {
						log.trace("start is without timezone, not UTC");
						start = new LocalDateTime(p.getRangeStart())
								.toDateTime();
					}
				} else if (DateTimeZone.getAvailableIDs().contains(
						p.getStart().getTimeZone().getID())) {
					log.trace("start is with known timezone: {}", p.getStart()
							.getTimeZone().getID());
					start = new org.joda.time.DateTime(p.getRangeStart(),
							DateTimeZone.forID(p.getStart().getTimeZone()
									.getID()));
				} else {
					// unknown timezone
					log.trace("start is with unknown timezone: {}", p
							.getStart().getTimeZone().getID());
					start = new org.joda.time.DateTime(p.getRangeStart(),
							defaultTimeZone);
				}

				if (p.getEnd().getTimeZone() == null) {
					if (p.getStart().isUtc()) {
						end = new org.joda.time.DateTime(p.getRangeEnd(),
								DateTimeZone.UTC).toLocalDateTime().toDateTime(
								defaultTimeZone);
					} else {
						end = new LocalDateTime(p.getRangeEnd()).toDateTime();
					}
				} else if (DateTimeZone.getAvailableIDs().contains(
						p.getEnd().getTimeZone().getID())) {
					end = new org.joda.time.DateTime(p.getRangeEnd(),
							DateTimeZone
									.forID(p.getEnd().getTimeZone().getID()));
				} else {
					// unknown timezone
					end = new org.joda.time.DateTime(p.getRangeEnd(),
							defaultTimeZone);
				}

				CalDavEvent event = new CalDavEvent(eventName, vEvent.getUid()
						.getValue(), config.getKey(), start, end);
				event.setLastChanged(lastChanged);
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
		addEventToMap(eventContainer, true);
		if (!readFromFile) {
			Util.storeToDisk(config.getKey(), filename, calendar);
		}
	}

	public void startLoading() {
		if (execService != null) {
			return;
		}
		log.trace("starting execution...");

		for (final CalendarRuntime eventRuntime : EventStorage.getInstance()
				.getEventCache().values()) {
			try {
				log.debug("reload cached events for config: {}", eventRuntime
						.getConfig().getKey());
				for (File fileCalendarKeys : new File(CACHE_PATH).listFiles()) {
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
									Util.getFilename(icsFile.getAbsolutePath()),
									new org.joda.time.DateTime(icsFile
											.lastModified()), fis, eventRuntime
											.getConfig(),
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
			}

			try {
				JobDetail job = JobBuilder
						.newJob()
						.ofType(EventReloaderJob.class)
						.usingJobData(EventReloaderJob.KEY_CONFIG,
								eventRuntime.getConfig().getKey()).build();
				SimpleTrigger jobTrigger = TriggerBuilder
						.newTrigger()
						.withIdentity(
								JOB_NAME_EVENT_RELOADER + "-"
										+ eventRuntime.getConfig().getKey(),
								JOB_NAME_EVENT_RELOADER)
						.startAt(new Date())
						.withSchedule(
								SimpleScheduleBuilder
										.repeatMinutelyForever(eventRuntime
												.getConfig().getReloadMinutes()))
						.build();
				this.scheduler.scheduleJob(job, jobTrigger);
			} catch (SchedulerException e) {
				log.error("cannot schedule calendar-reloader", e);
			}
		}
	}

	@Override
	protected void execute() {

	}

	@Override
	protected long getRefreshInterval() {
		return 1000;
	}

	@Override
	protected String getName() {
		return "CalDav Loader";
	}

	@Override
	public void addEvent(CalDavEvent calDavEvent) {
		final CalendarRuntime calendarRuntime = EventStorage.getInstance()
				.getEventCache().get(calDavEvent.getCalendarId());
		CalDavConfig config = calendarRuntime.getConfig();
		if (config == null) {
			log.error("cannot find config for calendar id: {}",
					calDavEvent.getCalendarId());
		}
		Sardine sardine = Util.getConnection(config);

		Calendar calendar = Util.createCalendar(calDavEvent,
				this.defaultTimeZone);

		try {
			final String fullIcsFile = config.getUrl() + "/"
					+ calDavEvent.getFilename() + ".ics";
			if (calendarRuntime.getEventContainerByFilename(calDavEvent
					.getFilename()) != null) {
				log.debug("event will be updated: {}", fullIcsFile);
				try {
					sardine.delete(fullIcsFile);
				} catch (IOException e) {
					log.error("cannot remove old ics file: {}", fullIcsFile);
				}
			} else {
				log.debug("event is new: {}", fullIcsFile);
			}

			sardine.put(fullIcsFile, calendar.toString().getBytes("UTF-8"));

			EventContainer eventContainer = new EventContainer(
					calDavEvent.getCalendarId());
			eventContainer.setEventId(calDavEvent.getId());
			eventContainer.setFilename(Util.getFilename(calDavEvent
					.getFilename()));
			eventContainer.getEventList().add(calDavEvent);
			eventContainer.setLastChanged(calDavEvent.getLastChanged());
			this.addEventToMap(eventContainer, false);
		} catch (UnsupportedEncodingException e) {
			log.error("cannot write event", e);
		} catch (IOException e) {
			log.error("cannot write event", e);
		}
	}

	@Override
	public List<CalDavEvent> getEvents(final CalDavQuery query) {
		log.trace("quering events for filter: {}", query);
		final ArrayList<CalDavEvent> eventList = new ArrayList<CalDavEvent>();

		if (query.getCalendarIds() != null) {
			for (String calendarId : query.getCalendarIds()) {
				final CalendarRuntime eventRuntime = EventStorage.getInstance()
						.getEventCache().get(calendarId);
				if (eventRuntime == null) {
					log.debug("calendar id {} not found", calendarId);
					continue;
				}

				for (EventContainer eventContainer : eventRuntime.getEventMap()
						.values()) {
					for (CalDavEvent calDavEvent : eventContainer
							.getEventList()) {
						if (query.getFrom() != null) {
							if (calDavEvent.getEnd().isBefore(query.getFrom())) {
								continue;
							}
						}
						if (query.getTo() != null) {
							if (calDavEvent.getStart().isAfter(query.getTo())) {
								continue;
							}
						}
						eventList.add(calDavEvent);
					}
				}
			}
		}

		if (query.getSort() != null) {
			Collections.sort(eventList, new Comparator<CalDavEvent>() {
				@Override
				public int compare(CalDavEvent arg0, CalDavEvent arg1) {
					if (query.getSort().equals(CalDavQuery.Sort.ASCENDING)) {
						return (int) (arg0.getStart().compareTo(arg1.getStart()));
					} else if (query.getSort().equals(
							CalDavQuery.Sort.DESCENDING)) {
						return (int) (arg1.getStart().compareTo(arg0.getStart()));
					} else {
						return 0;
					}

				}
			});
		}

		log.debug("return event list for {} with {} entries", query,
				eventList.size());
		return eventList;
	}

}
