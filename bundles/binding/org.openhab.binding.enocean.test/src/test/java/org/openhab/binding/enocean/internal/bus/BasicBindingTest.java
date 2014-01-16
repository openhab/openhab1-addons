/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.bus;

import org.enocean.java.ESP3Host;
import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.common.ProtocolConnector;
import org.junit.Before;

public class BasicBindingTest {

    protected EnoceanBinding binding;
    protected EventPublisherMock publisher;
    protected EnoceanBindingProviderMock provider;
    protected EnoceanParameterAddress parameterAddress;

    public BasicBindingTest() {
        super();
    }

    @Before
    public void setUp() {
        binding = new EnoceanBinding();
        ProtocolConnector connector = new ProtocolConnectorMock();
        binding.setEsp3Host(new ESP3Host(connector));
        publisher = new EventPublisherMock();
        binding.setEventPublisher(publisher);
        provider = new EnoceanBindingProviderMock();
    }

}