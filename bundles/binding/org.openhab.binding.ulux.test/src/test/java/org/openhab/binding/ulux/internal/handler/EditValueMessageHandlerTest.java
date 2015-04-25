package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.UpDownType;

public class EditValueMessageHandlerTest extends AbstractUluxMessageTest {

	@Test
	public void testColorItem() throws Exception {
		addBindingConfig(new ColorItem("Ulux_Color"), "{switchId=1, actorId=1, type='EDIT_VALUE'}");

		handleMessage("06:42:01:00:01:00");

		verify(eventPublisher).postCommand("Ulux_Color", IncreaseDecreaseType.INCREASE);
	}

	@Test
	public void testDimmerItem() throws Exception {
		addBindingConfig(new DimmerItem("Ulux_Dimmer"), "{switchId=1, actorId=1, type='EDIT_VALUE'}");

		handleMessage("06:42:01:00:01:00");

		verify(eventPublisher).postCommand("Ulux_Dimmer", IncreaseDecreaseType.INCREASE);
	}

	@Test
	public void testNumberItem() throws Exception {
		addBindingConfig(new NumberItem("Ulux_Number"), "{switchId=1, actorId=1, type='EDIT_VALUE'}");

		handleMessage("06:42:01:00:15:00");

		verify(eventPublisher).postCommand("Ulux_Number", new DecimalType(21));
	}

	@Test
	public void testRollershutterItem() throws Exception {
		addBindingConfig(new RollershutterItem("Ulux_Rollershutter"), "{switchId=1, actorId=1, type='EDIT_VALUE'}");

		handleMessage("06:42:01:00:01:00");

		verify(eventPublisher).postCommand("Ulux_Rollershutter", UpDownType.UP);
	}

	@Test
	public void testSwitchItem() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Switch"), "{switchId=1, actorId=1, type='EDIT_VALUE'}");

		handleMessage("06:42:01:00:01:00");

		verify(eventPublisher).postCommand("Ulux_Switch", OnOffType.ON);
	}

}
