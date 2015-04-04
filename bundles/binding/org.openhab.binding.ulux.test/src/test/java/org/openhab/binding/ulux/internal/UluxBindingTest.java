package org.openhab.binding.ulux.internal;

import java.util.Hashtable;

import org.junit.Ignore;
import org.junit.Test;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class UluxBindingTest {

	@Test
	@Ignore("Test uses a real network socket at the moment...")
	public void test() throws Exception {
		final Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("bind_address", "127.0.0.1");
		properties.put("ulux:switch.1", "127.0.0.101");

		final UluxBinding bundle = new UluxBinding();
		// TODO bundle.setEventPublisher(eventPublisher);
		bundle.activate();
		bundle.updated(properties);

		final UluxGenericBindingProvider bindingProvider = new UluxGenericBindingProvider();
		bundle.addBindingProvider(bindingProvider);

		final Item item1 = new SwitchItem("Ulux_Switch");
		bindingProvider.processBindingConfiguration("ulux.items", item1, "1:1");

		// TODO
		bundle.receiveCommand("Ulux_Switch", OnOffType.ON);

		// final UluxDatagram datagram = bundle.datagramFactory
		// .createDatagram(binding);
		//
		// datagram.addMessage(new ActivateMessage(false));
		// datagram.addMessage(new PageIndexMessage((byte) 0));
		// datagram.addMessage(new EditValueMessage(binding.getActorId(),
		// (short) 30));
		//
		// datagram.send(bundle.channel);
	}
}
