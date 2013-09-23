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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openhab.core.items.Item;
import org.openhab.io.rest.RESTApplication;
import org.openhab.io.rest.internal.resources.ItemResource;
import org.openhab.io.rest.internal.resources.ResponseTypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the {@link ResourceStateChangeListener} implementation for item REST requests
 * 
 * @author Kai Kreuzer
 * @author Oliver Mazur
 * @since 0.9.0
 */
public class ItemStateChangeListener extends ResourceStateChangeListener {

	static final Logger logger = LoggerFactory.getLogger(ItemStateChangeListener.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getResponseObject(HttpServletRequest request) {	
		String pathInfo = request.getPathInfo();

		if(pathInfo.endsWith("/state")) {
			// we need to return the plain value
			if (pathInfo.startsWith("/" + ItemResource.PATH_ITEMS)) {
	        	String[] pathSegments = pathInfo.substring(1).split("/");
	            if(pathSegments.length>=2) {
	            	String itemName = pathSegments[1];
					Item item = ItemResource.getItem(itemName);
					if(item!=null) {
						return item.getState().toString();
					}
	            }
			}
		} else {		
			// we want the full item data (as xml or json(p))
			String responseType = (new ResponseTypeHelper()).getResponseType(request);
			if(responseType!=null) {
				String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+(request.getContextPath().equals("null")?"":request.getContextPath())+ RESTApplication.REST_SERVLET_ALIAS +"/";
				if (pathInfo.startsWith("/" + ItemResource.PATH_ITEMS)) {
		        	String[] pathSegments = pathInfo.substring(1).split("/");
		            if(pathSegments.length>=2) {
		            	String itemName = pathSegments[1];
						Item item = ItemResource.getItem(itemName);
						if(item!=null) {
			            	Object itemBean = ItemResource.createItemBean(item, true, basePath);	    	
			            	return itemBean;
						}
		            }
		        }
			}
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getSingleResponseObject(Item item, HttpServletRequest request) {
		return getResponseObject(request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<String> getRelevantItemNames(String pathInfo) {       
        // check, if it is a request for items 
        if (pathInfo.startsWith("/" + ItemResource.PATH_ITEMS)) {
        	String[] pathSegments = pathInfo.substring(1).split("/");

            if(pathSegments.length>=2) {
            	return Collections.singleton(pathSegments[1]);
            }
        }
        return new HashSet<String>();
	}




}
