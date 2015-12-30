/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.broadcaster;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterLifeCyclePolicyListener;
import org.atmosphere.jersey.JerseyBroadcaster;
import org.openhab.io.cv.CVApplication;
import org.openhab.io.cv.internal.listeners.ResourceStateChangeListener;
import org.openhab.model.core.EventType;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oliver Mazur
 * @author Tobias Bräutigam
 * 
 * @since 1.4.0
 */
public class CometVisuBroadcaster extends JerseyBroadcaster implements ModelRepositoryChangeListener {
	private static final Logger logger = LoggerFactory.getLogger(CometVisuBroadcaster.class);
	protected Collection<ResourceStateChangeListener> listeners = Collections.newSetFromMap(new WeakHashMap<ResourceStateChangeListener, Boolean>());
	
	@Override
	public Broadcaster initialize(String name, URI uri, AtmosphereConfig config) {
		super.initialize(name, uri, config);
		this.addBroadcasterLifeCyclePolicyListener(new BroadcasterLifeCyclePolicyListener() {
			
			@Override
			public void onIdle() {
			}
			
			@Override
			public void onEmpty() {
			}
			
			@Override
			public void onDestroy() {
				logger.debug("broadcaster '{}' destroyed", this.toString());
				for (ResourceStateChangeListener l : listeners){
					l.unregisterItems();
					listeners.remove(l);
				}
			}
		});
		CVApplication.modelRepository.addModelRepositoryChangeListener(this);
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

	@Override
	public void modelChanged(String modelName, EventType type) {
		for (ResourceStateChangeListener l : listeners) {
			// Item Model has changed so the listener listen to non existent items and need to be registered again
			l.setBroadcaster(this);
			l.refreshRelevantItems();
			l.registerItems();
		}
	}
}
