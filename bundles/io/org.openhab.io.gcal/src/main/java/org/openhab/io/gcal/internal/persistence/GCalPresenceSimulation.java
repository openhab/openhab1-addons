/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.io.gcal.internal.persistence;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joda.time.DateTime;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.io.gcal.internal.GCalConfiguration;
import org.openhab.io.gcal.internal.GCalConnector;
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

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;


/**
 * This implementation of the {@link PersistenceService} provides Presence 
 * Simulation features based on the Google Calendar Service.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class GCalPresenceSimulation implements PersistenceService {

	private static final Logger logger =
		LoggerFactory.getLogger(GCalPresenceSimulation.class);
	
	private static final String GCAL_SCHEDULER_GROUP = "GoogleCalendar";
	
	/** the upload interval (optional, defaults to 10 seconds) */
	private static int uploadInterval = 10;
	
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
		return "presencesimulation";
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
		if (GCalConfiguration.isInitialized()) {
			String newAlias = alias != null ? alias : item.getName();
			
			CalendarEventEntry myEntry = new CalendarEventEntry();
				myEntry.setTitle(new PlainTextConstruct("[PresenceSimulation] " + newAlias));
				myEntry.setContent(new PlainTextConstruct(String.format(
					GCalConfiguration.executeScript, item.getName(), item.getState().toString())));

			DateTime nowPlusOffset = new DateTime().plusDays(GCalConfiguration.offset);
			
			com.google.gdata.data.DateTime time = 
				com.google.gdata.data.DateTime.parseDateTime(nowPlusOffset.toString()); 
			When eventTimes = new When();
				eventTimes.setStartTime(time);
				eventTimes.setEndTime(time);
			myEntry.addTime(eventTimes);
			
			entries.offer(myEntry);
			
			logger.trace("added new entry '{}' for item '{}' to upload queue", myEntry.getTitle().getPlainText(), item.getName());
		} else {
			logger.debug("GCal PresenceSimulation Service isn't configured properly! No entries will be uploaded to your Google Calendar");
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
			long startTime = System.currentTimeMillis();
			CalendarEventEntry createdEvent = GCalConnector.createCalendarEvent(entry);
			logger.debug("succesfully created new calendar event (title='{}', date='{}', content='{}') in {}ms",
				new Object[] { createdEvent.getTitle().getPlainText(), 
				createdEvent.getTimes().get(0).getStartTime().toString(),
				createdEvent.getPlainTextContent(),
				System.currentTimeMillis() - startTime});
		}
		
	}
	

}
