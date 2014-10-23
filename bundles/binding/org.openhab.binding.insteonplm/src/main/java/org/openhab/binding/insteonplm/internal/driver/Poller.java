/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

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
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public class Poller {
	private static final Logger logger = LoggerFactory.getLogger(Poller.class);
	private Thread m_pollThread = null;
	private TreeSet<PQEntry>	 m_pollQueue	= new TreeSet<PQEntry>();
	private final long MIN_MSEC_BETWEEN_POLLS = 2000L; 
	
	/**
	 * Constructor, starts a new thread!
	 */
	Poller() {
		m_pollThread = new Thread(new PollQueueReader());
		m_pollThread.start();
	}

	/**
	 * Register a device for polling.
	 * @param d device to register for polling
	 */
	public void startPolling(InsteonDevice d) {
		synchronized (m_pollQueue) {
			addToPollQueue(d, System.currentTimeMillis() + d.getPollInterval());
			m_pollQueue.notify();
		}
	}
	
	private void addToPollQueue(InsteonDevice d, long time) {
		SortedSet<PQEntry> ts = m_pollQueue.tailSet(new PQEntry(d, time - MIN_MSEC_BETWEEN_POLLS));
		if (ts.isEmpty()) {
			// all entries in the poll queue are well ahead of the new element, 
			// go ahead and simply add it to the end
			logger.trace("dev {} time {} added to end of poll queue", d, time);
			m_pollQueue.add(new PQEntry(d,time));
			return;
		}
		//
		// now find the earliest gap where we can squeeze in
		// a new poll while maintaining MIN_MSEC_BETWEEN_POLLS
		//
		Iterator<PQEntry> pqi = ts.iterator();
		PQEntry prev = pqi.next();
		while (pqi.hasNext()) {
			PQEntry pqe = pqi.next();
			long tcurr = pqe.getExpirationTime();
			long tprev = prev.getExpirationTime();
			if (tcurr - tprev >= 2 * MIN_MSEC_BETWEEN_POLLS) {
				// found gap
				logger.trace("dev {} time {} found slot between {} and {}", d, time, tprev, tcurr);
				break;
			}
			prev = pqe;
		}
		// now add to poll queue with suitable gap
		PQEntry ne = new PQEntry(d, prev.getExpirationTime() + MIN_MSEC_BETWEEN_POLLS);
		logger.trace("entry {} added at time {}", ne, time);
		m_pollQueue.add(ne);
	}

	private class PollQueueReader implements Runnable {
		@Override
		public void run() {
			logger.debug("starting poll thread.");
			while (true) {
				synchronized (m_pollQueue) {
					try {
						readPollQueue();
					} catch (InterruptedException e) {
						logger.warn("poll queue reader thread interrupted!");
					}
				}
			}
		}

		private void readPollQueue() throws InterruptedException {
			while (m_pollQueue.isEmpty()) {
				m_pollQueue.wait();
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
		
		private void processQueue(long now) {
			PQEntry pqe = m_pollQueue.pollFirst();
			pqe.getDevice().doPoll();
			addToPollQueue(pqe.getDevice(), now + pqe.getDevice().getPollInterval());
		}
	}
	
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
			return m_dev.getAddress().toString() + "/" + m_expirationTime;
		}
	}
	
}
