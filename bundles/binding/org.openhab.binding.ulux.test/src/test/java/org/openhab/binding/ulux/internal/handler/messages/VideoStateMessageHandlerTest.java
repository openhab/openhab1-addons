package org.openhab.binding.ulux.internal.handler.messages;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class VideoStateMessageHandlerTest extends AbstractUluxMessageTest {

	@Test
	public void testNoConfig() throws Exception {
		handleMessage("10:A1:00:00:01:00:00:00:00:00:00:00:00:00:00:00");

		// nothing happens
	}

	@Test
	public void testVideoActive() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Video"), "{switchId=1, type='VIDEO'}");

		handleMessage("10:A1:00:00:01:00:00:00:00:00:00:00:00:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_Video", OnOffType.ON);
	}

	@Test
	public void testVideoPossible() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Video"), "{switchId=1, type='VIDEO'}");

		handleMessage("10:A1:00:00:02:00:00:00:00:00:00:00:00:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_Video", OnOffType.OFF);
	}

}
