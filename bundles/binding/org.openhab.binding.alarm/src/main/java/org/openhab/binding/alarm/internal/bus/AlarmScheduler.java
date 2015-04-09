/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.internal.bus;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.alarm.config.AlarmCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the central class that takes care of the cyclical checking if items have received updates  (stale checking) and
 * scheduled delayed alarms. Delayed Alarms are alarms with a delay time attached. These are alarmed only if the alarm condition
 * isn't changing during the delay period.
 * 
 * @author Volker Daube
 * @since 1.7.0
 *
 */
public class AlarmScheduler {
	private static final Logger sLogger = LoggerFactory.getLogger(AlarmScheduler.class);

	private static AlarmScheduler instance;
	protected Collection<AlarmListener> mAlarmListeners = new CopyOnWriteArraySet<AlarmListener>();


	private static final int sMaxTerminationTimeout = 10;
	private List<StaleAlarmData> mStaleDevicesCheckList = new LinkedList<StaleAlarmData> ();
	private List<DelayedAlarmData> mDelayedAlarmList = new LinkedList<DelayedAlarmData> ();

	private ScheduledExecutorService mScheduledExecutorService;
	private boolean mIsRunning=false;
	private AlarmScheduler() {}

	public static AlarmScheduler getInstance() {
		if (instance==null) {
			instance = new AlarmScheduler();
		}
		return instance;
	}

	/**
	 * Registers an AlarmListener, which will received alarm events
	 * @param listener an AlarmListener
	 */
	public void registerAlarmListener(AlarmListener listener) {
		this.mAlarmListeners.add(listener);
	}
	/**
	 * Removes an AlarmListener, 
	 * @param listener an AlarmListener
	 */
	public void removeAlarmListener(AlarmListener listener) {
		this.mAlarmListeners.remove(listener);
	}

	private void fireStaleAlarm(String itemName, AlarmCondition alarmCondition) {
		for (AlarmListener staleAlarmlistener : mAlarmListeners) {
			staleAlarmlistener.staleAlarm(itemName, alarmCondition);
		}
	}
	private void fireCancelStaleAlarm(String itemName, String messageItemName) {
		for (AlarmListener staleAlarmlistener : mAlarmListeners) {
			staleAlarmlistener.staleAlarmCanceled(itemName, messageItemName);
		}
	}
	private void fireDelayedAlarm(String itemName, AlarmCondition alarmCondition) {
		for (AlarmListener alarmlistener : mAlarmListeners) {
			alarmlistener.delayedAlarm(itemName, alarmCondition);
		}
	}
	/**
	 * Starts the scheduler. Scheduler will check cyclically every checkCycleInSecs seconds if
	 * a stale device is present
	 * @param checkCycleInSecs the check cycle in seconds
	 */
	public void startCyclicalStaleChecking(int checkCycleInSecs) {
		if (mIsRunning) {
			sLogger.debug("Stale devices detection scheduler already running");
		}
		else if (checkCycleInSecs<1){
			sLogger.debug("Illegal cycle time {}. Scheduler not starting.", checkCycleInSecs);
		}
		else {
			mIsRunning=true;
			sLogger.trace("Starting stale/delayed alarm scheduler with cycle time {}", checkCycleInSecs);

			sLogger.debug("Starting schedule executor.");
			mScheduledExecutorService = Executors.newScheduledThreadPool(4);

			mScheduledExecutorService.scheduleAtFixedRate(new StaleCheckTask(), checkCycleInSecs, checkCycleInSecs, TimeUnit.SECONDS);
		}
	}

	/**
	 * Stop the scheduler
	 */
	public void shutdown() {
		sLogger.trace("Stopping stale/delayed alarm scheduler");

		if ((mIsRunning) && (mScheduledExecutorService!=null)) {
			sLogger.debug("Terminating schedule executor.");
			mScheduledExecutorService.shutdown();
			try {
				if (mScheduledExecutorService.awaitTermination(sMaxTerminationTimeout, TimeUnit.SECONDS)) {
					sLogger.debug("Alarm scheduler successfully terminated");
				}
				else {
					sLogger.debug("Alarm scheduler couldn't be terminated and termination timed out.");
				}
			} catch (InterruptedException e) {
				sLogger.warn("Alarm scheduler: interrupted while waiting for termination.");
				Thread.currentThread().interrupt();
			}
		}
		mIsRunning=false;
	}

	public boolean isRunning() {
		return mIsRunning;
	}
	/**
	 * Clears all Items from the scheduler
	 */
	public void clear() {
		sLogger.trace("Clearing all datapoints from alarm scheduler");

		//Restarting schedule executor
		if (mScheduledExecutorService != null) {
			sLogger.debug("Schedule executor restart.");
			mScheduledExecutorService.shutdown();
			try {
				if (mScheduledExecutorService.awaitTermination(sMaxTerminationTimeout, TimeUnit.SECONDS)) {
					sLogger.debug("Schedule executor restart: successfully terminated old instance");
				}
				else {
					sLogger.debug("Schedule executor restart failed: termination timed out.");
				}
			} catch (InterruptedException e) {
				sLogger.debug("Schedule executor restart failed: interrupted while waiting for termination.");
				Thread.currentThread().interrupt();
			}
			mScheduledExecutorService = Executors.newScheduledThreadPool(1);
			sLogger.debug("Schedule executor restart: started.");
		}

		synchronized(mStaleDevicesCheckList) {
			mStaleDevicesCheckList.clear();
		}
		synchronized(mStaleDevicesCheckList) {
			mStaleDevicesCheckList.clear();
		}
	}

	/**
	 * Schedules stale checking of a <code>Item</code>.  
	 * @param itemName the item's name> to read
	 * @return false if the item is null.
	 */
	public boolean addStaleCheck(String itemName, AlarmCondition alarmCondition) {
		boolean result=false;
		if (itemName==null) {
			sLogger.error("Argument item cannot be null");
			return false;
		}


		sLogger.debug("Item '{}':  added to stale device checking.", itemName);
		synchronized(mStaleDevicesCheckList) {
			result=mStaleDevicesCheckList.add(new StaleAlarmData(itemName, alarmCondition));
		}
		return result;
	}

	/**
	 * Removes an item from stale alarming
	 * @param itemName the name of the item to be removed
	 * @return true if item was removed, false otherwise
	 */
	public boolean removeStaleCheck(String itemName) {

		if (itemName==null) {
			sLogger.error("Argument itemName cannot be null");
			return false;
		}


		synchronized(mStaleDevicesCheckList) {
			for (Iterator<StaleAlarmData> iterator = mStaleDevicesCheckList.iterator(); iterator.hasNext();) {
				StaleAlarmData eItem = iterator.next();
				if (eItem.getItemName().equals(itemName)) {
					iterator.remove();
					sLogger.debug("Item '{}':  removed from stale device checking.", itemName);
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasStaleCheck(String itemName) {

		if (itemName==null) {
			sLogger.error("Argument itemName cannot be null");
			return false;
		}


		synchronized(mStaleDevicesCheckList) {
			for (Iterator<StaleAlarmData> iterator = mStaleDevicesCheckList.iterator(); iterator.hasNext();) {
				StaleAlarmData eItem = iterator.next();
				if (eItem.getItemName().equals(itemName)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Updates the last updated time of the item that was registered for stale alarming.
	 * If the item was already alarmed, then the alarm is canceled;
	 * @param itemName the name of the item registered for stale alarming
	 */
	public void touch(String itemName) {
		if (itemName==null) {
			sLogger.error("Argument itemName cannot be null");
			return;
		}
		synchronized(mStaleDevicesCheckList) {
			for (StaleAlarmData eItem : mStaleDevicesCheckList) {
				if (eItem.getItemName().equals(itemName)) {
					eItem.setLastUpdateInMs(new Date().getTime());
					sLogger.debug("Item '{}':  stale device checking: touched {}.", itemName, eItem.getLastUpdateInMs());
					if (eItem.isStaleAlarmed()) {
						eItem.setStaleAlarmed(false);
						sLogger.debug("Canceling stale alarm.", itemName, eItem.getLastUpdateInMs());
						fireCancelStaleAlarm(eItem.getItemName(), eItem.getAlarmCondition().getMessageItemName());					}
					return;
				}
			}
		}		
	}

	/**
	 * Updates the last updated time of the item that was registered for stale alarming.
	 * If the item was already alarmed, then the alarm is canceled;
	 * @param itemName the name of the item registered for stale alarming
	 */
	public boolean isStaleAlarmed(String itemName) {
		if (itemName==null) {
			throw new IllegalArgumentException("Argument itemName cannot be null");
		}

		synchronized(mStaleDevicesCheckList) {
			for (StaleAlarmData eItem : mStaleDevicesCheckList) {
				if (eItem.getItemName().equals(itemName)) {
					return eItem.isStaleAlarmed();
				}
			}
		}		
		sLogger.warn("Couldn't find item {} in stale check list!", itemName);
		return false;
	}

	/**
	 * Handles an alarms for items with a delayed alarm. If a delay is configured for an alarm
	 * then the delay time needs to be waited before triggering the alarm. The alarm is not triggered
	 * if the alarm condition is removed during the delay time.
	 * @param itemName name of the item
	 * @param delayInSeconds the delay time in seconds before the alarm is triggered
	 * @return true if the item was accepted, false otherwise
	 */
	public boolean addDelayedAlarm(String itemName, AlarmCondition alarmCondition) {
		boolean result=false;
		if (itemName==null) {
			sLogger.error("Argument item cannot be null");
			return false;
		}


		sLogger.debug("Item '{}':  delayed alarm scheduled.", itemName);
		ScheduledFuture<?> scheduledF = mScheduledExecutorService.schedule(
				new AlarmDelayTask(itemName, alarmCondition),
				alarmCondition.getAlarmTimeInSeconds(),
				TimeUnit.SECONDS);
		synchronized(mDelayedAlarmList) {
			result=mDelayedAlarmList.add(new DelayedAlarmData(itemName, alarmCondition, scheduledF));
		}

		return result;
	}

	/**
	 * Cancels an delayed alarm if existing.
	 * @param itemName the name of the alarmed item to cancel
	 * @return true if canceled, false otherwise
	 */
	public boolean cancelDelayedAlarm(String itemName) {
		if (removeDelayedAlarm(itemName)) {
			sLogger.debug("Item '{}':  delayed alarm canceled.", itemName);
			return true;
		}
		else {
			sLogger.warn("Item '{}':  Couldn't cancel delayed alarm!.", itemName);
		}
		return false;
	}

	/**
	 * Tests if an item was scheduled for a delayed alarm
	 * @param itemName the name of the item
	 * @return true if scheduled, false otherwise
	 */
	public boolean isDelayAlarmed(String itemName) {
		if (itemName==null) {
			sLogger.error("Argument itemName cannot be null");
			return false;
		}
		synchronized(mDelayedAlarmList) {
			for (Iterator<DelayedAlarmData> iterator = mDelayedAlarmList.iterator(); iterator.hasNext();) {
				DelayedAlarmData eItem = iterator.next();
				if (eItem.getItemName().equals(itemName)) {
					return true;
				}
			}
		}
		return false;
	}

	private final class StaleCheckTask implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Date now = new Date();
			synchronized(mStaleDevicesCheckList) {
				for (StaleAlarmData eItem : mStaleDevicesCheckList) {
					//If item is already alarmed: do not alarm again
					if (!eItem.isStaleAlarmed()) {
						long lastUpdate = eItem.getLastUpdateInMs();
						if (lastUpdate==0l) {
							sLogger.debug("Triggering stale alarm. Item {} was never updated: '{}'.", eItem.getItemName(), eItem.getAlarmCondition());
							eItem.setStaleAlarmed(true);
							fireStaleAlarm(eItem.getItemName(), eItem.getAlarmCondition());
						}
						else { 
							long timeDelta=now.getTime()-lastUpdate;
							long staleTime=eItem.getStaleTime()*1000l;
							if (timeDelta >staleTime) {
								sLogger.debug("Triggering stale alarm. Item {} was last updated {}, delta {}, staleTime {}.", eItem.getItemName(), lastUpdate, timeDelta, staleTime);
								eItem.setStaleAlarmed(true);
								fireStaleAlarm(eItem.getItemName(), eItem.getAlarmCondition());
							}
							else {
								sLogger.debug("Stale alarm checking: no alarm! Item {} was last updated {}, delta {}, staleTime {}.", eItem.getItemName(), lastUpdate, timeDelta, staleTime);
							}
						}
					}
				}
			}
		}
	}

	private boolean removeDelayedAlarm(String itemName) {

		if (itemName==null) {
			throw new IllegalArgumentException("Parameter itemName cannot be null!");
		}

		synchronized(mDelayedAlarmList) {
			for (Iterator<DelayedAlarmData> iterator = mDelayedAlarmList.iterator(); iterator.hasNext();) {
				DelayedAlarmData eItem = iterator.next();
				if (eItem.getItemName().equals(itemName)) {
					ScheduledFuture<?> sf=eItem.getScheduledFuture();
					if ((sf!=null)&&!sf.isDone()&&!sf.isCancelled()) {
						sf.cancel(false);
					}
					iterator.remove();
					return true;
				}
			}
		}
		return false;
	}

	private final class AlarmDelayTask implements Runnable {

		private String mItemName;
		private AlarmCondition mAlarmCondition;
		public AlarmDelayTask(String itemName, AlarmCondition alarmCondition) {
			mItemName=itemName;
			mAlarmCondition=alarmCondition;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			removeDelayedAlarm(mItemName);
			fireDelayedAlarm(mItemName, mAlarmCondition);
		}
	}


}
