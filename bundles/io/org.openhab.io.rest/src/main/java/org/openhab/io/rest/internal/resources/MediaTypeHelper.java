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
package org.openhab.io.rest.internal.resources;

import java.util.List;

import javax.ws.rs.core.MediaType;

/**
 * This class contains static helper methods for dealing with MediaTypes.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class MediaTypeHelper {

	public static final String APPLICATION_X_JAVASCRIPT = "application/x-javascript";

	/**
	 * This is a helper method to determine the response media type depending on a list
	 * of accepted types and an explicitely declared type parameter.
	 * Note that the explicit type parameter takes precedence over the accepted types.
	 * 
	 * @param acceptedTypes accepted media types
	 * @param typeParam either "xml", "json", "jsonp" or null
	 * 
	 * @return the media type to use for the response
	 */
	static public String getResponseMediaType(List<MediaType> acceptedTypes, String typeParam) {

		// first check the explicit type parameter
    	if("xml".equals(typeParam)) {
    		return MediaType.APPLICATION_XML;
    	} else if("json".equals(typeParam)) {
    		return MediaType.APPLICATION_JSON;
    	} else if("jsonp".equals(typeParam)) {
    		return APPLICATION_X_JAVASCRIPT;
    	}
    	
		// then check the accepted types
    	for(MediaType type : acceptedTypes) {
    		if(type.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
    			return MediaType.APPLICATION_XML;
    		} else if(type.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
    			return MediaType.APPLICATION_JSON;
    		} else if(type.toString().equals(APPLICATION_X_JAVASCRIPT)) {
    			return APPLICATION_X_JAVASCRIPT;
    		}
    	}
    	
    	// nothing found
    	return null;
	}
}
