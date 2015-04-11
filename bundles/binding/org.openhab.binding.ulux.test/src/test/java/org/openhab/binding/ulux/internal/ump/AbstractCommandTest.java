package org.openhab.binding.ulux.internal.ump;

import java.nio.ByteBuffer;
import java.util.Hashtable;

import org.junit.Before;
import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.UluxGenericBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

public class AbstractCommandTest extends AbstractMessageTest {

	private UluxConfiguration configuration;

	private Hashtable<String, String> configurationProperties;

	private UluxGenericBindingProvider bindingProvider;

	private UluxDatagramFactory datagramFactory;

	protected UluxDatagram datagram;

	@Before
	public void beforeTest() throws Exception {
		configurationProperties = new Hashtable<String, String>();
		configurationProperties.put("switch.1", "192.168.1.101");

		configuration = new UluxConfiguration();
		configuration.updated(configurationProperties);

		bindingProvider = new UluxGenericBindingProvider();
		datagramFactory = new UluxDatagramFactory(configuration);

		datagram = null;
	}

	protected final void addConfiguration(String key, String value) throws Exception {
		configurationProperties.put(key, value);
		configuration.updated(configurationProperties);
	}

	protected final void addBindingConfig(Item item, String bindingConfig) throws Exception {
		final String context = getClass().getSimpleName();

		this.bindingProvider.processBindingConfiguration(context, item, bindingConfig);
	}

	protected final void receiveCommand(String itemName, Command command) throws Exception {
		final UluxBindingConfig binding = this.bindingProvider.getBinding(itemName);

		datagram = datagramFactory.createDatagram(binding, command);
	}

	protected final byte[] toBytes(UluxDatagram datagram) {
		ByteBuffer buffer = datagram.getBuffer();

		byte[] bytes = new byte[buffer.position() - 16];

		System.arraycopy(buffer.array(), 16, bytes, 0, bytes.length);

		return bytes;
	}
}
