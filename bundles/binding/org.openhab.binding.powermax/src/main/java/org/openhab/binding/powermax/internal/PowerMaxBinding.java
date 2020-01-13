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
package org.openhab.binding.powermax.internal;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.powermax.PowerMaxBindingConfig;
import org.openhab.binding.powermax.PowerMaxBindingProvider;
import org.openhab.binding.powermax.internal.connector.PowerMaxEvent;
import org.openhab.binding.powermax.internal.connector.PowerMaxEventListener;
import org.openhab.binding.powermax.internal.message.PowerMaxBaseMessage;
import org.openhab.binding.powermax.internal.message.PowerMaxCommDriver;
import org.openhab.binding.powermax.internal.message.PowerMaxInfoMessage;
import org.openhab.binding.powermax.internal.message.PowerMaxPowerlinkMessage;
import org.openhab.binding.powermax.internal.message.PowerMaxReceiveType;
import org.openhab.binding.powermax.internal.message.PowerMaxSendType;
import org.openhab.binding.powermax.internal.state.PowerMaxPanelSettings;
import org.openhab.binding.powermax.internal.state.PowerMaxPanelType;
import org.openhab.binding.powermax.internal.state.PowerMaxState;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that get information from the Visonic alarm system.
 * The binding is listening to openHAB event bus and is able to send few commands to the alarm system
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxBinding extends AbstractActiveBinding<PowerMaxBindingProvider>
        implements ManagedService, PowerMaxEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxBinding.class);

    private static final int ONE_MINUTE = 60000;

    /** Default delay in milliseconds to reset a motion detection */
    private static final int DEFAULT_MOTION_OFF_DELAY = 3 * ONE_MINUTE;

    /** The serial port to use for connecting to the PowerMax alarm system */
    private String serialPort;

    /** The IP address and TCP port to use for connecting to the PowerMax alarm system */
    private String ipAddress;
    private int tcpPort;

    /** The delay in milliseconds to reset a motion detection */
    private int motionOffDelay;

    /** The PIN code to use for arming/disarming the PowerMax alarm system from openHAB */
    private String pinCode;

    /** Enable or disable arming the PowerMax alarm system from openHAB */
    private boolean allowArming;

    /** Enable or disable disarming the PowerMax alarm system from openHAB */
    private boolean allowDisarming;

    /** Force the standard mode rather than trying using the Powerlink mode */
    private boolean forceStandardMode;

    /** Panel type used when in standard mode */
    private PowerMaxPanelType panelType;

    /** Automatic sync time */
    private boolean autoSyncTime;

    /** The object to store the current state of the PowerMax alarm system */
    private PowerMaxState currentState;

    /** Number of items managed by the binding */
    private int itemCount;

    /** Boolean indicating whether or not the update of items is temporarily disabled by the binding */
    private boolean itemUpdateDisabled;

    /**
     * Boolean indicating whether or not an update of all items will be required
     * during the next refresh cycle
     */
    private boolean triggerItemsUpdate;

    /** Boolean indicating whether or not connection is established with the alarm system */
    private boolean connected;

    public PowerMaxBinding() {
        serialPort = null;
        ipAddress = null;
        tcpPort = 0;
        motionOffDelay = DEFAULT_MOTION_OFF_DELAY;
        pinCode = null;
        allowArming = false;
        allowDisarming = false;
        forceStandardMode = false;
        panelType = PowerMaxPanelType.POWERMAX_PRO;
        autoSyncTime = false;
        currentState = null;
        itemCount = 0;
        itemUpdateDisabled = true;
        triggerItemsUpdate = false;
        connected = false;
    }

    /**
     * Activates the binding. Actually does nothing, because on activation
     * OpenHAB always calls updated to indicate that the config is updated
     * Activation is done there
     */
    @Override
    public void activate() {
        logger.debug("Activate PowerMax alarm binding");
    }

    /**
     * Deactivates the binding
     */
    @Override
    public void deactivate() {
        logger.debug("Deactivate PowerMax alarm binding");
        closeConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return 20000; // 20 seconds
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "PowerMax Refresh Service";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        logger.debug("PowerMax alarm Execute");

        if (!isProperlyConfigured()) {
            logger.debug("execute(): not yet properly configured");
            return;
        }

        //
        // Off items linked to motion sensors after the delay defined by
        // the variable motionOffDelay
        //

        long now = System.currentTimeMillis();
        PowerMaxPanelSettings settings = PowerMaxPanelSettings.getThePanelSettings();
        PowerMaxState updateState = null;
        if ((currentState != null) && (settings != null)) {
            for (int i = 1; i <= settings.getNbZones(); i++) {
                if ((settings.getZoneSettings(i) != null)
                        && settings.getZoneSettings(i).getSensorType().equalsIgnoreCase("Motion")
                        && (currentState.isSensorTripped(i) == Boolean.TRUE)
                        && (currentState.getSensorLastTripped(i) != null)
                        && ((now - currentState.getSensorLastTripped(i)) > motionOffDelay)) {
                    if (updateState == null) {
                        updateState = new PowerMaxState();
                    }
                    updateState.setSensorTripped(i, false);
                }
            }
        }
        if (updateState != null) {
            updateItemsFromAlarmState(PowerMaxSelectorType.ZONE_STATUS, updateState);
            currentState.merge(updateState);
        }

        if (PowerMaxCommDriver.getTheCommDriver() != null) {
            connected = PowerMaxCommDriver.getTheCommDriver().isConnected();
        }

        // Check that we receive a keep alive message during the last minute
        if (connected && (currentState.isPowerlinkMode() != null) && currentState.isPowerlinkMode().equals(Boolean.TRUE)
                && (currentState.getLastKeepAlive() != null)
                && ((now - currentState.getLastKeepAlive()) > ONE_MINUTE)) {
            // Let Powermax know we are alive
            PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.RESTORE);
            currentState.setLastKeepAlive(now);
        }

        //
        // Try to reconnect if disconnected
        //

        if (!connected) {
            logger.debug("execute(): trying to reconnect...");
            closeConnection();
            if (triggerItemsUpdate) {
                logger.debug("execute(): items update enabled");
                itemUpdateDisabled = false;
                triggerItemsUpdate = false;
            }
            currentState = new PowerMaxState();
            openConnection();
            if (connected) {
                // TODO is INIT message required or not ?

                if (forceStandardMode) {
                    currentState.setPowerlinkMode(false);
                    updateItemsFromAlarmState(PowerMaxSelectorType.PANEL_MODE, currentState);
                    settings = PowerMaxPanelSettings.getThePanelSettings();
                    settings.process(false, panelType, null);
                    updateItemsFromPanelSettings();
                    logger.info("PowerMax alarm binding: running in Standard mode");
                    PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.ZONESNAME);
                    PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.ZONESTYPE);
                    PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.STATUS);
                } else {
                    PowerMaxCommDriver.getTheCommDriver().startDownload();
                }
            } else {
                logger.debug("execute(): reconnection failed");
            }
        } else if (triggerItemsUpdate) {
            logger.debug("execute(): update all items");
            itemUpdateDisabled = false;
            triggerItemsUpdate = false;
            // Adjust all the items to the current alarm state
            updateItemsFromAlarmState(currentState);
            // Adjust all the items to the current alarm settings
            updateItemsFromPanelSettings();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        logger.debug("bindingChanged(): Item Name: {} Provider items count: {}", itemName,
                provider.getItemNames().size());

        itemUpdateDisabled = true;
        if (provider.getItemNames().size() >= itemCount) {
            triggerItemsUpdate = true;
        }
        itemCount = provider.getItemNames().size();

        super.bindingChanged(provider, itemName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void allBindingsChanged(BindingProvider provider) {
        logger.debug("allBindingsChanged(): Provider items count: {}", provider.getItemNames().size());

        itemUpdateDisabled = true;
        if (provider.getItemNames().size() > 0) {
            triggerItemsUpdate = true;
        }
        itemCount = provider.getItemNames().size();

        super.allBindingsChanged(provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveCommand({},{}) is called", itemName, command);
        if (!isProperlyConfigured() || !connected) {
            logger.warn("PowerMax alarm binding is not properly configured. Command is ignored.");
            return;
        }

        String armMode = null;
        String actionPGMX10 = null;
        Byte devicePGMX10 = null;
        Boolean bypass = null;
        byte zoneNr = 0;
        String commandStr = null;

        PowerMaxPanelSettings settings = PowerMaxPanelSettings.getThePanelSettings();
        PowerMaxCommDriver comm = PowerMaxCommDriver.getTheCommDriver();

        for (PowerMaxBindingProvider provider : providers) {
            PowerMaxBindingConfig config = provider.getConfig(itemName);
            if (config == null) {
                continue;
            }
            switch (config.getSelectorType()) {
                case PARTITION_ARM_MODE:
                    if (command instanceof StringType) {
                        armMode = command.toString();
                    }
                    break;
                case PARTITION_ARMED:
                    if (command instanceof OnOffType) {
                        armMode = command.equals(OnOffType.ON) ? "Armed" : "Disarmed";
                    }
                    break;
                case PGM_STATUS:
                    if (command instanceof OnOffType) {
                        actionPGMX10 = command.toString();
                    }
                    break;
                case X10_STATUS:
                    if (command instanceof OnOffType || command instanceof StringType) {
                        actionPGMX10 = command.toString();
                    }
                    try {
                        devicePGMX10 = Byte.parseByte(config.getSelectorParam());
                    } catch (NumberFormatException e) {
                        logger.warn("PowerMax alarm binding: invalid X10 device id: {}", config.getSelectorParam());
                        actionPGMX10 = null;
                    }
                    break;
                case ZONE_BYPASSED:
                    if (command instanceof OnOffType) {
                        bypass = command.equals(OnOffType.ON) ? Boolean.TRUE : Boolean.FALSE;
                    }
                    try {
                        zoneNr = Byte.parseByte(config.getSelectorParam());
                    } catch (NumberFormatException e) {
                        logger.warn("PowerMax alarm binding: invalid zone number: {}", config.getSelectorParam());
                        bypass = null;
                    }
                    break;
                case COMMAND:
                    if (command instanceof StringType) {
                        commandStr = command.toString();
                        eventPublisher.postUpdate(itemName, new StringType(commandStr));
                    }
                    break;
                default:
                    break;
            }
            if (armMode != null) {

                HashMap<String, Boolean> allowedModes = new HashMap<String, Boolean>();
                allowedModes.put("Disarmed", allowDisarming);
                allowedModes.put("Stay", allowArming);
                allowedModes.put("Armed", allowArming);
                allowedModes.put("StayInstant", allowArming);
                allowedModes.put("ArmedInstant", allowArming);
                allowedModes.put("Night", allowArming);
                allowedModes.put("NightInstant", allowArming);

                Boolean allowed = allowedModes.get(armMode);
                if ((allowed == null) || !allowed) {
                    logger.warn("PowerMax alarm binding: rejected command {}", armMode);
                } else {
                    comm.requestArmMode(armMode, currentState.isPowerlinkMode()
                            ? PowerMaxPanelSettings.getThePanelSettings().getFirstPinCode() : pinCode);
                }
                break;
            } else if (actionPGMX10 != null) {
                comm.sendPGMX10(actionPGMX10, devicePGMX10);
                break;
            } else if (bypass != null) {
                if ((currentState.isPowerlinkMode() == null) || currentState.isPowerlinkMode().equals(Boolean.FALSE)) {
                    logger.warn("PowerMax alarm binding: Bypass option only supported in Powerlink mode");
                } else if (!PowerMaxPanelSettings.getThePanelSettings().isBypassEnabled()) {
                    logger.warn("PowerMax alarm binding: Bypass option not enabled in panel settings");
                } else {
                    comm.sendZoneBypass(bypass.booleanValue(), zoneNr,
                            PowerMaxPanelSettings.getThePanelSettings().getFirstPinCode());
                }
                break;
            } else if (commandStr != null) {
                if (commandStr.equalsIgnoreCase("get_event_log")) {
                    comm.requestEventLog(currentState.isPowerlinkMode()
                            ? PowerMaxPanelSettings.getThePanelSettings().getFirstPinCode() : pinCode);
                } else if (commandStr.equalsIgnoreCase("download_setup")) {
                    if ((currentState.isPowerlinkMode() == null)
                            || currentState.isPowerlinkMode().equals(Boolean.FALSE)) {
                        logger.warn("PowerMax alarm binding: download setup only supported in Powerlink mode");
                    } else {
                        comm.startDownload();
                        if (currentState.getLastKeepAlive() != null) {
                            currentState.setLastKeepAlive(System.currentTimeMillis());
                        }
                    }
                } else if (commandStr.equalsIgnoreCase("log_setup")) {
                    settings.log();
                } else if (commandStr.equalsIgnoreCase("help_items")) {
                    settings.helpItems();
                } else {
                    logger.warn("PowerMax alarm binding: rejected command {}", commandStr);
                }
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        logger.debug("updated(): updating configuration");

        closeConnection();

        serialPort = null;
        ipAddress = null;
        tcpPort = 0;
        motionOffDelay = DEFAULT_MOTION_OFF_DELAY;
        allowArming = false;
        allowDisarming = false;
        forceStandardMode = false;
        panelType = PowerMaxPanelType.POWERMAX_PRO;
        autoSyncTime = false;
        pinCode = null;

        PowerMaxReceiveType.POWERLINK.setHandlerClass(PowerMaxPowerlinkMessage.class);

        if (config != null) {
            String serialPortString = Objects.toString(config.get("serialPort"), null);
            if (StringUtils.isNotBlank(serialPortString)) {
                serialPort = serialPortString;
            }

            String ipString = Objects.toString(config.get("ip"), null);
            if (StringUtils.isNotBlank(ipString)) {
                ipAddress = ipString;
            }

            if (serialPort == null && ipAddress == null) {
                logger.warn("PowerMax alarm binding: one connection type (Serial Port or IP Address) must be defined");
                this.setProperlyConfigured(false);
                throw new ConfigurationException(null,
                        "one connection type (Serial Port or IP Address) must be defined");
            }

            if (serialPort != null && ipAddress != null) {
                logger.warn(
                        "PowerMax alarm binding: can only configure one connection type at a time: Serial Port or IP Address");
                this.setProperlyConfigured(false);
                throw new ConfigurationException(null,
                        "can only configure one connection type at a time: Serial Port or IP Address");
            }

            if (ipAddress != null) {
                String tcpPortString = Objects.toString(config.get("tcpPort"), null);
                if (StringUtils.isNotBlank(tcpPortString)) {
                    try {
                        tcpPort = Integer.parseInt(tcpPortString);
                    } catch (NumberFormatException numberFormatException) {
                        tcpPort = 0;
                        logger.warn(
                                "PowerMax alarm binding: TCP port not configured correctly (number expected, received '{}')",
                                tcpPortString);
                        this.setProperlyConfigured(false);
                        throw new ConfigurationException("tcpPort",
                                "TCP port not configured correctly (number expected, received '" + tcpPortString
                                        + "')");
                    }
                }
            }

            String motionOffDelayString = Objects.toString(config.get("motionOffDelay"), null);
            if (StringUtils.isNotBlank(motionOffDelayString)) {
                try {
                    motionOffDelay = Integer.parseInt(motionOffDelayString) * 60000;
                } catch (NumberFormatException numberFormatException) {
                    motionOffDelay = DEFAULT_MOTION_OFF_DELAY;
                    logger.warn(
                            "PowerMax alarm binding: motion off delay not configured correctly (number expected, received '{}')",
                            motionOffDelayString);
                }
            }

            String allowArmingString = Objects.toString(config.get("allowArming"), null);
            if (StringUtils.isNotBlank(allowArmingString)) {
                allowArming = Boolean.valueOf(allowArmingString);
            }
            String allowDisarmingString = Objects.toString(config.get("allowDisarming"), null);
            if (StringUtils.isNotBlank(allowDisarmingString)) {
                allowDisarming = Boolean.valueOf(allowDisarmingString);
            }
            String forceStandardModeString = Objects.toString(config.get("forceStandardMode"), null);
            if (StringUtils.isNotBlank(forceStandardModeString)) {
                forceStandardMode = Boolean.valueOf(forceStandardModeString);
                PowerMaxReceiveType.POWERLINK.setHandlerClass(
                        forceStandardMode ? PowerMaxBaseMessage.class : PowerMaxPowerlinkMessage.class);
            }

            String panelTypeString = Objects.toString(config.get("panelType"), null);
            if (StringUtils.isNotBlank(panelTypeString)) {
                try {
                    panelType = PowerMaxPanelType.fromLabel(panelTypeString);
                } catch (IllegalArgumentException exception) {
                    panelType = PowerMaxPanelType.POWERMAX_PRO;
                    logger.warn("PowerMax alarm binding: panel type not configured correctly");
                }
            }
            PowerMaxPanelSettings.initPanelSettings(panelType);

            String autoSyncTimeString = Objects.toString(config.get("autoSyncTime"), null);
            if (StringUtils.isNotBlank(autoSyncTimeString)) {
                autoSyncTime = Boolean.valueOf(autoSyncTimeString);
            }

            String pinCodeString = Objects.toString(config.get("pinCode"), null);
            if (StringUtils.isNotBlank(pinCodeString)) {
                pinCode = pinCodeString;
            }

            this.setProperlyConfigured(true);
        }
    }

    /**
     * Open a TCP or Serial connection to the PowerMax Alarm Panel
     */
    private void openConnection() {
        PowerMaxCommDriver.initTheCommDriver(serialPort, ipAddress, tcpPort);
        PowerMaxCommDriver comm = PowerMaxCommDriver.getTheCommDriver();
        if (comm != null) {
            comm.addEventListener(this);
            connected = comm.open();
            if (serialPort != null) {
                logger.info("PowerMax alarm binding: serial connection ({}): {}", serialPort,
                        connected ? "connected" : "disconnected");
            } else if (ipAddress != null) {
                logger.info("PowerMax alarm binding: TCP connection (IP {} port {}): {}", ipAddress, tcpPort,
                        connected ? "connected" : "disconnected");
            }
        } else {
            connected = false;
        }
        logger.debug("openConnection(): {}", connected ? "connected" : "disconnected");
    }

    /**
     * Close TCP or Serial connection to the PowerMax Alarm Panel and remove the Event Listener
     */
    private void closeConnection() {
        PowerMaxCommDriver comm = PowerMaxCommDriver.getTheCommDriver();
        if (comm != null) {
            comm.close();
            comm.removeEventListener(this);
        }
        connected = false;
        logger.debug("closeConnection(): disconnected");
    }

    /**
     * PowerMax Alarm incoming message event handler
     *
     * @param event
     */
    @Override
    public void powerMaxEventReceived(EventObject event) {
        PowerMaxEvent powerMaxEvent = (PowerMaxEvent) event;
        PowerMaxBaseMessage message = powerMaxEvent.getPowerMaxMessage();

        if (logger.isDebugEnabled()) {
            logger.debug("powerMaxEventReceived(): received message {}", (message.getReceiveType() != null)
                    ? message.getReceiveType().toString() : String.format("%02X", message.getCode()));
        }

        if (message instanceof PowerMaxInfoMessage) {
            ((PowerMaxInfoMessage) message).setAutoSyncTime(autoSyncTime);
        }

        PowerMaxState updateState = message.handleMessage();
        if (updateState != null) {

            if ((currentState.isPowerlinkMode() != null) && currentState.isPowerlinkMode().equals(Boolean.TRUE)
                    && (updateState.isDownloadSetupRequired() != null)
                    && updateState.isDownloadSetupRequired().equals(Boolean.TRUE)) {
                // After Enrolling Powerlink or if a reset is required
                logger.info("PowerMax alarm binding: Reset");
                PowerMaxCommDriver.getTheCommDriver().startDownload();
                if (currentState.getLastKeepAlive() != null) {
                    currentState.setLastKeepAlive(System.currentTimeMillis());
                }
            } else if ((currentState.isPowerlinkMode() != null) && currentState.isPowerlinkMode().equals(Boolean.FALSE)
                    && (updateState.getLastKeepAlive() != null)) {
                // Were are in standard mode but received a keep alive message
                // so we switch in PowerLink mode
                logger.info("PowerMax alarm binding: Switching to Powerlink mode");
                PowerMaxCommDriver.getTheCommDriver().startDownload();
            }

            boolean doProcessSettings = (updateState.isPowerlinkMode() != null);

            for (int i = 1; i <= PowerMaxPanelSettings.getThePanelSettings().getNbZones(); i++) {
                if ((updateState.isSensorArmed(i) != null) && updateState.isSensorArmed(i).equals(Boolean.TRUE)
                        && (currentState.isSensorBypassed(i) != null)
                        && currentState.isSensorBypassed(i).equals(Boolean.TRUE)) {
                    updateState.setSensorArmed(i, false);
                }
            }

            updateState.keepOnlyDifferencesWith(currentState);
            updateItemsFromAlarmState(updateState);
            currentState.merge(updateState);

            if (updateState.getUpdateSettings() != null) {
                PowerMaxPanelSettings.getThePanelSettings().updateRawSettings(updateState.getUpdateSettings());
            }

            if (doProcessSettings) {
                // There is a change of mode (standard or Powerlink)
                PowerMaxPanelSettings.getThePanelSettings().process(currentState.isPowerlinkMode(), panelType,
                        PowerMaxCommDriver.getTheCommDriver().getSyncTimeCheck());
                updateItemsFromPanelSettings();
                if (currentState.isPowerlinkMode()) {
                    logger.info("PowerMax alarm binding: running in Powerlink mode");
                    PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.RESTORE);
                } else {
                    logger.info("PowerMax alarm binding: running in Standard mode");
                    PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.ZONESNAME);
                    PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.ZONESTYPE);
                    PowerMaxCommDriver.getTheCommDriver().sendMessage(PowerMaxSendType.STATUS);
                }
                PowerMaxCommDriver.getTheCommDriver().exitDownload();
            }
        }
    }

    /**
     * Post item updates on the bus to match a new alarm system state
     *
     * @param selector
     *            filter on selector type; if null, no filter on selector type
     * @param state
     *            the alarm system state
     */
    private void updateItemsFromAlarmState(PowerMaxSelectorType selector, PowerMaxState state) {
        updateItemsFromAlarmState(null, null, selector, state);
    }

    /**
     * Post item updates on the bus to match a new alarm system state
     *
     * @param state
     *            the alarm system state
     */
    private void updateItemsFromAlarmState(PowerMaxState state) {
        updateItemsFromAlarmState(null, null, null, state);
    }

    /**
     * Post item updates on the bus to match a new alarm system state
     *
     * @param provider
     *            filter on provider; if null, no filter on provider
     * @param name
     *            filter on item name; if null, no filter on item name
     * @param selector
     *            filter on selector type; if null, no filter on selector type
     * @param state
     *            the alarm system state
     */
    private synchronized void updateItemsFromAlarmState(PowerMaxBindingProvider provider, String name,
            PowerMaxSelectorType selector, PowerMaxState state) {

        if (state == null) {
            return;
        }
        if (itemUpdateDisabled) {
            logger.debug("updateItemsFromAlarmState(): items update disabled");
            return;
        }

        if (provider == null) {
            for (PowerMaxBindingProvider prov : providers) {
                if (prov != null) {
                    updateItemsFromAlarmState(prov, name, selector, state);
                }
            }
        } else {
            for (String itemName : provider.getItemNames()) {
                if ((name == null) || itemName.equals(name)) {
                    String value = null;
                    String value2 = null;
                    PowerMaxBindingConfig config = provider.getConfig(itemName);
                    Integer num = config.getSelectorIntParam();
                    if ((selector == null) || (selector == config.getSelectorType())) {
                        switch (config.getSelectorType()) {
                            case PANEL_MODE:
                                value = state.getPanelMode();
                                break;
                            case PARTITION_STATUS:
                                value = state.getStatusStr();
                                break;
                            case PARTITION_READY:
                                if (state.isReady() != null) {
                                    value = state.isReady() ? OnOffType.ON.toString() : OnOffType.OFF.toString();
                                }
                                break;
                            case PARTITION_BYPASS:
                                if (state.isBypass() != null) {
                                    value = state.isBypass() ? OnOffType.ON.toString() : OnOffType.OFF.toString();
                                }
                                break;
                            case PARTITION_ALARM:
                                if (state.isAlarmActive() != null) {
                                    value = state.isAlarmActive() ? OnOffType.ON.toString() : OnOffType.OFF.toString();
                                }
                                break;
                            case PANEL_TROUBLE:
                                if (state.isTrouble() != null) {
                                    value = state.isTrouble() ? OnOffType.ON.toString() : OnOffType.OFF.toString();
                                }
                                break;
                            case PANEL_ALERT_IN_MEMORY:
                                if (state.isAlertInMemory() != null) {
                                    value = state.isAlertInMemory() ? OnOffType.ON.toString()
                                            : OnOffType.OFF.toString();
                                }
                                break;
                            case EVENT_LOG:
                                if ((num != null) && (state.getEventLog(num) != null)) {
                                    value = state.getEventLog(num);
                                }
                                break;
                            case PARTITION_ARMED:
                                if (state.isArmed() != null) {
                                    value = state.isArmed() ? OnOffType.ON.toString() : OnOffType.OFF.toString();
                                }
                                break;
                            case PARTITION_ARM_MODE:
                                value = state.getShortArmMode();
                                break;
                            case ZONE_STATUS:
                                if ((num != null) && (state.isSensorTripped(num) != null)) {
                                    value = state.isSensorTripped(num) ? OpenClosedType.OPEN.toString()
                                            : OpenClosedType.CLOSED.toString();
                                    value2 = state.isSensorTripped(num) ? OnOffType.ON.toString()
                                            : OnOffType.OFF.toString();
                                }
                                break;
                            case ZONE_LAST_TRIP:
                                if ((num != null) && (state.getSensorLastTripped(num) != null)) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTimeInMillis(state.getSensorLastTripped(num));
                                    value = new DateTimeType(cal).toString();
                                }
                                break;
                            case ZONE_BYPASSED:
                                if ((num != null) && (state.isSensorBypassed(num) != null)) {
                                    value = state.isSensorBypassed(num) ? OnOffType.ON.toString()
                                            : OnOffType.OFF.toString();
                                }
                                break;
                            case ZONE_ARMED:
                                if ((num != null) && (state.isSensorArmed(num) != null)) {
                                    value = state.isSensorArmed(num) ? OnOffType.ON.toString()
                                            : OnOffType.OFF.toString();
                                }
                                break;
                            case ZONE_LOW_BATTERY:
                                if ((num != null) && (state.isSensorLowBattery(num) != null)) {
                                    value = state.isSensorLowBattery(num) ? OnOffType.ON.toString()
                                            : OnOffType.OFF.toString();
                                }
                                break;
                            case PGM_STATUS:
                                if (state.getPGMX10DeviceStatus(0) != null) {
                                    value = state.getPGMX10DeviceStatus(0) ? OnOffType.ON.toString()
                                            : OnOffType.OFF.toString();
                                }
                                break;
                            case X10_STATUS:
                                if ((num != null) && (state.getPGMX10DeviceStatus(num) != null)) {
                                    value = state.getPGMX10DeviceStatus(num) ? OnOffType.ON.toString()
                                            : OnOffType.OFF.toString();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    State itemState = null;
                    if (value != null) {
                        itemState = TypeParser.parseState(config.getAcceptedDataTypes(), value);
                    }
                    if ((itemState == null) && (value2 != null)) {
                        itemState = TypeParser.parseState(config.getAcceptedDataTypes(), value2);
                    }
                    if (itemState != null) {
                        eventPublisher.postUpdate(itemName, itemState);
                    }
                }
            }
        }
    }

    /**
     * Post item updates on the bus to match the alarm panel settings
     */
    private void updateItemsFromPanelSettings() {
        updateItemsFromPanelSettings(null, null, null);
    }

    /**
     * Post item updates on the bus to match the alarm panel settings
     *
     * @param provider
     *            filter on provider; if null, no filter on provider
     * @param name
     *            filter on item name; if null, no filter on item name
     * @param selector
     *            filter on selector type; if null, no filter on selector type
     */
    private synchronized void updateItemsFromPanelSettings(PowerMaxBindingProvider provider, String name,
            PowerMaxSelectorType selector) {

        if (itemUpdateDisabled) {
            logger.debug("updateItemsFromPanelSettings(): items update disabled");
            return;
        }

        if (provider == null) {
            for (PowerMaxBindingProvider prov : providers) {
                if (prov != null) {
                    updateItemsFromPanelSettings(prov, name, selector);
                }
            }
        } else {
            PowerMaxPanelSettings settings = PowerMaxPanelSettings.getThePanelSettings();

            for (String itemName : provider.getItemNames()) {
                if ((name == null) || itemName.equals(name)) {
                    String value = null;
                    PowerMaxBindingConfig config = provider.getConfig(itemName);
                    if ((selector == null) || (selector == config.getSelectorType())) {
                        switch (config.getSelectorType()) {
                            case PANEL_TYPE:
                                value = (settings.getPanelType() != null) ? settings.getPanelType().getLabel() : null;
                                break;
                            case PANEL_SERIAL:
                                value = settings.getPanelSerial();
                                break;
                            case PANEL_EPROM:
                                value = settings.getPanelEprom();
                                break;
                            case PANEL_SOFTWARE:
                                value = settings.getPanelSoftware();
                                break;
                            default:
                                break;
                        }
                    }
                    if (value != null) {
                        State itemState = TypeParser.parseState(config.getAcceptedDataTypes(), value);
                        if (itemState != null) {
                            eventPublisher.postUpdate(itemName, itemState);
                        }
                    }
                }
            }
        }
    }

}
