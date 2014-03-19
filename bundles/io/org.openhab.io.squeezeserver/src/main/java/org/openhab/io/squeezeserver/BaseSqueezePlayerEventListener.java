/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.squeezeserver;

import org.openhab.io.squeezeserver.SqueezePlayer.PlayerEvent;

/**
 * @author Ben Jones
 * @since 1.4.0
 */
public class BaseSqueezePlayerEventListener implements SqueezePlayerEventListener {

	@Override
	public void powerChangeEvent(PlayerEvent event) {		
	}

	@Override
	public void modeChangeEvent(PlayerEvent event) {
	}

	@Override
	public void volumeChangeEvent(PlayerEvent event) {
	}

	@Override
	public void muteChangeEvent(PlayerEvent event) {
	}

	@Override
	public void titleChangeEvent(PlayerEvent event) {
	}

	@Override
	public void albumChangeEvent(PlayerEvent event) {
	}

	@Override
	public void artistChangeEvent(PlayerEvent event) {
	}

	@Override
	public void coverArtChangeEvent(PlayerEvent event) {
	}

	@Override
	public void yearChangeEvent(PlayerEvent event) {
	}

	@Override
	public void genreChangeEvent(PlayerEvent event) {
	}

	@Override
	public void remoteTitleChangeEvent(PlayerEvent event) {
	}
	
	@Override
	public void irCodeChangeEvent(PlayerEvent event) {
	}
}
