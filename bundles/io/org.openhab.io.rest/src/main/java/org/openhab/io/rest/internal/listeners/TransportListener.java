/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.io.rest.internal.resources.ItemResource;
import org.openhab.io.rest.internal.resources.MediaTypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an abstract super class for listener to events of the Atmosphere framework. 
 * Such listeners are called whenever some state change occurs about suspended requests.
 * 
 * Note that we are always using SCOPE.REQUEST for the atmosphere resources, so we have
 * our own instance of this class for every request. We can therefore use private fields
 * to store some information for a request.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
abstract public class TransportListener implements AtmosphereResourceEventListener {

	private static final Logger logger = LoggerFactory.getLogger(TransportListener.class);

	private StateChangeListener stateChangeListener = null;
	private Set<String> relevantItems = null;
	
    public void onSuspend(final AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
    	final HttpServletRequest request = event.getResource().getRequest();
    	String transport = request.getHeader("X-Atmosphere-Transport");
    	String upgrade = request.getHeader("Upgrade");
    	if((transport==null || transport.isEmpty()) && (upgrade==null || !upgrade.equalsIgnoreCase("websocket"))) {
    		event.getResource().resume();
    		return;
    	}
    	
    	// we need a listener which we attach to our items
    	stateChangeListener = new StateChangeListener() {
    		private volatile Boolean fired = false;
    		
			public void stateUpdated(Item item, State state) {}
			
			public void stateChanged(Item item, State oldState, State newState) {
				synchronized (fired) {
					if(!fired) {
						fired = true;
						// a change happened, so we are done and send the response!
						event.getResource().getBroadcaster().broadcast(getResponseObject(event));
					}
					// else do nothing as we have already sent the update
				}
			}
		};
		registerStateChangeListenerOnRelevantItems(request, stateChangeListener);
    }

    public void onResume(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
		if(event.isResumedOnTimeout()) {
			// we want to send the current state back to the caller
			event.getResource().getBroadcaster().broadcast(getResponseObject(event));
		}
		
		// remove our listener again
    	unregisterStateChangeListenerOnRelevantItems();
    }

    public void onDisconnect(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
		// remove our listener again
		unregisterStateChangeListenerOnRelevantItems();
    }

    public void onBroadcast(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
		// remove our listener again
		unregisterStateChangeListenerOnRelevantItems();
    }

    public void onThrowable(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        logger.warn("Error in suspended request", event.throwable());
    }

	protected void registerStateChangeListenerOnRelevantItems(
			HttpServletRequest request, StateChangeListener stateChangeListener) {
		relevantItems = getRelevantItemNames(request);
		for(String itemName : relevantItems) {
			registerChangeListenerOnItem(stateChangeListener, itemName);
		}
	}

	protected void unregisterStateChangeListenerOnRelevantItems() {
		if(relevantItems!=null && stateChangeListener!=null) {
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

	protected String getResponseType(HttpServletRequest request) {
		List<MediaType> mediaTypes = getAcceptedMediaTypes(request);
		String type = getQueryParam(request, "type");		
		String responseType = MediaTypeHelper.getResponseMediaType(mediaTypes, type);
		return responseType;
	}

	protected List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
		String[] acceptableMediaTypes = request.getHeader(HttpHeaders.ACCEPT).split(",");
		List<MediaType> mediaTypes = new ArrayList<MediaType>(acceptableMediaTypes.length);
		for(String type : acceptableMediaTypes) {
			MediaType mediaType = MediaType.valueOf(type.trim());
			if(mediaType!=null) {
				mediaTypes.add(mediaType);
			}
		}
		return mediaTypes;
	}

	protected String getQueryParam(HttpServletRequest request, String paramName) {
		if(request.getQueryString()==null) return null;
		String[] pairs = request.getQueryString().split("&");
		for(String pair : pairs) {
			String[] keyValue = pair.split("=");
			if(keyValue[0].trim().equals(paramName)) {
				return keyValue[1].trim();
			}
		}
		return null;
	}

	protected String getQueryParam(HttpServletRequest request, String paramName, String defaultValue) {
		String value = getQueryParam(request, paramName);
		return value!=null ? value : defaultValue;
	}

	/**
	 * Returns a set of all items that should be observed for this request. A status change of any of
	 * those items will resume the suspended request.
	 * 
	 * @param request the HTTP request
	 * @return a set of item names
	 */
	abstract protected Set<String> getRelevantItemNames(HttpServletRequest request);

	/**
	 * Determines the response content for an HTTP request.
	 * This method has to do all the HTTP header evaluation itself that is normally
	 * done through Jersey annotations (if anybody knows a way to avoid this, let
	 * me know!)
	 * 
	 * @param event the Atmosphere event, containing the request and response
	 * @return the response content
	 */
	abstract protected Object getResponseObject(final AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event);
}