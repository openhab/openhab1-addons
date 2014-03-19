/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;
import org.openhab.binding.homematic.test.CCUMock;
import org.openhab.binding.homematic.test.EventPublisherMock;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

public class BasicBindingTest {

    public static final String MOCK_PARAM_ADDRESS = "mockDevice:1";
    public static final String SHORT_PRESS_PARAMETER_KEY = "SHORT_PRESS";
    public static final String PEMPERATURE_PARAMETER_KEY = "TEMPERATURE";
    public static final String ITEM_NAME = HomematicBindingProviderMock.DEFAULT_ITEM_NAME;

    protected Map<String, Object> values;
    protected HomematicBinding binding;
    protected HomematicBindingProviderMock provider;
    protected EventPublisherMock publisher;
    protected CCUMock ccu;

    public BasicBindingTest() {
        super();
    }

    @Before
    public void setUpBindingMock() {
        binding = new HomematicBinding();
        ccu = new CCUMock();
        binding.getConverterLookupByConfiguredDevices().setCcu(ccu);
        values = new HashMap<String, Object>();
        ccu.getPhysicalDevice(null).getChannel(1).setValues(new Paramset(values));
        binding.setCCU(ccu);
        provider = new HomematicBindingProviderMock();
        binding.addBindingProvider(provider);
        provider.getItemNames().add(ITEM_NAME);
        publisher = new EventPublisherMock();
        binding.setEventPublisher(publisher);
    }

    protected void checkReceiveCommand(ParameterKey parameterKeySend, ParameterKey parameterKeyReceive, Command command,
            Object expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKeySend.name());
        provider.setParameterAddress(paramAddress);
        binding.configureConverterForItem(provider, ITEM_NAME, paramAddress, provider.getItem(ITEM_NAME));
        binding.internalReceiveCommand(ITEM_NAME, command);
        Object value = values.get(parameterKeyReceive.name());
        assertEquals("Value", expectedValue, value);
    }

    protected void checkReceiveCommand(ParameterKey parameterKey, Command command, Object expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        binding.configureConverterForItem(provider, ITEM_NAME, paramAddress, provider.getItem(ITEM_NAME));
        binding.internalReceiveCommand(ITEM_NAME, command);
        Object value = values.get(parameterKey.name());
        assertEquals("Value", expectedValue, value);
    }

    protected void checkReceiveState(ParameterKey parameterKey, State state, Object expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        binding.configureConverterForItem(provider, ITEM_NAME, paramAddress, provider.getItem(ITEM_NAME));
        binding.internalReceiveUpdate(ITEM_NAME, state);
        Object value = values.get(parameterKey.name());
        assertEquals("Value", expectedValue, value);
    }

    protected void checkInitialValue(ParameterKey parameterKey, Object homematicValue, Type expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        binding.configureConverterForItem(provider, ITEM_NAME, paramAddress, provider.getItem(ITEM_NAME));
        values.put(parameterKey.name(), homematicValue);
        binding.event("dummie", MOCK_PARAM_ADDRESS, parameterKey.name(), homematicValue);
        assertEquals("Update State", expectedValue, publisher.popLastState());
    }

    protected void checkValueReceived(ParameterKey parameterKey, Object homematicValue, Type expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        binding.configureConverterForItem(provider, ITEM_NAME, paramAddress, provider.getItem(ITEM_NAME));
        values.put(parameterKey.name(), homematicValue);
        binding.event("dummie", MOCK_PARAM_ADDRESS, parameterKey.name(), homematicValue);
        assertEquals("Update State", expectedValue, publisher.popLastState());
    }

    protected void checkCommandReceived(ParameterKey parameterKey, Object homematicValue, Command expectedCommand) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        binding.configureConverterForItem(provider, ITEM_NAME, paramAddress, provider.getItem(ITEM_NAME));
        values.put(parameterKey.name(), homematicValue);
        binding.event("dummie", MOCK_PARAM_ADDRESS, parameterKey.name(), homematicValue);
        assertEquals("Command received", expectedCommand, publisher.popLastCommand());
    }

}