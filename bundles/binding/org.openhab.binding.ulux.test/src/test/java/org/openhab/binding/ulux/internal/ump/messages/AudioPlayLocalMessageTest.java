package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractMessageTest;

public class AudioPlayLocalMessageTest extends AbstractMessageTest {

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

}
