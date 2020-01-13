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
package org.openhab.binding.garadget.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.HttpStatus;
import org.openhab.binding.garadget.GaradgetBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class polls the Garadget API and sends updates to the event bus
 * of configured items in openHAB
 *
 * @author John Cocula
 * @since 1.9.0
 */
public class GaradgetBinding extends AbstractActiveBinding<GaradgetBindingProvider> {
    private final Logger logger = LoggerFactory.getLogger(GaradgetBinding.class);

    @SuppressWarnings("unused")
    private BundleContext bundleContext;

    private Connection connection = null;
    private Map<String, GaradgetDevice> devices = null;

    private static final String CONFIG_GRANULARITY = "granularity";
    private static final String CONFIG_REFRESH = "refresh";
    private static final String CONFIG_QUICKPOLL = "quickpoll";
    private static final String CONFIG_TIMEOUT = "timeout";
    private static final long DEFAULT_GRANULARITY = 5000;
    private static final long DEFAULT_REFRESH = 180000;
    private static final long DEFAULT_QUICKPOLL = 11000;
    private static final int DEFAULT_TIMEOUT = 5000;

    /**
     * the interval which is used to call the execute() method
     */
    private long granularity = DEFAULT_GRANULARITY;

    /**
     * the quick refresh interval which is used to poll values from the Garadget after a function was called
     */
    private long quickPollInterval = DEFAULT_QUICKPOLL;

    /**
     * the refresh interval which is used to poll values from the Garadget API.
     * (optional, defaults to 180000ms)
     */
    private long refreshInterval = DEFAULT_REFRESH;

    /**
     * The next time to poll this instance. Initially 0 so pollTimeExpired() initially returns true.
     */
    private final AtomicLong pollTime = new AtomicLong(0);

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
        modified(configuration);
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed
     * through the ConfigAdmin service.
     *
     * @param configuration
     *            Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration) {

        // to override the default granularity one has to add a
        // parameter to the .cfg like [garadget:]granularity=2000
        String granularityString = Objects.toString(configuration.get(CONFIG_GRANULARITY), null);
        granularity = isNotBlank(granularityString) ? Long.parseLong(granularityString) : DEFAULT_GRANULARITY;

        // to override the default refresh interval one has to add a
        // parameter to .cfg like [garadget:]refresh=240000
        String refreshIntervalString = Objects.toString(configuration.get(CONFIG_REFRESH), null);
        refreshInterval = isNotBlank(refreshIntervalString) ? Long.parseLong(refreshIntervalString) : DEFAULT_REFRESH;

        // to override the default quickPoll interval one has to add a
        // parameter to .cfg like [garadget:]quickpoll=4000
        String quickPollIntervalString = Objects.toString(configuration.get(CONFIG_QUICKPOLL), null);
        quickPollInterval = isNotBlank(quickPollIntervalString) ? Long.parseLong(quickPollIntervalString)
                : DEFAULT_QUICKPOLL;

        // to override the default HTTP timeout one has to add a
        // parameter to .cfg like [garadget:]timeout=20000
        String timeoutString = Objects.toString(configuration.get(CONFIG_TIMEOUT), null);
        int timeout = isNotBlank(timeoutString) ? Integer.parseInt(timeoutString) : DEFAULT_TIMEOUT;

        String username = Objects.toString(configuration.get("username"), null);
        String password = Objects.toString(configuration.get("password"), null);

        if (isNotBlank(username) && isNotBlank(password)) {
            connection = new Connection(username, password, timeout);
            connection.login();
        }

        if (connection.isLoggedIn()) {
            // Poll at the earliest opportunity
            schedulePoll(0);
            setProperlyConfigured(true);
        } else {
            setProperlyConfigured(false);
        }
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

        if (connection != null) {
            connection.logout();
            connection = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "Garadget Refresh Service";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getRefreshInterval() {
        return granularity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {

        if (!pollTimeExpired()) {
            return;
        }

        // schedule the next poll at the standard refresh interval
        schedulePoll(refreshInterval);

        logger.trace("Polling Garadget devices");

        if (connection == null) {
            logger.debug("No connection defined; please check username and password.");
            return;
        }

        devices = connection.getDevices();

        // Collect all the devices and variables for which we have in bindings

        Map<String, TreeSet<String>> pollMap = new HashMap<String, TreeSet<String>>();

        for (GaradgetBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                GaradgetBindingConfig deviceConfig = getConfigForItemName(itemName);

                if (deviceConfig != null) {
                    for (GaradgetSubscriber subscriber : deviceConfig.getSubscribers()) {
                        TreeSet<String> varNames = pollMap.get(subscriber.getDeviceId());
                        if (varNames == null) {
                            varNames = new TreeSet<String>();
                            pollMap.put(subscriber.getDeviceId(), varNames);
                        }

                        final String varName = subscriber.getVarName();

                        // Handle Garadget compound variables if only the "sub-variables"
                        // are subscribed to.

                        if (varName.startsWith(GaradgetDevice.DOOR_STATUS)) {
                            varNames.add(GaradgetDevice.DOOR_STATUS);
                        } else if (varName.startsWith(GaradgetDevice.DOOR_CONFIG)) {
                            varNames.add(GaradgetDevice.DOOR_CONFIG);
                        } else if (varName.startsWith(GaradgetDevice.NET_CONFIG)) {
                            varNames.add(GaradgetDevice.NET_CONFIG);
                        } else if (AbstractDevice.isVar(varName)) {
                            varNames.add(varName);
                        }
                    }
                }
            }
        }

        // Retrieve variables for each in bound device if any were bound to items

        for (final String deviceId : pollMap.keySet()) {
            final GaradgetDevice device = devices.get(deviceId);
            if (device == null) {
                logger.warn("Unable to poll variables for deviceId {}; skipping.", deviceId);
                continue;
            }
            final TreeSet<String> varNames = pollMap.get(deviceId);
            for (final String varName : varNames) {
                connection.sendCommand(device, varName, null, new HttpResponseHandler() {
                    @Override
                    public void handleResponse(int statusCode, String responseBody) {
                        if (statusCode == HttpStatus.SC_OK) {
                            // save variable's value
                            device.setVar(varName, responseBody);
                        }
                    }
                });
            }
        }

        // Update all in-bound items

        for (final GaradgetBindingProvider provider : providers) {
            for (final String itemName : provider.getItemNames()) {
                GaradgetBindingConfig deviceConfig = getConfigForItemName(itemName);

                if (deviceConfig != null) {
                    for (final GaradgetSubscriber subscriber : deviceConfig.getSubscribers()) {
                        AbstractDevice device = devices.get(subscriber.getDeviceId());
                        if (device != null) {
                            eventPublisher.postUpdate(itemName, device.getState(subscriber));
                        }
                    }
                }
            }
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void internalReceiveCommand(String itemName, Command command) {
        logger.trace("Garadget binding received command '{}' for item '{}'", command, itemName);
        final GaradgetBindingConfig deviceConfig = getConfigForItemName(itemName);
        if (deviceConfig != null) {
            for (GaradgetPublisher publisher : deviceConfig.getPublishers()) {
                commandGaradget(itemName, publisher, command);
            }
        } else {
            logger.debug("Item '{}' has no garadget config; ignoring command '{}'", itemName, command);
        }
    }

    /**
     * Call a function in the Particle REST API using the property in the binding config. The argument to the Particle
     * device function is the String version of the command, with a special case for calling the setState function so
     * sending ON, OFF, UP, DOWN and STOP commands are translated as "open", "close" or "stopped". Upon successfully
     * calling the Particle device function, the state of the item is updated to the return value from the function
     * call.
     *
     * @param itemName
     *            the item that is receiving the command
     * @param publisher
     *            the binding config (deviceId,funcName) to send the command to
     * @param command
     *            the command to send to the API
     */
    private void commandGaradget(final String itemName, final GaradgetPublisher publisher, Command command) {

        if (connection == null) {
            logger.warn("Command '{}' not sent for item '{}'; no connection.", command, itemName);
            return;
        }

        try {
            final GaradgetDevice device = devices.get(publisher.getDeviceId());
            if (device == null) {
                logger.warn("No device found with ID: {}", publisher.getDeviceId());
                return;
            }

            // Handle the special cases for setState function

            if ("setState".equals(publisher.getFuncName())) {
                if (command.equals(OnOffType.ON) || command.equals(UpDownType.UP) || command.equals(PercentType.ZERO)) {
                    command = new StringType("open");
                } else if (command.equals(OnOffType.OFF) || command.equals(UpDownType.DOWN)
                        || command.equals(PercentType.HUNDRED)) {
                    command = new StringType("close");
                } else if (command.equals(StopMoveType.STOP) || command instanceof PercentType) {
                    command = new StringType("stopped");
                }
            }

            // TODO: make JSON properly so special characters are escaped as needed
            String json = String.format("{ \"arg\": \"%s\" }\r\n", command.toString());
            connection.sendCommand(device, publisher.getFuncName(), json, new HttpResponseHandler() {
                @Override
                public void handleResponse(int statusCode, String responseBody) {
                    if (statusCode == HttpStatus.SC_OK) {
                        logger.debug("Calling function '{}' returned '{}'", publisher.getFuncName(), responseBody);
                        // A function was called successfully; poll soon
                        schedulePoll(quickPollInterval);
                    } else {
                        logger.warn("Failed to call function '{}', status code={}", publisher.getFuncName(),
                                statusCode);
                    }
                }
            });

        } catch (Exception ex) {
            logger.warn("Exception in commandGaradget:", ex);
        }
    }

    protected void addBindingProvider(GaradgetBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(GaradgetBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    private GaradgetBindingConfig getConfigForItemName(String itemName) {
        for (GaradgetBindingProvider provider : providers) {
            GaradgetBindingConfig bindingConfig = provider.getItemBindingConfig(itemName);
            if (bindingConfig != null) {
                return bindingConfig;
            }
        }
        return null;
    }

    /**
     * Return true if this instance is at or past the time to poll.
     *
     * @return if this instance is at or past the time to poll.
     */
    private boolean pollTimeExpired() {
        return System.currentTimeMillis() >= pollTime.get();
    }

    /**
     * Record the earliest time in the future at which we are allowed to poll this instance.
     *
     * @param future
     *            the number of milliseconds in the future
     */
    private void schedulePoll(long future) {
        this.pollTime.set(System.currentTimeMillis() + future);
    }
}
