/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.sitewhere.internal;

import java.util.Map;

import org.slf4j.Logger;

/**
 * Configures how openHAB items are mapped to SiteWhere devices.
 * 
 * @author Derek
 */
public class SiteWhereConfiguration {

	/** Configuration field for default hardware id */
	public static final String CONFIG_DEFAULT_HARDWARE_ID = "defaultHardwareId";

	/** Configuration field for specification token if creating device */
	public static final String CONFIG_SPECIFICATION_TOKEN = "specificationToken";

	/** Configuration field for MQTT hostname agent will contact */
	public static final String CONFIG_MQTT_HOST = "mqttHost";

	/** Configuration field for MQTT port agent will contact */
	public static final String CONFIG_MQTT_PORT = "mqttPort";

	/** Default hardware id for unmapped items */
	private String defaultHardwareId = "123-OPENHAB-234908324";

	/** Default device specification token */
	private String deviceSpecificationToken = "5a95f3f2-96f0-47f9-b98d-f5c081d01948";

	/** Connection Information */
	private Connection connection;

	public String getDefaultHardwareId() {
		return defaultHardwareId;
	}

	public void setDefaultHardwareId(String defaultHardwareId) {
		this.defaultHardwareId = defaultHardwareId;
	}

	public String getDeviceSpecificationToken() {
		return deviceSpecificationToken;
	}

	public void setDeviceSpecificationToken(String deviceSpecificationToken) {
		this.deviceSpecificationToken = deviceSpecificationToken;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public SiteWhereConfiguration(Map<String, Object> config, Logger logger) {

		// Load hardware id if configured.
		String hwid = (String) config
				.get(SiteWhereConfiguration.CONFIG_DEFAULT_HARDWARE_ID);
		if (hwid != null) {
			setDefaultHardwareId(hwid);
		}

		// Load specification token for new registration if configured.
		String spec = (String) config
				.get(SiteWhereConfiguration.CONFIG_SPECIFICATION_TOKEN);
		if (spec != null) {
			setDeviceSpecificationToken(spec);
		}

		// Load connection parameters for MQTT broker.
		Connection conn = new Connection();
		String host = (String) config
				.get(SiteWhereConfiguration.CONFIG_MQTT_HOST);
		if (host != null) {
			conn.setMqttHost(host);
		}
		String port = (String) config
				.get(SiteWhereConfiguration.CONFIG_MQTT_PORT);
		if (port != null) {
			try {
				conn.setMqttPort(Integer.parseInt(port));
			} catch (NumberFormatException e) {
				logger.warn("SiteWhere MQTT port was non-numeric: " + port);
			}
		}
		setConnection(conn);
	}

	/**
	 * Connection parameters for MQTT connectivity.
	 * 
	 * @author Derek
	 */
	public static class Connection {

		/** Hostname for MQTT server */
		private String mqttHost = "localhost";

		/** Port for MQTT server */
		private int mqttPort = 1883;

		public String getMqttHost() {
			return mqttHost;
		}

		public void setMqttHost(String mqttHost) {
			this.mqttHost = mqttHost;
		}

		public int getMqttPort() {
			return mqttPort;
		}

		public void setMqttPort(int mqttPort) {
			this.mqttPort = mqttPort;
		}
	}
}