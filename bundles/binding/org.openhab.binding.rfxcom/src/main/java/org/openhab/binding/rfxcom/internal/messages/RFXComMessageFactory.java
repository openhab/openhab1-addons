/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;

public class RFXComMessageFactory {
	
	final static String classUrl = "org.openhab.binding.rfxcom.internal.messages.";
	
	@SuppressWarnings("serial")
	private static final Map<PacketType, String> messageClasses = Collections
			.unmodifiableMap(new HashMap<PacketType, String>() {
				{
					put(PacketType.INTERFACE_CONTROL, "RFXComControlMessage");
					put(PacketType.INTERFACE_MESSAGE, "RFXComInterfaceMessage");
					put(PacketType.TRANSMITTER_MESSAGE, "RFXComTransmitterMessage");
					put(PacketType.UNDECODED_RF_MESSAGE, "RFXComUndecodedRFMessage");
					put(PacketType.LIGHTING1, "RFXComLighting1Message");
					put(PacketType.LIGHTING2, "RFXComLighting2Message");
					put(PacketType.LIGHTING3, "RFXComLighting3Message");
					put(PacketType.LIGHTING4, "RFXComLighting4Message");
					put(PacketType.LIGHTING5, "RFXComLighting5Message");
					put(PacketType.LIGHTING6, "RFXComLighting6Message");
					put(PacketType.CHIME,"RFXComChimeMessage");
					put(PacketType.FAN,"RFXComFanMessage");
					put(PacketType.CURTAIN1, "RFXComCurtain1Message");
					put(PacketType.BLINDS1, "RFXComBlinds1Message");
					put(PacketType.SECURITY1, "RFXComSecurity1Message");
					put(PacketType.CAMERA1, "RFXComCamera1Message");
					put(PacketType.REMOTE_CONTROL, "RFXComRemoteControlMessage");
					put(PacketType.THERMOSTAT1, "RFXComThermostat1Message");
					put(PacketType.THERMOSTAT2, "RFXComThermostat2Message");
					put(PacketType.THERMOSTAT3, "RFXComThermostat3Message");
					put(PacketType.BBQ1,"RFXComBBQMessage");
					put(PacketType.TEMPERATURE_RAIN, "RFXComTemperatureRainMessage");
					put(PacketType.TEMPERATURE, "RFXComTemperatureMessage");
					put(PacketType.HUMIDITY, "RFXComHumidityMessage");
					put(PacketType.TEMPERATURE_HUMIDITY, "RFXComTemperatureHumidityMessage");
					put(PacketType.BAROMETRIC, "RFXComBarometricMessage");
					put(PacketType.TEMPERATURE_HUMIDITY_BAROMETRIC, "RFXComTemperatureHumidityBarometricMessage");
					put(PacketType.RAIN, "RFXComRainMessage");
					put(PacketType.WIND, "RFXComWindMessage");
					put(PacketType.UV, "RFXComUVMessage");
					put(PacketType.DATE_TIME, "RFXComDateTimeMessage");
					put(PacketType.CURRENT, "RFXComCurrentMessage");
					put(PacketType.ENERGY, "RFXComEnergyMessage");
					put(PacketType.CURRENT_ENERGY, "RFXComCurrentEnergyMessage");
					put(PacketType.POWER, "RFXComPowerMessage");
					put(PacketType.WEIGHT, "RFXComWeightMessage");
					put(PacketType.GAS, "RFXComGasMessage");
					put(PacketType.WATER, "RFXComWaterMessage");
					put(PacketType.RFXSENSOR, "RFXComRFXSensorMessage");
					put(PacketType.RFXMETER, "RFXComRFXMeterMessage");
					put(PacketType.FS20, "RFXComFS20Message");
					put(PacketType.IO_LINES, "RFXComIOLinesMessage");
				}
			});
				
	/**
	 * Command to reset RFXCOM controller.
	 * 
	 */
	public final static byte[] CMD_RESET = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to get RFXCOM controller status.
	 * 
	 */
	public final static byte[] CMD_STATUS = new byte[] { 0x0D, 0x00, 0x00,
			0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to save RFXCOM controller configuration.
	 * 
	 */
	public final static byte[] CMD_SAVE = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	
	public static RFXComMessageInterface getMessageInterface(PacketType packetType) throws RFXComException {
		
		try {
			String className = messageClasses.get(packetType);
			Class<?> cl = Class.forName(classUrl + className);
			return (RFXComMessageInterface) cl.newInstance();
			
		} catch (ClassNotFoundException e) {
			throw new RFXComException("Message " + packetType + " not implemented", e);
			
		} catch (Exception e) {
			throw new RFXComException(e);
		} 
	}
	
	public static RFXComMessageInterface getMessageInterface(byte[] packet) throws RFXComException {
		
		PacketType packetType = getPacketType(packet[1]);
		
		try {
			String className = messageClasses.get(packetType);
			Class<?> cl = Class.forName(classUrl + className);
			Constructor<?> c = cl.getConstructor(byte[].class);
			return (RFXComMessageInterface) c.newInstance(packet);
		
		} catch (ClassNotFoundException e) {
			throw new RFXComException("Message " + packetType + " not implemented", e);
			
		} catch (Exception e) {
			throw new RFXComException(e);
		} 
	}

	public static PacketType convertPacketType(String packetType)
			throws IllegalArgumentException {

		for (PacketType p : PacketType.values()) {
			if (p.toString().equals(packetType)) {
				return p;
			}
		}

		throw new IllegalArgumentException("Unknown packet type " + packetType);
	}

	private static PacketType getPacketType(byte packetType) {
		for (PacketType p : PacketType.values()) {
			if (p.toByte() == packetType) {
				return p;
			}
		}
		
		return PacketType.UNKNOWN;
	}
}
