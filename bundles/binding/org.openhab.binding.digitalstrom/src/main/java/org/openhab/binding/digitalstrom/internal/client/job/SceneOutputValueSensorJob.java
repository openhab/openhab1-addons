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
import org.openhab.binding.digitalstrom.internal.client.constants.DeviceParameterClassEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;
import org.openhab.binding.digitalstrom.internal.client.entity.Device;
import org.openhab.binding.digitalstrom.internal.client.entity.DeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public class SceneOutputValueSensorJob implements SensorJob {

	private static final Logger logger = LoggerFactory
			.getLogger(SceneOutputValueSensorJob.class);
	
	private Device device = null;
	private short sceneId = 0;

	public SceneOutputValueSensorJob(Device device, short sceneId) {
		this.device = device;
		this.sceneId = sceneId;
	}
	/* (non-Javadoc)
	 * @see org.openhab.binding.digitalSTROM2.internal.client.job.SensorJob#execute(org.openhab.binding.digitalSTROM2.internal.client.DigitalSTROMAPI, java.lang.String)
	 */
	@Override
	public void execute(DigitalSTROMAPI digitalSTROM, String token) {
		DeviceConfig config = digitalSTROM.getDeviceConfig(token, this.device.getDSID(), null, DeviceParameterClassEnum.CLASS_128, this.sceneId);
	
		if (config != null) {
			this.device.setSceneOutputValue(this.sceneId, (short) config.getValue());
			logger.info("UPDATED sceneOutputValue for dsid: "+this.device.getDSID()+", sceneID: "+sceneId+", value: "+config.getValue());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SceneOutputValueSensorJob) {
			SceneOutputValueSensorJob other = (SceneOutputValueSensorJob) obj;
			String str = other.device.getDSID().getValue()+"-"+other.sceneId;
			return (this.device.getDSID().getValue()+"-"+this.sceneId).equals(str);
		}
		return false;
	}

	@Override
	public DSID getDsid() {
		return device.getDSID();
	}	
	
}
