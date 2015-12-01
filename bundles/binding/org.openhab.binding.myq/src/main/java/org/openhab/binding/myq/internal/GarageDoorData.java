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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
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

	// JSON returnCode was 0
	private boolean success = false;

	HashMap<Integer, Device> devices = new HashMap<Integer, Device>();

	/**
	 * Constructor of the GarageDoorData.
	 * 
	 * @param deviceStatusData
	 *            The Json string as it has been returned myq website.
	 * 
	 * @param logData
	 *            Boolean to determine if devicedata should be logged.
	 */
	public GarageDoorData(String deviceStatusData, boolean logData) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(deviceStatusData);
			int ReturnCode = rootNode.get("ReturnCode").asInt();
			logger.debug("myq ReturnCode: " + Integer.toString(ReturnCode));

			if (ReturnCode == 0) {
				this.success = true;
				if (rootNode.has("Devices")) {
					JsonNode node = rootNode.get("Devices");
					if (node.isArray()) {
						if (logData) {
							logger.info("Chamberlain MyQ Devices:");
						}

						int arraysize = node.size();
						for (int i = 0; i < arraysize; i++) {
							int deviceId = node.get(i).get("MyQDeviceId").asInt();
							String deviceName = node.get(i).get("SerialNumber")
									.asText();
							String deviceType = node.get(i)
									.get("MyQDeviceTypeName").asText();

							if (deviceType.contains("Garage")
									&& deviceType.contains("Door")
									&& deviceType.contains("Opener")) {
								JsonNode attributes = node.get(i).get(
										"Attributes");
								if (attributes.isArray()) {
									int attributesSize = attributes.size();
									for (int j = 0; j < attributesSize; j++) {
										String attributeName = attributes
												.get(j).get("AttributeDisplayName").asText();
										if (attributeName.contains("doorstate")) {
											int doorstate = attributes.get(j)
													.get("Value").asInt();
											if (logData) {
												logger.info("DeviceID: "
														+ Integer
																.toString(deviceId)
														+ " DeviceName: "
														+ deviceName
														+ " DeviceType: "
														+ deviceType
														+ " Doorstate : "
														+ Integer
																.toString(doorstate));
											}
											this.devices.put(deviceId,
													new Device(deviceId,
															deviceType,
															deviceName,
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
		} catch (IOException e) {
			logger.error("Could not read GarageDoor JSON from MyQ Site.", e);
		}
	}

	public boolean getSuccess() {
		return this.success;
	}

	public HashMap<Integer, Device> getDevices() {
		return this.devices;
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
 * @since 1.8.0
 */
class Device {
	private int DeviceId;
	private String DeviceType;
	private String DeviceName;
	private int Status;

	public Device(int deviceId, String deviceType, String deviceName, int status) {
		this.DeviceId = deviceId;
		this.DeviceType = deviceType;
		this.DeviceName = deviceName;
		this.Status = status;
	}

	public int getDeviceId() {
		return this.DeviceId;
	}

	public String getDeviceType() {
		return this.DeviceType;
	}

	public String getDeviceName() {
		return this.DeviceName;
	}

	public int getStatus() {
		return this.Status;
	}

	public boolean IsDoorClosed() {
		if (this.Status == 2) {
			return true;
		}
		return false;
	}

	public String GetStrStatus() {
		switch (this.Status) {
		case 1:
			return "Open";
		case 2:
			return "Closed";
		case 3:
			return "Partially Open/Closed";
		case 4:
			return "Opening";
		case 5:
			return "Closing";
		}
		return "Unknown";
	}

	public String toString() {
		return this.DeviceName;
	}
}