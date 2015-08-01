/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UUIDBroadcasterCache that will enforce that only a single sitemap will exist
 * in the cache for any given resource.  This prevents leaks and other bad things
 * from happening.
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class SingleMessageBroadcastCache extends UUIDBroadcasterCache {

	private final static Logger logger = LoggerFactory.getLogger(SingleMessageBroadcastCache.class);
	
	@Override
    public CacheMessage addToCache(String broadcasterId, String uuid, BroadcastMessage message) {
		if(uuid != null && message.message() instanceof PageBean){
			//remove previous message
			retrieveFromCache(broadcasterId, uuid);
			//add the new message
			return super.addToCache(broadcasterId, uuid, message);
		} else {
			logger.trace("Not caching {}", message.message().getClass().getName());
			return null;
		}
	}
}