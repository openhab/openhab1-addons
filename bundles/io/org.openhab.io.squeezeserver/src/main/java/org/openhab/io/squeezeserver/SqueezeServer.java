/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.squeezeserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.io.squeezeserver.SqueezePlayer.Mode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Handles connection and event handling for all Squeezebox devices.
 *
 * @author Markus Wolters
 * @author Ben Jones
 * @since 1.4.0
 */
public class SqueezeServer implements ManagedService {
	// TODO: should probably add some sort of watchdog timer to check the 'listener' thread
	//		 periodically so we can re-connect without having to wait for a sendCommand()
	
	private static Logger logger = LoggerFactory.getLogger(SqueezeServer.class);

    // configuration defaults for optional properties
	private static int DEFAULT_CLI_PORT = 9090;
	private static int DEFAULT_WEB_PORT = 9000;

	/// regEx to validate SqueezeServer config <code>'^(squeeze:)(host|cliport|webport)=.+$'</code>
	private final Pattern SERVER_CONFIG_PATTERN = Pattern.compile("^(server)\\.(host|cliport|webport)$");
	
	// regEx to validate a mpdPlayer config <code>'^(.*?)\\.(id)$'</code>
	private final Pattern PLAYER_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(id)$");
	
	private final static String NEW_LINE = System.getProperty("line.separator");
	
	// the value by which the volume is changed by each INCREASE or DECREASE-Event 
	private static final int VOLUME_CHANGE_SIZE = 5;
	
	// connection properties and client socket
    private String host;
    private int cliPort;
    private int webPort;	// unused - needed if we want to bring back artwork via the Squeeze Server web server
    private Socket clientSocket;

    // configured players - keyed by playerId and MAC address
	private Map<String, SqueezePlayer> playersById = new ConcurrentHashMap<String, SqueezePlayer>();
	private Map<String, SqueezePlayer> playersByMacAddress = new ConcurrentHashMap<String, SqueezePlayer>();
    
    // listener thread for processing server messages
    private final SqueezeServerListener listener = new SqueezeServerListener();
    
    public boolean isConnected() {
        return (clientSocket != null && clientSocket.isConnected());
    }

	public List<SqueezePlayer> getPlayers() {
		return new ArrayList<SqueezePlayer>(playersById.values());
	}

	public SqueezePlayer getPlayer(String playerId) {
		if (!playersById.containsKey(playerId)) {
			logger.warn("No player exists for '{}'", playerId);
			return null;
		}
		return playersById.get(playerId);
	}

	public void mute(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		if (player.getVolume() > 0) {
			setVolume(playerId, 0);
		}
	}
	
	public void unMute(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		if (player.getVolume() == 0) {
			setVolume(playerId, player.getUnmuteVolume());
		}
	}
	
	public void powerOn(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " power 1");
	}
	
	public void powerOff(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " power 0");
	}
	
	public void syncPlayer(String playerId1, String playerId2) {
		SqueezePlayer player1 = getPlayer(playerId1);
		SqueezePlayer player2 = getPlayer(playerId2);
		if (player1 == null || player2 == null) return;
		sendCommand(player1.getMacAddress() + " sync " + player2.getMacAddress());
	}
	
	public void unSyncPlayer(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " sync -");
	}
	
	public void play(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " play");
	}
	
	public void playUrl(String playerId, String url) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " playlist play " + url);
	}
	
	public void pause(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " pause 1");
	}

	public void unPause(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " pause 0");
	}

	public void stop(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " stop");
	}
	
	public void prev(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " playlist index -1");
	}
	
	public void next(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " playlist index +1");
	}
	
	public void clearPlaylist(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " playlist clear");
	}
	
	public void volumeUp(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		setVolume(playerId, player.getVolume() + VOLUME_CHANGE_SIZE);
	}

	public void volumeDown(String playerId) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		setVolume(playerId, player.getVolume() - VOLUME_CHANGE_SIZE);
	}
	
	public void setVolume(String playerId, int volume) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		if (0 > volume) {
			volume = 0;
		} else if (volume > 100) {
			volume = 100;
		}
		sendCommand(player.getMacAddress() + " mixer volume " + String.valueOf(volume));
	}
			
	public void showString(String playerId, String line) {
		showString(playerId, line, 5);
	}
	
	public void showString(String playerId, String line, int duration) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " show line1:" + line + " duration:" + String.valueOf(duration));
	}
	
	public void showStringHuge(String playerId, String line) {
		showStringHuge(playerId, line, 5);
	}
	
	public void showStringHuge(String playerId, String line, int duration) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " show line1:" + line + " font:huge duration:" + String.valueOf(duration));
	}
	
	public void showStrings(String playerId, String line1, String line2) {
		showStrings(playerId, line1, line2, 5);
	}
	
	public void showStrings(String playerId, String line1, String line2, int duration) {
		SqueezePlayer player = getPlayer(playerId);
		if (player == null) return;
		sendCommand(player.getMacAddress() + " show line1:" + line1 + " line2:" + line2 + " duration:" + String.valueOf(duration));
	}
	
	/**
	 * Send a command to the Squeeze Server.
	 */
	public void sendCommand(String command) {
		if (!isConnected()) {
			logger.debug("No connection to SqueezeServer, will attempt to reconnect now...");
			connect();
			if (!isConnected()) {
				logger.error("Failed to re-connect to SqueezeServer, unable to send command {}", command);
				return;
			}
		}
		logger.debug("Sending command: {}", command);
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			writer.write(command + NEW_LINE);
			writer.flush();
		} catch (IOException e) {
			logger.error("Error while sending command to Squeeze Server (" + command + ")", e);
		}
	}
	
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		// disconnect first in case the config has been changed for an existing instance
        disconnect();
        
        host = null;
        cliPort = DEFAULT_CLI_PORT;
        webPort = DEFAULT_WEB_PORT;

        playersById.clear();
        playersByMacAddress.clear();
        
		if (config == null || config.isEmpty()) {
			logger.warn("Empty or null configuration. Ignoring.");            	
			return;
		}
        
        Enumeration<String> keys = config.keys();
        while (keys.hasMoreElements()) {
            
            String key = (String) keys.nextElement();
            
            // the config-key enumeration contains additional keys that we
            // don't want to process here ...
            if ("service.pid".equals(key)) {
                continue;
            }
            
            Matcher serverMatcher = SERVER_CONFIG_PATTERN.matcher(key);
            Matcher playerMatcher = PLAYER_CONFIG_PATTERN.matcher(key);
            
            String value = (String) config.get(key);
            
            if (serverMatcher.matches()) {	
            	String serverConfig = serverMatcher.group(2);
                if (serverConfig.equals("host")) {
                    host = value;
                }
                else if (serverConfig.equals("cliport")) {
                    cliPort = Integer.valueOf(value);
                }
                else if (serverConfig.equals("webport")) {
                    webPort = Integer.valueOf(value);
                }
            } else if (playerMatcher.matches()) {
            	String playerId = playerMatcher.group(1);
            	String macAddress = value;
            	SqueezePlayer player = new SqueezePlayer(playerId, macAddress);
                
            	playersById.put(playerId, player);
                playersByMacAddress.put(macAddress, player);
            } else {
    			logger.warn("Unexpected or unsupported configuration: " + key + ". Ignoring.");            	
            }
        }
        
        if (StringUtils.isEmpty(host))
            throw new ConfigurationException("host", "No Squeeze Server host specified - this property is mandatory");
        if (playersById.size() == 0)
            throw new ConfigurationException("host", "No Squeezebox players specified - there must be at least one player");
            
        // attempt to connect using our new config
        connect();
	}

    private void connect() {
		try {
			clientSocket = new Socket(host, cliPort);
	    	listener.start();
			logger.info("Squeeze Server connection established.");
		} catch (IOException e) {
			logger.error("Failed to connect to SqueezeServer at " + host + ":" + cliPort, e);
		}        
    }

    private void disconnect() {
		if (isConnected()) {
			try {
		  		listener.setInterrupted(true);
				clientSocket.close(); 
				logger.debug("Squeeze Server connection stopped.");
			} catch (IOException e) {
				logger.error("Failed to disconnect from SqueezeServer at " + host + ":" + cliPort, e);
			} finally {
				clientSocket = null;
			}
		}
    }
        
	/**
	 * Start service.
	 */
	public void activate() {
		logger.debug("Starting Squeeze Server connection...");
	}

	/**
	 * Stop service.
	 */
	public void deactivate() {
		logger.debug("Stopping Squeeze Server connection...");
        disconnect();
	}
	
	private class SqueezeServerListener extends Thread {
		private boolean interrupted = false;
		
		public SqueezeServerListener() {
			super("Squeeze Server Listener");
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		@Override
		public void run() {
			setInterrupted(false);
			
			BufferedReader reader = null;			
			try {
				reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				sendCommand("players 0");
				sendCommand("listen 1");
				
				String message;
				while (!interrupted && (message= reader.readLine()) != null) {
					logger.debug("Message received: {}", message);

					if (message.startsWith("listen 1"))
						continue;
					
					if (message.startsWith("players 0")) {
						handlePlayersList(message);
					} else {
						handlePlayerUpdate(message);
					}
				}
			} catch (IOException e) {
				logger.error("Error while reading from Squeeze Server", e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error("Error trying to close buffered reader, ignoring.");
					}
					reader = null;
				}
			}
		}

		private String decode(String raw) {
			try {
				return URLDecoder.decode(raw, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("Failed to decode '" + raw + "'", e);
				return null;
			}
		}
		
		private void handlePlayersList(String message) {
			String[] playersList = decode(message).split("playerindex:\\d+\\s");
			for (String playerParams : playersList) {
				String[] parameterList = playerParams.split("\\s");

				// parse out the MAC address first
				String macAddress = null;
				for (String parameter : parameterList) {
					if (parameter.contains("playerid")) {
						macAddress = parameter.substring(parameter.indexOf(":") + 1);
						break;
					}
				}
				
				// if none found then ignore this set of params
				if (macAddress == null)
					continue;

				// see if this player exists in our config
				SqueezePlayer player = playersByMacAddress.get(macAddress);
				if (player == null)
					continue;
				
				// populate the player state
				for (String parameter : parameterList) {
					if (parameter.contains("ip")) {
						player.setIpAddr(parameter.substring(parameter.indexOf(":") + 1));
					} else if (parameter.contains("uuid")) {
						player.setUuid(parameter.substring(parameter.indexOf(":") + 1));
					} else if (parameter.contains("name")) {
						player.setName(parameter.substring(parameter.indexOf(":") + 1));
					} else if (parameter.contains("model")) {
						player.setModel(parameter.substring(parameter.indexOf(":") + 1));
					}
				}
				
				// tell the server we want to subscribe to player updates
				sendCommand(player.getMacAddress() + " status - 1 subscribe:10 tags:yagJlN");
			}
		}
		
		private void handlePlayerUpdate(String message) {
			String[] messageParts = message.split("\\s");
			if (messageParts.length < 2) {
				logger.warn("Invalid message - expecting at least 2 parts. Ignoring.");
				return;
			}
			
			// get the MAC address 
			SqueezePlayer player = playersByMacAddress.get(decode(messageParts[0]));
			if (player == null) {
				logger.warn("Status message received for MAC address {} which is not configured in openHAB. Ignoring.", messageParts[0]);
				return;
			}

			// get the message type
			String messageType = messageParts[1];
			
			if (messageType.equals("status")) {
				handleStatusMessage(player, messageParts); 
			} else if (messageType.equals("playlist")) {
				handlePlaylistMessage(player, messageParts);
			} else if (messageType.equals("prefset")) {
				handlePrefsetMessage(player, messageParts);
			} else if (messageType.equals("ir")) {
					player.setIrCode(messageParts[2]);
			} else if (messageType.equals("power")) {
				// ignore these for now
				//player.setPowered(messageParts[1].equals("1"));
			} else if (messageType.equals("play") || messageType.equals("pause") || messageType.equals("stop")) {
				// ignore these for now
				//player.setMode(Mode.valueOf(messageType));
			} else if (messageType.equals("mixer") || messageType.equals("menustatus") || messageType.equals("button")) {
				// ignore these for now
			} else {
				logger.debug("Unhandled message type '{}'. Ignoring.", messageType);
			}
		}
		
		private void handleStatusMessage(SqueezePlayer player, String[] messageParts) {
			for (String messagePart : messageParts) {
				// Parameter Power
				if (messagePart.startsWith("power%3A")) {
					String value = messagePart.substring("power%3A".length());
					player.setPowered(value.matches("1"));
				}
				// Parameter Volume
				else if (messagePart.startsWith("mixer%20volume%3A")) {
					String value = messagePart.substring("mixer%20volume%3A".length());
					player.setVolume((int) Double.parseDouble(value));
				}
				// Parameter Mode
				else if (messagePart.startsWith("mode%3A")) {
					String value = messagePart.substring("mode%3A".length());
					player.setMode(Mode.valueOf(value));
				}
				// Parameter Title
				else if (messagePart.startsWith("title%3A")) {
					String value = messagePart.substring("title%3A".length());
					player.setTitle(decode(value));
				}
				// Parameter Remote Title (radio)
				else if (messagePart.startsWith("remote_title%3A")) {
					String value = messagePart.substring("remote_title%3A".length());
					player.setRemoteTitle(decode(value));
				}
				// Parameter Artist
				else if (messagePart.startsWith("artist%3A")) {
					String value = messagePart.substring("artist%3A".length());
					player.setArtist(decode(value));
				}
				// Parameter Album
				else if (messagePart.startsWith("album%3A")) {
					String value = messagePart.substring("album%3A".length());
					player.setAlbum(decode(value));
				}
				// Parameter Genre
				else if (messagePart.startsWith("genre%3A")) {
					String value = messagePart.substring("genre%3A".length());
					player.setGenre(decode(value));
				}
				// Parameter Year
				else if (messagePart.startsWith("year%3A")) {
					String value = messagePart.substring("year%3A".length());
					player.setYear(Integer.parseInt(value));
				}
				// Parameter Artwork
				else if (messagePart.startsWith("artwork_track_id%3A")) {
					String value = messagePart.substring("artwork_track_id%3A".length());
					// NOTE: what is returned if not an artwork id? i.e. if a space?
					if (!value.startsWith(" ")) {
						value = "http://" + host + ":" + webPort + "/music/" + value + "/cover.jpg";
					}
					player.setCoverArt(decode(value));
				}
			}
		}
		
		private void handlePlaylistMessage(SqueezePlayer player, String[] messageParts) {
			String action = messageParts[2];
			
			if (action.equals("newsong")) {
				player.setMode(Mode.play);
			} else if (action.equals("pause")) {
				player.setMode(messageParts[3].equals("0") ? Mode.play : Mode.pause);
			} else if (action.equals("stop")) {
				player.setMode(Mode.stop);
			}
		}

		private void handlePrefsetMessage(SqueezePlayer player, String[] messageParts) {
			if (messageParts.length < 5)
				return;
			
			// server prefsets
			if (messageParts[2].equals("server")) {
				String function = messageParts[3];
				String value = messageParts[4];
				
				if (function.equals("power")) {
					player.setPowered(value.equals("1"));
				} else if (function.equals("volume")) {
					player.setVolume(Integer.parseInt(value));
				}
			}
		}
	}	
}
