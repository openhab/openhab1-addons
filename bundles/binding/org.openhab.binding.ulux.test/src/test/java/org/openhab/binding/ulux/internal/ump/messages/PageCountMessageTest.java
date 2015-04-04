package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.PageCount;

import java.nio.ByteBuffer;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractMessageTest;

public class PageCountMessageTest extends AbstractMessageTest {

	@Test
	public void testIncoming() {
		// 06    - Length "6"
		// 0e    - ID-PageCount
		// 00:00 - Actor "0"
		// 02:00 - Pages "2"
		ByteBuffer buffer = toBuffer("06:0e:00:00:02:00");

		PageCountMessage message = parseMessage(buffer);

		assertThat(message.getMessageId(), equalTo(PageCount));
		assertThat(message.getActorId(), equalTo((short) 0));
		assertThat(message.getPageCount(), equalTo((byte) 2));
	}

}
