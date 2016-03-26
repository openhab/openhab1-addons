/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.onewire.internal.listener.InterfaceOneWireDevicePropertyWantsUpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the central class that takes care of the refreshing (cyclical reading) from the 1-Wire bus.
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public class OneWireUpdateScheduler {
    private static final Logger logger = LoggerFactory.getLogger(OneWireUpdateScheduler.class);

    /**
     * Number of threads executing in parallel for auto refresh feature. Default value is <code>5</code>
     */
    private static int cvNumberOfThreads = 5;

    /**
     * Time in seconds to wait for an orderly shutdown of the auto refresher feature. Default value is <code>5</code>
     */
    private static int cvScheduledExecutorServiceShutdownTimeout = 5;

    /**
     * list with items which wants to be updated
     */
    private final BlockingQueue<String> ivUpdateQueue = new LinkedBlockingQueue<String>();

    /**
     * map with scheduled items
     */
    private static Map<Integer, List<String>> cvScheduleMap = new ConcurrentHashMap<Integer, List<String>>();

    /**
     * Executor, which handles AutoRefreshTaks (Threads)
     */
    private ScheduledExecutorService ivScheduledExecutorService;

    /**
     * is this scheduler running
     */
    private boolean ivIsRunning = false;

    /**
     * this task runs in an separate Thread. It informs the Listeners, that an item wants to get updated
     */
    private OneWireUpdateTask ivOneWireUpdateTask = null;

    /**
     * @param pvWantsUpdateListener
     */
    public OneWireUpdateScheduler(InterfaceOneWireDevicePropertyWantsUpdateListener pvWantsUpdateListener) {
        super();
        ivOneWireUpdateTask = new OneWireUpdateTask(ivUpdateQueue, pvWantsUpdateListener);
    }

    /**
     * Starts the scheduler
     */
    public void start() {
        logger.debug("Starting auto refresh scheduler");

        logger.debug("Starting reader task.");

        ivOneWireUpdateTask.start();

        logger.debug("Starting schedule executor.");
        ivScheduledExecutorService = Executors.newScheduledThreadPool(cvNumberOfThreads);

        ivIsRunning = true;
    }

    /**
     * Stop the scheduler
     */
    public void stop() {
        logger.debug("Stopping auto refresh scheduler");

        logger.debug("Clearing all items from the refresher queue");
        ivUpdateQueue.clear();

        logger.debug("Terminating schedule executor.");
        ivScheduledExecutorService.shutdown();
        try {
            if (ivScheduledExecutorService.awaitTermination(cvScheduledExecutorServiceShutdownTimeout,
                    TimeUnit.SECONDS)) {
                logger.debug("Auto refresh scheduler successfully terminated");
            } else {
                logger.debug("Auto refresh scheduler couldn't be terminated and termination timed out.");
            }
        } catch (InterruptedException e) {
            logger.debug("Auto refresh scheduler: interrupted while waiting for termination.");
        }

        logger.debug("Stopping reader task");
        ivOneWireUpdateTask.interrupt();
        ivIsRunning = false;
    }

    /**
     * Clears all items from the scheduler
     */
    public synchronized void clear() {
        logger.debug("Clearing all items from auto refresh scheduler");
        ivUpdateQueue.clear();

        // Restarting schedule executor
        if (ivScheduledExecutorService != null) {
            logger.debug("Schedule executor restart.");
            ivScheduledExecutorService.shutdown();
            try {
                if (ivScheduledExecutorService.awaitTermination(cvScheduledExecutorServiceShutdownTimeout,
                        TimeUnit.SECONDS)) {
                    logger.debug("Schedule executor restart: successfully terminated old instance");
                } else {
                    logger.debug("Schedule executor restart failed: termination timed out.");
                }
            } catch (InterruptedException e) {
                logger.debug("Schedule executor restart failed: interrupted while waiting for termination.");
            }
            ivScheduledExecutorService = Executors.newScheduledThreadPool(cvNumberOfThreads);
            logger.debug("Schedule executor restart: started.");
        }

        for (Iterator<Integer> lvIterator = cvScheduleMap.keySet().iterator(); lvIterator.hasNext();) {
            int autoRefreshTimeInSecs = lvIterator.next();

            List<String> lvItemListe = cvScheduleMap.get(autoRefreshTimeInSecs);
            synchronized (lvItemListe) {
                logger.debug("Clearing list {}", autoRefreshTimeInSecs);
                lvItemListe.clear();
            }
            logger.debug("Removing list {} from scheduler", autoRefreshTimeInSecs);
            lvIterator.remove();
        }
    }

    /**
     * Schedules immediate and one-time reading of a <code>item</code>.
     * 
     * @param pvItemName
     *            the <code>item</code> to read
     * @return false if the item is null.
     */
    public synchronized boolean updateOnce(String pvItemName) {
        if (pvItemName == null) {
            logger.error("Argument itemName cannot be null");
            return false;
        }

        logger.debug("Item '{}':  one time reading scheduled.", pvItemName);
        return ivUpdateQueue.add(pvItemName);
    }

    /**
     * Schedules a <code>item</code> to be cyclicly read. When parameter <code>autoRefreshTimeInSecs</code> is 0 then
     * calling ths method is equal to calling <link>readOnce</link>.
     * 
     * @param bindingConfig
     *            the <code>OneWireBindingConfig</code> to be read
     * @param ovautoRefreshTimeInSecs
     *            time in seconds specifying the reading cycle. 0 is equal to calling <link>readOnce</link>
     * @return true if the OneWireBindingProvider was scheduled for reading, false in all other cases
     */
    public synchronized boolean scheduleUpdate(String pvItemName, int pvAutoRefreshTimeInSecs) {
        if (pvItemName == null) {
            logger.error("Argument itemName cannot be null");
            return false;
        }

        if (pvAutoRefreshTimeInSecs < 0) {
            logger.debug("AutoRefreshTimeInSecs must be >= 0 for itemName '{}'", pvItemName);
            return false;
        }

        // Check if item is already present in another list and if so, remove it
        int lvOldListNumber = getAutoRefreshTimeInSecs(pvItemName);
        if (lvOldListNumber > 0) {
            if (lvOldListNumber == pvAutoRefreshTimeInSecs) {
                logger.debug("item '{}' was already in auto refresh list {}", pvItemName, pvAutoRefreshTimeInSecs);
                return true;
            }
            List<String> lvOldList = cvScheduleMap.get(lvOldListNumber);
            synchronized (lvOldList) {
                logger.debug("item '{}' already present in different list: {}, removing", pvItemName, lvOldListNumber);
                /*
                 * The simple method to remove a <code>item</code> from a list would be
                 * <code>itemListe.remove(item)</code> Unfortunately, this cannot be used as the
                 * <code>item.equals()</code> method is comparing objects and sometimes new objects are being created
                 * for example when a configuration file is reread.
                 */

                for (Iterator<String> lvIterator = lvOldList.iterator(); lvIterator.hasNext();) {
                    String lvItemNameOldList = lvIterator.next();
                    if (lvItemNameOldList.toString().equals(pvItemName)) {
                        lvIterator.remove();
                    }
                }

            }
        }

        // Check if we have a list for autoRefreshTimeInSecs. If not create it.
        if (!cvScheduleMap.containsKey(pvAutoRefreshTimeInSecs)) {
            logger.debug("Creating auto refresh list: {}.", pvAutoRefreshTimeInSecs);
            cvScheduleMap.put(pvAutoRefreshTimeInSecs, new LinkedList<String>());
            if (ivIsRunning) {
                // Start scheduled task for the new time
                logger.debug("Starting auto refresh cycle {}", pvAutoRefreshTimeInSecs);
                ivScheduledExecutorService.scheduleAtFixedRate(new AutoRefreshTask(pvAutoRefreshTimeInSecs),
                        pvAutoRefreshTimeInSecs, pvAutoRefreshTimeInSecs, TimeUnit.SECONDS);
            }
        }

        // Add the item to the list
        List<String> lvItemListe = cvScheduleMap.get(pvAutoRefreshTimeInSecs);
        synchronized (lvItemListe) {
            logger.debug("Adding item '{}' to auto refresh list {}.", pvItemName, pvAutoRefreshTimeInSecs);
            return lvItemListe.add(pvItemName);
        }
    }

    /**
     * Returns the auto refresh time in seconds for <code>item</code>. If the item is not present <code>0</code> is
     * returned.
     * 
     * @param item
     *            the item to check
     * @return the auto refresh time in seconds if item was added previously. <code>0</code> otherwise.
     */
    private synchronized int getAutoRefreshTimeInSecs(String pvItemName) {
        for (int lvNumber : cvScheduleMap.keySet()) {
            List<String> lvItemListe = cvScheduleMap.get(lvNumber);
            synchronized (lvItemListe) {
                /*
                 * The simple method to see if a <code>item</code> is already in the list would be
                 * <code>itemListe.contains(item)</code> Unfortunately, this cannot be used as the item.equals() method
                 * is comparing objects and sometimes new objects are being created for example when a configuration
                 * file is reread.
                 */

                for (String lvTempItemName : lvItemListe) {
                    if (lvTempItemName.toString().equals(pvItemName)) {
                        return lvNumber;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Removes scheduled item
     * 
     * @param pvItemName
     */
    public synchronized void removeItem(String pvItemName) {
        for (int lvNumber : cvScheduleMap.keySet()) {
            List<String> lvItemListe = cvScheduleMap.get(lvNumber);
            if (lvItemListe.contains(pvItemName)) {
                logger.debug("remove item=" + pvItemName + " from scheduler!");
                lvItemListe.remove(pvItemName);
            }
        }
    }

    /**
     * This Taks fills the ivUpdateQueue with items, which must be updated.
     * The ivUpdateQueue is used by OneWireUpdateTask to inform the Listeners about theses items
     * 
     * @author Dennis Riegelbauer
     * @since 1.7.0
     *
     */
    private final class AutoRefreshTask implements Runnable {
        private int ivAutoRefreshTimeInSecs = 0;

        public AutoRefreshTask(int pvAutoRefreshTimeInSecs) {
            this.ivAutoRefreshTimeInSecs = pvAutoRefreshTimeInSecs;
        }

        @Override
        public void run() {
            List<String> lvItemNameList = null;
            synchronized (cvScheduleMap) {
                lvItemNameList = cvScheduleMap.get(ivAutoRefreshTimeInSecs);
                if (lvItemNameList == null) {
                    logger.debug("Autorefresh: List {} was deleted. Terminating thread.", ivAutoRefreshTimeInSecs);
                } else {
                    logger.debug("Autorefresh: Adding {} item(s) with refresh time {} to reader queue.",
                            lvItemNameList.size(), ivAutoRefreshTimeInSecs);
                    logger.debug("Update Task isAlive: " + ivOneWireUpdateTask.isAlive());
                    if (!ivOneWireUpdateTask.isAlive()) {
                        logger.debug("create and start a new Update Task again...");

                        OneWireUpdateTask lvNewOneWireUpdateTask = new OneWireUpdateTask(ivUpdateQueue,
                                ivOneWireUpdateTask.getIvWantsUpdateListener());
                        ivOneWireUpdateTask = lvNewOneWireUpdateTask;

                        ivOneWireUpdateTask.start();
                    }
                    synchronized (lvItemNameList) {
                        // increase performance one slower systems on startup
                        // only add items to queue which aren't already in queue
                        for (String lvItemName : lvItemNameList) {
                            if (!ivUpdateQueue.contains(lvItemName)) {
                                logger.debug("add item " + lvItemName + " to updateQueue");
                                ivUpdateQueue.add(lvItemName);
                            } else {
                                logger.debug("didn't add item " + lvItemName + " to updateQueue, it is alread there");
                            }
                        }
                    }
                }
            }
        }
    }
}