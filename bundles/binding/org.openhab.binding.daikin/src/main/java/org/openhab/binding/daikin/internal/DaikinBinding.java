/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.daikin.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import net.jonathangiles.daikin.DaikinFactory;
import net.jonathangiles.daikin.IDaikin;
import net.jonathangiles.daikin.enums.Fan;
import net.jonathangiles.daikin.enums.FanDirection;
import net.jonathangiles.daikin.enums.Mode;
import net.jonathangiles.daikin.enums.Timer;

import org.openhab.binding.daikin.DaikinBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An active binding which requests the state of a Daikin heat pump via the
 * KKRP01A online controller and can sends commands as well 
 * 
 *  - Commands supported:
 *  	- Power		
 *  	- Mode 		Auto, Cool, Dry, Heat, OnlyFun, Night
 *  	- Temp 		between 10C - 32C
 *  	- Fan 		Fun1, Fun2, Fun3, Fun4, Fun5 (speeds), FAuto (auto)
 *  	- Swing		Ud (up/down), Off
 * 
 * @author Ben Jones
 * @author Jos schering
 * @since 1.5.0
 */
public class DaikinBinding extends AbstractActiveBinding<DaikinBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(DaikinBinding.class);
	
	private static final String CONFIG_KEY_REFRESH = "refresh";

	private Long refreshInterval = 60000L;
	private Map<String, IDaikin> hosts = new HashMap<String, IDaikin>();
	
	/**
     * @{inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
    	return refreshInterval;
    }
    
    @Override
    protected String getName() {
    	return "Daikin Refresh Service";
    }
    
    @Override
    public void activate() {
    	super.activate();
    }
    
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		// update the internal state for the associated host and send the
		// new state (all values) to the controller to update
		for (DaikinBindingProvider provider : providers) {
			if (!provider.providesBindingFor(itemName))
				continue;
			
			DaikinBindingConfig bindingConfig = provider.getBindingConfig(itemName);
			if (!hosts.containsKey(bindingConfig.getId()))
				continue;
			
			IDaikin host = hosts.get(bindingConfig.getId());
			DaikinCommandType commandType = bindingConfig.getCommandType();
			
			if (!commandType.isExecutable()) {
				logger.warn("Attempting to send a command to '{}' which is not executable ({}). Ignoring.", itemName, commandType);
				continue;
			}
			
			setState(host, commandType, command);
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		// refresh the state for each host and then check for any item
		// bindings that are associated with this host, and update
		for (IDaikin host : hosts.values()) {
			host.readDaikinState();

			for (DaikinBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					DaikinBindingConfig bindingConfig = provider.getBindingConfig(itemName);
					if (!bindingConfig.getId().equals(host.getId()))
						continue;
					
					DaikinCommandType commandType = bindingConfig.getCommandType();
					eventPublisher.postUpdate(itemName, getState(host, commandType));
				}
			}
		}
	}
	
	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}	

	private Mode getModeEnum(String command) {
		if (isInteger(command)) {
			int modeValue = Integer.parseInt(command);
			for (Mode mode : Mode.values()) {
				if (mode.ordinal() == modeValue)
					return mode;
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Daikin mode: " + command);
	}
	
	private Fan getFanEnum(String command) {
		if (isInteger(command)) {
			int fanValue = Integer.parseInt(command);
			for (Fan fan : Fan.values()) {
				if (fan.ordinal() == fanValue)
					return fan;
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Daikin Fan: " + command);
	}
	
	private FanDirection getFanDirectionEnum(String command) {
		if (isInteger(command)) {
			int fanDirectionValue = Integer.parseInt(command);
			for (FanDirection fanDirection : FanDirection.values()) {
				if (fanDirection.ordinal() == fanDirectionValue)
					return fanDirection;
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Daikin FanDirection: " + command);
	}
	
	private Timer getTimerEnum(String command) {
		if (isInteger(command)) {
			int timerValue = Integer.parseInt(command);
			for (Timer timer : Timer.values()) {
				if (timer.ordinal() == timerValue)
					return timer;
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Daikin FanDirection: " + command);
	}
	
	private State getState(IDaikin host, DaikinCommandType commandType) {
		switch (commandType) {
			case POWER:
				return host.isOn() ? OnOffType.ON : OnOffType.OFF;
			case MODE:
				return new DecimalType(host.getMode().ordinal());
			case TEMP:
				return new DecimalType(host.getTargetTemperature());
			case FAN:
				return new DecimalType(host.getFan().ordinal());
			case SWING:
				return new DecimalType(host.getFanDirection().ordinal());
			case TEMPIN:
				return new DecimalType(host.getInsideTemperature());
			case TIMER:
				return new DecimalType(host.getTimer().ordinal());
			case TEMPOUT:
				return new DecimalType(host.getOutsideTemperature());
			case HUMIDITYIN:
				return new DecimalType(host.getTargetHumidity());
			default:
				throw new RuntimeException("Unsupported command type: " + commandType);
		}
	}
	
	private void setState(IDaikin host, DaikinCommandType commandType, Command value) {
		switch (commandType) {
			case POWER:
				host.setOn(value.equals(OnOffType.ON));
				break;
			case MODE:
				host.setMode(getModeEnum(value.toString()));
				break;
			case TEMP:
				host.setTargetTemperature(((DecimalType)value).floatValue());
				break;
			case FAN:
				host.setFan(getFanEnum(value.toString()));
				break;
			case SWING:
				host.setFanDirection(getFanDirectionEnum(value.toString()));
				break;
			case TIMER:
				host.setTimer(getTimerEnum(value.toString()));
				break;
				
			default:
				throw new RuntimeException("Unsupported command type: " + commandType);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = (String) config.get(key);
			
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				if (key.equals(CONFIG_KEY_REFRESH)) {
					refreshInterval = Long.parseLong(value); 
					continue;
				}				
								
				String[] keyParts = key.split("\\.");
				String hostId = keyParts[0];
				if (!hosts.containsKey(hostId)) {
					int index = value.indexOf("@");
					String connectionType = value.substring(0, index);
					String host = value.substring(index + 1);
					
			    	if (connectionType.toUpperCase().equals("WIRELESS")) {
						IDaikin newHost = DaikinFactory.createWirelessDaikin(hostId, host, 0);
						hosts.put(hostId, newHost);
			    	} else if (connectionType.toUpperCase().equals("WIRED")) {
						IDaikin newHost = DaikinFactory.createWiredDaikin(hostId, host, 0);
						hosts.put(hostId, newHost);
			    	} else 
						throw new RuntimeException("Unsupported connectionType: " + connectionType);
				}
			}
			
			// start the refresh thread
			setProperlyConfigured(true);
		}
	}	
}
