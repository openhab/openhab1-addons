package org.openhab.binding.lightwaverf.internal;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfDeviceRegistrationCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfDimCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatingInfoResponse;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfOnOffCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSetHeatingTemperatureCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Type;


public class LightwaverfConvertor {

	// LightwaveRF messageId
    private int nextMessageId = 200;
    private final Lock lock = new ReentrantLock();

    
    public LightwaveRFCommand convertToLightwaveRfMessage(String roomId, String deviceId, Type command){
    	if(deviceId == null){
    		return convertToLightwaveRfMessage(roomId, command);
    	}
    	
    	int messageId = getAndIncrementMessageId();
    	
    	if(command instanceof OnOffType){
            boolean on = (command == OnOffType.ON);
            return new LightwaveRfOnOffCommand(messageId, roomId, deviceId, on);
        }
        else if(command instanceof PercentType){
            int dimmingLevel = ((PercentType) command).intValue();
            return new LightwaveRfDimCommand(messageId, roomId, deviceId, dimmingLevel);
        }
        throw new RuntimeException("Unsupported Command: " + command);
    }

    /**
     * Increment message counter, so different messages have different IDs
     * Important for getting corresponding OK acknowledgements from port 9761 tagged with the same counter value
     */
    private int getAndIncrementMessageId() {
    	try{
    		lock.lock();
			int myMessageId = nextMessageId;
			if(myMessageId >= 999){
				nextMessageId = 200;
			}
			return myMessageId;
    	}
    	finally{
    		lock.unlock();
    	}
    }
	public LightwaveRFCommand convertToLightwaveRfMessage(String roomId, Type command){
    	if(roomId == null){
    		throw new IllegalArgumentException("Item not found");
    	}
    	throw new IllegalArgumentException("Not implemented yet");
    }
    
    public LightwaveRFCommand convertFromLightwaveRfMessage(String message) throws LightwaveRfMessageException {
    	if(LightwaveRfCommandOk.matches(message)){
    		return new LightwaveRfCommandOk(message);
    	}
    	else if(LightwaveRfVersionMessage.matches(message)){
    		return new LightwaveRfVersionMessage(message);
    	}
    	else if(LightwaveRfDeviceRegistrationCommand.matches(message)){
    		return new LightwaveRfDeviceRegistrationCommand(message);
    	}
    	else if(LightwaveRfHeatingInfoResponse.matches(message)){
    		return new LightwaveRfHeatingInfoResponse(message);
    	}
    	else if(LightwaveRfSetHeatingTemperatureCommand.matches(message)){
    		return new LightwaveRfSetHeatingTemperatureCommand(message);
    	}
    	else if(LightwaveRfHeatInfoRequest.matches(message)){
    		return new LightwaveRfHeatInfoRequest(message);
    	}
    	else if(LightwaveRfDimCommand.matches(message)){
    		return new LightwaveRfDimCommand(message);
    	}
    	else if(LightwaveRfOnOffCommand.matches(message)){
    		return new LightwaveRfOnOffCommand(message);
    	}
		throw new LightwaveRfMessageException("Message not recorgnised: " + message);
    	
    }
    
	public LightwaveRFCommand getRegistrationCommand() {
		return new LightwaveRfDeviceRegistrationCommand();
	}

	public LightwaveRFCommand getHeatRequest(String roomId) {
		int messageId = getAndIncrementMessageId();
		return new LightwaveRfHeatInfoRequest(messageId, roomId);
	}
}
