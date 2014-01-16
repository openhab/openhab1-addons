/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.job;

import org.openhab.binding.digitalstrom.internal.client.DigitalSTROMAPI;
import org.openhab.binding.digitalstrom.internal.client.constants.SensorIndexEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;
import org.openhab.binding.digitalstrom.internal.client.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public class DeviceConsumptionSensorJob implements SensorJob {

	private static final Logger logger = LoggerFactory
			.getLogger(DeviceConsumptionSensorJob.class);
	private Device device = null;
	private SensorIndexEnum sensorIndex = null;
	
	public DeviceConsumptionSensorJob(Device device, SensorIndexEnum index) {
		this.device = device;
		this.sensorIndex = index;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.digitalSTROM2.internal.client.job.SensorJob#execute(org.openhab.binding.digitalSTROM2.internal.client.DigitalSTROMAPI, java.lang.String)
	 */
	@Override
	public void execute(DigitalSTROMAPI digitalSTROM, String token) {
		int consumption = digitalSTROM.getDeviceSensorValue(token, this.device.getDSID(), null, this.sensorIndex);
		logger.info("DeviceConsumption : "+consumption+", DSID: "+this.device.getDSID().getValue());

		switch (this.sensorIndex) {
	
			case ACTIVE_POWER:
							this.device.setPowerConsumption(consumption);
							break;
			case OUTPUT_CURRENT:
							this.device.setElectricMeterValue(consumption);
							break;
			default:
				break;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DeviceConsumptionSensorJob) {
			DeviceConsumptionSensorJob other = (DeviceConsumptionSensorJob) obj;
			String device = this.device.getDSID().getValue()+this.sensorIndex.getIndex();
			return device.equals(other.device.getDSID().getValue()+other.sensorIndex.getIndex());
		}
		return false;
	}

	@Override
	public DSID getDsid() {
		return device.getDSID();
	}
}
