package org.openhab.binding.hue.internal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	private Map<String,HueTapState> lastTapStates = null;
	
	/**
	 * find tap devices that were pressed since last call
	 * @param settings
	 */
	public Set<String> findPressedTapDevices(HueSettings settings){
		Map<String,HueTapState> states=settings.getTapStates();
		HashSet<String> foundDevices=new HashSet<String>();
		if(lastTapStates!=null){
			// iterate over all saved states
			for(String deviceId:lastTapStates.keySet()){
				HueTapState s1=lastTapStates.get(deviceId);
				HueTapState s2=states.get(deviceId);
				if(s2!=null){
					if(!s2.equals(s1)){
						foundDevices.add(deviceId);
					}
				}
			}
		}
		lastTapStates=states;
		return foundDevices;
	}
}
