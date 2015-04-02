/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.listeners;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.io.cv.internal.broadcaster.CometVisuBroadcaster;
import org.openhab.io.cv.internal.cache.CVBroadcasterCache;
import org.openhab.io.cv.internal.filter.ResponseObjectFilter;
import org.openhab.io.cv.internal.resources.ReadResource;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;


/**
 * This is an abstract super class which adds Broadcaster config, lifecycle and filters to its derived classes and registers listeners to subscribed resources.   
 *  
 * 
 * @author Tobias Bräutigam
 * @since 1.4.0
 *
 */
abstract public class ResourceStateChangeListener {

	private StateChangeListener stateChangeListener;
	private CometVisuBroadcaster broadcaster;

	public ResourceStateChangeListener(){}


	public ResourceStateChangeListener(CometVisuBroadcaster broadcaster){
		this.broadcaster = broadcaster;
	}
	
	public CometVisuBroadcaster getBroadcaster() {
		return broadcaster;
	}
	
	public void setBroadcaster(CometVisuBroadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}
	
	public void registerItems(){
		broadcaster.getBroadcasterConfig().setBroadcasterCache(new CVBroadcasterCache());
		broadcaster.getBroadcasterConfig().getBroadcasterCache().configure(broadcaster.getBroadcasterConfig());
        broadcaster.getBroadcasterConfig().getBroadcasterCache().start();
        
		broadcaster.getBroadcasterConfig().addFilter(new PerRequestBroadcastFilter() {
			
			@Override
			public BroadcastAction filter(String broadcasterId, Object originalMessage, Object message) {
				return new BroadcastAction(message);
			}

			@Override
			public BroadcastAction filter(String broadcasterId, AtmosphereResource resource, 
					Object originalMessage, Object message) {
				 HttpServletRequest request = resource.getRequest();
				 Object responseObject;
				 if (message instanceof Item) {
					 responseObject = getSingleResponseObject((Item)message,request);
				 } else if (message instanceof ItemStateListBean) {
					 responseObject = message;
				 }
				 else {
					 responseObject = getResponseObject(request);
				 }
				 if (responseObject!=null)
					 return new BroadcastAction(ACTION.CONTINUE, responseObject);
				 else
					 return new BroadcastAction(ACTION.ABORT, message);
			}
		});
		
		broadcaster.getBroadcasterConfig().addFilter(new ResponseObjectFilter());
		
		
		
		stateChangeListener = new StateChangeListener() {
			// don't react on update events
            public void stateUpdated(Item item, State state) {
               //updates can be ignored                    
            }

			
			public void stateChanged(final Item item, State oldState, State newState) {
				// broadcast the item, or cache it when there is no resource available at the moment
				broadcaster.broadcast(item);
			}
		};
		registerStateChangeListenerOnRelevantItems(broadcaster.getID(), stateChangeListener);
	}
	
	public void unregisterItems(){
		unregisterStateChangeListenerOnRelevantItems();
	}
    

	protected void registerStateChangeListenerOnRelevantItems(String pathInfo, StateChangeListener stateChangeListener ) {
		for(String name : getRelevantItemNames()) {
			registerChangeListenerOnItem(stateChangeListener, name);
		}
	}

	protected void unregisterStateChangeListenerOnRelevantItems() {
		for(String itemName : getRelevantItemNames()) {
			unregisterChangeListenerOnItem(stateChangeListener, itemName);
		}
	}

	private void registerChangeListenerOnItem(
			StateChangeListener stateChangeListener, String itemName) {
		Item item = ReadResource.getItem(itemName);
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.addStateChangeListener(stateChangeListener);
			
		}
	}

	private void unregisterChangeListenerOnItem(
			StateChangeListener stateChangeListener, String itemName) {
		Item item = ReadResource.getItem(itemName);
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
	abstract protected List<String> getRelevantItemNames();

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
}