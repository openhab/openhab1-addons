/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gcal.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.meta.When;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.LongRange;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.io.gcal.auth.GCalGoogleOAuth;
import org.openhab.io.gcal.internal.util.ExecuteCommandJob;
import org.openhab.io.gcal.internal.util.TimeRangeCalendar;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
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
import com.google.api.services.calendar.model.Events;

/**
 * Service which downloads Calendar events, parses their content and creates
 * Quartz-jobs and triggers out of them.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class GCalEventDownloader extends AbstractActiveService implements ManagedService {

    private static final String GCAL_SCHEDULER_GROUP = "gcal";

    private static final Logger logger = LoggerFactory.getLogger(GCalEventDownloader.class);

    private static String calendar_name = "primary";
    private static String filter = "";

    /** holds the current refresh interval, default to 900000ms (15 minutes) */
    public static int refreshInterval = 900000;

    /** holds the local quartz scheduler instance */
    private Scheduler scheduler;

    /**
     * RegEx to extract the start and end commands from the Calendar-Event content.
     * (<code>'start\s*?\{(.*?)\}\s*end\s*?\{(.*?)\}\s*'</code>)
     */
    private static final Pattern EXTRACT_STARTEND_CONTENT = Pattern
            .compile("start\\s*?\\{(.*?)\\}\\s*end\\s*?\\{(.*?)\\}\\s*", Pattern.DOTALL);

    /**
     * RegEx to extract the modified by command from the Calendar-Event content.
     * (<code>'(.*?)modified by\s*?\{(.*?)\}.*'</code>)
     */
    private static final Pattern EXTRACT_MODIFIEDBY_CONTENT = Pattern.compile("(.*?)modified by\\s*?\\{(.*?)\\}.*",
            Pattern.DOTALL);

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    protected String getName() {
        return "Google Calendar Event Downloader";
    }

    @Override
    public void activate() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            super.activate();
        } catch (SchedulerException se) {
            logger.error("initializing scheduler throws exception", se);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void execute() {
        Events myFeed = downloadEventFeed();
        if (myFeed != null) {
            List<Event> entries = myFeed.getItems();

            if (entries.size() > 0) {
                logger.debug("found {} calendar events to process", entries.size());

                try {
                    if (scheduler.isShutdown()) {
                        logger.warn("Scheduler has been shut down - probably due to exceptions?");
                    }
                    cleanJobs();
                    processEntries(entries);
                } catch (SchedulerException se) {
                    logger.error("scheduling jobs throws exception", se);
                }
            } else {
                logger.debug("gcal feed contains no events ...");
            }
        }
    }

    /**
     * Connects to Google-Calendar Service and returns the specified Events
     *
     * @return the corresponding Events or <code>null</code> if an error
     *         occurs. <i>Note:</i> We do only return events if their startTime lies between
     *         <code>now</code> and <code>now + 2 * refreshInterval</code> to reduce
     *         the amount of events to process.
     */
    private static Events downloadEventFeed() {
        // TODO: teichsta: there could be more than one calendar url in openHAB.cfg
        // for now we accept this limitation of downloading just one feed ...

        if (StringUtils.isBlank(calendar_name)) {
            logger.warn("Login aborted no calendar name defined");
            return null;
        }
        // authorization

        CalendarListEntry calendarID = GCalGoogleOAuth.getCalendarId(calendar_name);

        if (calendarID == null) {
            return null;
        }

        DateTime start = new DateTime(new Date(), TimeZone.getTimeZone(calendarID.getTimeZone()));
        DateTime end = new DateTime(new Date(start.getValue() + (2 * refreshInterval)),
                TimeZone.getTimeZone(calendarID.getTimeZone()));
        logger.debug("Downloading calendar feed for time interval: {} to  {} ", start, end);

        Events feed = null;
        try {
            Credential credential = GCalGoogleOAuth.getCredential(false);

            // set up global Calendar instance
            Calendar client = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("openHAB").build();
            Calendar.Events.List l = client.events().list(calendarID.getId()).setSingleEvents(true).setTimeMin(start)
                    .setTimeMax(end);

            // add the fulltext filter if it has been configured
            if (StringUtils.isNotBlank(filter)) {
                l = l.setQ(filter);
            }
            feed = l.execute();
        } catch (IOException e1) {
            logger.error("Event fetch failed: {}", e1.getMessage());
        }

        try {

            if (feed != null) {
                checkIfFullCalendarFeed(feed.getItems());
            }

            return feed;
        } catch (Exception e) {
            logger.error("downloading CalendarEventFeed throws exception: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Checks the first {@link CalendarEventEntry} of <code>entries</code> for
     * completeness. If this first event is incomplete all other events will be
     * incomplete as well.
     *
     * @param list the set to check
     */
    private static void checkIfFullCalendarFeed(List<Event> list) {
        if (list != null && !list.isEmpty()) {
            Event referenceEvent = list.get(0);
            if (referenceEvent.getICalUID() == null || referenceEvent.getStart().toString().isEmpty()) {
                logger.warn("calendar entries are incomplete - please make sure to use the full calendar feed");
            }

        }
    }

    /**
     * Delete all {@link Job}s of the group <code>GCAL_SCHEDULER_GROUP</code>
     *
     * @throws SchedulerException if there is an internal Scheduler error.
     */
    private void cleanJobs() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(jobGroupEquals(GCAL_SCHEDULER_GROUP));
        scheduler.deleteJobs(new ArrayList<JobKey>(jobKeys));
    }

    /**
     * <p>
     * Iterates through <code>entries</code>, extracts the event content and
     * creates quartz calendars, jobs and corresponding triggers for each event.
     * </p>
     * <p>
     * The following steps are done at event processing:
     * <ul>
     * <li>find events with empty content</li>
     * <li>create a {@link TimeRangeCalendar} for each event (unique by title) and add a TimeRange for each {@link When}
     * </li>
     * <li>add each {@link TimeRangeCalendar} to the {@link Scheduler}</li>
     * <li>find events with content</li>
     * <li>add a Job with the corresponding Triggers for each event</li>
     * </ul>
     *
     * @param entries the GCalendar events to create quart jobs for.
     * @throws SchedulerException if there is an internal Scheduler error.
     */
    private void processEntries(List<Event> entries) throws SchedulerException {
        Map<String, TimeRangeCalendar> calendarCache = new HashMap<String, TimeRangeCalendar>();

        // find all events with empty content - these events are taken to modify
        // the scheduler
        for (Event event : entries) {
            String eventContent = event.getDescription();
            String eventTitle = event.getSummary();

            if (StringUtils.isBlank(eventContent)) {
                logger.debug(
                        "found event '{}' with no content, add this event to the excluded TimeRangesCalendar - this event could be referenced by the modifiedBy clause",
                        eventTitle);

                if (!calendarCache.containsKey(eventTitle)) {
                    calendarCache.put(eventTitle, new TimeRangeCalendar());
                }
                TimeRangeCalendar timeRangeCalendar = calendarCache.get(eventTitle);
                timeRangeCalendar.addTimeRange(new LongRange(event.getStart().getDateTime().getValue(),
                        event.getEnd().getDateTime().getValue()));

            }
        }

        // add all calendars to the Scheduler an rebase all existing Triggers
        // the calendars has to be added first, to schedule Triggers successfully
        for (Entry<String, TimeRangeCalendar> entry : calendarCache.entrySet()) {
            scheduler.addCalendar(entry.getKey(), entry.getValue(), true, true);
        }

        // now we process all events with content
        for (Event event : entries) {
            String eventContent = event.getDescription();
            String eventTitle = event.getSummary();

            if (StringUtils.isNotBlank(eventContent)) {
                CalendarEventContent cec = parseEventContent(eventContent,
                        (eventTitle != null) && eventTitle.startsWith("[PresenceSimulation]"));

                String modifiedByEvent = null;
                if (calendarCache.containsKey(cec.modifiedByEvent)) {
                    modifiedByEvent = cec.modifiedByEvent;
                }

                JobDetail startJob = createJob(cec.startCommands, event, true);
                boolean triggersCreated = createTriggerAndSchedule(startJob, event, modifiedByEvent, true);

                if (triggersCreated) {
                    logger.debug("created new startJob '{}' with details '{}'", eventTitle,
                            createJobInfo(event, startJob));
                }

                // do only create end-jobs if there are end-commands ...
                if (StringUtils.isNotBlank(cec.endCommands)) {
                    JobDetail endJob = createJob(cec.endCommands, event, false);
                    triggersCreated = createTriggerAndSchedule(endJob, event, modifiedByEvent, false);

                    if (triggersCreated) {
                        logger.debug("created new endJob '{}' with details '{}'", eventTitle,
                                createJobInfo(event, endJob));
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Extracts start, end and modified by-commands from <code>content</code>.
     * Start-Commands will be executed at start-time and End-Commands will be
     * executed at end-time of the calendar-event. The modified-by command defines
     * the name of special event which disables the created Job temporarily.
     * </p>
     * <p>
     * If the RegExp <code>EXTRACT_STARTEND_CONTENT</code> doen't match the
     * complete content is taken as set of Start-Commands.
     * </p>
     *
     * @param content the set of Start- and End-Commands
     * @return the parsed event content
     */
    protected CalendarEventContent parseEventContent(String content, boolean presenceSimulation) {
        CalendarEventContent eventContent = new CalendarEventContent();
        String commandContent;

        Matcher modifiedByMatcher = EXTRACT_MODIFIEDBY_CONTENT.matcher(content);
        if (modifiedByMatcher.find()) {
            commandContent = modifiedByMatcher.group(1);
            eventContent.modifiedByEvent = StringUtils.trimToEmpty(modifiedByMatcher.group(2));
        } else {
            commandContent = content;
        }

        Matcher startEndMatcher = EXTRACT_STARTEND_CONTENT.matcher(commandContent);
        if (startEndMatcher.find()) {
            eventContent.startCommands = StringUtils.trimToEmpty(startEndMatcher.group(1));
            eventContent.endCommands = StringUtils.trimToEmpty(startEndMatcher.group(2));
        } else {
            if (presenceSimulation) {
                eventContent.startCommands = StringUtils.trimToEmpty("[PresenceSimulation]" + "\n" + commandContent);
            } else {
                eventContent.startCommands = StringUtils.trimToEmpty(commandContent);
            }
            logger.debug(
                    "given event content doesn't match regular expression to extract start-, end commands - using whole content as startCommand ({})",
                    commandContent);
        }

        return eventContent;
    }

    /**
     * Creates a new quartz-job with jobData <code>content</code> in the scheduler
     * group <code>GCAL_SCHEDULER_GROUP</code> if <code>content</code> is not
     * blank.
     *
     * @param content the set of commands to be executed by the
     *            {@link ExecuteCommandJob} later on
     * @param event
     * @param isStartEvent indicator to identify whether this trigger will be
     *            triggering a start or an end command.
     *
     * @return the {@link JobDetail}-object to be used at further processing
     */
    protected JobDetail createJob(String content, Event event, boolean isStartEvent) {
        String jobIdentity = event.getICalUID() + (isStartEvent ? "_start" : "_end");

        if (StringUtils.isBlank(content)) {
            logger.debug("content of job '{}' is empty -> no task will be created!", jobIdentity);
            return null;
        }

        JobDetail job = newJob(ExecuteCommandJob.class).usingJobData(ExecuteCommandJob.JOB_DATA_CONTENT_KEY, content)
                .withIdentity(jobIdentity, GCAL_SCHEDULER_GROUP).build();

        return job;
    }

    /**
     * Creates a set quartz-triggers for <code>job</code>. For each {@link When}
     * object of <code>event</code> a new trigger is created. That is the case
     * in recurring events where gcal creates one event (with one unique IcalUID)
     * and a set of {@link When}-object for each occurrence.
     *
     * @param job the {@link Job} to create triggers for
     * @param event the {@link CalendarEventEntry} to read the {@link When}-objects
     *            from
     * @param modifiedByEvent defines the name of an event which modifies the
     *            schedule of the new Trigger
     * @param isStartEvent indicator to identify whether this trigger will be
     *            triggering a start or an end command.
     *
     * @throws SchedulerException if there is an internal Scheduler error.
     */
    protected boolean createTriggerAndSchedule(JobDetail job, Event event, String modifiedByEvent,
            boolean isStartEvent) {
        boolean triggersCreated = false;

        if (job == null) {
            logger.debug("job is null -> no triggers are created");
            return false;
        }

        String jobIdentity = event.getICalUID() + (isStartEvent ? "_start" : "_end");

        EventDateTime date = isStartEvent ? event.getStart() : event.getEnd();
        long dateValue = date.getDateTime().getValue();

        /*
         * TODO: TEE: do only create a new trigger when the start/endtime
         * lies in the future. This exclusion is necessary because the SimpleTrigger
         * triggers a job even if the startTime lies in the past. If somebody
         * knows the way to let quartz ignore such triggers this exclusion
         * can be omitted.
         */
        if (dateValue >= (new Date()).getTime()) {

            Trigger trigger;

            if (StringUtils.isBlank(modifiedByEvent)) {
                trigger = newTrigger().forJob(job)
                        .withIdentity(jobIdentity + "_" + dateValue + "_trigger", GCAL_SCHEDULER_GROUP)
                        .startAt(new Date(dateValue)).build();
            } else {
                trigger = newTrigger().forJob(job)
                        .withIdentity(jobIdentity + "_" + dateValue + "_trigger", GCAL_SCHEDULER_GROUP)
                        .startAt(new Date(dateValue)).modifiedByCalendar(modifiedByEvent).build();
            }

            try {
                scheduler.scheduleJob(job, trigger);
                triggersCreated = true;
            } catch (SchedulerException se) {
                logger.warn("scheduling Trigger '{}' throws an exception: {}", trigger, se);
            }
        }
        // }
        return triggersCreated;
    }

    /**
     * Creates a detailed description of a <code>job</code> for logging purpose.
     *
     * @param job the job to create a detailed description for
     * @return a detailed description of the new <code>job</code>
     */
    private String createJobInfo(Event event, JobDetail job) {
        if (job == null) {
            return "SchedulerJob [null]";
        }

        StringBuffer sb = new StringBuffer();
        sb.append("SchedulerJob [jobKey=").append(job.getKey().getName());
        sb.append(", jobGroup=").append(job.getKey().getGroup());

        try {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(job.getKey());

            sb.append(", ").append(triggers.size()).append(" triggers=[");

            int maxTriggerLogs = 24;
            for (int triggerIndex = 0; triggerIndex < triggers.size()
                    && triggerIndex < maxTriggerLogs; triggerIndex++) {
                Trigger trigger = triggers.get(triggerIndex);
                sb.append(trigger.getStartTime());
                if (triggerIndex < triggers.size() - 1 && triggerIndex < maxTriggerLogs - 1) {
                    sb.append(", ");
                }
            }

            if (triggers.size() >= maxTriggerLogs) {
                sb.append(" and ").append(triggers.size() - maxTriggerLogs).append(" more ...");
            }

            if (triggers.size() == 0) {
                sb.append("there are no triggers - probably the event lies in the past");
            }
        } catch (SchedulerException e) {
        }

        sb.append("], content=").append(event.getDescription());

        return sb.toString();
    }

    /**
     * Holds the parsed content of a GCal event
     *
     * @author Thomas.Eichstaedt-Engelen
     */
    class CalendarEventContent {
        String startCommands = "";
        String endCommands = "";
        String modifiedByEvent = "";
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            String usernameString = (String) config.get("client_id");
            if (!StringUtils.isBlank(usernameString)) {
                GCalGoogleOAuth.setClientId(usernameString);
            } else {
                logger.error(
                        "gcal:client_id must be configured in openhab.cfg. Refer to wiki how to create client_id/client_secret pair");
                throw new ConfigurationException("client_id",
                        "gcal:client_id must be configured in openhab.cfg. Refer to wiki how to create client_id/client_secret");
            }

            String passwordString = (String) config.get("client_secret");
            if (!StringUtils.isBlank(passwordString)) {
                GCalGoogleOAuth.setClientSecret(passwordString);
            } else {
                logger.error(
                        "gcal:client_secret must be configured in openhab.cfg. Refer to wiki how to create client_id/client_secret pair");
                throw new ConfigurationException("client_secret",
                        "gcal:client_secret must be configured in openhab.cfg. Refer to wiki how to create client_id/client_secret pair");
            }

            String urlString = (String) config.get("calendar_name");
            if (!StringUtils.isBlank(urlString)) {
                calendar_name = urlString;
            } else {
                logger.error(
                        "gcal:calendar_name must be configured in openhab.cfg. Calendar name or word \"primary\"  MUST be specified");
                throw new ConfigurationException("calendar_name",
                        "gcal:calendar_name must be configured in openhab.cfg. Calendar name or word \"primary\"  MUST be specified");
            }

            filter = (String) config.get("filter");

            String refreshString = (String) config.get("refresh");
            if (StringUtils.isNotBlank(refreshString)) {
                refreshInterval = Integer.parseInt(refreshString);
            }

            if (GCalGoogleOAuth.getCredential(true) == null) {
                logger.error("Cannnot obtain credential based on provided client_id/client_secret");
                throw new ConfigurationException("Credential error",
                        "Cannnot obtain credential based on provided client_id/client_secret");
            }

            setProperlyConfigured(true);
        }
    }

}
