package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractCommandTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class ActivateMessageTest extends AbstractCommandTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Display"), "1:0:Display");

		receiveCommand("Ulux_Display", OnOffType.ON);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:2D:00:00:01:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testCommandOff() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Display"), "1:0:Display");

		receiveCommand("Ulux_Display", OnOffType.OFF);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:2D:00:00:02:00");

		assertThat(actual, equalTo(expected));
	}

}
