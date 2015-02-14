package org.openhab.binding.lightwaverf.internal.command;

import org.openhab.binding.lightwaverf.internal.LightwaveRfMessageId;
import org.openhab.core.types.State;

public class LightwaveRfHeatingInfoCommand implements LightwaveRFCommand {

	/*
	 * Commands Like
	 * *!{
	 * 		"trans":1232,
	 * 		"mac":"03:02:71",
	 * 		"time":1423827547,
	 * 		"prod":"valve",
	 * 		"serial":"5A4F02",
	 * 		"signal":0,
	 * 		"type":"temp",
	 * 		"batt":2.72,
	 * 		"ver":56,
	 * 		"state":"run",
	 * 		"cTemp":17.8,
	 * 		"cTarg":19.0,
	 * 		"output":80,
	 * 		"nTarg":24.0,
	 * 		"nSlot":"06:00",
	 * 		"prof":5
	 * }
	 */  
	
	public LightwaveRfHeatingInfoCommand(String message) {
		// TODO Auto-generated constructor stub
	}

	public String getLightwaveRfCommandString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRoomId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeviceId() {
		// TODO Auto-generated method stub
		return null;
	}

	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public LightwaveRfMessageId getMessageId() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean matches(String message) {
		if(message.contains("cTemp")){
			return true;
		}
		return false;
	}

}
