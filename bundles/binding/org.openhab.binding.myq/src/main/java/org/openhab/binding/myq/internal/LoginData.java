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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class parses the JSON data and stores if the login request was
 * successful.
 * <ul>
 * <li>success: json request was successful</li>
 * <li>securityToken: securityToken from login request</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
public class LoginData {
	static final Logger logger = LoggerFactory.getLogger(LoginData.class);

	String securityToken;

	/**
	 * Constructor of the LoginData.
	 * 
	 * @param loginData
	 *            The Json string as it has been returned myq website.
	 */
	@SuppressWarnings("unchecked")
	public LoginData(String loginData) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode rootNode = mapper.readTree(loginData);
		Map<String, Object> treeData = mapper
				.readValue(rootNode, Map.class);
		String test = treeData.get("ReturnCode").toString();
		logger.debug("myq ReturnCode: {}", test);

		if (Integer.parseInt(treeData.get("ReturnCode").toString()) == 0) {
			securityToken = treeData.get("SecurityToken").toString();
			logger.debug("myq securityToken: {}", securityToken);
		} else {
			throw new IOException("Loging failed" + treeData.get("ReturnCode"));
		}
	}

	/**
	 * @return Login SecurityToken
	 */
	public String getSecurityToken() {
		return securityToken;
	}
}