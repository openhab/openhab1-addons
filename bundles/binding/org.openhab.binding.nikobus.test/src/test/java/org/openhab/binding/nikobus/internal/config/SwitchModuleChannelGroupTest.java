/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.config;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

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
import org.openhab.binding.nikobus.internal.util.CommandCache;
import org.openhab.core.library.types.OnOffType;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
@RunWith(MockitoJUnitRunner.class)
public class SwitchModuleChannelGroupTest {

	@Mock
	private NikobusBinding binding;

	@Mock
	private CommandCache cache;

	@Captor
	ArgumentCaptor<NikobusCommand> command;

	SwitchModuleChannelGroup group1;
	SwitchModuleChannelGroup group2;

	@Before
	public void setup() throws URISyntaxException {

		Mockito.when(binding.getCache()).thenReturn(cache);

		Mockito.when(cache.get("$666")).thenReturn("999");
		Mockito.when(cache.get("$10126C946CE5")).thenReturn("A0");
		Mockito.when(cache.get("$10176C948715")).thenReturn("BB");
		Mockito.when(cache.get("$1E156C94000000FF0000FF60E1")).thenReturn("49");
		Mockito.when(cache.get("$1E166C940000000000FFFF9972")).thenReturn("95");

		group1 = new SwitchModuleChannelGroup("6C94", 1);
		group2 = new SwitchModuleChannelGroup("6C94", 2);

	}

	@Test
	public void canRequestGroup1Status() throws Exception {

		NikobusCommand cmd = group1.getStatusRequestCommand(binding);
		assertEquals("$10126C946CE5A0", cmd.getCommand());
		assertEquals("$1C6C94", cmd.getAck());

	}

	@Test
	public void canRequestGroup2Status() throws Exception {

		NikobusCommand cmd = group2.getStatusRequestCommand(binding);
		assertEquals("$10176C948715BB", cmd.getCommand());
		assertEquals("$1C6C94", cmd.getAck());
	}

	@Test
	public void canSendGroup1Update() throws Exception {

		SwitchModuleChannel item = group1.addChannel("test4", 4);
		item.setState(OnOffType.ON);
		Mockito.when(cache.get(Mockito.anyString())).thenReturn("49");

		group1.publishStateToNikobus(item, binding);

		Mockito.verify(binding, Mockito.times(1)).getCache();
		Mockito.verify(binding, Mockito.times(1)).sendCommand(
				command.capture());

		NikobusCommand cmd = command.getAllValues().get(0);
		assertEquals("$1E156C94000000FF0000FF60E149", cmd.getCommand());
	}

	@Test
	public void canSendGroup2Update() throws Exception {

		SwitchModuleChannel item2 = group2.addChannel("test12", 12);
		item2.setState(OnOffType.ON);
		Mockito.when(cache.get(Mockito.anyString())).thenReturn("95");

		group2.publishStateToNikobus(item2, binding);

		Mockito.verify(binding, Mockito.times(1)).getCache();
		Mockito.verify(binding, Mockito.times(1)).sendCommand(
				command.capture());

		NikobusCommand cmd = command.getAllValues().get(0);
		assertEquals("$1E166C940000000000FFFF997295", cmd.getCommand());
	}

	@Test
	public void canProcessGroup1StatusUpdate() {

		SwitchModuleChannel item = group1.addChannel("test5", 5);
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

		SwitchModuleChannel item = group2.addChannel("test11", 11);
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

}
