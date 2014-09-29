/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.resources;

import java.util.List;

import javax.ws.rs.core.MediaType;

/**
 * This class contains static helper methods for dealing with MediaTypes.
 * 
 * @author Tobias Bräutigam
 * @since 1.4.0
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
	 * @param typeParam either "json", "jsonp" or null
	 * 
	 * @return the media type to use for the response
	 */
	static public String getResponseMediaType(List<MediaType> acceptedTypes) {
    	
		// then check the accepted types
    	for(MediaType type : acceptedTypes) {
    		if(type.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
    			return MediaType.APPLICATION_JSON;
    		} else if(type.toString().equals(APPLICATION_X_JAVASCRIPT)) {
    			return APPLICATION_X_JAVASCRIPT;
    		}
    	}
    	
    	// nothing found
    	return MediaType.APPLICATION_JSON;
	}
}
