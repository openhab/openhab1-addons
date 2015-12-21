/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;
import java.util.PriorityQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that manages all the per-device request queues using a single thread.
 *
 * - Each device has its own request queue, and the RequestQueueManager keeps a
 *   queue of queues.
 * - Each entry in m_requestQueues corresponds to a single device's request queue.
 *   A device should never be more than once in m_requestQueues.
 * - A hash map (m_requestQueueHash) is kept in sync with m_requestQueues for
 *   faster lookup in case a request queue is modified and needs to be
 *   rescheduled.
 *
 * @author Bernd Pfrommer
 * @since 1.6.0
*/
public class RequestQueueManager {
	private static RequestQueueManager s_instance = null;
	private static final Logger logger = LoggerFactory.getLogger(RequestQueueManager.class);
	private Thread	m_queueThread	= null;
	private PriorityQueue<RequestQueue> m_requestQueues = new PriorityQueue<RequestQueue>();
	private HashMap<InsteonDevice, RequestQueue> m_requestQueueHash = new HashMap<InsteonDevice, RequestQueue>();
	private boolean m_keepRunning = true;
	
	private RequestQueueManager() {
		m_queueThread = new Thread(new RequestQueueReader());
		m_queueThread.start();
	}
	/**
	 * Add device to global request queue.
	 * @param dev the device to add
	 * @param time the time when the queue should be processed
	 */
	public void addQueue(InsteonDevice dev, long time) {
		synchronized (m_requestQueues) {
			RequestQueue q = m_requestQueueHash.get(dev);
			if (q == null) {
				logger.trace("scheduling request for device {} in {} msec",
						dev.getAddress(), time - System.currentTimeMillis());
				q = new RequestQueue(dev, time);
			} else {
				logger.trace("queue for dev {} is already scheduled in {} msec", dev.getAddress(),
						q.getExpirationTime()- System.currentTimeMillis());
				if (!m_requestQueues.remove(q)) {
					logger.error("queue for {} should be there, report as bug!", dev);
				}
				m_requestQueueHash.remove(dev);
			}
			long expTime = q.getExpirationTime();
			if (expTime > time) q.setExpirationTime(time);
			// add the queue back in after (maybe) having modified
			// the expiration time
			m_requestQueues.add(q);
			m_requestQueueHash.put(dev, q);
			m_requestQueues.notify();
		}
	}

	/**
	 * Stops request queue thread
	 */
	private void stopThread() {
		logger.debug("stopping thread");
		if (m_queueThread != null) {
			synchronized (m_requestQueues) {
				m_keepRunning = false;
				m_requestQueues.notifyAll();
			}
			try {
				logger.debug("waiting for thread to join");
				m_queueThread.join();
				logger.debug("request queue thread exited!");
			} catch (InterruptedException e) {
				logger.error("got interrupted waiting for thread exit ", e);
			}
			m_queueThread = null;
		}
	}
	
	class RequestQueueReader implements Runnable {
		@Override
		public void run() {
			logger.debug("starting request queue thread");
			synchronized (m_requestQueues) {
				while (m_keepRunning) {
					try {
						while (m_keepRunning && !m_requestQueues.isEmpty()) {
							RequestQueue q = m_requestQueues.peek();
							long now = System.currentTimeMillis();
							long expTime = q.getExpirationTime();
							InsteonDevice dev = q.getDevice();
							if (expTime > now) {
								//
								// The head of the queue is not up for processing yet, wait().
								//
								logger.trace("request queue head: {} must wait for {} msec",
										dev.getAddress(), expTime - now);
								m_requestQueues.wait(expTime - now);
								//
								// note that the wait() can also return because of changes to
								// the queue, not just because the time expired!
								//
								continue;
							}
							//
							// The head of the queue has expired and can be processed!
							//
							q = m_requestQueues.poll(); // remove front element
							m_requestQueueHash.remove(dev); // and remove from hash map
							long nextExp = dev.processRequestQueue(now);
							if (nextExp > 0) {
								q = new RequestQueue(dev, nextExp);
								m_requestQueues.add(q);
								m_requestQueueHash.put(dev, q);
								logger.trace("device queue for {} rescheduled in {} msec",
											dev.getAddress(), nextExp - now);
							} else {
								// remove from hash since queue is no longer scheduled
								logger.debug("device queue for {} is empty!", dev.getAddress());
							}
						}
						logger.trace("waiting for request queues to fill");
						m_requestQueues.wait();
					} catch (InterruptedException e) {
						logger.error("request queue thread got interrupted, breaking..", e);
						break;
					}
				}
			}
			logger.debug("exiting request queue thread!");
		}
	}
		
	public static class RequestQueue implements Comparable<RequestQueue> {
		private InsteonDevice	m_device = null;
		private long			m_expirationTime = 0L;
		RequestQueue(InsteonDevice dev, long expirationTime) {
			m_device = dev;
			m_expirationTime = expirationTime;
		}
		public InsteonDevice getDevice() {
			return m_device;
		}
		public long getExpirationTime() {
			return m_expirationTime;
		}
		public void setExpirationTime(long t) {
			m_expirationTime = t;
		}
		@Override
		public int compareTo(RequestQueue a) {
			return (int)(m_expirationTime - a.m_expirationTime);
		}
	}
	public static synchronized RequestQueueManager s_instance() {
		if (s_instance == null) {
			s_instance = new RequestQueueManager();
		}
		return (s_instance);
	}
	public static void s_destroyInstance() {
		if (s_instance != null) {
			s_instance.stopThread();
			s_instance = null;
		}
	}
}
