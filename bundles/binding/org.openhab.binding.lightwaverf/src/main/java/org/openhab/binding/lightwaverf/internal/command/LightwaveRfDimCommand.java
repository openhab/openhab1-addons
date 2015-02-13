package org.openhab.binding.lightwaverf.internal.command;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfMessageId;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

public class LightwaveRfDimCommand extends AbstractLightwaveRfCommand implements LightwaveRFCommand {
	
	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!R([0-9])D([0-9])FdP([0-9]{1,2})");
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	
    private final String roomId;
    private final String deviceId;
    private final int openhabDimLevel;
    private final int lightWaveDimLevel;
    private final LightwaveRfMessageId messageId;
    
	/**
	 * Commands are like: 
	 *     100,!R2D3FdP1 (Lowest Brightness)
	 *     101,!R2D3FdP32 (High brightness)
	 */    
    
    public LightwaveRfDimCommand(int messageId, String roomId, String deviceId, int dimmingLevel) {
        this.roomId = roomId;
        this.deviceId = deviceId;
        this.openhabDimLevel = dimmingLevel;
        this.lightWaveDimLevel = convertOpenhabDimToLightwaveDim(dimmingLevel);
        this.messageId = new LightwaveRfMessageId(messageId);
    }

    public LightwaveRfDimCommand(String message) throws LightwaveRfMessageException {
    	try{
	    	Matcher matcher = REG_EXP.matcher(message);
	    	matcher.matches();
			this.messageId = new LightwaveRfMessageId(Integer.valueOf(matcher.group(1)));
	    	this.roomId = matcher.group(2);
	    	this.deviceId = matcher.group(3);
	    	this.lightWaveDimLevel = Integer.valueOf(matcher.group(4));
	    	this.openhabDimLevel = convertLightwaveDimToOpenhabDim(lightWaveDimLevel);
		}
		catch(Exception e){
			throw new LightwaveRfMessageException("Error converting message: " + message, e);
		}
    }
    
    public String getLightwaveRfCommandString() {
    	String function = "d";
    	return getMessageString(messageId, roomId, deviceId, function, String.valueOf(lightWaveDimLevel));
    }
	
	/**
	 * Convert a 0-31 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-31
	 */
	public static int convertOpenhabDimToLightwaveDim(int openhabDim) {
		return BigDecimal.valueOf(openhabDim)
				.multiply(BigDecimal.valueOf(32))
				.divide(HUNDRED, 0,
						BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * Convert a 0-31 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-31
	 */
	public static int convertLightwaveDimToOpenhabDim(int lightwavedim) {
		lightwavedim = Math.min(lightwavedim, 32);
		
		return BigDecimal
				.valueOf(lightwavedim)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(32), 0,
						BigDecimal.ROUND_UP).intValue();
	}

	public String getRoomId() {
		return roomId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public State getState() {
		return new PercentType(openhabDimLevel);
	}

	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}	

}
