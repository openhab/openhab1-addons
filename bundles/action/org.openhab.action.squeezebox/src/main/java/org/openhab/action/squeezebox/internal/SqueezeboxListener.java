/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.squeezebox.internal;

import java.util.concurrent.atomic.AtomicBoolean;

import org.openhab.io.squeezeserver.BaseSqueezePlayerEventListener;
import org.openhab.io.squeezeserver.SqueezePlayer.PlayerEvent;

/**
 * This class is used by the Squeezebox action to detect when a sentence has 
 * finished so it can restore the player state.
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public class SqueezeboxListener extends BaseSqueezePlayerEventListener {

	private final String playerId;
	private final String url;
	
	private final AtomicBoolean started = new AtomicBoolean(false);
	private final AtomicBoolean finished = new AtomicBoolean(false);
	
	public SqueezeboxListener(String playerId, String url) {
		this.playerId = playerId;
		this.url = url;
	}
	
	@Override
	public void titleChangeEvent(PlayerEvent event) {
		if (!this.playerId.equals(event.getPlayerId()))
			return;
		
		if (this.url.equals(event.getPlayer().getTitle()))
			this.started.set(true);
	}

	@Override
	public void modeChangeEvent(PlayerEvent event) {
		if (!this.playerId.equals(event.getPlayerId()))
			return;
		
		if (this.started.get() && event.getPlayer().isStopped())
			this.finished.set(true);
	}
	
	public boolean isFinished() {
		return this.finished.get();
	}
}	
