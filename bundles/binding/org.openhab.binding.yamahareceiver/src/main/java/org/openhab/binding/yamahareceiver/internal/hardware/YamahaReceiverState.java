/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahareceiver.internal.hardware;

/**
 * Receiver state
 * 
 * @author Eric Thill
 * @since 1.6.0
 */
public class YamahaReceiverState {
	
	private final boolean power;
	private final String input;
	private final String surroundProgram;
	private final float volume;
	private final boolean mute;
	
	public YamahaReceiverState(boolean power, String input, String surroundProgram, float volume, boolean mute) {
		this.power = power;
		this.input = input;
		this.surroundProgram = surroundProgram;
		this.volume = volume;
		this.mute = mute;
	}
	
	public boolean isPower() {
		return power;
	}
	
	public String getInput() {
		return input;
	}
	
	public String getSurroundProgram() {
		return surroundProgram;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public boolean isMute() {
		return mute;
	}
}
