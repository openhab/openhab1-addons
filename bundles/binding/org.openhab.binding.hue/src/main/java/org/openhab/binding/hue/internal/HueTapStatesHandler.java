package org.openhab.binding.hue.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.hue.internal.data.HueSettings;
import org.openhab.binding.hue.internal.hardware.HueTapState;

/**
 * hanldes al necessary i nformation for tap changes
 * @author gernot
 *
 */
public class HueTapStatesHandler {
	/**
	 * tap states when last checked
	 */
	private Map<Integer,HueTapState> lastTapStates = null;
	
	/**
	 * find tap devices that were pressed since last call
	 * @param settings
	 */
	public Map<Integer, HueTapState> findPressedTapDevices(HueSettings settings){
		Map<Integer,HueTapState> states=settings.getTapStates();
		Map<Integer,HueTapState> foundDevices=new HashMap<Integer,HueTapState>();
		if(lastTapStates!=null){
			// iterate over all saved states
			for(Integer deviceId:lastTapStates.keySet()){
				HueTapState s1=lastTapStates.get(deviceId);
				HueTapState s2=states.get(deviceId);
				if(s2!=null){
					if(!s2.equals(s1)){
						//TODO: what if 2 events on the same switch occurred? unlikely, but possible; only the younger one should prevail
						foundDevices.put(deviceId,s2);
					}
				}
			}
		}
		lastTapStates=states;
		return foundDevices;
	}
}
