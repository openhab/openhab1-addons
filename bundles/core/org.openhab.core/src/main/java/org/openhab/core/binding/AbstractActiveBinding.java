/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	 * 
	 * @param properlyConfigured
	 */
	protected void setProperlyConfigured(boolean properlyConfigured) {
		if (providers.size() > 0) {
			activeService.setProperlyConfigured(properlyConfigured);
		}
	}
	
	/**
	 * @return <code>true</code> if this binding is configured properly which means
	 * that all necessary data is available
	 */
	protected boolean isProperlyConfigured() {
		return activeService.isProperlyConfigured();
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
			super.start();
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

	}
	
}