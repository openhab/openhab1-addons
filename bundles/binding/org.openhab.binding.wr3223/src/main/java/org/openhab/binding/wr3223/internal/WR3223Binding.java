/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.wr3223.WR3223BindingProvider;
import org.openhab.binding.wr3223.WR3223CommandType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an active binding to control the WR3223. To control the WR3223 over RS232/USB it must receive
 * at least every 20 second a message.
 *
 * @author Michael Fraefel
 * @since 1.8.0
 */
public class WR3223Binding extends AbstractActiveBinding<WR3223BindingProvider> {

    private static final WR3223CommandType[] READ_COMMANDS = { WR3223CommandType.TEMPERATURE_EVAPORATOR,
            WR3223CommandType.TEMPERATURE_CONDENSER, WR3223CommandType.TEMPERATURE_OUTSIDE,
            WR3223CommandType.TEMPERATURE_OUTGOING_AIR, WR3223CommandType.TEMPERATURE_AFTER_HEAT_EXCHANGER,
            WR3223CommandType.TEMPERATURE_SUPPLY_AIR, WR3223CommandType.TEMPERATURE_AFTER_BRINE_PREHEATING,
            WR3223CommandType.TEMPERATURE_AFTER_PREHEATING_RADIATOR, WR3223CommandType.VENTILATION_LEVEL,
            WR3223CommandType.ROTATION_SPEED_SUPPLY_AIR_MOTOR, WR3223CommandType.ROTATION_SPEED_EXHAUST_AIR_MOTOR,
            WR3223CommandType.OPERATION_MODE, WR3223CommandType.TEMPERATURE_SUPPLY_AIR_TARGET,
            WR3223CommandType.HEAT_FEEDBACK_RATE, WR3223CommandType.SPEED_DEVIATION_MAX_LEVEL_1,
            WR3223CommandType.SPEED_DEVIATION_MAX_LEVEL_2, WR3223CommandType.SPEED_DEVIATION_MAX_LEVEL_3,
            WR3223CommandType.SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_1,
            WR3223CommandType.SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_2,
            WR3223CommandType.SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_3,
            WR3223CommandType.AIR_EXCHANGE_DECREASE_OUTSIDE_TEMPERATURE, WR3223CommandType.VENTILATION_SPEED_LEVEL_1,
            WR3223CommandType.VENTILATION_SPEED_LEVEL_2, WR3223CommandType.VENTILATION_SPEED_LEVEL_3,
            WR3223CommandType.SUMMER_EARTH_HEAT_EXCHANGER_ACTIVATION_TEMPERATURE,
            WR3223CommandType.WINTER_EARTH_HEAT_EXCHANGER_ACTIVATION_TEMPERATURE,
            WR3223CommandType.DEFROSTING_START_TEMPERATURE, WR3223CommandType.DEFROSTING_END_TEMPERATURE,
            WR3223CommandType.DEFROSTING_VENTILATION_LEVEL, WR3223CommandType.DEFROSTING_HOLD_OFF_TIME,
            WR3223CommandType.DEFROSTING_OVERTRAVEL_TIME, WR3223CommandType.DEFROSTING_HEAT_FEEDBACK_RATE,
            WR3223CommandType.SOLAR_MAX, WR3223CommandType.SOLAR_USAGE, WR3223CommandType.DELTA_T_OFF,
            WR3223CommandType.DELTA_T_ON, WR3223CommandType.TEMPERATURE_CONDENSER_MAX,
            WR3223CommandType.IDLE_TIME_PRESSURE_REDUCTION, WR3223CommandType.SUPPORT_FAN_LEVEL_1_EARTH_HEAT_EXCHANGER,
            WR3223CommandType.SUPPORT_FAN_LEVEL_2_EARTH_HEAT_EXCHANGER,
            WR3223CommandType.SUPPORT_FAN_LEVEL_3_EARTH_HEAT_EXCHANGER, WR3223CommandType.CONTROL_VOLTAGE_OUTGOING_AIR,
            WR3223CommandType.CONTROL_VOLTAGE_SUPPLY_AIR, WR3223CommandType.WARM_WATER_TARGET_TEMPERATURE,
            WR3223CommandType.HEAT_PUMP_OPEN, WR3223CommandType.ADDITIONAL_HEATER_OPEN };

    private static final WR3223CommandType[] WRITE_COMMANDS = { WR3223CommandType.OPERATION_MODE,
            WR3223CommandType.TEMPERATURE_SUPPLY_AIR_TARGET, WR3223CommandType.SPEED_DEVIATION_MAX_LEVEL_1,
            WR3223CommandType.SPEED_DEVIATION_MAX_LEVEL_2, WR3223CommandType.SPEED_DEVIATION_MAX_LEVEL_3,
            WR3223CommandType.SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_1,
            WR3223CommandType.SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_2,
            WR3223CommandType.SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_3,
            WR3223CommandType.AIR_EXCHANGE_DECREASE_OUTSIDE_TEMPERATURE, WR3223CommandType.VENTILATION_SPEED_LEVEL_1,
            WR3223CommandType.VENTILATION_SPEED_LEVEL_2, WR3223CommandType.VENTILATION_SPEED_LEVEL_3,
            WR3223CommandType.SUMMER_EARTH_HEAT_EXCHANGER_ACTIVATION_TEMPERATURE,
            WR3223CommandType.WINTER_EARTH_HEAT_EXCHANGER_ACTIVATION_TEMPERATURE,
            WR3223CommandType.DEFROSTING_START_TEMPERATURE, WR3223CommandType.DEFROSTING_END_TEMPERATURE,
            WR3223CommandType.DEFROSTING_VENTILATION_LEVEL, WR3223CommandType.DEFROSTING_HOLD_OFF_TIME,
            WR3223CommandType.DEFROSTING_OVERTRAVEL_TIME, WR3223CommandType.DEFROSTING_HEAT_FEEDBACK_RATE,
            WR3223CommandType.SOLAR_MAX, WR3223CommandType.DELTA_T_OFF, WR3223CommandType.DELTA_T_ON,
            WR3223CommandType.TEMPERATURE_CONDENSER_MAX, WR3223CommandType.IDLE_TIME_PRESSURE_REDUCTION,
            WR3223CommandType.SUPPORT_FAN_LEVEL_1_EARTH_HEAT_EXCHANGER,
            WR3223CommandType.SUPPORT_FAN_LEVEL_2_EARTH_HEAT_EXCHANGER,
            WR3223CommandType.SUPPORT_FAN_LEVEL_3_EARTH_HEAT_EXCHANGER, WR3223CommandType.WARM_WATER_TARGET_TEMPERATURE,
            WR3223CommandType.HEAT_PUMP_OPEN, WR3223CommandType.ADDITIONAL_HEATER_OPEN };

    private static final Logger logger = LoggerFactory.getLogger(WR3223Binding.class);

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
     * method and must not be accessed anymore once the deactivate() method was called or before activate()
     * was called.
     */
    private BundleContext bundleContext;

    /**
     * the refresh interval which is used to poll values from the WR3223
     * server (optional, defaults to 15000ms)
     */
    private long refreshInterval = 15000;

    /**
     * Host if connection over IP is used.
     */
    private String host;

    /**
     * Port if connection over IP is used.
     */
    private int port;

    /**
     * Serial port if connection over serial interface is used.
     */
    private String serialPort;

    /**
     * Controller address.
     */
    private byte controllerAddr = 1;

    /**
     * WR3223 Connector
     */
    private AbstractWR3223Connector connector;

    /**
     * Status of the WR3223
     */
    private StatusValueHolder statusHolder = new StatusValueHolder();

    /**
     * Store the value to update
     */
    private Map<WR3223CommandType, Integer> updateMap = new HashMap<WR3223CommandType, Integer>();

    /**
     * Called by the SCR to activate the component with its configuration read from CAS
     *
     * @param bundleContext BundleContext of the Bundle that defines this component
     * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
        this.bundleContext = bundleContext;
        configure(configuration);

        // Default start values
        updateMap.put(WR3223CommandType.OPERATION_MODE, 3);
        updateMap.put(WR3223CommandType.TEMPERATURE_SUPPLY_AIR_TARGET, 20);
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
     *
     * @param configuration Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration) {
        configure(configuration);
    }

    /**
     * Called by the SCR to deactivate the component when either the configuration is removed or
     * mandatory references are no longer satisfied or the component has simply been stopped.
     *
     * @param reason Reason code for the deactivation:<br>
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
        if (connector != null) {
            try {
                connector.close();
            } catch (IOException ex) {
                logger.error("Error by closing connector.", ex);
            }
        }
    }

    /**
     * Configure binding.
     *
     * @param configuration
     */
    private void configure(final Map<String, Object> configuration) {
        // Configure refresh
        String refreshIntervalString = (String) configuration.get("refresh");
        if (StringUtils.isNotBlank(refreshIntervalString)) {
            refreshInterval = Long.parseLong(refreshIntervalString);
            if (refreshInterval > 20000) {
                logger.warn("Refresh interval should not be over 20 seconds.");
            }
        }

        // Controller
        String controllerAddrString = (String) configuration.get("controllerAddr");
        if (StringUtils.isNotBlank(controllerAddrString)) {
            controllerAddr = Byte.parseByte(controllerAddrString);
        }

        // Configure connection
        String hostString = (String) configuration.get("host");
        String portString = (String) configuration.get("port");
        String serialPortString = (String) configuration.get("serialPort");
        if (StringUtils.isNotBlank(hostString) && StringUtils.isNotBlank(portString)) {
            host = hostString;
            port = Integer.parseInt(portString);
            serialPort = null;
            logger.info("Connect over IP to host {}:{}", host, port);
            setProperlyConfigured(true);
        } else if (StringUtils.isNotBlank(serialPortString)) {
            serialPort = serialPortString;
            host = null;
            logger.info("Connect over serial port {}", serialPort);
            setProperlyConfigured(true);
        } else {
            setProperlyConfigured(false);
            logger.error("No connection configured");
        }

        // Close the connector if already one is open.
        if (connector != null) {
            try {
                connector.close();
            } catch (IOException ex) {
                logger.error("Error by closing connector.", ex);
            }
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
        return "WR3223 Refresh Service";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void execute() {

        // Connector if not already connected.
        try {
            if (connector == null) {
                if (host != null) {
                    TcpWR3223Connector tcpConnector = new TcpWR3223Connector();
                    tcpConnector.connect(host, port);
                    connector = tcpConnector;
                    logger.info("Connected to WR3223 over tcp to host {}:{}.", host, port);
                } else if (serialPort != null) {
                    SerialWR3223Connector serialConnector = new SerialWR3223Connector();
                    serialConnector.connect(serialPort, 9600);
                    connector = serialConnector;
                    logger.info("Connected to WR3223 over serial port {}.", serialPort);
                }
            }
        } catch (IOException ex) {
            logger.error("Couldn't establish connection to WR3223.", ex);
            connector = null;
            return;
        }

        try {
            // Read relais
            RelaisValueDecoder relais = RelaisValueDecoder.valueOf(connector.read(controllerAddr, WR3223Commands.RL));

            // Write values if no control device connected
            if (!relais.isControlDeviceActive()) {
                if (connector.write(controllerAddr, WR3223Commands.SW, statusHolder.getStatusValue())) {

                    // Commit value updates to WR3223
                    Iterator<Entry<WR3223CommandType, Integer>> it = updateMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Entry<WR3223CommandType, Integer> update = it.next();
                        if (connector.write(controllerAddr, update.getKey().getWr3223Command(),
                                String.valueOf(update.getValue()))) {
                            it.remove();
                        }
                    }
                } else {
                    logger.error("Coudn't send keep alive message to WR3223.");
                }
            } else {
                logger.warn(
                        "The control device is activ! Openhab can only control the WR3223, when the control device is removed. (Bedienteil)");
            }

            // Publish relais values
            publishValueToBoundItems(WR3223CommandType.COMPRESSOR, relais.isCompressor());
            publishValueToBoundItems(WR3223CommandType.ADDITIONAL_HEATER, relais.isAdditionalHeater());
            publishValueToBoundItems(WR3223CommandType.PREHEATING_RADIATOR_ACTIVE, relais.isPreHeaterRadiatorActive());
            publishValueToBoundItems(WR3223CommandType.BYPASS, !relais.isBypass());
            publishValueToBoundItems(WR3223CommandType.BYPASS_RELAY, relais.isBypassRelay());
            publishValueToBoundItems(WR3223CommandType.CONTROL_DEVICE_ACTIVE, relais.isControlDeviceActive());
            publishValueToBoundItems(WR3223CommandType.EARTH_HEAT_EXCHANGER, relais.isEarthHeatExchanger());
            publishValueToBoundItems(WR3223CommandType.MAGNET_VALVE, relais.isMagnetValve());
            publishValueToBoundItems(WR3223CommandType.OPENHAB_INTERFACE_ACTIVE, relais.isOpenhabInterfaceActive());
            publishValueToBoundItems(WR3223CommandType.PREHEATING_RADIATOR, relais.isPreheatingRadiator());
            publishValueToBoundItems(WR3223CommandType.VENTILATION_LEVEL_AVAILABLE,
                    relais.isVentilationLevelAvailable());
            publishValueToBoundItems(WR3223CommandType.WARM_WATER_POST_HEATER, relais.isWarmWaterPostHeater());

            // Read and publish other values from WR3223
            for (WR3223CommandType readCommand : READ_COMMANDS) {
                if (!updateMap.containsKey(readCommand)) {
                    readAndPublishValue(readCommand);
                } else {
                    logger.info(
                            "Skip reading values for command {} from WR3223, because an updated values must fist be send to WR3223.",
                            readCommand.getCommand());
                }
            }
        } catch (IOException e) {
            logger.error("Communication error to WR3223.", e);
            if (connector != null) {
                try {
                    connector.close();
                } catch (IOException e1) {
                    logger.error("Couldn't close communication to WR3223.", e1);
                }
            }
            connector = null;
        }

    }

    /**
     * Read value of given command and publish it to the bus.
     *
     * @param wr3223CommandType
     * @throws IOException
     */
    private void readAndPublishValue(WR3223CommandType wr3223CommandType) throws IOException {
        List<String> itemNames = getBoundItemsForType(wr3223CommandType);
        if (itemNames.size() > 0) {
            String value = connector.read(controllerAddr, wr3223CommandType.getWr3223Command());
            publishValueToItems(itemNames, wr3223CommandType, value);
        }
    }

    /**
     * Publish the value to all bound items.
     *
     * @param wr3223CommandType
     * @param value
     */
    private void publishValueToBoundItems(WR3223CommandType wr3223CommandType, Object value) {
        List<String> itemNames = getBoundItemsForType(wr3223CommandType);
        if (itemNames.size() > 0) {
            publishValueToItems(itemNames, wr3223CommandType, value);
        }
    }

    /**
     * Publish the value to the given items.
     *
     * @param itemNames
     * @param wr3223CommandType
     * @param value
     */
    private void publishValueToItems(List<String> itemNames, WR3223CommandType wr3223CommandType, Object value) {
        if (value != null) {
            State state = null;
            if (wr3223CommandType.getItemClass() == NumberItem.class) {
                try {
                    state = DecimalType.valueOf(value.toString().trim());
                } catch (NumberFormatException nfe) {
                    logger.error("Can't set value {} to item type {} because it's not a decimal number.", value,
                            wr3223CommandType.getCommand());
                }
            } else if (wr3223CommandType.getItemClass() == SwitchItem.class) {
                state = parseBooleanValue(value);
            } else if (wr3223CommandType.getItemClass() == ContactItem.class) {
                state = parseBooleanValue(value) == OnOffType.ON ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
            } else {
                logger.error("Can't set value {} to item type {}.", value, wr3223CommandType.getCommand());
            }
            if (state != null) {
                for (String itemName : itemNames) {
                    eventPublisher.postUpdate(itemName, state);
                }
            }
        } else {
            logger.error("Can't set NULL value to item type {}.", wr3223CommandType.getCommand());

        }
    }

    /**
     * Versucht aus einem Objekt ein On/Off Status zu lesen
     *
     * @param value
     * @return
     */
    private State parseBooleanValue(Object value) {
        State state;
        String valStr = value.toString().trim();
        state = (valStr.equalsIgnoreCase("true") || valStr.equals("1") || valStr.equals("1.")) ? OnOffType.ON
                : OnOffType.OFF;
        return state;
    }

    private List<String> getBoundItemsForType(WR3223CommandType wr3223CommandType) {
        List<String> itemNames = new ArrayList<String>();
        for (WR3223BindingProvider provider : providers) {
            for (String itemName : provider.getItemNamesForType(wr3223CommandType)) {
                itemNames.add(itemName);
            }
        }
        return itemNames;
    }

    private WR3223CommandType getWR3223BindingConfig(String itemName) {
        WR3223CommandType type = null;
        Iterator<WR3223BindingProvider> providerIt = providers.iterator();
        while (providerIt.hasNext() && type == null) {
            type = providerIt.next().getWR3223CommandTypeForItemName(itemName);
        }
        return type;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
        WR3223CommandType type = getWR3223BindingConfig(itemName);
        if (type == null) {
            logger.error("Item {} is not bound to WR3223 binding.", itemName);
        } else {
            if (type == WR3223CommandType.VENTILATION_LEVEL) {
                if (command instanceof DecimalType) {
                    int value = ((DecimalType) command).intValue();
                    if (value >= 0 && value <= 3) {
                        statusHolder.setVentilationLevel(value);
                    } else {
                        // FIXME Error
                    }
                } else {
                    logger.warn("WR3223 item {} must be from type:{}.", itemName, DecimalType.class.getSimpleName());
                }
            } else {
                for (WR3223CommandType t : WRITE_COMMANDS) {
                    if (type == t) {
                        if (type.getItemClass() == NumberItem.class && command instanceof DecimalType) {
                            int value = ((DecimalType) command).intValue();
                            if (type.getMinValue() != null && type.getMinValue() > value) {
                                logger.warn("Value of WR3223 command {} must be bigger or equals {}.",
                                        type.getCommand(), type.getMinValue());
                            } else if (type.getMaxValue() != null && type.getMaxValue() < value) {
                                logger.warn("Value of WR3223 command {} must be lower or equals {}.", type.getCommand(),
                                        type.getMaxValue());
                            } else {
                                updateMap.put(type, value);
                            }
                        } else if (type.getItemClass() == SwitchItem.class && command instanceof OnOffType) {
                            if (command == OnOffType.ON) {
                                updateMap.put(type, 1);
                            } else if (command == OnOffType.OFF) {
                                updateMap.put(type, 0);
                            } else {
                                logger.warn("WR3223 command {} should be of type {}.", type.getCommand(),
                                        type.getItemClass());
                            }

                        } else {
                            logger.warn("WR3223 command {} should be of type {}.", type.getCommand(),
                                    type.getItemClass());
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Hold the values which must be send every 20 seconds to the WR3223. The values received from the bus are stored in
     * this class.
     *
     * @author Michael Fraefel
     *
     */
    private static final class StatusValueHolder {

        private boolean heatPumpOn = false;
        private int ventilationLevel = 2;
        private boolean additionalHeatingOn = false;
        private boolean coolingOn = false;

        /**
         * @param "Wärmepumpe  ein (bei Anlagen mit Wärmepumpe)"
         */
        public void setHeatPumpOn(boolean heatPumpOn) {
            this.heatPumpOn = heatPumpOn;
        }

        /**
         * @param "Lüftungsstufe 0-3, 0=Aus"
         */
        public void setVentilationLevel(int ventilationLevel) {
            this.ventilationLevel = ventilationLevel;
        }

        /**
         * @param "Zusatzheizung ein"
         */
        public void setAdditionalHeatingOn(boolean additionalHeatingOn) {
            this.additionalHeatingOn = additionalHeatingOn;
        }

        /**
         * @param "kühlen      (bei Anlagen mit Wärmepumpe)"
         */
        public void setCoolingOn(boolean coolingOn) {
            this.coolingOn = coolingOn;
        }

        public String getStatusValue() {
            int data = 0;
            if (!heatPumpOn) {
                data += 1;
            }
            if (ventilationLevel == 2 || ventilationLevel == 1) {
                data += 2;
            }
            if (ventilationLevel == 3 || ventilationLevel == 1) {
                data += 4;
            }
            if (!additionalHeatingOn) {
                data += 8;
            }
            if (ventilationLevel == 0) {
                data += 16;
            }
            if (!coolingOn) {
                data += 32;
            }
            return String.valueOf(data);
        }
    }

    /**
     * Coding of the RL command.
     *
     * @author Michael Fraefel
     *
     */
    private static final class RelaisValueDecoder {

        private static final int FLAG_COMPRESSOR = 1;
        private static final int FLAG_ADDITIONAL_HEATER = 2;
        private static final int FLAG_EARTH_HEAT_EXCHANGER = 4;
        private static final int FLAG_BYPASS = 8;
        private static final int FLAG_PREHEATING_RADIATOR = 16;
        private static final int FLAG_BYPASS_RELAY = 32;
        private static final int FLAG_CONTROL_DEVICE_ACTIVE = 64;
        private static final int FLAG_OPENHAB_INTERFACE_ACTIVE = 128;
        private static final int FLAG_VENTILATION_LEVEL_AVAILABLE = 256;
        private static final int FLAG_WARM_WATER_POST_HEATER = 512;
        private static final int FLAG_MAGNET_VALVE = 2048;
        private static final int FLAG_PRE_HEATER_RADIATOR_ACTIVE = 4096;

        private boolean compressor;
        private boolean additionalHeater;
        private boolean earthHeatExchanger;
        private boolean bypass;
        private boolean bypassRelay;
        private boolean preheatingRadiator;
        private boolean controlDeviceActive;
        private boolean openhabInterfaceActive;
        private boolean ventilationLevelAvailable;
        private boolean warmWaterPostHeater;
        private boolean magnetValve;
        private boolean preHeaterRadiatorActive;

        public static RelaisValueDecoder valueOf(String read) {
            RelaisValueDecoder relaisValue = new RelaisValueDecoder();
            read = read.trim();
            int decPoint = read.indexOf(".");
            if (decPoint > 0) {
                read = read.substring(0, decPoint);
            }
            int value = Integer.valueOf(read.trim());
            if ((value & FLAG_COMPRESSOR) == FLAG_COMPRESSOR) {
                relaisValue.compressor = true;
            }
            if ((value & FLAG_ADDITIONAL_HEATER) == FLAG_ADDITIONAL_HEATER) {
                relaisValue.additionalHeater = true;
            }
            if ((value & FLAG_EARTH_HEAT_EXCHANGER) == FLAG_EARTH_HEAT_EXCHANGER) {
                relaisValue.earthHeatExchanger = true;
            }
            if ((value & FLAG_BYPASS) == FLAG_BYPASS) {
                relaisValue.bypass = true;
            }
            if ((value & FLAG_PREHEATING_RADIATOR) == FLAG_PREHEATING_RADIATOR) {
                relaisValue.preheatingRadiator = true;
            }
            if ((value & FLAG_BYPASS_RELAY) == FLAG_BYPASS_RELAY) {
                relaisValue.bypassRelay = true;
            }
            if ((value & FLAG_CONTROL_DEVICE_ACTIVE) == FLAG_CONTROL_DEVICE_ACTIVE) {
                relaisValue.controlDeviceActive = true;
            }
            if ((value & FLAG_OPENHAB_INTERFACE_ACTIVE) == FLAG_OPENHAB_INTERFACE_ACTIVE) {
                relaisValue.openhabInterfaceActive = true;
            }
            if ((value & FLAG_VENTILATION_LEVEL_AVAILABLE) == FLAG_VENTILATION_LEVEL_AVAILABLE) {
                relaisValue.ventilationLevelAvailable = true;
            }
            if ((value & FLAG_WARM_WATER_POST_HEATER) == FLAG_WARM_WATER_POST_HEATER) {
                relaisValue.warmWaterPostHeater = true;
            }
            if ((value & FLAG_MAGNET_VALVE) == FLAG_MAGNET_VALVE) {
                relaisValue.magnetValve = true;
            }
            if ((value & FLAG_PRE_HEATER_RADIATOR_ACTIVE) == FLAG_PRE_HEATER_RADIATOR_ACTIVE) {
                relaisValue.preHeaterRadiatorActive = true;
            }
            return relaisValue;
        }

        /**
         * @return "Kompressor Relais"
         */
        public boolean isCompressor() {
            return compressor;
        }

        /**
         * @return "Zusatzheizung Relais"
         */
        public boolean isAdditionalHeater() {
            return additionalHeater;
        }

        /**
         * @return "Erdwärmetauscher"
         */
        public boolean isEarthHeatExchanger() {
            return earthHeatExchanger;
        }

        /**
         * @return "Bypass"
         */
        public boolean isBypass() {
            return bypass;
        }

        /**
         * "Netzrelais Bypass"
         *
         * @return
         */
        public boolean isBypassRelay() {
            return bypassRelay;
        }

        /**
         * @return "Vorheizregister"
         */
        public boolean isPreheatingRadiator() {
            return preheatingRadiator;
        }

        /**
         * @return "Bedienteil aktiv"
         */
        public boolean isControlDeviceActive() {
            return controlDeviceActive;
        }

        /**
         * @return "Bedienung über RS Schnittstelle"
         */
        public boolean isOpenhabInterfaceActive() {
            return openhabInterfaceActive;
        }

        /**
         * @return "Luftstufe vorhanden"
         */
        public boolean isVentilationLevelAvailable() {
            return ventilationLevelAvailable;
        }

        /**
         * @return "WW_Nachheizrgister"
         */
        public boolean isWarmWaterPostHeater() {
            return warmWaterPostHeater;
        }

        /**
         * @return "Magnetventil"
         */
        public boolean isMagnetValve() {
            return magnetValve;
        }

        /**
         * @return "Vorheizen aktiv"
         */
        public boolean isPreHeaterRadiatorActive() {
            return preHeaterRadiatorActive;
        }

    }
}
