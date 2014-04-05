/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.cache;

import org.atmosphere.cache.BroadcastMessage;
import org.atmosphere.cache.CacheMessage;
import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcasterCache;
import org.openhab.core.items.Item;
import org.openhab.io.rest.internal.resources.beans.ItemListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 * An {@link BroadcasterCache} implementation based on
 * {@link UUIDBroadcasterCache} which aggregates the cached messages to one
 * single message containing all changed states.
 * 
 * @author Tobias Bräutigam
 * 
 * @since 1.4.0
 */
public class RestBroadcasterCache extends UUIDBroadcasterCache {

    private final static Logger logger = LoggerFactory.getLogger(RestBroadcasterCache.class);
    
    @Override
    public CacheMessage addToCache(String broadcasterId, AtmosphereResource r, BroadcastMessage e) {
    	CacheMessage result = super.addToCache(broadcasterId, r, e);
    	return result;
    }
    
    @Override
	public List<Object> retrieveFromCache(String broadcasterId,
			AtmosphereResource r) {
		List<Object> result = new ArrayList<Object>();
		result = super.retrieveFromCache(broadcasterId, r);
		return result;
	}
}

