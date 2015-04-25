package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class ActivateMessageTest extends AbstractUluxMessageTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Display"), "{switchId=1, type='DISPLAY'}");

		receiveCommand("Ulux_Display", OnOffType.ON);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:2D:00:00:01:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testCommandOff() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Display"), "{switchId=1, type='DISPLAY'}");

		receiveCommand("Ulux_Display", OnOffType.OFF);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:2D:00:00:02:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Display"), "{switchId=1, type='DISPLAY'}");

		receiveUpdate("Ulux_Display", OnOffType.OFF);

		assertTrue(datagramList.isEmpty());
	}

}
