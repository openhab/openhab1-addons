package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.EmptyDatagramMatcher.isEmptyDatagram;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class AudioRecordMessageTest extends AbstractUluxMessageTest {

	@Test
	public void testCommand() throws Exception {
		addConfiguration("bind_address", "192.168.1.1");
		addBindingConfig(new SwitchItem("Ulux_AudioRecord"), "1:0:AudioRecord");

		receiveCommand("Ulux_AudioRecord", OnOffType.ON);

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("20:9A:00:00:40:00:00:00:72:03:00:00:20:4E:00:00:01:00:00:00:C0:A8:01:01:00:00:00:00:00:00:00:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testUpdate() throws Exception {
		addConfiguration("bind_address", "192.168.1.1");
		addBindingConfig(new SwitchItem("Ulux_AudioRecord"), "1:0:AudioRecord");

		receiveUpdate("Ulux_AudioRecord", OnOffType.ON);

		assertThat(datagram, isEmptyDatagram());
	}

}
