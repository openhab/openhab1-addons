package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.LedMessage;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class LedMessageHandlerTest extends AbstractHandlerTest<LedMessage> {

	@Override
	protected AbstractMessageHandler<LedMessage> createMessageHandler() {
		return new LedMessageHandler();
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new NumberItem("Ulux_Led"), "1:1:Led:2");

		handleMessage("08:44:01:00:00:31:00:00");

		verify(eventPublisher).postUpdate("Ulux_Led", new DecimalType(49));
	}

}
