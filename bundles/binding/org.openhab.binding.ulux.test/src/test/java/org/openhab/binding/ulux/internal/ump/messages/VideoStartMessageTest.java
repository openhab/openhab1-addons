package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class VideoStartMessageTest extends AbstractUluxMessageTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Video"), "{switchId=1, type='VIDEO'}");

		receiveCommand("Ulux_Video", OnOffType.ON);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("0C:A2:00:00:00:00:00:00:01:00:00:00");

		assertThat(actual, equalTo(expected));
	}

	// testCommandOff see VideoStopMessageTest

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Video"), "{switchId=1, type='VIDEO'}");

		receiveUpdate("Ulux_Video", OnOffType.OFF);

		assertTrue(datagramList.isEmpty());
	}

}
