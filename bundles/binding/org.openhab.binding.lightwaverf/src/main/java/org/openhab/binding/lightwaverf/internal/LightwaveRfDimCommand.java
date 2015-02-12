package org.openhab.binding.lightwaverf.internal;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LightwaveRfDimCommand implements LightwaveRFCommand {
	
	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!R([0-9])D([0-9])FdP([0-9]{1,2})");
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	
    private final String roomId;
    private final String deviceId;
    private final int dimLevel;

	/**
	 * Commands are like: 
	 *     100,!R2D3FdP1 (Lowest Brightness)
	 *     101,!R2D3FdP32 (High brightness)
	 */    
    
    public LightwaveRfDimCommand(String roomId, String deviceId, int dimmingLevel) {
        this.roomId = roomId;
        this.deviceId = deviceId;
        this.dimLevel = convertOpenhabDimToLightwaveDim(dimmingLevel);
    }

    public LightwaveRfDimCommand(String message){
    	Matcher matcher = REG_EXP.matcher(message);
    	roomId = matcher.group(1);
    	deviceId = matcher.group(2);
    	int lightwaveDimLevel = Integer.valueOf(matcher.group(3));
    	dimLevel = convertLightwaveDimToOpenhabDim(lightwaveDimLevel);
    }
    
	@Override
    public String getLightwaveRfCommandString() {
        return "!R" + roomId + "D" + deviceId + "FdP" + dimLevel + "\n";
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

}
