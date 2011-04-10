/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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

	protected boolean interrupted = false;
	
	/** holds the instance of the refresh thread or is <code>null</code> if 
	 * there is no thread active at the moment
	 */
	private Thread refreshThread;

	public AbstractActiveService() {
		super();
	}

	public void activate() {
		setInterrupted(false);
		start();
	}

	public void deactivate() {
		setInterrupted(true);
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	/**
	 * Takes care about starting the refresh thread. It either creates a new
	 * RefreshThread if no instance exists or starts the existing instance.
	 */
	protected void start() {
		if (this.refreshThread == null) {
			this.refreshThread = new RefreshThread(getName(), getRefreshInterval());
			this.refreshThread.start();
		}
		else {
			if (!this.refreshThread.isAlive()) {
				this.refreshThread.start();
			}
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
			this.refreshInterval = refreshInterval;
		}
		
		@Override
		public void run() {
			while (!interrupted) {
				try {
					execute();
				} catch(RuntimeException e) {
					logger.error("Error while executing background thread " + getName(), e);
				}
				pause(refreshInterval);
			}
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
				logger.error("pausing " + super.getName() +" throws exception", e);
			}
		}

	}

}