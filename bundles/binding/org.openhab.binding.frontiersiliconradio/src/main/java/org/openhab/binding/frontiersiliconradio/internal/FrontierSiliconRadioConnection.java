/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.frontiersiliconradio.internal;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * This class holds the http-connection and 
 * session information for controlling the radio.
 * 
 * @author Rainer Ostendorf
 *
 */
public class FrontierSiliconRadioConnection {
	
	private static final Logger logger = LoggerFactory.getLogger(FrontierSiliconRadioConnection.class);
	
	// hostname of the radio
	private String hostname;
	
	// portnumber, usually 80
	private int port;
	
	// access pin, passed upon login as GET parameter
	String pin;
	
	// the session ID we get from the radio after logging in
	String sessionId;
	
	// the timeout for HTTP requests
	private final static int SOCKET_TIMEOUT = 5000; //ms
	
	// http clients, store cookies, so it is kept in connection class
	HttpClient httpClient = null;
	
	// flag indicating if we are successfully logged in
	private Boolean isLoggedIn = false;
	
	public FrontierSiliconRadioConnection( String hostname, int port, String pin ) {
		this.hostname = hostname;
		this.port = port;
		this.pin = pin;
	}
	
	/**
	 *  Perform login/establish a new session. Uses the PIN number and when
	 *  sucessful saves the assigned sessionID for future requests.
	 */
	public void doLogin() {
		
		if( httpClient == null ) {
			httpClient = new HttpClient();
		}
		
		String url = "http://" + hostname + ":" + port + "/fsapi/CREATE_SESSION?pin=" + pin;
		
		logger.debug("Opening URL:" + url);
		
		HttpMethod method = new GetMethod( url );
        method.getParams().setSoTimeout( SOCKET_TIMEOUT );
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false) );
		
		try {
			
			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.warn("Method failed: " + method.getStatusLine());
			}

			String responseBody = IOUtils.toString(method.getResponseBodyAsStream());
			if (!responseBody.isEmpty()) {
				logger.debug("login response: " + responseBody);
			}
			
			try {
				FrontierSiliconRadioApiResult result = new FrontierSiliconRadioApiResult(responseBody);
				if( result.isStatusOk() )
				{
					logger.debug("login successful");
					sessionId = result.getSessionId();
					isLoggedIn = true;
				}
			}
			catch (Exception e) {
				logger.error("Parsing response failed");
			}

		}
		catch (HttpException he) {
			logger.error("Fatal protocol violation: {}", he.toString());
		}
		catch (IOException ioe) {
			logger.error("Fatal transport error: {}", ioe.toString());
		}
		finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * Performs a request to the radio with no further parameters.
	 * 
	 * Typically used for polling state info.
	 * 
	 * @param REST API requestString, e.g. "GET/netRemote.sys.power"
	 * @return request result
	 */
	public FrontierSiliconRadioApiResult doRequest( String requestString) {
		return doRequest(requestString, new String("") );
	}
	

	/**
	 * Performs a request to the radio with addition parameters.
	 * 
	 * Typically used for changing parameters.
	 * 
	 * @param REST API requestString, e.g. "SET/netRemote.sys.power"
	 * @param params, e.g. "value=1"
	 * @return request result
	 */
	public FrontierSiliconRadioApiResult doRequest( String requestString, String params) {
		
		int retries = 3;
		
		while(retries>0)
		{
			retries--;
		
			if( !isLoggedIn )
			{
				doLogin();
				continue;
			}
			
			String url;
			if( params.length() > 0)
				url = "http://" + hostname + ":" + port + "/fsapi/"+ requestString +"?pin=" + pin + "&sid=" + sessionId + "&"  + params;
			else
				url = "http://" + hostname + ":" + port + "/fsapi/"+ requestString +"?pin=" + pin + "&sid=" + sessionId;
			
			
			logger.trace("calling url: '"+url+"'");
			
			HttpMethod method = new GetMethod( url );
	        method.getParams().setSoTimeout( SOCKET_TIMEOUT );
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false) );
			
			try {
				
				int statusCode = httpClient.executeMethod(method);
				if (statusCode != HttpStatus.SC_OK) {
					logger.warn("Method failed: " + method.getStatusLine());
				}
	
				String responseBody = IOUtils.toString(method.getResponseBodyAsStream());
				if (!responseBody.isEmpty()) {
					logger.trace("got result: " + responseBody );
				}
				else {
					logger.debug("got empty result" );
				}
				
				FrontierSiliconRadioApiResult result = new FrontierSiliconRadioApiResult(responseBody);
				if( result.isStatusOk() )
					return result;
				else {
					continue; // try again
				}
			}
			catch (HttpException he) {
				logger.error("Fatal protocol violation: {}", he.toString());
				isLoggedIn = false;
			}
			catch (IOException ioe) {
				logger.error("Fatal transport error: {}", ioe.toString());
			}
			finally {
				method.releaseConnection();
			}			
		}
		isLoggedIn = false; // 3 tries failed. log in again next time, maybe our session went invalid (radio restarted?)
		return null;
	}
}
