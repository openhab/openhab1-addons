/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

/**
* Properties used by the binding 
* 
* @author Jeroen Idserda
* @since 1.7.0
*/
public enum PlexProperty {
	
	STATE("state"),
	POWER("power"),
	TYPE("type"),
	TITLE("title"),
	PROGRESS("playback/progress"),
	END_TIME("playback/endTime"),
	PLAY("playback/play"),
	PAUSE("playback/pause"),
	STOP("playback/stop"),
	STEP_BACK("playback/stepBack"),
	STEP_FORWARD("playback/stepForward"),
	VOLUME("playback/volume");
	
	private String name;
	
	private PlexProperty(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
