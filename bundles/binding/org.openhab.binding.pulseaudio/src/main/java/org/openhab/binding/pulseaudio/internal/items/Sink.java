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

import java.util.ArrayList;
import java.util.List;

/**
 * On a Pulseaudio server Sinks are the devices the audio streams are routed to
 * (playback devices) it can be a single item or a group of other Sinks that are
 * combined to one playback device
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class Sink extends AbstractAudioDeviceConfig {
	
	protected List<String> combinedSinkNames;
	protected List<Sink> combinedSinks;

	public Sink(int id, String name, Module module) {
		super(id, name, module);
		combinedSinkNames = new ArrayList<String>();
		combinedSinks = new ArrayList<Sink>();
	}
	
	public void addCombinedSinkName(String name) {
		this.combinedSinkNames.add(name);
	}
	
	public boolean isCombinedSink() {
		return combinedSinkNames.size()>0;
	}
	
	public List<String> getCombinedSinkNames() {
		return combinedSinkNames;
	}
	
	public List<Sink> getCombinedSinks() {
		return combinedSinks;
	}
	
	public void setCombinedSinks(List<Sink> combinedSinks) {
		this.combinedSinks = combinedSinks;
	}
	
	public void addCombinedSink(Sink sink) {
		if (sink!=null)
			this.combinedSinks.add(sink);
	}
	
}
