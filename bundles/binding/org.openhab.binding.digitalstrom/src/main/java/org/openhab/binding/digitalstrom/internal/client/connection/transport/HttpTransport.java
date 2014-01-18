/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.connection.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Betker
 * @author Alex Maier
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