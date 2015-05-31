/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhab.binding.denon.internal.communication.entities.types.OnOffType;
import org.openhab.binding.denon.internal.communication.entities.types.StringType;
import org.openhab.binding.denon.internal.communication.entities.types.VolumeType;

/**
 * Holds limited information about the secondary zones of the receiver
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZoneStatusLite {
	
	private OnOffType power; 

	private StringType inputFuncSelect;
	
	private StringType volumeDisplay;
	
	private VolumeType masterVolume;
	
	private OnOffType mute;

	public OnOffType getPower() {
		return power;
	}

	public void setPower(OnOffType power) {
		this.power = power;
	}

	public StringType getInputFuncSelect() {
		return inputFuncSelect;
	}

	public void setInputFuncSelect(StringType inputFuncSelect) {
		this.inputFuncSelect = inputFuncSelect;
	}

	public StringType getVolumeDisplay() {
		return volumeDisplay;
	}

	public void setVolumeDisplay(StringType volumeDisplay) {
		this.volumeDisplay = volumeDisplay;
	}

	public VolumeType getMasterVolume() {
		return masterVolume;
	}

	public void setMasterVolume(VolumeType masterVolume) {
		this.masterVolume = masterVolume;
	}

	public OnOffType getMute() {
		return mute;
	}

	public void setMute(OnOffType mute) {
		this.mute = mute;
	}
	
}
