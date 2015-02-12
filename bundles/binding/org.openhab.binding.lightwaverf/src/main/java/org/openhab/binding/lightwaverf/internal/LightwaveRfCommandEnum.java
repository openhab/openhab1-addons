package com.Lightwave;

public enum LightwaveRfCommandEnum {

    ON(true, false, '1'),
    OFF(true, false, '0'),
    ALL_OFF(false, false, 'a'),
    DIM_TO_LAST_LEVEL(true, false, 'o'),
    DIM_TO_LEVEL(true, true, 'd'),
    MOOD(false, true, 'm');
    
    /*
     * BASIC    ON/OFF/DIM
     * Turn On Room1 Device 1. F !R1D1F1
     * Turn Off Room1 Device 2 !R1D2F0
     * All off in Room 1 Fa !R1Fa
     * Set to last recorded dim level Fo !R1D1Fo (that letter o not zero)
     * Dim Device (Dim levels 1-32) FdP !R1D1FdP32
     * MOODS
     * Save current settings as Room 1 Mood 3 FsP !R1FsP3
     * Recall Room 3 Mood 1 FmP !R3FmP1
     * 
     */
    
    private final boolean requiresParameter;
    private final boolean requiresDevice;
    private final char functionCode;
    
    private LightwaveRfCommandEnum(boolean requiresDevice, boolean requiresParameter, char functionCode) {
        this.requiresDevice = requiresDevice;
        this.requiresParameter = requiresParameter;
        this.functionCode = functionCode;
    }
    
    public boolean isParameterRequired(){
        return requiresParameter;
    }
    
    public boolean isDeviceReqired(){
        return requiresDevice;
    }
    
    public char getFunctionCode() {
        return functionCode;
    }
    
    
}
