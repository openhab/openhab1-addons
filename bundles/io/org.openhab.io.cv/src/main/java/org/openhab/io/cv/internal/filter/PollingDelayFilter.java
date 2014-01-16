/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.filter;

import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.Item;
import org.openhab.io.cv.internal.broadcaster.CometVisuBroadcaster;
import org.openhab.io.cv.internal.resources.ResponseTypeHelper;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Filter delays the broadcast to polling connections. 
 * The delay is necessary for the completion of group events.
 *   
 *  
 * @author Oliver Mazur
 * @author Tobias Bräutigam
 * @since 1.4.0
 *
 */

public class PollingDelayFilter implements PerRequestBroadcastFilter {
	private static final Logger logger = LoggerFactory.getLogger(PollingDelayFilter.class);
	
	@Override
	public BroadcastAction filter(Object arg0, Object message) {
		return new BroadcastAction(ACTION.CONTINUE, message);
	}

	@Override
	public BroadcastAction filter(final AtmosphereResource resource, final Object originalMessage, final Object message) {
		final  HttpServletRequest request = resource.getRequest();
		try {	
			// delay first broadcast for long-polling and other polling transports
			if(!ResponseTypeHelper.isStreamingTransport(request) && message instanceof ItemStateListBean && originalMessage instanceof Item) {
				final String delayedBroadcasterName = resource.getRequest().getPathInfo();
				Executors.newSingleThreadExecutor().submit(new Runnable() {
		            public void run() {
		                try {
		                	Thread.sleep(300);
							CometVisuBroadcaster delayedBroadcaster = (CometVisuBroadcaster) BroadcasterFactory.getDefault().lookup(CometVisuBroadcaster.class, delayedBroadcasterName);
							
							delayedBroadcaster.broadcast(message, resource);
//							if (request.getParameter("i")!=null) {
//								// send only the changed item values since index
//								delayedBroadcaster.broadcast(delayedBroadcaster.getChanges(Long.valueOf(request.getParameter("i"))), resource);
//							}
//							else {
//								// sends all item values
//								delayedBroadcaster.broadcast(message, resource);
//							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						} 
		            }
		        });
			} else {
				//pass message to next filter
				return new BroadcastAction(ACTION.CONTINUE,  message);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		return new BroadcastAction(ACTION.ABORT,  message);
		
	}
}
