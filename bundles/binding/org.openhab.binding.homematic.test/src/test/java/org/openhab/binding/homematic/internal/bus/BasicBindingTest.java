/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
    public static final String ITEM_NAME = "mockItem";

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
        binding.getConverterFactory().setCcu(ccu);
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
        binding.internalReceiveCommand("default", command);
        Object value = values.get(parameterKeyReceive.name());
        assertEquals("Value", expectedValue, value);
    }

    protected void checkReceiveCommand(ParameterKey parameterKey, Command command, Object expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        binding.internalReceiveCommand("default", command);
        Object value = values.get(parameterKey.name());
        assertEquals("Value", expectedValue, value);
    }

    protected void checkReceiveState(ParameterKey parameterKey, State state, Object expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        binding.internalReceiveUpdate("default", state);
        Object value = values.get(parameterKey.name());
        assertEquals("Value", expectedValue, value);
    }

    protected void checkInitialValue(ParameterKey parameterKey, Object homematicValue, Type expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        values.put(parameterKey.name(), homematicValue);
        binding.event("dummie", MOCK_PARAM_ADDRESS, parameterKey.name(), homematicValue);
        assertEquals("Update State", expectedValue, publisher.getUpdateState());
    }

    protected void checkValueReceived(ParameterKey parameterKey, Object homematicValue, Type expectedValue) {
        HomematicParameterAddress paramAddress = HomematicParameterAddress.from(MOCK_PARAM_ADDRESS, parameterKey.name());
        provider.setParameterAddress(paramAddress);
        values.put(parameterKey.name(), homematicValue);
        binding.event("dummie", MOCK_PARAM_ADDRESS, parameterKey.name(), homematicValue);
        assertEquals("Update State", expectedValue, publisher.getUpdateState());
    }

}