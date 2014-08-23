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
