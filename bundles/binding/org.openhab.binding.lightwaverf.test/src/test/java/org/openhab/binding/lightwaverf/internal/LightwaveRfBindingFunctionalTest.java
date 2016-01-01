/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public class LightwaveRfBindingFunctionalTest {

	private static final String CONTEXT = "";
	private static final String WIFILINK_IP = "127.0.0.1";
	private static final int TRANSMIT_PORT = 9760;
	private static final int TIME_BETWEEN_COMMANDS_MS = 100;
	private static final int TIMEOUT_OK_MESSAGES_MS = 1000;
	
	
	
	@Mock EventPublisher mockEventPublisher;
	
	@Mock DatagramSocket mockReceiveSocket;
	@Mock DatagramSocket mockReceiveSocket2;
	@Mock DatagramSocket mockTransmitSocket;
	
	LightwaveRfBinding binding;
	LightwaveRfGenericBindingProvider bindingProvider;
	LightwaverfConvertor messageConvertor;
	LightwaveRfWifiLink wifiLink;
	
	@Before
	public void init() throws Exception{
		MockitoAnnotations.initMocks(this);
		binding = new LightwaveRfBinding();
		bindingProvider = new LightwaveRfGenericBindingProvider();
		messageConvertor = new LightwaverfConvertor();
		wifiLink = new LightwaveRfWifiLink(WIFILINK_IP, TRANSMIT_PORT, mockReceiveSocket, mockReceiveSocket2, mockTransmitSocket, messageConvertor, TIME_BETWEEN_COMMANDS_MS, TIMEOUT_OK_MESSAGES_MS);
		
		binding.setLightwaveRfConvertor(messageConvertor);
		binding.setWifiLink(wifiLink);
		binding.setEventPublisher(mockEventPublisher);
	}

	@Test
	public void testDimmingCommandZero() throws Exception {
		testSendingACommandAndVerify(new DimmerItem("MyDimmer"), "room=1,device=2,type=DIMMER", new PercentType("00"), "200,!R1D2F0\n");
	}
	
	@Test
	public void testDimmingCommandSend() throws Exception {
		testSendingACommandAndVerify(new DimmerItem("MyDimmer"), "room=1,device=2,type=DIMMER", new PercentType("50"), "200,!R1D2FdP16\n");
	}

	@Test
	public void testDimmingCommandReceive() throws Exception {
		testReceivingACommandAndVerify(new DimmerItem("MyDimmer"), "room=1,device=2,type=DIMMER", "200,!R1D2FdP16\n", new PercentType("50"));
	}
	
	@Test
	public void testOnCommandSend() throws Exception {
		testSendingACommandAndVerify(new SwitchItem("MySwitch"), "room=1,device=2,type=SWITCH", OnOffType.ON, "200,!R1D2F1\n");
	}
	
	@Test
	public void testOnCommandReceived() throws Exception {
		testReceivingACommandAndVerify(new SwitchItem("MySwitch"), "room=1,device=2,type=SWITCH", "200,!R1D2F1\n", OnOffType.ON);
	}
	
	@Test
	public void testOffCommandSend() throws Exception {
		testSendingACommandAndVerify(new SwitchItem("MySwitch"), "room=1,device=2,type=SWITCH", OnOffType.OFF, "200,!R1D2F0\n");
	}
	
	@Test
	public void testOffCommandReceived() throws Exception {
		testReceivingACommandAndVerify(new SwitchItem("MySwitch"), "room=1,device=2,type=SWITCH", "200,!R1D2F0\n", OnOffType.OFF);
	}	
	
	@Test
	public void testRelayCommandOpenSend() throws Exception {
		testSendingACommandAndVerify(new NumberItem("MyRelay"), "room=1,device=2,type=RELAY", new DecimalType("1"), "200,!R1D2F)\n");
	}

	@Test
	public void testRelayCommandCloseSend() throws Exception {
		testSendingACommandAndVerify(new NumberItem("MyRelay"), "room=1,device=2,type=RELAY", new DecimalType("-1"), "200,!R1D2F(\n");
	}

	@Test
	public void testRelayCommandStopSend() throws Exception {
		testSendingACommandAndVerify(new NumberItem("MyRelay"), "room=1,device=2,type=RELAY", new DecimalType("0"), "200,!R1D2F^\n");
	}
	
	@Test
	public void testHeatingInfoResponseReceived() throws Exception {
		String message = "*!{\"trans\":1506,\"mac\":\"03:02:71\",\"time\":1423850746,\"prod\":\"valve\",\"serial\":\"064402\",\"signal\":54,\"type\":\"temp\",\"batt\":2.99,\"ver\":56,\"state\":\"boost\",\"cTemp\":22.3,\"cTarg\":24.0,\"output\":100,\"nTarg\":20.0,\"nSlot\":\"18:45\",\"prof\":5}";
		List<ItemConfigAndExpectedState> itemConfigAndExpectedStates = new LinkedList<ItemConfigAndExpectedState>();
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new NumberItem("BATTERY"), "serial=064402,type=HEATING_BATTERY", new DecimalType("2.99")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new NumberItem("CURRENT_TEMP"), "serial=064402,type=HEATING_CURRENT_TEMP", new DecimalType("22.3")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new StringItem("MODE"), "serial=064402,type=HEATING_MODE", new StringType("boost")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new StringItem("VERSION"), "serial=064402,type=VERSION", new StringType("56")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new NumberItem("SIGNAL"), "serial=064402,type=SIGNAL", new DecimalType("54")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new NumberItem("SET_TEMP"), "serial=064402,type=HEATING_SET_TEMP", new DecimalType("24.0")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new NumberItem("OUTPUT"), "serial=064402,type=HEATING_OUTPUT", new DecimalType("100")));
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(1423850746000L));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new DateTimeItem("TIME"), "serial=064402,type=UPDATETIME", new DateTimeType(cal)));
		testReceivingACommandAndVerify(itemConfigAndExpectedStates, message);
	}
	
	@Test
	public void testWifiLinkStatusReceived() throws Exception {
		String message = "*!{\"trans\":452,\"mac\":\"ab:cd:ef\",\"time\":1447712274,\"type\":\"hub\",\"prod\":\"wfl\",\"fw\":\"U2.91Y\","
				 + "\"uptime\":1386309,\"timeZone\":0,\"lat\":52.48,\"long\":-87.89,\"duskTime\":1447690400,"
				 + "\"dawnTime\":1447659083,\"tmrs\":0,\"evns\":1,\"run\":0,\"macs\":8,\"ip\":\"192.168.0.1\",\"devs\":0}";

		List<ItemConfigAndExpectedState> itemConfigAndExpectedStates = new LinkedList<ItemConfigAndExpectedState>();
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new StringItem("WIFILINK_IP"), "serial=wifilink,type=WIFILINK_IP", new StringType("192.168.0.1")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new StringItem("WIFILINK_FIRMWARE"), "serial=wifilink,type=WIFILINK_FIRMWARE", new StringType("U2.91Y")));
		Calendar duskCal = Calendar.getInstance();
		duskCal.setTime(new Date(1447690400000L));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new DateTimeItem("WIFILINK_DUSK_TIME"), "serial=wifilink,type=WIFILINK_DUSK_TIME", new DateTimeType(duskCal)));
		Calendar dawnCal = Calendar.getInstance();
		dawnCal.setTime(new Date(1447659083000L));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new DateTimeItem("WIFILINK_DAWN_TIME"), "serial=wifilink,type=WIFILINK_DAWN_TIME", new DateTimeType(dawnCal)));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new NumberItem("WIFILINK_UPTIME"), "serial=wifilink,type=WIFILINK_UPTIME", new DecimalType("1386309")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new StringItem("WIFILINK_LONGITUDE"), "serial=wifilink,type=WIFILINK_LONGITUDE", new StringType("-87.89")));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new StringItem("WIFILINK_LATITUDE"), "serial=wifilink,type=WIFILINK_LATITUDE", new StringType("52.48")));
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(1447712274000L));
		itemConfigAndExpectedStates.add(new ItemConfigAndExpectedState(new NumberItem("TIME"), "serial=wifilink,type=UPDATETIME", new DateTimeType(cal)));
		testReceivingACommandAndVerify(itemConfigAndExpectedStates, message);
	}	
	
	@Test
	public void testMoodCommandReceive() throws Exception {
		testReceivingACommandAndVerify(new NumberItem("MyMood"), "room=1,type=MOOD", "200,!R1FmP2\n", new DecimalType("2"));
	}
	
	@Test
	public void testMoodCommandSend() throws Exception {
		testSendingACommandAndVerify(new NumberItem("MyMood"), "room=1,type=MOOD", new DecimalType("1"), "200,!R1FmP1\n");
	}
	
	@Test
	public void testAllOffCommandSend() throws Exception {
		testSendingACommandAndVerify(new SwitchItem("MyAllOff"), "room=2,type=ALL_OFF", OnOffType.OFF, "200,!R2Fa\n");
	}
	
	@Test
	public void testAllOffCommandReceive() throws Exception {
		testReceivingACommandAndVerify(new SwitchItem("MyAllOff"), "room=2,type=ALL_OFF", "200,!R2Fa\n", OnOffType.OFF);
	}
	
	@Test
	public void testUnknownCommand() throws Exception {
		String message = "{\"trans\":213454,\"mac\":\"03:02:71\",\"cmd\":\"get_duskdawn\",\"lat\":51.52,\"long\":-0.08,\"offset\":0}";
		testReceivingACommandAndVerify(new LinkedList<ItemConfigAndExpectedState>(), message);
	}
	
	@Test
	public void testOffMessageSentByAndriodApp() throws Exception {
		String message = "030271,102,!R3D1F0|Living Room|Side Light 1 Off";
		testReceivingACommandAndVerify(new DimmerItem("LivingRoom"), "room=3,device=1,type=DIMMER", message, OnOffType.OFF);
	}
	
	@Test
	public void testDimMessageSentByAndriodApp() throws Exception {
		String message = "030271,101,!R3D2FdP13|Living Room|Side Light 2 40%";
		testReceivingACommandAndVerify(new DimmerItem("LivingRoom"), "room=3,device=2,type=DIMMER", message, new PercentType("41"));
	}

	@Test
	public void testInOnlyMessageReceived() throws Exception {
		String message = "030271,101,!R3D2FdP13|Living Room|Side Light 2 40%";
		testReceivingACommandAndVerify(new DimmerItem("LivingRoom"), "<room=3,device=2,type=DIMMER", message, new PercentType("41"));
	}

	@Test
	public void testOutOnlyMessageReceived() throws Exception {
		String message = "030271,101,!R3D2FdP13|Living Room|Side Light 2 40%";
		testReceivingACommandAndVerifyNoInteractions(new DimmerItem("LivingRoom"), ">room=3,device=2,type=DIMMER", message, new PercentType("41"));
	}

	@Test
	public void testInOnlyCommandSend() throws Exception {
		testSendingACommandAndVerifyNoInteractions(new SwitchItem("MySwitch"), "<room=1,device=2,type=SWITCH", OnOffType.ON, "200,!R1D2F1\n");
	}

	@Test
	public void testOutOnlyCommandSend() throws Exception {
		testSendingACommandAndVerify(new SwitchItem("MySwitch"), ">room=1,device=2,type=SWITCH", OnOffType.ON, "200,!R1D2F1\n");
	}
	
	private void testReceivingACommandAndVerify(Item item, String itemConfig, String messageToReceive, State expectedState) throws Exception {
		List<ItemConfigAndExpectedState> listOfItemsAndExpectedStates = new LinkedList<ItemConfigAndExpectedState>();
		listOfItemsAndExpectedStates.add(new ItemConfigAndExpectedState(item, itemConfig, expectedState));
		testReceivingACommandAndVerify(listOfItemsAndExpectedStates, messageToReceive);
	}
	
	private void testReceivingACommandAndVerify(List<ItemConfigAndExpectedState> itemConfigAndExpectedStates, String messageToReceive) throws Exception {
		receiveACommand(itemConfigAndExpectedStates, messageToReceive);
		verifyReceivedCommands(itemConfigAndExpectedStates);
	}

	private void testReceivingACommandAndVerifyNoInteractions(Item item, String itemConfig, String messageToReceive, State expectedState) throws Exception {
		List<ItemConfigAndExpectedState> listOfItemsAndExpectedStates = new LinkedList<ItemConfigAndExpectedState>();
		listOfItemsAndExpectedStates.add(new ItemConfigAndExpectedState(item, itemConfig, expectedState));
		testReceivingACommandAndVerifyNoInteractions(listOfItemsAndExpectedStates, messageToReceive);
	}
	
	private void testReceivingACommandAndVerifyNoInteractions(List<ItemConfigAndExpectedState> itemConfigAndExpectedStates, String messageToReceive) throws Exception {
		receiveACommand(itemConfigAndExpectedStates, messageToReceive);
		verifyNoMoreInteractions(mockEventPublisher);
	}
	
	private void receiveACommand(List<ItemConfigAndExpectedState> itemConfigAndExpectedStates, String messageToReceive) throws Exception {
		// Set up sockets for testing
		CyclicBarrier sendMessageBarrier = new CyclicBarrier(2);
		CountDownLatch messageProcessedCountdownLatch = new CountDownLatch(itemConfigAndExpectedStates.size());
		
		doAnswer(sendMessageOnceUnlatched(messageToReceive, sendMessageBarrier)).when(mockReceiveSocket).receive(any(DatagramPacket.class));
		doAnswer(waitIndefinitely()).when(mockReceiveSocket2).receive(any(DatagramPacket.class));
		doAnswer(waitIndefinitely()).when(mockTransmitSocket).send(any(DatagramPacket.class));
		for(ItemConfigAndExpectedState t : itemConfigAndExpectedStates){
			doAnswer(unlatchWhenEventReceivedOrTimeout(messageProcessedCountdownLatch)).when(mockEventPublisher).postUpdate(t.getItem().getName(), t.getExpectedState());
		}

		// Setup Item config
		for(ItemConfigAndExpectedState t : itemConfigAndExpectedStates){
			bindingProvider.processBindingConfiguration(CONTEXT, t.getItem(), t.getItemConfig());
		}
		binding.addBindingProvider(bindingProvider);

		// Activate the binding ready for the test
		binding.activateForTesting();		
		
		// Receive the command
		sendMessageBarrier.await();
		// Wait till the receive has been processes we use a timeout in case we don't receive anything
		messageProcessedCountdownLatch.await(1000, TimeUnit.MILLISECONDS);

	}
	
	private void verifyReceivedCommands(List<ItemConfigAndExpectedState> itemConfigAndExpectedStates){
		// Validate what we sent on the event bus
		for(ItemConfigAndExpectedState t : itemConfigAndExpectedStates){
			verify(mockEventPublisher).postUpdate(t.getItem().getName(), t.getExpectedState());
		}
		verifyNoMoreInteractions(mockEventPublisher);
	}
	
	private void testSendingACommandAndVerify(Item item, String itemConfig, Command command, String expectedCommand) throws Exception {
		DatagramPacket sentPacket = sendCommand(item, itemConfig, command);
		// Validate what we sent
		verifyCommandSent(expectedCommand, sentPacket);
	}	

	private void testSendingACommandAndVerifyNoInteractions(Item item, String itemConfig, Command command, String expectedCommand) throws Exception {
		DatagramPacket sentPacket = null;
		try{
			sentPacket = sendCommand(item, itemConfig, command);
		}
		catch(TimeoutException e){
			assertEquals(null, sentPacket);
		}
	}	
	
	
	private DatagramPacket sendCommand(Item item, String itemConfig, Command command) throws Exception {
		// Set up sockets for testing
		DatagramPacket sentPacket = new DatagramPacket(new byte[1024], 1024);
		CyclicBarrier barrier = new CyclicBarrier(2);
		doAnswer(waitIndefinitely()).when(mockReceiveSocket).receive(any(DatagramPacket.class));
		doAnswer(waitIndefinitely()).when(mockReceiveSocket2).receive(any(DatagramPacket.class));
		doAnswer(transmitAnswer(sentPacket, barrier)).when(mockTransmitSocket).send(any(DatagramPacket.class));
		
		// Setup Item config
		bindingProvider.processBindingConfiguration(CONTEXT, item, itemConfig);
		binding.addBindingProvider(bindingProvider);
		
		// Activate the binding ready for the test
		binding.activateForTesting();		
		
		// Send the  command
		binding.internalReceiveCommand(item.getName() ,command);

		// Wait till the socket has sent the command
		barrier.await(1000, TimeUnit.MILLISECONDS);
		return sentPacket;
	}
	
	private void verifyCommandSent(String expectedCommand, DatagramPacket sentPacket) throws Exception {
		assertEquals(expectedCommand, getReceivedStringFromPacket(sentPacket));
		verify(mockTransmitSocket, times(1)).send(any(DatagramPacket.class));
		verifyNoMoreInteractions(mockTransmitSocket);
	}
	
	private Answer<DatagramPacket> waitIndefinitely(){
		return new Answer<DatagramPacket>() {
			public DatagramPacket answer(InvocationOnMock invocation) throws InterruptedException {
				// Wait on a latch that won't be decremented to wait indefinitely.
				new CountDownLatch(1).await();
				return null;
			}
		};
	}
	
	private Answer<DatagramPacket> sendMessageOnceUnlatched(final String data, final CyclicBarrier latch){
		return new Answer<DatagramPacket>() {
			public DatagramPacket answer(InvocationOnMock invocation) throws InterruptedException, BrokenBarrierException {
				Object[] args = invocation.getArguments();
				((DatagramPacket)args[0]).setData(data.getBytes());
				latch.await();
				latch.reset();
				return null;
			}
		};
	}
	
	private Answer<DatagramPacket> transmitAnswer(final DatagramPacket packet, final CyclicBarrier latch){
		return new Answer<DatagramPacket>() {
			public DatagramPacket answer(InvocationOnMock invocation) throws InterruptedException, BrokenBarrierException {
				Object[] args = invocation.getArguments();
				byte[] sentData = ((DatagramPacket)args[0]).getData();
				String sentAsString = new String(sentData, 0, sentData.length);
				packet.setData(sentAsString.getBytes());
				latch.await();
				return null;
			}
		};
	}	
	
	private Answer<String> unlatchWhenEventReceivedOrTimeout(final CountDownLatch latch){
		return new Answer<String>() {
			public String answer(InvocationOnMock invocation) throws InterruptedException, BrokenBarrierException {
				latch.countDown();
				return null;
			}
		};
	}
	
	private String getReceivedStringFromPacket(DatagramPacket packet){
		return new String(packet.getData(), 0, packet.getData().length);
	}
	
	private final class ItemConfigAndExpectedState {
		private final Item item;
		private final String itemConfig;
		private final State expectedState;
		
		public ItemConfigAndExpectedState(Item item, String itemConfig, State expectedState){
			this.item = item;
			this.itemConfig = itemConfig;
			this.expectedState = expectedState;
		}
		public Item getItem() { return item; }
		public String getItemConfig() { return itemConfig; }
		public State getExpectedState() { return expectedState; }

	}
}
