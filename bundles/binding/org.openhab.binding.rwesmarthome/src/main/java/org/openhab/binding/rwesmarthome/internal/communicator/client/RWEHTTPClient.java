/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.RWESmarthomeSessionExpiredException;
import org.openhab.binding.rwesmarthome.internal.communicator.util.HttpComponentsHelper;
import org.openhab.binding.rwesmarthome.internal.communicator.util.InputStream2String;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP client implementation.
 * 
 * @author ollie-dev
 *
 */
public class RWEHTTPClient implements RWEClient {

	private static final Logger logger = LoggerFactory.getLogger(RWEHTTPClient.class);
			
	/** The http helper. */
	HttpComponentsHelper httpHelper = new HttpComponentsHelper();
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.rwesmarthome.internal.communicator.client.RWEClient#execute(java.lang.String, java.lang.String)
	 */
	@Override
	public String execute(String hostname, String clientId, String request, String command) throws IOException, RWESmarthomeSessionExpiredException {
		// prepare connection
		HttpClient httpclient = httpHelper.getNewHttpClient();
		HttpPost httpPost = new HttpPost("https://" + hostname + command);
		httpPost.addHeader("ClientId", clientId);
		httpPost.addHeader("Connection", "Keep-Alive");
		HttpResponse response;
		StringEntity se = new StringEntity(request, HTTP.UTF_8);
		se.setContentType("text/xml");
		httpPost.setEntity(se);

		// execute HTTP request
		logger.trace("executing request: "+httpPost.getURI().toString());
		logger.trace("REQ:" + request);
		response = httpclient.execute(httpPost);
		logger.trace("Response: "+response.toString());

		// handle expired session
		if (response.getStatusLine().getStatusCode() == 401) {
			logger.info("401 Unauthorized returned - Session expired!");
			throw new RWESmarthomeSessionExpiredException("401 Unauthorized returned - Session expired!");
		}
		
		// handle return
		HttpEntity entity1 = response.getEntity();
		InputStream in = entity1.getContent();
		return InputStream2String.copyFromInputStream(in, "UTF-8");
	}

}
