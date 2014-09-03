/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import org.openhab.binding.maxcube.internal.Utils;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for devices provided by the MAX!Cube protocol.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public abstract class Device {

	private final static Logger logger = LoggerFactory.getLogger(Device.class);

	private String serialNumber = "";
	private String rfAddress = "";
	private int roomId = -1;

	private boolean batteryLow;

	private boolean initialized;
	private boolean answer;
	private boolean error;
	private boolean valid;
	private boolean DstSettingsActive;
	private boolean gatewayKnown;
	private boolean panelLocked;
	private boolean linkStatusError;

	public Device(Configuration c) {
		this.serialNumber = c.getSerialNumber();
		this.rfAddress = c.getRFAddress();
		this.roomId = c.getRoomId();
	}

	public abstract DeviceType getType();

	public abstract String getName();

	public abstract Calendar getLastUpdate();

	private static Device create(String rfAddress, List<Configuration> configurations) {
		Device returnValue = null;
		for (Configuration c : configurations) {
			if (c.getRFAddress().toUpperCase().equals(rfAddress.toUpperCase())) {
				switch (c.getDeviceType()) {
				case HeatingThermostatPlus:
				case HeatingThermostat:
					HeatingThermostat thermostat = new HeatingThermostat(c);
					thermostat.setType(c.getDeviceType());
					return thermostat;
				case EcoSwitch:
					return new EcoSwitch(c);
				case ShutterContact:
					return new ShutterContact(c);
				case WallMountedThermostat:
					return new WallMountedThermostat(c);
				default:
					return new UnsupportedDevice(c);
				}
			}
		}
		return returnValue;
	}

	public static Device create(byte[] raw, List<Configuration> configurations) {

		if (raw.length == 0) {
			return null;
		}

		String rfAddress = Utils.toHex(raw[0] & 0xFF, raw[1] & 0xFF, raw[2] & 0xFF);

		// Based on the RF address and the corresponding configuration,
		// create the device based on the type specified in it's configuration

		Device device = Device.create(rfAddress, configurations);
		if (device == null) {
			logger.warn("Can't create device from received message, returning NULL.");
		}

		// byte 4 is skipped

		// multiple device information are encoded in those particular bytes
		boolean[] bits1 = Utils.getBits(Utils.fromByte(raw[4]));
		boolean[] bits2 = Utils.getBits(Utils.fromByte(raw[5]));

		device.setInitialized(bits1[1]);
		device.setAnswer(bits1[2]);
		device.setError(bits1[3]);
		device.setValid(bits1[4]);

		device.setDstSettingActive(bits2[3]);
		device.setGatewayKnown(bits2[4]);
		device.setPanelLocked(bits2[5]);
		device.setLinkStatusError(bits2[6]);
		device.setBatteryLow(bits2[7]);

		logger.trace ("Device {} L Message length: {} content: {}", rfAddress,raw.length,Utils.getHex(raw));

		// TODO move the device specific readings into the sub classes
		switch (device.getType()) {
		case WallMountedThermostat:
		case HeatingThermostat:
		case HeatingThermostatPlus:
			HeatingThermostat heatingThermostat = (HeatingThermostat) device;
			// "xxxx xx00 = automatic, xxxx xx01 = manual, xxxx xx10 = vacation, xxxx xx11 = boost":
			if (bits2[1] == false && bits2[0] == false) {
				heatingThermostat.setMode(ThermostatModeType.AUTOMATIC);
			} else if (bits2[1] == false && bits2[0] == true) {
				heatingThermostat.setMode(ThermostatModeType.MANUAL);
			} else if (bits2[1] == true && bits2[0] == false) {
				heatingThermostat.setMode(ThermostatModeType.VACATION);
			} else if (bits2[1] == true && bits2[0] == true) {
				heatingThermostat.setMode(ThermostatModeType.BOOST);
			} else {
				// TODO: handel malformed message
			}

			heatingThermostat.setValvePosition(raw[6] & 0xFF);
			heatingThermostat.setTemperatureSetpoint(raw[7] & 0x7F);

			// 9 2 858B Date until (05-09-2011) (see Encoding/Decoding
			// date/time)
			// B 1 2E Time until (23:00) (see Encoding/Decoding date/time)
			String hexDate = Utils.toHex(raw[8] & 0xFF, raw[9] & 0xFF);
			int dateValue = Utils.fromHex(hexDate);
			int timeValue = raw[10] & 0xFF;
			Date date = Utils.resolveDateTime(dateValue, timeValue);
			heatingThermostat.setDateSetpoint(date);

			int actualTemp = 0;
			if (device.getType() == DeviceType.WallMountedThermostat) {
				actualTemp = (raw[11] & 0xFF) + (raw[7] & 0x80) * 2 ;
				
			} else {
				if ( heatingThermostat.getMode() != ThermostatModeType.VACATION && 
						heatingThermostat.getMode() != ThermostatModeType.BOOST){
					actualTemp = (raw[8] & 0xFF ) * 256  + ( raw[9] & 0xFF );
				} else{
					logger.debug ("No temperature reading in {} mode", heatingThermostat.getMode()) ;
				}
			}
			logger.debug ("Actual Temperature : {}",  (double)actualTemp / 10);
			heatingThermostat.setTemperatureActual((double)actualTemp / 10);
			break;
		case EcoSwitch:
			String eCoSwitchData = Utils.toHex(raw[3] & 0xFF, raw[4] & 0xFF, raw[5] & 0xFF);
			logger.trace ("EcoSwitch Device {} status bytes : {}", rfAddress, eCoSwitchData);
		case ShutterContact:
			ShutterContact shutterContact = (ShutterContact) device;
			// xxxx xx10 = shutter open, xxxx xx00 = shutter closed
			if (bits2[1] == true && bits2[0] == false) {
				shutterContact.setShutterState(OpenClosedType.OPEN);
				logger.trace ("Device {} status: Open", rfAddress);
			} else if (bits2[1] == false && bits2[0] == false) {
				shutterContact.setShutterState(OpenClosedType.CLOSED);
				logger.trace ("Device {} status: Closed", rfAddress);
			} else {
				logger.trace ("Device {} status switch status Unknown (true-true)", rfAddress);
			}

			break;
		default:
			logger.debug("Unhandled Device. DataBytes: " + Utils.getHex(raw));
			break;

		}
		return device;
	}

	private final void setBatteryLow(boolean batteryLow) {
		this.batteryLow = batteryLow;
	}

	public final StringType getBatteryLow() {
		return new StringType(this.batteryLow ? "low" : "ok");
	}

	public final String getRFAddress() {
		return this.rfAddress;
	}

	public final void setRFAddress(String rfAddress) {
		this.rfAddress = rfAddress;
	}

	public final int getRoomId() {
		return roomId;
	}

	public final void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	private void setLinkStatusError(boolean linkStatusError) {
		this.linkStatusError = linkStatusError;
	}

	private void setPanelLocked(boolean panelLocked) {
		this.panelLocked = panelLocked;
	}

	private void setGatewayKnown(boolean gatewayKnown) {
		this.gatewayKnown = gatewayKnown;
	}

	private void setDstSettingActive(boolean dstSettingsActive) {
		this.DstSettingsActive = dstSettingsActive;
	}

	private void setValid(boolean valid) {
		this.valid = valid;
	}

	private void setError(boolean error) {
		this.error = error;

	}

	public String getSerialNumber() {
		return serialNumber;
	}

	private void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	private void setAnswer(boolean answer) {
		this.answer = answer;
	}
}