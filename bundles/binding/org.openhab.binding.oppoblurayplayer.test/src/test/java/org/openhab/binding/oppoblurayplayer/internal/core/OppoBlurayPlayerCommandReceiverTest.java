package org.openhab.binding.oppoblurayplayer.internal.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openhab.binding.oppoblurayplayer.OppoBlurayPlayerBindingProvider;
import org.openhab.binding.oppoblurayplayer.connector.OppoBlurayPlayerConnector;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerBinding;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerCommand;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerException;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;

public class OppoBlurayPlayerCommandReceiverTest {

	@Mock
	OppoBlurayPlayerConnector serialInterface;
	
	@Mock
	OppoBlurayPlayerBinding binding = new OppoBlurayPlayerBinding();
	
	@Mock
	EventPublisher eventPublisher;
	
	String message = "@UPL DISC";
	String playerName = "hometheatre_oppoblurayplayer";
	
	int timeout = 10;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(binding.findFirstMatchingItemForCommand(playerName, OppoBlurayPlayerCommand.PLAYBACK_STATUS_NO_DISC)).thenReturn(playerName);
	}

	@Test
	public void testReceiveMessage() throws InterruptedException, OppoBlurayPlayerException {
		OppoBlurayPlayerCommandReceiver receiver = new OppoBlurayPlayerCommandReceiver(binding, playerName);
		serialInterface.registerCommandReceiver(receiver);
		Thread t = new Thread(receiver);
		t.start();
		receiver.receivedCommand(message);
		Thread.sleep(1000);
		verify(binding).postUpdate(playerName, StringType.valueOf("PLAYBACK_STATUS_NO_DISC"));
		receiver.stop();
		Thread.sleep(1000);
	}

}
