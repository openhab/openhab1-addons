/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
