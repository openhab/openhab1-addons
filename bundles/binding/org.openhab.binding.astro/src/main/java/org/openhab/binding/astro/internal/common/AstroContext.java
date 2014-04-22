/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.common;

import java.util.Collection;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.core.events.EventPublisher;

/**
 * Singleton with the important objects for this binding.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class AstroContext {
	private EventPublisher eventPublisher;
	private Collection<AstroBindingProvider> providers;
	private AstroConfig config = new AstroConfig();

	private static AstroContext instance;

	private AstroContext() {
	}

	/**
	 * Create or returns the instance of this class.
	 */
	public static AstroContext getInstance() {
		if (instance == null) {
			instance = new AstroContext();
		}
		return instance;
	}

	/**
	 * Returns the AstroConfig.
	 */
	public AstroConfig getConfig() {
		return config;
	}

	/**
	 * Sets the EventPublisher for use in the binding.
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Returns the EventPublisher.
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	/**
	 * Returns all AstroBindingProviders.
	 */
	public Collection<AstroBindingProvider> getProviders() {
		return providers;
	}

	/**
	 * Sets all AstroBindingProviders for use in the binding.
	 */
	public void setProviders(Collection<AstroBindingProvider> providers) {
		this.providers = providers;
	}

}
