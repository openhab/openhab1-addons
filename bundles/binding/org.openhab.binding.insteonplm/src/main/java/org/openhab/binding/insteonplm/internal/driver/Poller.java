/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import java.sql.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages the polling of all devices.
 * Between successive polls of a any device there is a quiet time of
 * at least MIN_MSEC_BETWEEN_POLLS. This avoids bunching up of poll messages
 * and keeps the network bandwidth open for other messages.
 * 
 * - An entry in the poll queue corresponds to a single device, i.e. each device should
 *   have exactly one entry in the poll queue. That entry is created when startPolling()
 *   is called, and then re-enqueued whenever it expires.  
 * - When a device comes up for polling, its doPoll() method is called, which in turn
 *   puts an entry into that devices request queue. So the Poller class actually never
 *   sends out messages directly. That is done by the device itself via its request
 *   queue. The poller just reminds the device to poll.
 *    
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public class Poller {
	private static final Logger logger = LoggerFactory.getLogger(Poller.class);
	private static Poller s_poller; // for singleton
	
	private Thread m_pollThread = null;
	private TreeSet<PQEntry>	 m_pollQueue	= new TreeSet<PQEntry>();
	private final long MIN_MSEC_BETWEEN_POLLS = 2000L;
	private boolean m_keepRunning = true;
	
	/**
	 * Constructor
	 */
	private Poller() {
	}
	/**
	 * Get size of poll queue
	 * @return number of devices being polled
	 */
	public int getSizeOfQueue() {
		return (m_pollQueue.size());
	}
	/**
	 * Register a device for polling.
	 * @param d device to register for polling
	 * @param aNumDev approximate number of total devices
	 */
	public void startPolling(InsteonDevice d, int aNumDev) {
		logger.debug("start polling device {}", d);
		synchronized (m_pollQueue) {
			// try to spread out the scheduling when
			// starting up
			int n = m_pollQueue.size();
			long pollDelay = n * d.getPollInterval() / (aNumDev > 0 ? aNumDev : 1); 
			addToPollQueue(d, System.currentTimeMillis() + pollDelay);
			m_pollQueue.notify();
		}
	}
	/**
	 * Start polling a given device
	 * @param d reference to the device to be polled
	 */
	public void stopPolling(InsteonDevice d) {
		synchronized (m_pollQueue) {
			for (Iterator<PQEntry> i = m_pollQueue.iterator(); i.hasNext();) {
				if (i.next().getDevice().getAddress().equals(d.getAddress())) {
					i.remove();
					logger.debug("stopped polling device {}", d);
				}
			}
		}
	}
	/**
	 * Starts the poller thread
	 */
	public void start() {
		if (m_pollThread == null) {
			m_pollThread = new Thread(new PollQueueReader());
			m_pollThread.start();
		}
	}
	/**
	 * Stops the poller thread
	 */
	public void stop() {
		logger.debug("stopping poller!");
		synchronized (m_pollQueue) {
			m_pollQueue.clear();
			m_keepRunning = false;
			m_pollQueue.notify();
		}
		try {
			m_pollThread.join();
			m_keepRunning = true;
			m_pollThread = null;
		} catch (InterruptedException e) {
			logger.debug("got interrupted on exit: {}", e.getMessage());
		}
	}
	/**
	 * Adds a device to the poll queue. After this call, the device's doPoll() method
	 * will be called according to the polling frequency set.
	 * @param d the device to poll periodically
	 * @param time the target time for the next poll to happen. Note that this time is merely
	 * a suggestion, and may be adjusted, because there must be at least a minimum gap in polling.
	 */
	
	private void addToPollQueue(InsteonDevice d, long time) {
		long texp = findNextExpirationTime(d, time);
		PQEntry ne = new PQEntry(d, texp);
		logger.trace("added entry {} originally aimed at time {}", ne, String.format("%tc", new Date(time)));
		m_pollQueue.add(ne);
	}
	/**
	 * Finds the best expiration time for a poll queue, i.e. a time slot that is after the
	 * desired expiration time, but does not collide with any of the already scheduled
	 * polls.
	 *
	 * @param d		device to poll (for logging)
	 * @param aTime desired time after which the device should be polled
	 * @return the suggested time to poll
	 */
	
	private long findNextExpirationTime(InsteonDevice d, long aTime) {
		long expTime = aTime;
		// tailSet finds all those that expire after aTime - buffer
		SortedSet<PQEntry> ts = m_pollQueue.tailSet(new PQEntry(d, aTime - MIN_MSEC_BETWEEN_POLLS));
		if (ts.isEmpty()) {
			// all entries in the poll queue are ahead of the new element, 
			// go ahead and simply add it to the end
			expTime = aTime;
		} else {
			Iterator<PQEntry> pqi = ts.iterator();
			PQEntry prev = pqi.next();
			if (prev.getExpirationTime() > aTime + MIN_MSEC_BETWEEN_POLLS) {
				// there is a time slot free before the head of the tail set
				expTime = aTime;
			} else {
				// look for a gap where we can squeeze in
				// a new poll while maintaining MIN_MSEC_BETWEEN_POLLS
				while (pqi.hasNext()) {
					PQEntry pqe = pqi.next();
					long tcurr = pqe.getExpirationTime();
					long tprev = prev.getExpirationTime();
					if (tcurr - tprev >= 2 * MIN_MSEC_BETWEEN_POLLS) {
						// found gap
						logger.trace("dev {} time {} found slot between {} and {}", d, aTime, tprev, tcurr);
						break;
					}
					prev = pqe;
				}
				expTime = prev.getExpirationTime() + MIN_MSEC_BETWEEN_POLLS;
			}
		}
		return expTime;
	}

	private class PollQueueReader implements Runnable {
		@Override
		public void run() {
			logger.debug("starting poll thread.");
			synchronized (m_pollQueue) {
				while (m_keepRunning) {
					try {
						readPollQueue();
					} catch (InterruptedException e) {
						logger.warn("poll queue reader thread interrupted!");
						break;
					}
				}
			}
			logger.debug("poll thread exiting");
		}

		/**
		 * Waits for first element of poll queue to become current,
		 * then process it.
		 * @throws InterruptedException
		 */
		private void readPollQueue() throws InterruptedException {
			while (m_pollQueue.isEmpty() && m_keepRunning) {
				m_pollQueue.wait();
			}
			if (!m_keepRunning) {
				return;
			}
			// something is in the queue
			long now = System.currentTimeMillis();
			PQEntry pqe = m_pollQueue.first();
			long tfirst = pqe.getExpirationTime();
			long dt = tfirst - now;
			if (dt > 0) {	// must wait for this item to expire
				logger.trace("waiting for {} msec until {} comes due", dt, pqe);
				m_pollQueue.wait(dt);
			} else { // queue entry has expired, process it!
				logger.trace("entry {} expired at time {}", pqe, now);
				processQueue(now);
			}
		}
		/**
		 * Takes first element off the poll queue, polls the corresponding device,
		 * and puts the device back into the poll queue to be polled again later.
		 * @param now the current time 
		 */
		private void processQueue(long now) {
			PQEntry pqe = m_pollQueue.pollFirst();
			pqe.getDevice().doPoll(0);
			addToPollQueue(pqe.getDevice(), now + pqe.getDevice().getPollInterval());
		}
	}
	/**
	 * A poll queue entry corresponds to a single device that needs
	 * to be polled.
	 * @author Bernd Pfrommer
	 *
	 */
	private static class PQEntry implements Comparable<PQEntry> {
		private InsteonDevice	m_dev = null;
		private long			m_expirationTime = 0L;
		PQEntry(InsteonDevice dev, long time) {
			m_dev			 = dev;
			m_expirationTime = time;
		}
		long getExpirationTime() {
			return m_expirationTime;
		}
		InsteonDevice getDevice() {
			return m_dev;
		}
		@Override
		public int compareTo(PQEntry b) {
			return (int)(m_expirationTime - b.m_expirationTime);
		}
		@Override
		public String toString() {
			return m_dev.getAddress().toString() + "/" + String.format("%tc", new Date(m_expirationTime));
		}
	}
	/**
	 * Singleton pattern instance() method
	 * @return the poller instance
	 */
	public static synchronized Poller s_instance() {
		if (s_poller == null) {
			s_poller = new Poller();
		}
		s_poller.start();
		return (s_poller);
	}
	
}
