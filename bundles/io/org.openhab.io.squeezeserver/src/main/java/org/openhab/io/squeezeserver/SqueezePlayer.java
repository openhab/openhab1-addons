/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
		this.coverArt = "";
		this.artist = "";
		this.year = 0;
		this.genre = "";
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
		}
	}
		
	public void setVolume(int volume) {	
		if (this.volume != volume) {
			this.volume = volume;
			if (this.volume <= 0) {
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
	
	public void setTitle(String title) {
		if (!StringUtils.equals(this.title, title)) {
			logger.trace("Title: " + this.title + " != " + title);
			this.title = title;
			fireTitleChangeEvent();
		}
	}
		
	public void setAlbum(String album) {
		if (!StringUtils.equals(this.album, album)) {
			logger.trace("Album: " + this.album + " != " + album);
			this.album = album;
			fireAlbumChangeEvent();
		}
	}
	
	public void setCoverArt(String coverArt) {
		if (!StringUtils.equals(this.coverArt, coverArt)) {
			logger.trace("CoverArt: " + this.coverArt + " != " + coverArt);
			this.coverArt = coverArt;
			fireCoverArtChangeEvent();
		}
	}
	
	public void setYear(int year) {
		if (this.year != year) {
			logger.trace("Year: " + this.year + " != " + year);
			this.year = year;
			fireYearChangeEvent();
		}
	}
	
	public void setArtist(String artist) {
		if (!StringUtils.equals(this.artist, artist)) {
			logger.trace("Artist: " + this.artist + " != " + artist);
			this.artist = artist;
			fireArtistChangeEvent();
		}
	}
	
	public void setGenre(String genre) {
		if (!StringUtils.equals(this.genre, genre)) {
			logger.trace("Genre: " + this.genre + " != " + genre);
			this.genre = genre;
			fireGenreChangeEvent();
		}
	}
	
	public void setRemoteTitle(String remoteTitle) {
		if (!StringUtils.equals(this.remoteTitle, remoteTitle)) {
			logger.trace("RemoteTitle: " + this.remoteTitle + " != " + remoteTitle);
			this.remoteTitle = remoteTitle;
			fireRemoteTitleChangeEvent();
		}
	}
	
	@SuppressWarnings("serial")
	public class PlayerEvent extends EventObject {
		public PlayerEvent(Object source) {
			super(source);
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

		int tmpVolume;
	    if (this.volume < 0) {
	    	tmpVolume = 0;
	    } else if  (this.volume > 100) {
	    	tmpVolume = 100;
	    } else {
	    	tmpVolume = this.volume;
	    }
	    
	    while(itr.hasNext())  {
	    	itr.next().volumeChangeEvent(event, this.playerId, tmpVolume);
	    }
	}
	
	private synchronized void fireMuteChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().muteChangeEvent(event, this.playerId, this.isMuted);
	    }
	}
	
	private synchronized void firePowerChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().powerChangeEvent(event, this.playerId, this.isPowered);
	    }
	}
	
	private synchronized void fireModeChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().modeChangeEvent(event, this.playerId, this.mode);
	    }
	}
		
	private synchronized void fireTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().titleChangeEvent(event, this.playerId, this.title);
	    }
	}
	
	private synchronized void fireAlbumChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().albumChangeEvent(event, this.playerId, this.album);
	    }
	}
	
	private synchronized void fireYearChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().yearChangeEvent(event, this.playerId, this.year);
	    }
	}
	
	private synchronized void fireArtistChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().artistChangeEvent(event, this.playerId, this.artist);
	    }
	}
	
	private synchronized void fireCoverArtChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().coverArtChangeEvent(event, this.playerId, this.coverArt);
	    }
	}
	
	private synchronized void fireGenreChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().genreChangeEvent(event, this.playerId, this.genre);
	    }
	}
	
	private synchronized void fireRemoteTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().remoteTitleChangeEvent(event, this.playerId, this.remoteTitle);
	    }
	}	
}
