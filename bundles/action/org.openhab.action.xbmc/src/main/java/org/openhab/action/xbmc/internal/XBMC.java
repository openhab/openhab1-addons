/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xbmc.internal;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.net.http.HttpUtil;

/** 
 * This class provides static methods that can be used in automation rules
 * for sending XBMC notifications
 * 
 * @author Ben Jones
 * @author Panos Kastanidis
 * @since 1.3.0
 */
public class XBMC {

	/** Constant which represents the content type <code>application/json</code> */
	public final static String CONTENT_TYPE_JSON = "application/json";
	
	/** 
	* Send an XBMC notification via POST-HTTP. Errors will be logged, returned values just ignored. 
	*
	* @param host the XBMC client to be notified
	* @param port the XBMC web server port
	* @param title the notification title 
	* @param message the notification text 
	*/ 
	@ActionDoc(text="Send an XBMC notification via POST-HTTP. Errors will be logged, returned values just ignored. ")
	static public void sendXbmcNotification(
			@ParamDoc(name="host") String host, 
			@ParamDoc(name="port") int port, 
			@ParamDoc(name="title") String title, 
			@ParamDoc(name="message") String message) { 
		sendXbmcNotification(host, port, title, message, null, -1);
	} 
	
	/** 
	* Send an XBMC notification via POST-HTTP. Errors will be logged, returned values just ignored.
	* Additional implementation to be able to show also images and to define a display time 
	*
	* @param host the XBMC client to be notified
	* @param port the XBMC web server port
	* @param title the notification title 
	* @param message the notification text 
	* @param image A URL pointing to an image (only used if not null)
	* @param displayTime A display time for the message in milliseconds (between 1500 and 2147483647 (inclusive)) 
	*/
	@ActionDoc(text="Send an XBMC notification via POST-HTTP. Errors will be logged, returned values just ignored. ")
	static public void sendXbmcNotification(@ParamDoc(name="host") String host,@ParamDoc(name="port") int port,@ParamDoc(name="title") String title,@ParamDoc(name="message") String message,@ParamDoc(name="image") String image,@ParamDoc(name="displayTime") long displayTime) { 
		String url = "http://" + host + ":" + port + "/jsonrpc";
		
		StringBuilder content = new StringBuilder();
		content.append("{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"GUI.ShowNotification\",\"params\":{\"title\":\"" + title + "\",\"message\":\"" + message + "\"");
		if (StringUtils.isNotEmpty(image))
			content.append(",\"image\":\"" + image + "\"");
		if (displayTime >= 1500 && displayTime <= 2147483647)
			content.append(",\"displaytime\":" + displayTime);
		content.append("}}");
        
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000); 
	}
}
