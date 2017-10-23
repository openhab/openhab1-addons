/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.openhab.binding.isy.internal.ErrorHandler;
import org.openhab.binding.isy.internal.ISYControl;
import org.openhab.binding.isy.internal.ISYModelChangeListener;
import org.openhab.binding.isy.internal.ISYNodeType;
import org.openhab.binding.isy.internal.InsteonClient;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.universaldevices.device.model.UDControl;
import com.universaldevices.device.model.UDGroup;
import com.universaldevices.device.model.UDNode;
import com.universaldevices.resources.errormessages.Errors;

/**
 * This handles the connection and binding to the ISY device. All interaction
 * through the device runs through this class. It uses the ISY SDK to create the
 * client object and connect to the device using credentials in the
 * configuration file for openhab.
 *
 * @author Tim Diekmann
 * @since 1.10.0
 */
public class ISYActiveBinding extends AbstractActiveBinding<ISYBindingProvider> implements ISYModelChangeListener {

    private final Logger logger = LoggerFactory.getLogger(ISYActiveBinding.class);

    private ISYActiveBindingConfig runtimeConfiguration;

    /**
     * The client used to send and receive messages from ISY.
     */
    private InsteonClient insteonClient;

    /**
     * Constructor.
     */
    public ISYActiveBinding() {
    }

    /**
     * Called by SCR when the binding is resolved
     *
     * @param config
     */
    public void activate(final Map<Object, Object> config) {

        runtimeConfiguration = new ISYActiveBindingConfig(config);

        // create the Insteon client using their SDK
        //
        this.insteonClient = new InsteonClient(runtimeConfiguration.getUser(), runtimeConfiguration.getPassword(),
                this);
        Errors.addErrorListener(new ErrorHandler());

        if (runtimeConfiguration.isUseUpnp()) {
            this.logger.info("ISY: using UPNP to communicate with ISY");

            this.insteonClient.start();
        } else {
            this.logger.info("ISY: using URL to communicate with ISY");

            try {
                this.insteonClient.start(runtimeConfiguration.getUuid(), String.format("http://%s:%s",
                        runtimeConfiguration.getIpAddress(), runtimeConfiguration.getPort()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        setProperlyConfigured(true);
    }

    /**
     * Called by SCR when the binding configuration was modified
     */
    public void modified(final Map<Object, Object> config) {

        runtimeConfiguration = new ISYActiveBindingConfig(config);

        // create the Insteon client using their SDK
        //

        if (this.insteonClient != null) {
            this.insteonClient.stop();
        }

        this.insteonClient = new InsteonClient(runtimeConfiguration.getUser(), runtimeConfiguration.getPassword(),
                this);
        Errors.addErrorListener(new ErrorHandler());

        if (runtimeConfiguration.isUseUpnp()) {
            this.logger.info("ISY: using UPNP to communicate with ISY");

            this.insteonClient.start();
        } else {
            this.logger.info("ISY: using URL to communicate with ISY");

            try {
                this.insteonClient.start(runtimeConfiguration.getUuid(), String.format("http://%s:%s",
                        runtimeConfiguration.getIpAddress(), runtimeConfiguration.getPort()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        setProperlyConfigured(true);
    }

    /**
     * Called by the SCR to deactivate the component when either the
     * configuration is removed or mandatory references are no longer satisfied
     * or the component has simply been stopped.
     */
    @Override
    public void deactivate() {
        if (this.insteonClient != null) {
            this.insteonClient.stop();
            this.insteonClient = null;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return this.runtimeConfiguration.getRefreshInterval();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected String getName() {
        return "ISY Refresh Service";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void execute() {

        this.logger.debug("refreshing status");

        queryStatus();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(final String itemName, final Command command) {

        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        // See the Readme.md for the item formatting for the ISY link.

        this.logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

        ISYBindingConfig config = getBindingConfigByName(itemName);

        if (config != null) {
            processCommand(config, command);
        } else {
            this.logger.warn("No configured Insteon address found for item [{}]", itemName);
        }
    }

    private static final BigDecimal HALF = new BigDecimal("0.5");
    private static final BigDecimal TWO = new BigDecimal("0.5");
    private static final BigDecimal HUNDRED = new BigDecimal("100.0");
    private static final BigDecimal BYTEMAX = new BigDecimal("255.0");

    /**
     * Callback from the ISY client when a node changed its state. Find the
     * corresponding openHAB item and send a notification on the event bus.
     *
     * @param control
     *            {@link UDControl} ISY command
     * @param action
     *            Mostly a string with the value, e.g. "0", "255"
     * @param node
     *            {@link UDNode} ISY node can be a device or a group
     */
    @Override
    public void onModelChanged(final UDControl control, final Object action, final UDNode node) {
        Collection<ISYBindingConfig> configs;
        State state = null;
        ISYControl ctrl;

        try {
            ctrl = ISYControl.valueOf(control.name);
        } catch (IllegalArgumentException ie) {
            ctrl = ISYControl.UNDEFINED;
        }

        switch (ctrl) {
            case ST:
                // only propagate status updates

                configs = getBindingConfigFromAddress(node.address, control.name);

                for (ISYBindingConfig config : configs) {

                    switch (config.getType()) {
                        case SWITCH:
                            if ("0".equals(action)) {
                                state = OnOffType.OFF;
                            } else {
                                state = OnOffType.ON;
                            }
                            break;
                        case GROUP:
                            if ("0".equals(action)) {
                                state = OnOffType.OFF;
                            } else {
                                state = OnOffType.ON;
                            }
                            break;
                        case CONTACT:
                            if ("0".equals(action)) {
                                state = OpenClosedType.CLOSED;
                            } else {
                                state = OpenClosedType.OPEN;
                            }
                            break;
                        case THERMOSTAT:
                            state = new DecimalType(new BigDecimal((String) action).multiply(HALF));
                            break;
                        case NUMBER:
                            state = new DecimalType((String) action);
                            break;
                        case STRING:
                            state = new StringType((String) action);
                            break;
                        case DIMMER:
                            if ("0".equals(action)) {
                                state = OnOffType.OFF;
                            } else if ("255".equals(action)) {
                                state = OnOffType.ON;
                            } else {
                                BigDecimal dim = new BigDecimal((String) action).multiply(HUNDRED).divide(BYTEMAX)
                                        .setScale(0, BigDecimal.ROUND_HALF_UP);

                                state = new PercentType(dim);
                            }
                            break;
                        case LOCK:
                            if ("0".equals(action)) {
                                state = OnOffType.OFF;
                            } else {
                                state = OnOffType.ON;
                            }
                            break;
                        case HEARTBEAT:
                            if ("255".equals(action)) {
                                // Return current timestamp when we get a heartbeat
                                state = new DateTimeType();
                            }
                            break;
                        default:
                            break;
                    }

                    if (state != null) {
                        this.eventPublisher.postUpdate(config.getItemName(), state);
                    }
                }
                break;

            case DON:
            case DFON:
                for (ISYBindingConfig config : getBindingConfigFromAddress(node.address, control.name)) {

                    if (config.getItem().getAcceptedDataTypes().contains(OpenClosedType.class)) {
                        if ("1".equals(action)) {
                            state = OpenClosedType.OPEN;
                        }
                    } else if (config.getItem().getAcceptedDataTypes().contains(OnOffType.class)) {
                        if (!"0".equals(action)) {
                            state = OnOffType.ON;
                        }
                    } else if (config.getItem().getAcceptedDataTypes().contains(DecimalType.class)) {
                        state = new DecimalType((String) action);
                    }

                    if (state != null) {
                        this.eventPublisher.postUpdate(config.getItemName(), state);
                    }
                }
                break;

            case DOF:
            case DFOF:
                for (ISYBindingConfig config : getBindingConfigFromAddress(node.address, control.name)) {

                    if (config.getItem().getAcceptedDataTypes().contains(OpenClosedType.class)) {
                        if ("1".equals(action)) {
                            state = OpenClosedType.CLOSED;
                        }
                    } else if (config.getItem().getAcceptedDataTypes().contains(OnOffType.class)) {
                        if ("1".equals(action)) {
                            state = OnOffType.OFF;
                        }
                    } else if (config.getItem().getAcceptedDataTypes().contains(DecimalType.class)) {
                        state = new DecimalType((String) action);
                    }

                    if (state != null) {
                        this.eventPublisher.postUpdate(config.getItemName(), state);
                    }
                }
                break;

            case CLISPH:
            case CLISPC:
                for (ISYBindingConfig config : getBindingConfigFromAddress(node.address, control.name)) {

                    switch (config.getType()) {
                        case THERMOSTAT:

                            state = new DecimalType(new BigDecimal((String) action).multiply(HALF));
                            break;
                        default:
                            state = null;
                            break;
                    }

                    if (state != null) {
                        this.eventPublisher.postUpdate(config.getItemName(), state);
                    }
                }
                break;

            case CLIHUM:
            case CLIMD:
            case CLIHCS:
            case CLIFS:
                for (ISYBindingConfig config : getBindingConfigFromAddress(node.address, control.name)) {

                    switch (config.getType()) {
                        case THERMOSTAT:
                            state = new DecimalType((String) action);
                            break;
                        default:
                            state = null;
                            break;
                    }

                    if (state != null) {
                        this.eventPublisher.postUpdate(config.getItemName(), state);
                    }
                }
                break;

            case BATLVL:
                for (ISYBindingConfig config : getBindingConfigFromAddress(node.address, control.name)) {

                    state = new DecimalType((String) action);
                    this.eventPublisher.postUpdate(config.getItemName(), state);
                }
                break;

            default:
                this.logger.debug("Unsupported control '{}'", control.name);
        }
    }

    /**
     * This will look through items that have a binding to the ISY unit and
     * return a collection of them to be checked using the address. This allows
     * a many to one relationship of a item in ISY to OpenHab items.
     *
     * @param address
     *            The address to search for (w.x.y.z)
     * @param cmd
     *            The commands associated with the binding. (ST, DON, DOF, etc)
     * @return A collection of items to update.
     */
    private Collection<ISYBindingConfig> getBindingConfigFromAddress(final String address, final String cmd) {
        for (ISYBindingProvider provider : this.providers) {
            Collection<ISYBindingConfig> config = provider.getBindingConfigFromAddress(address, cmd);

            if (config != null) {
                return config;
            }
        }

        return Collections.EMPTY_LIST;
    }

    /**
     * Returns a binding for a item based on name.
     *
     * @param itemName
     *            The name of the item being searched for.
     * @return The Binding configuration for the named item.
     */
    private ISYBindingConfig getBindingConfigByName(final String itemName) {
        for (ISYBindingProvider provider : this.providers) {
            ISYBindingConfig config = provider.getBindingConfigFromItemName(itemName);

            if (config != null) {
                return config;
            }
        }

        return null;
    }

    /**
     * Translate the openHAB command into a Insteon command
     *
     * @param address
     *            Insteon address of device/node/group/scene
     * @param command
     *            openHAB command
     */
    private void processCommand(final ISYBindingConfig config, final Command command) {
        if (!this.insteonClient.isISYReady()) {
            this.logger.warn("ISY is not ready; dropping command '{}'", command);

            return;
        }

        UDNode node = null;

        // find out whether node is group or node
        try {
            node = this.insteonClient.getNodes().get(config.getController());
        } catch (Exception e) {
            this.logger.warn("no such device: '{}' - {} ", config.getController(), e);
        }

        if (node == null) {
            try {
                node = this.insteonClient.getGroups().get(config.getController());
            } catch (Exception e) {
                this.logger.warn("no such group found: '{}' - {}", config.getController(), e);
            }
        }

        if (node != null) {
            executeCommand(config, node, command);
        } else {
            this.logger.warn("No node found for address: '{}' ", config.getController());
        }
    }

    /***
     * Performs the execution of the command in the ISY unit. It will handle the
     * translation of the OpenHab command to the ISY version.
     *
     * @param config
     *            Configuration item for the item in OpenHab
     * @param node
     *            The node in ISY that represents the binding configuration.
     * @param command
     *            The OpenHab command to execute.
     */
    private void executeCommand(final ISYBindingConfig config, final UDNode node, final Command command) {

        // Do percent first so dimmers dim and do not just go on and off.
        if (command instanceof IncreaseDecreaseType && ISYNodeType.DIMMER.equals(config.getType())) {
            IncreaseDecreaseType type = (IncreaseDecreaseType) command;

            switch (type) {
                case INCREASE:
                    if (node instanceof UDGroup) {
                        this.insteonClient.brightenScene(node.address);
                    } else {
                        this.insteonClient.brightenDevice(node.address);
                    }
                    break;
                case DECREASE:
                    if (node instanceof UDGroup) {
                        this.insteonClient.dimScene(node.address);
                    } else {
                        this.insteonClient.dimDevice(node.address);
                    }
                    break;
            }
        } else if (command instanceof OnOffType) {
            OnOffType type = (OnOffType) command;

            switch (type) {
                case ON:
                    if (ISYNodeType.THERMOSTAT.equals(config.getType())) {
                        // turn thermostat Fan State to On
                        this.insteonClient.changeNodeState(ISYControl.CLIFS.name(), "7", node.address);

                        // provide immediate feedback such that the UI gets updated
                        // or else the switch button will not be refreshed until the
                        // new state has been reached.
                        // TODO: Make it a configuration option to enable immediate
                        // state update before ISY device poll.
                        // this.eventPublisher.postUpdate(config.getItemName(),
                        // type);
                    } else if (ISYNodeType.LOCK.equals(config.getType())) {
                        this.insteonClient.changeNodeState(ISYControl.SECMD.name(), "1", node.address);
                        // TODO: Make it a configuration option to enable immediate
                        // state update before ISY device poll.
                        // this.eventPublisher.postUpdate(config.getItemName(),
                        // type);
                    } else if (node instanceof UDGroup) {
                        this.insteonClient.turnSceneOn(node.address);
                    } else {
                        // TODO: See if openhab can handle a slow or fast or make it
                        // a config option.
                        this.insteonClient.turnDeviceFastOn(node.address);
                    }
                    break;

                case OFF:
                    if (ISYNodeType.THERMOSTAT.equals(config.getType())) {
                        // turn thermostat off
                        this.insteonClient.changeNodeState(ISYControl.CLIMD.name(), "0", node.address);
                        // provide immediate feedback such that the UI gets updated
                        // or else the switch button will not be refreshed until the
                        // new state has been reached
                        // TODO: Make it a configuration option to enable immediate
                        // state update before ISY device poll.
                        // this.eventPublisher.postUpdate(config.getItemName(),
                        // type);

                    } else if (ISYNodeType.LOCK.equals(config.getType())) {
                        this.insteonClient.changeNodeState(ISYControl.SECMD.name(), "0", node.address);
                        // TODO: Make it a configuration option to enable immediate
                        // state update before ISY device poll.
                        // this.eventPublisher.postUpdate(config.getItemName(),
                        // type);
                    } else if (node instanceof UDGroup) {
                        this.insteonClient.turnSceneFastOff(node.address);
                    } else {
                        this.insteonClient.turnDeviceFastOff(node.address);
                    }
                    break;
            }
        } else if (command instanceof DecimalType) {
            DecimalType type = (DecimalType) command;

            switch (config.getType()) {
                case THERMOSTAT:
                    switch (config.getControlCommand()) {
                        case CLISPH:
                        case CLISPC:
                            DecimalType value = new DecimalType(type.toBigDecimal().multiply(TWO));
                            this.insteonClient.changeNodeState(config.getControlCommand().name(), value.format("%d"),
                                    node.address);
                            break;
                        case CLIMD:
                        case CLIFS:
                            this.insteonClient.changeNodeState(config.getControlCommand().name(), type.format("%s"),
                                    node.address);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
                // Dimmer Commands come in as DecimalType, not PercentType
                case DIMMER:
                    switch (config.getControlCommand()) {
                        case ST:
                            DecimalType dim = new DecimalType(type.toBigDecimal().multiply(HUNDRED).divide(BYTEMAX)
                                    .setScale(0, BigDecimal.ROUND_HALF_UP));

                            if (dim.intValue() <= 0) {
                                this.insteonClient.turnDeviceFastOff(node.address);
                            } else if (dim.intValue() >= 255) {
                                this.insteonClient.turnDeviceFastOn(node.address);
                            } else {
                                this.insteonClient.changeNodeState(ISYControl.DON.name(), dim.format("%s"),
                                        node.address);
                            }

                            // TODO: Make it a configuration option to enable immediate
                            // state update before ISY device poll.
                            // this.eventPublisher.postUpdate(config.getItemName(),
                            // dim);
                            break;
                        default:
                            break;
                    }
            }
        } else if (command instanceof IncreaseDecreaseType) {
            IncreaseDecreaseType type = (IncreaseDecreaseType) command;

            switch (config.getType()) {
                case DIMMER:
                    switch (config.getControlCommand()) {
                        case ST:
                            if (type.equals(IncreaseDecreaseType.INCREASE)) {
                                this.insteonClient.brightenDevice(node.address);
                            } else if (type.equals(IncreaseDecreaseType.DECREASE)) {
                                this.insteonClient.dimDevice(node.address);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Run a update status on every item in OpenHab that has a ISY binding
     * reference.
     */
    private void queryStatus() {
        for (ISYBindingProvider provider : this.providers) {
            for (String itemName : provider.getItemNames()) {
                ISYBindingConfig config = provider.getBindingConfigFromItemName(itemName);

                updateStatus(itemName, config);
            }
        }
    }

    private void updateStatus(final String itemName, final ISYBindingConfig config) {

        this.logger.debug("updateStatus({},{}) is called!", itemName, config);

        if (this.insteonClient != null && this.insteonClient.isISYReady()) {
            try {
                UDNode node = this.insteonClient.getNodes().get(config.getAddress());
                if (node == null) {
                    node = this.insteonClient.getGroups().get(config.getAddress());
                }

                if (node != null) {
                    Object value = this.insteonClient.getCurrValue(node, config.getControlCommand().name());

                    UDControl control = new UDControl();
                    control.name = config.getControlCommand().name();

                    onModelChanged(control, value, node);
                }
            } catch (Exception ex) {
                this.logger.error("Failed to update status of '{}' ", itemName, ex);
            }
        } else {

            this.logger.info("ISY is not ready yet");

        }
    }

}
