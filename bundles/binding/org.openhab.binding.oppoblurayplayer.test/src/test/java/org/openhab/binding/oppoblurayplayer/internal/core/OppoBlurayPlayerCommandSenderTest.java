package org.openhab.binding.oppoblurayplayer.internal.core;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openhab.binding.oppoblurayplayer.connector.OppoBlurayPlayerConnector;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerCommand;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerException;

public class OppoBlurayPlayerCommandSenderTest {

	@Mock
	OppoBlurayPlayerConnector serialInterface;
	
	OppoBlurayPlayerCommand command = OppoBlurayPlayerCommand.PLAYBACK_STATUS_PLAYING;
	
	int timeout = 10;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSendCommand() throws InterruptedException, OppoBlurayPlayerException {
		OppoBlurayPlayerCommandSender sender = new OppoBlurayPlayerCommandSender(serialInterface, timeout);
		sender.sendCommand(command);
		Thread t = new Thread(sender);
		t.start();
		Thread.sleep(5000);
		verify(serialInterface).sendMessage(OppoBlurayPlayerCommand.PLAYBACK_STATUS_PLAYING.getCommandString(), timeout);
		sender.stop();
		Thread.sleep(1000);
	}

}
