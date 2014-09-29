/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.gcal.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
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

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;


/**
 * This implementation of the {@link PersistenceService} provides Presence 
 * Simulation features based on the Google Calendar Service.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class GCalPersistenceService implements PersistenceService, ManagedService {

	private static final Logger logger =
		LoggerFactory.getLogger(GCalPersistenceService.class);
	
	private static final String GCAL_SCHEDULER_GROUP = "GoogleCalendar";
	
	/** the upload interval (optional, defaults to 10 seconds) */
	private static int uploadInterval = 10;

	private static String username = "";
	private static String password = "";
	private static String url = "";
	
	/** the offset (in days) which will used to store future events */
	private static int offset = 14;
	
	/**
	 * the base script which is written to the newly created Calendar-Events by
	 * the GCal based presence simulation. It must contain two format markers
	 * <code>%s</code>. The first marker represents the Item to send the command
	 * to and the second represents the State.
	 */
	private static String executeScript = 
		"> if (PresenceSimulation.state == ON) %s.sendCommand(%s)";

	/** indicated whether this service was properly initialized */ 
	private boolean initialized = false;
	
	/** holds the Google Calendar entries to upload to Google */
	private static Queue<CalendarEventEntry> entries = new ConcurrentLinkedQueue<CalendarEventEntry>();
	
	
	public void activate() {
		scheduleUploadJob();
	}
	
	public void deactivate() {
		cancelAllJobs();
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "gcal";
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		store(item, item.getName());
	}

	/**
	 * Creates a new Google Calendar Entry for each <code>item</code> and adds
	 * it to the processing queue. The entries' title will either be the items
	 * name or <code>alias</code> if it is <code>!= null</code>.
	 * 
	 * The new Calendar Entry will contain a single command to be executed e.g.<br>
	 * <p><code>send &lt;item.name&gt; &lt;item.state&gt;</code></p>
	 * 
	 * @param item the item which state should be persisted.
	 * @param alias the alias under which the item should be persisted.
	 */
	public void store(final Item item, final String alias) {
		if (initialized) {
			String newAlias = alias != null ? alias : item.getName();
			
			CalendarEventEntry myEntry = new CalendarEventEntry();
				myEntry.setTitle(new PlainTextConstruct("[PresenceSimulation] " + newAlias));
				myEntry.setContent(new PlainTextConstruct(String.format(
					executeScript, item.getName(), item.getState().toString())));

			DateTime nowPlusOffset = new DateTime().plusDays(offset);
			
			com.google.gdata.data.DateTime time = 
				com.google.gdata.data.DateTime.parseDateTime(nowPlusOffset.toString()); 
			When eventTimes = new When();
				eventTimes.setStartTime(time);
				eventTimes.setEndTime(time);
			myEntry.addTime(eventTimes);
			
			entries.offer(myEntry);
			
			logger.trace("added new entry '{}' for item '{}' to upload queue", myEntry.getTitle().getPlainText(), item.getName());
		} else {
			logger.debug("GCal PresenceSimulation Service isn't initialized properly! No entries will be uploaded to your Google Calendar");
		}
	}
	
	/**
	 * Schedules new quartz scheduler job for uploading calendar entries to Google
	 */
	private void scheduleUploadJob() {
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			JobDetail job = newJob(SynchronizationJob.class)
				.withIdentity("Upload_GCal-Entries", GCAL_SCHEDULER_GROUP)
			    .build();

			SimpleTrigger trigger = newTrigger()
			    .withIdentity("Upload_GCal-Entries", GCAL_SCHEDULER_GROUP)
			    .withSchedule(repeatSecondlyForever(uploadInterval))
			    .build();

			sched.scheduleJob(job, trigger);
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
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			Set<JobKey> jobKeys = sched.getJobKeys(jobGroupEquals(GCAL_SCHEDULER_GROUP));
			if (jobKeys.size() > 0) {
				sched.deleteJobs(new ArrayList<JobKey>(jobKeys));
				logger.debug("Found {} Google Calendar Upload-Jobs to delete from DefaulScheduler (keys={})", jobKeys.size(), jobKeys);
			}
		} catch (SchedulerException e) {
			logger.warn("Couldn't remove Google Calendar Upload-Job: {}", e.getMessage());
		}		
	}
	
	
	/**
	 * A quartz scheduler job to upload {@link CalendarEventEntry}s to
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
			for (CalendarEventEntry entry : entries) {
				upload(entry);
				entries.remove(entry);
			}
		}
		
		private void upload(CalendarEventEntry entry) {
			try {
				long startTime = System.currentTimeMillis();
				CalendarEventEntry createdEvent = createCalendarEvent(username, password, url, entry);
				logger.debug("succesfully created new calendar event (title='{}', date='{}', content='{}') in {}ms",
					new Object[] { createdEvent.getTitle().getPlainText(), 
					createdEvent.getTimes().get(0).getStartTime().toString(),
					createdEvent.getPlainTextContent(),
					System.currentTimeMillis() - startTime});
			}
			catch (AuthenticationException ae) {
				logger.error("authentication failed: {}", ae.getMessage());
			}
			catch (Exception e) {
				logger.error("creating a new calendar entry throws an exception: {}", e.getMessage());
			}
		}
		
		/**
		 * Creates a new calendar entry.
		 * 
		 * @param event the event to create in the remote calendar identified by the
		 * full calendar feed configured in </code>openhab.cfg</code>
		 * @return the newly created entry
		 * @throws ServiceException 
		 * @throws IOException 
		 */
		private CalendarEventEntry createCalendarEvent(String username, String password, String url, CalendarEventEntry event) throws IOException, ServiceException {
			CalendarService myService = new CalendarService("openHAB");
				myService.setUserCredentials(username, password);
			URL feedUrl = new URL(url);
				
			return myService.insert(feedUrl, event);
		}	
		
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
			
			String offsetString = (String) config.get("offset");
			if (StringUtils.isNotBlank(offsetString)) {
				try {
					offset = Integer.valueOf(offsetString);
				}
				catch (IllegalArgumentException iae) {
					logger.warn("couldn't parse '{}' to an integer");
				}
			}
			
			String executeScriptString = (String) config.get("executescript");
			if (StringUtils.isNotBlank(executeScriptString)) {
				executeScript = executeScriptString;
			}
			
			initialized = true;
		}
	}
	

}
