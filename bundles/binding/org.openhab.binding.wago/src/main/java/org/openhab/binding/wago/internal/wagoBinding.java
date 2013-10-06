/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.wago.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.wago.wagoBindingProvider;
import org.openhab.binding.wago.internal.wagoGenericBindingProvider.wagoBindingConfig;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
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
 * @author Kaltofen
 * @since 1.3.0
 */
public class wagoBinding extends AbstractActiveBinding<wagoBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(wagoBinding.class);
	
	private static final Pattern EXTRACT_CONFIG_WAGO_PATTERN = Pattern.compile("^(.*?)\\.(ip|modbus|ftp|username|password)$");

	
	/** 
	 * the refresh interval which is used to poll values from the wago
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	private static Map<String, FBCoupler> couplers = Collections.synchronizedMap(new HashMap<String, FBCoupler>());
	
	private FBCoupler getCoupler(String couplerName) {
		return Collections.synchronizedMap(couplers).get(couplerName);
	}
	
	public wagoBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
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
		return "wago Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		for(FBCoupler coupler : Collections.synchronizedMap(couplers).values()) {
			coupler.update(this);
		}
	}
	
	public void updateItem(String itemName, String couplerName, int module, boolean states[]) {
		for (wagoBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				wagoBindingConfig conf = provider.getConfig(itemName);
				if (conf.couplerName.equals(couplerName) && conf.module == module) {
					State currentState = conf.getItemState();
					State newState = conf.translateBoolean2State(states[conf.channel]);
					if(!newState.equals(currentState)) {
						eventPublisher.postUpdate(itemName, newState);
					}
				}
			}
		}
	}
	
	public void updateItemPWM(String itemName, String couplerName, int module, int values[]) {
		for (wagoBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				wagoBindingConfig conf = provider.getConfig(itemName);
				if (conf.couplerName.equals(couplerName) && conf.module == module) {
					State currentState = conf.getItemState();
					State newState;
					if(conf.getItem() instanceof DimmerItem) {
						newState = new PercentType((int) ((float)values[conf.channel] / 1023 * 100));
					} else if(conf.getItem() instanceof SwitchItem) {
						if(values[conf.channel] == 0) {
							newState = OnOffType.OFF;
						} else {
							newState = OnOffType.ON;
						}
					} else {
						logger.debug("unsupported itemtype");
						return;
					}
					if(!newState.equals(currentState)) {
						eventPublisher.postUpdate(itemName, newState);
					}
				}
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		
		for(wagoBindingProvider provider : providers) {
			if(provider.providesBindingFor(itemName)) {
				wagoBindingConfig conf = provider.getConfig(itemName);
				FBCoupler coupler = getCoupler(conf.couplerName);
				coupler.executeCommand(command, conf);
			}
		}
		
		logger.debug("internalReceiveCommand() is called!");
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			/*String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}*/
			
			if(config != null) {
				Enumeration keys = config.keys();
				while(keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					
					if ("service.pid".equals(key)) {
						continue;
					}
					
					Matcher matcher = EXTRACT_CONFIG_WAGO_PATTERN.matcher(key);
					if(!matcher.matches()) {
						if("refresh".equals(key)) {
							refreshInterval = Integer.valueOf((String) config.get(key));
						} else {
							logger.debug("unexpected configuration given. \"" + key + "\" does not follow the expected pattern.");
						}
						continue;
					}
					
					matcher.reset();
					matcher.find();
					
					String couplerName = matcher.group(1);
					FBCoupler coupler = Collections.synchronizedMap(couplers).get(couplerName);
					if(coupler == null) {
						coupler = new FBCoupler(couplerName);
						Collections.synchronizedMap(couplers).put(couplerName, coupler);
					}
					
					String attrName = matcher.group(2);
					String value = (String) config.get(key);
					switch(attrName) {
					case "ip": coupler.setIp(value); break;
					case "modbus": coupler.setModbus(Integer.parseInt(value)); break;
					case "ftp": coupler.setFTP(Integer.parseInt(value)); break;
					case "username": coupler.setUsername(value); break;
					case "password": coupler.setPassword(value); break;
					}
				}
				
				for(FBCoupler coupler : Collections.synchronizedMap(couplers).values()) {
					coupler.setup();
				}
			}

			setProperlyConfigured(true);
		}
	}
	
	public Collection<String> getItemNames() {
		Collection<String> items = null;
		for (BindingProvider provider : providers) {
			if (items == null)
				items = provider.getItemNames();
			else
				items.addAll(provider.getItemNames());
		}
		return items;
	}
}
