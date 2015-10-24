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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.CompatibilityHints;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
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
import org.openhab.io.caldav.CalDavQuery;
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
 * @since 1.8.0
 * 
 */
public class CalDavLoaderImpl extends AbstractActiveService implements
		ManagedService, CalDavLoader {
	private static final String PROP_RELOAD_INTERVAL = "reloadInterval";
	private static final String PROP_PRELOAD_TIME = "preloadTime";
	private static final String PROP_HISTORIC_LOAD_TIME = "historicLoadTime";
	private static final String PROP_URL = "url";
	private static final String PROP_PASSWORD = "password";
	private static final String PROP_USERNAME = "username";
	private static final String PROP_TIMEZONE = "timeZone";
	private static final String PROP_DISABLE_CERTIFICATE_VERIFICATION = "disableCertificateVerification";
	private DateTimeZone defaultTimeZone = DateTimeZone.getDefault();

	private static final Logger LOG = LoggerFactory
			.getLogger(CalDavLoaderImpl.class);
	private static final String CACHE_PATH = "etc/caldav";

	private ScheduledExecutorService execService;
	private ConcurrentHashMap<String, CalendarRuntime> eventCache = new ConcurrentHashMap<String, CalendarRuntime>();
	private List<EventNotifier> eventListenerList = new ArrayList<EventNotifier>();
	
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
				} else if (paramKey.equals(PROP_HISTORIC_LOAD_TIME)) {
					calDavConfig.setHistoricLoadMinutes(Integer.parseInt(value));
				} else if (paramKey.equals(PROP_DISABLE_CERTIFICATE_VERIFICATION)) {
					calDavConfig.setDisableCertificateVerification(BooleanUtils.toBoolean(value));
				}
			}

			// verify if all required parameters are set
			for (String id : configMap.keySet()) {
				if (configMap.get(id).getUrl() == null) {
					throw new ConfigurationException(PROP_URL, PROP_URL + " must be set");
				}
				if (configMap.get(id).getUsername() == null) {
					throw new ConfigurationException(PROP_USERNAME, PROP_USERNAME + " must be set");
				}
				if (configMap.get(id).getPassword() == null) {
					throw new ConfigurationException(PROP_PASSWORD, PROP_PASSWORD + " must be set");
				}
				LOG.trace("config for id '{}': {}", id, configMap.get(id));
			}
			
			// initialize event cache
			for (CalDavConfig calDavConfig : configMap.values()) {
				final CalendarRuntime eventRuntime = new CalendarRuntime();
				eventRuntime.setConfig(calDavConfig);
				getCachePath(calDavConfig.getKey()).mkdirs();
				this.eventCache.put(calDavConfig.getKey(), eventRuntime);
			}

			setProperlyConfigured(true);
			this.startLoading();
		}
	}

	public void addListener(EventNotifier notifier) {
		this.eventListenerList.add(notifier);
		
		// notify for missing changes
		for (CalendarRuntime calendarRuntime : this.eventCache.values()) {
			for (EventContainer eventContainer : calendarRuntime.getEventMap().values()) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					notifier.eventLoaded(event);
				}
			}
		}
		
	}
	
	public void removeListener(EventNotifier notifier) {
		this.eventListenerList.remove(notifier);
	}
	
	private void storeToDisk(String calendarId, String filename, Calendar calendar) {
		try {
			FileOutputStream fout = new FileOutputStream(getCacheFile(calendarId, filename));
			CalendarOutputter outputter = new CalendarOutputter();
			outputter.setValidating(false);
			outputter.output(calendar, fout);
			fout.flush();
			fout.close();
		} catch (IOException e) {
			LOG.error("cannot store event '{}' to disk", filename);
		} catch (ValidationException e) {
			LOG.error("cannot store event '{}' to disk", filename);
		}
	}
	
	private void removeFromDisk(EventContainer eventContainer) {
		getCacheFile(eventContainer.getCalendarId(), eventContainer.getFilename()).delete();
	}

	private synchronized void addEventToMap(EventContainer eventContainer, boolean createTimer) {
		CalendarRuntime calendarRuntime = this.eventCache.get(eventContainer.getCalendarId());
		
		ConcurrentHashMap<String, EventContainer> eventContainerMap = calendarRuntime.getEventMap();
		
		if (eventContainerMap.containsKey(eventContainer.getEventId())) {
			EventContainer eventContainerOld = eventContainerMap.get(eventContainer.getEventId());
			// event is already in map
			if (eventContainer.getLastChanged().isAfter(eventContainerOld.getLastChanged())) {
				LOG.debug("event is already in event map and newer -> delete the old one, reschedule timer");
				// cancel old jobs
				for (TimerTask timerTask : eventContainerOld.getTimerBeginMap()) {
					timerTask.cancel();
				}
				eventContainerOld.getTimerBeginMap().clear();
				for (TimerTask timerTask : eventContainerOld.getTimerEndMap()) {
					timerTask.cancel();
				}
				eventContainerOld.getTimerEndMap().clear();
				
				// override event
				eventContainerMap.put(eventContainer.getEventId(), eventContainer);
				
				for (EventNotifier notifier : eventListenerList) {
					for (CalDavEvent event : eventContainerOld.getEventList()) {
						LOG.trace("notify listener... {}", notifier);
						try {
							notifier.eventRemoved(event);
						} catch (Exception e) {
							LOG.error("error while invoking listener", e);
						}
					}
				}
				for (EventNotifier notifier : eventListenerList) {
					for (CalDavEvent event : eventContainer.getEventList()) {
						LOG.trace("notify listener... {}", notifier);
						try {
							notifier.eventLoaded(event);
						} catch (Exception e) {
							LOG.error("error while invoking listener", e);
						}
					}
				}
				
				if (createTimer) {
					for (CalDavEvent event : eventContainer.getEventList()) {
						if (event.getEnd().isAfterNow()) {
							createJob(eventContainer, event);
						}
					}
				}
			} else {
				// event is already in map and not updated, ignoring
			}
		} else {
			// event is new
			eventContainerMap.put(eventContainer.getEventId(), eventContainer);
			LOG.trace("listeners for events: {}", eventListenerList.size());
			for (EventNotifier notifier : eventListenerList) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					LOG.trace("notify listener... {}", notifier);
					try {
						notifier.eventLoaded(event);
					} catch (Exception e) {
						LOG.error("error while invoking listener", e);
					}
				}
			}
			if (createTimer) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					if (event.getEnd().isAfterNow()) {
						createJob(eventContainer, event);
					}
				}
			}
		}
	}
	
	private synchronized void removeDeletedEvents(String calendarKey, List<String> oldMap) {
		final CalendarRuntime eventRuntime = this.eventCache.get(calendarKey);
		
		for (String filename : oldMap) {
			EventContainer eventContainer = eventRuntime.getEventContainerByFilename(filename);
			if (eventContainer == null) {
				LOG.error("cannot find event container for filename: {}", filename);
				continue;
			}
			
			// cancel old jobs
			for (TimerTask timerTask : eventContainer.getTimerBeginMap()) {
				timerTask.cancel();
			}
			eventContainer.getTimerBeginMap().clear();
			for (TimerTask timerTask : eventContainer.getTimerEndMap()) {
				timerTask.cancel();
			}
			eventContainer.getTimerEndMap().clear();
			
			for (EventNotifier notifier : eventListenerList) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					try {
						notifier.eventRemoved(event);
					} catch (Exception e) {
						LOG.error("error while invoking listener", e);
					}
				}
			}
			
			ConcurrentHashMap<String,EventContainer> eventContainerMap = eventRuntime.getEventMap();
			if (eventContainer != null) {
				this.removeFromDisk(eventContainer);
				
				LOG.debug("remove deleted event: {}", eventContainer.getEventId());
				eventContainerMap.remove(eventContainer.getEventId());
			}
		}
	}
	
	private synchronized void createJob(final EventContainer eventContainer, 
			final CalDavEvent event) {
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
			}
		};
		eventContainer.getTimerBeginMap().add(timerTaskBegin);
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
				
				eventCache.get(event.getCalendarId()).getEventMap().remove(eventContainer.getEventId());
			}
		};
		eventContainer.getTimerEndMap().add(timerTaskEnd);
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
			if (StringUtils.isEmpty(config.getUsername()) && StringUtils.isEmpty(config.getPassword())) {
				return new SardineImpl(httpClientBuilder);
			} else {
				return new SardineImpl(httpClientBuilder, config.getUsername(), config.getPassword());
			}
		} else {
			if (StringUtils.isEmpty(config.getUsername()) && StringUtils.isEmpty(config.getPassword())) {
				return new SardineImpl();
			} else {
				return new SardineImpl(config.getUsername(), config.getPassword());
			}
		}
	}

	/**
	 * all events which are available must be removed from the oldEventIds list
	 * @param calendarRuntime
	 * @param oldEventIds
	 * @throws IOException
	 * @throws ParserException
	 */
	private synchronized void loadEvents(final CalendarRuntime calendarRuntime, final List<String> oldEventIds)
			throws IOException, ParserException {
		CalDavConfig config = calendarRuntime.getConfig();

		Sardine sardine = getConnection(config);

		List<DavResource> list = sardine.list(config.getUrl(), 1, false);

		for (DavResource resource : list) {
			if (resource.isDirectory()) {
				continue;
			}
			
			final String filename = getFilename(resource.getName());
			oldEventIds.remove(filename);
			
			// must not be loaded
			EventContainer eventContainer = calendarRuntime.getEventContainerByFilename(filename);
			final org.joda.time.DateTime lastResourceChange = new org.joda.time.DateTime(resource.getModified());
			
			LOG.trace("eventContainer found: {}", eventContainer != null);
			LOG.trace("last resource modification: {}", lastResourceChange);
			LOG.trace("last change of already loaded event: {}", eventContainer != null ? eventContainer.getLastChanged() : null);
			if (eventContainer != null 
					&& !lastResourceChange.isAfter(eventContainer.getLastChanged())) {
				// check if some timers or single (from repeating events) have to be created
				if (eventContainer.getCalculatedUntil().isAfter(org.joda.time.DateTime.now().plusMinutes(config.getReloadMinutes()))) {
					// the event is calculated as long as the next reload interval can handle this
					LOG.trace("skipping resource {}, not changed", resource.getName());
					continue;
				}
				
				if (eventContainer.isHistoricEvent()) {
					// no more upcoming events, do nothing
					LOG.trace("skipping resource {}, not changed", resource.getName());
					continue;
				}
				
				File icsFile = getCacheFile(config.getKey(), filename);
				if (icsFile != null && icsFile.exists()) {
					FileInputStream fis = new FileInputStream(icsFile);
					this.loadEvents(filename, lastResourceChange, fis, config, oldEventIds, false);
					fis.close();
					continue;
				}
			}
			
			LOG.debug("loading resource: {}", resource);

			URL url = new URL(config.getUrl());
			url = new URL(url.getProtocol(), url.getHost(), url.getPort(),
					resource.getPath());

			InputStream inputStream = sardine.get(url.toString().replaceAll(
					" ", "%20"));

			this.loadEvents(filename, lastResourceChange, inputStream, config, oldEventIds, false);
		}
	}
	
	private String getFilename(String name) {
		name = FilenameUtils.getBaseName(name);
		name = name.replaceAll("[^a-zA-Z0-9-_]", "_");
		return name;
	}
	
	private void loadEvents(String filename, org.joda.time.DateTime lastChanged, 
			final InputStream inputStream, final CalDavConfig config, 
			final List<String> oldEventIds, boolean readFromFile) throws IOException, ParserException {
		CalendarBuilder builder = new CalendarBuilder();
		final UnfoldingReader uin = new UnfoldingReader(new BufferedReader(new InputStreamReader(inputStream), 50), 50, true);
		Calendar calendar = builder.build(uin);
		uin.close();
		LOG.trace("calendar: {}", calendar);
		
		EventContainer eventContainer = new EventContainer(config.getKey());
		eventContainer.setFilename(filename);
		eventContainer.setLastChanged(lastChanged);
		
		org.joda.time.DateTime loadFrom = org.joda.time.DateTime.now().minusMinutes(config.getHistoricLoadMinutes());
		org.joda.time.DateTime loadTo = org.joda.time.DateTime.now().plusMinutes(config.getPreloadMinutes());
		
		final ComponentList<CalendarComponent> vEventComponents = calendar.getComponents(Component.VEVENT);
		if (vEventComponents.size() == 0) {
			// no events inside
			if (!readFromFile) {
				storeToDisk(config.getKey(), filename, calendar);
			}
			return;
		}
		for (CalendarComponent comp : vEventComponents) {
			VEvent vEvent = (VEvent) comp;
			LOG.trace("loading event: " + vEvent.getUid().getValue() + ":" + vEvent.getSummary().getValue());
			
			PeriodList periods = vEvent
					.calculateRecurrenceSet(new Period(new DateTime(
							loadFrom.toDate()), new DateTime(loadTo.toDate())));
			
			String eventId = vEvent.getUid().getValue();
			final String eventName = vEvent.getSummary().getValue();
			
			// no more upcoming events
			if (periods.size() > 0) {
				if (vEvent.getConsumedTime(
						new net.fortuna.ical4j.model.Date(), 
						new net.fortuna.ical4j.model.Date(org.joda.time.DateTime.now().plusYears(10).getMillis())).size() == 0
						) {
					LOG.trace("event will never be occur (historic): {}", eventName);
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
				
				CalDavEvent event = new CalDavEvent(eventName, 
						vEvent.getUid().getValue(),
						config.getKey(), 
						start, 
						end
				);
				event.setLastChanged(lastChanged);
				if (vEvent.getLocation() != null) {
					event.setLocation(vEvent.getLocation().getValue());
				}
				if (vEvent.getDescription() != null) {
					event.setContent(vEvent.getDescription().getValue());
				}
				event.setFilename(filename);
				LOG.trace("adding event: " + event.getShortName());
				eventContainer.getEventList().add(event);
				
			}
		}
		addEventToMap(eventContainer, true);
		if (!readFromFile) {
			storeToDisk(config.getKey(), filename, calendar);
		}
	}

	public void startLoading() {
		if (execService != null) {
			return;
		}
		LOG.trace("starting execution...");
		
		execService = Executors.newScheduledThreadPool(eventCache.size());
		for (final CalendarRuntime eventRuntime : eventCache.values()) {
			try {
				LOG.debug("reload cached events for config: {}", eventRuntime.getConfig().getKey());
				for (File fileCalendarKeys : new File(CACHE_PATH).listFiles()) {
					if (!eventRuntime.getConfig().getKey().equals(getFilename(fileCalendarKeys.getName()))) {
						continue;
					}
					final Collection<File> icsFiles = FileUtils.listFiles(fileCalendarKeys, new String[]{"ics"}, false);
					for (File icsFile : icsFiles) {
						try {
							FileInputStream fis = new FileInputStream(icsFile);
							loadEvents(getFilename(icsFile.getAbsolutePath()), new org.joda.time.DateTime(icsFile.lastModified()), fis, eventRuntime.getConfig(), new ArrayList<String>(), true);
						} catch (IOException e) {
							LOG.error("cannot load events for file: " + icsFile, e);
						} catch (ParserException e) {
							LOG.error("cannot load events for file: " + icsFile, e);
						}
					}
					break;
				}
			} catch (Error e) {
				LOG.error("cannot load events", e);
			}
			
			execService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						LOG.debug("loading events for config: "
								+ eventRuntime.getConfig().getKey());
						List<String> oldEventIds = new ArrayList<String>();
						for (EventContainer eventContainer : eventRuntime.getEventMap().values()) {
							oldEventIds.add(eventContainer.getFilename());
						}
						loadEvents(eventRuntime, oldEventIds);
						// stop all events in oldMap
						removeDeletedEvents(eventRuntime.getConfig().getKey(), oldEventIds);
						
						for (EventNotifier notifier : eventListenerList) {
							try {
								notifier.calendarReloaded(eventRuntime.getConfig().getKey());
							} catch (Exception e) {
								LOG.error("error while invoking listener", e);
							}
						}
						
						printAllEvents();
					} catch (IOException e) {
						LOG.error("error while loading calendar entries: " + e.getMessage(), e);
					} catch (ParserException e) {
						LOG.error("error while loading calendar entries: " + e.getMessage(), e);
					} catch (Error e) {
						LOG.error("error while loading calendar entries: " + e.getMessage(), e);
					}
				}
			}, 10, eventRuntime.getConfig().getReloadMinutes() * 60, TimeUnit.SECONDS);
		}
	}
	
	private synchronized void printAllEvents() {
		for (CalendarRuntime eventRuntime : this.eventCache.values()) {
			LOG.trace("------------ list " + eventRuntime.getEventMap().size() + " -------------");
			for (EventContainer eventContainer : eventRuntime.getEventMap().values()) {
				for (CalDavEvent event : eventContainer.getEventList()) {
					LOG.trace(event.getShortName());
				}
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
		final CalendarRuntime calendarRuntime = eventCache.get(calDavEvent.getCalendarId());
		CalDavConfig config = calendarRuntime.getConfig();
		if (config == null) {
			LOG.error("cannot find config for calendar id: {}", calDavEvent.getCalendarId());
		}
		Sardine sardine = getConnection(config);
		
		Calendar calendar = this.createCalendar(calDavEvent);
		
		try {
			final String fullIcsFile = config.getUrl() + "/" + calDavEvent.getFilename() + ".ics";
			if (calendarRuntime.getEventContainerByFilename(calDavEvent.getFilename()) != null) {
				LOG.debug("event will be updated: {}", fullIcsFile);
				try {
					sardine.delete(fullIcsFile);
				} catch (IOException e) {
					LOG.error("cannot remove old ics file: {}", fullIcsFile);
				}
			} else {
				LOG.debug("event is new: {}", fullIcsFile);
			}
			
			sardine.put(fullIcsFile, calendar.toString().getBytes("UTF-8"));
			
			EventContainer eventContainer = new EventContainer(calDavEvent.getCalendarId());
			eventContainer.setEventId(calDavEvent.getId());
			eventContainer.setFilename(getFilename(calDavEvent.getFilename()));
			eventContainer.getEventList().add(calDavEvent);
			eventContainer.setLastChanged(calDavEvent.getLastChanged());
			this.addEventToMap(eventContainer, false);
		} catch (UnsupportedEncodingException e) {
			LOG.error("cannot write event", e);
		} catch (IOException e) {
			LOG.error("cannot write event", e);
		}
	}

	@Override
	public List<CalDavEvent> getEvents(final CalDavQuery query) {
		LOG.trace("quering events for filter: {}", query);
		final ArrayList<CalDavEvent> eventList = new ArrayList<CalDavEvent>();
		
		if (query.getCalendarIds() != null) {
			for (String calendarId : query.getCalendarIds()) {
				final CalendarRuntime eventRuntime = eventCache.get(calendarId);
				if (eventRuntime == null) {
					LOG.debug("calendar id {} not found", calendarId);
					continue;
				}
				
				for (EventContainer eventContainer : eventRuntime.getEventMap().values()) {
					for (CalDavEvent calDavEvent : eventContainer.getEventList()) {
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
					} else if (query.getSort().equals(CalDavQuery.Sort.DESCENDING)) {
						return (int) (arg1.getStart().compareTo(arg0.getStart()));
					} else {
						return 0;
					}
					
				}
			});
		}
		
		LOG.debug("return event list for {} with {} entries", query, eventList.size());
		return eventList;
	}
	
	private Calendar createCalendar(CalDavEvent calDavEvent) {
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone(defaultTimeZone.getID());
		
		Calendar calendar = new Calendar();
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(new ProdId("openHAB"));
		VEvent vEvent = new VEvent();
		vEvent.getProperties().add(new Summary(calDavEvent.getName()));
		vEvent.getProperties().add(new Description(calDavEvent.getContent()));
		final DtStart dtStart = new DtStart(new net.fortuna.ical4j.model.DateTime(calDavEvent.getStart().toDate()));
		dtStart.setTimeZone(timezone);
		vEvent.getProperties().add(dtStart);
		final DtEnd dtEnd = new DtEnd(new net.fortuna.ical4j.model.DateTime(calDavEvent.getEnd().toDate()));
		dtEnd.setTimeZone(timezone);
		vEvent.getProperties().add(dtEnd);
		vEvent.getProperties().add(new Uid(calDavEvent.getId()));
		vEvent.getProperties().add(Clazz.PUBLIC);
		vEvent.getProperties().add(new LastModified(new net.fortuna.ical4j.model.DateTime(calDavEvent.getLastChanged().toDate())));
		calendar.getComponents().add(vEvent);
		
		return calendar;
	}
	
	private File getCachePath(String calendarKey) {
		return new File(CACHE_PATH + "/" + calendarKey);
	}
	
	private File getCacheFile(String calendarId, String filename) {
		return new File(getCachePath(calendarId), filename + ".ics");
	}

	class CalendarRuntime {
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
	class EventContainer {
		private String calendarId;
		private String eventId;
		private org.joda.time.DateTime lastChanged;
		private String filename;
		private boolean historicEvent;
		private org.joda.time.DateTime calculatedUntil;
		
		private List<CalDavEvent> eventList = new ArrayList<CalDavEvent>();
		private final List<TimerTask> timerBeginMap = new ArrayList<TimerTask>();
		private final List<TimerTask> timerEndMap = new ArrayList<TimerTask>();
		
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

		public List<TimerTask> getTimerBeginMap() {
			return timerBeginMap;
		}
		
		public List<TimerTask> getTimerEndMap() {
			return timerEndMap;
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
