/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.io.rest.internal.listeners;



import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.io.rest.internal.broadcaster.GeneralBroadcaster;
import org.openhab.io.rest.internal.filter.DuplicateBroadcastProtectionFilter;
import org.openhab.io.rest.internal.filter.MessageTypeFilter;
import org.openhab.io.rest.internal.filter.PollingDelayFilter;
import org.openhab.io.rest.internal.filter.ResponseObjectFilter;
import org.openhab.io.rest.internal.filter.SendPageUpdateFilter;
import org.openhab.io.rest.internal.resources.ItemResource;

/**
 * This is an abstract super class which adds Broadcaster config, lifecycle and filters to its derived classes and registers listeners to subscribed resources.   
 *  
 * @author Oliver Mazur
 * @since 0.9.0
 */
abstract public class ResourceStateChangeListener {

	final static ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();

	private Set<String> relevantItems = null;
	private StateChangeListener stateChangeListener;
	private GeneralBroadcaster broadcaster;

	public ResourceStateChangeListener(){}


	public ResourceStateChangeListener(GeneralBroadcaster broadcaster){
		this.broadcaster = broadcaster;
	}
	
	public GeneralBroadcaster getBroadcaster() {
		return broadcaster;
	}
	
	public void setBroadcaster(GeneralBroadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}
	
	public static ConcurrentMap<String, Object> getMap() {
		return map;
	}
	
	public void registerItems(){
		broadcaster.getBroadcasterConfig().addFilter(new PerRequestBroadcastFilter() {
			
			@Override
			public BroadcastAction filter(Object originalMessage, Object message) {
				// TODO Auto-generated method stub
				return new BroadcastAction(ACTION.CONTINUE,  message);
			}

			@Override
			public BroadcastAction filter(AtmosphereResource resource, Object originalMessage, Object message) {
				 HttpServletRequest request = resource.getRequest();
				 return new BroadcastAction(ACTION.CONTINUE,  getResponseObject(request));
			}
		});
		
		broadcaster.getBroadcasterConfig().addFilter(new PollingDelayFilter());
		broadcaster.getBroadcasterConfig().addFilter(new SendPageUpdateFilter());
		broadcaster.getBroadcasterConfig().addFilter(new DuplicateBroadcastProtectionFilter());
		broadcaster.getBroadcasterConfig().addFilter(new ResponseObjectFilter());
		broadcaster.getBroadcasterConfig().addFilter(new MessageTypeFilter());
		
		
		
		stateChangeListener = new StateChangeListener() {
			// broadcast update events only for GroupItems
			public void stateUpdated(Item item, State state) {
				if(item instanceof GroupItem && !broadcaster.getAtmosphereResources().isEmpty()){
					broadcaster.broadcast(item);
				}
			}
			
			public void stateChanged(final Item item, State oldState, State newState) {	
				if(!broadcaster.getAtmosphereResources().isEmpty()){
					broadcaster.broadcast(item);
				}
			}
		};
		registerStateChangeListenerOnRelevantItems(broadcaster.getID(), stateChangeListener);
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
}