/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.io.gcal.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.service.AbstractActiveService;
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

	/** holds the local quartz scheduler instance */
	private Scheduler scheduler;
	
	private String username = "";
	private String password = "";
	private String url = "";
	private int refreshInterval = 900000;
	
	private boolean isProperlyConfigured = false;
	
	
	/**
	 * RegEx to extract the start and end commands 
	 * <code>'start ?\{(.*?)\}\s*end ?\{(.*)\}'</code> out of the Calendar-Event
	 * content
	 */
	private static final Pattern EXTRACT_START_END_COMMANDS = 
		Pattern.compile("start ?\\{(.*?)\\}\\s*end ?\\{(.*)\\}\\s*", Pattern.DOTALL);
		

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Google Calender Event-Downloader";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
				
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(url)) {
			logger.warn("username, password and url must not be blank -> gcal calendar login aborted");
			return;
		}
		
		// TODO: teichsta: there could be more than calender url in openHAB.cfg
		// for now we accept this limitation if downloading just one feed ...
		CalendarEventFeed myFeed = downloadEventFeed(url, username, password);
		
		if (myFeed != null) {
			
			List<CalendarEventEntry> entries = myFeed.getEntries();
			
			if (entries.size() > 0) {
				
				logger.info("found {} calendar events to process", entries.size());
				
				try {
					cleanJobs();
					processEntries(entries);
				}
		        catch (SchedulerException se) {
					logger.error("scheduling job throws exception", se);
				}		
			}
			else {
				logger.info("gcal feed contains no entries ...");
			}
		}
		
	}
	
	/**
	 * Connects to Google-Calendar Service and returns the specified Calender-Feed
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * 
	 * @return the corresponding Calendar-Feed or <code>null</code> if an error
	 * occurs
	 */
	private CalendarEventFeed downloadEventFeed(String url, String username, String password) {
		
		try {
			
			URL feedUrl = new URL(url);
			
			// TODO: teichsta: creation of the service could done earlier (and
			// only once
			CalendarService myService = new CalendarService("openHAB-event-downloader");
				myService.setUserCredentials(username, password);
			CalendarQuery myQuery = new CalendarQuery(feedUrl);
				myQuery.setMinimumStartTime(DateTime.now());
	
			return myService.getFeed(myQuery, CalendarEventFeed.class);
		}
		catch (AuthenticationException ae) {
			logger.error("authentication failed!", ae);
		}
		catch (Exception e) {
			logger.error("downloading CalenerEventFeed throws exception", e);
		}
		
		return null;
	}
	
	/**
	 * Delete all {@link Job}s of the group <code>GCAL_SCHEDULER_GROUP</code>
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	@SuppressWarnings("unchecked")
	private void cleanJobs() throws SchedulerException {
		Set<JobKey> jobKeys = 
			scheduler.getJobKeys(groupEquals(GCAL_SCHEDULER_GROUP));
		scheduler.deleteJobs(new ArrayList<JobKey>(jobKeys));
	}
	
	/**
	 * Iterates over <code>entries</code>, extracts the event content and
	 * creates quartz jobs and corresponding triggers for each event.
	 *  
	 * @param entries the Calendar events to create quart job for. 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void processEntries(List<CalendarEventEntry> entries) throws SchedulerException {
		
		checkForFullCalendarFeed(entries);
		
		for (CalendarEventEntry event : entries) {
			
			String plainText = event.getPlainTextContent();
			
			if (StringUtils.isBlank(plainText)) {
				logger.debug("skipped event '{}' with no content", event.getTitle().getPlainText());
			}
			else {
				
				String[] content = parseEventContent(plainText);
				
				JobDetail startJob = createAndScheduleJob(content[0], event, true);
				boolean triggersCreated = createAndScheduleTrigger(startJob, event, true);
				
				if (triggersCreated) {
					logger.info("created new startJob '{}' with details '{}'", 
						event.getTitle().getPlainText(), createJobInfo(event, startJob));
				}
				
				// do only create end-jobs if there are end-commands ...
				if (StringUtils.isNotBlank(content[1])) {
					JobDetail endJob = createAndScheduleJob(content[1], event, false);
					triggersCreated = createAndScheduleTrigger(endJob, event, false);
					
					if (triggersCreated) {
						logger.info("created new endJob '{}' with details '{}'",
							event.getTitle().getPlainText(), createJobInfo(event, endJob));
					}
				}

			}
		}
	}

	/**
	 * <p>
	 * Extracts start and end-commands from <code>content</code>. Start-Commands
	 * will be executed at start-time and End-Commands will be executed at end-time
	 * of the calendar-event.
	 * </p><p>
	 * If the RegExp <code>EXTRACT_START_END_COMMANDS</code> doen't match the
	 * complete content is interpreted as set of Start-Commands.
	 * </p>
	 * 
	 * @param content the set of Start- and End-Commands
	 * 
	 * @return an array containing two Strings. The first String contains the
	 * Start-Commands the second contains the End-Commands. If the RegExp didn't
	 * match the second String is empty.
	 */
	protected String[] parseEventContent(String content) {
		
		Matcher matcher = EXTRACT_START_END_COMMANDS.matcher(content);
		
		String startCommands = "";
		String endCommands = "";

		if (!matcher.matches()) {
			startCommands = content.toString();
			logger.debug("given event content doesn't match regular expression for " +
				"extracting start- and stopCommands -> using whole content as startCommand instead ({})", content.toString());
		}
		else {
			matcher.reset();
			matcher.find();
			
			startCommands = matcher.group(1);
			endCommands = matcher.group(2);
		}
	
		return new String[] { startCommands, endCommands };
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
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	protected JobDetail createAndScheduleJob(String content, CalendarEventEntry event, boolean isStartEvent) throws SchedulerException {
		
		String jobIdentity = event.getIcalUID() + (isStartEvent ? "_start" : "_end");
		
		if (StringUtils.isBlank(content)) {
			logger.debug("content of job '" + jobIdentity + "' is empty -> no task will be created!");
			return null;
		}

        JobDetail job = newJob(ExecuteCommandJob.class)
        	.usingJobData(ExecuteCommandJob.JOB_DATA_CONTENT_KEY, content)
            .withIdentity(jobIdentity, GCAL_SCHEDULER_GROUP)
            .build();
        
		scheduler.addJob(job, true);

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
	 * @param isStartEvent indicator to identify whether this trigger will be
	 * triggering a start or an end command.
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	protected boolean createAndScheduleTrigger(JobDetail job, CalendarEventEntry event, boolean isStartEvent) throws SchedulerException {
		
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
		        Trigger trigger = newTrigger()
		            .forJob(job)
		            .withIdentity(jobIdentity + "_" + dateValue + "_trigger", GCAL_SCHEDULER_GROUP)
		            .startAt(new Date(dateValue))
		            .build();
	
				scheduler.scheduleJob(trigger);
				triggersCreated = true;
			}
		} 
		
		return triggersCreated;
	}

	/**
	 * Checks the first {@link CalendarEventEntry} of <code>entries</code> for
	 * completeness. If this first event is incomplete all other events will be
	 * incomplete as well.
	 * 
	 * @param entries the set to check 
	 */
	private void checkForFullCalendarFeed(List<CalendarEventEntry> entries) {
		
		if (entries != null && !entries.isEmpty()) {
			
			CalendarEventEntry referenceEvent = entries.get(0);
			if (referenceEvent.getIcalUID() == null || referenceEvent.getTimes().isEmpty()) {
				logger.warn("calender entries are incomplete - please asure to use the full calendar feed");
			}
			
		}
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
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		
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
			
			String refreshString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshString)) {
				refreshInterval = Integer.parseInt(refreshString);
			}
			
	        try {
	            scheduler = StdSchedulerFactory.getDefaultScheduler();
				isProperlyConfigured = true;
				start();
	        }
	        catch (SchedulerException se) {
	            logger.error("initializing scheduler throws exception", se);
	        }

		}

	}
	
	
}
