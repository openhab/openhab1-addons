/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.actions;

import org.apache.commons.io.IOUtils;
import org.openhab.io.net.http.HttpUtil;

/** 
 * This class provides static methods that can be used in automation rules
 * for sending HTTP requests
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class HTTP {
	
	/** Constant which represents the content type <code>application/json</code> */
	public final static String CONTENT_TYPE_JSON = "application/json";
	

	/** 
	* Send out a GET-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the GET request. 
	* @return the response body or <code>NULL</code> when the request went wrong
	*/ 
	static public String sendHttpGetRequest(String url) { 
		return HttpUtil.executeUrl("GET", url, 5000); 
	} 
	
	/** 
	* Send out a PUT-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the PUT request. 
	* @return the response body or <code>NULL</code> when the request went wrong
	*/ 
	static public String sendHttpPutRequest(String url) { 
		return HttpUtil.executeUrl("PUT", url, 1000); 
	}
	
	/** 
	* Send out a PUT-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the PUT request. 
	* @param contentType the content type of the given <code>content</code>
	* @param content the content to be send to the given <code>url</code> or
	*  <code>null</code> if no content should be send.
	* @return the response body or <code>NULL</code> when the request went wrong
	*/ 
	static public String sendHttpPutRequest(String url, String contentType, String content) { 
		return HttpUtil.executeUrl("PUT", url, IOUtils.toInputStream(content), contentType, 1000); 
	}
	
	/** 
	* Send out a POST-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the POST request. 
	* @return the response body or <code>NULL</code> when the request went wrong
	*/ 
	static public String sendHttpPostRequest(String url) { 
		return HttpUtil.executeUrl("POST", url, 1000); 
	}
	
	/** 
	* Send out a POST-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the POST request.
	* @param contentType the content type of the given <code>content</code>
	* @param content the content to be send to the given <code>url</code> or
	*  <code>null</code> if no content should be send.
	* @return the response body or <code>NULL</code> when the request went wrong
	*/ 
	static public String sendHttpPostRequest(String url, String contentType, String content) { 
		return HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content), contentType, 1000); 
	}
	
	/** 
	* Send out a DELETE-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the DELETE request. 
	* @return the response body or <code>NULL</code> when the request went wrong
	*/ 
	static public String sendHttpDeleteRequest(String url) { 
		return HttpUtil.executeUrl("DELETE", url, 1000); 
	}

}
