package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class PageIndexMessageTest extends AbstractUluxMessageTest {

	@Test
	public void testCommand() throws Exception {
		addBindingConfig(new NumberItem("Ulux_Page"), "{switchId=1, type='PAGE_INDEX'}");

		receiveCommand("Ulux_Page", new DecimalType(2));

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("06:2e:00:00:02:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testRequest() throws Exception {
		byte[] actual = toBytes(new PageIndexMessage());
		byte[] expected = toBytes("04:2e:00:00");

		assertThat(actual, equalTo(expected));
	}

	@Test
	public void testUpdate() throws Exception {
		addBindingConfig(new NumberItem("Ulux_Page"), "{switchId=1, type='PAGE_INDEX'}");

		receiveUpdate("Ulux_Page", new DecimalType(2));

		assertTrue(datagramList.isEmpty());
	}

}
