/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Base class for services that frequently run some action in a separate thread in the 
 * background.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 */
public abstract class AbstractActiveService {

	private static final Logger logger = LoggerFactory.getLogger(AbstractActiveService.class);
	
	/** <code>true</code> if this binding is configured properly which means that all necessary data is available */
	private boolean properlyConfigured = false;

	/**
	 * indicates that the background thread will shutdown after the current
	 * execution cycle.
	 */
	protected boolean shutdown = false;
	
	/**
	 * holds the instance of the refresh thread or is <code>null</code> if 
	 * there is no thread active at the moment
	 */
	private Thread refreshThread;
	
	
	public AbstractActiveService() {
		super();
	}
	

	public void activate() {
		start();
	}

	public void deactivate() {
		shutdown();
	}
	
	/**
	 * Takes care about starting the refresh thread. It creates a new
	 * RefreshThread if no instance exists.
	 */
	protected void start() {
		if (!isProperlyConfigured()) {
			logger.trace("{} won't be started because it isn't yet properly configured.", getName());
			return;
		}
				
		shutdown = false;
		if (!isRunning()) {
			this.refreshThread = new RefreshThread(getName(), getRefreshInterval());
			this.refreshThread.start();
		} else {
			logger.trace("{} is already started > calling start() changed nothing.", getName());
		}
	}

	/**
	 * Gracefully shuts down the refresh background thread. It will shuts down
	 * after the current execution cycle.
	 */
	public void shutdown() {
		this.shutdown = true;
	}
	
	/**
	 * Interrupts the refresh thread immediately.
	 */
	public void interrupt() {
		if (isRunning()) {
			this.refreshThread.interrupt();
			logger.trace("{} has been interrupted.", getName());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isRunning() {
		if (this.refreshThread != null) {
			return this.refreshThread.isAlive();
		}
		return false;
	}
	
	/**
	 * @return <code>true</code> if this binding is configured properly which means
	 * that all necessary data is available
	 */
	final public boolean isProperlyConfigured() {
		return properlyConfigured;
	}
	
	
	/**
	 * Used to define whether this binding is fully configured so that it can be
	 * activated and used.
	 * Note that the implementation will automatically start the active service if
	 * <code>true</code> is passed as a parameter.
	 * 
	 * @param properlyConfigured
	 */
	public void setProperlyConfigured(boolean properlyConfigured) {
		this.properlyConfigured = properlyConfigured;
		if (properlyConfigured && !isRunning()) {
			start();
		} else if (!properlyConfigured && isRunning()) {
			shutdown();
		}
	}
	
	/**
	 * The working method which is called by the refresh thread frequently. 
	 * Developers should put their binding code here.
	 */
	protected abstract void execute();

	/**
	 * Returns the refresh interval to be used by the RefreshThread between to
	 * calls of the execute method.
	 * 
	 * @return the refresh interval
	 */
	protected abstract long getRefreshInterval();

	/**
	 * Returns the name of the Refresh thread.
	 * 
	 * @return the name of the refresh thread.
	 */
	protected abstract String getName();
	
	/**
	 * Worker thread which calls the execute method frequently.
	 *  
	 * @author Thomas.Eichstaedt-Engelen
	 */
	class RefreshThread extends Thread {
		
		private long refreshInterval;
		
		public RefreshThread(String name, long refreshInterval) {
			super(name);
			this.setDaemon(true);
			this.refreshInterval = refreshInterval;
			
			// reset 'interrupted' after stopping this refresh thread ...
			shutdown = false;
		}
		
		@Override
		public void run() {
			logger.info(getName() + " has been started");
			
			while (!shutdown) {
				try {
					execute();
				} catch(RuntimeException e) {
					logger.error("Error while executing background thread " + getName(), e);
				}
				pause(refreshInterval);
			}
			
			refreshThread = null;
			logger.info(getName() + " has been shut down");
		}
		
		/**
		 * Pause polling for the given <code>refreshInterval</code>. Possible
		 * {@link InterruptedException} is logged with no further action.
		 *  
		 * @param refreshInterval 
		 */
		protected void pause(long refreshInterval) {
			
			try {
				Thread.sleep(refreshInterval);
			}
			catch (InterruptedException e) {
				logger.debug("pausing thread " + super.getName() +" interrupted");
			}
		}

	}

}