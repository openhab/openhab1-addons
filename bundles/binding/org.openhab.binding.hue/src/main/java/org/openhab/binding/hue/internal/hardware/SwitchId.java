package org.openhab.binding.hue.internal.hardware;

import java.util.HashMap;

/**
 * The binding type of the hue item.
 * <ul>
 * <li>switch1 - . </li>
 * <li>switch2 - : </li>
 * <li>switch3 - :. </li>
 * <li>switch4 - :: </li>
 * </ul>
 * 
 * The last button pressed is found in "buttonevent", which is 34,16,17,18 for buttons 1,2,3,4
 * @see http://www.developers.meethue.com/documentation/sensors-api
 *
 */
public enum SwitchId {
	SWITCH1(1,34), SWITCH2(2,16), SWITCH3(3,17), SWITCH4(4,18);
	
	/**
	 * the Id as used in the binding config
	 */
	private final int configId;		
	
	/**
	 * the button event from hue hardware
	 */
	private final int buttonEvent;

	/**
	 * constructor which also defines the config-hardware mapping
	 * @param configId
	 * @param buttonEvent
	 */
	private SwitchId(int configId, int buttonEvent) {
		this.configId = configId;
		this.buttonEvent = buttonEvent;
	}	
	
	public int getConfigId() {
		return configId;
	}


	public int getButtonEvent() {
		return buttonEvent;
	}

	/**
	 * cache for my ids
	 */
	private static HashMap<Integer,SwitchId> ids=null;
	
	/**
	 * returns the switch id for this config value
	 * @param configId
	 * @return SwitchId or null if none is matching
	 */
	public static SwitchId switchIdForConfigId(int configId){
		
		if(ids==null){
			//lazily build lookupmap
			ids=new HashMap<Integer,SwitchId>();

			for(SwitchId id:SwitchId.values()){
				ids.put(id.configId, id);
			}
		}
		
		return ids.get(configId);
	}
	
}