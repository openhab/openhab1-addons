/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.filter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.io.rest.internal.resources.MediaTypeHelper;
import org.openhab.io.rest.internal.resources.ResponseTypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * This Filter creates the correct responseObject for the requested MediaType   
 *  
 * @author Oliver Mazur
 * @since 1.0
 *
 *
 */

public class MessageTypeFilter implements PerRequestBroadcastFilter {
	private static final Logger logger = LoggerFactory.getLogger(MessageTypeFilter.class);
	
	@Override
	public BroadcastAction filter(Object arg0, Object message) {
		return new BroadcastAction(ACTION.CONTINUE, message);
	}

	@Override
	public BroadcastAction filter(AtmosphereResource resource, Object originalMessage, Object message) {
		final  HttpServletRequest request = resource.getRequest();
		ResponseTypeHelper responseTypeHelper = new ResponseTypeHelper();
		String responseType = responseTypeHelper.getResponseType(request);
		try {	
			Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ?
	    			new JSONWithPadding(message, responseTypeHelper.getQueryParam(request, "callback")) : message;  			
	    	return new BroadcastAction(ACTION.CONTINUE, Response.ok(responseObject, responseType).build());
			
		
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BroadcastAction(ACTION.ABORT,  message);
		} 
		
		
	}
}
