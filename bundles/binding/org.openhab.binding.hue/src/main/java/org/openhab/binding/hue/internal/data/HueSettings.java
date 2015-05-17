/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal.data;

import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains all information that is provided by the Hue bridge. There are
 * different information that can be requested about the connected bulbs.
 * <ul>
 * <li>Is a bulb switched on?</li>
 * <li>How is a bulb's color temperature?</li>
 * <li>How is the current brightness of a blub?</li>
 * <li>How is the hue of a given bulb?</li>
 * <li>How is the saturation of a given bulb?</li>
 * </ul>
 * 
 * @author Roman Hartmann
 * @author Jos Schering
 * @since 1.2.0
 */
public class HueSettings {

	static final Logger logger = LoggerFactory.getLogger(HueSettings.class);

	private SettingsTree settingsData = null;

	/**
	 * Constructor of HueSettings. It takes the settings of the Hue bridge to
	 * enable the HueSettings to determine the needed information about the
	 * bulbs.
	 * 
	 * @param settings
	 *            This is the settings string in Json format returned by the Hue
	 *            bridge.
	 */
	@SuppressWarnings("unchecked")
	public HueSettings(String settings) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			settingsData = new SettingsTree(mapper.readValue(settings,
					Map.class));
		} catch (Exception e) {
			logger.error("Could not read Settings-Json from Hue Bridge.");
		}
	}

	/**
	 * Return the keys of lights connected to Hue hub
	 * 
	 * @return the keys of lights connected to Hue hub
	 */
	public Set<String> getKeys() {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return null;
		}
		return settingsData.node("lights").getKeys();
	}

	/**
	 * Determines whether the given bulb is valid in the settings of the bridge.
	 * 
	 * @param deviceId
	 *            The bulb id the bridge has filed the bulb under.
	 * @return true if the bulb is turned on, false otherwise.
	 */
	public boolean isValidId(String deviceId) {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return false;
		}
		return (Boolean) (settingsData.node("lights")
				.node(deviceId) != null);
	}
	
	/**
	 * Determines whether the given bulb is turned on.
	 * 
	 * @param deviceId
	 *            The bulb id the bridge has filed the bulb under.
	 * @return true if the bulb is turned on, false otherwise.
	 */
	public boolean isBulbOn(String deviceId) {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return false;
		}
		return (Boolean) settingsData.node("lights")
				.node(deviceId).node("state").value("on");
	}
	
	/**
	 * Determines whether the given bulb is reachable.
	 * Once a bulb is physical disconnected it becomes unreachable after around 10 seconds. 
	 * The hearbeat service of the Hue hub device is responsible for this behavior.  
	 * 
	 * @param deviceId
	 *            The bulb id the bridge has filed the bulb under.
	 * @return true if the bulb is reachable, false otherwise.
	 */
	public boolean isReachable(String deviceId) {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return false;
		}
		return (Boolean) settingsData.node("lights")
				.node(deviceId).node("state").value("reachable");
	}
	
	/**
	 * Determines the color temperature of the given bulb.
	 * 
	 * @param deviceId
	 *            The bulb id the bridge has filed the bulb under.
	 * @return The color temperature as a value from 154 - 500
	 */
	public int getColorTemperature(String deviceId) {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return 154;
		}
		Object ct = settingsData.node("lights").node(deviceId).node("state").value("ct");
		if(ct instanceof Integer) {
			return (Integer) ct;
		} else {
			return 154;
		}
	}

	/**
	 * Determines the brightness of the given bulb.
	 * 
	 * @param deviceId
	 *            The bulb id the bridge has filed the bulb under.
	 * @return The brightness as a value from 0 - {@link HueBulb#MAX_BRIGHTNESS}
	 */
	public int getBrightness(String deviceId) {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return 0;
		}
		return (Integer) settingsData.node("lights")
				.node(deviceId).node("state")
				.value("bri");
	}

	/**
	 * Determines the hue of the given bulb.
	 * 
	 * @param deviceId
	 *            The bulb id the bridge has filed the bulb under.
	 * @return The hue as a value from 0 - 65535
	 */
	public int getHue(String deviceId) {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return 0;
		}

		Object hue = settingsData.node("lights")
				.node(deviceId).node("state")
				.value("hue");
		if(hue instanceof Integer) {
			return (Integer) hue;
		} else {
			return 0;
		}
	}

	/**
	 * Determines the saturation of the given bulb.
	 * 
	 * @param deviceId
	 *            The bulb id the bridge has filed the bulb under.
	 * @return The saturation as a value from 0 - {@link HueBulb#MAX_BRIGHTNESS}
	 */
	public int getSaturation(String deviceId) {
		if (settingsData == null) {
			logger.error("Hue bridge settings not initialized correctly.");
			return 0;
		}

		Object sat = settingsData.node("lights")
				.node(deviceId).node("state")
				.value("sat");
		if(sat instanceof Integer) {
			return (Integer) sat;
		} else {
			return 0;
		}
	}

	/**
	 * The SettingsTree represents the settings Json as a tree with some
	 * convenience methods to get subtrees and the values of interest easily.
	 */
	class SettingsTree {

		private Map<String, Object> dataMap;

		/**
		 * Constructor of the SettingsTree.
		 * 
		 * @param treeData
		 *            The Json data as it has been returned by the Json object
		 *            mapper.
		 */
		public SettingsTree(Map<String, Object> treeData) {
			this.dataMap = treeData;
		}

		/**
		 * @param nodeName
		 *            The name of the child node.
		 * @return The child node named like nodeName. This will be a sub tree.
		 */
		@SuppressWarnings("unchecked")
		protected SettingsTree node(String nodeName) {
			return new SettingsTree((Map<String, Object>) dataMap.get(nodeName));
		}

		/**
		 * @return the amount of lights connected to Hue hub
		 */
		protected int count() {
			return dataMap.size();
		}

		protected Set<String> getKeys(){
			return dataMap.keySet();
		}
		
		/**
		 * @param valueName
		 *            The name of the child node.
		 * @return The child node named like nodeName. This will be an object.
		 */
		protected Object value(String valueName) {
			return dataMap.get(valueName);
		}

	}

}
