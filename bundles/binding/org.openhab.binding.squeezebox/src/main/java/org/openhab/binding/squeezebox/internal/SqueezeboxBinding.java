/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.squeezebox.SqueezeboxBindingConfig;
import org.openhab.binding.squeezebox.SqueezeboxBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
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
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		if (squeezeServer == null) {
			logger.warn("Squeeze Server not initialised or configured yet, ignoring command '{}' for item '{}'", command.toString(), itemName);
			return;
		}

		logger.trace("internalReceiveCommand(itemname = {}, command = {})", itemName, command.toString());
		for (SqueezeboxBindingProvider provider : providers) {
			SqueezeboxBindingConfig bindingConfig = provider.getSqueezeboxBindingConfig(itemName);

			String playerId = bindingConfig.getPlayerId();
			SqueezePlayer player = squeezeServer.getPlayer(playerId);
			if (player == null) {
				logger.warn("No Squeezebox player configured with id '{}'. Ignoring.", playerId);
				continue;
			}
		
			try {
				switch (bindingConfig.getCommandType()) {
					case POWER:
						if (command.equals(OnOffType.ON))
							squeezeServer.powerOn(playerId);
						else if (command.equals(OnOffType.OFF))
							squeezeServer.powerOff(playerId);						
						break;
					
					case MUTE:
						if (command.equals(OnOffType.ON))
							squeezeServer.mute(playerId);
						else if (command.equals(OnOffType.OFF))
							squeezeServer.unMute(playerId);						
						break;
					
					case VOLUME: 
						if (command.equals(IncreaseDecreaseType.INCREASE))						
							squeezeServer.volumeUp(playerId);
						else if (command.equals(IncreaseDecreaseType.DECREASE))
							squeezeServer.volumeDown(playerId); 
						else if (command.equals(UpDownType.UP))						
							squeezeServer.volumeUp(playerId);
						else if (command.equals(UpDownType.DOWN))
							squeezeServer.volumeDown(playerId); 
						else if (command instanceof DecimalType)
							squeezeServer.setVolume(playerId, ((DecimalType)command).intValue()); 
						break;

					case PLAY:
						if (command.equals(OnOffType.ON))
							squeezeServer.play(playerId);
						else if (command.equals(OnOffType.OFF))
							squeezeServer.stop(playerId);
						break;
					case PAUSE:
						if (command.equals(OnOffType.ON))
							squeezeServer.pause(playerId);
						else if (command.equals(OnOffType.OFF))
							squeezeServer.play(playerId);
						break;
					case STOP:
						if (command.equals(OnOffType.ON))
							squeezeServer.stop(playerId);
						else if (command.equals(OnOffType.OFF))
							squeezeServer.play(playerId);
						break;
					case NEXT:
						if (command.equals(OnOffType.ON))
							squeezeServer.next(playerId);
						break;						
					case PREV:
						if (command.equals(OnOffType.ON))
							squeezeServer.prev(playerId);
						break;
						
					case HTTP: 
						if (command.equals(OnOffType.ON))
							squeezeServer.playUrl(playerId, "http://" + bindingConfig.getExtra()); 
						else if (command.equals(OnOffType.OFF))
							squeezeServer.stop(playerId);
						break;
					case FILE: 
						if (command.equals(OnOffType.ON))
							squeezeServer.playUrl(playerId, "file://" + bindingConfig.getExtra()); 
						else if (command.equals(OnOffType.OFF))
							squeezeServer.stop(playerId);
						break;
					
					case SYNC: 
						if (command.equals(OnOffType.ON))
							squeezeServer.syncPlayer(playerId, bindingConfig.getExtra()); 
						else if (command.equals(OnOffType.OFF))
							squeezeServer.unSyncPlayer(playerId);
						break;

					default:
						logger.warn("Unsupported command type '{}'", bindingConfig.getCommandType()); 
				}
			}
			catch (Exception e) {
				logger.warn("Error executing command type '" + bindingConfig.getCommandType() + "'", e);
			}	
		}
	}

	@Override
	public void powerChangeEvent(PlayerEvent event, String playerId, boolean isPowered) {
		logger.debug("SqueezePlayer " + playerId + " -> is powered: " + Boolean.toString(isPowered));
		booleanStateChange(playerId, CommandType.POWER, isPowered);
	}

	@Override
	public void muteChangeEvent(PlayerEvent event, String playerId, boolean isMuted) {
		logger.debug("SqueezePlayer " + playerId + " -> is muted: " + Boolean.toString(isMuted));
		booleanStateChange(playerId, CommandType.MUTE, isMuted);
	}
	
	@Override
	public void volumeChangeEvent(PlayerEvent event, String playerId, int volume) {
		logger.debug("SqueezePlayer " + playerId + " -> new volume: " + Integer.toString(volume));
		for (String itemName : getItemNames(playerId, CommandType.VOLUME)) {
			eventPublisher.postUpdate(itemName, new PercentType(volume));
		}
	}
	
	@Override
	public void modeChangeEvent(PlayerEvent event, String playerId, Mode mode) {
		logger.debug("SqueezePlayer " + playerId + " -> mode: " + mode.toString());
		booleanStateChange(playerId, CommandType.PLAY, mode.equals(Mode.play));
		booleanStateChange(playerId, CommandType.PAUSE, mode.equals(Mode.pause));
		booleanStateChange(playerId, CommandType.STOP, mode.equals(Mode.stop));
	}

	@Override
	public void titleChangeEvent(PlayerEvent event, String playerId, String title) {
		logger.debug("SqueezePlayer " + playerId + " -> title: " + title);
		stringChangeEvent(playerId, CommandType.TITLE, title);
	}
		
	@Override
	public void albumChangeEvent(PlayerEvent event, String playerId, String album) {
		logger.debug("SqueezePlayer " + playerId + " -> album: " + album);
		stringChangeEvent(playerId, CommandType.ALBUM, album);
	}

	@Override
	public void artistChangeEvent(PlayerEvent event, String playerId, String artist) {
		logger.debug("SqueezePlayer " + playerId + " -> artist: " + artist);
		stringChangeEvent(playerId, CommandType.ARTIST, artist);
	}

	@Override
	public void coverArtChangeEvent(PlayerEvent event, String playerId, String coverart) {
		logger.debug("SqueezePlayer " + playerId + " -> coverart: " + coverart);
		stringChangeEvent(playerId, CommandType.COVERART, coverart);
	}

	@Override
	public void yearChangeEvent(PlayerEvent event, String playerId, int year) {
		logger.debug("SqueezePlayer " + playerId + " -> year: " + year);
		stringChangeEvent(playerId, CommandType.YEAR, Integer.toString(year));
	}

	@Override
	public void genreChangeEvent(PlayerEvent event, String playerId, String genre) {
		logger.debug("SqueezePlayer " + playerId + " -> genre: " + genre);
		stringChangeEvent(playerId, CommandType.GENRE, genre);
	}

	@Override
	public void remoteTitleChangeEvent(PlayerEvent event, String playerId, String title) {
		logger.debug("SqueezePlayer " + playerId + " -> title: " + title);
		stringChangeEvent(playerId, CommandType.REMOTETITLE, title);
	}
	
	private void stringChangeEvent(String playerId, CommandType commandType, String newState) {
		for (String itemName : getItemNames(playerId, commandType)) {
			eventPublisher.postUpdate(itemName, StringType.valueOf(newState));
		}
	}
	
	private void booleanStateChange(String playerId, CommandType commandType, boolean newState) {
		for (String itemName : getItemNames(playerId, commandType)) {
			if (newState) {
				eventPublisher.postUpdate(itemName, OnOffType.ON);
			} else {
				eventPublisher.postUpdate(itemName, OnOffType.OFF);
			}
		}
	}

	private List<String> getItemNames(String playerId, CommandType commandType) {
		List<String> itemNames = new ArrayList<String>();
		for (SqueezeboxBindingProvider provider : this.providers) {
			for (String itemName : provider.getItemNames()) {
				SqueezeboxBindingConfig bindingConfig = provider.getSqueezeboxBindingConfig(itemName);
				if (!bindingConfig.getPlayerId().equals(playerId))
					continue;
				if (!bindingConfig.getCommandType().equals(commandType))
					continue;
				itemNames.add(itemName);
			}
		}
		return itemNames;
	}
	
	/**
	 * Setter for Declarative Services. Adds the SqueezeServer instance.
	 * 
	 * @param squeezeServer
	 *            Service.
	 */
	public void setSqueezeServer(SqueezeServer squeezeServer) {
		this.squeezeServer = squeezeServer;
		
		for (SqueezePlayer player : squeezeServer.getPlayers()) {
			player.addPlayerEventListener(this);
		}
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param squeezeServer
	 *            Service to remove.
	 */
	public void unsetSqueezeServer(SqueezeServer squeezeServer) {
		this.squeezeServer = null;
		
		for (SqueezePlayer player : squeezeServer.getPlayers()) {
			player.removePlayerEventListener(this);
		}
	}
}
