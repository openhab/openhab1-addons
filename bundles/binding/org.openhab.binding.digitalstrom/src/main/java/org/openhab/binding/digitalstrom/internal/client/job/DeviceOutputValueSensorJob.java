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
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;
import org.openhab.binding.digitalstrom.internal.client.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public class DeviceOutputValueSensorJob implements SensorJob {

	private static final Logger logger = LoggerFactory
			.getLogger(DeviceOutputValueSensorJob.class);
	private Device device = null;
	private short index = 0;
	
	public DeviceOutputValueSensorJob(Device device, short index) {
		this.device = device;
		this.index = index;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.digitalSTROM2.internal.client.job.SensorJob#execute(org.openhab.binding.digitalSTROM2.internal.client.DigitalSTROMAPI)
	 */
	@Override
	public void execute(DigitalSTROMAPI digitalSTROM, String token) {
		int value = digitalSTROM.getDeviceOutputValue(token, this.device.getDSID(), null, this.index);
		logger.info("DeviceOutputValue on Demand : "+value+", DSID: "+this.device.getDSID().getValue());
	
		if (value != 1) {
			switch (this.index) {
			case 0:
				this.device.setOutputValue(value);
				break;
			case 4:
				this.device.setSlatPosition(value);
				break;
		
			default: 
				break;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DeviceOutputValueSensorJob) {
			DeviceOutputValueSensorJob other = (DeviceOutputValueSensorJob) obj;
			String key = this.device.getDSID().getValue()+this.index;
			return key.equals((other.device.getDSID().getValue()+other.index));
		}
		return false;
	}

	@Override
	public DSID getDsid() {
		return device.getDSID();
	}
}
