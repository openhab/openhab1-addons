/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.common;

import java.util.Collection;

import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.communicator.CcuStateHolder;
import org.openhab.binding.homematic.internal.communicator.client.TclRegaScriptClient;
import org.openhab.binding.homematic.internal.converter.ConverterFactory;
import org.openhab.core.events.EventPublisher;

/**
 * Singleton with the important objects for this binding.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HomematicContext {
	private EventPublisher eventPublisher;
	private Collection<HomematicBindingProvider> providers;
	private HomematicConfig config = new HomematicConfig();
	private ConverterFactory converterFactory = new ConverterFactory();
	private TclRegaScriptClient tclRegaScriptClient;
	private CcuStateHolder stateHolder;

	private static HomematicContext instance;

	private HomematicContext() {
	}

	/**
	 * Create or returns the instance of this class.
	 */
	public static HomematicContext getInstance() {
		if (instance == null) {
			instance = new HomematicContext();
			instance.tclRegaScriptClient = new TclRegaScriptClient(instance);
			instance.stateHolder = new CcuStateHolder(instance);
		}
		return instance;
	}

	/**
	 * Returns the HomematicConfig.
	 */
	public HomematicConfig getConfig() {
		return config;
	}

	/**
	 * Returns the EventPublisher.
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	/**
	 * Sets the EventPublisher for use in the binding.
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Returns all HomematicBindingProviders.
	 */
	public Collection<HomematicBindingProvider> getProviders() {
		return providers;
	}

	/**
	 * Sets all HomematicBindingProviders for use in the binding.
	 */
	public void setProviders(Collection<HomematicBindingProvider> providers) {
		this.providers = providers;
	}

	/**
	 * Returns the CoverterFactory.
	 */
	public ConverterFactory getConverterFactory() {
		return converterFactory;
	}

	/**
	 * Returns the TclRegaScriptClient.
	 */
	public TclRegaScriptClient getTclRegaScriptClient() {
		return tclRegaScriptClient;
	}

	/**
	 * Returns the CcuStateHolder.
	 */
	public CcuStateHolder getStateHolder() {
		return stateHolder;
	}
}
