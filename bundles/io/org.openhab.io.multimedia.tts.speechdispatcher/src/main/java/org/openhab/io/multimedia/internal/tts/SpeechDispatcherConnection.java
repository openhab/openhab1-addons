package org.openhab.io.multimedia.internal.tts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import speechd.ssip.SSIPClient;
import speechd.ssip.SSIPException;
import speechd.ssip.SSIPPriority;

/**
 * This class open a TCP/IP connection to the Speech Dispatcher service
 * and can send messages to be announced
 * @author Gaël L'hopital
 * @since 1.6.0
 */

public class SpeechDispatcherConnection {

	private static Logger logger = LoggerFactory.getLogger(SpeechDispatcherConnection.class);

	public String port;
	public String host;

	private static SSIPClient speechDispatcherClient =  null;
		
	public SpeechDispatcherConnection() {
	}

	public void openConnection() {
		try {
			speechDispatcherClient = new SSIPClient("openhab", null, null, host, port);
		} catch (SSIPException e) {
			logger.error("Error connecting to SpeechDispatcher Host {}: {}",host,e.getLocalizedMessage());
		}
	}

	public void closeConnection() {
		if (speechDispatcherClient != null) {
			try {
				speechDispatcherClient.close();
				speechDispatcherClient =null;
			} catch (SSIPException e) {
				logger.error("Error closing connection to SpeechDispatcher Host {}: {}",host,e.getLocalizedMessage());
			}		
		}
	}
	
	/**
	 * Speaks the text with a given voice
	 * 
	 * @param text the text to speak
	 * @param voice the name of the voice to use or null, if the default voice should be used
	 */
	public void say(String text, String voiceName) {
		if (speechDispatcherClient==null) {
			openConnection();
		}
		if(text==null) {
			return;
		}
		try {
			if (voiceName != null) {
				speechDispatcherClient.setVoice(voiceName);
			}
			speechDispatcherClient.say(SSIPPriority.MESSAGE, text);
		} catch (SSIPException e) {
			logger.error("Error sending text to SpeechDispatcher Host {}: {}",host,e.getLocalizedMessage());
		}

	}
	
	@Override
	public String toString() {
		return "Device [host=" + host + ", port=" + port + "]";
	}

}
