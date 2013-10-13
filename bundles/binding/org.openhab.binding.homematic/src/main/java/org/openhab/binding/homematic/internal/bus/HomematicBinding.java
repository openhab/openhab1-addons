/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.config.AdminItem;
import org.openhab.binding.homematic.internal.config.ConfiguredDevice;
import org.openhab.binding.homematic.internal.config.DeviceConfigLocator;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.converter.BooleanOnOffConverter;
import org.openhab.binding.homematic.internal.converter.BooleanOpenCloseConverter;
import org.openhab.binding.homematic.internal.converter.BrightnessConverter;
import org.openhab.binding.homematic.internal.converter.CommandConverter;
import org.openhab.binding.homematic.internal.converter.ConverterFactory;
import org.openhab.binding.homematic.internal.converter.DoubleOnOffConverter;
import org.openhab.binding.homematic.internal.converter.DoublePercentageConverter;
import org.openhab.binding.homematic.internal.converter.DoubleUpDownConverter;
import org.openhab.binding.homematic.internal.converter.IncreaseDecreasePercentageCommandConverter;
import org.openhab.binding.homematic.internal.converter.IntegerDecimalConverter;
import org.openhab.binding.homematic.internal.converter.IntegerOnOffConverter;
import org.openhab.binding.homematic.internal.converter.IntegerPercentConverter;
import org.openhab.binding.homematic.internal.converter.IntegerPercentageOnOffConverter;
import org.openhab.binding.homematic.internal.converter.IntegerPercentageOpenClosedConverter;
import org.openhab.binding.homematic.internal.converter.NegativeBooleanOnOffConverter;
import org.openhab.binding.homematic.internal.converter.OnOffPercentageCommandConverter;
import org.openhab.binding.homematic.internal.converter.StateConverter;
import org.openhab.binding.homematic.internal.converter.StopMoveBooleanCommandConverter;
import org.openhab.binding.homematic.internal.converter.TemperatureConverter;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.internal.device.channel.HMChannel;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.device.physical.rf.DefaultHMRFDevice;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackHandler;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackReceiver;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackServer;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Homematic binding implementation.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class HomematicBinding extends AbstractBinding<HomematicBindingProvider> implements ManagedService, CallbackReceiver {

    private static final Logger logger = LoggerFactory.getLogger(HomematicBinding.class);

    private static final Object CONFIG_KEY_CCU_HOST = "host";
    private static final Object CONFIG_KEY_CALLBACK_PORT = "callback.port";
    private static final Object CONFIG_KEY_CALLBACK_HOST = "callback.host";
    private static final Integer DEFAULT_CALLBACK_PORT = 9123;

    private ConverterFactory converterFactory = new ConverterFactory();

    private CCU<?> ccu;
    protected EventPublisher eventPublisher = null;
    private Integer callbackPort;
    private String ccuHost;
    private String callbackHost;
    private CallbackServer cbServer;

    public HomematicBinding() {
        converterFactory.addStateConverter(ParameterKey.INSTALL_TEST.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.BRIGHTNESS.name(), PercentType.class, BrightnessConverter.class);
        converterFactory.addStateConverter(ParameterKey.BRIGHTNESS.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterFactory.addStateConverter(ParameterKey.PRESS_SHORT.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.PRESS_LONG.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.PRESS_LONG_RELEASE.name(), OnOffType.class, NegativeBooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.PRESS_CONT.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.HUMIDITY.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterFactory.addStateConverter(ParameterKey.HUMIDITY.name(), PercentType.class, IntegerPercentConverter.class);

        converterFactory.addStateConverter(ParameterKey.LEVEL.name(), PercentType.class, DoublePercentageConverter.class);
        converterFactory.addStateConverter(ParameterKey.LEVEL.name(), UpDownType.class, DoubleUpDownConverter.class);
        converterFactory.addStateConverter(ParameterKey.LEVEL.name(), OnOffType.class, DoubleOnOffConverter.class);
        converterFactory.addCommandConverter(ParameterKey.LEVEL.name(), OnOffType.class, OnOffPercentageCommandConverter.class);
        converterFactory.addCommandConverter(ParameterKey.LEVEL.name(), IncreaseDecreaseType.class,
                IncreaseDecreasePercentageCommandConverter.class);
        // Roller shutter: convert Stop to Off and Off to FALSE. Set this at the
        // STOP parameter
        converterFactory.addStateConverter(ParameterKey.STOP.name(), OnOffType.class, NegativeBooleanOnOffConverter.class);
        converterFactory.addCommandConverter(ParameterKey.LEVEL.name(), StopMoveType.class, StopMoveBooleanCommandConverter.class);

        converterFactory.addStateConverter(ParameterKey.MOTION.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.STATE.name(), DecimalType.class, IntegerDecimalConverter.class);
        converterFactory.addStateConverter(ParameterKey.STATE.name(), OnOffType.class, BooleanOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.STATE.name(), OpenClosedType.class, BooleanOpenCloseConverter.class);

        converterFactory.addStateConverter(ParameterKey.TEMPERATURE.name(), DecimalType.class, TemperatureConverter.class);
        converterFactory.addStateConverter(ParameterKey.SETPOINT.name(), DecimalType.class, TemperatureConverter.class);
        converterFactory.addStateConverter(ParameterKey.MODE_TEMPERATUR_VALVE.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterFactory.addStateConverter(ParameterKey.VALVE_STATE.name(), PercentType.class, IntegerPercentConverter.class);
        converterFactory.addStateConverter(ParameterKey.VALVE_STATE.name(), OnOffType.class, IntegerPercentageOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.VALVE_STATE.name(), OpenClosedType.class,
                IntegerPercentageOpenClosedConverter.class);

        converterFactory.addStateConverter(ParameterKey.ERROR.name(), OnOffType.class, IntegerOnOffConverter.class);
        converterFactory.addStateConverter(ParameterKey.ERROR.name(), DecimalType.class, IntegerDecimalConverter.class);

        converterFactory.addStateConverter(ParameterKey.UNREACH.name(), OnOffType.class, BooleanOnOffConverter.class);

        converterFactory.addStateConverter(ParameterKey.LOWBAT.name(), OnOffType.class, BooleanOnOffConverter.class);

        DeviceConfigLocator locator = new DeviceConfigLocator("HM-LC-Bl1-FM.xml", "HM-LC-BI1PBU-FM.xml", "HM-Sec-SC.xml", "HM-Sec-RHS.xml");
        List<ConfiguredDevice> configuredDevices = locator.findAll();
        converterFactory.addConfiguredDevices(configuredDevices);

    }

    @Override
    public void activate() {
        if (ccu != null && cbServer == null) {
            registerCallbackHandler();
        }
    }

    @Override
    public void deactivate() {
        if (cbServer != null) {
            removeCallbackHandler(cbServer);
            cbServer = null;
        }
    }

    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        for (HomematicBindingProvider provider : providers) {
            logger.debug("Checking provider with names {}", provider.getItemNames());
            if (provider.isAdminItem(itemName)) {
                handleAdminCommand(provider.getAdminItem(itemName), command);
            } else {
                HomematicParameterAddress parameterAddress = provider.getParameterAddress(itemName);
                Item item = provider.getItem(itemName);
                State actualState = item.getState();
                String parameterKey = parameterAddress.getParameterId();
                CommandConverter<?, ?> commandConverter = converterFactory.getCommandConverter(parameterAddress, command);
                if (commandConverter == null) {
                    logger.warn("No command converter found for {}. No command will be executed.", parameterAddress);
                    return;
                }
                State newState = commandConverter.convertFrom(actualState, command);
                logger.debug("Setting new state " + newState + " on item " + itemName);
                if (command instanceof StopMoveType && parameterKey.equals(ParameterKey.LEVEL.name())) {
                    // Roller shutter workaround: StopMove commands go to STOP
                    // parameterKey
                    parameterAddress = HomematicParameterAddress.from(parameterAddress.getAddress(), ParameterKey.STOP.name());
                }
                setStateOnDevice(newState, parameterAddress);
            }
        }
    }

    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        for (HomematicBindingProvider provider : providers) {
            logger.debug("Checking provider with names {}", provider.getItemNames());
            HomematicParameterAddress parameterAddress = provider.getParameterAddress(itemName);
            setStateOnDevice(newState, parameterAddress);
        }
    }

    @Override
    public Integer event(String interfaceId, String address, String parameterKey, Object valueObject) {
        HomematicParameterAddress parameterAddress = HomematicParameterAddress.from(address, parameterKey);
        logger.debug("Received new value {} for device at {}", valueObject, parameterAddress);
        Item item = getItemForParameter(parameterAddress);
        if (item != null) {
            StateConverter<?, ?> converter = converterFactory.getToStateConverter(parameterAddress, item);
            if (converter == null) {
                logger.warn("No converter found for " + parameterAddress + " - doing nothing.");
                return null;
            }
            State value = converter.convertTo(valueObject);
            logger.debug("Received new value {} for item {}", value, item);

            eventPublisher.postUpdate(item.getName(), value);
            if (parameterKey.equals(ParameterKey.WORKING.name())) {
                if (!(Boolean) valueObject) {
                    // When no longer in working state, get the actual value and
                    // set it.
                    // State value = getValueFromDevice(parameterAddress, item);
                    // eventPublisher.postUpdate(item.getName(), value);
                }
            }
        }
        return null;
    }

    private void setStateOnDevice(State newState, HomematicParameterAddress parameterAddress) {
        HMPhysicalDevice device = ccu.getPhysicalDevice(parameterAddress.getDeviceId());
        HMChannel channel = device.getChannel(parameterAddress.getChannelNumber());
        String parameterKey = parameterAddress.getParameterId();
        StateConverter<?, ?> converter = converterFactory.getFromStateConverter(parameterAddress, newState);
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
        if (config == null) {
            return;
        }
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
        ccuHost = (String) config.get(CONFIG_KEY_CCU_HOST);
        ccu = new CCURF(new XmlRpcConnectionRF(ccuHost));
        converterFactory.setCcu(ccu);
        if (ccu != null && cbServer == null) {
            registerCallbackHandler();
        }
    }

    @Override
    public void allBindingsChanged(BindingProvider provider) {
        if (provider instanceof HomematicBindingProvider) {
            HomematicBindingProvider homematicBindingProvider = (HomematicBindingProvider) provider;
            queryAndSendAllActualStates(homematicBindingProvider);
        }
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
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
        if (provider.isAdminItem(itemName)) {
            return;
        }
        HomematicParameterAddress parameterAddress = provider.getParameterAddress(itemName);
        if (provider.getConverter(itemName) != null) {
            converterFactory.addCustomConverter(parameterAddress, provider.getConverter(itemName));
        }
        Item item = provider.getItem(itemName);
        if (item == null) {
            logger.warn("No item found for " + parameterAddress + " - doing nothing.");
            return;
        }
        State value = getValueFromDevice(parameterAddress, item);
        eventPublisher.postUpdate(itemName, value);
    }

    @SuppressWarnings("rawtypes")
    void setCCU(CCU ccu) {
        this.ccu = ccu;
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    private Item getItemForParameter(HomematicParameterAddress parameterAddress) {
        for (HomematicBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                if (parameterAddress.equals(provider.getParameterAddress(itemName))) {
                    return provider.getItem(itemName);
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
        StateConverter<?, ?> converter = converterFactory.getToStateConverter(parameterAddress, item);
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

    private void registerCallbackHandler() {
        logger.debug("Registering callback handler.");
        CallbackHandler handler = new CallbackHandler();
        handler.registerCallbackReceiver(ccu);
        handler.registerCallbackReceiver(this);

        cbServer = new CallbackServer(null, callbackPort, handler);
        cbServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (cbServer != null) {
                    removeCallbackHandler(cbServer);
                }
            }
        });
        ccu.getConnection().init("http://" + callbackHost + ":" + callbackPort + "/xmlrpc", "" + ccu.getConnection().hashCode());
    }

    private void removeCallbackHandler(final CallbackServer cbServer) {
        logger.debug("Removing callback handler.");
        ccu.getConnection().init("", "" + ccu.getConnection().hashCode());
        cbServer.stop();
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }
}
