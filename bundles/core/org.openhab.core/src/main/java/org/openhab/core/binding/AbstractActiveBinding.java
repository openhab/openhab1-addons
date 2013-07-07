/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.core.binding;

import org.openhab.core.service.AbstractActiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Base class for active bindings which polls something and sends events frequently.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * 
 * @since 0.6.0
 */
public abstract class AbstractActiveBinding<P extends BindingProvider> extends AbstractBinding<P> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractActiveBinding.class);

	/** embedded active service to allow the binding to have some code executed in a given interval. */
	protected AbstractActiveService activeService = new BindingActiveService();

	/** <code>true</code> if this binding is configured properly which means that all necessary data is available */
	private boolean properlyConfigured = false;
		
	/**
	 * Adds <code>provider</code> to the list of {@link BindingProvider}s and 
	 * adds <code>this</code> as {@link BindingChangeListener}. If 
	 * <code>provider</code> contains any binding an the refresh-Thread is
	 * stopped it will be started.
	 * 
	 * @param provider the new {@link BindingProvider} to add
	 */
	public void addBindingProvider(P provider) {
		super.addBindingProvider(provider);
		activeService.activate();
	}

	/**
	 * Removes <code>provider</code> from the list of providers. If there is no
	 * provider left the refresh thread is getting interrupted.
	 * 
	 * @param provider the {@link BindingProvider} to remove
	 */
	public void removeBindingProvider(P provider) {
		super.removeBindingProvider(provider);
		
		// if there are no binding providers there is no need to run this 
		// refresh thread any longer ...
		if (this.providers.size() == 0) {
			activeService.deactivate();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		
		if (bindingsExist()) {
			activeService.activate();
		} else {
			activeService.deactivate();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {
		super.allBindingsChanged(provider);
		
		if (bindingsExist()) {
			activeService.activate();
		} else {
			activeService.deactivate();
		}
	}
	
	/**
	 * Used to define whether this binding is fully configured so that it can be
	 * activated and used.
	 * Note that the implementation will automatically start the active service if
	 * <code>true</code> is passed as a parameter and there are binding providers available.
	 */
	protected void setProperlyConfigured(boolean properlyConfigured) {
		this.properlyConfigured = properlyConfigured;
		if(properlyConfigured || !activeService.isRunning() && providers.size() > 0) {
			activeService.activate();
		}
	}
			
	/**
	 * @return <code>true</code> if this binding is configured properly which means
	 * that all necessary data is available
	 */
	final protected boolean isProperlyConfigured() {
		return properlyConfigured;
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
	
	
	/** private inner class, which delegates method calls to the outer binding instance */
	private class BindingActiveService extends AbstractActiveService {

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void start() {
			if (bindingsExist()) {
				super.start();
			} else {
				logger.trace("{} won't be started because no bindings exist.", getName());
			}
		}

		/**
		 * @{inheritDoc}
		 */
		@Override
		public void interrupt() {
			if (!bindingsExist()) {
				super.interrupt();
			} else {
				logger.trace("{} won't be interrupted because bindings exist.", getName());
			}
		}

		@Override
		protected void execute() {
			AbstractActiveBinding.this.execute();
		}

		@Override
		protected long getRefreshInterval() {
			return AbstractActiveBinding.this.getRefreshInterval();
		}

		@Override
		protected String getName() {
			return AbstractActiveBinding.this.getName();
		}

		@Override
		public boolean isProperlyConfigured() {
			return AbstractActiveBinding.this.isProperlyConfigured();
		}
	}
	
}