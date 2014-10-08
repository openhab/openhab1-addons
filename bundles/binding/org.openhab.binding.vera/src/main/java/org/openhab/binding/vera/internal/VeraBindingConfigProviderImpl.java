/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal;

import static org.openhab.binding.vera.VeraBindingConstants.ARG_HVAC;
import static org.openhab.binding.vera.VeraBindingConstants.ARG_SENSOR;
import static org.openhab.binding.vera.VeraBindingConstants.ARG_SETPOINT;
import static org.openhab.binding.vera.VeraBindingConstants.DIMMING;
import static org.openhab.binding.vera.VeraBindingConstants.HA_DEVICE;
import static org.openhab.binding.vera.VeraBindingConstants.HUMIDITY_SENSOR;
import static org.openhab.binding.vera.VeraBindingConstants.HVAC_MODE;
import static org.openhab.binding.vera.VeraBindingConstants.HVAC_OPERATING_STATE;
import static org.openhab.binding.vera.VeraBindingConstants.HVAC_STATE;
import static org.openhab.binding.vera.VeraBindingConstants.HVAC_USER_OPERATING_MODE;
import static org.openhab.binding.vera.VeraBindingConstants.SECURITY_SENSOR;
import static org.openhab.binding.vera.VeraBindingConstants.SENSOR_BATTERY;
import static org.openhab.binding.vera.VeraBindingConstants.SENSOR_HUMIDITY;
import static org.openhab.binding.vera.VeraBindingConstants.SENSOR_SECURITY;
import static org.openhab.binding.vera.VeraBindingConstants.SENSOR_TEMPERATURE;
import static org.openhab.binding.vera.VeraBindingConstants.SETPOINT_HEAT;
import static org.openhab.binding.vera.VeraBindingConstants.SWITCH_POWER;
import static org.openhab.binding.vera.VeraBindingConstants.TEMPERATURE_SENSOR;
import static org.openhab.binding.vera.VeraBindingConstants.TEMPERATURE_SETPOINT_HEAT;
import static org.openhab.binding.vera.VeraBindingVariable.Armed;
import static org.openhab.binding.vera.VeraBindingVariable.BatteryLevel;
import static org.openhab.binding.vera.VeraBindingVariable.CurrentLevel;
import static org.openhab.binding.vera.VeraBindingVariable.CurrentSetpoint;
import static org.openhab.binding.vera.VeraBindingVariable.CurrentTemperature;
import static org.openhab.binding.vera.VeraBindingVariable.LastTrip;
import static org.openhab.binding.vera.VeraBindingVariable.LoadLevelStatus;
import static org.openhab.binding.vera.VeraBindingVariable.ModeState;
import static org.openhab.binding.vera.VeraBindingVariable.ModeStatus;
import static org.openhab.binding.vera.VeraBindingVariable.Status;
import static org.openhab.binding.vera.VeraBindingVariable.Tripped;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.model.types.ServiceId;
import org.openhab.binding.vera.VeraBindingConfig;
import org.openhab.binding.vera.VeraBindingConfigProvider;
import org.openhab.binding.vera.VeraBindingVariable;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for parsing {@link VeraBindingConfig}s 
 * found in <code>.items</code> configuration files.
 * 
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class VeraBindingConfigProviderImpl extends AbstractGenericBindingProvider implements VeraBindingConfigProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(VeraBindingConfigProviderImpl.class);

    // static lookup tables
    private static Map<Class<? extends Item>, ServiceId> itemServiceMap = new HashMap<Class<? extends Item>, ServiceId>();
    private static Map<String, ServiceId> sensorServiceMap = new HashMap<String, ServiceId>();
    private static Map<String, ServiceId> hvacServiceMap = new HashMap<String, ServiceId>();
    private static Map<String, ServiceId> setpointServiceMap = new HashMap<String, ServiceId>();
    
    // static lookup table for supported variables
    private static Map<ServiceId, VeraBindingVariable[]> variableMap = new HashMap<ServiceId, VeraBindingVariable[]>();
    
    static {
        // init item lookup table
        itemServiceMap.put(DimmerItem.class,   DIMMING);
        itemServiceMap.put(SwitchItem.class,   SWITCH_POWER);
        
        // init sensor lookup table
        sensorServiceMap.put(SENSOR_BATTERY,     HA_DEVICE);
        sensorServiceMap.put(SENSOR_HUMIDITY,    HUMIDITY_SENSOR);
        sensorServiceMap.put(SENSOR_SECURITY,    SECURITY_SENSOR);
        sensorServiceMap.put(SENSOR_TEMPERATURE, TEMPERATURE_SENSOR);
         
        // init hvac lookup table
        hvacServiceMap.put(HVAC_MODE,  HVAC_USER_OPERATING_MODE);
        hvacServiceMap.put(HVAC_STATE, HVAC_OPERATING_STATE);
        
        // init setpoint lookup table
        setpointServiceMap.put(SETPOINT_HEAT, TEMPERATURE_SETPOINT_HEAT);
        
        // init variable lookup table
        variableMap.put(SWITCH_POWER,               new VeraBindingVariable[] { Status });
        variableMap.put(DIMMING,                    new VeraBindingVariable[] { LoadLevelStatus });
        variableMap.put(TEMPERATURE_SENSOR,         new VeraBindingVariable[] { CurrentTemperature });
        variableMap.put(HUMIDITY_SENSOR,            new VeraBindingVariable[] { CurrentLevel });
        variableMap.put(HA_DEVICE,                  new VeraBindingVariable[] { BatteryLevel });
        variableMap.put(SECURITY_SENSOR,            new VeraBindingVariable[] { Armed, 
                                                                                LastTrip, 
                                                                                Tripped });
        variableMap.put(HVAC_OPERATING_STATE,       new VeraBindingVariable[] { ModeState });
        variableMap.put(HVAC_USER_OPERATING_MODE,   new VeraBindingVariable[] { ModeStatus });
        variableMap.put(TEMPERATURE_SETPOINT_HEAT,  new VeraBindingVariable[] { CurrentSetpoint });
    }
    
    /**
     * Factory method for discovering the {@link ServiceId} based on the binding arguments.
     * 
     * @param arguments the arguments parsed from the binding config
     * @return the {@link ServiceId}; or <code>null</code> if not found
     */
    private static ServiceId getServiceIdFromArguments(Map<String, String> arguments) {
        ServiceId serviceId = null;

        // sensor=?
        String sensor = arguments.get(ARG_SENSOR);
        if (StringUtils.isNotBlank(sensor) 
                && (serviceId = sensorServiceMap.get(sensor.toLowerCase())) != null) {
            return serviceId;
        }
        
        // hvac=?
        String hvac = arguments.get(ARG_HVAC);
        if (StringUtils.isNotBlank(hvac) 
                && (serviceId = hvacServiceMap.get(hvac.toLowerCase())) != null) {
            return serviceId;
        }
        
        // setpoint=?
        String setpoint = arguments.get(ARG_SETPOINT);
        if (StringUtils.isNotBlank(setpoint) 
                && (serviceId = setpointServiceMap.get(setpoint.toLowerCase())) != null) {
            return serviceId;
        }
        
        return serviceId;
    }
    
    /**
     * {@inheritDoc 
     */
    @Override
    public String getBindingType() {
        return "vera";
    }
    
    /**
     * {@inheritDoc 
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // all item types are valid
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
        logger.trace("processBindingConfiguration({}, {}, {})", context, item, bindingConfig);
        super.processBindingConfiguration(context, item, bindingConfig);
        
        String[] segments = bindingConfig.split(":");
        if (segments.length < 2 || segments.length > 3) {
            throw new BindingConfigParseException("invalid number of segments in binding: " + bindingConfig);
        }
        
        String unitId = segments[0];
        int deviceId = 0;
        try {
            deviceId = Integer.valueOf(segments[1]);
        } catch (NumberFormatException e) {
            throw new BindingConfigParseException(segments[1] + " is not a valid device id");
        }
        
        String itemName = item.getName();
        Class<? extends Item> itemType = item.getClass();
        
        // discover the arguments
        Map<String, String> arguments = new HashMap<String, String>();
        if (segments.length == 3) {
            for (String kvPairStr: segments[2].split(",")) {
                if (kvPairStr.contains("=")) {
                    String[] kvPair = kvPairStr.split("=");
                    arguments.put(kvPair[0].trim().toLowerCase(), kvPair[1].trim().toLowerCase());
                }
            }
        }
        
        // discover the service
        ServiceId serviceId = getServiceIdFromArguments(arguments);
        if (serviceId == null) {
            serviceId = itemServiceMap.get(itemType);
        }
        // cannot continue without service
        if (serviceId == null) {
            throw new BindingConfigParseException("could not determine service from binding = " + bindingConfig);
        }
        
        // discover the variable
        VeraBindingVariable variable = null;
        for (VeraBindingVariable var: variableMap.get(serviceId)) {
            if (var.supports(itemType)) {
                variable = var;
                break;
            }
        }
        // cannot continue without variable
        if (variable == null) {
            throw new BindingConfigParseException("could not determine " + serviceId.getId() + " variable from binding = " + bindingConfig);
        }
        
        VeraBindingConfig config = new VeraBindingConfig(itemType, itemName, unitId, deviceId, arguments, serviceId, variable);
        logger.debug("[{}] valid binding config = {}", itemName, config);
        addBindingConfig(item, config);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VeraBindingConfig getBindingConfig(String itemName) {
        return (VeraBindingConfig) bindingConfigs.get(itemName);
    }
    
}
