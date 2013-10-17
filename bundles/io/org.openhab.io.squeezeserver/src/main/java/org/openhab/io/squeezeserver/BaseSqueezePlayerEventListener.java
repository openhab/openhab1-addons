/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.squeezeserver;

import org.openhab.io.squeezeserver.SqueezePlayer.Mode;
import org.openhab.io.squeezeserver.SqueezePlayer.PlayerEvent;

/**
 * @author Ben Jones
 * @since 1.4.0
 */
public class BaseSqueezePlayerEventListener implements SqueezePlayerEventListener {

	@Override
	public void powerChangeEvent(PlayerEvent event,
			String playerId, boolean isPowered) {		
	}

	@Override
	public void modeChangeEvent(PlayerEvent event,
			String playerId, Mode mode) {
	}

	@Override
	public void volumeChangeEvent(PlayerEvent event,
			String playerId, int volume) {
	}

	@Override
	public void muteChangeEvent(PlayerEvent event,
			String playerId, boolean isMuted) {
	}

	@Override
	public void titleChangeEvent(PlayerEvent event,
			String playerId, String title) {
	}

	@Override
	public void albumChangeEvent(PlayerEvent event,
			String playerId, String album) {
	}

	@Override
	public void artistChangeEvent(PlayerEvent event,
			String playerId, String artist) {
	}

	@Override
	public void artChangeEvent(PlayerEvent event,
			String playerId, String art) {
	}

	@Override
	public void yearChangeEvent(PlayerEvent event,
			String playerId, String year) {
	}

	@Override
	public void genreChangeEvent(PlayerEvent event,
			String playerId, String genre) {
	}

	@Override
	public void remoteTitleChangeEvent(PlayerEvent event,
			String playerId, String title) {
	}
}
