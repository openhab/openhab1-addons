/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera;

import org.fourthline.cling.model.types.ServiceId;

/**
 * A constants interface used in the Vera binding.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public interface VeraBindingConstants {
    
    // static service ids
    ServiceId DIMMING                   = ServiceId.valueOf("urn:upnp-org:serviceId:Dimming1");
    ServiceId HA_DEVICE                 = ServiceId.valueOf("urn:micasaverde-com:serviceId:HaDevice1");
    ServiceId HUMIDITY_SENSOR           = ServiceId.valueOf("urn:micasaverde-com:serviceId:HumiditySensor1");
    ServiceId HVAC_OPERATING_STATE      = ServiceId.valueOf("urn:micasaverde-com:serviceId:HVAC_OperatingState1");
    ServiceId HVAC_USER_OPERATING_MODE  = ServiceId.valueOf("urn:upnp-org:serviceId:HVAC_UserOperatingMode1");
    ServiceId SECURITY_SENSOR           = ServiceId.valueOf("urn:micasaverde-com:serviceId:SecuritySensor1");
    ServiceId SWITCH_POWER              = ServiceId.valueOf("urn:upnp-org:serviceId:SwitchPower1");
    ServiceId TEMPERATURE_SENSOR        = ServiceId.valueOf("urn:upnp-org:serviceId:TemperatureSensor1");
    ServiceId TEMPERATURE_SETPOINT_HEAT = ServiceId.valueOf("urn:upnp-org:serviceId:TemperatureSetpoint1_Heat");
    
    // valid argument keys
    String ARG_HVAC     = "hvac";
    String ARG_SENSOR   = "sensor";
    String ARG_SETPOINT = "setpoint";
    
    // valid hvac=? values
    String HVAC_MODE  = "mode";
    String HVAC_STATE = "state";
    
    // valid sensor=? values
    String SENSOR_BATTERY     = "battery";
    String SENSOR_HUMIDITY    = "humidity";
    String SENSOR_SECURITY    = "security";
    String SENSOR_TEMPERATURE = "temperature";

    // valid sensor=? values
    String SETPOINT_HEAT = "heat";
    
    // key used in job data maps
    String SUBSCRIPTION_JOB_DATA_KEY = "binding";
    
}
