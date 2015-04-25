package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.UnDefType;

public class LuxMessageHandlerTest extends AbstractUluxMessageTest {

	@Test
	public void testInvalid() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Lux"), "{switchId=1, type='LUX'}");

		handleMessage("08:03:00:00:00:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_Lux", UnDefType.UNDEF);
	}

	@Test
	public void testValid() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Lux"), "{switchId=1, type='LUX'}");

		handleMessage("08:03:00:00:13:02:01:00");

		verify(eventPublisher).postUpdate("Ulux_Lux", new DecimalType(531));
	}

}
