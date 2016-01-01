package org.openhab.binding.lightwaverf.internal;

import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSetHeatingTemperatureCommand;
import org.openhab.core.library.types.OnOffType;

public class LightwaveRfWifiLinkTest {

	private final String LIGHTWAVE_IP = "255.255.255.255";
	private final int TRANSMIT_PORT = 9760;
	private final int RECEIVE_PORT = 9761;
	private final LightwaverfConvertor CONVERTOR = new LightwaverfConvertor();
	private final int TIME_BETWEEN_COMMANDS = 2000;
	private final int TIMEOUT_OK = 2000;
	private final int THREAD_SLEEP = 10 * 1000;
	
	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testSwitch() throws Exception {
		LightwaveRfWifiLink wifiLink = new LightwaveRfWifiLink(
				LIGHTWAVE_IP, 
				TRANSMIT_PORT, 
				RECEIVE_PORT, 
				CONVERTOR, 
				TIME_BETWEEN_COMMANDS, 
				TIMEOUT_OK);
		wifiLink.start();
		
		LightwaveRFCommand command = CONVERTOR.convertToLightwaveRfMessage("3", "5", LightwaveRfType.SWITCH, OnOffType.OFF);
		wifiLink.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}	
	
	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testDimmer() throws Exception {
		LightwaveRfWifiLink wifiLink = new LightwaveRfWifiLink(
				LIGHTWAVE_IP, 
				TRANSMIT_PORT, 
				RECEIVE_PORT, 
				CONVERTOR, 
				TIME_BETWEEN_COMMANDS, 
				TIMEOUT_OK);
		wifiLink.start();
		
		LightwaveRFCommand command = CONVERTOR.convertToLightwaveRfMessage("2", "2", LightwaveRfType.DIMMER, OnOffType.OFF);
		wifiLink.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}	

	
	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testSetHeating() throws Exception {
		LightwaveRfWifiLink wifiLink = new LightwaveRfWifiLink(
				LIGHTWAVE_IP, 
				TRANSMIT_PORT, 
				RECEIVE_PORT, 
				CONVERTOR, 
				TIME_BETWEEN_COMMANDS, 
				TIMEOUT_OK);
		wifiLink.start();
		
		LightwaveRFCommand command = new LightwaveRfSetHeatingTemperatureCommand(100, "4", 17);
		wifiLink.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}			

	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testHeatingInfo() throws Exception {
		LightwaveRfWifiLink wifiLink = new LightwaveRfWifiLink(
				LIGHTWAVE_IP, 
				TRANSMIT_PORT, 
				RECEIVE_PORT, 
				CONVERTOR, 
				TIME_BETWEEN_COMMANDS, 
				TIMEOUT_OK);
		wifiLink.start();
		
		LightwaveRfHeatInfoRequest command = new LightwaveRfHeatInfoRequest(500, "4");
		wifiLink.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}
	
	@Test
	@Ignore(value="This is a functional test to check the code is working")
	public void test() throws Exception {
		LightwaveRfWifiLink wifiLink = new LightwaveRfWifiLink(
				LIGHTWAVE_IP, 
				TRANSMIT_PORT, 
				RECEIVE_PORT, 
				CONVERTOR, 
				TIME_BETWEEN_COMMANDS, 
				TIMEOUT_OK);
		wifiLink.start();		
		Thread.sleep(5 * 1000 * 60);	
	}	
}
