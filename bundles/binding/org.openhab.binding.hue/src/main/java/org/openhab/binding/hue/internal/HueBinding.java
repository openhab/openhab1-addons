/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.hue.HueBindingProvider;
import org.openhab.binding.hue.internal.HueBindingConfig.BindingType;
import org.openhab.binding.hue.internal.data.HueSettings;
import org.openhab.binding.hue.internal.hardware.HueBridge;
import org.openhab.binding.hue.internal.hardware.HueBulb;
import org.openhab.binding.hue.internal.tools.SsdpDiscovery;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding is able to do the following tasks with the Philips hue system:
 * <ul>
 * <li>Switching bulbs on and off.</li>
 * <li>Change color temperature of a bulb, what results in a white color.</li>
 * <li>Change the brightness of a bulb without changing the color.</li>
 * <li>Change the RGB values of a bulb.</li>
 * </ul>
 * 
 * @author Roman Hartmann
 * @author Jos Schering
 * @since 1.2.0
 */
public class HueBinding extends AbstractActiveBinding<HueBindingProvider> implements ManagedService {

	static final Logger logger = LoggerFactory.getLogger(HueBinding.class);

	/** refresh interval is only set by configuration */
	private long refreshInterval;
	
	private HueBridge activeBridge = null;
	private String bridgeIP = null;
	private String bridgeSecret = "openHAB";

	// Caches all bulbs controlled to prevent the recreation of the bulbs which
	// triggers a rereading of the settings from the bridge which is very
	// expensive.
	private HashMap<Integer, HueBulb> bulbCache = new HashMap<Integer, HueBulb>();

	/**
	 * Default constructor for the Hue binding.
	 */
	public HueBinding() {
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
    	return "Hue Refresh Service";
    }
    
	/**
	 * Get current hue settings of the bulbs and update the items that are connected with the bulb.
	 * The refreshinterval determines the polling frequency.
	 */
	@Override
	public void execute() {
		if (activeBridge != null)
		{
			// Get settings and update the bulbs
			// Observation : If the power of a hue lamp is removed, the status is not updated in hue hub.
			// The heartbeat functionality should fix this, but 
			HueSettings settings = activeBridge.getSettings();
			for (int i = 1; i <= settings.getCount(); i++) {
				HueBulb bulb = bulbCache.get(i);
				if (bulb == null) {
					bulb = new HueBulb(activeBridge, i);
					bulbCache.put(i, bulb);
				}
				bulb.getStatus(settings);
			}
			
			// Update the items that are linked with the bulbs.
			// Multiple items of different types can be linked to one bulb.
			for (HueBindingProvider provider : this.providers) {
				for (String hueItemName : provider.getInBindingItemNames()) {
					HueBindingConfig deviceConfig = getConfigForItemName(hueItemName);
					
					if (deviceConfig != null) {
						HueBulb bulb = bulbCache.get(deviceConfig.getDeviceNumber());
						if (bulb != null) {
						
							// Enhancement: only send a postUpdate for items that have changed.
							// Tried to use item.getState() as found in enOcean binding, but the state value was always uninitialized
							// State actualState = provider.getItem(itemName).getState(); --> always return Uninitialized
							// Workaround for now, store the OnOff state in deviceConfig 
							//
							if ((bulb.getIsOn() == true) && (bulb.getIsReachable() == true)) {
								if ((deviceConfig.itemStateOnOffType == null) || (deviceConfig.itemStateOnOffType.equals(OnOffType.ON) == false)) {
									eventPublisher.postUpdate(hueItemName, OnOffType.ON);
									deviceConfig.itemStateOnOffType = OnOffType.ON;
								}
							} else { 
								if ((deviceConfig.itemStateOnOffType == null) || (deviceConfig.itemStateOnOffType.equals(OnOffType.OFF) == false)) {
									eventPublisher.postUpdate(hueItemName, OnOffType.OFF);
									deviceConfig.itemStateOnOffType = OnOffType.OFF;
								}
							}
							
							if (deviceConfig.getType().equals(BindingType.brightness)) {
								if ((bulb.getIsOn() == true) && (bulb.getIsReachable() == true)) {
									// Only postUpdate when bulb is on, otherwise dimmer item is not retaining state and shows to max brightness value
									PercentType newPercent = new PercentType((int)Math.round((bulb.getBrightness() * (double)100) / (double)255));
									if ((deviceConfig.itemStatePercentType == null) || (deviceConfig.itemStatePercentType.equals(newPercent) == false)) {
										eventPublisher.postUpdate(hueItemName, newPercent);
										deviceConfig.itemStatePercentType = newPercent;
									}
								}									
							} else if (deviceConfig.getType().equals(BindingType.rgb)) {
								if ((bulb.getIsOn() == true) && (bulb.getIsReachable() == true)) {
									// Only postUpdate when bulb is on, otherwise color item is not retaining state and shows to max brightness value
									DecimalType decimalHue = new DecimalType(bulb.getHue() / (double)182);
									PercentType percentBrightness = new PercentType((int)Math.round((bulb.getBrightness() * (double)100) / (double)255));
									PercentType percentSaturation = new PercentType((int)Math.round((bulb.getSaturation() * (double)100) / (double)255));
									HSBType newHsb = new HSBType(decimalHue, percentSaturation, percentBrightness);
									if ((deviceConfig.itemStateHSBType == null) || (deviceConfig.itemStateHSBType.equals(newHsb) == false)) {
										eventPublisher.postUpdate(hueItemName, newHsb);
										deviceConfig.itemStateHSBType = newHsb;
									}
								}									
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		super.internalReceiveCommand(itemName, command);

		logger.debug("Hue binding received command '" + command
				+ "' for item '" + itemName + "'");

		if (activeBridge != null) {
			computeCommandForItemOnBridge(command, itemName, activeBridge);
		} else {
			logger.warn("Hue binding skipped command because no Hue bridge is connected.");
		}

	}

	/**
	 * Checks whether the command is for one of the configured Hue bulbs. If
	 * this is the case, the command is translated to the corresponding action
	 * which is then sent to the given bulb.
	 * 
	 * @param command
	 *            The command from the openHAB bus.
	 * @param itemName
	 *            The name of the targeted item.
	 * @param bridge
	 *            The Hue bridge the Hue bulb is connected to
	 */
	private void computeCommandForItemOnBridge(Command command,
			String itemName, HueBridge bridge) {
		HueBindingConfig deviceConfig = getConfigForItemName(itemName);

		if (deviceConfig == null) {
			return;
		}

		HueBulb bulb = bulbCache.get(deviceConfig.getDeviceNumber());
		if (bulb == null) {
			bulb = new HueBulb(bridge, deviceConfig.getDeviceNumber());
			bulbCache.put(deviceConfig.getDeviceNumber(), bulb);
		}

		if (command instanceof OnOffType) {
			bulb.switchOn(OnOffType.ON.equals(command));
		}

		if (command instanceof HSBType) {
			HSBType hsbCommand = (HSBType) command;
			DecimalType hue = hsbCommand.getHue();
			PercentType sat = hsbCommand.getSaturation();
			PercentType bri = hsbCommand.getBrightness();
			bulb.colorizeByHSB(hue.doubleValue() / 360,
					sat.doubleValue() / 100, bri.doubleValue() / 100);
		}

		if (deviceConfig.getType().equals(BindingType.brightness)
				|| deviceConfig.getType().equals(BindingType.rgb)) {
			if (IncreaseDecreaseType.INCREASE.equals(command)) {
				int resultingValue = bulb.increaseBrightness(deviceConfig.getStepSize());
				eventPublisher.postUpdate(itemName, new PercentType(resultingValue));
			} else if (IncreaseDecreaseType.DECREASE.equals(command)) {
				int resultingValue = bulb.decreaseBrightness(deviceConfig.getStepSize());
				eventPublisher.postUpdate(itemName, new PercentType(resultingValue));
			} else if ((command instanceof PercentType) && !(command instanceof HSBType)) {
				bulb.setBrightness((int)Math.round((double)255 / (double)100 * ((PercentType) command).intValue()));
			}
		}

		if (deviceConfig.getType().equals(BindingType.colorTemperature)) {
			if (IncreaseDecreaseType.INCREASE.equals(command)) {
				bulb.increaseColorTemperature(deviceConfig.getStepSize());
			} else if (IncreaseDecreaseType.DECREASE.equals(command)) {
				bulb.decreaseColorTemperature(deviceConfig.getStepSize());
			} else if (command instanceof PercentType) {
				bulb.setColorTemperature((int)Math.round((((double)346 / (double)100) * ((PercentType) command).intValue()) + 154));
			}
		}

	}

	/**
	 * Lookup of the configuration of the named item.
	 * 
	 * @param itemName
	 *            The name of the item.
	 * @return The configuration, null otherwise.
	 */
	private HueBindingConfig getConfigForItemName(String itemName) {
		for (HueBindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			String ip = (String) config.get("ip");
			if (StringUtils.isNotBlank(ip)) {
				this.bridgeIP = ip;
			} else {
				try {
					this.bridgeIP = new SsdpDiscovery()
							.findIpForResponseKeywords("description.xml",
									"FreeRTOS");
				} catch (IOException e) {
					logger.warn("Could not find hue bridge automatically. Please make sure it is switched on and connected to the same network as openHAB. If it permanently fails you may configure the IP address of your hue bridge manually in the openHAB configuration.");
				}
			}
			String secret = (String) config.get("secret");
			if (StringUtils.isNotBlank(secret)) {
				this.bridgeSecret = secret;
			}

			// connect the Hue bridge with the new configs
			if(this.bridgeIP!=null) {
				activeBridge = new HueBridge(bridgeIP, bridgeSecret);
				activeBridge.pairBridgeIfNecessary();
			}
			
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
				
				// RefreshInterval is specified in openhap.cfg, therefore enable polling
				setProperlyConfigured(true);
			}
		}

	}

}
