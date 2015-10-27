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
	private String appKey;
	
	private Client client;
	
	public myqData(String username, String password) {
		this.userName = username;
		this.password = password;
		client = Client.create();
		client.setConnectTimeout(5000);
	}
	
	private String getMyqJson(String url) {
		WebResource webResource = client.resource(url);

		try {
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			String settingsString = response.getEntity(String.class);

			if (response.getStatus() != 200) {
				logger.warn("Failed to connect to Hue bridge: HTTP error code: "
						+ response.getStatus());
				return null;
			}
			logger.trace("Received Hue Bridge Settings: {}", settingsString);
			return settingsString;
		} catch(ClientHandlerException e) {
			logger.warn("Failed to connect to Hue bridge: HTTP request timed out.");
			return null;
		}
	}

}
