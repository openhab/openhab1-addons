package org.openhab.binding.lightwaverf.internal;

import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openhab.binding.lightwaverf.LightwaveRFBindingProvider;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfOnOffCommand;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class LightwaveRFBindingTest {

	@Mock EventPublisher mockEventPublisher;
	@Mock LightwaveRFBindingProvider mockBindingProvider; 
	@Mock LightwaveRFSender mockLightwaveRfSender;
	@Mock LightwaverfConvertor mockLightwaveRfConvertor;
	@Mock LightwaveRFCommand mockLightwaveRfCommand;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testInternalReceiveCommand() {
		when(mockBindingProvider.getRoomId("MySwitch")).thenReturn("2");
		when(mockBindingProvider.getDeviceId("MySwitch")).thenReturn("3");
		when(mockLightwaveRfConvertor.convertToLightwaveRfMessage("2", "3", OnOffType.ON)).thenReturn(mockLightwaveRfCommand);
		
		LightwaveRFBinding binding = new LightwaveRFBinding();
		binding.addBindingProvider(mockBindingProvider);
		binding.setLightaveRfSender(mockLightwaveRfSender);
		binding.setLightwaveRfConvertor(mockLightwaveRfConvertor);
		binding.internalReceiveCommand("MySwitch", OnOffType.ON);
		verify(mockLightwaveRfSender).sendUDP(mockLightwaveRfCommand);
	}

	@Test
	public void testInternalReceiveUpdate() {
		when(mockBindingProvider.getRoomId("MySwitch")).thenReturn("2");
		when(mockBindingProvider.getDeviceId("MySwitch")).thenReturn("3");
		when(mockLightwaveRfConvertor.convertToLightwaveRfMessage("2", "3", OnOffType.ON)).thenReturn(mockLightwaveRfCommand);
		
		LightwaveRFBinding binding = new LightwaveRFBinding();
		binding.addBindingProvider(mockBindingProvider);
		binding.setLightaveRfSender(mockLightwaveRfSender);
		binding.setLightwaveRfConvertor(mockLightwaveRfConvertor);
		binding.internalReceiveUpdate("MySwitch", OnOffType.ON);
		verify(mockLightwaveRfSender).sendUDP(mockLightwaveRfCommand);	}

	@Test
	public void testMessageRecevied() {
		when(mockBindingProvider.getBindingItemsForRoomDevice("2", "3")).thenReturn(Arrays.asList("MySwitch"));
		when(mockBindingProvider.getTypeForItemName("MySwitch")).thenReturn(LightwaveRfType.SWITCH);
		
		LightwaveRFBinding binding = new LightwaveRFBinding();
		binding.addBindingProvider(mockBindingProvider);
		binding.setEventPublisher(mockEventPublisher);
		binding.messageRecevied(new LightwaveRfOnOffCommand(1, "2", "3", true));
		verify(mockEventPublisher).postUpdate("MySwitch", OnOffType.ON);
	}
	

}
