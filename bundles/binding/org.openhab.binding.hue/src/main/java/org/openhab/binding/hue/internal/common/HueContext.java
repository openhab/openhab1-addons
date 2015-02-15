package org.openhab.binding.hue.internal.common;

import org.openhab.binding.hue.internal.hardware.HueBridge;

//import org.openhab.binding.homematic.internal.communicator.StateHolder;

/**
 * singleton to hold common data for action and binding
 * @author Gernot Eger
 *
 */
public class HueContext {

	public HueBridge getBridge() {
		return bridge;
	}

	public void setBridge(HueBridge bridge) {
		this.bridge = bridge;
	}

	private HueBridge bridge;
	
	
	private static HueContext instance;

	
	private HueContext() {
	}

	/**
	 * Create or returns the instance of this class.
	 */
	public static HueContext getInstance() {
		if (instance == null) {
			instance = new HueContext();
			//instance.stateHolder = new StateHolder(instance);
		}
		return instance;
	}
}
