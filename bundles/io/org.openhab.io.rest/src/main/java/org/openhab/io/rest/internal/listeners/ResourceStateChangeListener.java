/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.listeners;



import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.BroadcasterConfig;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.io.rest.internal.broadcaster.GeneralBroadcaster;
import org.openhab.io.rest.internal.filter.DuplicateBroadcastProtectionFilter;
import org.openhab.io.rest.internal.filter.PollingDelayFilter;
import org.openhab.io.rest.internal.filter.ResponseObjectFilter;
import org.openhab.io.rest.internal.filter.SendPageUpdateFilter;
import org.openhab.io.rest.internal.resources.ItemResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an abstract super class which adds Broadcaster config, lifecycle and filters to its derived classes and registers listeners to subscribed resources.   
 *  
 * @author Oliver Mazur
 * @since 0.9.0
 */
abstract public class ResourceStateChangeListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ResourceStateChangeListener.class);

	final static long CACHE_TIME = 300 * 1000; // 5 mins
	
	final static ConcurrentMap<String, CacheEntry> cachedEntries = new ConcurrentHashMap<String, CacheEntry>();
	
	static ScheduledFuture<?> executorFuture;
	
	protected Item lastChange;
	private Set<String> relevantItems = null;
	private StateChangeListener stateChangeListener;
	protected GeneralBroadcaster broadcaster;

	public ResourceStateChangeListener(){
		
	}


	public ResourceStateChangeListener(GeneralBroadcaster broadcaster){
		this.broadcaster = broadcaster;
	}
	
	public GeneralBroadcaster getBroadcaster() {
		return broadcaster;
	}
	
	public void setBroadcaster(GeneralBroadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}
	
	public static ConcurrentMap<String, CacheEntry> getCachedEntries() {
		return cachedEntries;
	}
	
	/**
	 * Configure what cache we want to use
	 * @param config
	 */
	public void configureCache(BroadcasterConfig config){
		config.setBroadcasterCache(new UUIDBroadcasterCache());
		config.getBroadcasterCache().configure(broadcaster.getBroadcasterConfig());
		config.getBroadcasterCache().start();
	}
	
	public void registerItems(){
		StartCacheExecutor();
		BroadcasterConfig config = broadcaster.getBroadcasterConfig();
		
		configureCache(config);

		addBroadcastFilter(config, new PerRequestBroadcastFilter() {

			@Override
			public BroadcastAction filter(String broadcasterId,
					Object originalMessage, Object message) {
				return new BroadcastAction(message);
			}

			@Override
			public BroadcastAction filter(String broadcasterId,
					AtmosphereResource resource, Object originalMessage,
					Object message) {
				HttpServletRequest request = null;
				BroadcastAction result = null;
				try {
					request = resource.getRequest();
					Object response = getResponseObject(request);
					result = new BroadcastAction(ACTION.CONTINUE, response);
				} catch (Exception e) {
					result = new BroadcastAction(ACTION.ABORT,
							getResponseObject(request));
				}
				return result;
			}

		});
		
		addBroadcastFilter(config, new PollingDelayFilter());
		addBroadcastFilter(config, new SendPageUpdateFilter());
		addBroadcastFilter(config, new DuplicateBroadcastProtectionFilter());
		addBroadcastFilter(config, new ResponseObjectFilter());
				
		stateChangeListener = new StateChangeListener() {
			// don't react on update events
			public void stateUpdated(Item item, State state) {
//				broadcaster.broadcast(item);
				// if the group has a base item and thus might calculate its state
				// as a DecimalType or other, we also consider it to be necessary to
				// send an update to the client as the label of the item might have changed,
				// even though its state is yet the same.
//				if(item instanceof GroupItem) {
//					GroupItem gItem = (GroupItem) item;
//					if(gItem.getBaseItem()!=null) {
//						Collection<AtmosphereResource> resources = broadcaster.getAtmosphereResources();
//						if(!resources.isEmpty()) {
//							for (AtmosphereResource resource : resources) {
//								broadcaster.broadcast(item, resource);
//							}
//						}
//					}
//				}
			}
			
			public void stateChanged(final Item item, State oldState, State newState) {
				lastChange = item;
				broadcaster.broadcast(item);
//				Collection<AtmosphereResource> resources = broadcaster.getAtmosphereResources();
//				if(!resources.isEmpty()) {
//					for (AtmosphereResource resource : resources) {
//						broadcaster.broadcast(item, resource);
//					}
//				}

//				if(!broadcaster.getAtmosphereResources().isEmpty()) {
//					broadcaster.broadcast(item);
//				}
			}
		};
		
		registerStateChangeListenerOnRelevantItems(broadcaster.getID(), stateChangeListener);
	}


	private void addBroadcastFilter(BroadcasterConfig config,
			BroadcastFilter filter) {
		if (!config.addFilter(filter) && logger.isDebugEnabled()) {
			logger.debug("Could not add filter '{}'", filter.getClass().getName());
		}
	}
	
	public void unregisterItems(){
		unregisterStateChangeListenerOnRelevantItems();
	}
    

	protected void registerStateChangeListenerOnRelevantItems(String pathInfo, StateChangeListener stateChangeListener ) {
		relevantItems = getRelevantItemNames(pathInfo);
		for(String itemName : relevantItems) {
			registerChangeListenerOnItem(stateChangeListener, itemName);
		}
	}

	protected void unregisterStateChangeListenerOnRelevantItems() {
		
		if(relevantItems!=null) {
			for(String itemName : relevantItems) {
				unregisterChangeListenerOnItem(stateChangeListener, itemName);
			}
		}
	}

	private void registerChangeListenerOnItem(
			StateChangeListener stateChangeListener, String itemName) {
		Item item = ItemResource.getItem(itemName);
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.addStateChangeListener(stateChangeListener);
			
		}
	}

	private void unregisterChangeListenerOnItem(
			StateChangeListener stateChangeListener, String itemName) {
		Item item = ItemResource.getItem(itemName);
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.removeStateChangeListener(stateChangeListener);
		}
	}

	/**
	 * Returns a set of all items that should be observed for this request. A status change of any of
	 * those items will resume the suspended request.
	 * 
	 * @param pathInfo the pathInfo object from the http request
	 * @return a set of item names
	 */
	abstract protected Set<String> getRelevantItemNames(String pathInfo);

	/**
	 * Determines the response content for an HTTP request.
	 * This method has to do all the HTTP header evaluation itself that is normally
	 * done through Jersey annotations (if anybody knows a way to avoid this, let
	 * me know!)
	 * 
	 * @param request the HttpServletRequest
	 * @return the response content
	 */
	abstract protected Object getResponseObject(final HttpServletRequest request);
	
	/**
	 * Determines the response content for a single item.
	 * This method has to do all the HTTP header evaluation itself that is normally
	 * done through Jersey annotations (if anybody knows a way to avoid this, let
	 * me know!)
	 * 
	 * @param item the Item object
	 * @param request the HttpServletRequest
	 * @return the response content
	 */
	abstract protected Object getSingleResponseObject(Item item, final HttpServletRequest request);
	
	static void StartCacheExecutor(){
		if(executorFuture == null || executorFuture.isCancelled()){
			executorFuture = Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					cleanCache();
				}
			}, CACHE_TIME, CACHE_TIME, TimeUnit.MILLISECONDS);
		}
	}
	
	/**
	 * Clean up expired entries in our cache.
	 */
	static void cleanCache(){
		/*
		 * This map object will start to collect dead uuid entries over time, but its 
		 * difficult to know when these uuid's are really not valid anymore.
		 */
		long invalidCacheTime = System.currentTimeMillis() - CACHE_TIME;
		for(String uuid : cachedEntries.keySet()){
			if(cachedEntries.get(uuid).getCacheTime() <= invalidCacheTime )
				cachedEntries.remove(uuid);
		}
	}
	
	/**
	 * A CacheEntry object is stored in our static cache map to prevent duplicate messages
	 * @author Dan Cunningham
	 *
	 */
	public static class CacheEntry {
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