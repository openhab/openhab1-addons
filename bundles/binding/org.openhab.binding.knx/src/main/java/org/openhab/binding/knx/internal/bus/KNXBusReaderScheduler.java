/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.bus;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.knx.internal.connection.KNXConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.datapoint.Datapoint;

/**
 * This is the central class that takes care of the refreshing (cyclical reading) of GAs from the KNX bus.
 * 
 * @author Volker Daube
 * @since 1.6.0
 *
 */
public class KNXBusReaderScheduler {
	private static final Logger sLogger = LoggerFactory.getLogger(KNXBusReaderScheduler.class);

	private final BlockingQueue<Datapoint> mReadQueue = new LinkedBlockingQueue<Datapoint> ();

	private static Map<Integer, List<Datapoint>> mScheduleMap = new ConcurrentHashMap<Integer,List<Datapoint>>();
	private ScheduledExecutorService mScheduledExecutorService;
	private boolean mIsRunning=false;
	/** the datapoint initializer, which runs in a separate thread */
	private KNXBindingDatapointReaderTask mDatapointReaderTask = null;

	/**
	 * Starts the scheduler
	 */
	public void start() {
		sLogger.trace("Starting auto refresh scheduler");

		sLogger.debug("Starting reader task.");
		mDatapointReaderTask=new KNXBindingDatapointReaderTask(mReadQueue);
		mDatapointReaderTask.start();

		sLogger.debug("Starting schedule executor.");
		mScheduledExecutorService = Executors.newScheduledThreadPool(KNXConnection.getNumberOfThreads());

		mIsRunning=true;
	}

	/**
	 * Stop the scheduler
	 */
	public void stop() {
		sLogger.trace("Stopping auto refresh scheduler");

		sLogger.trace("Clearing all items from the refresher queue");
		mReadQueue.clear();

		sLogger.debug("Terminating schedule executor.");
		mScheduledExecutorService.shutdown();
		try {
			if (mScheduledExecutorService.awaitTermination(KNXConnection.getScheduledExecutorServiceShutdownTimeout(), TimeUnit.SECONDS)) {
				sLogger.debug("Auto refresh scheduler successfully terminated");
			}
			else {
				sLogger.debug("Auto refresh scheduler couldn't be terminated and termination timed out.");
			}
		} catch (InterruptedException e) {
			sLogger.warn("Auto refresh scheduler: interrupted while waiting for termination.");
			Thread.currentThread().interrupt();
		}

		sLogger.trace("Stopping reader task");
		mDatapointReaderTask.interrupt();
		mIsRunning=false;
	}

	public boolean isRunning() {
		return mIsRunning;
	}
	/**
	 * Clears all datapoints from the scheduler
	 */
	public synchronized void clear() {
		sLogger.trace("Clearing all datapoints from auto refresh scheduler");
		mReadQueue.clear();

		//Restarting schedule executor
		if (mScheduledExecutorService != null) {
			sLogger.debug("Schedule executor restart.");
			mScheduledExecutorService.shutdown();
			try {
				if (mScheduledExecutorService.awaitTermination(KNXConnection.getScheduledExecutorServiceShutdownTimeout(), TimeUnit.SECONDS)) {
					sLogger.debug("Schedule executor restart: successfully terminated old instance");
				}
				else {
					sLogger.debug("Schedule executor restart failed: termination timed out.");
				}
			} catch (InterruptedException e) {
				sLogger.debug("Schedule executor restart failed: interrupted while waiting for termination.");
				Thread.currentThread().interrupt();
			}
			mScheduledExecutorService = Executors.newScheduledThreadPool(KNXConnection.getNumberOfThreads());
			sLogger.debug("Schedule executor restart: started.");
		}

		for (Iterator<Integer> iterator = mScheduleMap.keySet().iterator(); iterator.hasNext();) {
			int autoRefreshTimeInSecs = iterator.next();
			List<Datapoint> dpList = mScheduleMap.get(autoRefreshTimeInSecs);
			synchronized(dpList) {
				sLogger.debug("Clearing list {}", autoRefreshTimeInSecs);
				dpList.clear();
			}
			sLogger.debug("Removing list {} from scheduler", autoRefreshTimeInSecs);
			iterator.remove();
		}
	}

	/**
	 * Schedules immediate and one-time reading of a <code>Datapoint</code>.  
	 * @param datapoint the <code>Datapoint</code> to read
	 * @return false if the datapoint is null.
	 */
	public synchronized boolean readOnce(Datapoint datapoint) {
		if (datapoint==null) {
			sLogger.error("Argument datapoint cannot be null");
			return false;
		}

		if (mReadQueue.size()>KNXConnection.getMaxRefreshQueueEntries()) {
			sLogger.error("Maximium number of permissible reading queue entries reached ('{}'). Ignoring new entries.", KNXConnection.getMaxRefreshQueueEntries());
			return false;
		}

		sLogger.debug("Datapoint '{}':  one time reading scheduled.", datapoint.getName());
		return mReadQueue.add(datapoint);
	}

	/**
	 * Schedules a <code>Datapoint</code> to be cyclicly read. When parameter
	 * <code>autoRefreshTimeInSecs</code> is 0 then calling ths method is equal
	 * to calling <link>readOnce</link>. This function will return true if the <code>Datapoint</code>
	 * was added or if it was already scheduled with an identical <code>autoRefreshTimeInSecs</code>.
	 * 
	 * @param datapoint
	 *            the <code>Datapoint</code> to be read
	 * @param autoRefreshTimeInSecs
	 *            time in seconds specifying the reading cycle. 0 is equal to
	 *            calling <link>readOnce</link>
	 * @return true if the Datapoint was scheduled for reading, false in all
	 *         other cases
	 */
	public synchronized boolean scheduleRead(Datapoint datapoint, int autoRefreshTimeInSecs) {
		if (datapoint==null) {
			sLogger.error("Argument datapoint cannot be null");
			return false;
		}

		if (autoRefreshTimeInSecs<0) {
			sLogger.error("AutoRefreshTimeInSecs must be >= 0 for datapoint '{}'", datapoint.getName());
			return false;
		}

		if (autoRefreshTimeInSecs==0) {
			return readOnce(datapoint);
		}

		if (mReadQueue.size()>KNXConnection.getMaxRefreshQueueEntries()) {
			sLogger.error("Maximium number of permissible reading queue entries reached ('{}'). Ignoring new entries.", KNXConnection.getMaxRefreshQueueEntries());
			return false;
		}

		//Check if datapoint is already present in another list and if so, remove it
		int oldListNumber = getAutoRefreshTimeInSecs(datapoint);
		if (oldListNumber > 0) { 
			if (oldListNumber==autoRefreshTimeInSecs) {
				sLogger.debug("Datapoint '{}' was already in  auto refresh list {}", datapoint.getName(), autoRefreshTimeInSecs);
				return true;
			}
			List<Datapoint> oldList = mScheduleMap.get(oldListNumber);
			synchronized(oldList) {
				sLogger.debug("Datapoint '{}' already present in different list: {}, removing", datapoint.getName(), oldListNumber);
				/*
				 * The simple method to remove a <code>Datapoint</code> from a
				 * list would be <code>dpList.remove(datapoint)</code>
				 * Unfortunately, this cannot be used as the
				 * <code>Datapoint.equals()</code> method is comparing objects
				 * and sometimes new objects are being created for example when
				 * a configuration file is reread.
				 */

				for (Iterator<Datapoint> iterator = oldList.iterator(); iterator.hasNext();) {
					Datapoint dp = iterator.next();
					if (dp.toString().equals(datapoint.toString())) {
						iterator.remove();
					}
				}

			}
		}

		//Check if we have a list for autoRefreshTimeInSecs. If not create it.
		if (!mScheduleMap.containsKey(autoRefreshTimeInSecs)) {
			sLogger.debug("Creating auto refresh list: {}.", autoRefreshTimeInSecs);
			mScheduleMap.put(autoRefreshTimeInSecs, new LinkedList<Datapoint>());
			if (mIsRunning) {
				//Start scheduled task for the new time
				sLogger.debug("Starting auto refresh cycle {}", autoRefreshTimeInSecs);
				mScheduledExecutorService.scheduleAtFixedRate(new AutoRefreshTask(autoRefreshTimeInSecs), autoRefreshTimeInSecs, autoRefreshTimeInSecs, TimeUnit.SECONDS);
			}
		}

		//Add the datapoint to the list
		List<Datapoint> dpList = mScheduleMap.get(autoRefreshTimeInSecs);
		synchronized(dpList) {
			sLogger.debug("Adding datapoint '{}' to auto refresh list {}.", datapoint.getName(), autoRefreshTimeInSecs);
			return dpList.add(datapoint);
		}
	}


	/**
	 * Returns the auto refresh time in seconds for <code>datapoint</code>.
	 * If the datapoint is not present <code>0</code> is returned. 
	 * @param datapoint the data point to check
	 * @return the auto refresh time in seconds if datapoint was added previously. <code>0</code> otherwise.
	 */
	private synchronized int getAutoRefreshTimeInSecs(Datapoint datapoint) {
		for (int number : mScheduleMap.keySet()) {
			List<Datapoint> dpList = mScheduleMap.get(number);
			synchronized(dpList) {
				/*
				 * The simple method to see if a <code>Datapoint</code> is
				 * already in the list would be
				 * <code>dpList.contains(datapoint)</code> Unfortunately, this
				 * cannot be used as the Datapoint.equals() method is comparing
				 * objects and sometimes new objects are being created for
				 * example when a configuration file is reread.
				 */

				for (Datapoint dp : dpList) {
					if (dp.toString().equals(datapoint.toString())) {
						return number;
					}
				}
			}
		}
		return 0;
	}


	private final class AutoRefreshTask implements Runnable {
		private int autoRefreshTimeInSecs=0;

		public AutoRefreshTask(int autoRefreshTimeInSecs) {
			this.autoRefreshTimeInSecs=autoRefreshTimeInSecs;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if(KNXConnection.sShutdown) {
				return;
			}
			List<Datapoint> dpList = null;
			synchronized(mScheduleMap) {
				dpList = mScheduleMap.get(autoRefreshTimeInSecs);
				if (dpList==null) {
					sLogger.debug("Autorefresh: List {} was deleted. Terminating thread.", autoRefreshTimeInSecs);
				}
				else {
					sLogger.debug("Autorefresh: Adding {} item(s) with refresh time {} to reader queue.", dpList.size(), autoRefreshTimeInSecs);
					synchronized(dpList) {
						mReadQueue.addAll(dpList);
					}
				}
			}
		}
	}
}
