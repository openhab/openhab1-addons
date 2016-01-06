/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.junit.Before;

import static junit.framework.Assert.*;

/**
 * @author Dominic Lerbs
 * @since 1.8.0
 */
public class L_MessageTest {

	private static final String rawData = "L:BgVPngkSEAsLhBkJEhkLJQDAAAsLhwwJEhkRJwDKAAYO8ZIJEhAGBU+kCRIQCwxuRPEaGQMmAMcACwxuQwkSGQgnAM8ACwQd5t0SGQ0oAMsA";

	private final Map<String, Device> testDevices = new HashMap<String, Device>();

	private L_Message message;
	private final List<Configuration> configurations = new ArrayList<Configuration>();

	@Before
	public void setUp() {
		message = new L_Message(rawData);
		createTestDevices();
	}
	
	private void createTestDevices(){
		addShutterContact("054f9e");
		addShutterContact("0ef192");
		addShutterContact("054fa4");
		addHeatingThermostat("0b8419");
		addHeatingThermostat("0b870c");
		addHeatingThermostat("0c6e43");
		addHeatingThermostat("041de6");
		addHeatingThermostat("0c6e44").setError(true);
	}

	private ShutterContact addShutterContact(String rfAddress){
		ShutterContact device = new ShutterContact(createConfiguration(DeviceType.ShutterContact, rfAddress));
		testDevices.put(rfAddress, device);
		return device;
	}
	
	private HeatingThermostat addHeatingThermostat(String rfAddress){
		HeatingThermostat device = new HeatingThermostat(createConfiguration(DeviceType.HeatingThermostat, rfAddress));
		testDevices.put(rfAddress, device);
		return device;
	}
	
	private Configuration createConfiguration(DeviceType type, String rfAddress) {
		Configuration configuration = Configuration.create(new DeviceInformation(type, "", rfAddress, "", 1));
		configurations.add(configuration);
		return configuration;
	}

	@Test
	public void isCorrectMessageType() {
		MessageType messageType = ((Message) message).getType();
		assertEquals(MessageType.L, messageType);
	}

	@Test
	public void allDevicesCreatedFromMessage() {
		Collection<? extends Device> devices = message.getDevices(configurations);
		assertEquals("Incorrect number of devices created", testDevices.size(), devices.size());
		for (Device device : devices) {
			assertTrue("Unexpected device created: " + device.getRFAddress(),
					testDevices.containsKey(device.getRFAddress()));
		}
	}

	@Test
	public void isCorrectErrorState() {
		Collection<? extends Device> devices = message.getDevices(configurations);
		for (Device device : devices) {
			Device testDevice = testDevices.get(device.getRFAddress());
			assertEquals("Error set incorrectly in Device", testDevice.isError(), device.isError());
		}
	}
	
}