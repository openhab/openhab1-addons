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
import java.util.HashMap;
import java.util.LinkedList;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class parses the JSON data and creates a HashMap of Garage Door Opener
 * Devices with the TypeName as the Key.
 * <ul>
 * <li>success: JSON request was successful</li>
 * <li>devices: HashMap of Devices</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
public class GarageDoorData {
	static final Logger logger = LoggerFactory.getLogger(GarageDoorData.class);

	LinkedList<GarageDoorDevice> devices = new LinkedList<GarageDoorDevice>();

	/**
	 * Constructor of the GarageDoorData.
	 * 
	 * @param deviceStatusData
	 *            The Json string as it has been returned myq website.
	 * 
	 * @param logData
	 *            Boolean to determine if devicedata should be logged.
	 */
	public GarageDoorData(JsonNode rootNode) throws IOException {
		if (rootNode.has("Devices")) {
			JsonNode node = rootNode.get("Devices");
			if (node.isArray()) {
				logger.trace("Chamberlain MyQ Devices:");
				int arraysize = node.size();
				for (int i = 0; i < arraysize; i++) {
					int deviceId = node.get(i).get("MyQDeviceId").asInt();
					String deviceName = node.get(i).get("SerialNumber")
							.asText();
					String deviceType = node.get(i).get("MyQDeviceTypeName")
							.asText();

					if (deviceType.contains("Garage")
							&& deviceType.contains("Door")
							&& deviceType.contains("Opener")) {
						JsonNode attributes = node.get(i).get("Attributes");
						if (attributes.isArray()) {
							int attributesSize = attributes.size();
							for (int j = 0; j < attributesSize; j++) {
								String attributeName = attributes.get(j)
										.get("AttributeDisplayName").asText();
								if (attributeName.contains("doorstate")) {
									int doorstate = attributes.get(j)
											.get("Value").asInt();
									logger.trace(
											"DeviceID: {} DeviceName: {} DeviceType: {} Doorstate : ",
											deviceId, deviceName, deviceType,
											doorstate);

									devices.add(new GarageDoorDevice(deviceId,
													deviceType, deviceName,
													doorstate));
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public GarageDoorDevice getDevice(int index) {
		return index >= devices.size() ? null : devices.get(index);
	}
}

/**
 * This Class holds the Garage Door Opener Device data.
 * <ul>
 * <li>DeviceId: DeviceId from API, need for http Posts</li>
 * <li>DeviceType: MYQ Device Type. GarageDoorOpener or Gateway I've seen</li>
 * <li>DeviceName: Serial number of device I think</li>
 * <li>Status: Garage Door Opener "doorstate" Attribute</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @author Dan Cunningham
 * @since 1.8.0
 */
class GarageDoorDevice {
	private int deviceId;
	private String deviceType;
	private String deviceName;
	private GarageDoorStatus status;

	public GarageDoorDevice(int deviceId, String deviceType, String deviceName,
			int status) {
		this.deviceId = deviceId;
		this.deviceType = deviceType;
		this.deviceName = deviceName;
		this.status = GarageDoorStatus.GetDoorStatus(status);
	}

	public int getDeviceId() {
		return deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public GarageDoorStatus getStatus() {
		return status;
	}

	public enum GarageDoorStatus {
		OPEN("Open", 1), CLOSED("Closed", 2), PARTIAL("Partially Open/Closed",
				3), OPENING("Opening", 4), CLOSING("Closing", 5), UNKNOWN(
				"Unknown", -1);

		/**
		 * The label used to display status to a user
		 */
		private String label;
		/**
		 * The int value returned from the MyQ API
		 */
		private int value;

		private GarageDoorStatus(String label, int value) {
			this.label = label;
			this.value = value;
		}

		/**
		 * Label for the door status
		 * 
		 * @return human readable label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * Int value of the door status
		 * 
		 * @return int value of the door status
		 */
		public int getValue() {
			return value;
		}

		/**
		 * Is the door in a closed or closing state
		 * 
		 * @return is closed or is closing
		 */
		public boolean isClosedOrClosing() {
			return (this == CLOSED || this == CLOSING);
		}

		/**
		 * Is the door in motion
		 * 
		 * @return door in motion
		 */
		public boolean inMotion() {
			return (this == OPENING || this == CLOSING);
		}

		/**
		 * Lookup a door status by its int value
		 * 
		 * @param value
		 * @return a door status enum
		 */
		public static GarageDoorStatus GetDoorStatus(int value) {
			for (GarageDoorStatus ds : values()) {
				if (ds.getValue() == value)
					return ds;
			}
			return UNKNOWN;
		}
	}

	public String toString() {
		return deviceName;
	}
}