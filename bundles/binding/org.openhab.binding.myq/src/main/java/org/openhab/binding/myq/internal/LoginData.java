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
 * This Class parses the JSON data and throws if the login request was not
 * successful.
 * <ul>
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
	 *            The Json node from the myq website.
	 */
	@SuppressWarnings("unchecked")
	public LoginData(JsonNode root) throws IOException, InvalidDataException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> treeData = mapper.readValue(root, Map.class);
		Object data = treeData.get("SecurityToken");
		if (data == null)
			throw new InvalidDataException(
					"Could not find SecurityToken in JSON data");
		securityToken = data.toString();
		logger.debug("myq securityToken: {}", securityToken);
	}

	/**
	 * @return Login SecurityToken
	 */
	public String getSecurityToken() {
		return securityToken;
	}
}
