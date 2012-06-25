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


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.HeaderConfig;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.io.rest.internal.broadcaster.GeneralBroadcaster;
import org.openhab.io.rest.internal.resources.ItemResource;
import org.openhab.io.rest.internal.resources.MediaTypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an abstract super class which adds Broadcaster config, lifecycle and filters to its derived classes and registers listeners to subscribed resources.   
 *  
 * @author Kai Kreuzer
 * @author Oliver Mazur
 * @since 0.9.0
 */
abstract public class ResourceStateChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(ResourceStateChangeListener.class);
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
	
	public void registerItems(){
		broadcaster.getBroadcasterConfig().addFilter(new PerRequestBroadcastFilter() {
			
			private volatile Object firedObject = new Object();
			ObjectMapper mapper = new ObjectMapper();
			
			@Override
			public BroadcastAction filter(AtmosphereResource resource, Object originalMessage, Object message) {
				HttpServletRequest request = resource.getRequest();
				try {
					synchronized (firedObject) {
						String firedResponse =   firedObject instanceof Response ? mapper.writeValueAsString(((Response) firedObject).getEntity()) : "" ;
						Response response = null;
						if(isStreamingTransport(request)){
							if(message instanceof Item) {
								response = (Response) getSingleResponseObject((Item)message, request);			
							} else return new BroadcastAction(ACTION.ABORT, message);
						} 
						else {
							response = (Response) getResponseObject(request);
						}
						
						String newResponse = mapper.writeValueAsString( response.getEntity());
						if(!newResponse.equals(firedResponse)) {
							firedObject = response;
							return new BroadcastAction(ACTION.CONTINUE, response);
						}
					}
					
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
				return new BroadcastAction(ACTION.ABORT, message);
				
			}

			@Override
			public BroadcastAction filter(Object originalMessage, Object message) {
				// TODO Auto-generated method stub
				return new BroadcastAction(ACTION.CONTINUE,  message);
			}
		});
		

		
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
	 * Returns a boolean. The method detects if the underlying connection should be resumed after broadcast
	 * 
	 * @param request the HttpServletRequest
	 * @return boolean
	 */
	public static boolean isStreamingTransport(HttpServletRequest request) {
        String transport = request.getHeader(HeaderConfig.X_ATMOSPHERE_TRANSPORT);
		String upgrade = request.getHeader(HeaderConfig.WEBSOCKET_UPGRADE);
		if(HeaderConfig.WEBSOCKET_TRANSPORT.equalsIgnoreCase(transport) || HeaderConfig.STREAMING_TRANSPORT.equalsIgnoreCase(transport) || HeaderConfig.WEBSOCKET_TRANSPORT.equalsIgnoreCase(upgrade)) {
		        return true;
		} else {
		        return false;
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