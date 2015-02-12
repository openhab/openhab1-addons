package org.openhab.binding.lightwaverf.internal;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

public class LightwaveRfDimCommand implements LightwaveRFCommand {
	
	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!R([0-9])D([0-9])FdP([0-9]{1,2})");
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	
    private final String roomId;
    private final String deviceId;
    private final int openhabDimLevel;
    private final int lightWaveDimLevel;

	/**
	 * Commands are like: 
	 *     100,!R2D3FdP1 (Lowest Brightness)
	 *     101,!R2D3FdP32 (High brightness)
	 */    
    
    public LightwaveRfDimCommand(String roomId, String deviceId, int dimmingLevel) {
        this.roomId = roomId;
        this.deviceId = deviceId;
        this.openhabDimLevel = dimmingLevel;
        this.lightWaveDimLevel = convertOpenhabDimToLightwaveDim(dimmingLevel);
    }

    public LightwaveRfDimCommand(String message){
    	Matcher matcher = REG_EXP.matcher(message);
    	this.roomId = matcher.group(1);
    	this.deviceId = matcher.group(2);
    	this.lightWaveDimLevel = Integer.valueOf(matcher.group(3));
    	this.openhabDimLevel = convertLightwaveDimToOpenhabDim(lightWaveDimLevel);
    }
    
    public String getLightwaveRfCommandString() {
        return "!R" + roomId + "D" + deviceId + "FdP" + lightWaveDimLevel + "\n";
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
				.multiply(BigDecimal.valueOf(31))
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
		lightwavedim = Math.min(lightwavedim, 31);
		
		return BigDecimal
				.valueOf(lightwavedim)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(31), 0,
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

}
