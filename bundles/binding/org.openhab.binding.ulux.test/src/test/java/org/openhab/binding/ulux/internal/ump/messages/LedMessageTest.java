package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.ump.messages.LedMessage.BlinkMode.BLINK_FAST;
import static org.openhab.binding.ulux.internal.ump.messages.LedMessage.Color.COLOR_RED;
import static org.openhab.binding.ulux.internal.ump.messages.LedMessage.Led.LED_2;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractMessageTest;

public class LedMessageTest extends AbstractMessageTest {

	@Test
	public void testSet() {
		LedMessage message = new LedMessage((short) 1); // TODO
		message.setOverride(LED_2, true);
		message.setColor(LED_2, COLOR_RED);
		message.setBlinkMode(LED_2, BLINK_FAST);

		byte[] actual = toBytes(message);
		byte[] expected = toBytes("08:44:01:00:00:B1:00:00");

		assertThat(actual, equalTo(expected));
	}

}
