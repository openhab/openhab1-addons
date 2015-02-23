package org.openhab.binding.hue.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.hue.internal.data.HueSettings;
import org.openhab.binding.hue.internal.hardware.HueTapState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hanldes al necessary i nformation for tap changes
 * @author gernot
 *
 */
public class HueTapStatesHandler {
	
	static final Logger logger = LoggerFactory.getLogger(HueTapStatesHandler.class);
	
	/**
	 * tap states when last checked
	 */
	private Map<Integer,HueTapState> lastTapStates = null;
	
	/**
	 * find tap devices that were pressed since last call
	 * @param settings
	 * @throws HueSettingsParseException 
	 */
	public Map<Integer, HueTapState> findPressedTapDevices(HueSettings settings) {
		Map<Integer, HueTapState> states;
		Map<Integer, HueTapState> foundDevices;
		foundDevices = new HashMap<Integer,HueTapState>();
		try {
			states = settings.getTapStates();
			if(lastTapStates!=null){
				// iterate over all saved states
				for(Integer deviceId:lastTapStates.keySet()){
					HueTapState s1=lastTapStates.get(deviceId);
					HueTapState s2=states.get(deviceId);
					if(s2!=null){
						if(!s2.equals(s1)){
							foundDevices.put(deviceId,s2);
						}
					}
				}
			}
			lastTapStates=states;

		} catch (Exception e) {
			logger.warn("error parsing hue bridge settings", e);
		}
		return foundDevices;

	}
}
