package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openhab.binding.ulux.internal.AudioDatagramMatcher.isAudioDatagram;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.StringType;

public class AudioPlayRemoteMessageTest extends AbstractUluxMessageTest {

	private static final String AUDIO_FILE = "file:src/test/resources/ulux.wav";

	@Test
	public void testCommand() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "{switchId=1, type='AUDIO_PLAY_REMOTE'}");

		receiveCommand("Ulux_Audio", new StringType(AUDIO_FILE));

		byte[] actual = toBytes(datagramList.poll());
		byte[] expected = toBytes("20:99:00:00:64:00:0A:00:00:00:00:00:72:03:00:00:20:4E:00:00:FF:FF:FF:FF:00:00:00:00:00:00:00:00");

		assertThat(actual, equalTo(expected));

		assertThat(datagramList.size(), equalTo(73));

		for (int i = 1; !datagramList.isEmpty(); i++) {
			final UluxDatagram datagram = datagramList.poll();

			assertThat(datagram, isAudioDatagram());

			// TODO
			// if (i % 10 == 0) {
			// actual = toBytes(datagram);
			// expected = toBytes(new File("src/test/resources/AudioStreamMessageTest-" + i + ".txt"));
			//
			// assertThat(actual, equalTo(expected));
			// }
		}
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Audio"), "{switchId=1, type='AUDIO_PLAY_REMOTE'}");

		receiveUpdate("Ulux_Audio", new StringType(AUDIO_FILE));

		assertTrue(datagramList.isEmpty());
	}
}
