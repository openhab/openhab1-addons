package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA;

public class SceneDevice extends TellstickDevice{

	public SceneDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}
	
	/**
	 * Executes Scene.
	 * 
	 * @throws TellstickException
	 */
	public void execute() throws TellstickException{
		int status = JNA.CLibrary.INSTANCE.tdExecute(getId());
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);
	}
	
	public String getType(){
		return "Scene";
	}
}
