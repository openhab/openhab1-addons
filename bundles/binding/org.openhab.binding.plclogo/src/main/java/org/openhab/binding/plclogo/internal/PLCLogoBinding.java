/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.plclogo.PLCLogoBindingConfig;
import org.openhab.binding.plclogo.PLCLogoBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Moka7.S7;
import Moka7.S7Client;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 *
 * @author Lehane Kellett
 * @author Vladimir Grebenschikov
 * @author Alexander Falkenstern
 * @since 1.9.0
 */
public class PLCLogoBinding extends AbstractActiveBinding<PLCLogoBindingProvider> implements ManagedService {
    private final ReentrantLock lock = new ReentrantLock();
    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);
    private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(.*?)$");

    /**
     * the refresh interval which is used to poll values from the PlcLogo
     * server (optional, defaults to 500ms)
     */
    private long refreshInterval = 5000;

    /**
     * Buffer for read/write operations
     */
    private byte data[] = new byte[2048];

    private static Map<String, PLCLogoConfig> controllers = new HashMap<String, PLCLogoConfig>();

    private int ReadLogoDBArea(S7Client client, int size) {
        int result = 0;
        int offset = 0;
        final int bufSize = 1024;

        // read first portion directly to data, to avoid extra copy
        result = client.ReadArea(S7.S7AreaDB, 1, 0, bufSize, data);
        offset = offset + bufSize;

        while ((result == 0) && (offset < size)) {
            byte buffer[] = new byte[Math.min(size - offset, bufSize)];
            result = client.ReadArea(S7.S7AreaDB, 1, offset, buffer.length, buffer);
            System.arraycopy(buffer, 0, data, offset, buffer.length);
            offset = offset + buffer.length;
        }

        return result;
    }

    private void ReconnectLogo(S7Client client) {
        // try and reconnect
        client.Disconnect();
        client.Connect();
        if (client.Connected) {
            logger.warn("Reconnect successful");
        }
    }

    public PLCLogoBinding() {
        logger.info("PLCLogoBinding constuctor");
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
        for (PLCLogoBindingProvider provider : providers) {
            provider.removeBindingChangeListener(this);
        }

        providers.clear();
        Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, PLCLogoConfig> thisEntry = entries.next();
            PLCLogoConfig logoConfig = thisEntry.getValue();
            S7Client LogoS7Client = logoConfig.getS7Client();
            if (LogoS7Client != null) {
                LogoS7Client.Disconnect();
            }
        }
        controllers.clear();
    }

    protected void addBindingProvider(PLCLogoBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(PLCLogoBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    protected String getName() {
        return "PLCLogo Polling Service";
    }

    @Override
    protected void execute() {
        if (!bindingsExist()) {
            logger.debug("There is no existing plclogo binding configuration => refresh cycle aborted!");
            return;
        }

        Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, PLCLogoConfig> thisEntry = entries.next();
            String controller = thisEntry.getKey();
            PLCLogoConfig logoConfig = thisEntry.getValue();
            S7Client LogoS7Client = logoConfig.getS7Client();
            if (LogoS7Client == null) {
                logger.debug("No S7client for {} found", controller);
            } else {
                lock.lock();
                int result = ReadLogoDBArea(LogoS7Client, logoConfig.getMemorySize());
                lock.unlock();

                if (result != 0) {
                    logger.warn("Failed to read memory: {}. Reconnecting...", S7Client.ErrorText(result));
                    ReconnectLogo(LogoS7Client);
                    return;
                }

                // Now have the LOGO! memory (note: not suitable for S7) - more efficient than multiple reads (test
                // shows <14mS to read all)
                // iterate through bindings to see what has changed - this approach assumes a small number (< 100)of
                // bindings
                // otherwise might see what has changed in memory and map to binding
            }
            for (PLCLogoBindingProvider provider : providers) {
                for (String itemName : provider.getItemNames()) {
                    PLCLogoBindingConfig config = provider.getBindingConfig(itemName);
                    if (config.getController().equals(controller)) {
                        // it is for our currently selected controller
                        PLCLogoMemoryConfig rd = config.getRD();
                        int address = -1;
                        try {
                            address = rd.getAddress(logoConfig.getModel());
                        } catch (BindingConfigParseException exception) {
                            logger.error("Invalid address for block {} on {}", rd.getBlockName(), controller);
                            continue;
                        }

                        int currentValue;
                        if (rd.isDigital()) {
                            int bit = -1;
                            try {
                                bit = rd.getBit(logoConfig.getModel());
                            } catch (BindingConfigParseException exception) {
                                logger.error("Invalid bit for block {} on {}", rd.getBlockName(), controller);
                                continue;
                            }
                            currentValue = S7.GetBitAt(data, address, bit) ? 1 : 0;
                        } else {
                            /*
                             * After the data transfer from a LOGO! Base Module to LOGO!Soft Comfort,
                             * you can view only analog values within the range of -32768 to 32767 on LOGO!Soft Comfort.
                             * If an analog value exceeds the value range,
                             * then only the nearest upper limit (32767) or lower limit (-32768) can be displayed.
                             */
                            currentValue = S7.GetShortAt(data, address);
                        }

                        if (config.isSet()) {
	                        if (currentValue == config.getLastValue()) {
	                            continue;
	                        }

	                        int delta = Math.abs(config.getLastValue() - currentValue);
	                        if (!rd.isDigital() && (delta < config.getAnalogDelta())) {
	                            continue;
	                        }
                        }

                        boolean isValid = false;
                        Item item = provider.getItem(itemName);
                        switch (rd.getKind()) {
                            case I:
                            case NI: {
                                isValid = item instanceof ContactItem;
                                break;
                            }
                            case Q:
                            case NQ: {
                                isValid = item instanceof SwitchItem;
                                break;
                            }
                            case M:
                            case VB:
                            case VW: {
                                isValid = item instanceof ContactItem || item instanceof SwitchItem;
                                break;
                            }
                            default: {
                                break;
                            }
                        }

                        if (item instanceof NumberItem || isValid) {
                            eventPublisher.postUpdate(itemName, createState(item, currentValue));
                            config.setLastValue(currentValue);
                        } else {
                            String block = rd.getBlockName();
                            logger.warn("Block {} is incompatible with item {} on {}", block, item.getName(),
                                    controller);
                        }
                    }
                }

            }
        }
    }

    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        // Note itemname is the item name not the controller name/instance!
        //
        super.internalReceiveCommand(itemName, command);
        logger.debug("internalReceiveCommand() is called!");
        for (PLCLogoBindingProvider provider : providers) {
            if (!provider.providesBindingFor(itemName)) {
                continue;
            }

            PLCLogoBindingConfig config = provider.getBindingConfig(itemName);
            if (!controllers.containsKey(config.getController())) {
                logger.warn("Invalid write requested for controller {}", config.getController());
                continue;
            }

            PLCLogoConfig controller = controllers.get(config.getController());

            PLCLogoMemoryConfig wr = config.getWR();
            int address = -1;
            try {
                address = wr.getAddress(controller.getModel());
            } catch (BindingConfigParseException exception) {
                logger.error("Invalid address for block {} on {}", wr.getBlockName(), controller);
                continue;
            }

            int bit = -1;
            try {
                bit = wr.getBit(controller.getModel());
            } catch (BindingConfigParseException exception) {
                logger.error("Invalid bit for block {} on {}", wr.getBlockName(), controller);
                continue;
            }

            if (!wr.isInRange(controller.getModel())) {
                logger.warn("Invalid write request for block {} at address {}", wr.getBlockName(), address);
                continue;
            }

            // Send command to the LOGO! controller memory
            S7Client LogoS7Client = controller.getS7Client();
            if (LogoS7Client == null) {
                logger.debug("No S7client for controller {} found", config.getController());
                continue;
            }

            final byte buffer[] = new byte[2];
            int size = wr.isDigital() ? 1 : 2;

            lock.lock();
            int result = LogoS7Client.ReadArea(S7.S7AreaDB, 1, address, size, buffer);
            logger.debug("Read word from logo memory: at {} {} bytes, result = {}", address, size, result);
            if (result == 0) {
                Item item = config.getItem();
                if (item instanceof NumberItem && !wr.isDigital()) {
                    if (command instanceof DecimalType) {
                        int oldValue = S7.GetShortAt(buffer, 0);
                        int newValue = ((DecimalType) command).intValue();
                        S7.SetWordAt(buffer, 0, newValue);
                        logger.debug("Changed word at {} from {} to {}", address, oldValue, newValue);

                        result = LogoS7Client.WriteArea(S7.S7AreaDB, 1, address, size, buffer);
                        logger.debug("Wrote to memory at {} two bytes: [{}, {}]", address, buffer[0], buffer[1]);
                    }
                } else if (item instanceof SwitchItem && wr.isDigital()) {
                    if (command instanceof OnOffType) {
                        boolean oldValue = S7.GetBitAt(buffer, 0, bit) ? true : false;
                        boolean newValue = command == OnOffType.ON ? true : false;
                        S7.SetBitAt(buffer, 0, bit, newValue);
                        logger.debug("Changed bit {}.{} from {} to {}", address, bit, oldValue, newValue);

                        result = LogoS7Client.WriteArea(S7.S7AreaDB, 1, address, size, buffer);
                        logger.debug("Wrote to memory at {} one byte: [{}]", address, buffer[0]);
                    }
                }

                // If nothing was written and read was ok, nothing will happen here
                if (result != 0) {
                    logger.warn("Failed to write memory: {}. Reconnecting...", S7Client.ErrorText(result));
                    ReconnectLogo(LogoS7Client);
                }
            } else {
                logger.warn("Failed to read memory: {}. Reconnecting...", S7Client.ErrorText(result));
                ReconnectLogo(LogoS7Client);
            }
            lock.unlock();
        }
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        Boolean configured = false;
        if (config != null) {
            String refreshIntervalString = Objects.toString(config.get("refresh"), null);
            if (StringUtils.isNotBlank(refreshIntervalString)) {
                refreshInterval = Long.parseLong(refreshIntervalString);
            }

            if (controllers == null) {
                controllers = new HashMap<String, PLCLogoConfig>();
            }

            Enumeration<String> keys = config.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();

                // the config-key enumeration contains additional keys that we
                // don't want to process here ...
                if ("service.pid".equals(key)) {
                    continue;
                }

                Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);
                if (!matcher.matches()) {
                    continue;
                }

                matcher.reset();
                matcher.find();
                String controllerName = matcher.group(1);
                PLCLogoConfig deviceConfig = controllers.get(controllerName);

                if (deviceConfig == null) {
                    deviceConfig = new PLCLogoConfig();
                    controllers.put(controllerName, deviceConfig);
                    logger.info("Create new config for {}", controllerName);
                }
                if (matcher.group(2).equals("host")) {
                    String ip = config.get(key).toString();
                    deviceConfig.setIP(ip);
                    logger.info("Set host of {}: {}", controllerName, ip);
                    configured = true;
                }
                if (matcher.group(2).equals("remoteTSAP")) {
                    String tsap = config.get(key).toString();
                    deviceConfig.setRemoteTSAP(Integer.decode(tsap));
                    logger.info("Set remote TSAP for {}: {}", controllerName, tsap);
                }
                if (matcher.group(2).equals("localTSAP")) {
                    String tsap = config.get(key).toString();
                    deviceConfig.setLocalTSAP(Integer.decode(tsap));
                    logger.info("Set local TSAP for {}: {}", controllerName, tsap);
                }
                if (matcher.group(2).equals("model")) {
                    PLCLogoModel model = null;
                    String modelName = config.get(key).toString();
                    if (modelName.equalsIgnoreCase("0BA7")) {
                        model = PLCLogoModel.LOGO_MODEL_0BA7;
                    } else if (modelName.equalsIgnoreCase("0BA8")) {
                        model = PLCLogoModel.LOGO_MODEL_0BA8;
                    } else {
                        logger.info("Found unknown model for {}: {}", controllerName, modelName);
                    }

                    if (model != null) {
                        deviceConfig.setModel(model);
                        logger.info("Set model for {}: {}", controllerName, modelName);
                    }
                }
            } // while

            Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, PLCLogoConfig> thisEntry = entries.next();
                String controllerName = thisEntry.getKey();
                PLCLogoConfig deviceConfig = thisEntry.getValue();
                S7Client LogoS7Client = deviceConfig.getS7Client();
                if (LogoS7Client == null) {
                    LogoS7Client = new Moka7.S7Client();
                } else {
                    LogoS7Client.Disconnect();
                }
                LogoS7Client.SetConnectionParams(deviceConfig.getlogoIP(), deviceConfig.getlocalTSAP(),
                        deviceConfig.getremoteTSAP());
                logger.info("About to connect to {}", controllerName);

                if ((LogoS7Client.Connect() == 0) && LogoS7Client.Connected) {
                    logger.info("Connected to PLC LOGO! device {}", controllerName);
                } else {
                    logger.error("Could not connect to PLC LOGO! device {} : {}", controllerName,
                            S7Client.ErrorText(LogoS7Client.LastError));
                    throw new ConfigurationException("Could not connect to PLC LOGO! device ",
                            controllerName + " " + deviceConfig.getlogoIP());
                }
                deviceConfig.setS7Client(LogoS7Client);
            }

            setProperlyConfigured(configured);
        } else {
            logger.info("No configuration for PLCLogoBinding");
        }
    }

    private State createState(Item item, Object value) {
        DecimalType number = null;
        if (value instanceof Number) {
            number = new DecimalType(value.toString());
        }

        State state = null;
        if (item instanceof StringType) {
            state = new StringType((String) value);
        } else if (item instanceof NumberItem) {
            if (number != null) {
                return number;
            } else if (value instanceof String) {
                state = new DecimalType(((String) value).replaceAll("[^\\d|.]", ""));
            }
        } else if (item instanceof SwitchItem && (number != null)) {
            state = (number.intValue() > 0) ? OnOffType.ON : OnOffType.OFF;
        } else if (item instanceof ContactItem && (number != null)) {
            state = (number.intValue() > 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
        }
        return state;
    }

    /**
     * Class which represents a LOGO! online controller/PLC connection parameters
     * and current instance - there may be multiple PLC's
     *
     * @author Lehane Kellett
     * @since 1.9.0
     */
    private class PLCLogoConfig {
        private String logoIP;
        private PLCLogoModel logoModel = PLCLogoModel.LOGO_MODEL_0BA7;
        private int localTSAP = 0x0300;
        private int remoteTSAP = 0x0200;
        private S7Client LogoS7Client;

        public PLCLogoConfig() {
        }

        public void setIP(String logoIP) {
            this.logoIP = logoIP;
        }

        public void setLocalTSAP(int localTSAP) {
            this.localTSAP = localTSAP;
        }

        public void setRemoteTSAP(int remoteTSAP) {
            this.remoteTSAP = remoteTSAP;
        }

        public void setS7Client(S7Client LogoS7Client) {
            this.LogoS7Client = LogoS7Client;
        }

        public String getlogoIP() {
            return logoIP;
        }

        public int getlocalTSAP() {
            return localTSAP;
        }

        public int getremoteTSAP() {
            return remoteTSAP;
        }

        public void setModel(PLCLogoModel model) {
            this.logoModel = model;
        }

        public PLCLogoModel getModel() {
            return logoModel;
        }

        public int getMemorySize() {
            return (logoModel == PLCLogoModel.LOGO_MODEL_0BA8) ? 1470 : 1024;
        }

        public S7Client getS7Client() {
            return LogoS7Client;
        }
    }

}
