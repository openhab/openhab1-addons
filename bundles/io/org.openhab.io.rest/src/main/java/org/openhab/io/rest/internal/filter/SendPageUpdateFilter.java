/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.filter;

import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.HeaderConfig;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.Item;
import org.openhab.io.rest.internal.broadcaster.GeneralBroadcaster;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener.CacheEntry;
import org.openhab.io.rest.internal.resources.ResponseTypeHelper;
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Filter sends page updates to websocket or streaming connections
 * The filter needs the X-Atmosphere-tracking-id HTTP Header to determine the client
 *  
 * @author Oliver Mazur
 * @since 1.0
 *
 *
 */

public class SendPageUpdateFilter implements PerRequestBroadcastFilter {
	private static final Logger logger = LoggerFactory.getLogger(SendPageUpdateFilter.class);
	
	@Override
	public BroadcastAction filter(String broadcasterId, Object originalMessage, Object message) {
		return new BroadcastAction(message);
	}

	@Override
	public BroadcastAction filter(String broadcasterId, final AtmosphereResource resource, Object originalMessage, final Object message) {
		final  HttpServletRequest request = resource.getRequest();
		try {	
			// broadcast page updates to streaming transports
			if(ResponseTypeHelper.isStreamingTransport(request)){
				if( message instanceof PageBean && originalMessage instanceof Item) {
					// check if the page icon or label has been changed and do a separate broadcast for the changed page object
					final String delayedBroadcasterName = resource.getRequest().getPathInfo();
					if (isPageUpdated(request, message)){	
						Executors.newSingleThreadExecutor().submit(new Runnable() {
				            public void run() {
				                try {
				                    Thread.sleep(300);
				                    
				                    BroadcasterFactory broadcasterFactory = resource.getAtmosphereConfig().getBroadcasterFactory();
			                		GeneralBroadcaster delayedBroadcaster = broadcasterFactory.lookup(GeneralBroadcaster.class, delayedBroadcasterName);
			                		delayedBroadcaster.broadcast(message, resource);				                	
									
								} catch (Exception e) {
									logger.error("Could not broadcast messages", e);
								} 
				            }
				        });
					}
				}
				// remove the widgets
				if (originalMessage instanceof PageBean){
	        		final PageBean responseBean = clonePageBeanWithoutWidgets((PageBean) message);
	        		return new BroadcastAction(ACTION.CONTINUE,  responseBean);
				}
			}
			
			//pass message to next filter
			return new BroadcastAction(ACTION.CONTINUE, message);
		
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new BroadcastAction(ACTION.ABORT, message);
		} 
		
		
	}

	private PageBean clonePageBeanWithoutWidgets(PageBean originalBean) {
		final PageBean responseBean = new PageBean();
		responseBean.icon = originalBean.icon;
		responseBean.id = originalBean.id;
		responseBean.link = originalBean.link;
		responseBean.parent = originalBean.parent;
		responseBean.title = originalBean.title;
		// TODO What to do with (the new) leaf attribute?
		return responseBean;
	}
	
	private boolean isPageUpdated(HttpServletRequest request, Object responseEntity) {
		// TODO: Atmosphere docs say, the param can be a request param, too!
		final String clientId = request.getHeader(HeaderConfig.X_ATMOSPHERE_TRACKING_ID);
		
		// return false if the X-Atmosphere-tracking-id is not set
		if(clientId == null || clientId.isEmpty()){
			return false;
		}
		
		final CacheEntry entry =  ResourceStateChangeListener.getCachedEntries().get(clientId); 
		if(entry != null && entry.getData() instanceof PageBean){
			final PageBean firedEntity = (PageBean)entry.getData();
			final PageBean responsePageBean = (PageBean)responseEntity;
			if( firedEntity == null || 
					firedEntity.icon != responsePageBean.icon ||  
					firedEntity.title != responsePageBean.title    ) {
		    	return true;
		    }
		}
            
        return false;
	}
}
