/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.LongRange;
import org.openhab.core.service.AbstractActiveService;
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

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;


/**
 * Service which downloads Calendar events, parses their content and creates
 * Quartz-jobs and triggers out of them.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class GCalEventDownloader extends AbstractActiveService implements ManagedService {

	private static final String GCAL_SCHEDULER_GROUP = "gcal";

	private static final Logger logger = 
		LoggerFactory.getLogger(GCalEventDownloader.class);
	
	private static String username = "";
	private static String password = "";
	private static String url = "";
	private static String filter = "";
	
	/** holds the current refresh interval, default to 900000ms (15 minutes) */
	public static int refreshInterval = 900000;
	

	/** holds the local quartz scheduler instance */
	private Scheduler scheduler;
	
	
	/**
	 * RegEx to extract the start and end commands from the Calendar-Event content.
	 * (<code>'start\s*?\{(.*?)\}\s*end\s*?\{(.*?)\}\s*'</code>)
	 */
	private static final Pattern EXTRACT_STARTEND_CONTENT = 
		Pattern.compile("start\\s*?\\{(.*?)\\}\\s*end\\s*?\\{(.*?)\\}\\s*", Pattern.DOTALL);
	
	/**
	 * RegEx to extract the modified by command from the Calendar-Event content.
	 * (<code>'(.*?)modified by\s*?\{(.*?)\}.*'</code>)
	 */
	private static final Pattern EXTRACT_MODIFIEDBY_CONTENT = 
		Pattern.compile("(.*?)modified by\\s*?\\{(.*?)\\}.*", Pattern.DOTALL);
	

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Google Calender Event-Downloader";
	}
	
	@Override
	public void activate() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            super.activate();
        }
        catch (SchedulerException se) {
            logger.error("initializing scheduler throws exception", se);
        }
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		CalendarEventFeed myFeed = downloadEventFeed(username, password, url, refreshInterval);
		if (myFeed != null) {
			List<CalendarEventEntry> entries = myFeed.getEntries();
			
			if (entries.size() > 0) {
				logger.debug("found {} calendar events to process", entries.size());
				
				try {
					if (scheduler.isShutdown()) {
						logger.warn("Scheduler has been shut down - probably due to exceptions?");
					}
					cleanJobs();
					processEntries(entries);
				}
		        catch (SchedulerException se) {
					logger.error("scheduling jobs throws exception", se);
				}		
			}
			else {
				logger.debug("gcal feed contains no events ...");
			}
		}
	}
	
	/**
	 * Connects to Google-Calendar Service and returns the specified Calender-Feed
	 * <code>url</code>, <code>username</code> and <code>password</code> are taken
	 * from the corresponding config parameter in <code>openhab.cfg</code>. 
	 * 
	 * @param url the {@link URL} of the full Google Calendar-Feed
	 * @param username
	 * @param password
	 * 
	 * @return the corresponding Calendar-Feed or <code>null</code> if an error
	 * occurs. <i>Note:</i> We do only return events if their startTime lies between
	 * <code>now</code> and <code>now + 2 * refreshInterval</code> to reduce
	 * the amount of events to process.
	 */
	public static CalendarEventFeed downloadEventFeed(String username, String password, String url, int refreshInterval) {
		// TODO: teichsta: there could be more than one calender url in openHAB.cfg
		// for now we accept this limitation of downloading just one feed ...
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(url)) {
			logger.warn("username, password and url must not be blank -> gcal calendar login aborted");
			return null;
		}
		
		try {
			URL feedUrl = new URL(url);
			
			CalendarService myService = new CalendarService("openHAB");
				myService.setUserCredentials(username, password);
			CalendarQuery myQuery = new CalendarQuery(feedUrl);
				myQuery.setMinimumStartTime(DateTime.now());
				myQuery.setMaximumStartTime(new DateTime(DateTime.now().getValue() + (2 * refreshInterval)));
			
			// add the fulltext filter if it has been configured
			if (StringUtils.isNotBlank(filter)) {
				myQuery.setFullTextQuery(filter);
			}
	
			CalendarEventFeed feed = myService.getFeed(myQuery, CalendarEventFeed.class);
			if (feed != null) {
				checkIfFullCalendarFeed(feed.getEntries());
			}
			
			return feed;
		}
		catch (AuthenticationException ae) {
			logger.error("authentication failed: {}", ae.getMessage());
		}
		catch (Exception e) {
			logger.error("downloading CalenerEventFeed throws exception: {}", e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Checks the first {@link CalendarEventEntry} of <code>entries</code> for
	 * completeness. If this first event is incomplete all other events will be
	 * incomplete as well.
	 * 
	 * @param entries the set to check 
	 */
	private static void checkIfFullCalendarFeed(List<CalendarEventEntry> entries) {
		if (entries != null && !entries.isEmpty()) {
			CalendarEventEntry referenceEvent = entries.get(0);
			if (referenceEvent.getIcalUID() == null || referenceEvent.getTimes().isEmpty()) {
				logger.warn("calender entries are incomplete - please make sure to use the full calendar feed");
			}
			
		}
	}
	
	/**
	 * Delete all {@link Job}s of the group <code>GCAL_SCHEDULER_GROUP</code>
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void cleanJobs() throws SchedulerException {
		Set<JobKey> jobKeys = 
			scheduler.getJobKeys(jobGroupEquals(GCAL_SCHEDULER_GROUP));
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
	 * <li>create a {@link TimeRangeCalendar} for each event (unique by title) and add a TimeRange for each {@link When}</li>
	 * <li>add each {@link TimeRangeCalendar} to the {@link Scheduler}</li>
	 * <li>find events with content</li>
	 * <li>add a Job with the corresponding Triggers for each event</li>
	 * </ul> 
	 *  
	 * @param entries the GCalendar events to create quart jobs for. 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void processEntries(List<CalendarEventEntry> entries) throws SchedulerException {
		Map<String, TimeRangeCalendar> calendarCache = new HashMap<String, TimeRangeCalendar>();
		
		// find all events with empty content - these events are taken to modify
		// the scheduler
		for (CalendarEventEntry event : entries) {
			String eventContent = event.getPlainTextContent();
			String eventTitle = event.getTitle().getPlainText();
			
			if (StringUtils.isBlank(eventContent)) {
				logger.debug("found event '{}' with no content, add this event to the excluded " +
					"TimeRangesCalendar - this event could be referenced by the modifiedBy clause",
					eventTitle);
				
				if (!calendarCache.containsKey(eventTitle)) {
					calendarCache.put(eventTitle, new TimeRangeCalendar());
				}
				TimeRangeCalendar timeRangeCalendar = calendarCache.get(eventTitle);
		    	for (When when : event.getTimes()) {
		    		timeRangeCalendar.addTimeRange(new LongRange(when.getStartTime().getValue(), when.getEndTime().getValue()));
		    	}
			}
		}
		
		// add all calendars to the Scheduler an rebase all existing Triggers
		// the calendars has to be added first, to schedule Triggers successfully
		for (Entry<String, TimeRangeCalendar> entry : calendarCache.entrySet()) {
			scheduler.addCalendar(entry.getKey(), entry.getValue(), true, true);
		}

		// now we process all events with content
		for (CalendarEventEntry event : entries) {
			String eventContent = event.getPlainTextContent();
			String eventTitle = event.getTitle().getPlainText();
			
			if (StringUtils.isNotBlank(eventContent)) {
				CalendarEventContent cec = parseEventContent(eventContent);
				
				String modifiedByEvent = null;
				if (calendarCache.containsKey(cec.modifiedByEvent)) {
					modifiedByEvent = cec.modifiedByEvent;
				}
				
				JobDetail startJob = createJob(cec.startCommands, event, true);
				boolean triggersCreated = 
					createTriggerAndSchedule(startJob, event, modifiedByEvent, true);
				
				if (triggersCreated) {
					logger.info("created new startJob '{}' with details '{}'", 
						eventTitle, createJobInfo(event, startJob));
				}
				
				// do only create end-jobs if there are end-commands ...
				if (StringUtils.isNotBlank(cec.endCommands)) {
					JobDetail endJob = createJob(cec.endCommands, event, false);
					triggersCreated = createTriggerAndSchedule(endJob, event, modifiedByEvent, false);
					
					if (triggersCreated) {
						logger.info("created new endJob '{}' with details '{}'",
							eventTitle, createJobInfo(event, endJob));
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
	 * </p><p>
	 * If the RegExp <code>EXTRACT_STARTEND_CONTENT</code> doen't match the
	 * complete content is taken as set of Start-Commands.
	 * </p>
	 * 
	 * @param content the set of Start- and End-Commands
	 * @return the parsed event content
	 */
	protected CalendarEventContent parseEventContent(String content) {
		CalendarEventContent eventContent = new CalendarEventContent();
		String commandContent;

		Matcher modifiedByMatcher = EXTRACT_MODIFIEDBY_CONTENT.matcher(content);
		if (modifiedByMatcher.find()) {
			commandContent = modifiedByMatcher.group(1);
			eventContent.modifiedByEvent = StringUtils.trimToEmpty(modifiedByMatcher.group(2));
		}
		else {
			commandContent = content;
		}
			
		Matcher startEndMatcher = EXTRACT_STARTEND_CONTENT.matcher(commandContent);
		if (startEndMatcher.find()) {
			eventContent.startCommands = StringUtils.trimToEmpty(startEndMatcher.group(1));
			eventContent.endCommands = StringUtils.trimToEmpty(startEndMatcher.group(2));
		}
		else {
			eventContent.startCommands = StringUtils.trimToEmpty(commandContent);
			logger.debug("given event content doesn't match regular expression to " +
				"extract start-, end commands - using whole content as startCommand ({})", commandContent);
		}
	
		return eventContent;
	}
	
	/**
	 * Creates a new quartz-job with jobData <code>content</code> in the scheduler
	 * group <code>GCAL_SCHEDULER_GROUP</code> if <code>content</code> is not
	 * blank.
	 * 
	 * @param content the set of commands to be executed by the
	 * {@link ExecuteCommandJob} later on
	 * @param event
	 * @param isStartEvent indicator to identify whether this trigger will be
	 * triggering a start or an end command.
	 * 
	 * @return the {@link JobDetail}-object to be used at further processing
	 */
	protected JobDetail createJob(String content, CalendarEventEntry event, boolean isStartEvent) {
		String jobIdentity = event.getIcalUID() + (isStartEvent ? "_start" : "_end");
		
		if (StringUtils.isBlank(content)) {
			logger.debug("content of job '" + jobIdentity + "' is empty -> no task will be created!");
			return null;
		}

        JobDetail job = newJob(ExecuteCommandJob.class)
        	.usingJobData(ExecuteCommandJob.JOB_DATA_CONTENT_KEY, content)
            .withIdentity(jobIdentity, GCAL_SCHEDULER_GROUP)
            .build();
        
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
	 * from
	 * @param modifiedByEvent defines the name of an event which modifies the
	 * schedule of the new Trigger
	 * @param isStartEvent indicator to identify whether this trigger will be
	 * triggering a start or an end command.
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	protected boolean createTriggerAndSchedule(JobDetail job, CalendarEventEntry event, String modifiedByEvent, boolean isStartEvent) {
		boolean triggersCreated = false;
		
		if (job == null) {
			logger.debug("job is null -> no triggers are created");
			return false;
		}
		
		String jobIdentity = event.getIcalUID() + (isStartEvent ? "_start" : "_end");

		List<When> times = event.getTimes();
		for (When time : times) {
			DateTime date = 
				isStartEvent ? time.getStartTime() : time.getEndTime();
			long dateValue = date.getValue();
			
			/* TODO: TEE: do only create a new trigger when the start/endtime 
			 * lies in the future. This exclusion is necessary because the SimpleTrigger
			 * triggers a job even if the startTime lies in the past. If somebody
			 * knows the way to let quartz ignore such triggers this exclusion
			 * can be omitted. */
			if (dateValue >= DateTime.now().getValue()) { 
				
				Trigger trigger;
				
				if (StringUtils.isBlank(modifiedByEvent)) {
			        trigger = newTrigger()
			            .forJob(job)
			            .withIdentity(jobIdentity + "_" + dateValue + "_trigger", GCAL_SCHEDULER_GROUP)
			            .startAt(new Date(dateValue))
			            .build();
				} else {
			        trigger = newTrigger()
			            .forJob(job)
			            .withIdentity(jobIdentity + "_" + dateValue + "_trigger", GCAL_SCHEDULER_GROUP)
			            .startAt(new Date(dateValue))
			            .modifiedByCalendar(modifiedByEvent)
			            .build();
				}
	
				try {
					scheduler.scheduleJob(job, trigger);
					triggersCreated = true;
				}
				catch (SchedulerException se) {
					logger.warn("scheduling Trigger '" + trigger + "' throws an exception.", se);
				}
			}
		} 
		return triggersCreated;
	}
	
	/**
	 * Creates a detailed description of a <code>job</code> for logging purpose.
	 * 
	 * @param job the job to create a detailed description for
	 * @return a detailed description of the new <code>job</code>
	 */
	private String createJobInfo(CalendarEventEntry event, JobDetail job) {
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
			for (int triggerIndex = 0; triggerIndex < triggers.size() && triggerIndex < maxTriggerLogs; triggerIndex++) {
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
		}
		catch (SchedulerException e) {
		}
		
		sb.append("], content=").append(event.getPlainTextContent());
		
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
			String usernameString = (String) config.get("username");
			username = usernameString;
			if (StringUtils.isBlank(username)) {
				throw new ConfigurationException("gcal:username", "username must not be blank - please configure an aproppriate username in openhab.cfg");
			}

			String passwordString = (String) config.get("password");
			password = passwordString;
			if (StringUtils.isBlank(password)) {
				throw new ConfigurationException("gcal:password", "password must not be blank - please configure an aproppriate password in openhab.cfg");
			}

			String urlString = (String) config.get("url");
			url = urlString;
			if (StringUtils.isBlank(url)) {
				throw new ConfigurationException("gcal:url", "url must not be blank - please configure an aproppriate url in openhab.cfg");
			}
			
			filter = (String) config.get("filter");
			
			String refreshString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshString)) {
				refreshInterval = Integer.parseInt(refreshString);
			}
			
			setProperlyConfigured(true);
		}
	}
	
	
}
