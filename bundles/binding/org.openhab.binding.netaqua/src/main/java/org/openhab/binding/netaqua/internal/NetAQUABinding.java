/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netaqua.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Calendar;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.openhab.binding.netaqua.NetAQUABindingProvider;
import org.openhab.binding.netaqua.internal.messages.SystemsRequest;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.library.types.OnOffType;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that gets measurements from the Enphase Energy API every couple of minutes.
 * 
 * @author Markus Fritze
 * @author Andreas Brenk
 * @author Thomas.Eichstaedt-Engelen
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class NetAQUABinding extends AbstractActiveBinding<NetAQUABindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(NetAQUABinding.class);

    protected static final String CONFIG_REFRESH = "refresh";
    protected static final String CONFIG_LONGREFRESH = "longrefresh";
    protected static final String CONFIG_SERVER = "server";

	private String	server;
	private Map<String, State> currentValues;
	private int updateEverything;

    /**
     * The refresh interval which is used to poll values from the netAqua. A limited number of parameters are updated
     * frequently (watering status, time, current zone), while the rest is updated only rarely (programming settings, warnings, etc)
     */
    private long refreshInterval = 60000;
    private long longRefreshInterval = 60000 * 15;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        this.updateEverything = 0;
        return "netAQUA Refresh Service";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return this.refreshInterval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        logger.debug("Querying netAQUA");
		try {
			this.currentValues = new HashMap<String, State>();
			Map<String, String> response = new HashMap<String, String>();

			final SystemsRequest requestStatus = new SystemsRequest(this.server, "get_status.cgi?output=all");
//			logger.debug("requestStatus: {} as {}", requestStatus, requestStatus.prepare());

			final Map<String, String> responseStatus = requestStatus.execute();
//			logger.debug("responseStatus: {}", responseStatus);

			this.updateEverything = this.updateEverything + 1;
			if(this.updateEverything >= (this.longRefreshInterval / this.refreshInterval)) {
				this.updateEverything = 0;

				final SystemsRequest requestControl = new SystemsRequest(this.server, "get_control.cgi?output=all");
	//			logger.debug("requestControl: {} as {}", requestControl, requestControl.prepare());

				final Map<String, String> responseControl = requestControl.execute();
	//			logger.debug("responseControl: {}", responseControl);
				response.putAll(responseControl);

				final SystemsRequest requestConfig = new SystemsRequest(this.server, "get_config.cgi?output=all");
	//			logger.debug("requestConfig: {} as {}", requestConfig, requestConfig.prepare());

				final Map<String, String> responseConfig = requestConfig.execute();
	//			logger.debug("responseConfig: {}", responseConfig);
				response.putAll(responseConfig);

				// Merge the two lines into one
				responseStatus.put("watering_notifications", responseStatus.get("watering_modified1") + " " + responseStatus.get("watering_modified2"));

			} else {
				Map<String, String> shortResponse = new HashMap<String, String>();
				// These are the status keys, which are updated frequently. ALL others are updated based on the longRefreshInterval
				String[] shortKeys = {"watering", "cycle_nbr", "zone_nbr", "time_left", "last_cycle", "next_cycle"};
				for(String key : shortKeys) {
					shortResponse.put(key, responseStatus.get(key));
				}
				responseStatus.clear();
				responseStatus.putAll(shortResponse);
			}

			response.putAll(responseStatus);
			logger.debug("response: {}", response);

			for (final NetAQUABindingProvider provider : this.providers) {
				for (final String itemName : provider.getItemNames()) {

					final NetAQUAItemType itemType = provider.getItemType(itemName);
					final Integer itemIndex = provider.getItemIndex(itemName);
					final Integer itemIndexB = provider.getItemIndexB(itemName);
					String	keyStr = null;
					final String value;
					switch(itemType) {
					case CYCLE_DURATION:
						keyStr = String.format("a_c%c_dur%02d", 96+itemIndex, itemIndexB);
						break;
					case CYCLE_ENABLED:
					case CYCLE_START_HOUR12:
					case CYCLE_START_MINUTE:
					case CYCLE_START_AMPM:
						keyStr = itemType.getItem() + String.format("%d_%d", itemIndex, itemIndexB);
						break;
					case ZONE_NAME:
						keyStr = itemType.getItem() + String.format("%02d", itemIndex);
						break;
					default:
						if (itemIndex >= 0) {
							keyStr = itemType.getItem() + String.format("%d", itemIndex);
						} else {
							keyStr = itemType.getItem();
						}
					}
					value = response.get(keyStr);
					if(value == null) {
						continue;
					}
					State state = null;
					switch(itemType) {
					case CYCLE_FREQUENCY:
					case CYCLE_DURATION:
					case CYCLE_START_HOUR12:
					case CYCLE_START_MINUTE:
					case LOCAL_TEMPERATURE:
					case SYSTEM_TEMPERATURE:
						state = new DecimalType(Double.parseDouble(value.replaceAll("[^\\d.]", "")));
						break;
					case CYCLE_START_AMPM:
						if (value.equals("0")) {
							state = OnOffType.OFF;
						} else {
							state = OnOffType.ON;
						}
						break;
					case CYCLE_ENABLED:
						if (value.equals("false")) {
							state = OnOffType.OFF;
						} else {
							state = OnOffType.ON;
						}
						break;
					case CURRENTLY_WATERING:
						if (value.equals("No")) {
							state = OnOffType.OFF;
						} else {
							state = OnOffType.ON;
						}
						break;
					default:
						state = new StringType(value);
						break;
					}
					final String	valueStoreKeyStr = String.format("%s:%d:%d", itemType, itemIndex, itemIndexB);
//					if(this.currentValues.containsKey(valueStoreKeyStr) && this.currentValues.get(valueStoreKeyStr).equals(state)) {
//						continue;
//					}
					this.currentValues.put(valueStoreKeyStr, state);

					if (state != null) {
						this.eventPublisher.postUpdate(itemName, state);
					}
				}
			}
		} catch (NetAQUAException ne) {
			logger.error(ne.getMessage());
		}
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand {} with {}",itemName,command);
		for (final NetAQUABindingProvider provider : this.providers) {
			if (provider.providesBindingFor(itemName)) {
				final NetAQUAItemType itemType = provider.getItemType(itemName);
				final Integer itemIndex = provider.getItemIndex(itemName);
				final Integer itemIndexB = provider.getItemIndexB(itemName);

				// The default is to abort an sprinkler program
				String	requestStr = "apply_control_abort.cgi?checkbox_confirm_abort=1";
				switch(itemType) {
				case CURRENTLY_WATERING:
					if(command.equals(OnOffType.ON)) {	// Can't turn the sprinkler just "ON"
						requestStr = null;
						this.eventPublisher.postCommand(itemName, OnOffType.OFF);
					}
					break;
				case RUN_CYCLE:
					if(command.equals(OnOffType.ON))
						requestStr = "apply_control_manual_starts.cgi?mode=1&cycle_number=" + (itemIndex-1);
					break;
				case RUN_ZONE:
					if(command.equals(OnOffType.ON))
						requestStr = "apply_control_manual_starts.cgi?mode=2&zone_number=" + itemIndex + "&manual_duration=1";
					break;
				case TEST_ALL_ZONES:
					if(command.equals(OnOffType.ON))
						requestStr = "apply_control_manual_starts.cgi?mode=3";
					break;
				case TEST_NEXT_ZONE:
					if(command.equals(OnOffType.ON))
						requestStr = "apply_control_manual_starts.cgi?mode=4";
					break;

				case CYCLE_FREQUENCY:
				case CYCLE_ENABLED:
				case CYCLE_START_HOUR12:
				case CYCLE_START_MINUTE:
				case CYCLE_START_AMPM:
				case CYCLE_DURATION:
					final String	valueStoreKeyStr = String.format("%s:%d:%d", itemType, itemIndex, itemIndexB);
					this.currentValues.put(valueStoreKeyStr, (State)command);
					
					List<String> list = new ArrayList<String>();
					int			val;
					String		valStr;
					State		state;
					Integer		cycle = itemIndex;	// modified cycle

					list.add("cycle="+cycle);
					state = this.currentValues.get("CYCLE_FREQUENCY:"+cycle+":-1");
					valStr = (state == null) ? "0" : state.toString();
					list.add("frequencyx="+valStr);

					list.add("time_format=0");
					for(int x=1; x<=4; x=x+1) {
						state = this.currentValues.get("CYCLE_ENABLED:"+cycle+":"+x);
						val = (state == null || state == OnOffType.OFF) ? 0 : 1;
						list.add("enablex_"+x+"="+val);

						state = this.currentValues.get("CYCLE_START_HOUR12:"+cycle+":"+x);
						valStr = (state == null) ? "0" : state.toString();
						list.add("hour12x_"+x+"="+valStr);

						state = this.currentValues.get("CYCLE_START_MINUTE:"+cycle+":"+x);
						valStr = (state == null) ? "0" : state.toString();
						list.add("minutex_"+x+"="+valStr);

						state = this.currentValues.get("CYCLE_START_AMPM:"+cycle+":"+x);
						val = (state == null || state == OnOffType.OFF) ? 0 : 1;
						list.add("ampmx_"+x+"="+val);
					}
					for(int x=1; x<=45; x=x+1) {
						state = this.currentValues.get("CYCLE_DURATION:"+cycle+":"+(x-1));
						if(state == null) state = new DecimalType(0);
						list.add("c_dur"+x+"="+state);
					}
					requestStr = "apply_control_watering_cycle.cgi?";
					for(String item : list)
						requestStr += item + "&";
					requestStr = requestStr.substring(0, requestStr.length()-1);
					break;

				default:
					logger.debug("Unknown item: {}.{}.{}", itemType, itemIndex, itemIndexB);
					requestStr = null;
					break;
				}
				if (requestStr != null) {
					final SystemsRequest requestConfig = new SystemsRequest(this.server, requestStr);
					logger.debug("requestConfig: {}", requestConfig.prepare());

					final Map<String, String> responseConfig = requestConfig.execute();
					logger.debug("responseConfig: {}", responseConfig);
				}
			}
		}
	}
    
    /**
     * {@inheritDoc}
     */
/*    @Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.debug("internalReceiveUpdate {} with {}",itemName,newState);
	}
*/
    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(final Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            Enumeration<String> configKeys = config.keys();
            while (configKeys.hasMoreElements()) {
                String configKey = (String) configKeys.nextElement();
                if ("service.pid".equals(configKey)) {
                	continue;
                }
                
                String value = (String) config.get(configKey);
				logger.debug("Set config {} to {}",configKey,value);
				if(CONFIG_REFRESH.equals(configKey)) {
					if (isNotBlank(value)) {
						this.refreshInterval = Long.parseLong(value);
					}
				}
				else if(CONFIG_LONGREFRESH.equals(configKey)) {
					if (isNotBlank(value)) {
						this.longRefreshInterval = Long.parseLong(value);
					}
				}
                else if (CONFIG_SERVER.equals(configKey)) {
                    this.server = value;
                }
                else {
                    throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
                }
            }

            setProperlyConfigured(true);
        }
    }

}
