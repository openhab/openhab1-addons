/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.filter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Filter prevents duplicate broadcasts   
 *  
 * @author Dan Cunningham
 * @author Oliver Mazur
 * @since 1.0
 *
 *
 */
public class DuplicateBroadcastProtectionFilter implements PerRequestBroadcastFilter {

	private static final Logger logger = LoggerFactory.getLogger(DuplicateBroadcastProtectionFilter.class);
	private static final long CACHE_TIME = 300 * 1000; // 5 mins
	
	ConcurrentMap<String, CacheEntry> cachedMessages = new ConcurrentHashMap<String, CacheEntry>();

	public DuplicateBroadcastProtectionFilter(){
		//clear the uuid cache at a regular interval
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				cleanCache();
			}
		}, CACHE_TIME, CACHE_TIME, TimeUnit.MILLISECONDS);
	}
	
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
		try{
			CacheEntry entry = cachedMessages.put(clientId, new CacheEntry(responseEntity));
			//there was an existing cached entry, see if its the same
			if(entry != null){
				ObjectMapper mapper = new ObjectMapper();
				//cached data
				String firedResponse =  mapper.writeValueAsString(entry.getData()); 
				//new data
				String responseValue =  mapper.writeValueAsString(responseEntity);
				//the same ?
	            if(responseValue.equals(firedResponse)) {
	            	return true;
				}
			}
		} catch (Exception e) {
			logger.error("Could not check if double broadcast",e);
		} 
        return false;
	}
	
	/**
	 * Clean up expired entries in our cache.
	 */
	public void cleanCache(){
		/*
		 * This map object will start to collect dead uuid entries over time, but its 
		 * difficult to know when these uuid's are really not valid anymore.  this
		 * checks for dead entries before adding any new ones.
		 */
		long invalidCacheTime = System.currentTimeMillis() - CACHE_TIME;
		for(String uuid : cachedMessages.keySet()){
			if(cachedMessages.get(uuid).getCacheTime() <= invalidCacheTime )
				cachedMessages.remove(uuid);
		}
	}
	
	/**
	 * A CacheEntry object is stored in our cache map to prevent duplicate messages
	 * @author Dan Cunningham
	 *
	 */
	public class CacheEntry {
		long cacheTime;
		Object data;
		/**
		 * Create a new CacheEntry object the data to cache
		 * @param data
		 */
		public CacheEntry(Object data) {
			super();
			this.data = data;
			this.cacheTime = System.currentTimeMillis();
		}
		/**
		 * 
		 * @return the time the entry was cached
		 */
		public long getCacheTime() {
			return cacheTime;
		}
		/**
		 * 
		 * @return the cached data
		 */
		public Object getData() {
			return data;
		}
	}

}
