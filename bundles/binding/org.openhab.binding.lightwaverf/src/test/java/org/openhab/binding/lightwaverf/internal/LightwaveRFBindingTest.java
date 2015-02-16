package org.openhab.binding.lightwaverf.internal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openhab.binding.lightwaverf.LightwaveRFBindingProvider;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatingInfoResponse;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfOnOffCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;

public class LightwaveRFBindingTest {

	@Mock EventPublisher mockEventPublisher;
	@Mock LightwaveRFBindingProvider mockBindingProvider; 
	@Mock LightwaveRFSender mockLightwaveRfSender;
	@Mock LightwaverfConvertor mockLightwaveRfConvertor;
	@Mock LightwaveRFCommand mockLightwaveRfCommand;
	
	@Mock LightwaveRfVersionMessage mockVersionMessage;
	@Mock LightwaveRfHeatingInfoResponse mockHeatInfoResponse;
	@Mock LightwaveRfHeatInfoRequest mockHeatInfoRequest;
	
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
		verify(mockLightwaveRfSender).sendUDP(mockLightwaveRfCommand);	
	}

	
	@Test
	public void testRoomDeviceMessageRecevied() {
		when(mockBindingProvider.getBindingItemsForRoomDevice("2", "3")).thenReturn(Arrays.asList("MySwitch"));
		when(mockBindingProvider.getTypeForItemName("MySwitch")).thenReturn(LightwaveRfType.SWITCH);

		LightwaveRFBinding binding = new LightwaveRFBinding();
		binding.addBindingProvider(mockBindingProvider);
		binding.setEventPublisher(mockEventPublisher);
		binding.roomDeviceMessageReceived(new LightwaveRfOnOffCommand(1, "2", "3", true));
		verify(mockEventPublisher).postUpdate("MySwitch", OnOffType.ON);
	}
	
	@Test
	public void testRoomMessageRecevied() {
		when(mockHeatInfoRequest.getState(LightwaveRfType.HEATING_SET_TEMP)).thenReturn(new DecimalType(24.5));
		when(mockHeatInfoRequest.getRoomId()).thenReturn("2");
		when(mockBindingProvider.getBindingItemsForRoom("2")).thenReturn(Arrays.asList("Temp"));
		when(mockBindingProvider.getTypeForItemName("Temp")).thenReturn(LightwaveRfType.HEATING_SET_TEMP);

		LightwaveRFBinding binding = new LightwaveRFBinding();
		binding.addBindingProvider(mockBindingProvider);
		binding.setEventPublisher(mockEventPublisher);
		binding.roomMessageReceived(mockHeatInfoRequest);

		verify(mockEventPublisher).postUpdate("Temp", new DecimalType(24.5));

	}
	
	@Test
	public void testSerialMessageRecevied() {
		when(mockHeatInfoResponse.getState(LightwaveRfType.HEATING_BATTERY)).thenReturn(new DecimalType(2.99));
		when(mockHeatInfoResponse.getState(LightwaveRfType.HEATING_CURRENT_TEMP)).thenReturn(new DecimalType(22.3));
		when(mockHeatInfoResponse.getSerial()).thenReturn("655432");
		when(mockBindingProvider.getBindingItemsForSerial("655432")).thenReturn(Arrays.asList("Battery", "Temp"));
		when(mockBindingProvider.getTypeForItemName("Battery")).thenReturn(LightwaveRfType.HEATING_BATTERY);
		when(mockBindingProvider.getTypeForItemName("Temp")).thenReturn(LightwaveRfType.HEATING_CURRENT_TEMP);

		LightwaveRFBinding binding = new LightwaveRFBinding();
		binding.addBindingProvider(mockBindingProvider);
		binding.setEventPublisher(mockEventPublisher);
		binding.serialMessageReceived(mockHeatInfoResponse);

		verify(mockEventPublisher).postUpdate("Battery", new DecimalType(2.99));
		verify(mockEventPublisher).postUpdate("Temp", new DecimalType(22.3));
	}
	
	@Test
	public void testVersionMessageRecevied() {
		when(mockVersionMessage.getState(LightwaveRfType.VERSION)).thenReturn(new StringType("2.91"));
		when(mockBindingProvider.getBindingItemsForType(LightwaveRfType.VERSION)).thenReturn(Arrays.asList("MyVersion"));
		when(mockBindingProvider.getTypeForItemName("MyVersion")).thenReturn(LightwaveRfType.VERSION);

		LightwaveRFBinding binding = new LightwaveRFBinding();
		binding.addBindingProvider(mockBindingProvider);
		binding.setEventPublisher(mockEventPublisher);
		binding.versionMessageReceived(mockVersionMessage);
		verify(mockEventPublisher).postUpdate("MyVersion", new StringType("2.91"));
	}
}
