/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 * @version	digitalSTROM-API 1.14.5
 */
public class SceneToStateMapper {
	
	private Map<Short, Boolean>	map = null;
	
	public SceneToStateMapper() {
		this.map = new HashMap<Short, Boolean>();
		this.init();
	}
	
	private void init() {
		map.put((short) 0, false);
		map.put((short) 1, false);
		map.put((short) 2, false);
		map.put((short) 3, false);
		map.put((short) 4, false);
		
		map.put((short) 5, true);
		map.put((short) 6, true);
		map.put((short) 7, true);
		map.put((short) 8, true);
		map.put((short) 9, true);
		
		map.put((short) 13, false);
		map.put((short) 14, true);
		
		map.put((short) 32, false);
		map.put((short) 33, true);
		map.put((short) 34, false);
		map.put((short) 35, true);
		map.put((short) 36, false);
		map.put((short) 37, true);
		map.put((short) 38, false);
		map.put((short) 39, true);
		
		map.put((short) 50, false);
		map.put((short) 51, true);
		
	}
	
	/**
	 * If there is a state mapping for this scene,
	 * returns true. You should run this before using
	 * the method 'getMapping(short)' !!!
	 * 
	 * @param val	sceneId
	 * @return		true, if a mapping exists for this sceneId
	 */
	public boolean isMappable(short val) {
		return map.containsKey(val);
	}
	
	/**
	 * Please check at first with a call 'isMappable(short)'
	 * if there is a mapping for this number.
	 * If not you can not be sure to get a valid boolean-false
	 * 
	 * @param val	scene-number
	 * @return		true or false if this scene will cause a 'isOn' state in device
	 */
	public boolean getMapping(short val) {
		if (map.containsKey(val)) {
			return map.get(val);
		}
		return false;
	}

}
