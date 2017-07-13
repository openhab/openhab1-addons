/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mcp23017.internal;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mcp23017.MCP23017BindingProvider;
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
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 *
 * @author Diego A. Fliess
 * @author Alexander Falkenstern
 * @since 1.9.0
 */
public class MCP23017Binding extends AbstractActiveBinding<MCP23017BindingProvider> implements GpioPinListenerDigital {

    private static final Logger logger = LoggerFactory.getLogger(MCP23017Binding.class);

    private final GpioController gpio;

    private Map<String, GpioPin> gpioPins = new HashMap<String, GpioPin>();

    private static final Map<Integer, MCP23017GpioProvider> mcpProviders = new HashMap<Integer, MCP23017GpioProvider>();

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is
     * set in the activate() method and must not be accessed anymore once the
     * deactivate() method was called or before activate() was called.
     */
    @SuppressWarnings("unused")
    private BundleContext bundleContext;

    /**
     * the refresh interval which is used to poll values from the mcp23017 server
     * (optional, defaults to 60000ms)
     */
    private long refreshInterval = 60000;

    /**
     * the polling interval mcp23071 check interrupt register (optional, defaults to 50ms)
     */
    private int pollingInterval = 50;

    public MCP23017Binding() {
        try {
            // ask for non privileged access (run without root)
            GpioUtil.enableNonPrivilegedAccess();
        } catch (UnsatisfiedLinkError e) {
            logger.error("MCP23017 Binding needs to be run a Raspberry Pi - deactivating it here.");
            gpio = null;
            return;
        }
        // now create a controller
        gpio = GpioFactory.getInstance();
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
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
        this.bundleContext = bundleContext;

        // to override the default refresh interval one has to add a
        // parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
        String refreshIntervalString = (String) configuration.get("refresh");
        if (StringUtils.isNotBlank(refreshIntervalString)) {
            refreshInterval = Long.parseLong(refreshIntervalString);
        }

        // to override the default polling interval one has to add a
        // parameter to openhab.cfg like <bindingName>:polling=<intervalInMs>
        String pollingIntervalString = (String) configuration.get("polling");
        if (StringUtils.isNotBlank(pollingIntervalString)) {
            pollingInterval = Integer.parseInt(pollingIntervalString);
        }

        setProperlyConfigured(true);
        logger.debug("mcp23017 activated and properly configured {}", this.hashCode());
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
        logger.debug("mcp23017 modified");
    }

    /**
     * Called by the SCR to deactivate the component when either the
     * configuration is removed or mandatory references are no longer satisfied
     * or the component has simply been stopped.
     *
     * @param reason
     *            Reason code for the deactivation:<br>
     *            <ul>
     *            <li>0 - Unspecified
     *            <li>1 - The component was disabled
     *            <li>2 - A reference became unsatisfied
     *            <li>3 - A configuration was changed
     *            <li>4 - A configuration was deleted
     *            <li>5 - The component was disposed
     *            <li>6 - The bundle was stopped
     *            </ul>
     */
    public void deactivate(final int reason) {
        this.bundleContext = null;
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
        logger.debug("mcp23017 deactivated");
        mcpProviders.clear();
        if (gpio != null) {
            gpio.shutdown();
        }
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
        return "mcp23017 Refresh Service";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void execute() {
    }

    @Override
    public void addBindingProvider(BindingProvider provider) {
        super.addBindingProvider(provider);

        /* first call contains all, better use activate ? if you have providers */
        logger.debug("addBindingProvider: {}", Arrays.toString(provider.getItemNames().toArray()));
        for (String itemName : provider.getItemNames()) {
            bindGpioPin((MCP23017BindingProvider) provider, itemName);
        }
    }

    @Override
    public void removeBindingProvider(BindingProvider provider) {
        super.removeBindingProvider(provider);
        /* shutdown call contains all better use deactivate */
        logger.debug("removeBindingProvider: {}", Arrays.toString(provider.getItemNames().toArray()));
        for (String itemName : provider.getItemNames()) {
            unBindGpioPin((MCP23017BindingProvider) provider, itemName);
        }
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        if (gpio == null) {
            // we are not properly initialized
            return;
        }
        if (provider instanceof MCP23017BindingProvider) {
            String allItemNames = Arrays.toString(provider.getItemNames().toArray());
            if (provider.getItemNames().contains(itemName)) {
                bindGpioPin((MCP23017BindingProvider) provider, itemName);
                logger.debug("bindingChanged item bound {} - {}", itemName, allItemNames);
            } else {
                unBindGpioPin((MCP23017BindingProvider) provider, itemName);
                logger.debug("bindingChanged item unbound  {} - {}", itemName, allItemNames);
            }
        }
        super.bindingChanged(provider, itemName);
    }

    private void bindGpioPin(MCP23017BindingProvider provider, String itemName) {
        try {
            int address = provider.getBusAddress(itemName);
            MCP23017GpioProvider mcp = mcpProviders.get(address);
            if (mcp == null) {
                try {
                    mcp = new MCP23017GpioProvider(I2CBus.BUS_1, address);
                    mcp.setPollingTime(pollingInterval);
                } catch (UnsupportedBusNumberException ex) {
                    throw new IllegalArgumentException("Tried to access not available I2C bus");
                }
                mcpProviders.put(address, mcp);
            }

            Pin pin = provider.getPin(itemName);
            PinMode mode = provider.getPinMode(itemName);
            if (mode.equals(PinMode.DIGITAL_OUTPUT)) {
                GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(mcp, pin, itemName,
                        provider.getDefaultState(itemName));
                gpioPins.put(itemName, output);

                logger.debug("Provisioned digital output for {}", itemName);
            } else if (mode.equals(PinMode.DIGITAL_INPUT)) {
                GpioPinDigitalInput input = gpio.provisionDigitalInputPin(mcp, pin, itemName, PinPullResistance.OFF);
                input.setDebounce(20);
                input.addListener(this);
                gpioPins.put(itemName, input);

                logger.debug("Provisioned digital input for {}", itemName);
            } else {
                throw new IllegalArgumentException("Invalid Pin Mode in config " + mode.name());
            }
        } catch (IOException exception) {
            logger.error("I/O error {}", exception.getMessage());
        }
    }

    private void unBindGpioPin(MCP23017BindingProvider provider, String itemName) {
        GpioPin pin = gpioPins.remove(itemName);
        gpio.unprovisionPin(pin);
        logger.debug("Unbound item {}", itemName);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

        // the configuration is guaranteed not to be null, because the component
        // definition has the configuration-policy set to require. If set to
        // 'optional' then the configuration may be null
        if (command instanceof OnOffType) {
            final OnOffType switchCommand = (OnOffType) command;

            for (MCP23017BindingProvider provider : this.providers) {
                if (provider.providesBindingFor(itemName)) {
                    GpioPin pin = gpioPins.get(itemName);
                    if (switchCommand == OnOffType.OFF) {
                        gpio.setState(PinState.LOW, (GpioPinDigitalOutput) pin);
                    } else if (switchCommand == OnOffType.ON) {
                        gpio.setState(PinState.HIGH, (GpioPinDigitalOutput) pin);
                    }
                }
            }
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        GpioPin pin = event.getPin();
        OpenClosedType state = OpenClosedType.CLOSED; // Assume we are high...
        if (event.getState() == PinState.LOW) { // To err is human...
            state = OpenClosedType.OPEN;
        }
        this.eventPublisher.postUpdate(pin.getName(), state);
        logger.debug("GPIO pin state change: {} = {}", pin, state);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        // the code being executed when a state was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveUpdate({},{}) is called", itemName, newState);
    }
}
