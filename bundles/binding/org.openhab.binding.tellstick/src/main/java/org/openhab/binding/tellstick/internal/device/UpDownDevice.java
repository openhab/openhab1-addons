package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA;

/**
 * Up / Down devices can be such devices as Projector screens.
 * 
 * @author peec
 *
 */
public class UpDownDevice extends TellstickDevice{

	public UpDownDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}
	
	
	/**
	 * Sends up command.
	 * @throws TellstickException 
	 */
	public void up() throws TellstickException{
		int status = JNA.CLibrary.INSTANCE.tdUp(getId());
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);		
	}

	/**
	 * Sends down command.
	 * @throws TellstickException 
	 */
	public void down() throws TellstickException{
		int status = JNA.CLibrary.INSTANCE.tdDown(getId());
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);				
	}
	
	/**
	 * Stops execution.
	 * @throws TellstickException
	 */
	public void stop() throws TellstickException{
		int status = JNA.CLibrary.INSTANCE.tdStop(getId());
		if (status != TELLSTICK_SUCCESS)throw new TellstickException(this, status);		
	}
	

	public String getType(){
		return "Projector Screen";
	}
	
}
