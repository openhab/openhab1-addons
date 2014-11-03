/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis.internal;

/**
 * Constants 
 * 
 * @author Thomas Trathnigg
 */
public interface Constants {

    public static final byte ACK = 0x06;  
    
    public static final String FORECAST_ICON_RAIN = "rain";
    public static final String FORECAST_ICON_CLOUD = "cloud";
    public static final String FORECAST_ICON_PARTLY_CLOUD = "partly_cloud";
    public static final String FORECAST_ICON_SUN = "sun";
    public static final String FORECAST_ICON_SNOW = "snow";    
    public static final byte FORECAST_ICON_RAIN_BIT = 0x01;
    public static final byte FORECAST_ICON_CLOUD_BIT = 0x02;
    public static final byte FORECAST_ICON_PARTLY_CLOUD_BIT = 0x04;
    public static final byte FORECAST_ICON_SUN_BIT = 0x08;
    public static final byte FORECAST_ICON_SNOW_BIT = 0x10;
    
    public static final int RESPONSE_TYPE_NONE = 0;
    public static final int RESPONSE_TYPE_OK = 1;
    public static final int RESPONSE_TYPE_ACK = 2;

    public static final int RESPONSE_LIMITER_TYPE_CRLF = 0;
    public static final int RESPONSE_LIMITER_TYPE_MULTIPLE_CRLF = 1;
    public static final int RESPONSE_LIMITER_TYPE_FIXED_SIZE = 2;

    public static final int CRC_CHECK_TYPE_NONE = 0;
    public static final int CRC_CHECK_TYPE_VAR1 = 1;  
    
    public static final double RAIN_CLICK_BASE = 0.2; 
}
