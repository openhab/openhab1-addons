/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.squeezeserver;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.squeezebox.internal.SqueezeboxBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal state of a Squeezebox player 
 * 
 * @author Markus Wolters
 * @since 1.3.0
 */
public class SqueezePlayer {
	
	public enum STATES {
	    UNKNOWN, TRUE, FALSE
	}

	private String id;
	private String ipAddr;
	private String uuid;
	private int port;
	private String name;
	private String model;
	private STATES isPlaying;
	private STATES isStopped;
	private STATES isPaused;
	private STATES isMuted;
	private STATES isPowered;
	private String currentTitle;
	private String ohName;
	private String infoYear;
	private String infoAlbum;
	private String infoArtist;
	private String infoArt;
	private String infoGenre;
	private String infoRemoteTitle;
	private byte volume;
	private byte unmuteVolume;
	
	private List<SqueezePlayerEventListener> playerListeners = new ArrayList<SqueezePlayerEventListener>();
	
	private static final Logger logger = LoggerFactory.getLogger(SqueezeboxBinding.class);
	
	public SqueezePlayer(String id, String ohName) {
		this.id = id;
		this.uuid = "";
		this.ipAddr = "";
		this.port = 0;
		this.name = "";
		this.model = "";
		this.volume = -128;
		this.unmuteVolume = 50;
		this.ohName = ohName;
		this.infoAlbum = "";
		this.infoArt = "";
		this.infoArtist = "";
		this.infoYear = "";
		this.infoGenre = "";
		this.currentTitle = "";
		this.infoRemoteTitle = "";
		
		isPowered = isMuted = isPaused = isStopped = isPlaying = STATES.UNKNOWN;
		
		printDebug();
	}
	
	public String getOhName() {
		return (this.ohName);
	}
	
	public String getName() {
		return (this.name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUuid() {
		return (this.uuid);
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getId() {
		return (this.id);
	}
	
	public String getIpAddr() {
		return (this.ipAddr);
	}
	
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr.substring(0, ipAddr.indexOf(":"));
		this.port = Integer.parseInt(ipAddr.substring(ipAddr.indexOf(":") + 1));
	}
	
	public int getPort() {
		return (this.port);
	}
	
	public String getModel() {
		return (this.model);
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public STATES isPowered() {
		return (this.isPowered);
	}
	
	public void setPowerState(STATES isPowered) {
		if (this.isPowered != isPowered) {
			
			this.isPowered = isPowered;
			
			firePowerChangeEvent();
			}
	}
	
	public STATES isMuted() {
		return (this.isMuted);
	}
	
	public void setMuteState(STATES isMuted) {
		if (this.isMuted != isMuted) {
			this.isMuted = isMuted;
			
			fireMuteChangeEvent();
		}
	}
	
	public STATES isPlaying() {
		return (this.isPlaying);
	}
	
	public void setPlayState(STATES isPlaying) {
		if (this.isPlaying != isPlaying) {
			this.isPlaying = isPlaying;
			
			firePlayChangeEvent();
			
			if (STATES.TRUE == this.isPlaying) {
				setStopState(STATES.FALSE);
				setPauseState(STATES.FALSE);
			}
		}
	}
	
	public STATES isStopped() {
		return (this.isStopped);
	}
	
	public void setStopState(STATES isStopped) {
		if (this.isStopped != isStopped) {
			this.isStopped = isStopped;
			
			fireStopChangeEvent();
			
			if (STATES.TRUE == this.isStopped) {
				setPlayState(STATES.FALSE);
			}
		}
	}
	
	public STATES isPaused() {
		return (this.isPaused);
	}
	
	public void setPauseState(STATES isPaused) {
		if (this.isPaused != isPaused) {
			this.isPaused = isPaused;
			
			firePauseChangeEvent();
			
			if (STATES.TRUE == this.isPaused) {
				setPlayState(STATES.FALSE);
			}
		}
	}
	
	public void setVolume(String vol) {	
		byte newVolume = Byte.parseByte(vol);
		
		if (this.volume != newVolume) {
			this.volume = newVolume;
			if (0 >= this.volume) {
				setMuteState(STATES.TRUE);
			} else {
				setMuteState(STATES.FALSE);
				this.unmuteVolume = this.volume;
			}
			
			fireVolumeChangeEvent();
		}
	}
	
	public byte getVolume() {
		return (this.volume);
	}
	
	public byte getUnmuteVolume() {
		return (this.unmuteVolume);
	}
	
	public void printDebug() {
		logger.debug("SqueezePlayer    id: " + this.id);
		logger.debug("SqueezePlayer  uuid: " + this.uuid);
		logger.debug("SqueezePlayer  name: " + this.name);
		logger.debug("SqueezePlayer oname: " + this.ohName);
		logger.debug("SqueezePlayer model: " + this.model);
		logger.debug("SqueezePlayer    id: " + this.ipAddr);
		logger.debug("SqueezePlayer  port: " + String.valueOf(this.port));
	}
	
	public void setAlbum(String info) {
		if (!infoAlbum.equals(info)) {
			logger.debug(infoAlbum + " != " + info);
			this.infoAlbum = info;
			fireAlbumChangeEvent();
		}
	}
	
	public void setArt(String info) {
		if (!infoArt.equals(info)) {
			logger.debug(infoArt + " != " + info);
			this.infoArt = info;
			fireArtChangeEvent();
		}
	}
	
	public void setYear(String info) {
		if (!infoYear.equals(info)) {
			logger.debug(infoYear + " != " + info);
			this.infoYear = info;
			fireYearChangeEvent();
		}
	}
	
	public void setArtist(String info) {
		if (!infoArtist.equals(info)) {
			logger.debug(infoArtist + " != " + info);
			this.infoArtist = info;
			fireArtistChangeEvent();
		}
	}
	
	public void setGenre(String info) {
		if (!infoGenre.equals(info)) {
			logger.debug(infoGenre + " != " + info);
			this.infoGenre = info;
			fireGenreChangeEvent();
		}
	}
	
	public void setRemoteTitle(String info) {
		if (!infoRemoteTitle.equals(info)) {
			logger.debug(infoRemoteTitle + " != " + info);
			this.infoRemoteTitle = info;
			fireRemoteTitleChangeEvent();
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
	
	public void refreshPlay() {
		firePlayChangeEvent();
	}
	
	public void refreshMute() {
		fireMuteChangeEvent();
	}
	
	public void refreshStop() {
		fireStopChangeEvent();
	}
	
	public void refreshPause() {
		firePauseChangeEvent();
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
	
	public void setCurrentTitle(String currentTitle) {
		if ((0 != this.currentTitle.compareTo(currentTitle)) && (STATES.TRUE == this.isPowered)) {
			this.currentTitle = currentTitle;
			fireTitleChangeEvent();
		}
	}
	
	
	@SuppressWarnings("serial")
	public class PlayerEvent extends EventObject {
		public PlayerEvent(Object source) {
			super(source);
		}
	}
	
	public synchronized void addPlayerhangeEventListener(SqueezePlayerEventListener listener)  {
		playerListeners.add(listener);
	}

	public synchronized void removePlayerChangeEventListener(SqueezePlayerEventListener listener)   {
		playerListeners.remove(listener);

	}
	

	private synchronized void fireVolumeChangeEvent() {
		byte tmpVolume;
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    if (0 > this.volume) {
	    	tmpVolume = 0;
	    } else if  (100 < this.volume) {
	    	tmpVolume = 100;
	    } else {
	    	tmpVolume = this.volume;
	    }
	    
	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerVolumeChangeEvent(event, this.ohName, tmpVolume);
	    }
	}
	
	private synchronized void fireTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerTitleChangeEvent(event, this.ohName, this.currentTitle);
	    }
	}
	
	private synchronized void fireMuteChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerMuteStateChangeEvent(event, this.ohName, this.isMuted);
	    }
	}
	
	private synchronized void firePowerChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerPowerStateChangeEvent(event, this.ohName, this.isPowered);
	    }
	}
	
	private synchronized void firePlayChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerPlayStateChangeEvent(event, this.ohName, this.isPlaying);
	    }
	}
	
	private synchronized void fireStopChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerStopStateChangeEvent(event, this.ohName, this.isStopped);
	    }
	}
	
	private synchronized void firePauseChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerPauseStateChangeEvent(event, this.ohName, this.isPaused);
	    }
	}
	
	private synchronized void fireAlbumChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerAlbumStateChangeEvent(event, this.ohName, this.infoAlbum);
	    }
	}
	
	private synchronized void fireYearChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerYearStateChangeEvent(event, this.ohName, this.infoYear);
	    }
	}
	
	private synchronized void fireArtistChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerArtistStateChangeEvent(event, this.ohName, this.infoArtist);
	    }
	}
	
	private synchronized void fireArtChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerArtStateChangeEvent(event, this.ohName, this.infoArt);
	    }
	}
	
	private synchronized void fireGenreChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerGenreStateChangeEvent(event, this.ohName, this.infoGenre);
	    }
	}
	
	private synchronized void fireRemoteTitleChangeEvent() {
		PlayerEvent event = new PlayerEvent(this);
	    Iterator<SqueezePlayerEventListener> itr = playerListeners.iterator();

	    while(itr.hasNext())  {
	    	itr.next().onSqueezePlayerRemoteTitleStateChangeEvent(event, this.ohName, this.infoRemoteTitle);
	    }
	}
	
}
