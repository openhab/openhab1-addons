/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
 * Class that manages all the per-device request queues using a single thread
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
	
	private RequestQueueManager() {
		m_queueThread = new Thread(new RequestQueueReader());
		m_queueThread.start();
	}
	
	public void addQueue(InsteonDevice dev, long time) {
		synchronized (m_requestQueues) {
			if (!m_requestQueueHash.containsKey(dev)) {
				RequestQueue q = new RequestQueue(dev, time);
				m_requestQueues.add(q);
				m_requestQueues.notify();
				m_requestQueueHash.put(dev, q);
				logger.trace("scheduling queue for dev {}", dev.getAddress());
			} else {
				logger.trace("queue for dev {} is already scheduled", dev.getAddress());
			}
		}
	}
	
	class RequestQueueReader implements Runnable {
		@Override
		public void run() {
			logger.info("starting request queue thread");
			synchronized (m_requestQueues) {
				while (true) {
					try {
						while (!m_requestQueues.isEmpty()) {
							RequestQueue q = m_requestQueues.poll();
							long now = System.currentTimeMillis();
							long expTime = q.getExpirationTime();
							InsteonDevice dev = q.getDevice();
							logger.trace("found busy request queue dev {} expTime: {} wait: {}",
									dev.getAddress(), expTime, expTime - now);
							if (expTime > now) {
								m_requestQueues.wait(expTime - now);
							}
							long nextExp = dev.processRequestQueue(now);
							if (nextExp > 0) {
								m_requestQueues.add(new RequestQueue(dev, nextExp));
								logger.trace("device queue for {} rescheduled", dev.getAddress());
							} else {
								// remove from hash since queue is no longer scheduled
								m_requestQueueHash.remove(dev);
								logger.trace("device queue for {} off schedule", dev.getAddress());
							}
						}
						logger.trace("waiting for request queues to fill");
						m_requestQueues.wait();
					} catch (InterruptedException e) {
						logger.error("request queue thread got interrupted, continuing", e);
					}
				}
			}
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
		@Override
		public int compareTo(RequestQueue a) {
			return (int)(m_expirationTime - a.m_expirationTime);
		}
	}
	public static RequestQueueManager instance() {
		if (s_instance == null) {
			s_instance = new RequestQueueManager();
		}
		return (s_instance);
	}
}
