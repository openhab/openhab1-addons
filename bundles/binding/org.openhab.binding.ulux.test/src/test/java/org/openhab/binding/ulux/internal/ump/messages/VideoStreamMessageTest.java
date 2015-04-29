package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.VideoDatagramMatcher.isVideoDatagram;

import java.io.File;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;

public class VideoStreamMessageTest extends AbstractUluxMessageTest {

	@Test
	public void testImage() throws Exception {
		addBindingConfig(new StringItem("Ulux_Image"), "{switchId=1, type='IMAGE'}");

		receiveCommand("Ulux_Image", new StringType("http://www.openhab.org/assets/images/openhab-logo-square.png"));

		byte[] actual = toBytes(datagramList.poll());
		byte[] expected = toBytes("0C:A2:00:00:00:00:00:00:01:00:00:00");

		assertThat(actual, equalTo(expected));

		assertThat(datagramList.size(), equalTo(46));

		for (int i = 1; !datagramList.isEmpty(); i++) {
			final UluxDatagram datagram = datagramList.poll();

			assertThat(datagram, isVideoDatagram());

			if (i % 10 == 0) {
				actual = toBytes(datagram);
				expected = toBytes(new File("src/test/resources/VideoStreamMessageTest-" + i + ".txt"));

				assertThat(actual, equalTo(expected));
			}
		}
	}

}
