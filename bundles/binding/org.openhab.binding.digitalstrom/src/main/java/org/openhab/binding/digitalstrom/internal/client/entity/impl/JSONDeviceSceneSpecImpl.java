/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
