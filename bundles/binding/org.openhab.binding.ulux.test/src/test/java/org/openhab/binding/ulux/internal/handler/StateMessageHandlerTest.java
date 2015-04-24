package org.openhab.binding.ulux.internal.handler;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.StateMessage;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class StateMessageHandlerTest extends AbstractHandlerTest<StateMessage> {

	@Test
	public void testInitRequest() throws Exception {
		handleMessage("08:01:00:00:60:00:00:00");

		assertThat(response.hasMessages(), equalTo(true));
		// TODO more assertions
	}

	@Test
	public void testDisplayActive() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Display"), "1:0:Display");

		handleMessage("08:01:00:00:04:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_Display", OnOffType.ON);
	}

	@Test
	public void testProximityDetected() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Proximity"), "1:0:Proximity");

		handleMessage("08:01:00:00:02:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_Proximity", OnOffType.ON);
	}

	@Test
	public void testAmbientLight() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_AmbientLight"), "1:0:AmbientLight");

		handleMessage("08:01:00:00:01:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_AmbientLight", OnOffType.ON);
	}

	@Test
	public void testAudioPlaying() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "1:0:Audio");

		handleMessage("08:01:00:00:08:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_Audio", OnOffType.ON);
	}

}
