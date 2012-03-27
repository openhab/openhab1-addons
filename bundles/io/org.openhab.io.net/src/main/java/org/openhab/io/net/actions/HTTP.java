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
package org.openhab.io.net.actions;

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

	/** 
	* Send out a GET-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the GET request. 
	*/ 
	static public void sendHttpGetRequest(String url) { 
		HttpUtil.executeUrl("GET", url, 5000); 
	} 

	/** 
	* Send out a PUT-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the PUT request. 
	*/ 
	static public void sendHttpPutRequest(String url) { 
		HttpUtil.executeUrl("PUT", url, 1000); 
	}
	
	/** 
	* Send out a POST-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the POST request. 
	*/ 
	static public void sendHttpPostRequest(String url) { 
		HttpUtil.executeUrl("POST", url, 1000); 
	}
	
	/** 
	* Send out a DELETE-HTTP request. Errors will be logged, returned values just ignored. 
	*
	* @param url the URL to be used for the DELETE request. 
	*/ 
	static public void sendHttpDeleteRequest(String url) { 
		HttpUtil.executeUrl("DELETE", url, 1000); 
	}

}
