/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sallegra.internal;

import org.openhab.binding.sallegra.internal.xml.XmlUtils;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Node structure to kindly represent a Sallegra Module
 * 
 * @author Benjamin Marty (Developed on behalf of Satelco.ch)
 * @since 1.8.0
 * 
 */
public class SallegraNode {

	private static final Logger logger = LoggerFactory.getLogger(SallegraNode.class);

	private static final String GET = "GET";

	/*
	 * Suffix for specific recurring Parts in Sallegra Modules
	 */
	private static final String SUFFIX_PASSWORD = "pw=";
	private static final String XML_PATH = "current_state.xml";

	/*
	 * Suffix for specific recurring Parts in URL gen
	 */
	private static final String SUFFIX_DIMMER = "Dimmer";
	private static final String SUFFIX_RELAY = "Relay";

	private String hostName;
	private String password;
	private int timeOut = 10000;

	/*
	 * Getter
	 */
	public String getHostName() {
		return hostName;
	}

	public String getPassword() {
		return password;
	}

	protected void setHostName(String hostName) {
		this.hostName = hostName;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Properly configured if hostname & password is set
	 */
	public boolean properlyConfigured() {
		return (hostName != null && password != null) || timeOut != 0;
	}

	/**
	 * Sets the State of the Relay on the Relay Module
	 */
	public void setRelay(Command command, String number) {
		// Check if Instance is Digital OnOff Value
		if (command instanceof OnOffType) {
			if (((OnOffType) command) == OnOffType.ON) {
				String URL = createURL() + "/" + XML_PATH + "?" + SUFFIX_PASSWORD + getPassword() + "&" + SUFFIX_RELAY
						+ number + "=" + "1";
				HttpUtil.executeUrl(GET, URL, this.timeOut);
			} else {
				String URL = createURL() + "/" + XML_PATH + "?" + SUFFIX_PASSWORD + getPassword() + "&" + SUFFIX_RELAY
						+ number + "=" + "0";
				HttpUtil.executeUrl(GET, URL, this.timeOut);
			}
		} else {
			logger.error("Unsupported command type");
		}
	}

	/**
	 * Get the State of the Relay on the Relay Module
	 */
	public String getRelay(String number) {
		String URL = createURL() + "/" + XML_PATH + "?" + SUFFIX_PASSWORD + getPassword();
		String content = HttpUtil.executeUrl(GET, URL, this.timeOut);

		content = XmlUtils.getContentOfElement(content, SUFFIX_RELAY + number);

		// Get Java happy
		int status = Integer.parseInt(XmlUtils.getContentOfElement(content, "State"));

		if (status == 1) {
			return "ON";
		} else {
			return "OFF";
		}
	}

	/**
	 * Sets the State of the Dimmer on the Dimmer Module
	 */
	public void setDimmer(Command command, String number) {
		// Check if Instance is a Decimal Value
		if (command instanceof DecimalType) {
			int value = (int) ((double) ((DecimalType) command).intValue() * 2.54);
			String URL = createURL() + "/" + XML_PATH + "?" + SUFFIX_PASSWORD + getPassword() + "&" + SUFFIX_DIMMER
					+ number + "=" + value;
			HttpUtil.executeUrl(GET, URL, this.timeOut);

		} else {
			logger.error("Unsupported command type");
		}
	}

	/**
	 * 
	 * Gets the State of the Dimmer on the Dimmer Module
	 */
	public String getDimmer(String number) {
		String URL = createURL() + "/" + XML_PATH + "?" + SUFFIX_PASSWORD + getPassword();
		String content = HttpUtil.executeUrl(GET, URL, this.timeOut);

		content = XmlUtils.getContentOfElement(content, SUFFIX_DIMMER + number);

		double dimmer_value = Double.parseDouble(XmlUtils.getContentOfElement(content, "Value"));
		double calc = Math.round(dimmer_value / 2.54);

		String string_value = String.valueOf((int) calc);

		return string_value;
	}

	/**
	 * Gets the State of the Input on the Input Module
	 */
	public String getInput(String input) {
		String URL = createURL() + "/" + XML_PATH + "?" + SUFFIX_PASSWORD + getPassword();
		String content = HttpUtil.executeUrl(GET, URL, this.timeOut);

		content = XmlUtils.getContentOfElement(content, input);

		// Get Java happy
		int status = Integer.parseInt(XmlUtils.getContentOfElement(content, "Value"));

		if (status == 1) {
			return "OPEN";
		} else {
			return "CLOSED";
		}
	}

	/**
	 * Put "http://" before the hostname/ip
	 */
	private String createURL() {
		return "http://" + getHostName();
	}
}
