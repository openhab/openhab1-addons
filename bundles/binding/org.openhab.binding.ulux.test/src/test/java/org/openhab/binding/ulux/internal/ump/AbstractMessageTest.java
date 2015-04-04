package org.openhab.binding.ulux.internal.ump;

import java.nio.ByteBuffer;

import org.openhab.binding.ulux.internal.AbstractUluxTest;

public abstract class AbstractMessageTest extends AbstractUluxTest {

	protected UluxMessageParser parser = new UluxMessageParser();

	@SuppressWarnings("unchecked")
	protected <T extends UluxMessage> T parseMessage(ByteBuffer buffer) {
		return (T) parser.parseNextMessage(buffer);
	}

}
