/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.HeaderConfig;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Filter prevents duplicate broadcasts   
 *  
 * @author Oliver Mazur
 * @since 1.0
 *
 *
 */
public class DuplicateBroadcastProtectionFilter implements PerRequestBroadcastFilter {

	private static final Logger logger = LoggerFactory.getLogger(DuplicateBroadcastProtectionFilter.class);
	
	@Override
	public BroadcastAction filter(String broadcasterId, Object originalMessage, Object message) {
		return new BroadcastAction(message);
	}

	@Override
	public BroadcastAction filter(String broadcasterId, AtmosphereResource resource, Object originalMessage, Object message) {
		final  HttpServletRequest request = resource.getRequest();
		
		try {	
			if(!isDoubleBroadcast(request,message ) ){
				return new BroadcastAction(ACTION.CONTINUE,  message);
			}
			else {
				return new BroadcastAction(ACTION.ABORT,  message);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new BroadcastAction(ACTION.ABORT,  message);
		} 
		
	}
	
	private boolean isDoubleBroadcast(HttpServletRequest request,
			Object responseEntity) throws JsonGenerationException,
			JsonMappingException, IOException {
	
		String clientId = request.getHeader(HeaderConfig.X_ATMOSPHERE_TRACKING_ID);

		// return false if the X-Atmosphere-tracking-id is not set
		if (clientId == null || clientId.isEmpty()) {
			return false;
		}

		CacheEntry entry = ResourceStateChangeListener.getCachedEntries().put(
				clientId, new CacheEntry(responseEntity));
		// there was an existing cached entry, see if its the same
		if (entry != null) {
			ObjectMapper mapper = new ObjectMapper();
			// cached data
			final String firedResponse = mapper.writeValueAsString(entry.getData());
			// new data
			final String responseValue = mapper.writeValueAsString(responseEntity);
			// the same ?
			return responseValue.equals(firedResponse); 
		}

		return false;
	}

}
