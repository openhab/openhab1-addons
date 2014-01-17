/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.cache;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcasterCache;
import org.openhab.core.items.Item;
import org.openhab.io.cv.internal.resources.beans.ItemListBean;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
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
public class CVBroadcasterCache extends UUIDBroadcasterCache {

    private final static Logger logger = LoggerFactory.getLogger(CVBroadcasterCache.class);
    
    @Override
	public List<Object> retrieveFromCache(String broadcasterId,
			AtmosphereResource r) {
		List<Object> result = new ArrayList<Object>();
		ItemStateListBean response = new ItemStateListBean(new ItemListBean());
		for (Object cacheMessage : super.retrieveFromCache(broadcasterId, r)) {
			if (cacheMessage instanceof ItemStateListBean) {
				ItemStateListBean cachedStateList = (ItemStateListBean) cacheMessage;
				// add states to the response (maybe a comparison is needed here
				// so that only the last state of an item is used)
				response.stateList.entries
						.addAll(cachedStateList.stateList.entries);
				if (response.index < cachedStateList.index) {
					response.index = cachedStateList.index;
				}
			} else if (cacheMessage instanceof Item) {
				Item item = (Item) cacheMessage;
				response.stateList.entries.add(new JAXBElement(new QName(item
						.getName()), String.class, item.getState().toString()));
			}
		}
		if (response.stateList.entries.size() > 0) {
			if (response.index == 0) {
				response.index = System.currentTimeMillis();
			}
			result.add(response);
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Retrieved for AtmosphereResource {} cached messages {}",
				r.uuid(), result);
		}
		return result;
	}

}

