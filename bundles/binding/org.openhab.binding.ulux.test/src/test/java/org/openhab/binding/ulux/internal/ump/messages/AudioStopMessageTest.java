package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractCommandTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class AudioStopMessageTest extends AbstractCommandTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "1:0:Audio");

		receiveCommand("Ulux_Audio", OnOffType.ON);

		assertFalse(datagram.hasMessages());
	}

	@Test
	public void testCommandOff() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "1:0:Audio");

		receiveCommand("Ulux_Audio", OnOffType.OFF);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:92:00:00:03:00");

		assertThat(actual, equalTo(expected));
	}

}
