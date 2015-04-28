package org.openhab.binding.ulux.internal.ump;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.UluxGenericBindingProvider;
import org.openhab.binding.ulux.internal.handler.UluxMessageHandlerFacade;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import com.google.common.io.Files;

/**
 * Base class for tests of u::Lux Message Protocol functionality.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public abstract class AbstractUluxMessageTest {

	// TODO private
	protected UluxMessageParser parser;

	private UluxConfiguration configuration;

	private Hashtable<String, String> configurationProperties;

	private UluxGenericBindingProvider bindingProvider;

	// TODO private
	protected UluxDatagramFactory datagramFactory;

	private UluxMessageHandlerFacade messageHandlerFacade;

	@Mock
	protected EventPublisher eventPublisher;

	@Mock
	protected ItemRegistry itemRegistry;

	/**
	 * Set by {@link #receiveCommand(String, Command)} or {@link #receiveUpdate(String, State)}.
	 */
	protected List<UluxDatagram> datagramList;

	/**
	 * The only datagram in {@link #datagramList} if its size is 1.
	 */
	protected UluxDatagram datagram;

	/**
	 * Set by {@link #handleMessage(String)}.
	 */
	protected UluxMessageDatagram response;

	@Before
	public void beforeTest() throws Exception {
		initMocks(this);
		configurationProperties = new Hashtable<String, String>();
		configurationProperties.put("switch.1", "192.168.1.101");

		configuration = new UluxConfiguration();
		configuration.updated(configurationProperties);

		bindingProvider = new UluxGenericBindingProvider();
		datagramFactory = new UluxDatagramFactory(configuration);

		messageHandlerFacade = new UluxMessageHandlerFacade(
				Collections.<UluxBindingProvider> singleton(bindingProvider));
		messageHandlerFacade.setEventPublisher(eventPublisher);
		messageHandlerFacade.setItemRegistry(itemRegistry);

		parser = new UluxMessageParser();

		response = null;
		datagram = null;
	}

	@After
	public void afterTest() {
		verifyNoMoreInteractions(eventPublisher);
	}

	protected final void addConfiguration(String key, String value) throws Exception {
		configurationProperties.put(key, value);
		configuration.updated(configurationProperties);
	}

	protected final void addBindingConfig(Item item, String bindingConfig) throws Exception {
		final String context = getClass().getSimpleName();

		this.bindingProvider.processBindingConfiguration(context, item, bindingConfig);

		when(itemRegistry.getItem(item.getName())).thenReturn(item);
	}

	protected final void receiveCommand(String itemName, Command command) throws Exception {
		final UluxBindingConfig binding = this.bindingProvider.getBinding(itemName);

		datagramList = datagramFactory.createDatagram(binding, command);
		if (datagramList.size() == 1) {
			datagram = datagramList.get(0);
		}
	}

	protected final void receiveUpdate(String itemName, State newState) throws Exception {
		final UluxBindingConfig binding = this.bindingProvider.getBinding(itemName);

		datagramList = datagramFactory.createDatagram(binding, newState);
		if (datagramList.size() == 1) {
			datagram = datagramList.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	protected final <T extends UluxMessage> T parseMessage(ByteBuffer buffer) {
		return (T) parser.parseNextMessage(buffer);
	}

	protected final void handleMessage(String data) throws Exception {
		final UluxMessage message = parseMessage(toBuffer(data));

		response = datagramFactory.createMessageDatagram((short) 1, InetAddress.getByName("127.0.0.1"));

		messageHandlerFacade.handleMessage(message, response);
	}

	protected final ByteBuffer toBuffer(CharSequence data) {
		ByteBuffer buffer = ByteBuffer.wrap(toBytes(data));
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		return buffer;
	}

	protected final byte[] toBytes(UluxDatagram datagram) {
		ByteBuffer buffer = datagram.prepareBuffer();

		byte[] bytes = new byte[buffer.limit() - 16];

		System.arraycopy(buffer.array(), 16, bytes, 0, bytes.length);

		return bytes;
	}

	protected final byte[] toBytes(UluxMessage message) {
		return message.getBuffer().array();
	}

	protected final byte[] toBytes(CharSequence data) {
		final StringTokenizer tokenizer = new StringTokenizer(data.toString(), ":");
		final byte[] bytes = new byte[tokenizer.countTokens()];

		for (int i = 0; i < bytes.length; i++) {
			// TODO remove cast to byte ?
			bytes[i] = (byte) Short.parseShort(tokenizer.nextToken(), 16);
		}

		return bytes;
	}

	protected final byte[] toBytes(File file) throws IOException {
		final StringBuilder data = new StringBuilder();

		final List<String> lines = Files.readLines(file, Charset.forName("UTF-8"));
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			data.append(iterator.next());
			if (iterator.hasNext()) {
				data.append(":");
			}
		}

		return toBytes(data);
	}

	protected final String toString(byte[] bytes) {
		final StringBuilder builder = new StringBuilder();

		int count = 0;

		for (byte b : bytes) {
			count++;

			builder.append(String.format("%02x", b));
			builder.append(":");

			if (count == 32) {
				builder.setLength(builder.length() - 1);
				builder.append("\n");
				count = 0;
			}
		}

		builder.setLength(builder.length() - 1);

		return builder.toString();
	}

}
