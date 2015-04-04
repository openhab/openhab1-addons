package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.EditValue;

import java.nio.ByteBuffer;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractMessageTest;

public class EditValueMessageTest extends AbstractMessageTest {

	@Test
	public void testIncoming() {
		ByteBuffer buffer = toBuffer("06:42:01:00:13:00");

		EditValueMessage message = parseMessage(buffer);

		assertThat(message.getMessageId(), equalTo(EditValue));
		assertThat(message.getActorId(), equalTo((short) 1));
		assertThat(message.getValue(), equalTo((short) 19));
	}

	@Test
	public void testOutgoing() {
		EditValueMessage message = new EditValueMessage((short) 1, (short) 19);

		byte[] actual = toBytes(message);
		byte[] expected = toBytes("06:42:01:00:13:00");

		assertThat(actual, equalTo(expected));
	}

}
