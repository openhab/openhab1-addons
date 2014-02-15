/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.db4o.internal;

import static org.openhab.persistence.db4o.internal.Db4oConfiguration.backupInterval;
import static org.openhab.persistence.db4o.internal.Db4oConfiguration.commitInterval;
import static org.openhab.persistence.db4o.internal.Db4oConfiguration.maxBackups;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
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

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oException;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.query.Query;


/**
 * This is a {@link PersistenceService} implementation using the db4o database.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class Db4oPersistenceService implements QueryablePersistenceService {

	private static final Logger logger = LoggerFactory.getLogger(Db4oPersistenceService.class);
	
	private static final String SERVICE_NAME = "db4o";
	
	private static final String DB_FOLDER_NAME = "etc/db4o";
	private static final String DB_FILE_NAME = "store.db4o";

	private static final String SCHEDULER_GROUP = "DB4O_SchedulerGroup";
	
	private static ObjectContainer db;
	
	
	public String getName() {
		return SERVICE_NAME;
	}
	
	public void activate() {
		File folder = new File(DB_FOLDER_NAME);
		if(!folder.exists()) {
			folder.mkdir();
		}
	    openDbFile();
	    Db4oItem.configure(db.ext().configure());
	    
	    scheduleJob();
	}

	public void deactivate() {
		cancelAllJobs();
		
		if(db!=null) {
			db.close();
			db = null;
		}
	}
	

	public void store(Item item) {
		store(item, null);
	}

	public void store(Item item, String alias) {
		if(alias==null) alias = item.getName();
		
		Db4oItem historicItem = new Db4oItem();
		historicItem.setName(alias);
		historicItem.setState(item.getState());
		historicItem.setTimestamp(new Date());
		
		try {
			db.store(historicItem);
			logger.debug("Stored item state '{}' -> '{}'", new String[] {historicItem.getName(), historicItem.getState().toString() } );
		} catch(Db4oException e) {
			db.rollback();
			logger.warn("Error storing state for item '{}' as '{}': {}", new String[] { item.getName(), alias, e.getMessage() });
		}
	}

	public Iterable<HistoricItem> query(FilterCriteria filter) {
		Query query = queryWithReconnect();
		
		if (query != null) {
			query.constrain(Db4oItem.class);
			
			if (filter==null) {
				filter = new FilterCriteria();
			}
			if (filter.getBeginDate()!=null) {
				query.descend("timestamp").constrain(filter.getBeginDate()).greater().equal();
			}
			if (filter.getEndDate()!=null) {
				query.descend("timestamp").constrain(filter.getEndDate()).smaller().equal();
			}
			if (filter.getItemName()!=null) {
				query.descend("name").constrain(filter.getItemName()).equal();
			}
			if (filter.getState()!=null && filter.getOperator()!=null) {
				switch(filter.getOperator()) {
					case EQ : query.descend("state").constrain(filter.getState()).equal(); break;
					case GT : query.descend("state").constrain(filter.getState()).greater(); break;
					case LT : query.descend("state").constrain(filter.getState()).smaller(); break;
					case NEQ : query.descend("state").constrain(filter.getState()).equal().not(); break;
					case GTE : query.descend("state").constrain(filter.getState()).greater().equal(); break;
					case LTE : query.descend("state").constrain(filter.getState()).smaller().equal(); break;
				}
			}
			
			if(filter.getOrdering()==Ordering.ASCENDING) {
				query.descend("timestamp").orderAscending();
			} else {
				query.descend("timestamp").orderDescending();
			}
			ObjectSet<HistoricItem> results = query.execute();
	
			int startIndex = filter.getPageNumber() * filter.getPageSize();
			if (startIndex < results.size()) {
				int endIndex = startIndex + filter.getPageSize();
				if(endIndex > results.size()) {
					endIndex = results.size();
				}
				return results.subList(startIndex, endIndex);
			}
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Creates a new Query and returns it. In case the Database is closed for
	 * some reason we'll try to reopen it again and try to create a query a
	 * second time. If that fails too <code>null</code> is returned. 
	 * 
	 * @return a Query-Object or <code>null</code> if there are errors or the
	 * Database couldn't be opened again.
	 */
	private Query queryWithReconnect() {
		Query query = null;
		try {
			query = db.query();
		} catch (DatabaseClosedException dce) {
			logger.debug("Database '{}' is closed, we'll try to reopen it again ...");
			openDbFile();
			query = db.query();
		}
		return query;
	}

	private static void openDbFile() {
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FOLDER_NAME + File.separator + DB_FILE_NAME);
	}
	

	/**
	 * Schedules new quartz scheduler jobs for committing transactions and 
	 * backing up the database
	 */
	private void scheduleJob() {
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			
			// schedule commit-job
			JobDetail job = newJob(CommitJob.class)
				.withIdentity("Commit_Transaction", SCHEDULER_GROUP)
			    .build();

			SimpleTrigger trigger = newTrigger()
			    .withIdentity("Commit_Transaction", SCHEDULER_GROUP)
			    .withSchedule(repeatSecondlyForever(commitInterval))
			    .build();

			sched.scheduleJob(job, trigger);
			logger.debug("Scheduled Commit-Job with interval {}sec.", commitInterval);
			
			// schedule backup-job
			JobDetail backupJob = newJob(BackupJob.class)
					.withIdentity("Backup_DB", SCHEDULER_GROUP)
				    .build();

			CronTrigger backupTrigger = newTrigger()
				    .withIdentity("Backup_DB", SCHEDULER_GROUP)
				    .withSchedule(CronScheduleBuilder.cronSchedule(backupInterval))
				    .build();

			sched.scheduleJob(backupJob, backupTrigger);
			logger.debug("Scheduled Backup-Job with cron expression '{}'", backupInterval);
		} catch (SchedulerException e) {
			logger.warn("Could not create Job: {}", e.getMessage());
		}		
	}

	/**
	 * Delete all quartz scheduler jobs of the group <code>Dropbox</code>.
	 */
	private void cancelAllJobs() {
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			Set<JobKey> jobKeys = sched.getJobKeys(jobGroupEquals(SCHEDULER_GROUP));
			if (jobKeys.size() > 0) {
				sched.deleteJobs(new ArrayList<JobKey>(jobKeys));
				logger.debug("Found {} DB4O-Jobs to delete from DefaulScheduler (keys={})", jobKeys.size(), jobKeys);
			}
		} catch (SchedulerException e) {
			logger.warn("Couldn't remove Commit-Job: {}", e.getMessage());
		}		
	}
	
	
	/**
	 * A quartz scheduler job to commit the db4o transaction frequently. There
	 * can be only one instance of a specific job type running at the same time.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.0.0
	 */
	@DisallowConcurrentExecution
	public static class CommitJob implements Job {
		
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			long startTime = System.currentTimeMillis();
			try {
				db.commit();
				logger.trace("successfully commited db4o transaction in {}ms", System.currentTimeMillis() - startTime);
			} catch(Db4oException e) {
				try {
					db.rollback();
					logger.warn("Error committing transaction : {}", e.getMessage());
				} catch(DatabaseClosedException dce) {
					// ignore a failed rollback if database is closed (what happens regularly during shutdown)
					logger.debug("Cannot roll back transaction because database is closed: {}", e.getMessage());
				}
			}
		}
		
	}
	
	/**
	 * A quartz scheduler job to backup the db4o database frequently. It also
	 * removes obsolete backup files if the maximum amount is reached. There
	 * can be only one instance of a specific job type running at the same time.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.0.0
	 */
	@DisallowConcurrentExecution
	public static class BackupJob implements Job {
		
		final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			long startTime = System.currentTimeMillis();
			String backupFileName = DB_FOLDER_NAME + File.separator + 
				DATE_FORMATTER.format(System.currentTimeMillis()) + "_" + DB_FILE_NAME + ".bak";
			
			removeObsoleteBackupFiles(DB_FOLDER_NAME);
			try {
				ExtObjectContainer extDb = db.ext();
				if (!extDb.isClosed()) {
					extDb.backup(backupFileName);
					logger.debug("successfully created new DB4O backup '{}' in {}ms", backupFileName, System.currentTimeMillis() - startTime);
				} else {
					logger.debug("couldn't create DB4O backup '{}' because db is closed", backupFileName);
				}
			} catch(Db4oException e) {
				logger.warn("Error creating backup '{}': {}", backupFileName, e.getMessage());
			}
		}
		
		/**
		 * Removes the oldest x backup files from the file system.
		 * 
		 * @param dbFolderName the name of the folder where the db4o date file
		 * is stored
		 */
		protected void removeObsoleteBackupFiles(String dbFolderName) {
			File dbFolder = new File(dbFolderName);
			if (dbFolder.exists() && dbFolder.isDirectory()) {
				File[] backupFiles = dbFolder.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(DB_FILE_NAME + ".bak");
					}
				});
				
				Arrays.sort(backupFiles);
				
				if (backupFiles.length > maxBackups) {
					logger.debug("found {} backup files but only {} are allowed. will remove the oldest {} file(s) now",
						new Object[] { backupFiles.length, maxBackups, backupFiles.length - maxBackups });
					for (int index = 0; index < backupFiles.length - maxBackups; index++) {
						boolean successful = backupFiles[index].delete();
						if (successful) {
							logger.trace("successfully deleted file '{}'", backupFiles[index]);
						} else {
							logger.debug("couldn't delete file '{}'", backupFiles[index]);
						}
					}
				}
			}
		}
		
	}
	

}
