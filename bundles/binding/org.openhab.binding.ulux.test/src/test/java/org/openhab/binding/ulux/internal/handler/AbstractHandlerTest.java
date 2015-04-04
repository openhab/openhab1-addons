package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.net.InetAddress;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.openhab.binding.ulux.internal.ump.AbstractMessageTest;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.core.events.EventPublisher;

// TODO rework class hierarchy of tests
public abstract class AbstractHandlerTest<T extends UluxMessage> extends AbstractMessageTest {

	private AbstractMessageHandler<T> handler;

	@Mock
	protected EventPublisher eventPublisher;

	@Before
	public void beforeTest() {
		initMocks(this);

		handler = createMessageHandler();
		handler.setEventPublisher(eventPublisher);
		// TODO handler.setProviders(providers);
	}

	protected abstract AbstractMessageHandler<T> createMessageHandler();

	@After
	public void afterTest() {
		verifyNoMoreInteractions(eventPublisher);
	}

	protected final UluxDatagram handleMessage(String data) throws Exception {
		final T message = parseMessage(toBuffer(data));

		// TODO
		final UluxDatagram response = new UluxDatagram((short) 1, InetAddress.getByName("127.0.0.1"));

		handler.handleMessage(message, response);

		return response;
	}

}
