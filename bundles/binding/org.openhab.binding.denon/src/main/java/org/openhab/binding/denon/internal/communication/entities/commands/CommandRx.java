/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.entities.commands;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Response to a {@link CommandTx}
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name="cmd")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandRx {
	
	private String zone1;
	
	private String zone2;
	
	private String zone3;
	
	private String zone4;
	
	private String volume;
	
	private String disptype;
	
	private String dispvalue;
	
	private String mute;
	
	private String type;
	
	@XmlElement(name="text")
	private List<Text> texts = new ArrayList<Text>();
	
	private String playstatus;
	
	private String playcontents;
	
	private String repeat;
	
	private String shuffle;
	
	private String source;
	
	public CommandRx() {
	}
	
	public String getZone1() {
		return zone1;
	}

	public void setZone1(String zone1) {
		this.zone1 = zone1;
	}

	public String getZone2() {
		return zone2;
	}

	public void setZone2(String zone2) {
		this.zone2 = zone2;
	}

	public String getZone3() {
		return zone3;
	}

	public void setZone3(String zone3) {
		this.zone3 = zone3;
	}

	public String getZone4() {
		return zone4;
	}

	public void setZone4(String zone4) {
		this.zone4 = zone4;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getDisptype() {
		return disptype;
	}

	public void setDisptype(String disptype) {
		this.disptype = disptype;
	}

	public String getDispvalue() {
		return dispvalue;
	}

	public void setDispvalue(String dispvalue) {
		this.dispvalue = dispvalue;
	}

	public String getMute() {
		return mute;
	}

	public void setMute(String mute) {
		this.mute = mute;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlaystatus() {
		return playstatus;
	}

	public void setPlaystatus(String playstatus) {
		this.playstatus = playstatus;
	}

	public String getPlaycontents() {
		return playcontents;
	}

	public void setPlaycontents(String playcontents) {
		this.playcontents = playcontents;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getShuffle() {
		return shuffle;
	}

	public void setShuffle(String shuffle) {
		this.shuffle = shuffle;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getText(String key) {
		for (Text text : texts) {
			if (text.getId().equals(key)) {
				return text.getValue();
			}
		}
		return null;
	}
}
