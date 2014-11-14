/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.filter;

import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceFactory;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener;
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
	public BroadcastAction filter(Object arg0, Object message) {
		return new BroadcastAction(ACTION.CONTINUE, message);
	}

	@Override
	public BroadcastAction filter(AtmosphereResource resource, Object originalMessage, Object message) {
		final  HttpServletRequest request = resource.getRequest();
		
		try {	
			if(!isDoubleBroadcast(request,message ) ){
				return new BroadcastAction(ACTION.CONTINUE,  message);
			}
			else {
				return new BroadcastAction(ACTION.ABORT,  message);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BroadcastAction(ACTION.ABORT,  message);
		} 
		
	}
	
	private boolean isDoubleBroadcast(HttpServletRequest request, Object responseEntity){
		String clientId = request.getHeader("X-Atmosphere-tracking-id");
		
		// return false if the X-Atmosphere-tracking-id is not set
		if(clientId == null || clientId.isEmpty()){
			return false;
		}
		ObjectMapper mapper = new ObjectMapper();
		try{
			/*
			 * This map object will start to collect dead uuid entries over time, but its 
			 * difficult to know when this uuid's are really not valid anymore.  this
			 * checks for dead entries before adding any new ones.
			 */
			ConcurrentMap<String, Object> resources = ResourceStateChangeListener.getMap();
			for(String uuid : resources.keySet()){
				AtmosphereResource resource = AtmosphereResourceFactory.getDefault().find(uuid);
				if(resource == null){
					logger.trace("removing {} from duplicate cache", uuid);
					resources.remove(uuid);
				}
			}
			String firedResponse =  mapper.writeValueAsString(ResourceStateChangeListener.getMap().put(clientId, responseEntity)); 
			String responseValue =  mapper.writeValueAsString(responseEntity);
            if(responseValue.equals(firedResponse)) {
            	logger.trace("Duplicate message for uuid {}", clientId);
            	return true;
			}
		} catch (Exception e) {
			logger.error("Could not check if double broadcast",e);
		} 
        return false;
	}

}
