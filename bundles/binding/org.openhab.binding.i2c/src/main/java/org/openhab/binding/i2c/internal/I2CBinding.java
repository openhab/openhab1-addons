/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.i2c.internal;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.i2c.I2CBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Diego A. Fliess
 * @since 1.8.0
 */
public class I2CBinding extends AbstractActiveBinding<I2CBindingProvider> implements GpioPinListenerDigital {

	private static final Logger logger = LoggerFactory.getLogger(I2CBinding.class);
	
	private final GpioController gpio = GpioFactory.getInstance();
	
	private Map<String, GpioPin> gpioPins = new HashMap<>();
	
	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the i2c server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public I2CBinding() {
	}

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext,
			final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;
		
		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// read further config parameters here ...
		logger.debug("i2c activated " + this.hashCode());
		setProperlyConfigured(true);
		logger.debug("i2c activated and ProperlyConfigured " + this.hashCode());
	}


	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
		logger.debug("i2c modified");
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		logger.debug("i2c deactivated");
		gpio.shutdown();	
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "i2c Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}
	
	@Override
	public void addBindingProvider(I2CBindingProvider provider) {
		super.addBindingProvider(provider);
		
		/*first call contains all, better use activate ? if you have providers*/
		logger.debug("addBindingProvider: " + Arrays.toString(provider.getItemNames().toArray()));
		for (String itemName: provider.getItemNames()) {
			bindGpioPin(provider, itemName);
		}
	}

	
	@Override
	public void removeBindingProvider(I2CBindingProvider provider) {		
		super.removeBindingProvider(provider);
		/*shutdown call contains all better use deactivate*/
		logger.debug("removeBindingProvider: " + Arrays.toString(provider.getItemNames().toArray()));
		for (String itemName: provider.getItemNames()) {
			unBindGpioPin(itemName);
		}
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof I2CBindingProvider) {
			if (provider.getItemNames().contains(itemName)) {
				 bindGpioPin((I2CBindingProvider) provider, itemName);
				logger.debug("bindingChanged item bound " + itemName + " - " + Arrays.toString(provider.getItemNames().toArray()));
			} else {
				unBindGpioPin(itemName);
				logger.debug("bindingChanged item unbound " + itemName + " - " + Arrays.toString(provider.getItemNames().toArray()));
			}
		}
		super.bindingChanged(provider, itemName);
	}

	private void bindGpioPin(I2CBindingProvider provider, String itemName) {
		try {
			final MCP23017GpioProvider gpioProvider = new MCP23017GpioProvider(I2CBus.BUS_1, provider.getBusAddress(itemName));
			GpioPin pin;
			
			if (provider.getPinMode(itemName).equals(PinMode.DIGITAL_OUTPUT)) {
				pin = gpio.provisionDigitalOutputPin(gpioProvider, provider.getPin(itemName), itemName, provider.getDefaultState(itemName));
				logger.debug("Provisioned Digital Output for " + itemName );
			} else if (provider.getPinMode(itemName).equals(PinMode.DIGITAL_INPUT))  {
				
				 /** Note: MCP23017 has no internal pull-down, so I used pull-up and 
				 *  inverted the button reading logic with a "not" 
				 **/
				pin = gpio.provisionDigitalInputPin(gpioProvider, provider.getPin(itemName),itemName,PinPullResistance.PULL_UP);

				pin.addListener(this);
				
				logger.debug("Provisioned Digital Input for " + itemName );
			} else {
				throw new IllegalArgumentException("Invalid Pin Mode in config " + provider.getPinMode(itemName).name());
			}
			gpioPins.put(itemName,pin);
		} catch (IOException e) {
			logger.error("IO ERROR " + e.getMessage());
		}
	}

	private void unBindGpioPin(String itemName) {
		GpioPin pin = gpioPins.remove(itemName);
		pin.removeAllListeners();
		pin.unexport();
		logger.debug("unbound " + itemName );
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		
		// the configuration is guaranteed not to be null, because the component
		// definition has the
		// configuration-policy set to require. If set to 'optional' then the
		// configuration may be null
		if (command instanceof OnOffType) {
			final OnOffType switchCommand = (OnOffType) command;
			
			for (I2CBindingProvider provider : this.providers) {
				if (provider.providesBindingFor(itemName)) {

					//Map for converting OFF -> LOW and ON->HIGH
					EnumMap<OnOffType, PinState> states = new EnumMap<OnOffType, PinState>(OnOffType.class);
					states.put(OnOffType.OFF, PinState.LOW);
					states.put(OnOffType.ON, PinState.HIGH);

					gpio.setState(states.get(switchCommand), (GpioPinDigitalOutput) gpioPins.get(itemName));
				}
			}
		}

	}

	/** Note: MCP23017 has no internal pull-down, so I used pull-up and 
	 *  inverted the button reading logic with a "not" 
	 **/
	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		
		
	
		//Inverted state map (same as PinState.getInverseState(event.getState()))
		EnumMap<PinState,OpenClosedType> states = new EnumMap<PinState,OpenClosedType>(PinState.class);
		states.put(PinState.LOW,OpenClosedType.OPEN);
		states.put(PinState.HIGH,OpenClosedType.CLOSED);
		this.eventPublisher.postUpdate(event.getPin().getName(), states.get(event.getState()));
		
		logger.debug(" --> GPIO PIN STATE CHANGE: {} = {}",event.getPin(), states.get(event.getState()) );
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName,
				newState);
	}

}
