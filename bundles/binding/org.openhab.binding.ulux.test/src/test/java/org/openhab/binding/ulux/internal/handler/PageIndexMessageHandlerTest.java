package org.openhab.binding.ulux.internal.handler;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.PageIndexMessage;

public class PageIndexMessageHandlerTest extends AbstractHandlerTest<PageIndexMessage> {

	@Override
	protected AbstractMessageHandler<PageIndexMessage> createMessageHandler() {
		return new PageIndexMessageHandler();
	}

	@Test
	public void test() throws Exception {
		handleMessage("06:2e:00:00:00:00");

		// nothing happens
	}

}
