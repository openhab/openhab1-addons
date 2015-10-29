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
	
	public GarageDoorData getMyqData() 
	{
		if(sercurityTokin == null)
			Login();
		String json = getGarageStatus(0);
		return json != null ? new GarageDoorData(json) : null;
	}
	
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
