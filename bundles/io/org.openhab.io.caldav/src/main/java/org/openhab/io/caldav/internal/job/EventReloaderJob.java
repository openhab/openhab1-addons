/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineException;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Summary;

public class EventReloaderJob implements Job {
    public static final String KEY_CONFIG = "config";
    private static final Logger log = LoggerFactory.getLogger(EventReloaderJob.class);

    private static Map<String, Boolean> cachedEventsLoaded = new ConcurrentHashMap<String, Boolean>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        final String config = context.getJobDetail().getJobDataMap().getString(KEY_CONFIG);
        CalendarRuntime eventRuntime = EventStorage.getInstance().getEventCache().get(config);

        log.debug("running EventReloaderJob for config : {}", config);

        // reload cached events (if necessary)
        if (!cachedEventsLoaded.containsKey(config)) {
            try {
                log.debug("reload cached events for config: {}", eventRuntime.getConfig().getKey());
                for (File fileCalendarKeys : new File(CalDavLoaderImpl.CACHE_PATH).listFiles()) {
                    if (!eventRuntime.getConfig().getKey().equals(Util.getFilename(fileCalendarKeys.getName()))) {
                        log.trace("not our config : {}", Util.getFilename(fileCalendarKeys.getName()));
                        continue;
                    }
                    log.trace("found our config : {}", Util.getFilename(fileCalendarKeys.getName()));
                    final Collection<File> icsFiles = FileUtils.listFiles(fileCalendarKeys, new String[] { "ics" },
                            false);
                    for (File icsFile : icsFiles) {
                        try {
                            FileInputStream fis = new FileInputStream(icsFile);
                            log.debug("loading events from file : {}", icsFile);
                            loadEvents(Util.getFilename(icsFile.getAbsolutePath()),
                                    new org.joda.time.DateTime(icsFile.lastModified()), fis, eventRuntime.getConfig(),
                                    new ArrayList<String>(), true);
                        } catch (IOException e) {
                            log.warn("Cannot load events for file: {}", icsFile, e);
                        } catch (ParserException e) {
                            log.warn("Cannot load events for file: {}", icsFile, e);
                        }
                    }
                    break;
                }
            } catch (Throwable e) {
                log.warn("Cannot load events", e);
            } finally {
                cachedEventsLoaded.put(config, true);
            }
        }

        try {
            log.debug("loading events for config: {}", config);
            List<String> oldEventIds = new ArrayList<String>();
            for (EventContainer eventContainer : eventRuntime.getEventMap().values()) {
                oldEventIds.add(eventContainer.getFilename());
                log.debug(
                        "old eventcontainer -- id : {} -- filename : {} -- calcuntil : {} -- lastchanged : {} -- ishistoric : {}",
                        eventContainer.getEventId(), eventContainer.getFilename(), eventContainer.getCalculatedUntil(),
                        eventContainer.getLastChanged(), eventContainer.isHistoricEvent());

                if (log.isDebugEnabled()) {
                    for (int i = 0; i < eventContainer.getEventList().size(); i++) {
                        CalDavEvent elem = eventContainer.getEventList().get(i);
                        log.debug("old eventlist contains the event : {} -- deb : {} -- fin : {} -- lastchanged {}",
                                elem.getName(), elem.getStart(), elem.getEnd(), elem.getLastChanged());
                    }
                }
            }

            loadEvents(eventRuntime, oldEventIds);
            // stop all events in oldMap
            removeDeletedEvents(config, oldEventIds);

            for (EventNotifier notifier : CalDavLoaderImpl.instance.getEventListenerList()) {
                try {
                    notifier.calendarReloaded(config);
                } catch (Exception e) {
                    log.warn("Error while invoking listener", e);
                }
            }

            // print All scheduled jobs :
            if (log.isDebugEnabled()) {
                log.debug("jobs scheduled : ");
                Scheduler scheduler = CalDavLoaderImpl.instance.getScheduler();
                for (String groupName : CalDavLoaderImpl.instance.getScheduler().getJobGroupNames()) {

                    for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                        String jobName = jobKey.getName();
                        String jobGroup = jobKey.getGroup();

                        // get job's trigger
                        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                        Date nextFireTime = triggers.get(0).getNextFireTime();

                        log.debug("[job] : {} - [groupName] : {} - {}", jobName, jobGroup, nextFireTime);

                    }

                }
            }

        } catch (SardineException e) {
            log.warn("Sardine error while loading calendar entries: {} ({} - {})", e.getMessage(), e.getStatusCode(),
                    e.getResponsePhrase(), e);
            throw new JobExecutionException("Sardine error while loading calendar entries", e, false);
        } catch (Exception e) {
            log.warn("Error while loading calendar entries: {}", e.getMessage(), e);
            throw new JobExecutionException("Error while loading calendar entries", e, false);
        }
    }

    private synchronized void removeDeletedEvents(String calendarKey, List<String> oldMap) {
        final CalendarRuntime eventRuntime = EventStorage.getInstance().getEventCache().get(calendarKey);

        for (String filename : oldMap) {
            EventContainer eventContainer = eventRuntime.getEventContainerByFilename(filename);
            if (eventContainer == null) {
                log.warn("Cannot find event container for filename: {}", filename);
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
                    log.warn("Cannot delete job '{}'", jobId, e);
                }
            }
            eventContainer.getTimerMap().clear();

            for (EventNotifier notifier : CalDavLoaderImpl.instance.getEventListenerList()) {
                for (CalDavEvent event : eventContainer.getEventList()) {
                    try {
                        notifier.eventRemoved(event);
                    } catch (Exception e) {
                        log.warn("Error while invoking listener", e);
                    }
                }
            }

            ConcurrentHashMap<String, EventContainer> eventContainerMap = eventRuntime.getEventMap();
            this.removeFromDisk(eventContainer);

            log.debug("remove deleted event: {}", eventContainer.getEventId());
            eventContainerMap.remove(eventContainer.getEventId());
        }
    }

    private void removeFromDisk(EventContainer eventContainer) {
        Util.getCacheFile(eventContainer.getCalendarId(), eventContainer.getFilename()).delete();
    }

    /**
     * all events which are available must be removed from the oldEventIds list
     *
     * @param calendarRuntime
     * @param oldEventIds
     * @throws IOException
     * @throws ParserException
     */
    public synchronized void loadEvents(final CalendarRuntime calendarRuntime, final List<String> oldEventIds)
            throws IOException, ParserException {
        CalDavConfig config = calendarRuntime.getConfig();

        Sardine sardine = Util.getConnection(config);

        List<DavResource> list = sardine.list(config.getUrl(), 1, false);

        log.trace("before load events : oldeventids contains : {}", oldEventIds);

        for (DavResource resource : list) {
            final String filename = Util.getFilename(resource.getName());

            try {
                if (resource.isDirectory()) {
                    continue;
                }

                // an ics file can contain multiple events
                // ==> multiple eventcontainers could have the same filename (and different eventid),
                // ==>we must not have one of them remaining in oldEventIds var (bad chosen name, cause it's a list of
                // oldEventContainers's filename, so with doubles possible)
                // or the remaining jobs with this filename will get unscheduled on the "removeDeletedEvents(config,
                // oldEventIds)" call
                oldEventIds.removeAll(Arrays.asList(filename));

                // must not be loaded
                EventContainer eventContainer = calendarRuntime.getEventContainerByFilename(filename);
                final org.joda.time.DateTime lastResourceChangeFS = new org.joda.time.DateTime(resource.getModified());

                log.trace("eventContainer found: {}", eventContainer != null);
                log.trace("last resource modification: {}", lastResourceChangeFS);
                log.trace("last change of already loaded event: {}",
                        eventContainer != null ? eventContainer.getLastChanged() : null);
                if (config.isLastModifiedFileTimeStampValid()) {
                    if (eventContainer != null && !lastResourceChangeFS.isAfter(eventContainer.getLastChanged())) {
                        // check if some timers or single (from repeating events) have
                        // to be created
                        if (eventContainer.getCalculatedUntil() != null && eventContainer.getCalculatedUntil()
                                .isAfter(org.joda.time.DateTime.now().plusMinutes(config.getReloadMinutes()))) {
                            // the event is calculated as long as the next reload
                            // interval can handle this
                            log.trace("skipping resource {}; not changed. calculated until: {}", resource.getName(),
                                    eventContainer.getCalculatedUntil());
                            continue;
                        }

                        if (eventContainer.isHistoricEvent()) {
                            // no more upcoming events, do nothing
                            log.trace("skipping resource {}; not changed (historic)", resource.getName());
                            continue;
                        }

                        File icsFile = Util.getCacheFile(config.getKey(), filename);
                        if (icsFile != null && icsFile.exists()) {
                            FileInputStream fis = new FileInputStream(icsFile);
                            this.loadEvents(filename, lastResourceChangeFS, fis, config, oldEventIds, false);
                            fis.close();
                            continue;
                        }
                    }
                }

                log.debug("loading resource: {} (FSchangedTS not valid)", resource);

                // prepare resource url
                URL url = new URL(config.getUrl());
                log.trace("Raw URL: {}", url);
                String resourcePath = resource.getPath();
                URI uri;
                try {
                    uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), resourcePath, null, null);
                } catch(URISyntaxException e) {
                    log.warn("Unable to load events; the provided string could not be parsed as a valid URI: '{}'", url);
                    return;
                }
                url = uri.toURL();
                log.trace("URL after encoding: {}", url);
                InputStream inputStream = sardine.get(url.toString());

                this.loadEvents(filename, lastResourceChangeFS, inputStream, config, oldEventIds, false);
            } catch (ParserException e) {
                log.warn("Parser exception parsing ics file: {}", filename, e);
            } catch (SardineException e) {
                log.warn("Sardine exception reading ics file: {}", filename, e);
            }
        }

        log.trace("after load events : oldeventids contains : {}", oldEventIds.toString());
    }

    public void loadEvents(String filename, org.joda.time.DateTime lastResourceChangeFS, final InputStream inputStream,
            final CalDavConfig config, final List<String> oldEventIds, boolean readFromFile)
            throws IOException, ParserException {
        CalendarBuilder builder = new CalendarBuilder();
        InputStreamReader is = new InputStreamReader(inputStream, config.getCharset());
        BufferedReader in = new BufferedReader(is, 50);

        final UnfoldingReader uin = new UnfoldingReader(in, 50, true);
        Calendar calendar = builder.build(uin);
        uin.close();

        EventContainer eventContainer = new EventContainer(config.getKey());
        eventContainer.setFilename(filename);
        eventContainer.setLastChanged(lastResourceChangeFS);

        org.joda.time.DateTime loadFrom = org.joda.time.DateTime.now().minusMinutes(config.getHistoricLoadMinutes());
        log.trace("loadFrom = {}", loadFrom);
        org.joda.time.DateTime loadTo = org.joda.time.DateTime.now().plusMinutes(config.getPreloadMinutes());
        log.trace("loadTo = {}", loadTo);

        final ComponentList<CalendarComponent> vEventComponents = calendar.getComponents(Component.VEVENT);
        if (vEventComponents.size() == 0) {
            log.debug("could not find a VEVENT from calendar build based on file {}", filename);
            // no events inside
            if (!readFromFile) {
                Util.storeToDisk(config.getKey(), filename, calendar);
            }
            return;
        }
        org.joda.time.DateTime lastModifedVEventOverAll = null;
        for (CalendarComponent comp : vEventComponents) {
            VEvent vEvent = (VEvent) comp;
            Summary vEventSummary = vEvent.getSummary();
            log.trace("loading event: {}:{}", vEvent.getUid().getValue(), vEventSummary == null ? "(none)" : vEventSummary.getValue());
            // fallback, because 'LastModified' in VEvent is optional
            org.joda.time.DateTime lastModifedVEvent = lastResourceChangeFS;
            if (vEvent.getLastModified() != null) {
                lastModifedVEvent = new org.joda.time.DateTime(vEvent.getLastModified().getDateTime());
                log.trace("overriding lastmodified from file FS ({}) with event's last-modified property ({})",
                        lastResourceChangeFS, lastModifedVEvent);
            }

            if (!config.isLastModifiedFileTimeStampValid()) {
                if (lastModifedVEventOverAll == null || lastModifedVEvent.isAfter(lastModifedVEventOverAll)) {
                    lastModifedVEventOverAll = lastModifedVEvent;
                }
                if (!lastModifedVEvent.isBefore(eventContainer.getLastChanged())) {
                    // check if some timers or single (from repeating events) have
                    // to be created
                    if (eventContainer.getCalculatedUntil() != null && vEventComponents.size() == 1
                            && eventContainer.getCalculatedUntil()
                                    .isAfter(org.joda.time.DateTime.now().plusMinutes(config.getReloadMinutes()))) {
                        // the event is calculated as long as the next reload
                        // interval can handle this
                        log.trace("skipping resource processing. File {} has not changed.", filename);
                        continue;
                    }

                    if (eventContainer.isHistoricEvent()) {
                        // no more upcoming events, do nothing
                        log.trace("skipping resource processing. File {} is historic.", filename);
                        continue;
                    }
                }
            }

            Period period = new Period(new DateTime(loadFrom.toDate()), new DateTime(loadTo.toDate()));
            PeriodList periods = vEvent.calculateRecurrenceSet(period);
            periods = periods.normalise();

            String eventId = vEvent.getUid().getValue();
            final String eventName = vEventSummary == null ? "(none)" : vEventSummary.getValue();
            log.debug("Processing event '{}'", eventName);

            // no more upcoming events
            if (periods.size() > 0) {
                if (vEvent.getConsumedTime(new net.fortuna.ical4j.model.Date(),
                        new net.fortuna.ical4j.model.Date(org.joda.time.DateTime.now().plusYears(10).getMillis()))
                        .size() == 0) {
                    log.trace("event will never occur (historic): {}", eventName);
                    eventContainer.setHistoricEvent(true);
                }
            }
            else {
                log.debug("No periods exist for event '{}'", eventName);
            }

            // expecting this is for every vEvent inside a calendar equals
            eventContainer.setEventId(eventId);

            eventContainer.setCalculatedUntil(loadTo);

            for (Period p : periods) {
                log.debug("Processing periods...");
                org.joda.time.DateTime start = getDateTime("start", p.getStart(), p.getRangeStart());
                org.joda.time.DateTime end = getDateTime("end", p.getEnd(), p.getRangeEnd());

                log.trace("Processing period {} - {}", start, end);
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
                log.trace("adding event: {}", event.getShortName());
                eventContainer.getEventList().add(event);
            }
        }
        if (lastModifedVEventOverAll != null && !config.isLastModifiedFileTimeStampValid()) {
            eventContainer.setLastChanged(lastModifedVEventOverAll);
            log.debug("changing eventcontainer last modified to {}", lastModifedVEventOverAll);
        }
        CalDavLoaderImpl.instance.addEventToMap(eventContainer, true);
        if (!readFromFile) {
            Util.storeToDisk(config.getKey(), filename, calendar);
        }
    }

    /**
     * Returns a list of categories or an empty list if none found.
     *
     * @param vEvent
     * @return
     */
    private List<String> readCategory(VEvent vEvent) {
        PropertyList propertyCategoryList = vEvent.getProperties(Property.CATEGORIES);
        ArrayList<String> splittedCategoriesToReturn = new ArrayList<String>();
        if (propertyCategoryList != null) {
            for (int categoriesLineNum = 0; categoriesLineNum < propertyCategoryList.size(); categoriesLineNum++) {
                Property propertyCategory = propertyCategoryList.get(categoriesLineNum);
                String categories = propertyCategory.getValue();
                if (categories != null) {
                    String[] categoriesSplit = StringUtils.split(categories, ",");
                    for (String category : categoriesSplit) {
                        if (!splittedCategoriesToReturn.contains(category)) {
                            splittedCategoriesToReturn.add(category);
                        }
                    }
                }
            }
        }

        return splittedCategoriesToReturn;
    }

    private org.joda.time.DateTime getDateTime(String dateType, DateTime date, Date rangeDate) {
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
