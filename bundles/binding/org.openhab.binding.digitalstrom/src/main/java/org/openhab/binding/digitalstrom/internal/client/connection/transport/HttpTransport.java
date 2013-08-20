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
package org.openhab.binding.digitalstrom.internal.client.connection.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

//import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Betker
 * @since 1.3.0
 */
public class HttpTransport {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpTransport.class);
	
	private final String uri;
	
	private final int connectTimeout;
	private final int readTimeout;
	
	public HttpTransport(String uri, int connectTimeout, int readTimeout) {
		this.uri = uri;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	public String execute(String request) {
		return execute(request, this.connectTimeout, this.readTimeout );
	}
	
	public String execute(String request, int connectTimeout, int readTimeout) {
		if (request != null && !request.trim().equals("")) {
			
			
/*			String response = HttpUtil.executeUrl("GET", this.uri+request, null, null, null, connectTimeout );
			if (response != null) {
				return response.toString();
			}
			else
				return null;*/
			
			
			HttpURLConnection connection = null;
			
			StringBuilder response = new StringBuilder();
			BufferedReader in = null;
			try {
				URL url = new URL(this.uri+request);
				
				connection = (HttpURLConnection) url.openConnection();
				int responseCode =-1;
				if (connection != null) {
					connection.setConnectTimeout(connectTimeout);
					connection.setReadTimeout(readTimeout);
				
					try {
						connection.connect();
						responseCode = connection.getResponseCode();
					} catch (SocketTimeoutException e) {
						logger.warn(e.getMessage()+" : "+request);
						return null;
					}
					
					if (responseCode == HttpURLConnection.HTTP_OK) {
						String inputLine = null;
						in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						
						in.close();
					}
					else {
						response = null;
					}
				
				}
				if (response != null) {
					return response.toString();
				}

			} catch (MalformedURLException e) {
				logger.error("MalformedURLException by executing jsonRequest: "
						+ request +" ; "+e.getLocalizedMessage());

			} catch (IOException e) {
				logger.error("IOException by executing jsonRequest: "
						+ request +" ; "+e.getLocalizedMessage());							
			}
			finally{
				if (connection != null)
					connection.disconnect();
			}
		}
		return null;
	}
	
}
