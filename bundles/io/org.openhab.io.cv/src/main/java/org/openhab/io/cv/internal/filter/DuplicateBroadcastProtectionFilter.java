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
package org.openhab.io.cv.internal.filter;


import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.io.cv.internal.listeners.ResourceStateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Filter prevents duplicate broadcasts   
 *  
 * @author Oliver Mazur
 * @since 1.0
 *
 *
 */
public class DuplicateBroadcastProtectionFilter implements PerRequestBroadcastFilter {

	private static final Logger logger = LoggerFactory.getLogger(DuplicateBroadcastProtectionFilter.class);
	
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
		ObjectMapper mapper = new ObjectMapper();
		try{
			String firedResponse =  mapper.writeValueAsString(ResourceStateChangeListener.getMap().put(clientId, responseEntity)); 
			String responseValue =  mapper.writeValueAsString(responseEntity);
            if(responseValue.equals(firedResponse)) {
            	return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
        return false;
	}

}
