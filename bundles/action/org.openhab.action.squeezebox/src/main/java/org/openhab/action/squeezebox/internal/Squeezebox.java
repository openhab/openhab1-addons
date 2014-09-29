/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.squeezebox.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.squeezeserver.SqueezePlayer;
import org.openhab.io.squeezeserver.SqueezePlayer.Mode;
import org.openhab.io.squeezeserver.SqueezeServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the methods that are made available in scripts and rules
 * for Squeezebox integration.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class Squeezebox {

	private static final Logger logger = 
		LoggerFactory.getLogger(Squeezebox.class);

	// handle to the Squeeze Server connection
	public static SqueezeServer squeezeServer;

	private final static String GOOGLE_TRANSLATE_URL = "http://translate.google.com/translate_tts?tl=%s&q=";
	private final static int MAX_SENTENCE_LENGTH = 100;

	@ActionDoc(text = "Turn one of your Squeezebox devices on/off", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxPower(
			@ParamDoc(name = "playerId", text = "The Squeezebox to turn on/off") String playerId,
			@ParamDoc(name = "power", text = "True to turn on, False to turn off") boolean power) {		
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;
		
		if (power) {
			squeezeServer.powerOn(playerId);
		} else {
			squeezeServer.powerOff(playerId);
		}
		return true;
	}
	
	@ActionDoc(text = "Mute/unmute one of your Squeezebox devices", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxMute(
			@ParamDoc(name = "playerId", text = "The Squeezebox to turn on/off") String playerId,
			@ParamDoc(name = "mute", text = "True to mute, False to un-mute") boolean mute) {		
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;

		if (mute) {
			squeezeServer.mute(playerId);
		} else {
			squeezeServer.unMute(playerId);
		}
		return true;
	}
	
	@ActionDoc(text = "Set the volume on one of your Squeezebox devices", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxVolume(
			@ParamDoc(name = "playerId", text = "The Squeezebox to turn on/off") String playerId,
			@ParamDoc(name = "volume", text = "The volume between 0-100") int volume) {		
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;

		squeezeServer.setVolume(playerId, volume);
		return true;
	}
	
	@ActionDoc(text = "Send the 'play' command to one of your Squeezebox devices", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxPlay(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the command to") String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;
		
		squeezeServer.play(playerId);
		return true;
	}
	
	@ActionDoc(text = "Send the 'pause' command to one of your Squeezebox devices", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxPause(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the command to") String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;

		squeezeServer.pause(playerId);
		return true;
	}
	
	@ActionDoc(text = "Send the 'stop' command to one of your Squeezebox devices", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxStop(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the command to") String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;

		squeezeServer.stop(playerId);
		return true;
	}
	
	@ActionDoc(text = "Send the 'next' command to one of your Squeezebox devices", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxNext(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the command to") String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;

		squeezeServer.next(playerId);
		return true;
	}
	
	@ActionDoc(text = "Send the 'prev' command to one of your Squeezebox devices", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxPrev(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the command to") String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;

		squeezeServer.prev(playerId);
		return true;
	}
	
	@ActionDoc(text = "Play a URL on one of your Squeezebox devices using the current volume for that device", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxPlayUrl(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the URL to") String playerId,
			@ParamDoc(name = "url", text = "The URL to play (if empty will clear the playlist)") String url) {
		return squeezeboxPlayUrl(playerId, url, -1);
	}	
	
	@ActionDoc(text = "Play a URL on one of your Squeezebox devices using the specified volume", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxPlayUrl(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the URL to") String playerId,
			@ParamDoc(name = "url", text = "The URL to play (if empty will clear the playlist)") String url,
			@ParamDoc(name = "volume", text = "The volume to set the device when playing this URL (between 1-100)") int volume) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;

		// set the player ready to play this URL
   		if (volume != -1) {
			logger.trace("Setting player state: volume {}", volume);
			squeezeServer.setVolume(playerId, volume);
		}

		// play the url
   		if (StringUtils.isEmpty(url)) {
   			squeezeServer.clearPlaylist(playerId);
   		} else {
   			squeezeServer.playUrl(playerId, url);
   		}
		return true;
	}
	
	@ActionDoc(text = "Speak a message via one of your Squeezebox devices using the current volume for that device", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxSpeak(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the message to") String playerId,
			@ParamDoc(name = "message", text = "The message to say") String message) {
		return squeezeboxSpeak(playerId, message, -1, true);
	}	
	@ActionDoc(text = "Speak a message via one of your Squeezebox devices using the specified volume and always resume previous playback", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxSpeak(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the message to") String playerId,
			@ParamDoc(name = "message", text = "The message to say") String message,
			@ParamDoc(name = "volume", text = "The volume to set the device when speaking this message (between 1-100)") int volume) {
		return squeezeboxSpeak(playerId, message, -1, true);
	}	
	
	@ActionDoc(text = "Speak a message via one of your Squeezebox devices using the specified volume and using specified resume status", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean squeezeboxSpeak(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the message to") String playerId,
			@ParamDoc(name = "message", text = "The message to say") String message,
			@ParamDoc(name = "volume", text = "The volume to set the device when speaking this message (between 1-100)") int volume,
			@ParamDoc(name = "resumePlayback", text = "Continue playback after speech") Boolean resumePlayback ){
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return false;
		logger.trace("***START SPEECH**** Player: '{}'", playerId);
		// get the current player state
		int playerVolume = player.getUnmuteVolume();
		boolean playerPowered = player.isPowered();
		boolean playerMuted = player.isMuted();
		Mode playerMode = player.getMode();
		int currNumTracks = player.getNumberPlaylistTracks();
		int currPlayingTime = player.getCurrentPlayingTime();
		int currPlaylistIndex = player.getCurrentPlaylistIndex();
		int currPlaylistShuffle = player.getCurrentPlaylistShuffle();
		int currPlaylistRepeat = player.getCurrentPlaylistRepeat();
		int newNumTracks = 0;
		logger.trace("Current Playing Mode '{}'", playerMode.toString());
		logger.trace("Current Volume '{}'", playerVolume);
		logger.trace("Current Num Playlist Tracks '{}'", currNumTracks);
		logger.trace("Current Playing Playlist Index '{}'", currPlaylistIndex);
		logger.trace("Current Playing Time '{}'", currPlayingTime);
		logger.trace("Current Shuffle Mode '{}'", currPlaylistShuffle);
		logger.trace("Current Repeat Mode '{}'", currPlaylistRepeat);
		
		// If Playing Pause player before adjusting volume!
		if (playerMode == Mode.play)
			squeezeServer.pause(playerId);
		
		// set the player ready to play this announcement
		if (playerMuted) {
			logger.trace("Setting player state: unmuted");
			squeezeServer.unMute(playerId);
		}
		if (volume != -1) {
			logger.trace("Setting player state: volume {}", volume);
			squeezeServer.setVolume(playerId, volume);
		}
		
		if (currPlaylistRepeat != 0)
			squeezeServer.setRepeatMode(playerId, 0);
		if (currPlaylistShuffle != 0) {
			squeezeServer.setShuffleMode(playerId, 0);
			currPlaylistIndex = 0;
			logger.trace("Shuffle Changed! Set Current Playing Index to 0");
		}
		
		
		// can only 'say' 100 chars at a time
		List<String> sentences = getSentences(message, MAX_SENTENCE_LENGTH);

		// send each sentence in turn
		for (String sentence : sentences) {
			logger.trace("Sending sentence to " + playerId + " (" + sentence + ")");
			
			String encodedSentence;
			try {
				encodedSentence = URLEncoder.encode(sentence, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.warn("Failed to encode sentence '" + sentence + "'. Skipping sentence.", e);
				continue;
			}
			encodedSentence = encodedSentence.replace("+", "%20");
			logger.trace("Encoded sentence " + encodedSentence);
			
			// build the URL to send to the Squeezebox to play
			String url = String.format(GOOGLE_TRANSLATE_URL,squeezeServer.language()) + encodedSentence;
			
			// create an instance of our special listener so we can detect when the sentence is complete
			SqueezeboxSentenceListener listener = new SqueezeboxSentenceListener(playerId);
			squeezeServer.addPlayerEventListener(listener);
			
			// send the URL (this will power up the player and un-mute if necessary)
			logger.trace("Adding URL to current playlist '{}' to play", url);
			squeezeServer.addPlaylistItem(playerId, url);
			logger.trace("Sleeping for 1s for updated playlist to refresh", url);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				continue;
			}
			newNumTracks = player.getNumberPlaylistTracks();
			logger.trace("New Playlist Track Number: '{}'", newNumTracks);
			
			squeezeServer.playPlaylistItem(playerId, newNumTracks-1);
			
			squeezeServer.play(playerId);
			
			// wait for this message to complete (timing out after 30s)
			int timeoutCount = 0;
			while (!listener.isFinished() && timeoutCount < 300) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
				timeoutCount++;
			}
			
			if (timeoutCount >= 200) {
				logger.warn("Sentence timed out while speaking!");
			}
			squeezeServer.stop(playerId);
			// clean up the listener
			squeezeServer.removePlayerEventListener(listener);
			listener = null;
			logger.trace("Done playing speech - restore state...");
		}
		
		
		logger.trace("Deleting Playlist Index: '{}'", newNumTracks-1);
		squeezeServer.deletePlaylistItem(playerId, newNumTracks-1);

		// restore the player volume before playback
		if (volume != -1) {
			logger.trace("Restoring player to previous state: volume {}", playerVolume);
			squeezeServer.setVolume(playerId, playerVolume);
		}
		if (playerMode != Mode.stop) {
			logger.trace("Restoring Playlist Index Number: '{}'", currPlaylistIndex);
			squeezeServer.playPlaylistItem(playerId, currPlaylistIndex);
			logger.trace("Restoring Playing Time : '{}'", currPlayingTime);
			squeezeServer.setPlayingTime(playerId, currPlayingTime);
		}
		// Must sleep 350ms before restoring previous playback state...
		try {
			Thread.sleep(350);
		} catch (InterruptedException e) { }
	
		// restore play mode state
		if (playerMode == Mode.play) {
			if (resumePlayback) {
				logger.trace("Restoring Playing Mode: '{}'", playerMode);
				squeezeServer.play(playerId);
			}
			else {
				logger.warn("NOT restoring Playing Mode: '{}' because resumePlayback is false", playerMode);
				squeezeServer.pause(playerId);
			}
		} else if (playerMode == Mode.pause) {
			squeezeServer.pause(playerId);
		} else {
			squeezeServer.stop(playerId);
		}
		
		logger.trace("Restoring player to previous state: shuffle {}", currPlaylistShuffle);
		squeezeServer.setShuffleMode(playerId, currPlaylistShuffle);
		logger.trace("Restoring player to previous state: repeat {}", currPlaylistRepeat);
		squeezeServer.setRepeatMode(playerId, currPlaylistRepeat);

		if (playerMuted) {
			logger.trace("Restoring player to previous state: muted");
			squeezeServer.mute(playerId);
		}
		if (!playerPowered) {
			logger.trace("Restoring player to previous state: off");
			squeezeServer.powerOff(playerId);
		}
		logger.trace("*****DONE SPEECH****** Player: '{}'", playerId);
		return true;
	}
	
	private static SqueezePlayer getPlayer(String playerId) {
		if (StringUtils.isEmpty(playerId))
			throw new NullArgumentException("playerId");

		// check the Squeeze Server has been initialised
		if (squeezeServer == null) {
			logger.warn("Squeeze Server yet to be initialised. Ignoring action.");
			return null;
		}
		
		// check we are connected to the Squeeze Server
		if (!squeezeServer.isConnected()) {
			logger.warn("Not connected to the Squeeze Server. Please check your config and consult the openHAB WIKI for instructions on how to configure. Ignoring action.");
			return null;
		}

		SqueezePlayer player = squeezeServer.getPlayer(playerId);
		if (player == null) {
			logger.warn("No Squeezebox player exists with name '{}'. Ignoring action.", playerId);
			return null;
		}
		
		return player;
	}

    private static List<String> getSentences(String message, int maxSentenceLength) {
        List<String> sentences = new ArrayList<String>();

        if (StringUtils.isEmpty(message))
            return sentences;

        if (message.length() <= maxSentenceLength) {
            sentences.add(message.trim());
            return sentences;
        }

        String current = "";

        for (String sentence : StringUtils.split(message, '.')) {
            sentence = sentence.trim();

            if (sentence.length() == 0) {
                continue;
            }

            // if this sentence is too long then split up
            if (sentence.length() > maxSentenceLength) {
                if (current.length() > 0) {
                    sentences.add(current.trim());
                    current = "";
                }

                // split this long sentence up and add each part
                sentences.addAll(splitSentence(sentence, maxSentenceLength));
            } else {
                if (current.length() + sentence.length() + 2 > maxSentenceLength) {
                    sentences.add(current.trim());
                    current = "";
                }

                // add this sentence to the current phrase
                current += sentence + ". ";
            }
        }

        // add the final sentence
        if (current.length() > 0)
            sentences.add(current.trim());

        return sentences;
    }

    private static List<String> splitSentence(String sentence, int maxSentenceLength) {
        List<String> parts = new ArrayList<String>();

        if (StringUtils.isEmpty(sentence))
            return parts;

        if (sentence.length() <= maxSentenceLength) {
            parts.add(sentence.trim());
            return parts;
        }

        String current = "";

        for (String word : StringUtils.split(sentence, ' ')) {
            word = word.trim();

            if (word.length() == 0) {
                continue;
            }

            // check this word isn't too long by itself
            if (word.length() > maxSentenceLength) {
                logger.warn("Unable to say '{}' as this word is longer than the maximum sentence allowed ({})", word, maxSentenceLength);
                continue;
            }

            // if this word makes our sentence too long start a new sentence
            if (current.length() + word.length() > maxSentenceLength) {
                parts.add(current.trim());
                current = "";
            }

            // add this word to the current sentence
            current += word + " ";
        }

        // add the final sentence
        if (current.length() > 0)
            parts.add(current.trim());

        return parts;
    }
}
