package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.PageIndexMessage;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;

public class PageIndexMessageHandlerTest extends AbstractHandlerTest<PageIndexMessage> {

	@Test
	public void testNoConfig() throws Exception {
		handleMessage("06:2e:00:00:00:00");

		// nothing happens
	}

	@Test
	public void testPage2() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Page"), "1:0:PageIndex");

		handleMessage("06:2e:00:00:02:00");

		verify(eventPublisher).postUpdate("Ulux_Page", new DecimalType(2));
	}

}
