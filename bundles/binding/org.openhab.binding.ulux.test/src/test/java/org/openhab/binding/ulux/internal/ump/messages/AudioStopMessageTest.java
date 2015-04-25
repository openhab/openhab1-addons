package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class AudioStopMessageTest extends AbstractUluxMessageTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "{switchId=1, type='AUDIO'}");

		receiveCommand("Ulux_Audio", OnOffType.ON);

		assertTrue(datagramList.isEmpty());
	}

	@Test
	public void testCommandOff() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "{switchId=1, type='AUDIO'}");

		receiveCommand("Ulux_Audio", OnOffType.OFF);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:92:00:00:03:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "{switchId=1, type='AUDIO'}");

		receiveUpdate("Ulux_Audio", OnOffType.OFF);

		assertTrue(datagramList.isEmpty());
	}
}
