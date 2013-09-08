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