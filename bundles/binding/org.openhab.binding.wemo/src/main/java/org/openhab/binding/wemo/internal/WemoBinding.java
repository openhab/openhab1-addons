/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wemo.internal;

import java.util.Dictionary;

import com.palominolabs.wemo.*;

import org.openhab.binding.wemo.WemoBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Hans-Jörg Merk
 * @since 1.5.0
 */
public class WemoBinding extends AbstractActiveBinding<WemoBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(WemoBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the Wemo
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	
	public WemoBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Wemo Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("execute() method is called!");
		
		for (WemoBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String switchFriendlyName = provider.getWemoFriendlyName(itemName);
				logger.info("wemo: wemo switch {} state will be updated", switchFriendlyName);
				
				try (InsightSwitchFinder insightSwitchFinder = new InsightSwitchFinder(switchFriendlyName)) {
				    if (!insightSwitchFinder.findSwitches()) {
				        throw new IllegalStateException("Unable to find switches");
				    }
				 
				    InsightSwitch insightSwitch = insightSwitchFinder.getSwitch(switchFriendlyName);
				 
				    while (true) {
				    	PowerUsage wemoSwitchPower = insightSwitch.getPowerUsage();
				    	if (wemoSwitchPower.equals("ON")) {
				    		State state = OnOffType.valueOf("ON");
							logger.info("wemo: new state for item {} = {}", itemName, state);
							eventPublisher.postUpdate(itemName, state);
				    	}else if (wemoSwitchPower.equals("OFF")) {
				    		State state = OnOffType.valueOf("OFF");
							logger.info("wemo: new state for item {} = {}", itemName, state);
							eventPublisher.postUpdate(itemName, state);
				    	} else if (! wemoSwitchPower.equals("ON") || wemoSwitchPower.equals("OFF")) {
							logger.info("wemo: new state for item {} = {}", itemName, wemoSwitchPower);
				    	}
				    }
				} catch (Exception e) {
		            logger.error("wemo: Unable to find switch '{}'",switchFriendlyName, e);
		        }
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand() is called!");

		for (WemoBindingProvider provider : providers) {

			String switchFriendlyName = provider.getWemoFriendlyName(itemName);

			try (InsightSwitchFinder insightSwitchFinder = new InsightSwitchFinder(switchFriendlyName)) {
			    if (!insightSwitchFinder.findSwitches()) {
			        throw new IllegalStateException("Unable to find switches");
			    }
			 
			    InsightSwitch insightSwitch = insightSwitchFinder.getSwitch(switchFriendlyName);
			 
			    while (true) {
				    if (OnOffType.ON.equals(command)) {
						logger.info("wemo: wemo switch {} is set to ON", switchFriendlyName);
				    	insightSwitch.switchOn();
				    	
				    } else if (OnOffType.OFF.equals(command)) {
						logger.info("wemo: wemo switch {} is set to OFF", switchFriendlyName);
				        insightSwitch.switchOff();
				    }
			    }
			} catch (Exception e) {
	            logger.error("wemo: Unable to finde switch '{}'",switchFriendlyName, e);
	        }
		}

	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.debug("internalReceiveCommand() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			setProperlyConfigured(true);
		}
	}
	

}
