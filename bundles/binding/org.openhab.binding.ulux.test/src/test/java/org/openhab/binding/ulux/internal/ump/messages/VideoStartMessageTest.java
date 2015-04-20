package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.EmptyDatagramMatcher.isEmptyDatagram;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractCommandTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class VideoStartMessageTest extends AbstractCommandTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Video"), "1:0:Video");

		receiveCommand("Ulux_Video", OnOffType.ON);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("0C:A2:00:00:00:00:00:00:01:00:00:00");

		assertThat(actual, equalTo(expected));
	}

	// testCommandOff see VideoStopMessageTest

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Video"), "1:0:Video");

		receiveUpdate("Ulux_Video", OnOffType.OFF);

		assertThat(datagram, isEmptyDatagram());
	}

}
