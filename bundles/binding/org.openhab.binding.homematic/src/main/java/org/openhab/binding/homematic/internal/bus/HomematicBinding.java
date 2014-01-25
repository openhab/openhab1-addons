/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.config.AdminItem;
import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.DeviceConfigLocator;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.converter.command.CommandConverter;
import org.openhab.binding.homematic.internal.converter.lookup.ConverterLookup;
import org.openhab.binding.homematic.internal.converter.lookup.StateConverterLookupByConfiguredDevices;
import org.openhab.binding.homematic.internal.converter.lookup.StateConverterLookupByCustomConverter;
import org.openhab.binding.homematic.internal.converter.lookup.StateConverterLookupByParameterId;
import org.openhab.binding.homematic.internal.converter.lookup.StateConverterLookupByParameterIdConfigurer;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.internal.device.channel.HMChannel;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.device.physical.rf.DefaultHMRFDevice;
import org.openhab.binding.homematic.internal.xmlrpc.HomematicBindingException;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackHandler;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackReceiver;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackServer;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Homematic binding implementation.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class HomematicBinding extends AbstractActiveBinding<HomematicBindingProvider> implements ManagedService, CallbackReceiver {

    private static final Logger logger = LoggerFactory.getLogger(HomematicBinding.class);

    private static final String CONFIG_KEY_CCU_HOST = "host";
    private static final String CONFIG_KEY_CALLBACK_HOST = "callback.host";
    private static final String CONFIG_KEY_CALLBACK_PORT = "callback.port";
    private static final Integer DEFAULT_CALLBACK_PORT = 9123;
    private static final String CONFIG_KEY_CONNECTION_REFRESH_INTERVALL = "connection.refresh.ms";
    private static final long DEFAULT_INTERVALL_5_MINUTES = TimeUnit.MINUTES.toMillis(5);

    private ConverterLookup converterLookup = new ConverterLookup();

    private CCU<?> ccu;
    private Integer callbackPort;
    private String ccuHost;
    private String callbackHost;
    private CallbackServer cbServer;
    private long checkAlifeIntervallMS;
    private long lastEventTime = 0;

    private StateConverterLookupByConfiguredDevices converterLookupByConfiguredDevices;
    private StateConverterLookupByCustomConverter converterLookupByCustomConverter;
    private StateConverterLookupByParameterId converterLookupByParameterId;

    private Map<String, State> itemStates = new HashMap<String, State>();

    public HomematicBinding() {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                removeCallbackHandler();
            }
        });

        converterLookupByConfiguredDevices = new StateConverterLookupByConfiguredDevices();
        DeviceConfigLocator locator = new DeviceConfigLocator("HM-CC-RT-DN.xml", "HM-LC-Dim1L-Pl.xml", "HM-LC-Bl1PBU-FM.xml",
                "HM-LC-Bl1-FM.xml", "HM-LC-Dim2L-SM.xml", "HM-LC-Dim2L-CV.xml", "HM-LC-Dim1L-CV.xml", "HM-LC-Dim1T-Pl.xml",
                "HM-LC-Dim1T-CV.xml", "HM-LC-Dim2T-SM.xml", "HM-PB-4DIS-WM.xml", "HM-Sec-SD.xml", "HM-Sec-SC.xml", "HM-Sec-RHS.xml");
        List<ConfiguredDevice> configuredDevices = locator.findAll();
        converterLookupByConfiguredDevices.addConfiguredDevices(configuredDevices);
        converterLookup.setConverterLookupByConfiguredDevices(converterLookupByConfiguredDevices);
        converterLookupByCustomConverter = new StateConverterLookupByCustomConverter();
        converterLookup.setConverterLookupByCustomConverter(converterLookupByCustomConverter);
        converterLookupByParameterId = new StateConverterLookupByParameterId();
        new StateConverterLookupByParameterIdConfigurer().configure(converterLookupByParameterId);
        converterLookup.setConverterLookupByParameterId(converterLookupByParameterId);
    }

    @Override
    public void activate() {
        logger.debug("activate");
        super.activate();
        if (isCCUInitialized() && !isCallbackServerInitialized()) {
            registerCallbackHandler();
        }
    }

    @Override
    public void deactivate() {
        logger.debug("deactivate");
        super.deactivate();
        if (isCallbackServerInitialized()) {
            removeCallbackHandler();
        }
    }

    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.debug("Received command {} for item {}", command, itemName);
        for (HomematicBindingProvider provider : providers) {
            logger.debug("Checking provider with names {}", provider.getItemNames());
            if (provider.isAdminItem(itemName)) {
                handleAdminCommand(provider.getAdminItem(itemName), command);
            } else {
                HomematicParameterAddress parameterAddress = provider.getParameterAddress(itemName);
                State actualState = itemStates.get(itemName);
                if (actualState == null) {
                    actualState = UnDefType.NULL;
                }
                String parameterKey = parameterAddress.getParameterId();
                State newState;

                CommandConverter<?, ?> commandConverter = converterLookup.getCommandToBindingValueConverter(itemName, command.getClass());
                if (commandConverter == null) {
                    if (command instanceof State) {
                        logger.debug("CommandConverter not found, using state instead.");
                        State state = (State) command;
                        newState = state;
                    } else {
                        logger.warn("No command converter found for {}. No command will be executed.", parameterAddress);
                        return;
                    }
                } else {
                    newState = commandConverter.convertFrom(actualState, command);
                }
                logger.debug("Setting new state " + newState + " on item " + itemName);
                if (command instanceof StopMoveType && parameterKey.equals(ParameterKey.LEVEL.name())) {
                    // Roller shutter workaround: StopMove commands go to STOP
                    // parameterKey
                    parameterAddress = HomematicParameterAddress.from(parameterAddress.getAddress(), ParameterKey.STOP.name());
                }
                setStateOnDevice(newState, parameterAddress, itemName);
            }
        }
    }

    @Override
    public void handleEvent(Event event) {
        super.handleEvent(event);
    }

    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        itemStates.put(itemName, newState);
        for (HomematicBindingProvider provider : providers) {
            logger.debug("Checking provider with names {}", provider.getItemNames());
            HomematicParameterAddress parameterAddress = provider.getParameterAddress(itemName);
            setStateOnDevice(newState, parameterAddress, itemName);
        }
    }

    @Override
    public Integer event(String interfaceId, String address, String parameterKey, Object valueObject) {
        HomematicParameterAddress parameterAddress = HomematicParameterAddress.from(address, parameterKey);
        logger.debug("Received new value {} for device at {}", valueObject, parameterAddress);
        lastEventTime = System.currentTimeMillis();
        String itemName = getItemNameForParameter(parameterAddress);
        if (itemName != null) {
            StateConverter<?, ?> converter = converterLookup.getBindingValueToStateConverter(itemName);
            if (converter == null) {
                logger.warn("No converter found for " + parameterAddress + " - doing nothing.");
                return null;
            }
            State value = converter.convertTo(valueObject);
            logger.debug("Received new value {} for item {}", value, itemName);
            if (ParameterKey.PRESS_SHORT.name().equals(parameterAddress.getParameterId())) {
                // Workaround for button short not sending an OFF command
                postUpdate(itemName, OnOffType.ON);
                postUpdate(itemName, OnOffType.OFF);
            } else {
                postUpdate(itemName, value);
            }
        }
        return null;
    }

    private void postUpdate(String itemName, State value) {
        itemStates.put(itemName, value);
        eventPublisher.postUpdate(itemName, value);
    }

    @SuppressWarnings("unused")
    private void postCommand(String itemName, Command value) {
        if (value instanceof State) {
            State state = (State) value;
            itemStates.put(itemName, state);
        }
        eventPublisher.postCommand(itemName, value);
    }

    private void setStateOnDevice(State newState, HomematicParameterAddress parameterAddress, String itemName) {
        HMPhysicalDevice device = ccu.getPhysicalDevice(parameterAddress.getDeviceId());
        HMChannel channel = device.getChannel(parameterAddress.getChannelNumber());
        String parameterKey = parameterAddress.getParameterId();
        StateConverter<?, ?> converter = converterLookup.getStateToBindingValueConverter(itemName, newState.getClass());
        if (converter == null) {
            logger.warn("No converter found for " + parameterAddress + "!");
            return;
        }
        Object value = converter.convertFrom(newState);
        logger.debug("Setting new value " + value + " on parameter " + parameterAddress);
        channel.setValue(parameterKey, value);
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        logger.debug("updated config={}", config);
        if (config == null) {
            return;
        }
        String checkAliveIntervallStr = (String) config.get(CONFIG_KEY_CONNECTION_REFRESH_INTERVALL);
        if (StringUtils.isBlank(checkAliveIntervallStr)) {
            checkAlifeIntervallMS = DEFAULT_INTERVALL_5_MINUTES;
        } else {
            checkAlifeIntervallMS = Integer.valueOf(checkAliveIntervallStr);
        }
        ccuHost = (String) config.get(CONFIG_KEY_CCU_HOST);
        ccu = new CCURF(new XmlRpcConnectionRF(ccuHost));
        converterLookupByConfiguredDevices.setCcu(ccu);
        String callbackPortStr = (String) config.get(CONFIG_KEY_CALLBACK_PORT);
        if (StringUtils.isBlank(callbackPortStr)) {
            callbackPort = DEFAULT_CALLBACK_PORT;
        } else {
            callbackPort = Integer.valueOf(callbackPortStr);
        }
        callbackHost = (String) config.get(CONFIG_KEY_CALLBACK_HOST);
        if (StringUtils.isBlank(callbackHost)) {
            callbackHost = LocalNetworkInterface.getLocalNetworkInterface();
        }
        if (isCallbackServerInitialized()) {
            removeCallbackHandler();
        }
        registerCallbackHandler();
        setProperlyConfigured(true);
        for (HomematicBindingProvider provider : providers) {
            queryAndSendAllActualStates(provider);
        }
    }

    @Override
    public void allBindingsChanged(BindingProvider provider) {
        logger.debug("allBindingsChanged provider={}", provider);
        if (provider instanceof HomematicBindingProvider) {
            HomematicBindingProvider homematicBindingProvider = (HomematicBindingProvider) provider;
            queryAndSendAllActualStates(homematicBindingProvider);
        }
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        logger.debug("bindingChanged provider={}, itemName={}", provider, itemName);
        if (provider instanceof HomematicBindingProvider) {
            HomematicBindingProvider homematicBindingProvider = (HomematicBindingProvider) provider;
            initializeDeviceAndParameters(homematicBindingProvider, itemName);
        }
    }

    private void handleAdminCommand(AdminItem adminItem, Type type) {
        AdminCommand command = AdminCommand.valueOf(adminItem.getCommand());
        switch (command) {
        case DUMP_UNCONFIGURED_DEVICES:
            dumpUnconfiguredDevices();
            break;
        case DUMP_UNSUPPORTED_DEVICES:
            dumpUnsupportedDevices();
            break;
        }
    }

    private void dumpUnsupportedDevices() {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    private void dumpUnconfiguredDevices() {
        logger.info("Dumping unconfigured devices:");
        Collection<String> configuredDeviceAddresses = new ArrayList<String>();
        for (HomematicBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                if (!provider.isAdminItem(itemName)) {
                    configuredDeviceAddresses.add(provider.getParameterAddress(itemName).getAddress());
                }
            }
        }
        Set<DefaultHMRFDevice> physicalDevices = (Set<DefaultHMRFDevice>) ccu.getPhysicalDevices();
        for (DefaultHMRFDevice device : physicalDevices) {
            if (isNonCCUDevice(device) && !configuredDeviceAddresses.contains(device.getAddress())) {
                dumpDeviceItemLine(device);
            }
        }

    }

    private void dumpDeviceItemLine(DefaultHMRFDevice device) {
        logger.info("Device with physical address " + device.getAddress() + " of type " + device.getDeviceDescription().getType());
        for (HMChannel channel : device.getChannels()) {
            String channelNum = channel.getAddress().split(":")[1];
            logger.info("  Channel " + channelNum + " with values " + channel.getValuesDescription());
        }
    }

    private boolean isNonCCUDevice(DefaultHMRFDevice device) {
        String parent = device.getDeviceDescription().getParent();
        return StringUtils.isBlank(parent) && !device.getAddress().equals("BidCoS-RF");
    }

    private void queryAndSendAllActualStates(HomematicBindingProvider provider) {
        logger.debug("Updating item state for items {}", provider.getItemNames());
        for (String itemName : provider.getItemNames()) {
            initializeDeviceAndParameters(provider, itemName);
        }
    }

    private void initializeDeviceAndParameters(HomematicBindingProvider provider, String itemName) {
        if (!isCCUInitialized()) {
            return;
        }
        if (provider.isAdminItem(itemName)) {
            return;
        }
        HomematicParameterAddress parameterAddress = provider.getParameterAddress(itemName);
        Item item = provider.getItem(itemName);
        if (item == null) {
            logger.warn("No item found for " + parameterAddress + " - doing nothing.");
            return;
        }
        configureConverterForItem(provider, itemName, parameterAddress, item);
        State value = getValueFromDevice(parameterAddress, item);
        postUpdate(itemName, value);
    }

    public void configureConverterForItem(HomematicBindingProvider provider, String itemName, HomematicParameterAddress parameterAddress,
            Item item) {
        if (provider.getConverter(itemName) != null) {
            converterLookupByCustomConverter.addCustomConverter(itemName, provider.getConverter(itemName));
        }
        converterLookup.configureItem(item, parameterAddress);
    }

    @SuppressWarnings("rawtypes")
    void setCCU(CCU ccu) {
        this.ccu = ccu;
    }

    private String getItemNameForParameter(HomematicParameterAddress parameterAddress) {
        for (HomematicBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                if (parameterAddress.equals(provider.getParameterAddress(itemName))) {
                    return itemName;
                }
            }
        }
        return null;
    }

    private State getValueFromDevice(HomematicParameterAddress parameterAddress, Item item) {
        HMPhysicalDevice physicalDevice = ccu.getPhysicalDevice(parameterAddress.getDeviceId());
        if (physicalDevice == null) {
            logger.warn("Physical device not found for item " + item.getName() + " with address " + parameterAddress
                    + " - no state updated.");
            return null;
        }
        HMChannel channel = physicalDevice.getChannel(parameterAddress.getChannelNumber());
        if (channel == null) {
            logger.warn("Channel not found for " + parameterAddress + " - doing nothing.");
            return null;
        }
        Paramset values = channel.getValues();
        if (values == null) {
            logger.warn("Values not found for " + parameterAddress + " - doing nothing.");
            return null;
        }
        Object valueObject = values.getValue(parameterAddress.getParameterId());
        StateConverter<?, ?> converter = converterLookup.getBindingValueToStateConverter(item.getName());
        if (converter == null) {
            logger.warn("No converter found for " + parameterAddress + " - doing nothing.");
            return null;
        }
        State value = converter.convertTo(valueObject);
        logger.debug("Found device at {} with value {}", parameterAddress, value);
        return value;
    }

    @Override
    public Object[] listDevices(String interfaceId) {
        return null;
    }

    @Override
    public Integer newDevices(String interfaceId, Object[] deviceDescriptions) {
        return null;
    }

    @Override
    public Integer deleteDevices(String interfaceId, Object[] addresses) {
        return null;
    }

    @Override
    public Integer updateDevice(String interfaceId, String address, Integer hint) {
        return null;
    }

    private synchronized void registerCallbackHandler() {
        if (isCallbackServerInitialized()) {
            return;
        }
        logger.debug("Registering callback handler.");
        CallbackHandler handler = new CallbackHandler();
        handler.registerCallbackReceiver(ccu);
        handler.registerCallbackReceiver(this);

        try {
            cbServer = new CallbackServer(InetAddress.getByName(callbackHost), callbackPort, handler);
        } catch (UnknownHostException e) {
            throw new HomematicBindingException("Could not create CallbackServer", e);
        }
        cbServer.start();
        ccu.getConnection().init("http://" + callbackHost + ":" + callbackPort + "/xmlrpc", createHomematicId());
        lastEventTime = System.currentTimeMillis();
        logger.debug("Callback handler registered.");
    }

    private synchronized void removeCallbackHandler() {
        if (!isCallbackServerInitialized()) {
            return;
        }
        logger.debug("Removing callback handler.");
        try {
            ccu.getConnection().init("", createHomematicId());
            cbServer.stop();
        } catch (Exception e) {
            logger.debug("Error while unregistering callback server. Will be ignored.");
        }
        cbServer = null;
    }

    private String createHomematicId() {
        return callbackHost + ":" + callbackPort + "/OPENHAB";
    }

    public StateConverterLookupByConfiguredDevices getConverterLookupByConfiguredDevices() {
        return converterLookupByConfiguredDevices;
    }

    @Override
    protected void execute() {
        long timeSinceLastEvent = System.currentTimeMillis() - lastEventTime;
        if (timeSinceLastEvent <= checkAlifeIntervallMS) {
            logger.debug("Last event was only " + timeSinceLastEvent + "ms ago. No need to refresh connection.");
            return;
        }
        logger.info("Check alife timeout reached. Refreshing CCU connection.");
        removeCallbackHandler();
        registerCallbackHandler();
    }

    @Override
    protected long getRefreshInterval() {
        return checkAlifeIntervallMS;
    }

    @Override
    protected String getName() {
        return "Homematic Connection Refresh Thread";
    }

    private boolean isCallbackServerInitialized() {
        return cbServer != null;
    }

    private boolean isCCUInitialized() {
        return ccu != null;
    }

    public void updateItemState(String itemName, State state) {
        itemStates.put(itemName, state);
    }

}
