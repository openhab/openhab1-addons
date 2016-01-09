package org.openhab.binding.pixtend.internal;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhab.binding.pixtend.PiXtendBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;

import de.goroot.pixtend4j.pixtend4j.PiXtend;

@RunWith(MockitoJUnitRunner.class)
public class PiXtendBindingTest {

	@Mock
	private PiXtendBindingProvider bindingProviderMock;

	@Mock
	private EventPublisher publisherMock;

	@Mock
	private PiXtend piXtendMock;
	private PiXtendBinding testling;

	class IsStateThatEqualsString extends ArgumentMatcher<State> {
		private String expected;

		public IsStateThatEqualsString(String equalityValue) {
			expected = equalityValue;
		}

		@Override
		public boolean matches(Object list) {
			return StringUtils.equals(list.toString(), expected);
		}
	}

	@Before
	public void setup() throws Exception {
		testling = new PiXtendBinding();
		testling.setEventPublisher(publisherMock);

		Field connectorField = testling.getClass().getDeclaredField("connector");
		connectorField.setAccessible(true);
		connectorField.set(testling, piXtendMock);
	}

	@Test
	public void testAllValuesRead_noBindingDefined_success() {
		testling.execute();

		verify(piXtendMock).getAnalogInput(0);
		verify(piXtendMock).getAnalogInput(1);
		verify(piXtendMock).getAnalogInput(2);
		verify(piXtendMock).getAnalogInput(3);
		verify(piXtendMock).getDigitalInput(0);
		verify(piXtendMock).getDigitalInput(1);
		verify(piXtendMock).getDigitalInput(2);
		verify(piXtendMock).getDigitalInput(3);
		verify(piXtendMock).getDigitalInput(4);
		verify(piXtendMock).getDigitalInput(5);
		verify(piXtendMock).getDigitalInput(6);
		verify(piXtendMock).getDigitalInput(7);
		verify(piXtendMock).getGpioValue(0);
		verify(piXtendMock).getGpioValue(1);
		verify(piXtendMock).getGpioValue(2);
		verify(piXtendMock).getGpioValue(3);
		verify(piXtendMock).getUcVersion();
		verify(piXtendMock).getUcStatusRegister();

		verifyNoMoreInteractions(piXtendMock);
	}

	@Test
	public void testValuesPublished_noBindingDefined_NoPublishCalls() {
		when(piXtendMock.getAnalogInput(anyInt())).thenReturn((short) 10, (short) 20, (short) 30, (short) 40);

		verifyNoMoreInteractions(publisherMock);
	}

	@Test
	public void testValuesPublished_AI1BindingDefined_NoPublishCalls() {
		List<String> boundItems = new LinkedList<String>();
		boundItems.add("TEST_1");
		boundItems.add("TEST_2");
		boundItems.add("TEST_3");
		when(piXtendMock.getAnalogInput(1)).thenReturn((short) 10, (short) 20, (short) 30, (short) 40);
		when(bindingProviderMock.getItemsForDataPort(PiXtendPort.AI1)).thenReturn(boundItems);
		testling.addBindingProvider(bindingProviderMock);

		testling.execute();

		verify(publisherMock).postUpdate(eq("TEST_1"), argThat(new IsStateThatEqualsString("10")));
		verify(publisherMock).postUpdate(eq("TEST_2"), argThat(new IsStateThatEqualsString("10")));
		verify(publisherMock).postUpdate(eq("TEST_3"), argThat(new IsStateThatEqualsString("10")));
		verifyNoMoreInteractions(publisherMock);
	}

	@Test
	public void testReceiveCommand_NoBindingsDefined_NothingHappens() {
		testling.addBindingProvider(bindingProviderMock);

		testling.internalReceiveCommand("FOO", OnOffType.ON);

		verifyNoMoreInteractions(piXtendMock);
	}

	@Test
	public void testReceiveCommand_AnalogOutBindingDefined_ValueWrittenToFoo() {
		when(bindingProviderMock.getOutPort("FOO")).thenReturn(PiXtendPort.AO0);
		when(bindingProviderMock.getOutPort("FOO1")).thenReturn(PiXtendPort.AO1);
		testling.addBindingProvider(bindingProviderMock);

		testling.internalReceiveCommand("FOO", DecimalType.valueOf("10.123"));
		testling.internalReceiveCommand("FOOBAR", DecimalType.valueOf("1"));
		testling.internalReceiveCommand("FOO", DecimalType.valueOf("2"));

		verify(piXtendMock).setAnalogOutput(0, (short) 10);
		verify(piXtendMock).setAnalogOutput(0, (short) 2);
		verifyNoMoreInteractions(piXtendMock);
	}

	@Test
	public void testReceiveCommand_IncompatibleAnalogCommand_CommandDiscarded() {
		when(bindingProviderMock.getOutPort("FOO")).thenReturn(PiXtendPort.AO0);
		testling.addBindingProvider(bindingProviderMock);

		testling.internalReceiveCommand("FOO", OnOffType.OFF);

		verifyNoMoreInteractions(piXtendMock);
	}

	@Test
	public void testReceiveCommand_IncompatibleDigitalCommand_CommandDiscarded() {
		when(bindingProviderMock.getOutPort("FOO")).thenReturn(PiXtendPort.RESET_UC);
		testling.addBindingProvider(bindingProviderMock);

		testling.internalReceiveCommand("FOO", DecimalType.valueOf("13.12"));

		verifyNoMoreInteractions(piXtendMock);
	}
}
