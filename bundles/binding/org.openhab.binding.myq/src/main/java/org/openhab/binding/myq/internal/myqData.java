/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * This Class handles the Chamberlain myQ http conection.
 * @method Login() 
 * 
 * <ul>
 * <li>userName: myQ Login Username</li>
 * <li>password: myQ Login Password</li>
 * <li>sercurityTokin: sercurityTokin for API requests</li>
 * <li>webSite: url of myQ API</li>
 * <li>appId: appId for API requests</li>
 * <li>client: http client for API requests</li>
 * <li>MaxRetrys: max login attemps in a row</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */

public class myqData {
	
	static final Logger logger = LoggerFactory.getLogger(myqData.class);
	
	private String userName;
	private String password;
	private String sercurityTokin;
	
	private String webSite = "https://myqexternal.myqdevice.com";
	private String appId = "Vj8pQggXLhLy0WHahglCD4N1nAkkXQtGYpq2HrHD7H1nvmbT55KqtN6RSF4ILB%2fi";
	
	private Client client;
	
	private final int MaxRetrys = 3;
	
	public myqData(String username, String password) {
		this.userName = username;
		this.password = password;
		client = Client.create();
		client.setConnectTimeout(5000);
	}

	/**
	 * Gets Garage Door Opener Data in GarageDoorData object format
	 */
	public GarageDoorData getMyqData() 
	{
		if(sercurityTokin == null)
			Login();
		String json = getGarageStatus(0);
		return json != null ? new GarageDoorData(json) : null;
	}

	/**
	 * retreives JSON string of device data from myq website
	 * returns null if conection fails or user login fails
	 */
	private String getGarageStatus(int attemps) 
	{
		if(sercurityTokin == null)
			if(Login())
				return null;
		String url =  String.format("%s/api/UserDeviceDetails?appId=%s&securityToken=%s",this.webSite,this.appId,sercurityTokin);
		WebResource webResource = client.resource(url);

		try 
		{
			ClientResponse response = webResource.get(ClientResponse.class);			
			
			String dataString = response.getEntity(String.class);

			if (response.getStatus() != 200) 
			{
				logger.error("Failed to connect to MyQ site: HTTP error code: "
						+ response.getStatus());
				if(attemps < MaxRetrys)
				{
					Login();
					return getGarageStatus(++attemps);
				}
				return null;
			}
			logger.trace("Received MyQ Device Data: {}", dataString);
			return dataString;
		} catch(ClientHandlerException e) 
		{			
			logger.error("Failed to connect to MyQ site: HTTP request timed out.");
			return null;
		}
	}


	/**
	 * Validates Username and Password then saved sercurityTokin to a varible
	 * Returns false if return code from API is not correct or conection fails
	 */
	private boolean Login() 
	{
		String url =  String.format("%s/Membership/ValidateUserWithCulture?appId=%s&securityToken=null&username=%s&password=%s&culture=en",
				this.webSite,this.appId,this.userName,this.password);
		WebResource webResource = client.resource(url);

		try 
		{
			ClientResponse response = webResource.get(ClientResponse.class);
			String loginString = response.getEntity(String.class);

			if (response.getStatus() != 200) 
			{
				logger.error("Failed to connect to MyQ site: HTTP error code: "
						+ response.getStatus());
				return false;
			}
			logger.trace("Received MyQ Login JSON: {}", loginString);
			LoginData login = new LoginData(loginString);
			if(login.getSuccess())
			{
				this.sercurityTokin = login.getSecurityToken();
				return true;
			}
			return false;
		} 
		catch(ClientHandlerException e) 
		{
			logger.error("Failed to connect to MyQ site: HTTP request timed out.");
			return false;
		}
	}
}
