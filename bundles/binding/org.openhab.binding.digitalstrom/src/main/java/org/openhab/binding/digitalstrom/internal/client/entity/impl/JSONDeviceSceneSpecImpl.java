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
package org.openhab.binding.digitalstrom.internal.client.entity.impl;

import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.constants.SceneEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.DeviceSceneSpec;
import org.openhab.binding.digitalstrom.internal.client.entity.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONDeviceSceneSpecImpl implements DeviceSceneSpec {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONDeviceSceneSpecImpl.class);
	
	private Scene		scene			= null;
	private boolean		dontcare		= false;
	private boolean		localPrio		= false;
	private boolean		specialMode 	= false;
	private boolean		flashMode		= false;
	
	public JSONDeviceSceneSpecImpl(JSONObject jObject) {
		if (jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_SCENE_ID.getKey()) != null) {
			int val = -1;
			try {
				val = Integer.parseInt(jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_SCENE_ID.getKey()).toString());
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by getting sceneID: "+jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_SCENE_ID.getKey()).toString());
			}
				
			if (val > -1) {
				this.scene = SceneEnum.getScene(val);
			}
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_DONT_CARE.getKey()) != null) {
			this.dontcare = jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_DONT_CARE.getKey()).toString().equals("true");
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_LOCAL_PRIO.getKey()) != null) {
			this.localPrio = jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_LOCAL_PRIO.getKey()).toString().equals("true");
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_SPECIAL_MODE.getKey()) != null) {
			this.specialMode = jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_SPECIAL_MODE.getKey()).toString().equals("true");
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_FLASH_MODE.getKey()) != null) {
			this.flashMode = jObject.get(JSONApiResponseKeysEnum.DEVICE_GET_SCENE_MODE_FLASH_MODE.getKey()).toString().equals("true");
		}
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public boolean isDontCare() {
		return dontcare;
	}

	@Override
	public synchronized void setDontcare(boolean dontcare) {
		this.dontcare = dontcare;
	}

	@Override
	public boolean isLocalPrio() {
		return localPrio;
	}

	@Override
	public synchronized void setLocalPrio(boolean localPrio) {
		this.localPrio = localPrio;
	}

	@Override
	public boolean isSpecialMode() {
		return specialMode;
	}

	@Override
	public synchronized void setSpecialMode(boolean specialMode) {
		this.specialMode = specialMode;
	}

	@Override
	public boolean isFlashMode() {
		return flashMode;
	}

	@Override
	public synchronized void setFlashMode(boolean flashMode) {
		this.flashMode = flashMode;
	}
	
	@Override
	public String toString() {
		return "Scene: "+this.getScene()+", dontcare: "+this.isDontCare()+", localPrio: "+this.isLocalPrio()+", specialMode: "+this.isSpecialMode()+", flashMode: "+this.isFlashMode();
	}

}
