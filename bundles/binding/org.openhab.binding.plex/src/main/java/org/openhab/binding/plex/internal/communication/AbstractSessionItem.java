/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal.communication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Abstract base class for media that is playing in a session. 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractSessionItem {
	
	@XmlAttribute
	private String key;
	
	@XmlAttribute
	private String sessionKey;
	
	@XmlAttribute
	private String type;
	
	@XmlAttribute
	private String title;
	
	@XmlAttribute
	private String grandparentTitle;

	@XmlAttribute
	private String duration;

	@XmlElement(name = "Player")
	private Player player;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGrandparentTitle() {
		return grandparentTitle;
	}

	public void setGrandparentTitle(String grandparentTitle) {
		this.grandparentTitle = grandparentTitle;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
