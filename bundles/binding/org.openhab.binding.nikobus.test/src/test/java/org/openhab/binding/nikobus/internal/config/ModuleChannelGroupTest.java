/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.config;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.openhab.binding.nikobus.internal.core.NikobusCommand;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ModuleChannelGroupTest {

	@Mock
	private NikobusBinding binding;

	@Captor
	ArgumentCaptor<NikobusCommand> command;

	ModuleChannelGroup group1;
	ModuleChannelGroup group2;
	ModuleChannelGroup group3;

	@Before
	public void setup() throws URISyntaxException {

		group1 = new ModuleChannelGroup("6C94", 1);
		group2 = new ModuleChannelGroup("6C94", 2);
		group3 = new ModuleChannelGroup("5FCB", 1);

	}

	@Test
	public void canRequestGroup1Status() throws Exception {

		NikobusCommand cmd = group1.getStatusRequestCommand();
		assertEquals("$10126C946CE5A0", cmd.getCommand());
		assertEquals("$1C6C94", cmd.getAck());

	}

	@Test
	public void canRequestGroup2Status() throws Exception {

		NikobusCommand cmd = group2.getStatusRequestCommand();
		assertEquals("$10176C948715BB", cmd.getCommand());
		assertEquals("$1C6C94", cmd.getAck());
	}

	@Test
	public void canSendGroup1Update() throws Exception {

		ModuleChannel item = group1.addChannel("test4", 4, new ArrayList<Class<? extends Command>>());
		item.setState(OnOffType.ON);

		group1.publishStateToNikobus(item, binding);

		Mockito.verify(binding, Mockito.times(1)).sendCommand(
				command.capture());

		NikobusCommand cmd = command.getAllValues().get(0);
		assertEquals("$1E156C94000000FF0000FF60E149", cmd.getCommand());
	}
	
	@Test
	public void canSendGroup1DimmerUpdate() throws Exception {

		ModuleChannel item = group1.addChannel("test4", 4, new ArrayList<Class<? extends Command>>());
		item.setState(new PercentType(25));

		group1.publishStateToNikobus(item, binding);

		Mockito.verify(binding, Mockito.times(1)).sendCommand(
				command.capture());

		NikobusCommand cmd = command.getAllValues().get(0);
		assertEquals("$1E156C94000000400000FF45DE7B", cmd.getCommand());
	}

	@Test
	public void canSendGroup2Update() throws Exception {

		ModuleChannel item2 = group2.addChannel("test12", 12, new ArrayList<Class<? extends Command>>());
		item2.setState(OnOffType.ON);

		group2.publishStateToNikobus(item2, binding);

		Mockito.verify(binding, Mockito.times(1)).sendCommand(
				command.capture());

		NikobusCommand cmd = command.getAllValues().get(0);
		assertEquals("$1E166C940000000000FFFF997295", cmd.getCommand());
	}

	@Test
	public void canProcessGroup1StatusUpdate() {

		ModuleChannel item = group1.addChannel("test5", 5, new ArrayList<Class<? extends Command>>());
		item.setState(OnOffType.OFF);

		group1.processNikobusCommand(new NikobusCommand("$0512"), binding);

		group1.processNikobusCommand(new NikobusCommand(
				"$1C6C940000000000FF00557CF8"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test5",
				OnOffType.ON);

		group1.processNikobusCommand(new NikobusCommand("$0512"), binding);
		group1.processNikobusCommand(new NikobusCommand(
				"$1C6C9400000000FF00FF557CF8"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test5",
				OnOffType.OFF);
	}

	@Test
	public void canProcessGroup2StatusUpdate() {

		ModuleChannel item = group2.addChannel("test11", 11, new ArrayList<Class<? extends Command>>());
		item.setState(OnOffType.OFF);

		group2.processNikobusCommand(new NikobusCommand("$0517"), binding);
		group2.processNikobusCommand(new NikobusCommand(
				"$1C6C940000000000FF00557CF8"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test11",
				OnOffType.ON);

		group2.processNikobusCommand(new NikobusCommand("$0517"), binding);
		group2.processNikobusCommand(new NikobusCommand(
				"$1C6C9400000000FF00FF557CF8"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test11",
				OnOffType.OFF);

	}

	@Test
	public void canProcessGroup1StatusUpdateDimmer() {
		
		List<Class<? extends Command>> acceptedCommands = new ArrayList<Class<? extends Command>>();
		acceptedCommands.add(PercentType.class);
		ModuleChannel item = group3.addChannel("test2", 1, acceptedCommands);
		item.setState(OnOffType.OFF);

		group3.processNikobusCommand(new NikobusCommand("$0512"), binding);
		group3.processNikobusCommand(new NikobusCommand(
				"$1C5FCB03400000000000E36D38"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test2",
				PercentType.valueOf("26"));

		group3.processNikobusCommand(new NikobusCommand("$0512"), binding);
		group3.processNikobusCommand(new NikobusCommand(
				"$1C5FCB037F000000000009E2C0"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test2",
				PercentType.valueOf("50"));
		
		group3.processNikobusCommand(new NikobusCommand("$0512"), binding);
		group3.processNikobusCommand(new NikobusCommand(
				"$1C5FCB03D90000000000652B76"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test2",
				PercentType.valueOf("86"));
		
		group3.processNikobusCommand(new NikobusCommand("$0512"), binding);
		group3.processNikobusCommand(new NikobusCommand(
				"$1C5FCB03A70000000000A0143B"), binding);
		Mockito.verify(binding, Mockito.times(1)).postUpdate("test2",
				PercentType.valueOf("66"));
		
	}
}
