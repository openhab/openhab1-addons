package org.openhab.binding.ulux.internal.handler;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.StateMessage;

public class StateMessageHandlerTest extends AbstractHandlerTest<StateMessage> {

	@Override
	protected AbstractMessageHandler<StateMessage> createMessageHandler() {
		return new StateMessageHandler();
	}

	@Test
	public void test() throws Exception {
		handleMessage("08:01:00:00:60:00:00:00");

		// TODO
		// assertThat(response.hasMessages(), equalTo(true));
	}

}
