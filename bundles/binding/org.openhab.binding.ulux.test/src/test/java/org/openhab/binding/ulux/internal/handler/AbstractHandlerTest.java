package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.net.InetAddress;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.UluxGenericBindingProvider;
import org.openhab.binding.ulux.internal.ump.AbstractMessageTest;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

// TODO rework class hierarchy of tests
public abstract class AbstractHandlerTest<T extends UluxMessage> extends AbstractMessageTest {

	private UluxGenericBindingProvider bindingProvider;

	private AbstractMessageHandler<T> handler;

	@Mock
	protected EventPublisher eventPublisher;

	/**
	 * Set by {@link #handleMessage(String)}.
	 */
	protected UluxDatagram response;

	@Before
	public void beforeTest() {
		initMocks(this);

		bindingProvider = new UluxGenericBindingProvider();

		handler = createMessageHandler();
		handler.setEventPublisher(eventPublisher);
		handler.setProviders(Collections.<UluxBindingProvider> singleton(bindingProvider));

		response = null;
	}

	protected abstract AbstractMessageHandler<T> createMessageHandler();

	@After
	public void afterTest() {
		verifyNoMoreInteractions(eventPublisher);
	}

	protected final void addBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		final String context = getClass().getSimpleName();

		this.bindingProvider.processBindingConfiguration(context, item, bindingConfig);
	}

	protected final void handleMessage(String data) throws Exception {
		final T message = parseMessage(toBuffer(data));

		response = new UluxDatagram((short) 1, InetAddress.getByName("127.0.0.1"));

		handler.handleMessage(message, response);
	}

}
