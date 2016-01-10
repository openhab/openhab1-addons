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
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class parses the JSON data and creates a LinkedList of Garage Door
 * Openers
 * <ul>
 * <li>devices: LinkedList of Devices</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
public class GarageDoorData {
	static final Logger logger = LoggerFactory.getLogger(GarageDoorData.class);

	/**
	 * Matching pattern for door device types, some models have spaces in them,
	 * some don't
	 *
	 * "VGDO"
	 * "Garage Door Opener WGDO"
	 * "GarageDoorOpener"
	 */
	private static final Pattern DEVICE_TYPE_PATTERN = Pattern
			.compile("Garage\\s?Door\\s?Opener|VGDO");

	LinkedList<GarageDoorDevice> devices = new LinkedList<GarageDoorDevice>();

	/**
	 * Constructor of the GarageDoorData.
	 * 
	 * @param rootNode
	 *            The Json node returned from the myq website.
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

					if (DEVICE_TYPE_PATTERN.matcher(deviceType).matches()) {
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
											"DeviceID: {} DeviceName: {} DeviceType: {} Doorstate : {}",
											deviceId, deviceName, deviceType,
											doorstate);

									devices.add(new GarageDoorDevice(deviceId,
											deviceType, deviceName, doorstate));
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
