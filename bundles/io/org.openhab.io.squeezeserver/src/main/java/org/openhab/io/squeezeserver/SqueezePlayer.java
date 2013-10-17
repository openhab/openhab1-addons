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
	
	private String playerId;
	private String macAddress;

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

	private String currentTitle;
	private String infoYear;
	private String infoAlbum;
	private String infoArtist;
	private String infoArt;
	private String infoGenre;
	private String infoRemoteTitle;
	
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

		this.currentTitle = "";
		this.infoAlbum = "";
		this.infoArt = "";
		this.infoArtist = "";
		this.infoYear = "";
		this.infoGenre = "";
		this.infoRemoteTitle = "";		
				
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
		this.ipAddr = ipAddr.substring(0, ipAddr.indexOf(":"));
		this.port = Integer.parseInt(ipAddr.substring(ipAddr.indexOf(":") + 1));
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
	
	public void setAlbum(String info) {
		if (!this.infoAlbum.equals(info)) {
			logger.debug(this.infoAlbum + " != " + info);
			this.infoAlbum = info;
			fireAlbumChangeEvent();
		}
	}
	
	public void setArt(String info) {
		if (!this.infoArt.equals(info)) {
			logger.debug(this.infoArt + " != " + info);
			this.infoArt = info;
			fireArtChangeEvent();
		}
	}
	
	public void setYear(String info) {
		if (!this.infoYear.equals(info)) {
			logger.debug(this.infoYear + " != " + info);
			this.infoYear = info;
			fireYearChangeEvent();
		}
	}
	
	public void setArtist(String info) {
		if (!this.infoArtist.equals(info)) {
			logger.debug(this.infoArtist + " != " + info);
			this.infoArtist = info;
			fireArtistChangeEvent();
		}
	}
	
	public void setGenre(String info) {
		if (!this.infoGenre.equals(info)) {
			logger.debug(this.infoGenre + " != " + info);
			this.infoGenre = info;
			fireGenreChangeEvent();
		}
	}
	
	public void setRemoteTitle(String info) {
		if (!this.infoRemoteTitle.equals(info)) {
			logger.debug(this.infoRemoteTitle + " != " + info);
			this.infoRemoteTitle = info;
			fireRemoteTitleChangeEvent();
		}
	}
	
	public void setCurrentTitle(String title) {
		if (!this.currentTitle.equals(title) && this.isPowered) {
			this.currentTitle = title;
			fireTitleChangeEvent();
		}
	}
		
	public void refreshRemoteTitle() {
		fireRemoteTitleChangeEvent();
	}
	
	public void refreshGenre() {
		fireGenreChangeEvent();
	}
	
	public void refreshTitle() {
		fireTitleChangeEvent();
	}
	
	public void refreshVolume() {
		fireVolumeChangeEvent();
	}
	
	public void refreshPower() {
		firePowerChangeEvent();
	}
	
	public void refreshMode() {
		fireModeChangeEvent();
	}
	
	public void refreshMute() {
		fireMuteChangeEvent();
	}
	
	public void refreshAlbum() {
		fireAlbumChangeEvent();
	}

	public void refreshArt() {
		fireArtChangeEvent();
	}

	public void refreshArtist() {
		fireArtistChangeEvent();
	}

	public void refreshYear() {
		fireYearChangeEvent();
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
	
	private synchronized void fireTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().titleChangeEvent(event, this.playerId, this.currentTitle);
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
		
	private synchronized void fireAlbumChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().albumChangeEvent(event, this.playerId, this.infoAlbum);
	    }
	}
	
	private synchronized void fireYearChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().yearChangeEvent(event, this.playerId, this.infoYear);
	    }
	}
	
	private synchronized void fireArtistChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().artistChangeEvent(event, this.playerId, this.infoArtist);
	    }
	}
	
	private synchronized void fireArtChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().artChangeEvent(event, this.playerId, this.infoArt);
	    }
	}
	
	private synchronized void fireGenreChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().genreChangeEvent(event, this.playerId, this.infoGenre);
	    }
	}
	
	private synchronized void fireRemoteTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().remoteTitleChangeEvent(event, this.playerId, this.infoRemoteTitle);
	    }
	}	
}
