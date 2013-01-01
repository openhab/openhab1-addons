/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.ihc.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Dictionary;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ihc.utcs.IhcClient;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class establishes the connection to the IHC / ELKO LS controller. It
 * uses the ConfigAdmin service to retrieve the relevant configuration data.
 * 
 * @author Pauli Anttila
 * @since 1.1.0
 */
public class IhcConnection implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(IhcConnection.class);

	private static boolean isProperlyConfigured = false;

	private static IhcClient ihc;

	/** IP address of IHC / ELKO LS Controller */
	private static String ip = null;

	/** User name for controller authentication */
	private static String username = null;

	/** Password for controller authentication */
	private static String password = null;

	/**
	 * Path for IHC / ELKO lS project file, if it's empty/null project is
	 * download from controller
	 */
	private static String projectFile = null;

	/** Timeout for controller communication */
	private static int timeout = 5000;

	/** Holds time when controller is opened */
	private static long lastOpenTime = 0;

	/** Flag to indicate if connection to controller is successfully opened */
	private static boolean ready = false;

	/**
	 * Returns time when connection to controller is opened.
	 * 
	 * @return time stamp in seconds.
	 */

	public static synchronized long getLastOpenTime() {
		return lastOpenTime;
	}

	/**
	 * Set time when connection to controller is opened.
	 * 
	 * @param newTime
	 *            time stamp in seconds.
	 */
	private static synchronized void setLastOpenTime(long newTime) {
		lastOpenTime = newTime;
	}

	/**
	 * Returns the IHC / ELKO LS client for talking to the controller. The link
	 * can be null, if it has not (yet) been established successfully.
	 * 
	 * @return instance to current IHC client.
	 */
	public static synchronized IhcClient getCommunicator() {
		if (ready)
			return ihc;
		else
			return null;
	}

	/**
	 * Reopen connection to IHC / ELKO LS controller.
	 * 
	 */
	public static synchronized void reconnect()
			throws UnsupportedEncodingException, XPathExpressionException,
			IOException {
		ihc.openConnection();
		setLastOpenTime(System.currentTimeMillis());
	}

	/**
	 * Initialize IHC client and open connection to IHC / ELKO LS controller.
	 * 
	 */
	public static synchronized void connect()
			throws UnsupportedEncodingException, XPathExpressionException,
			IOException {
		if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(username)
				&& StringUtils.isNotBlank(password)) {
			ready = false;

			logger.info(
					"Connecting to IHC / ELKO LS controller [IP='{}' Username='{}' Password='{}'].",
					new Object[] { ip, username, password });

			ihc = new IhcClient();

			ihc.setIp(ip);
			ihc.setUsername(username);
			ihc.setPassword(password);
			ihc.setTimeoutInMillisecods(timeout);
			ihc.setProjectFile(projectFile);
			ihc.openConnection();
			ihc.loadProject();

			setLastOpenTime(System.currentTimeMillis());
			ready = true;

		} else {
			logger.warn(
					"Couldn't connect to IHC controller because of missing connection parameters [IP='{}' Username='{}' Password='{}'].",
					new Object[] { ip, username, password });
		}
	}

	/**
	 * @{inheritDoc
	 */
	public static synchronized boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		logger.debug("Configuration updated, config {}", config != null ? true
				: false);

		if (config != null) {
			ip = (String) config.get("ip");
			username = (String) config.get("username");
			password = (String) config.get("password");
			timeout = Integer.parseInt((String) config.get("timeout"));
			projectFile = (String) config.get("projectFile");

			// there is a valid IHC-configuration, so connect to the IHC
			// controller
			try {
				connect();
				isProperlyConfigured = true;
			} catch (Exception e) {
				logger.error("Connection to IHC / ELKO LS controller failed.",
						e);
				ihc = null;
			}

		}

	}

}
