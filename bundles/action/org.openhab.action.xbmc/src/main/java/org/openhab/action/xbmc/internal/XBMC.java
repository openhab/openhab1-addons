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
package org.openhab.action.xbmc.internal;

import org.apache.commons.io.IOUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.net.http.HttpUtil;

/** 
 * This class provides static methods that can be used in automation rules
 * for sending XBMC notifications
 * 
 * @author Ben Jones
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
<<<<<<< HEAD
		String url = "http://" + host + ":" + port + "/jsonrpc";
		String content = "{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"GUI.ShowNotification\",\"params\":{\"title\":\"" + title + "\",\"message\":\"" + message + "\"}}";
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content), CONTENT_TYPE_JSON, 1000); 
	} 
	
=======
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
>>>>>>> upstream/master
}
