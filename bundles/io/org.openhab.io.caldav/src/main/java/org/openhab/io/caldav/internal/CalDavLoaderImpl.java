/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.CompatibilityHints;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.EventNotifier;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineImpl;

/**
 * Loads all events from the configured calDAV servers.
 * This is done with an interval.
 * All interesting events are hold in memory.
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 * 
 */
public class CalDavLoaderImpl extends AbstractActiveService implements
		ManagedService, CalDavLoader {
	private static final String PROP_RELOAD_INTERVAL = "reloadInterval";
	private static final String PROP_PRELOAD_TIME = "preloadTime";
	private static final String PROP_URL = "url";
	private static final String PROP_PASSWORD = "password";
	private static final String PROP_USERNAME = "username";
	private static final String PROP_TIMEZONE = "timeZone";
	private static final String PROP_DISABLE_CERTIFICATE_VERIFICATION = "disableCertificateVerification";
	private DateTimeZone defaultTimeZone = DateTimeZone.getDefault();

	private static final Logger LOG = LoggerFactory
			.getLogger(CalDavLoaderImpl.class);

	private Map<String, CalDavConfig> configMap = new HashMap<String, CalDavConfig>();

	private ConcurrentHashMap<String, ConcurrentHashMap<String, CalDavEvent>> eventCache = new ConcurrentHashMap<String, ConcurrentHashMap<String, CalDavEvent>>();
	private ConcurrentHashMap<String, TimerTask> timerBeginMap = new ConcurrentHashMap<String, TimerTask>();
	private ConcurrentHashMap<String, TimerTask> timerEndMap = new ConcurrentHashMap<String, TimerTask>();
	private List<EventNotifier> eventListenerList = new ArrayList<EventNotifier>();
	
	
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			Enumeration<String> iter = config.keys();
			while (iter.hasMoreElements()) {
				String key = iter.nextElement();
				LOG.trace("configuration parameter: " + key);
				if (key.equals("service.pid")) {
					continue;
				} else if (key.equals(PROP_TIMEZONE)) {
					LOG.debug("overriding default timezone {} with {}", defaultTimeZone, config.get(key));
					defaultTimeZone = DateTimeZone.forID(config.get(key) + "");
					if (defaultTimeZone == null) {
						throw new ConfigurationException(PROP_TIMEZONE, "invalid timezone value: " + config.get(key));
					}
					LOG.debug("found timeZone: {}", defaultTimeZone);
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
				} else if (paramKey.equals(PROP_DISABLE_CERTIFICATE_VERIFICATION)) {
					calDavConfig.setDisableCertificateVerification(BooleanUtils.toBoolean(value));
				}
			}

			// verify if all required parameters are set
			for (String id : this.configMap.keySet()) {
				if (configMap.get(id).getUrl() == null) {
					throw new ConfigurationException(PROP_URL, PROP_URL + " must be set");
				}
				if (configMap.get(id).getUsername() == null) {
					throw new ConfigurationException(PROP_USERNAME, PROP_USERNAME + " must be set");
				}
				if (configMap.get(id).getPassword() == null) {
					throw new ConfigurationException(PROP_PASSWORD, PROP_PASSWORD + " must be set");
				}
				if (configMap.get(id).getReloadMinutes() == 0) {
					throw new ConfigurationException(PROP_RELOAD_INTERVAL, PROP_RELOAD_INTERVAL + " must be set");
				}
				if (configMap.get(id).getPreloadMinutes() == 0) {
					throw new ConfigurationException(PROP_PRELOAD_TIME, PROP_PRELOAD_TIME + " must be set");
				}
				LOG.trace("config for id '{}': {}", id, configMap.get(id));
			}

			setProperlyConfigured(true);
			this.startLoading();
		}
	}

	public void addListener(EventNotifier notifier) {
		this.eventListenerList.add(notifier);
		
		// notify for missing changes
		for (ConcurrentHashMap<String, CalDavEvent> eventMap : this.eventCache.values()) {
			for (CalDavEvent event : eventMap.values()) {
				notifier.eventRemoved(event);
				notifier.eventLoaded(event);
			}
		}
		
	}

	public void removeListener(EventNotifier notifier) {
		this.eventListenerList.remove(notifier);
	}

	private synchronized void addEventToMap(CalDavEvent event) {
		if (!this.eventCache.containsKey(event.getCalendarId())) {
			LOG.debug("creating event map for calendar: {}", event.getCalendarId());
			this.eventCache.put(event.getCalendarId(), new ConcurrentHashMap<String, CalDavEvent>());
		}
		
		ConcurrentHashMap<String, CalDavEvent> eventMap = this.eventCache.get(event.getCalendarId());
		
		if (eventMap.containsKey(event.getId())) {
			if (event.getLastChanged().isAfter(eventMap.get(event.getId()).getLastChanged())) {
				LOG.debug("event is already in event map and newer -> delete the old one, reschedule timer");
				// cancel old jobs
				if (timerBeginMap.contains(event.getId())) {
					timerBeginMap.get(event.getId()).cancel();
					timerBeginMap.remove(event.getId());
				}
				if (timerEndMap.contains(event.getId())) {
					timerEndMap.get(event.getId()).cancel();
					timerEndMap.remove(event.getId());
				}
				
				// override event
				eventMap.put(event.getId(), event);
				
				for (EventNotifier notifier : eventListenerList) {
					try {
						notifier.eventChanged(event);
					} catch (Exception e) {
						LOG.error("error while invoking listener", e);
					}
				}
				
				createJob(event);
			} else {
				// event is already in map and not updated, ignoring
			}
		} else {
			eventMap.put(event.getId(), event);
			LOG.trace("listeners for events: {}", eventListenerList.size());
			for (EventNotifier notifier : eventListenerList) {
				LOG.trace("notify listener... {}", notifier);
				try {
					notifier.eventLoaded(event);
				} catch (Exception e) {
					LOG.error("error while invoking listener", e);
				}
			}
			createJob(event);
		}
	}
	
	private synchronized void removeDeletedEvents(String eventMapKey, List<String> oldMap) {
		ConcurrentHashMap<String, CalDavEvent> eventMap = this.eventCache.get(eventMapKey);
		
		for (String eventId : oldMap) {
			CalDavEvent event = eventMap.get(eventId);
			LOG.debug("remove deleted event: {}", event.getShortName());
			eventMap.remove(eventId);
			// cancel old jobs
			if (timerBeginMap.contains(eventId)) {
				timerBeginMap.get(eventId).cancel();
				timerBeginMap.remove(eventId);
			}
			if (timerEndMap.contains(eventId)) {
				timerEndMap.get(eventId).cancel();
				timerEndMap.remove(eventId);
			}
			for (EventNotifier notifier : eventListenerList) {
				try {
					notifier.eventRemoved(event);
				} catch (Exception e) {
					LOG.error("error while invoking listener", e);
				}
			}
		}
	}
	
	private synchronized void createJob(final CalDavEvent event) {
		TimerTask timerTaskBegin = new TimerTask() {
			@Override
			public void run() {
				LOG.info("event start for: {}", event.getShortName());
				for (EventNotifier notifier : eventListenerList) {
					try {
						notifier.eventBegins(event);
					} catch (Exception e) {
						LOG.error("error while invoking listener", e);
					}
				}
				timerBeginMap.get(event.getId()).cancel();
				timerBeginMap.remove(event.getId());
			}
		};
		timerBeginMap.put(event.getId(), timerTaskBegin);
		Date startAsDate = event.getStart().toDate();
		new Timer().schedule(timerTaskBegin, startAsDate);
		LOG.debug("begin timer scheduled for event '{}' @ {}", event.getShortName(), startAsDate);
		
		TimerTask timerTaskEnd = new TimerTask() {
			@Override
			public void run() {
				LOG.info("event end for: {}", event.getShortName());
				for (EventNotifier notifier : eventListenerList) {
					try {
						notifier.eventEnds(event);
					} catch (Exception e) {
						LOG.error("error while invoking listener", e);
					}
				}
				timerEndMap.get(event.getId()).cancel();
				timerEndMap.remove(event.getId());
				
				eventCache.get(event.getCalendarId()).remove(event.getId());
			}
		};
		timerEndMap.put(event.getId(), timerTaskEnd);
		Date endAsDate = event.getEnd().toDate();
		new Timer().schedule(timerTaskEnd, endAsDate);
		LOG.debug("end timer scheduled for event '{}' @ {}", event.getShortName(), endAsDate);
	}
	
	private Sardine getConnection(CalDavConfig config) {
		if (config.isDisableCertificateVerification()) {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setHostnameVerifier(new AllowAllHostnameVerifier());
			try {
				httpClientBuilder.setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
				{
				    public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
				    {
				        return true;
				    }
				}).build());
			} catch (KeyManagementException e) {
				LOG.error("error verifying certificate", e);
			} catch (NoSuchAlgorithmException e) {
				LOG.error("error verifying certificate", e);
			} catch (KeyStoreException e) {
				LOG.error("error verifying certificate", e);
			}
			return new SardineImpl(httpClientBuilder, config.getUsername(), config.getPassword());
		} else {
			return new SardineImpl(config.getUsername(), config.getPassword());
		}
	}

	private synchronized List<CalDavEvent> loadEvents(CalDavConfig config, List<String> currentLoad)
			throws IOException, ParserException {
		List<CalDavEvent> eventList = new ArrayList<CalDavEvent>();

		Sardine sardine = getConnection(config);

		CompatibilityHints.setHintEnabled(
				CompatibilityHints.KEY_RELAXED_PARSING, true);
		
		List<DavResource> list = sardine.list(config.getUrl(), 1, false);

		for (DavResource resource : list) {
			if (resource.isDirectory()) {
				continue;
			}
			
			LOG.debug("loading resource: {}", resource);

			URL url = new URL(config.getUrl());
			url = new URL(url.getProtocol(), url.getHost(), url.getPort(),
					resource.getPath());

			InputStream inputStream = sardine.get(url.toString().replaceAll(
					" ", "%20"));

			CalendarBuilder builder = new CalendarBuilder();

			Calendar calendar = builder.build(inputStream);
			LOG.trace("calendar: {}", calendar);
			
//			DateTimeZone calendarsDateTimeZone = DateTimeZone.UTC;
//			CalendarComponent compTimeZone = calendar.getComponent("VTIMEZONE");
//			if (compTimeZone != null) {
//				calendarsDateTimeZone = DateTimeZone.forID(((VTimeZone) compTimeZone).getTimeZoneId().getValue());
//			}
			for (CalendarComponent comp : calendar.getComponents("VEVENT")) {
				LOG.trace("loading event: {}", comp.getName());
				if (comp instanceof VEvent) {
					VEvent vEvent = (VEvent) comp;
					LOG.trace("loading event: " + vEvent.getUid().getValue() + ":" + vEvent.getSummary().getValue());
					java.util.Calendar instance1 = java.util.Calendar
							.getInstance();
					instance1.add(java.util.Calendar.HOUR_OF_DAY, -1);
					Date startDate = instance1.getTime();

					PeriodList periods = vEvent
							.calculateRecurrenceSet(new Period(new DateTime(
									startDate), new Dur(0, 0, config.getPreloadMinutes(), 0)));

					for (Period p : periods) {
						if (p.getEnd().before(new Date())) {
							// date is already ended, ignoring
							continue;
						}
						
						org.joda.time.DateTime start = null;
						org.joda.time.DateTime end = null;
						
//						DateTimeZone dateTimeZone = defaultTimeZone;
						if (p.getStart().getTimeZone() == null) {
							if (p.getStart().isUtc()) {
								LOG.trace("start is without timezone, but UTC");
								start = new org.joda.time.DateTime(p.getRangeStart(), DateTimeZone.UTC).toLocalDateTime().toDateTime(defaultTimeZone);
							} else {
								LOG.trace("start is without timezone, not UTC");
								start = new LocalDateTime(p.getRangeStart()).toDateTime();
							}
						} else if (DateTimeZone.getAvailableIDs().contains(p.getStart().getTimeZone().getID())) {
							LOG.trace("start is with known timezone: {}", p.getStart().getTimeZone().getID());
							start = new org.joda.time.DateTime(p.getRangeStart(), DateTimeZone.forID(p.getStart().getTimeZone().getID()));
						} else {
							// unknown timezone
							LOG.trace("start is with unknown timezone: {}", p.getStart().getTimeZone().getID());
							start = new org.joda.time.DateTime(p.getRangeStart(), defaultTimeZone);
						}
						
						if (p.getEnd().getTimeZone() == null) {
							if (p.getStart().isUtc()) {
								end = new org.joda.time.DateTime(p.getRangeEnd(), DateTimeZone.UTC).toLocalDateTime().toDateTime(defaultTimeZone);
							} else {
								end = new LocalDateTime(p.getRangeEnd()).toDateTime();
							}
						} else if (DateTimeZone.getAvailableIDs().contains(p.getEnd().getTimeZone().getID())) {
							end = new org.joda.time.DateTime(p.getRangeEnd(), DateTimeZone.forID(p.getEnd().getTimeZone().getID()));
						} else {
							// unknown timezone
							end = new org.joda.time.DateTime(p.getRangeEnd(), defaultTimeZone);
						}
						
//						org.joda.time.DateTime eventStart = new org.joda.time.DateTime(p.getRangeStart(), dateTimeZone);
//						org.joda.time.DateTime eventEnd = new org.joda.time.DateTime(p.getRangeEnd(), dateTimeZone);
//						if (eventStart.getSecondOfDay() != 0) {
//							eventStart = eventStart.toDateTime(defaultTimeZone);
//						}
//						if (eventEnd.getSecondOfDay() != 0) {
//							eventEnd = eventEnd.toDateTime(defaultTimeZone);
//						}
						
						CalDavEvent event = new CalDavEvent(vEvent.getSummary()
								.getValue(), 
								vEvent.getUid().getValue(),
								config.getKey(), 
								start, 
								end
						);
						if (vEvent.getLastModified() != null) {
							event.setLastChanged(new org.joda.time.DateTime(vEvent.getLastModified().getDate()));
						} else {
							// set dummy date
							event.setLastChanged(new org.joda.time.DateTime(0));
						}
						if (vEvent.getLocation() != null) {
							event.setLocation(vEvent.getLocation().getValue());
						}
						if (vEvent.getDescription() != null) {
							event.setContent(vEvent.getDescription().getValue());
						}
						LOG.trace("adding event: " + event.getShortName());
						addEventToMap(event);
						currentLoad.add(event.getId());
					}

				}
			}
		}

		return eventList;
	}
	
	

	public void startLoading() {
		if (timerBeginMap.size() > 0
				|| timerEndMap.size() > 0) {
			return;
		}
		LOG.trace("starting execution...");

		ScheduledExecutorService execService = Executors.newScheduledThreadPool(configMap.values().size());
		for (final CalDavConfig config : configMap.values()) {
			execService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						LOG.debug("loading events for config: "
								+ config.getKey());
						ConcurrentHashMap<String, CalDavEvent> map = eventCache.get(config.getKey());
						List<String> oldMap = Collections.list(map != null ? map.keys() : Collections.enumeration(new ArrayList<String>()));
						List<String> loadedCalendarIds = new ArrayList<String>();
						loadEvents(config, loadedCalendarIds);
						oldMap.removeAll(loadedCalendarIds);
						// stop all events in oldMap
						removeDeletedEvents(config.getKey(), oldMap);
						printAllEvents();
					} catch (IOException e) {
						LOG.error("error while loading calendar entries: " + e.getMessage(), e);
					} catch (ParserException e) {
						LOG.error("error while loading calendar entries: " + e.getMessage(), e);
					}
				}
			}, 10, config.getReloadMinutes() * 60, TimeUnit.SECONDS);
		}
	}
	
	private synchronized void printAllEvents() {
		for (ConcurrentHashMap<String, CalDavEvent> map : this.eventCache.values()) {
			LOG.trace("------------ list " + map.size() + " -------------");
			for (CalDavEvent event : map.values()) {
				LOG.trace(event.getShortName());
			}
			LOG.trace("------------ list end ---------");
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
		CalDavConfig config = configMap.get(calDavEvent.getCalendarId());
		Sardine sardine = getConnection(config);
		
		String uid = UUID.randomUUID().toString();
		
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone(defaultTimeZone.getID());
		
		Calendar calendar = new Calendar();
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(new ProdId("openHAB caldav-persistence"));
		VEvent vEvent = new VEvent();
		vEvent.getProperties().add(new Summary(calDavEvent.getName()));
		vEvent.getProperties().add(new Description(calDavEvent.getContent()));
		final DtStart dtStart = new DtStart(new net.fortuna.ical4j.model.DateTime(calDavEvent.getStart().toDate()));
		dtStart.setTimeZone(timezone);
		vEvent.getProperties().add(dtStart);
		final DtEnd dtEnd = new DtEnd(new net.fortuna.ical4j.model.DateTime(calDavEvent.getEnd().toDate()));
		dtEnd.setTimeZone(timezone);
		vEvent.getProperties().add(dtEnd);
		vEvent.getProperties().add(new Uid(uid));
		vEvent.getProperties().add(Clazz.PUBLIC);
		calendar.getComponents().add(vEvent);
		
		try {
			sardine.put(config.getUrl() + "/openHAB-" + uid + ".ics", calendar.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOG.error("cannot write event", e);
		} catch (IOException e) {
			LOG.error("cannot write event", e);
		}
	}

	@Override
	public List<CalDavEvent> getEvents(String calendarId) {
		return new ArrayList<CalDavEvent>(eventCache.get(calendarId).values());
	}

}
