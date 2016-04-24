package org.openhab.binding.oppoblurayplayer.internal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openhab.core.library.types.OnOffType;

public class OppoBlurayPlayerCommandTest {

	@Test
	public void testFindMatchingCommandFromResponse() throws OppoBlurayPlayerException {
		OppoBlurayPlayerCommand command = OppoBlurayPlayerCommand.findMatchingCommandFromResponse("@UPL PLAY");
		assertEquals("PLAY Command", OppoBlurayPlayerCommand.PLAYBACK_STATUS_PLAYING, command);
	}

	@Test
	public void testGetCommandString() {
		assertEquals("PLAY Command", "#PLA", OppoBlurayPlayerCommand.PLAYBACK_STATUS_PLAYING.getCommandString());
	}
	
	@Test
	public void testFindMessageForCommand() throws OppoBlurayPlayerException{
		OppoBlurayPlayerCommand commandOff = OppoBlurayPlayerCommand.findMessageForCommand(OppoBlurayPlayerCommandType.POWER, OnOffType.OFF);
		assertEquals("Message: ", OppoBlurayPlayerCommand.POWER_OFF, commandOff);
		assertEquals("State: ", OnOffType.OFF, commandOff.getState());
		OppoBlurayPlayerCommand commandOn = OppoBlurayPlayerCommand.findMessageForCommand(OppoBlurayPlayerCommandType.POWER, OnOffType.ON);
		assertEquals("Message: ", OppoBlurayPlayerCommand.POWER_ON, commandOn);
		assertEquals("State: ", OnOffType.ON, commandOn.getState());
	}
}
