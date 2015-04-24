package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class AudioPlayLocalMessageTest extends AbstractUluxMessageTest {

	@Test
	public void outgoingDefaults() {
		AudioPlayLocalMessage message = new AudioPlayLocalMessage((short) 1);

		byte[] actual = toBytes(message);
		byte[] expected = toBytes("10:98:00:00:00:00:18:00:00:00:00:00:00:00:01:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void outgoingAlarm() {
		AudioPlayLocalMessage message = new AudioPlayLocalMessage((short) 1);
		message.setAlarm(true);

		byte[] actual = toBytes(message);
		byte[] expected = toBytes("10:98:00:00:00:00:19:00:00:00:00:00:00:00:01:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new NumberItem("Ulux_Audio"), "1:0:AudioPlayLocal");

		receiveUpdate("Ulux_Audio", new DecimalType(2));

		assertTrue(datagramList.isEmpty());
	}

}
