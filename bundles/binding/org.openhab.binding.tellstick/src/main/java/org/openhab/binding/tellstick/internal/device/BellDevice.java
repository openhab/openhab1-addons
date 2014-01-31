package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA;
import org.openhab.binding.tellstick.internal.device.iface.BellDeviceIntf;



public class BellDevice extends TellstickDevice implements BellDeviceIntf {

	public BellDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	@Override
	public void bell() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdBell(getId());
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);
	}
	
	public String getType(){
		return "Bell Device";
	}

}
