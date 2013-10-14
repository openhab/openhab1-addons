/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.squeezebox.SqueezeboxBindingProvider;
import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer;
import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer.PlayerEvent;
import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer.STATES;
import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayerEventListener;
import org.openhab.binding.squeezebox.squeezeserver.SqueezeServer;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding which communicates with (one or many) Squeezeboxes. 
 * 
 * @author Markus Wolters
 * @since 1.3.0
 */
public class SqueezeboxBinding extends AbstractBinding<SqueezeboxBindingProvider> implements ManagedService, SqueezePlayerEventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(SqueezeboxBinding.class);

	private SqueezeServer squeezeServer;
		
	private final static int DEFAULT_CLI_PORT = 9090;
	private final static int DEFAULT_WEB_PORT = 9000;
	private final static String DEFAULT_HOST = "0.0.0.0";
	
	/** RegEx to validate SqueezeServer config <code>'^(squeeze:)(host|cliport|webport)=.+$'</code> */
	private static final Pattern EXTRACT_SERVER_CONFIG_PATTERN = Pattern.compile("^(server)\\.(host|cliport|webport)$");
	
	/** RegEx to validate a mpdPlayer config <code>'^(.*?)\\.(id)$'</code> */
	private static final Pattern EXTRACT_PLAYER_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(id)$");
	
	
	public void activate() {
	}
	
	public void deactivate() {
	}

	
	private void connectSqueezeServer(String host, int cliport, int webport, Map<String, String> tmpPlayerMap) {
		squeezeServer = new SqueezeServer(host, cliport, webport);
		
		for (Map.Entry<String, String> playerMapEntry : tmpPlayerMap.entrySet()) {
			SqueezePlayer player = new SqueezePlayer(playerMapEntry.getKey(), playerMapEntry.getValue());
			player.addPlayerhangeEventListener(this);
			squeezeServer.addPlayer(player);
		}
		
		squeezeServer.connect();
	}
	
	private void disconnectSqueezeServer() {
        if (null != squeezeServer) {
        	squeezeServer = null;
        }
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		SqueezeboxBindingProvider provider = 
			findFirstMatchingBindingProvider(itemName, command.toString());
		
		if (provider == null) {
			logger.warn("cannot find matching binding provider [itemName={}, command={}]", itemName, command);
			return;
		}
		
		String playerCommand = 
			provider.getPlayerCommand(itemName, command.toString());
		if (StringUtils.isNotBlank(playerCommand)) {
			executePlayerCommand(playerCommand);
		}
		
	}

	/**
	 * Find the first matching {@link SqueezeboxBindingProvider} according to 
	 * <code>itemName</code> and <code>command</code>. 
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 * provider could be found
	 */
	private SqueezeboxBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		SqueezeboxBindingProvider firstMatchingProvider = null;
		for (SqueezeboxBindingProvider provider : this.providers) {
			
			String playerCommand = provider.getPlayerCommand(itemName, command);
			if (playerCommand != null) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}	
	
	/**
	 * Executes the given <code>playerCommandLine</code> on the MPD. The
	 * <code>playerCommandLine</code> is split into it's properties 
	 * <code>playerId</code> and <code>playerCommand</code>.
	 * 
	 * @param playerCommandLine the complete commandLine which gets splitted into
	 * it's properties.
	 */
	private void executePlayerCommand(String playerCommandLine) {
		String playerId = "";
		String playerCommand = "";
		String argument = "";
		
		Pattern cmdPattern = Pattern.compile("(\\w*):(.*)");
		Matcher m = cmdPattern.matcher(playerCommandLine);
		if (m.matches()) {
			playerId   = m.group(1);
			playerCommand = m.group(2);
		}
		logger.info("executed commandLine '{}' for player '{}'", playerCommand, playerId);
		
		if (null != squeezeServer) {
			PlayerCommandTypeMapping command;
			if (playerCommand.contains("=")) {
				command = PlayerCommandTypeMapping.fromString(playerCommand.substring(0, playerCommand.indexOf("=")));
					try {
						argument = URLEncoder.encode(playerCommand.substring(playerCommand.indexOf("=") + 1), "UTF-8").replace("+", "%20");
					} catch (UnsupportedEncodingException e) {
						logger.error("error while encoding message");
					}

			} else {
				command = PlayerCommandTypeMapping.fromString(playerCommand);	
			}
			
			SqueezePlayer player = squeezeServer.getPlayerByOhName(playerId);
			
			if ((null != player) && (null != command)) {
				try {
					switch (command) {
						case MUTE: squeezeServer.muteByOhName(playerId, true); break;
						case UNMUTE: squeezeServer.muteByOhName(playerId, false); break;
						case VOLUME_INCREASE: squeezeServer.increaseVolumeByOhName(playerId); break;
						case VOLUME_DECREASE: squeezeServer.decreaseVolumeByOhName(playerId); break;
						case VOLUME: squeezeServer.setVolumeByOhName(playerId, argument); break;
						case PLAY: squeezeServer.playByOhName(playerId); break;
						case PAUSE: squeezeServer.pauseByOhName(playerId, true); break;
						case POWER_ON: squeezeServer.powerByOhName(playerId, true); break;
						case POWER_OFF: squeezeServer.powerByOhName(playerId, false); break;
						case NEXT: squeezeServer.navigateByOhName(playerId, false); break;
						case PREV: squeezeServer.navigateByOhName(playerId, true); break;
						case STOP: squeezeServer.stopByOhName(playerId); break;
						case HTTP: squeezeServer.playUrlByOhName(playerId, "http://" + argument); break;
						case FILE: squeezeServer.playUrlByOhName(playerId, "file://" + argument); break;
						case ADD: squeezeServer.syncPlayer(playerId, "", true); break;
						case REMOVE: squeezeServer.syncPlayer(playerId, argument, false); break;
						default:
							break;
					}
				}
				catch (Exception e) {
					logger.warn("unknow playerCommand '{}'", playerCommand);
				}	
			}
		}
	}
	
	private String[] getItemNamesByPlayerAndPlayerCommand(String playerId, PlayerCommandTypeMapping playerCommand) {
		Set<String> itemNames = new HashSet<String>();
		for (SqueezeboxBindingProvider provider : this.providers) {
			itemNames.addAll(Arrays.asList(
				provider.getItemNamesByPlayerAndPlayerCommand(playerId, playerCommand)));
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof SqueezeboxBindingProvider) {
			logger.debug("Squeezebox bindingChanged: " + itemName);
			SqueezeboxBindingProvider squeezeProvider = (SqueezeboxBindingProvider) provider;
			String playerName;
			if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.VOLUME.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshVolume();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_POWERED.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshPower();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_MUTED.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshMute();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.TITLE.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshTitle();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_PLAYING.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshPlay();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_STOPPED.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshStop();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_PAUSED.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshPause();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.ALBUM.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshAlbum();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.COVERART.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshArt();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.YEAR.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshYear();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.ARTIST.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshArtist();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.GENRE.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshGenre();
			} else if (!(playerName = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.REMOTETITLE.getPlayerCommand())).equals("")) {
				squeezeServer.getPlayerByOhName(playerName).refreshRemoteTitle();
			}
		}
	}

	public void onSqueezePlayerTitleChangeEvent(PlayerEvent event, String id, String title) {
		onSqueezePlayerStringChangeEvent(id, title, PlayerCommandTypeMapping.TITLE);
	}
	
	public void onSqueezePlayerStringChangeEvent(String id, String newState, PlayerCommandTypeMapping type) {
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(id, type);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, StringType.valueOf(newState));
			}
		}
	}
	
	@Override
	public void onSqueezePlayerVolumeChangeEvent(PlayerEvent event, String id, byte volume) {
		logger.debug("SqueezePlayer " + id + " -> new volume: " + String.valueOf(volume));
		
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(id, PlayerCommandTypeMapping.VOLUME);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, new PercentType(volume));
			}
		}
	}
	
	private void onSqueezePlayerCommonStateChange(String id, SqueezePlayer.STATES newState, PlayerCommandTypeMapping type) {
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(id, type);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				if (SqueezePlayer.STATES.TRUE == newState) {
					eventPublisher.postUpdate(itemName, OnOffType.ON);
				} else {
					eventPublisher.postUpdate(itemName, OnOffType.OFF);
				}
			}
		}
	}

	@Override
	public void onSqueezePlayerMuteStateChangeEvent(PlayerEvent event, String id, SqueezePlayer.STATES isMuted) {
		logger.debug("SqueezePlayer " + id + " -> is muted: " + String.valueOf(isMuted));
		onSqueezePlayerCommonStateChange(id, isMuted, PlayerCommandTypeMapping.IS_MUTED);
	}

	
	@Override
	public void onSqueezePlayerPlayStateChangeEvent(PlayerEvent event, String id, SqueezePlayer.STATES isPlaying) {
		logger.debug("SqueezePlayer " + id + " -> is playing: " + String.valueOf(isPlaying));
		onSqueezePlayerCommonStateChange(id, isPlaying, PlayerCommandTypeMapping.IS_PLAYING);
	}

	@Override
	public void onSqueezePlayerPowerStateChangeEvent(PlayerEvent event, String id, SqueezePlayer.STATES isPowered) {
		logger.debug("SqueezePlayer " + id + " -> is powered: " + String.valueOf(isPowered));
		onSqueezePlayerCommonStateChange(id, isPowered, PlayerCommandTypeMapping.IS_POWERED);
	}

	@Override
	public void onSqueezePlayerPauseStateChangeEvent(PlayerEvent event, String id, STATES isPaused) {
		logger.debug("SqueezePlayer " + id + " -> is paused: " + String.valueOf(isPaused));
		
		onSqueezePlayerCommonStateChange(id, isPaused, PlayerCommandTypeMapping.IS_PAUSED);
	}


	@Override
	public void onSqueezePlayerStopStateChangeEvent(PlayerEvent event, String id, STATES isStopped) {
		logger.debug("SqueezePlayer " + id + " -> is stopped: " + String.valueOf(isStopped));
		
		onSqueezePlayerCommonStateChange(id, isStopped, PlayerCommandTypeMapping.IS_STOPPED);
	}


	@Override
	public void onSqueezePlayerAlbumStateChangeEvent(PlayerEvent event, String id, String album) {
		logger.debug("SqueezePlayer " + id + " -> album: " + album);
		onSqueezePlayerStringChangeEvent(id, album, PlayerCommandTypeMapping.ALBUM);
	}

	@Override
	public void onSqueezePlayerArtistStateChangeEvent(PlayerEvent event, String id, String artist) {
		logger.debug("SqueezePlayer " + id + " -> artist: " + artist);
		onSqueezePlayerStringChangeEvent(id, artist, PlayerCommandTypeMapping.ARTIST);
	}

	@Override
	public void onSqueezePlayerArtStateChangeEvent(PlayerEvent event, String id, String art) {
		logger.debug("SqueezePlayer " + id + " -> art: " + art);
		onSqueezePlayerStringChangeEvent(id, art, PlayerCommandTypeMapping.COVERART);
	}

	@Override
	public void onSqueezePlayerYearStateChangeEvent(PlayerEvent event, String id, String year) {
		logger.debug("SqueezePlayer " + id + " -> year: " + year);
		onSqueezePlayerStringChangeEvent(id, year, PlayerCommandTypeMapping.YEAR);
	}

	@Override
	public void onSqueezePlayerGenreStateChangeEvent(PlayerEvent event, String id, String genre) {
		logger.debug("SqueezePlayer " + id + " -> genre: " + genre);
		onSqueezePlayerStringChangeEvent(id, genre, PlayerCommandTypeMapping.GENRE);
	}

	@Override
	public void onSqueezePlayerRemoteTitleStateChangeEvent(PlayerEvent event, String id, String title) {
		logger.debug("SqueezePlayer " + id + " -> title: " + title);
		onSqueezePlayerStringChangeEvent(id, title, PlayerCommandTypeMapping.REMOTETITLE);
	}
	

	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			disconnectSqueezeServer();
			int cliport = DEFAULT_CLI_PORT;
			int webport = DEFAULT_WEB_PORT;
			String host = DEFAULT_HOST;
			
			Map<String, String> tmpPlayerMap = new HashMap<String, String>();
			
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				
				String key = (String) keys.nextElement();
				
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}
				
				Matcher serverMatcher = EXTRACT_SERVER_CONFIG_PATTERN.matcher(key);
				Matcher playerMatcher = EXTRACT_PLAYER_CONFIG_PATTERN.matcher(key);
				
				String value = (String) config.get(key);
				
				if (serverMatcher.matches()) {	
					serverMatcher.reset();
					serverMatcher.find();
					
					if ("host".equals(serverMatcher.group(2))) {
						host = value;
					}
					else if ("cliport".equals(serverMatcher.group(2))) {
						cliport = Integer.valueOf(value);
					}
					else if ("webport".equals(serverMatcher.group(2))) {
						webport = Integer.valueOf(value);
					}
				} else if (playerMatcher.matches()) {
					tmpPlayerMap.put(value.toString(), playerMatcher.group(1));
				}
			}
			
			connectSqueezeServer(host, cliport, webport, tmpPlayerMap);
		}
	}

	
}
