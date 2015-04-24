package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.ulux.internal.EmptyDatagramMatcher.isEmptyDatagram;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class LedMessageTest extends AbstractUluxMessageTest {

	@Test
	public void testCommandOn() throws Exception {
		addBindingConfig(new NumberItem("Ulux_Led"), "1:1:Led:2");

		receiveCommand("Ulux_Led", new DecimalType(49));

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("08:44:01:00:00:31:00:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testRequest() throws Exception {
		byte[] actual = toBytes(new LedMessage((short) 3));
		byte[] expected = toBytes("04:44:03:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new NumberItem("Ulux_Led"), "1:1:Led:2");

		receiveUpdate("Ulux_Led", new DecimalType(5));

		assertThat(datagram, isEmptyDatagram());
	}

}
