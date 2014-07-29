/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.common;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.util.LocalNetworkInterface;
import org.osgi.service.cm.ConfigurationException;
/**
 * Parses the config in openhab.cfg.
 * <pre>
 * ############################## Homematic Binding ##############################
 * #
 * # Hostname / IP address of the Homematic CCU or Homegear server
 * homematic:host=
 *
 * # The communication with the Homematic server. xml for xmlrpc or bin for the lightweight binrpc, (optional, default is bin).
 * # homematic:rpc=
 * 
 * # Hostname / IP address for the callback server (optional, default is auto-discovery)
 * # This is normally the IP / hostname of the local host (but not "localhost" or "127.0.0.1"). 
 * # homematic:callback.host=
 * 
 * # Port number for the callback server. (optional, default is 9123)
 * # homematic:callback.port=
 * 
 * # The interval in seconds to check if the communication with the Homematic server is still alive.
 * # If no message receives from the Homematic server, the binding restarts. (optional, default is 300)
 * # homematic:alive.interval
 * </pre>
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HomematicConfig {
	private static final String CONFIG_KEY_HOMEMATIC_HOST = "host";
	private static final String CONFIG_KEY_CALLBACK_HOST = "callback.host";
	private static final String CONFIG_KEY_CALLBACK_PORT = "callback.port";
	private static final String CONFIG_KEY_ALIVE_INTERVAL = "alive.interval";
	private static final String CONFIG_KEY_RPC = "rpc";

	private static final Integer DEFAULT_CALLBACK_PORT = 9123;
	private static final int DEFAULT_ALIVE_INTERVAL = 300;

	private boolean valid;
	private String host;
	private String callbackHost;
	private Integer callbackPort;
	private Integer aliveInterval;
	private String rpc;

	/**
	 * Parses and validates the properties in the openhab.cfg.
	 */
	public void parse(Dictionary<String, ?> properties) throws ConfigurationException {
		valid = false;

		host = (String) properties.get(CONFIG_KEY_HOMEMATIC_HOST);
		if (StringUtils.isBlank(host)) {
			throw new ConfigurationException("homematic",
					"Parameter host is mandatory and must be configured. Please check your openhab.cfg!");
		}

		callbackHost = (String) properties.get(CONFIG_KEY_CALLBACK_HOST);
		if (StringUtils.isBlank(callbackHost)) {
			callbackHost = LocalNetworkInterface.getLocalNetworkInterface();
		}

		callbackPort = parseInt(properties, CONFIG_KEY_CALLBACK_PORT, DEFAULT_CALLBACK_PORT);
		aliveInterval = parseInt(properties, CONFIG_KEY_ALIVE_INTERVAL, DEFAULT_ALIVE_INTERVAL);

		rpc = StringUtils.defaultIfBlank((String) properties.get(CONFIG_KEY_RPC), "bin").toLowerCase();
		if (!"bin".equals(rpc) && !"xml".equals(rpc)) {
			throw new ConfigurationException("homematic", "Unknown value for parameter rpc:" + rpc
					+ ", only bin or xml is valid. Please check your openhab.cfg!");
		}

		valid = true;
	}

	/**
	 * Parses a integer property.
	 */
	private Integer parseInt(Dictionary<String, ?> properties, String key, Integer defaultValue)
			throws ConfigurationException {
		String value = (String) properties.get(key);
		if (StringUtils.isNotBlank(value)) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException ex) {
				throw new ConfigurationException("homematic", "Parameter " + key
						+ " in wrong format. Please check your openhab.cfg!");
			}

		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns the Homematic server host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns the callback host.
	 */
	public String getCallbackHost() {
		return callbackHost;
	}

	/**
	 * Returns the callback port.
	 */
	public Integer getCallbackPort() {
		return callbackPort;
	}

	/**
	 * Returns the alive interval.
	 */
	public Integer getAliveInterval() {
		return aliveInterval;
	}

	/**
	 * Returns true if this config is valid.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Returns true if the communication mode is set to BIN-RPC.
	 */
	public boolean isBinRpc() {
		return "bin".equals(rpc);
	}

	/**
	 * Returns the XML-RPC url.
	 */
	public String getXmlRpcCallbackUrl() {
		return "http://" + callbackHost + ":" + callbackPort + "/xmlrpc";
	}

	/**
	 * Returns the BIN-RPC url.
	 */
	public String getBinRpcCallbackUrl() {
		return "binary://" + callbackHost + ":" + callbackPort;
	}

	/**
	 * Returns the TclRegaScript url.
	 */
	public String getTclRegaUrl() {
		return "http://" + host + ":" + HmInterface.TCL.getPort() + "/tclrega.exe";
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("host", host)
				.append("callbackHost", callbackHost).append("callbackPort", callbackPort)
				.append("aliveInterval", aliveInterval).append("rpc", rpc).toString();
	}
}
