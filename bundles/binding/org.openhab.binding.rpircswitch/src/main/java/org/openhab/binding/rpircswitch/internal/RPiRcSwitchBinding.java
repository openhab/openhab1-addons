/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rpircswitch.internal;

import java.util.Dictionary;
import java.util.EnumSet;
import java.util.Enumeration;

import org.openhab.binding.rpircswitch.RPiRcSwitchBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.impl.PinImpl;

import de.pi3g.pi.rcswitch.RCSwitch;

/**
 * <p>
 * Binding listening OpenHAB bus and send commands to RC switches when command
 * is received.
 * 
 * @author Matthias Röckl
 * @since 1.8.0
 */
public class RPiRcSwitchBinding extends
		AbstractBinding<RPiRcSwitchBindingProvider> implements ManagedService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RPiRcSwitchBinding.class);

	private RCSwitch transmitter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.core.binding.AbstractBinding#activate()
	 */
	@Override
	public void activate() {
		LOGGER.debug("Raspberry Pi RC Switch activated");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.core.binding.AbstractBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		LOGGER.debug("Raspberry Pi RC Switch deactivated");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.core.binding.AbstractBinding#internalReceiveCommand(java.
	 * lang.String, org.openhab.core.types.Command)
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// Command has been received
		if (itemName != null) {

			// Get the configuration
			RPiRcSwitchBindingConfig config = this
					.getConfigForItemName(itemName);

			if (config == null) {
				LOGGER.error("No configuration for Raspberry Pi RC Switch available.");
				return;
			}

			// Only OnOffType is supported
			if (command instanceof OnOffType) {
				if (this.transmitter != null) {
					if (OnOffType.ON.equals(command)) {
						this.transmitter.switchOn(config.getGroupAddress(),
								config.getDeviceAddress());
					} else {
						this.transmitter.switchOff(config.getGroupAddress(),
								config.getDeviceAddress());
					}
				} else {
					LOGGER.error("Transmitter has not been initialized. Please configure it correctly in your OpenHAB configuration.");
				}
			} else {
				LOGGER.error("Only On/Off commands are supported.");
			}
		}
	}

	/**
	 * Returns the configuration for the item with the given name.
	 * 
	 * @param itemName
	 *            the name of the item
	 * @return the configuration to use
	 */
	protected RPiRcSwitchBindingConfig getConfigForItemName(String itemName) {
		for (RPiRcSwitchBindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		LOGGER.debug("New configuration received");
		if (config != null) {
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				if ("service.pid".equals(key)) {
					continue;
				}

				Object o = config.get("gpioPin");
				if (o instanceof String) {
					String pinAddressString = (String) o;

					try {
						int pinAddress = Integer.parseInt(pinAddressString);
						PinImpl gpioPin = new PinImpl(RaspiGpioProvider.NAME,
								pinAddress, "GPIO_" + pinAddress, EnumSet.of(
										PinMode.DIGITAL_INPUT,
										PinMode.DIGITAL_OUTPUT),
								PinPullResistance.all());
						this.transmitter = new RCSwitch(gpioPin);
					} catch (NumberFormatException e) {
						LOGGER.error("Invalid configuration. Please provide an Integer value in the 'gpioPin' configuration");
					}
				}
			}
		}
	}

}
