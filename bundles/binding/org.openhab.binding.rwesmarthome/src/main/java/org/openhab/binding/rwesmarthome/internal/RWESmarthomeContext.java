/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider;
import org.openhab.binding.rwesmarthome.internal.communicator.RWESmarthomeSession;
import org.openhab.binding.rwesmarthome.internal.communicator.StateHolder;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Context class to hold relevant objects.
 * 
 * @author ollie-dev
 *
 */
public class RWESmarthomeContext {
	
	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeContext.class);
	private static RWESmarthomeContext instance;
	private StateHolder stateHolder;
	private RWESmarthomeConfig config = new RWESmarthomeConfig();
	private RWESmarthomeSession rweSmarthomeSession;
	private EventPublisher eventPublisher;
	private Collection<RWESmarthomeBindingProvider> providers;
	private boolean bindingChanged = false;
	
	/**
	 * used to store events that we have sent ourselves; we need to remember them for not reacting to them
	 */
	private ConcurrentHashMap<String, Long> ignoreEventList = new ConcurrentHashMap<String, Long>(10);
	
	/**
	 * Create or returns the instance of this class.
	 */
	public static RWESmarthomeContext getInstance() {
		if (instance == null) {
			instance = new RWESmarthomeContext();
			instance.stateHolder = new StateHolder(instance);
		}
		return instance;
	}

	/**
	 * Returns the RWESmarthomeConfig.
	 */
	public RWESmarthomeConfig getConfig() {
		return config;
	}

	/**
	 * Returns the session.
	 * 
	 * @return the session
	 */
	public RWESmarthomeSession getRweSmarthomeSession() {
		return rweSmarthomeSession;
	}

	/**
	 * Sets the session.
	 * 
	 * @param rweSmarthomeSession
	 */
	public void setRweSmarthomeSession(RWESmarthomeSession rweSmarthomeSession) {
		this.rweSmarthomeSession = rweSmarthomeSession;
	}

	/**
	 * Sets the EventPublisher for use in the binding.
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	/**
	 * Returns the EventPublisher.
	 * 
	 * @return
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	/**
	 * Returns the binding type.
	 * @return
	 */
	public String getBindingType() {
		return "rwe";
	}

	/**
	 * Returns all RWESmarthomeBindingProviders.
	 */
	public Collection<RWESmarthomeBindingProvider> getProviders() {
		return providers;
	}
	
	/**
	 * Sets all RWESmarthomeBindingProviders for use in the binding.
	 */
	public void setProviders(Collection<RWESmarthomeBindingProvider> providers) {
		this.providers = providers;
	}

	/**
	 * Sets the changed status.
	 * 
	 * @param bindingChanged
	 */
	public void setBindingChanged(boolean bindingChanged) {
		this.bindingChanged  = bindingChanged;
		if(bindingChanged) {
			logger.debug("RWE Smarthome binding marked as changed.");
		}
	}
	
	/**
	 * Returns true, if the binding has changed.
	 * @return
	 */
	public boolean getBindingChanged() {
		return bindingChanged;
	}
	
	/**
	 * Returns the list of events to ignore to avoid handling events from RWE, which we
	 * sent to RWE ourselves.
	 * 
	 * @return
	 */
	public ConcurrentHashMap<String, Long> getIgnoreEventList() {
		return ignoreEventList;
	}
}
