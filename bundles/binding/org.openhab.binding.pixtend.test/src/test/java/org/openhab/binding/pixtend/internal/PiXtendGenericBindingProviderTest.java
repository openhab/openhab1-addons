package org.openhab.binding.pixtend.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

@RunWith(MockitoJUnitRunner.class)
public class PiXtendGenericBindingProviderTest {

	@Mock
	private Item itemMock1;

	private PiXtendGenericBindingProvider testling;

	@Before
	public void setup() {

		testling = new PiXtendGenericBindingProvider();

		when(itemMock1.getName()).thenReturn("ITEM1");

	}

	@Test
	public void testProcessBindingConfiguration_ValidBinding_BindingAddedToConfig() throws Exception {

		testling.processBindingConfiguration("context.items", itemMock1, "AI0");

		assertEquals(PiXtendPort.AI0, testling.getOutPort("ITEM1"));

	}

	@Test(expected = BindingConfigParseException.class)
	public void testProcessBindingConfiguration_InvalidBindingChars_ExceptionThrown() throws Exception {
		testling.processBindingConfiguration("context.items", itemMock1, "§!%§!$");
	}

	@Test(expected = BindingConfigParseException.class)
	public void testProcessBindingConfiguration_InvalidPort_ExceptionThrown() throws Exception {
		testling.processBindingConfiguration("context.items", itemMock1, "A100");
	}

}
