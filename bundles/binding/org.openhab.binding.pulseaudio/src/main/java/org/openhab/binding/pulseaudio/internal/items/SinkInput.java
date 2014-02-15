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
 * A SinkInput is an audio stream which can be routed to a {@link Sink}
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class SinkInput extends AbstractAudioDeviceConfig {
	
	private Sink sink;

	public SinkInput(int id, String name, Module module) {
		super(id, name, module);
	}

	public Sink getSink() {
		return sink;
	}

	public void setSink(Sink sink) {
		this.sink = sink;
	}

}
