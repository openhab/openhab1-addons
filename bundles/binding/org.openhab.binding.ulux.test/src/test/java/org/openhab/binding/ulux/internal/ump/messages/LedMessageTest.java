package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractCommandTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class LedMessageTest extends AbstractCommandTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Led"), "1:1:Led:2");

		receiveCommand("Ulux_Led", OnOffType.ON);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("08:44:01:00:00:B1:00:00");

		assertThat(actual, equalTo(expected));
	}

}
