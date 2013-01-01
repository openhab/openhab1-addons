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
package org.openhab.io.rest.internal.resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.atmosphere.cpr.HeaderConfig;

public class ResponseTypeHelper {
	
	public String getResponseType(HttpServletRequest request) {
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

	public String getQueryParam(HttpServletRequest request, String paramName) {
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

}
