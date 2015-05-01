/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.broadcaster;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterLifeCyclePolicyListener;
import org.atmosphere.jersey.JerseyBroadcaster;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oliver Mazur
 *
 */
public class GeneralBroadcaster extends JerseyBroadcaster {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneralBroadcaster.class);
	protected Collection<ResourceStateChangeListener> listeners = Collections.newSetFromMap(new WeakHashMap<ResourceStateChangeListener, Boolean>());
	
	@Override
	public Broadcaster initialize(String name, URI uri, AtmosphereConfig config) {
		super.initialize(name, uri, config);
		
		this.addBroadcasterLifeCyclePolicyListener(new BroadcasterLifeCyclePolicyListener() {
			
			@Override
			public void onIdle() {
				logger.debug("broadcaster '{}' is idle", this.toString());
			}
			
			@Override
			public void onEmpty() {
				logger.debug("broadcaster '{}' is empty", this.toString());
			}
			
			@Override
			public void onDestroy() {
				logger.debug("broadcaster '{}' destroyed", this.toString());
				logger.trace("broadcaster '{}' left {} {} instaces", this.toString(), listeners.size(), ResourceStateChangeListener.class.getName());
				for (ResourceStateChangeListener listener : listeners){
					listener.unregisterItems();
					boolean removed = listeners.remove(listener);
					if (!removed) logger.warn("Could not remove event listener '{}', this may cause a memory leak.", listener.toString());
				}
			}
		});
		
		return this;
	}
	
	public void addStateChangeListener(final ResourceStateChangeListener listener){
		synchronized (listeners) {
			if(listeners.isEmpty()){
				listener.setBroadcaster(this);
				listener.registerItems();
				listeners.add(listener);	
			}
		}

	}


}
