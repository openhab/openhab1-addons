package org.openhab.binding.tellstick.internal.device;
import org.openhab.binding.tellstick.internal.JNA;

import com.sun.jna.Pointer;


public class TellstickException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected TellstickDevice dev;

	protected int errorcode;
	
	public TellstickException(TellstickDevice dev, int errorcode){
		super();
		this.dev = dev;
		this.errorcode = errorcode;
	}
	
	
	@Override
	public String getMessage(){
		Pointer errorP = JNA.CLibrary.INSTANCE.tdGetErrorString(errorcode);
		String error = errorP.getString(0);
		JNA.CLibrary.INSTANCE.tdReleaseString(errorP);
		return dev.getName() + ": " + error;
	}
	
}
