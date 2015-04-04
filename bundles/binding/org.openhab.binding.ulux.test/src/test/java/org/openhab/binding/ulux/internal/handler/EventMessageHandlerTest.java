package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.EventMessage;
import org.openhab.core.library.types.OnOffType;

public class EventMessageHandlerTest extends AbstractHandlerTest<EventMessage> {

	@Override
	protected AbstractMessageHandler<EventMessage> createMessageHandler() {
		return new EventMessageHandler();
	}

	@Test
	public void testNoKeyPressed() throws Exception {
		handleMessage("06:51:00:00:00:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey1Pressed() throws Exception {
		handleMessage("06:51:00:00:01:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.ON);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey2Pressed() throws Exception {
		handleMessage("06:51:00:00:02:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.ON);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey3Pressed() throws Exception {
		handleMessage("06:51:00:00:04:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.ON);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey4Pressed() throws Exception {
		handleMessage("06:51:00:00:08:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.ON);
	}

}
