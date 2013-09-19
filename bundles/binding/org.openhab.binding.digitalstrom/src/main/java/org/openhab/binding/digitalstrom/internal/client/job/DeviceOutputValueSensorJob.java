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
