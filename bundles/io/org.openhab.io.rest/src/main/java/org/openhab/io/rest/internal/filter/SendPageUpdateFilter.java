/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.Item;
import org.openhab.io.rest.internal.broadcaster.GeneralBroadcaster;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener;
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
	public BroadcastAction filter(Object arg0, Object message) {
		return new BroadcastAction(ACTION.CONTINUE, message);
	}

	@Override
	public BroadcastAction filter(final AtmosphereResource resource, Object originalMessage, final Object message) {
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
			                		
			                		GeneralBroadcaster delayedBroadcaster = (GeneralBroadcaster) BroadcasterFactory.getDefault().lookup(GeneralBroadcaster.class, delayedBroadcasterName);
			                		delayedBroadcaster.broadcast(message, resource);
				                	
									
								} catch (Exception e) {
									logger.error(e.getMessage());
								} 
				            }
				        });
					}
				}
				// remove the widgets
				if (originalMessage instanceof PageBean){
					PageBean originalBean = (PageBean) message ;
	        		PageBean responseBeam = new PageBean();
	        		responseBeam.icon = originalBean.icon;
	        		responseBeam.id = originalBean.id;
	        		responseBeam.link = originalBean.link;
	        		responseBeam.parent = originalBean.parent;
	        		responseBeam.title = originalBean.title;
	        		return new BroadcastAction(ACTION.CONTINUE,  responseBeam);
				}
			}
			
			//pass message to next filter
			return new BroadcastAction(ACTION.CONTINUE,  message);
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return new BroadcastAction(ACTION.ABORT,  message);
		} 
		
		
	}
	
	private boolean isPageUpdated(HttpServletRequest request, Object responseEntity){
		String clientId = request.getHeader("X-Atmosphere-tracking-id");
		
		// return false if the X-Atmosphere-tracking-id is not set
		if(clientId == null || clientId.isEmpty()){
			return false;
		}
		
		Object firedEntity =  ResourceStateChangeListener.getMap().get(clientId); 
		if(firedEntity==null || firedEntity instanceof PageBean){
			if( firedEntity == null ||  ((PageBean)firedEntity).icon != ((PageBean)responseEntity).icon ||  ((PageBean)firedEntity).title != ((PageBean)responseEntity).title    ) {
		    	return true;
		    }
		}
            
        return false;
	}
}
