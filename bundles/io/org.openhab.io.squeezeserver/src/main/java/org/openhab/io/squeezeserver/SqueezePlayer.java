/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.squeezeserver;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal state of a Squeezebox player 
 * 
 * @author Markus Wolters
 * @author Ben Jones
 * @since 1.4.0
 */
public class SqueezePlayer {
	
	public enum Mode {
		play,
		pause,
		stop;
	}
	
	private final String playerId;
	private final String macAddress;

	private String ipAddr;
	private String uuid;
	private int port;
	private String name;
	private String model;
	private Mode mode;
	private boolean isPowered;
	private boolean isMuted;
	private int volume;
	private int unmuteVolume;

	private String title;
	private String album;
	private String artist;
	private String coverArt;
	private String genre;
	private int year;
	private String remoteTitle;
	
	private String irCode;
	
	private List<SqueezePlayerEventListener> playerListeners = new ArrayList<SqueezePlayerEventListener>();
	
	private static final Logger logger = LoggerFactory.getLogger(SqueezePlayer.class);
	
	public SqueezePlayer(String playerId, String macAddress) {
		this.playerId = playerId;
		this.macAddress = macAddress;

		this.uuid = "";
		this.ipAddr = "";
		this.port = 0;
		this.name = "";
		this.model = "";
		this.mode = Mode.stop;
		this.isPowered = false;
		this.isMuted = false;
		this.volume = -128;
		this.unmuteVolume = 50;

		this.title = "";
		this.album = "";
		this.artist = "";
		this.coverArt = "";
		this.genre = "";
		this.year = 0;
		this.remoteTitle = "";		
				
		printDebug();
	}
	
	public String getPlayerId() {
		return this.playerId;
	}
	
	public String getMacAddress() {
		return this.macAddress;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getIpAddr() {
		return this.ipAddr;
	}
	
	public void setIpAddr(String ipAddr) {
		if (ipAddr.contains(":")) {
			this.ipAddr = ipAddr.substring(0, ipAddr.indexOf(":"));
			this.port = Integer.parseInt(ipAddr.substring(ipAddr.indexOf(":") + 1));
		} else {
			this.ipAddr = ipAddr;
		}	
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getModel() {
		return this.model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public boolean isPowered() {
		return this.isPowered;
	}
	
	public void setPowered(boolean isPowered) {
		if (this.isPowered != isPowered) {
			this.isPowered = isPowered;
			firePowerChangeEvent();
		}
	}
	
	public boolean isMuted() {
		return this.isMuted;
	}
	
	public void setMuted(boolean isMuted) {
		if (this.isMuted != isMuted) {
			this.isMuted = isMuted;
			fireMuteChangeEvent();
		}
	}
	
	public Mode getMode() {
		return this.mode;
	}

	public boolean isPlaying() {
		return this.mode.equals(Mode.play);
	}
	
	public boolean isPaused() {
		return this.mode.equals(Mode.pause);
	}
	
	public boolean isStopped() {
		return this.mode.equals(Mode.stop);
	}
	
	public void setMode(Mode mode) {
		if (this.mode != mode) {
			this.mode = mode;
			fireModeChangeEvent();
			if (mode == Mode.play)
			{
				fireTitleChangeEvent();
				fireVolumeChangeEvent();
			}
		}
	}
		
	public void setVolume(int volume) {	
		if (this.volume != volume) {
		    if (volume < 0) {
		    	this.volume = 0;
		    } else if  (volume > 100) {
		    	this.volume = 100;
		    } else {
		    	this.volume = volume;
		    }
		    
			if (this.volume == 0) {
				setMuted(true);
			} else {
				setMuted(false);
				this.unmuteVolume = this.volume;
			}
			fireVolumeChangeEvent();
		}
	}
	
	public int getVolume() {
		return this.volume;
	}
	
	public int getUnmuteVolume() {
		return this.unmuteVolume;
	}
	
	public void printDebug() {
		logger.trace("SqueezePlayer    id: " + this.playerId);
		logger.trace("SqueezePlayer   MAC: " + this.macAddress);
		logger.trace("SqueezePlayer  uuid: " + this.uuid);
		logger.trace("SqueezePlayer  name: " + this.name);
		logger.trace("SqueezePlayer model: " + this.model);
		logger.trace("SqueezePlayer    ip: " + this.ipAddr);
		logger.trace("SqueezePlayer  port: " + String.valueOf(this.port));
	}
	
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		if (!StringUtils.equals(this.title, title)) {
			logger.trace("Title: " + this.title + " != " + title);
			this.title = title;
			fireTitleChangeEvent();
		}
	}
		
	public String getAlbum() {
		return this.album;
	}

	public void setAlbum(String album) {
		if (!StringUtils.equals(this.album, album)) {
			logger.trace("Album: " + this.album + " != " + album);
			this.album = album;
			fireAlbumChangeEvent();
		}
	}
	
	public String getArtist() {
		return this.artist;
	}

	public void setArtist(String artist) {
		if (!StringUtils.equals(this.artist, artist)) {
			logger.trace("Artist: " + this.artist + " != " + artist);
			this.artist = artist;
			fireArtistChangeEvent();
		}
	}
	
	public String getCoverArt() {
		return this.coverArt;
	}

	public void setCoverArt(String coverArt) {
		if (!StringUtils.equals(this.coverArt, coverArt)) {
			logger.trace("CoverArt: " + this.coverArt + " != " + coverArt);
			this.coverArt = coverArt;
			fireCoverArtChangeEvent();
		}
	}
	
	public String getGenre() {
		return this.genre;
	}

	public void setGenre(String genre) {
		if (!StringUtils.equals(this.genre, genre)) {
			logger.trace("Genre: " + this.genre + " != " + genre);
			this.genre = genre;
			fireGenreChangeEvent();
		}
	}
	
	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		if (this.year != year) {
			logger.trace("Year: " + this.year + " != " + year);
			this.year = year;
			fireYearChangeEvent();
		}
	}
	
	public String getRemoteTitle() {
		return this.remoteTitle;
	}

	public void setRemoteTitle(String remoteTitle) {
		if (!StringUtils.equals(this.remoteTitle, remoteTitle)) {
			logger.trace("RemoteTitle: " + this.remoteTitle + " != " + remoteTitle);
			this.remoteTitle = remoteTitle;
			fireRemoteTitleChangeEvent();
		}
	}
	
	
	public String getIrCode() {
		return this.irCode;
	}
	
	public void setIrCode(String irCode) {
		if (!StringUtils.equals(this.irCode, irCode)) {
			logger.trace("IrCode: " + this.irCode + " != " + irCode);
			this.irCode = irCode;
			fireIrCodeChangeEvent();
		}
	}
	
	
	@SuppressWarnings("serial")
	public class PlayerEvent extends EventObject {
		public PlayerEvent(SqueezePlayer source) {
			super(source);
		}
		
		public SqueezePlayer getPlayer() {
			return (SqueezePlayer)this.source;
		}
		
		public String getPlayerId() {
			// shortcut to get the player id
			return getPlayer().getPlayerId();
		}
	}
	
	public synchronized void addPlayerEventListener(SqueezePlayerEventListener listener)  {
		playerListeners.add(listener);
	}

	public synchronized void removePlayerEventListener(SqueezePlayerEventListener listener)   {
		playerListeners.remove(listener);
	}
	
	private synchronized void fireVolumeChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().volumeChangeEvent(event);
	    }
	}
	
	private synchronized void fireMuteChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().muteChangeEvent(event);
	    }
	}
	
	private synchronized void firePowerChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().powerChangeEvent(event);
	    }
	}
	
	private synchronized void fireModeChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().modeChangeEvent(event);
	    }
	}
		
	private synchronized void fireTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().titleChangeEvent(event);
	    }
	}
	
	private synchronized void fireArtistChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().artistChangeEvent(event);
	    }
	}
	
	private synchronized void fireAlbumChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().albumChangeEvent(event);
	    }
	}
	
	private synchronized void fireCoverArtChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().coverArtChangeEvent(event);
	    }
	}
	
	private synchronized void fireGenreChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().genreChangeEvent(event);
	    }
	}
	
	private synchronized void fireYearChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().yearChangeEvent(event);
	    }
	}
	
	private synchronized void fireRemoteTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().remoteTitleChangeEvent(event);
	    }
	}	
	
	private synchronized void fireIrCodeChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().irCodeChangeEvent(event);
	    }
	}	
}
