/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mcp3424.internal;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mcp3424.MCP3424BindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.gpio.extension.mcp.MCP3424GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 *
 * @author Alexander Falkenstern
 * @since 1.9.0
 */
public class MCP3424Binding extends AbstractActiveBinding<MCP3424BindingProvider> implements GpioPinListenerAnalog {

    private static final Logger logger = LoggerFactory.getLogger(MCP3424Binding.class);

    private final GpioController gpio;

    private Map<String, GpioPin> gpioPins = new HashMap<String, GpioPin>();

    private static final Map<Integer, MCP3424GpioProvider> mcpProviders = new HashMap<Integer, MCP3424GpioProvider>();

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is
     * set in the activate() method and must not be accessed anymore once the
     * deactivate() method was called or before activate() was called.
     */
    @SuppressWarnings("unused")
    private BundleContext bundleContext;

    /**
     * the refresh interval which is used to poll values from the mcp3424 server
     * (optional, defaults to 60000ms)
     */
    private long refreshInterval = 60000;

    /**
     * the polling interval mcp3424 check interrupt register (optional, defaults to 50ms)
     */
    private int pollingInterval = 50;

    public MCP3424Binding() {
        // ask for non privileged access (run without root)
        GpioUtil.enableNonPrivilegedAccess();
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

        // read further config parameters here ...
        setProperlyConfigured(true);
        logger.debug("mcp3424 activated and properly configured {}", this.hashCode());
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
        logger.debug("mcp3424 modified");
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
        logger.debug("mcp3424 deactivated");
        mcpProviders.clear();
        gpio.shutdown();
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
        return "mcp3424 Refresh Service";
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
            bindGpioPin((MCP3424BindingProvider) provider, itemName);
        }
    }

    @Override
    public void removeBindingProvider(BindingProvider provider) {
        super.removeBindingProvider(provider);
        /* shutdown call contains all better use deactivate */
        logger.debug("removeBindingProvider: {}", Arrays.toString(provider.getItemNames().toArray()));
        for (String itemName : provider.getItemNames()) {
            unBindGpioPin((MCP3424BindingProvider) provider, itemName);
        }
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        if (provider instanceof MCP3424BindingProvider) {
            String allItemNames = Arrays.toString(provider.getItemNames().toArray());
            if (provider.getItemNames().contains(itemName)) {
                bindGpioPin((MCP3424BindingProvider) provider, itemName);
                logger.debug("bindingChanged item bound {} - {}", itemName, allItemNames);
            } else {
                unBindGpioPin((MCP3424BindingProvider) provider, itemName);
                logger.debug("bindingChanged item unbound  {} - {}", itemName, allItemNames);
            }
        }
        super.bindingChanged(provider, itemName);
    }

    private void bindGpioPin(MCP3424BindingProvider provider, String itemName) {
        try {
            int address = provider.getBusAddress(itemName);
            MCP3424GpioProvider mcp = mcpProviders.get(address);
            if (mcp == null) {
                try {
                    int gain = provider.getGain(itemName);
                    int resolution = provider.getResolution(itemName);
                    mcp = new MCP3424GpioProvider(I2CBus.BUS_1, address, resolution, gain);
                    mcp.setMonitorInterval(pollingInterval);
                } catch (UnsupportedBusNumberException ex) {
                    throw new IllegalArgumentException("Tried to access not available I2C bus");
                }
                mcpProviders.put(address, mcp);
            }

            mcp.setMonitorEnabled(false);
            Pin pin = provider.getPin(itemName);

            GpioPinAnalogInput input = gpio.provisionAnalogInputPin(mcp, pin, itemName);
            input.addListener(this);
            input.setTag(provider.getItem(itemName));
            gpioPins.put(itemName, input);

            mcp.setEventThreshold(0, input);
            mcp.setMonitorEnabled(true);

            logger.debug("Provisioned analog input for {}", itemName);
        } catch (IOException exception) {
            logger.error("I/O error {}", exception.getMessage());
        }
    }

    private void unBindGpioPin(MCP3424BindingProvider provider, String itemName) {
        GpioPin pin = gpioPins.remove(itemName);
        gpio.unprovisionPin(pin);
        logger.debug("Unbound item {}", itemName);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        super.internalReceiveCommand(itemName, command);
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
    }

    /**
     * Function will be called by Pi4J, if new conversion result is available.
     * On this time we can notify openhab, that something was changed.
     **/
    @Override
    public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
        GpioPin pin = event.getPin();
        MCP3424GpioProvider provider = (MCP3424GpioProvider) pin.getProvider();

        if (pin.getTag() instanceof NumberItem) {
            double value = provider.getAnalogValue(pin.getPin());
            try {
                this.eventPublisher.postUpdate(pin.getName(), new DecimalType(value));
            } catch (NumberFormatException exception) {
                logger.warn("Unable to convert '{}' for item {} to number", String.valueOf(value), pin.getName());
            }
            logger.debug("GPIO channel change: {} = {}", pin, value);
        } else if (pin.getTag() instanceof DimmerItem) {
            double value = provider.getPercentValue(pin.getPin());
            try {
                this.eventPublisher.postUpdate(pin.getName(), new PercentType((int) value));
            } catch (NumberFormatException e) {
                logger.warn("Unable to convert '{}' for item {} to number", String.valueOf(value), pin.getName());
            }
            logger.debug("GPIO channel change: {} = {}", pin, value);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        super.internalReceiveUpdate(itemName, newState);
        // the code being executed when a state was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveUpdate({},{}) is called", itemName, newState);
    }
}
