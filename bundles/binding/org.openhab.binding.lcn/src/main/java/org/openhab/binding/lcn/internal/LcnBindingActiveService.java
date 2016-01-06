/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.internal;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Active service used by the LCN openHAB binding.
 * <p>
 * It is based on "AbstractActiveService" and {@link AbstractActiveBinding.BindingActiveService}
 * without the refresh-interval polling.
 * 
 * @author Tobias Jüttner
 */
class LcnBindingActiveService implements Runnable {
	
	/** Logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger(LcnBindingActiveService.class);
	
	/** The (refresh-)thread name. */
	private static final String THREAD_NAME = "LCN Refresh Service";
	
	/** Must be implemented by the owner. */
	interface Callback {
		
		/**
		 * Main worker method of the binding to call periodically.
		 */
		void execute();
		
		/**
		 * Checks whether any of the {@link BindingProvider}s provides a binding.
		 * 
		 * @return true if any of the {@link BindingProvider}s provides a binding
		 */
		boolean bindingsExists();
		
	}
	
	/** The callback to the owner. */
	private final Callback callback;
	
	/** true if the binding is configured properly (all necessary data is available). */
	private boolean properlyConfigured = false;

	/** Indicates that the background thread should shutdown after the current execution cycle. */
	protected volatile boolean threadTreminate = false;
	
	/** The instance of the worker or null if there is no thread active at the moment. */
	private Thread thread;
	
	/**
	 * Constructs an active service with the given callback.
	 * 
	 * @param callback the callback to the owner
	 */
	LcnBindingActiveService(Callback callback) {
		this.callback = callback;
	}
	
	/**
	 * Gets the current running-state.
	 * 
	 * @return true if this active service is running
	 */
	boolean isRunning() {
		return this.thread != null ? this.thread.isAlive() : false;
	}
	
	/** Starts the worker thread if it is not running yet. */
	void start() {
		if (!this.properlyConfigured) {
			logger.trace("{} won't be started because it isn't yet properly configured.", THREAD_NAME);
			return;
		}
		this.threadTreminate = false;
		if (!this.isRunning()) {
			this.thread = new Thread(this, THREAD_NAME);
			this.thread.setDaemon(true);
			this.thread.start();
		} else {
			logger.trace("{} is already started > calling start() changed nothing.", THREAD_NAME);
		}
	}
	
	/** Tells the worker thread to stop as soon as possible. */
	void stop() {
		this.threadTreminate = true;
	}

	/**
	 * Used to define whether this binding is fully configured so it can be activated and used.
	 * Note that the active service will be started automatically if true is passed.
	 * 
	 * @param properlyConfigured the configuration state
	 */
	void setProperlyConfigured(boolean properlyConfigured) {
		this.properlyConfigured = properlyConfigured;
		if (this.properlyConfigured && !this.isRunning()) {
			this.start();
		}
		else if (!this.properlyConfigured && this.isRunning()) {
			this.stop();
		}
	}
	
	/** Main method of the thread. */
	@Override
	public void run() {
		logger.info(THREAD_NAME + " has been started");
		while (!this.threadTreminate) {
			try {
				this.callback.execute();
			}
			catch(RuntimeException ex) {
				logger.error("Error while executing background thread " + THREAD_NAME, ex);
			}
		}
		this.thread = null;
		logger.info(THREAD_NAME + " has been shut down");
	}
	
}
