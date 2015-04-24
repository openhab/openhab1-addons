package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.EmptyDatagramMatcher.isEmptyDatagram;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractCommandTest;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.PercentType;

public class AudioVolumeMessageTest extends AbstractCommandTest {

	@Test
	public void testCommand() throws Exception {
		addBindingConfig(new DimmerItem("Ulux_AudioVolume"), "1:0:AudioVolume");

		receiveCommand("Ulux_AudioVolume", new PercentType(75));

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:91:00:00:4B:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testRequest() throws Exception {
		byte[] actual = toBytes(new AudioVolumeMessage());
		byte[] expected = toBytes("04:91:00:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new DimmerItem("Ulux_AudioVolume"), "1:0:AudioVolume");

		receiveUpdate("Ulux_AudioVolume", new PercentType(75));

		assertThat(datagram, isEmptyDatagram());
	}
}
