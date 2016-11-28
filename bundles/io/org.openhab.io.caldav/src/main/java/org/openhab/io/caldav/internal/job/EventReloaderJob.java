/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.EventNotifier;
import org.openhab.io.caldav.internal.CalDavConfig;
import org.openhab.io.caldav.internal.CalDavLoaderImpl;
import org.openhab.io.caldav.internal.EventStorage;
import org.openhab.io.caldav.internal.OAuthUtil;
import org.openhab.io.caldav.internal.Util;
import org.openhab.io.caldav.internal.model.CalendarFile;
import org.openhab.io.caldav.internal.model.CalendarRuntime;
import org.openhab.io.caldav.internal.model.EventContainer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineException;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;

/**
 * Reloads events from the server.
 *
 * @author Robert Delbrück
 * @since 1.9.0
 *
 */
public class EventReloaderJob implements Job {
    private static final String OLD_EVENT_SPLIT_CHAR = "///";
    public static final String KEY_CONFIG = "config";
    private static final Logger log = LoggerFactory.getLogger(EventReloaderJob.class);

    private static Map<String, Boolean> cachedEventsLoaded = new ConcurrentHashMap<String, Boolean>();

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {

        final String config = context.getJobDetail().getJobDataMap().getString(KEY_CONFIG);
        CalendarRuntime eventRuntime = EventStorage.getInstance().getEventCache().get(config);

        // reload cached events (if necessary)
        if (!cachedEventsLoaded.containsKey(config)) {
            try {
                log.debug("reload cached events for config: {}", eventRuntime.getConfig().getKey());
                for (File fileCalendarKeys : new File(CalDavLoaderImpl.CACHE_PATH).listFiles()) {
                    if (!eventRuntime.getConfig().getKey().equals(Util.getFilename(fileCalendarKeys.getName()))) {
                        continue;
                    }
                    final Collection<File> icsFiles = FileUtils.listFiles(fileCalendarKeys, new String[] { "ics" },
                            false);
                    for (File icsFile : icsFiles) {
                        CalendarFile calendarFile = new CalendarFile(Util.getFilename(icsFile.getAbsolutePath()));
                        eventRuntime.addCalendarFile(calendarFile);
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(icsFile);
                            String calendar = IOUtils.toString(fis);
                            loadEvents(calendarFile, calendar, eventRuntime.getConfig(), new ArrayList<String>());
                        } catch (IOException e) {
                            log.error("cannot load events for file: " + icsFile, e);
                        } catch (ParserException e) {
                            log.error("cannot load events for file: " + icsFile, e);
                        } finally {
                            if (fis != null) {
                                fis.close();
                            }
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
            for (CalendarFile calendarFile : eventRuntime.getCalendarFiles()) {
                for (EventContainer eventContainer : calendarFile.getEventContainerList()) {
                    oldEventIds.add(calendarFile.getFilename() + OLD_EVENT_SPLIT_CHAR + eventContainer.getEventId());
                }
            }
            if (eventRuntime.getConfig().isOauth()) {
                loadEventsOauth(eventRuntime, oldEventIds);
            } else {
                loadEvents(eventRuntime, oldEventIds);
            }
            // stop all events in oldMap
            removeDeletedEvents(config, oldEventIds);

            for (EventNotifier notifier : CalDavLoaderImpl.instance.getEventListenerList()) {
                try {
                    notifier.calendarReloaded(config);
                } catch (Exception e) {
                    log.error("error while invoking listener", e);
                }
            }

            // printAllEvents();
        } catch (SardineException e) {
            log.error(String.format("error while loading calendar entries: %s (%s - %s)", e.getMessage(),
                    e.getStatusCode(), e.getResponsePhrase()), e);
            throw new JobExecutionException("error while loading calendar entries", e, false);
        } catch (Exception e) {
            log.error(String.format("error while loading calendar entries: %s", e.getMessage()), e);
            throw new JobExecutionException("error while loading calendar entries", e, false);
        }
    }

    private void loadEventsOauth(final CalendarRuntime calendarRuntime, final List<String> oldEventIds) {
        CalDavConfig config = calendarRuntime.getConfig();

        try {
            String calendar = OAuthUtil.getCalendars(config.getKey(), config.getUsername(), config.getPassword(),
                    config.getUrl());

            String newUid = DigestUtils.shaHex(calendar);
            String filename = "oauth";
            CalendarFile calendarFile = calendarRuntime.getCalendarFileByFilename(filename);
            if (calendarFile != null) {
                // if (calendarFile.toBeRead(newUid)) {
                // the same calendar as before, nothing changed
                this.loadEvents(calendarFile, calendar, config, oldEventIds);
                // } else {
                // this.removeOldIdsForFilename(oldEventIds, filename);
                // }
            } else {
                calendarFile = new CalendarFile(filename);
                calendarRuntime.addCalendarFile(calendarFile);
                this.loadEvents(calendarFile, calendar, config, oldEventIds);
            }
            calendarFile.setLastGenUID(newUid);
        } catch (Exception e) {
            log.error("error loading calendar entries", e);
        }
    }

    private void removeOldIdsForFilename(final List<String> oldEventIds, final String filename) {
        for (String oldId : new ArrayList<>(oldEventIds)) {
            if (oldId.startsWith(filename + OLD_EVENT_SPLIT_CHAR)) {
                oldEventIds.remove(oldId);
            }
        }
    }

    private synchronized void removeDeletedEvents(final String calendarKey, final List<String> oldMap) {
        final CalendarRuntime eventRuntime = EventStorage.getInstance().getEventCache().get(calendarKey);

        for (String filenameEventId : oldMap) {
            String[] split = filenameEventId.split(OLD_EVENT_SPLIT_CHAR);
            String filename = split[0];
            String eventId = split[1];
            CalendarFile calendarFile = eventRuntime.getCalendarFileByFilename(filename);
            if (calendarFile == null) {
                log.error("cannot find calendar file for filename: {}", filename);
                continue;
            }
            EventContainer eventContainer = calendarFile.getEventContainerForId(eventId);
            if (eventContainer == null) {
                log.error("cannot find event container for event id: {}", eventId);
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
                    boolean deleteJob = CalDavLoaderImpl.instance.getScheduler().deleteJob(JobKey.jobKey(jobId, group));
                    log.debug("old job ({}) deleted? {}", jobId, deleteJob);
                } catch (SchedulerException e) {
                    log.error("cannot delete job '{}'", jobId);
                }
            }
            eventContainer.getTimerMap().clear();

            for (EventNotifier notifier : CalDavLoaderImpl.instance.getEventListenerList()) {
                for (CalDavEvent event : eventContainer.getEventList()) {
                    try {
                        notifier.eventRemoved(event);
                    } catch (Exception e) {
                        log.error("error while invoking listener", e);
                    }
                }
            }

            calendarFile.removeEvent(eventId);
            if (calendarFile.isEmpty()) {
                Util.getCacheFile(eventRuntime.getConfig().getKey(), filename).delete();
                eventRuntime.removeCalendarFile(filename);
            }
        }
    }

    /**
     * all events which are available must be removed from the oldEventIds list
     *
     * @param calendarRuntime
     * @param oldEventIds
     * @param oldEventIds
     * @throws IOException
     * @throws ParserException
     */
    public synchronized void loadEvents(final CalendarRuntime calendarRuntime, final List<String> oldEventIds)
            throws IOException, ParserException {
        CalDavConfig config = calendarRuntime.getConfig();

        Sardine sardine = Util.getConnection(config);

        List<DavResource> list = sardine.list(config.getUrl(), 1, false);

        for (DavResource resource : list) {
            final String filename = Util.getFilename(resource.getName());

            try {
                if (resource.isDirectory()) {
                    continue;
                }

                URL url = Util.createURL(config.getUrl(), resource);

                // must not be loaded
                CalendarFile calendarFile = calendarRuntime.getCalendarFileByFilename(filename);
                final org.joda.time.DateTime lastResourceChange = new org.joda.time.DateTime(resource.getModified());

                log.trace("calendarFile found: {}", calendarFile != null);
                log.trace("last resource modification: {}", lastResourceChange);

                if (calendarFile != null) {
                    // calendarFile does already exist, update it
                    log.trace("last change of already loaded event: {}", calendarFile.getLastResourceChange());

                    if (config.isLastModifiedFileTimeStampValid()) {
                        // timestamp of last change is always correct
                        if (lastResourceChange.isAfter(calendarFile.getLastResourceChange())) {
                            // file has been updated
                            log.debug("loading resource: {}", resource);
                            String calendarStr = IOUtils.toString(sardine.get(url.toString().replaceAll(" ", "%20")),
                                    config.getCharset());
                            this.loadEvents(calendarFile, calendarStr, config, oldEventIds);
                        } else {
                            // no change
                            removeOldIdsForFilename(oldEventIds, filename);
                        }
                    } else {
                        // last update timestamp is not always valid
                        String calendarStr = IOUtils.toString(sardine.get(url.toString().replaceAll(" ", "%20")),
                                config.getCharset());
                        String digest = DigestUtils.shaHex(calendarStr);
                        if (calendarFile.toBeRead(digest)) {
                            log.debug("loading resource: {}", resource);
                            this.loadEvents(calendarFile, calendarStr, config, oldEventIds);
                        }
                        calendarFile.setLastGenUID(digest);
                    }
                } else {
                    // file is completely new
                    log.debug("loading resource: {}", resource);
                    String calendarStr = IOUtils.toString(sardine.get(url.toString().replaceAll(" ", "%20")),
                            config.getCharset());
                    calendarFile = new CalendarFile(filename);
                    calendarRuntime.addCalendarFile(calendarFile);
                    this.loadEvents(calendarFile, calendarStr, config, oldEventIds);
                }

                calendarFile.setLastResourceChange(lastResourceChange);

            } catch (ParserException e) {
                log.error("error parsing ics file: " + filename, e);
            } catch (SardineException e) {
                log.error("error reading ics file: " + filename, e);
            }
        }
    }

    private void loadEvents(final CalendarFile calendarFile, final String calendarStr, final CalDavConfig config,
            final List<String> oldEventIds) throws IOException, ParserException {
        Calendar calendar = Util.getCalendarObj(calendarStr);

        Util.storeToDisk(config.getKey(), calendarFile.getFilename(), calendar);

        org.joda.time.DateTime loadFrom = org.joda.time.DateTime.now().minusMinutes(config.getHistoricLoadMinutes());
        org.joda.time.DateTime loadTo = org.joda.time.DateTime.now().plusMinutes(config.getPreloadMinutes());
        calendarFile.setCalculatedUntil(loadTo);

        final ComponentList<CalendarComponent> vEventComponents = calendar.getComponents(Component.VEVENT);
        for (CalendarComponent comp : vEventComponents) {
            VEvent vEvent = (VEvent) comp;
            String eventId = vEvent.getUid().getValue();
            String eventName = "";
            if (vEvent.getSummary() != null) {
                eventName = vEvent.getSummary().getValue();
            }
            log.trace("loading event: " + eventId + ":" + eventName);

            // 'LastModified' in VEvent is optional
            org.joda.time.DateTime eventLastModified = null;
            if (vEvent.getLastModified() != null) {
                eventLastModified = new org.joda.time.DateTime(vEvent.getLastModified().getDateTime());
            }

            oldEventIds.remove(calendarFile.getFilename() + OLD_EVENT_SPLIT_CHAR + eventId);

            EventContainer eventContainer = calendarFile.getEventContainerForId(eventId);

            if (eventContainer != null && eventLastModified != null
                    && eventLastModified.isAfter(eventContainer.getLastChanged())) {
                // event has been loaded before and last modification is newer

            } else if (eventContainer == null) {
                // event is new

            } else if (eventContainer != null && eventLastModified == null) {
                // event has been loaded before, but the last modification date is not set

            } else {
                log.trace("skipping resource processing {}, not changed", eventId);
                continue;
            }

            // create always a new event if it modified
            eventContainer = new EventContainer(eventId, eventLastModified, loadTo);

            Period period = new Period(new DateTime(loadFrom.toDate()), new DateTime(loadTo.toDate()));
            PeriodList periods = vEvent.calculateRecurrenceSet(period);
            periods = periods.normalise();

            if (isHistoric(vEvent, periods)) {
                log.trace("event will never be occur (historic): {}", eventName);
                eventContainer.setHistoricEvent(true);
            }

            eventContainer.setLastChanged(eventLastModified);

            for (Period p : periods) {
                CalDavEvent event = createEvent(calendarFile.getFilename(), config, vEvent, eventLastModified,
                        eventName, p);
                eventContainer.getEventList().add(event);
            }

            CalDavLoaderImpl.instance.addEventToMap(config.getKey(), calendarFile, eventContainer, true);
        }
    }

    private boolean isHistoric(final VEvent vEvent, final PeriodList periods) {
        // no more upcoming events
        if (periods.size() > 0) {
            if (vEvent
                    .getConsumedTime(new net.fortuna.ical4j.model.Date(),
                            new net.fortuna.ical4j.model.Date(org.joda.time.DateTime.now().plusYears(10).getMillis()))
                    .size() == 0) {
                return true;
            }
        }
        return false;
    }

    private CalDavEvent createEvent(final String filename, final CalDavConfig config, final VEvent vEvent,
            final org.joda.time.DateTime lastModifedVEvent, final String eventName, final Period p) {
        org.joda.time.DateTime start = getDateTime("start", p.getStart(), p.getRangeStart());
        org.joda.time.DateTime end = getDateTime("end", p.getEnd(), p.getRangeEnd());

        CalDavEvent event = new CalDavEvent(eventName, vEvent.getUid().getValue(), config.getKey(), start, end);
        event.setLastChanged(lastModifedVEvent);
        if (vEvent.getLocation() != null) {
            event.setLocation(vEvent.getLocation().getValue());
        }
        if (vEvent.getDescription() != null) {
            event.setContent(vEvent.getDescription().getValue());
        }
        event.getCategoryList().addAll(readCategory(vEvent));
        event.setFilename(filename);
        log.trace("adding event: " + event.getShortName());
        return event;
    }

    /**
     * Returns a list of categories or an empty list if none found.
     *
     * @param vEvent
     * @return
     */
    private List<String> readCategory(final VEvent vEvent) {
        Property propertyCategory = vEvent.getProperty(Property.CATEGORIES);
        if (propertyCategory != null) {
            String categories = propertyCategory.getValue();
            if (categories != null) {
                String[] categoriesSplit = StringUtils.split(categories, ",");
                return Arrays.asList(categoriesSplit);
            }

        }
        return new ArrayList<String>();
    }

    private org.joda.time.DateTime getDateTime(final String dateType, final DateTime date, final Date rangeDate) {
        org.joda.time.DateTime start;
        if (date.getTimeZone() == null) {
            if (date.isUtc()) {
                log.trace("{} is without timezone, but UTC", dateType);
                start = new org.joda.time.DateTime(rangeDate, DateTimeZone.UTC).toLocalDateTime()
                        .toDateTime(CalDavLoaderImpl.defaultTimeZone);
            } else {
                log.trace("{} is without timezone, not UTC", dateType);
                start = new LocalDateTime(rangeDate).toDateTime();
            }
        } else if (DateTimeZone.getAvailableIDs().contains(date.getTimeZone().getID())) {
            log.trace("{} is with known timezone: {}", dateType, date.getTimeZone().getID());
            start = new org.joda.time.DateTime(rangeDate, DateTimeZone.forID(date.getTimeZone().getID()));
        } else {
            // unknown timezone
            log.trace("{} is with unknown timezone: {}", dateType, date.getTimeZone().getID());
            start = new org.joda.time.DateTime(rangeDate, CalDavLoaderImpl.defaultTimeZone);
        }
        return start;
    }
}
