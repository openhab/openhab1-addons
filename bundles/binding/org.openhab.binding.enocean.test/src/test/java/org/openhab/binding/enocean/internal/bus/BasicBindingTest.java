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
package org.openhab.binding.enocean.internal.bus;

import org.junit.Before;
import org.opencean.core.ESP3Host;
import org.opencean.core.address.EnoceanParameterAddress;
import org.opencean.core.common.ProtocolConnector;

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