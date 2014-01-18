/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxy;
import org.openhab.binding.insteonhub.internal.hardware.api.serial.InsteonHubSerialProxy;

/**
 * Creates InsteonHubProxy instances
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubProxyFactory {

	public static final String CONFIG_KEY_HOST = "host";
	public static final String CONFIG_KEY_PORT = "port";

	public static Map<String, InsteonHubProxy> createInstances(
			Dictionary<String, ?> config) {
		Map<String, InsteonHubProxy> proxies = new HashMap<String, InsteonHubProxy>();
		// parse all configured receivers
		// ( insteonhub:<hubid>.host=10.0.0.2 )
		Enumeration<String> keys = config.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key.endsWith(CONFIG_KEY_HOST)) {
				// parse host
				String host = (String) config.get(key);
				int separatorIdx = key.indexOf('.');
				String hubId, keyPrefix;
				if (separatorIdx == -1) {
					// no prefix/hubid => one hub => use default hub ID
					hubId = InsteonHubBinding.DEFAULT_HUB_ID;
					keyPrefix = "";
				} else {
					// prefix => use it as the hub ID
					hubId = key.substring(0, separatorIdx);
					keyPrefix = hubId + ".";
				}
				String portStr = (String) config.get(keyPrefix
						+ CONFIG_KEY_PORT);
				int port = StringUtils.isBlank(portStr) ? InsteonHubSerialProxy.DEFAULT_PORT
						: Integer.parseInt(config.get(
								keyPrefix + CONFIG_KEY_PORT).toString());
				// Create proxy, and add it to map
				InsteonHubProxy proxy = new InsteonHubSerialProxy(host, port);
				proxies.put(hubId, proxy);
			}
		}
		return proxies;
	}
}
