package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractCommandTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class AudioStopMessageTest extends AbstractCommandTest {

	@Test
	public void testCommand() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_AudioStop"), "1:0:AudioStop");

		receiveCommand("Ulux_AudioStop", OnOffType.ON);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:92:00:00:03:00");

		assertThat(actual, equalTo(expected));
	}

}
