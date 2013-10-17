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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.squeezebox.SqueezeboxBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.io.squeezeserver.SqueezePlayer;
import org.openhab.io.squeezeserver.SqueezePlayer.Mode;
import org.openhab.io.squeezeserver.SqueezePlayer.PlayerEvent;
import org.openhab.io.squeezeserver.SqueezePlayerEventListener;
import org.openhab.io.squeezeserver.SqueezeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding which communicates with (one or many) Squeezeboxes. 
 * 
 * @author Markus Wolters
 * @author Ben Jones
 * @since 1.3.0
 */
public class SqueezeboxBinding extends AbstractBinding<SqueezeboxBindingProvider> implements SqueezePlayerEventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(SqueezeboxBinding.class);

	private SqueezeServer squeezeServer;

	/**
	 * @{inheritDoc}
	 */
	public void activate() {
		for (SqueezePlayer player : squeezeServer.getPlayers()) {
			player.addPlayerEventListener(this);
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
			logger.warn("Cannot find matching binding provider [itemName={}, command={}], ignoring.", itemName, command);
			return;
		}
		
		String playerCommand = provider.getPlayerCommand(itemName, command.toString());
		if (StringUtils.isEmpty(playerCommand)) {
			logger.warn("Invalid player command [itemName={}, command={}], ignoring.", itemName, command);
			return;
		}	

		executePlayerCommand(playerCommand);
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
			if (!StringUtils.isEmpty(playerCommand)) {
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
		
		Pattern pattern = Pattern.compile("(\\w*):(.*)");
		Matcher matcher = pattern.matcher(playerCommandLine);
		if (matcher.matches()) {
			playerId = matcher.group(1);
			playerCommand = matcher.group(2);
		}

		if (squeezeServer == null) {
			logger.warn("Squeeze Server not initialised or configured yet, ignoring commandLine '{}' for player '{}'", playerCommand, playerId);
			return;
		}

		logger.debug("Executed commandLine '{}' for player '{}'", playerCommand, playerId);
		
		SqueezePlayer player = squeezeServer.getPlayer(playerId);
		if (player == null) {
			logger.warn("No Squeezebox player configured with id '{}', ignoring.", playerId);
			return;
		}
		
		PlayerCommandTypeMapping command;
		if (playerCommand.contains("=")) {
			command = PlayerCommandTypeMapping.fromString(playerCommand.substring(0, playerCommand.indexOf("=")));
				try {
					argument = URLEncoder.encode(playerCommand.substring(playerCommand.indexOf("=") + 1), "UTF-8").replace("+", "%20");
				} catch (UnsupportedEncodingException e) {
					logger.error("Error while encoding message, ignoring.");
					return;
				}

		} else {
			command = PlayerCommandTypeMapping.fromString(playerCommand);	
		}

		if (command == null) {
			logger.warn("Could not match to a Squeezebox command for commandLine '{}', ignoring.", playerCommand);
			return;
		}
		
		try {
			switch (command) {
				case MUTE: squeezeServer.mute(playerId); break;
				case UNMUTE: squeezeServer.unMute(playerId); break;
				case VOLUME_INCREASE: squeezeServer.volumeUp(playerId); break;
				case VOLUME_DECREASE: squeezeServer.volumeDown(playerId); break;
				case VOLUME: squeezeServer.setVolume(playerId, Integer.parseInt(argument)); break;
				case PLAY: squeezeServer.play(playerId); break;
				case PAUSE: squeezeServer.pause(playerId); break;
				case POWER_ON: squeezeServer.powerOn(playerId); break;
				case POWER_OFF: squeezeServer.powerOff(playerId); break;
				case NEXT: squeezeServer.next(playerId); break;
				case PREV: squeezeServer.prev(playerId); break;
				case STOP: squeezeServer.stop(playerId); break;
				case HTTP: squeezeServer.playUrl(playerId, "http://" + argument); break;
				case FILE: squeezeServer.playUrl(playerId, "file://" + argument); break;
				case ADD: squeezeServer.syncPlayer(playerId, argument); break;
				case REMOVE: squeezeServer.unSyncPlayer(playerId); break;
				default: logger.warn("Unknown playerCommand '{}'", playerCommand); break;
			}
		}
		catch (Exception e) {
			logger.warn("Error executing playerCommand '" + playerCommand + "'", e);
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
			String playerId;
			if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.VOLUME.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshVolume();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_POWERED.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshPower();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_MUTED.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshMute();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.TITLE.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshTitle();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_PLAYING.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshMode();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_STOPPED.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshMode();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.IS_PAUSED.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshMode();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.ALBUM.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshAlbum();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.COVERART.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshArt();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.YEAR.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshYear();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.ARTIST.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshArtist();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.GENRE.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshGenre();
			} else if (!StringUtils.isEmpty(playerId = squeezeProvider.getPlayerByItemnameAndCommand(itemName, PlayerCommandTypeMapping.REMOTETITLE.getPlayerCommand()))) {
				squeezeServer.getPlayer(playerId).refreshRemoteTitle();
			}
		}
	}

	public void titleChangeEvent(PlayerEvent event, String id, String title) {
		stringChangeEvent(id, title, PlayerCommandTypeMapping.TITLE);
	}
	
	public void stringChangeEvent(String id, String newState, PlayerCommandTypeMapping type) {
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(id, type);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, StringType.valueOf(newState));
			}
		}
	}
	
	@Override
	public void volumeChangeEvent(PlayerEvent event, String id, int volume) {
		logger.debug("SqueezePlayer " + id + " -> new volume: " + Integer.toString(volume));
		
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(id, PlayerCommandTypeMapping.VOLUME);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, new PercentType(volume));
			}
		}
	}
	
	private void commonStateChange(String id, boolean newState, PlayerCommandTypeMapping type) {
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(id, type);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				if (newState) {
					eventPublisher.postUpdate(itemName, OnOffType.ON);
				} else {
					eventPublisher.postUpdate(itemName, OnOffType.OFF);
				}
			}
		}
	}

	@Override
	public void muteChangeEvent(PlayerEvent event, String id, boolean isMuted) {
		logger.debug("SqueezePlayer " + id + " -> is muted: " + Boolean.toString(isMuted));
		commonStateChange(id, isMuted, PlayerCommandTypeMapping.IS_MUTED);
	}

	
	@Override
	public void modeChangeEvent(PlayerEvent event, String id, Mode mode) {
		logger.debug("SqueezePlayer " + id + " -> mode: " + mode.toString());
		commonStateChange(id, mode.equals(Mode.play), PlayerCommandTypeMapping.IS_PLAYING);
		commonStateChange(id, mode.equals(Mode.pause), PlayerCommandTypeMapping.IS_PAUSED);
		commonStateChange(id, mode.equals(Mode.stop), PlayerCommandTypeMapping.IS_STOPPED);
	}

	@Override
	public void powerChangeEvent(PlayerEvent event, String id, boolean isPowered) {
		logger.debug("SqueezePlayer " + id + " -> is powered: " + Boolean.toString(isPowered));
		commonStateChange(id, isPowered, PlayerCommandTypeMapping.IS_POWERED);
	}

	@Override
	public void albumChangeEvent(PlayerEvent event, String id, String album) {
		logger.debug("SqueezePlayer " + id + " -> album: " + album);
		stringChangeEvent(id, album, PlayerCommandTypeMapping.ALBUM);
	}

	@Override
	public void artistChangeEvent(PlayerEvent event, String id, String artist) {
		logger.debug("SqueezePlayer " + id + " -> artist: " + artist);
		stringChangeEvent(id, artist, PlayerCommandTypeMapping.ARTIST);
	}

	@Override
	public void artChangeEvent(PlayerEvent event, String id, String art) {
		logger.debug("SqueezePlayer " + id + " -> art: " + art);
		stringChangeEvent(id, art, PlayerCommandTypeMapping.COVERART);
	}

	@Override
	public void yearChangeEvent(PlayerEvent event, String id, String year) {
		logger.debug("SqueezePlayer " + id + " -> year: " + year);
		stringChangeEvent(id, year, PlayerCommandTypeMapping.YEAR);
	}

	@Override
	public void genreChangeEvent(PlayerEvent event, String id, String genre) {
		logger.debug("SqueezePlayer " + id + " -> genre: " + genre);
		stringChangeEvent(id, genre, PlayerCommandTypeMapping.GENRE);
	}

	@Override
	public void remoteTitleChangeEvent(PlayerEvent event, String id, String title) {
		logger.debug("SqueezePlayer " + id + " -> title: " + title);
		stringChangeEvent(id, title, PlayerCommandTypeMapping.REMOTETITLE);
	}
	
	/**
	 * Setter for Declarative Services. Adds the SqueezeServer instance.
	 * 
	 * @param squeezeServer
	 *            Service.
	 */
	public void setSqueezeServer(SqueezeServer squeezeServer) {
		this.squeezeServer = squeezeServer;
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param squeezeServer
	 *            Service to remove.
	 */
	public void unsetSqueezeServer(SqueezeServer squeezeServer) {
		this.squeezeServer = null;
	}
}
