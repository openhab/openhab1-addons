/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.persistence.gcal.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.io.gcal.auth.GCalGoogleOAuth;
import org.osgi.framework.BundleContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

/**
 * This implementation of the {@link PersistenceService} provides Presence
 * Simulation features based on the Google Calendar Service.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class GCalPersistenceService implements PersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(GCalPersistenceService.class);

    private static final String GCAL_SCHEDULER_GROUP = "GoogleCalendar";
    private static String calendar_name = "";

    /** the upload interval (optional, defaults to 10 seconds) */
    private static int uploadInterval = 10;

    /** the offset (in days) which will used to store future events */
    private static int offset = 14;

    /**
     * the base script which is written to the newly created Calendar-Events by
     * the GCal based presence simulation. It must contain two format markers
     * <code>%s</code>. The first marker represents the Item to send the command
     * to and the second represents the State.
     */
    private static String executeScript = "send %s %s";

    /** indicated whether this service was properly initialized */
    private boolean initialized = false;

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /** holds the local quartz scheduler instance */
    private Scheduler scheduler;

    /** holds the Google Calendar entries to upload to Google */
    private static Queue<Event> entries = new ConcurrentLinkedQueue<Event>();

    public void activate(final BundleContext bundleContext, final Map<String, Object> config) {

        String offsetString = (String) config.get("offset");
        if (StringUtils.isNotBlank(offsetString)) {
            try {
                offset = Integer.valueOf(offsetString);
            } catch (IllegalArgumentException iae) {
                logger.warn("couldn't parse '{}' to an integer");
            }
        }

        String urlString = (String) config.get("calendar_name");
        if (!StringUtils.isBlank(urlString)) {
            calendar_name = urlString;
        } else {
            logger.warn(
                    "gcal-persistence:calendar_name must be configured in openhab.cfg. Calendar name or word \"primary\" MUST be specified");
        }

        String executeScriptString = (String) config.get("executescript");
        if (StringUtils.isNotBlank(executeScriptString)) {
            executeScript = executeScriptString;
        }

        initialized = true;

        scheduleUploadJob();
    }

    public void deactivate(final int reason) {
        cancelAllJobs();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getName() {
        return "gcal";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void store(Item item) {
        store(item, item.getName());
    }

    /**
     * Creates a new Google Calendar Entry for each <code>item</code> and adds
     * it to the processing queue. The entries' title will either be the items
     * name or <code>alias</code> if it is <code>!= null</code>.
     *
     * The new Calendar Entry will contain a single command to be executed e.g.<br>
     * <p>
     * <code>send &lt;item.name&gt; &lt;item.state&gt;</code>
     * </p>
     *
     * @param item the item which state should be persisted.
     * @param alias the alias under which the item should be persisted.
     */
    @Override
    public void store(final Item item, final String alias) {
        if (initialized) {
            String newAlias = alias != null ? alias : item.getName();

            Event event = new Event();
            event.setSummary("[PresenceSimulation] " + newAlias);
            event.setDescription(String.format(executeScript, item.getName(), item.getState().toString()));
            Date now = new Date();
            Date startDate = new Date(now.getTime() + 3600000L * 24 * offset);
            Date endDate = startDate;
            DateTime start = new DateTime(startDate);
            event.setStart(new EventDateTime().setDateTime(start));
            DateTime end = new DateTime(endDate);
            event.setEnd(new EventDateTime().setDateTime(end));

            entries.offer(event);

            logger.trace("added new entry '{}' for item '{}' to upload queue", event.getSummary(), item.getName());
        } else {
            logger.debug(
                    "GCal PresenceSimulation Service isn't initialized properly! No entries will be uploaded to your Google Calendar");
        }
    }

    /**
     * Schedules new quartz scheduler job for uploading calendar entries to Google
     */
    private void scheduleUploadJob() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobDetail job = newJob(SynchronizationJob.class).withIdentity("Upload_GCal-Entries", GCAL_SCHEDULER_GROUP)
                    .build();

            SimpleTrigger trigger = newTrigger().withIdentity("Upload_GCal-Entries", GCAL_SCHEDULER_GROUP)
                    .withSchedule(repeatSecondlyForever(uploadInterval)).build();

            scheduler.scheduleJob(job, trigger);
            logger.debug("Scheduled Google Calendar Upload-Job with interval '{}'", uploadInterval);
        } catch (SchedulerException e) {
            logger.warn("Could not create Google Calendar Upload-Job: {}", e.getMessage());
        }
    }

    /**
     * Delete all quartz scheduler jobs of the group <code>Dropbox</code>.
     */
    private void cancelAllJobs() {
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(jobGroupEquals(GCAL_SCHEDULER_GROUP));
            if (jobKeys.size() > 0) {
                scheduler.deleteJobs(new ArrayList<JobKey>(jobKeys));
                logger.debug("Found {} Google Calendar Upload-Jobs to delete from DefaulScheduler (keys={})",
                        jobKeys.size(), jobKeys);
            } else {
                logger.debug("Not found Google Calendar Upload to remove");
            }

        } catch (SchedulerException e) {
            logger.warn("Couldn't remove Google Calendar Upload-Job: {}", e.getMessage());
        }
    }

    /**
     * A quartz scheduler job to upload {@link Event}s to
     * the remote Calendar. There can be only one instance of a specific job
     * type running at the same time.
     *
     * @author Thomas.Eichstaedt-Engelen
     * @since 1.0.0
     */
    @DisallowConcurrentExecution
    public static class SynchronizationJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            logger.trace("going to upload {} calendar entries to Google now ...", entries.size());
            Calendar calendarClient = null;
            if (entries.size() > 0) {
                Credential credential = GCalGoogleOAuth.getCredential(false);
                if (credential == null) {
                    logger.error(
                            "Please configure gcal:client_id/gcal:client_secret in openhab.cfg. Refer to wiki how to create client_id/client_secret pair");
                } else {
                    // set up global Calendar instance
                    calendarClient = new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                            credential).setApplicationName("openHABpersistence").build();
                }
            }
            for (Event entry : entries) {
                upload(calendarClient, entry);
                entries.remove(entry);
            }
        }

        private void upload(Calendar calendarClient, Event entry) {
            try {
                long startTime = System.currentTimeMillis();
                Event createdEvent = createCalendarEvent(calendarClient, entry);
                logger.debug("succesfully created new calendar event (title='{}', date='{}', content='{}') in {}ms",
                        new Object[] { createdEvent.getSummary(), createdEvent.getStart().toString(),
                                createdEvent.getDescription(), System.currentTimeMillis() - startTime });
            } catch (Exception e) {
                logger.error("creating a new calendar entry throws an exception: {}", e.getMessage());
            }
        }

        /**
         * Creates a new calendar entry.
         *
         * @param event the event to create in the remote calendar identified by the
         *            full calendar feed configured in </code>openhab.cfg</code>
         * @return the newly created entry
         * @throws IOException
         */
        private Event createCalendarEvent(Calendar calendarClient, Event event) throws IOException {

            if (calendarClient == null) {
                logger.error(
                        "Please configure gcal:client_id/gcal:client_secret in openhab.cfg. Refer to wiki how to create client_id/client_secret pair");
            } else {
                // set up global Calendar instance
                CalendarListEntry calendarID = GCalGoogleOAuth.getCalendarId(calendar_name);
                if (calendarID != null) {
                    return calendarClient.events().insert(calendarID.getId(), event).execute();
                }

            }

            return null;

        }

    }

}
