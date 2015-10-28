package org.openhab.binding.myq.internal;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginData {

	static final Logger logger = LoggerFactory.getLogger(LoginData.class);
	
	String securityToken;
	boolean success = false;

	/**
	 * Constructor of the LoginData.
	 * 
	 * @param treeData
	 *            The Json data as it has been returned by the Json object
	 *            mapper.
	 */
	public LoginData(String loginData) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(loginData);
			
			Map<String, Object> treeData = mapper.readValue(rootNode, Map.class);
			
			String test = treeData.get("ReturnCode").toString();
			logger.warn("myq treeData: "+test);
			
			if(Integer.parseInt(treeData.get("ReturnCode").toString())==0)
			{
				this.success = true;
				this.securityToken = treeData.get("SecurityToken").toString();
				
			}
			
		} catch (IOException e) {
			logger.error("Could not read Settings-Json from Hue Bridge.", e);
		}
	}
	
	/**
	 * @return if it works return true
	 */
	public boolean getSuccess() {
		return this.success;
	}

	/**
	 * @return Login SecurityToken
	 */
	public String getSecurityToken(){
		return this.securityToken;
	}
	
}
