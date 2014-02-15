/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
