/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device;

import java.util.ArrayList;
import org.openhab.binding.tellstick.internal.JNA;

/**
 * A group can contain many methods and many devices! A Device group can contain
 * many actions such as Bell, Dim and etc.
 * 
 * 
 * You must iterate getActions() in this class. Check with instanceof operator.
 * 
 * 
 * Example: -------------- GroupDevice gp = new GroupDevice(deviceId);
 * for(TellstickDevice dev : gp.getActions()){ if (dev instanceof BellDevice){
 * // bell method accepted. } if (dev instanceof DimmableDevice){ // dim method
 * accepted. } if(dev instanceof Device){ // Device instance. (On / Off accepted
 * ) } }
 * 
 * @author peec
 * 
 */
public class GroupDevice extends TellstickDevice {

	/**
	 * Contains collection of instances to BellDevice / DimmableDevice / Device.
	 * Can be only BellDevice and etc.
	 */
	ArrayList<TellstickDevice> deviceActions = new ArrayList<TellstickDevice>();

	public GroupDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);

		int methods = JNA.CLibrary.INSTANCE.tdMethods(deviceId, getSupportedMethods());

		// Now single action based.
		if ((methods & JNA.CLibrary.TELLSTICK_BELL) > 0) {
			deviceActions.add(new BellDevice(deviceId));
		}

		if ((methods & JNA.CLibrary.TELLSTICK_DIM) > 0) {
			deviceActions.add(new DimmableDevice(deviceId));
		} else if ((methods & JNA.CLibrary.TELLSTICK_TURNON) > 0 && (methods & JNA.CLibrary.TELLSTICK_TURNOFF) > 0) {
			deviceActions.add(new Device(deviceId));
		}

		if ((methods & JNA.CLibrary.TELLSTICK_UP) > 0 && (methods & JNA.CLibrary.TELLSTICK_DOWN) > 0
				&& (methods & JNA.CLibrary.TELLSTICK_STOP) > 0) {
			deviceActions.add(new UpDownDevice(deviceId));
		}

		if ((methods & JNA.CLibrary.TELLSTICK_EXECUTE) > 0) {
			deviceActions.add(new SceneDevice(deviceId));
		}

	}

	/**
	 * Gets all the device actions possible.
	 * 
	 * @return list of actions
	 */
	public ArrayList<TellstickDevice> getActions() {
		return deviceActions;
	}

	/**
	 * Can be used such as: this.hasDevice(Device.class, DimmableDevice.class,
	 * BellDevice.class, ... ) Return true if it has the device action.
	 * 
	 * 
	 * @param types
	 * @return true if type is instance of device
	 */
	public boolean hasDevice(Class<? extends TellstickDevice>... types) {

		ArrayList<TellstickDevice> list = getActions();
		for (TellstickDevice i : list) {
			for (Class<? extends TellstickDevice> dev : types) {
				if (dev.isInstance(i)) {
					return true;
				}
			}
		}

		return false;
	}

	public String getType() {
		return "Group";
	}

}
