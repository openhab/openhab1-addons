package org.openhab.binding.ulux.internal.ump.messages;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractCommandTest;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;

public class TextMessageTest extends AbstractCommandTest {

	@Test
	public void testCommand() throws Exception {
		addBindingConfig(new StringItem("Ulux_Text"), "1:1:EditValue");

		receiveCommand("Ulux_Text", new StringType("Example"));

		byte[] actual = toBytes(datagram);
		byte[] expected = toBytes("14:45:01:00:00:00:00:00:00:00:00:00:45:78:61:6D:70:6C:65:00");

		assertThat(actual, equalTo(expected));
	}
}
