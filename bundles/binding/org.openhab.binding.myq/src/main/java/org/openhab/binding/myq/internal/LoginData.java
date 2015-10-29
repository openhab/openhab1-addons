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


public class LoginData {

	static final Logger logger = LoggerFactory.getLogger(LoginData.class);
	
	String securityToken;
	boolean success = false;

	/**
	 * Constructor of the LoginData.
	 * 
	 * @param loginData
	 *            The Json string as it has been returned myq website.
	 */
	public LoginData(String loginData) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(loginData);
			
			Map<String, Object> treeData = mapper.readValue(rootNode, Map.class);
			
			String test = treeData.get("ReturnCode").toString();
			logger.debug("myq ReturnCode: " + test);
			
			if(Integer.parseInt(treeData.get("ReturnCode").toString())==0)
			{
				this.success = true;
				this.securityToken = treeData.get("SecurityToken").toString();
				logger.debug("myq securityToken: " + this.securityToken);
			}
			
		} catch (IOException e) {
			logger.error("Could not read Settings-Json from myq site.", e);
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