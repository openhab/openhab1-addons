/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * See {@link DevicelistModel}.
 * 
 * In the functionbitmask element value the following bits are used:
 * 
 * <ol>
 * <li>Bit 7: Energie Messgerät</li>
 * <li>Bit 8: Temperatursensor</li>
 * <li>Bit 9: Schaltsteckdose</li>
 * <li>Bit 10: AVM DECT Repeater</li>
 * </ol>
 * 
 * @author Robert Bausdorf
 * @since 1.6
 * 
 */
@SuppressWarnings("restriction")
@XmlRootElement(name = "device")
public class DeviceModel {
	public static final int POWERMETER_BIT = 128;
	public static final int TEMPSENSOR_BIT = 256;
	public static final int SWITCH_BIT = 512;
	public static final int DECT_REPEATER_BIT = 1024;

	@XmlAttribute(name = "identifier")
	private String ident;

	@XmlAttribute(name = "id")
	private String deviceId;

	@XmlAttribute(name = "functionbitmask")
	private int bitmask;

	@XmlAttribute(name = "fwversion")
	private String firmwareVersion;

	@XmlAttribute(name = "manufacturer")
	private String deviceManufacturer;

	@XmlAttribute(name = "productname")
	private String productName;

	private PowerMeterModel powermeterModel;

	private TemperatureModel temperatureModel;

	public PowerMeterModel getPowermeter() {
		return powermeterModel;
	}

	public void setPowermeter(PowerMeterModel powermeter) {
		this.powermeterModel = powermeter;
	}

	public TemperatureModel getTemperature() {
		return temperatureModel;
	}

	public void setTemperature(TemperatureModel temperature) {
		this.temperatureModel = temperature;
	}

	public String getIdentifier() {
		return ident != null ? ident.replace(" ", "") : null;
	}

	public void setIdentifier(String identifier) {
		this.ident = identifier;
	}

	public boolean isSwitchableOutlet() {
		return (bitmask & DeviceModel.SWITCH_BIT) > 0;
	}

	public boolean isTempSensor() {
		return (bitmask & DeviceModel.TEMPSENSOR_BIT) > 0;
	}

	public boolean isPowermeter() {
		return (bitmask & DeviceModel.POWERMETER_BIT) > 0;
	}

	public boolean isDectRepeater() {
		return (bitmask & DeviceModel.DECT_REPEATER_BIT) > 0;
	}

	public String toString() {
		StringBuilder out = new StringBuilder("device");
		out.append("(ain=").append(this.getIdentifier());
		out.append(",bitmask=").append(this.bitmask).append('(');
		StringBuilder types = new StringBuilder();
		if (this.isDectRepeater()) {
			types.append("DECT Repeater");
		}
		if (this.isPowermeter()) {
			if (!types.toString().isEmpty()) {
				types.append(", ");
			}
			types.append("Powermeter");
		}
		if (this.isSwitchableOutlet()) {
			if (!types.toString().isEmpty()) {
				types.append(", ");
			}
			types.append("SwitchableOutlet");
		}
		if (this.isTempSensor()) {
			if (!types.toString().isEmpty()) {
				types.append(", ");
			}
			types.append("TemperatureSensor");
		}
		out.append(types.toString());
		out.append("), id=").append(this.deviceId);
		out.append(", manufacturer=").append(this.deviceManufacturer);
		out.append(", productname=").append(this.productName);
		out.append(", fwversion=").append(this.firmwareVersion);
		out.append(") [").append(this.getPowermeter()).append(',');
		out.append(this.getTemperature()).append(']');
		return out.toString();
	}
}
