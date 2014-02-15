/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pulseaudio.internal.items;

/**
 * GenericAudioItems are any kind of items that deal with audio data and can be
 * muted or their volume can be changed.
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public abstract class AbstractAudioDeviceConfig extends AbstractDeviceConfig {
	
	public enum State {
		SUSPENDED, IDLE, RUNNING, CORKED, DRAINED
	}

	protected State state;
	protected boolean muted;
	protected int volume;
	protected Module module;

	public AbstractAudioDeviceConfig(int id, String name, Module module) {
		super(id, name);
		this.module = module;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String toString() {
		return this.getClass().getSimpleName() + " #" + id + " (Module: "
				+ module + ") " + name + ", muted: " + muted + ", state: "
				+ state + ", volume: " + volume;
	}
	
}
