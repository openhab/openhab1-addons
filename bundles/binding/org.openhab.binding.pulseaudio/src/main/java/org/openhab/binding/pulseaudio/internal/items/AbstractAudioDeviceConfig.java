/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
