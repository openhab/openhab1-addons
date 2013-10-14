/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.squeezeserver;

import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer.PlayerEvent;
import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer.STATES;

/**
 * @author Markus Wolters
 * @since 1.3.0
 */
public interface SqueezePlayerEventListener {
	
	 void onSqueezePlayerMuteStateChangeEvent(PlayerEvent event, String id, STATES isMuted);
	 void onSqueezePlayerPlayStateChangeEvent(PlayerEvent event, String id, STATES isPlaying);
	 void onSqueezePlayerPauseStateChangeEvent(PlayerEvent event, String id, STATES isPaused);
	 void onSqueezePlayerStopStateChangeEvent(PlayerEvent event, String id, STATES isStopped);
	 void onSqueezePlayerPowerStateChangeEvent(PlayerEvent event, String id, STATES isPowered);
	 void onSqueezePlayerVolumeChangeEvent(PlayerEvent event, String id, byte volume);
	 void onSqueezePlayerTitleChangeEvent(PlayerEvent event, String id, String title);
	 void onSqueezePlayerAlbumStateChangeEvent(PlayerEvent event, String id, String album);	 
	 void onSqueezePlayerArtistStateChangeEvent(PlayerEvent event, String id, String artist);	 
	 void onSqueezePlayerArtStateChangeEvent(PlayerEvent event, String id, String art);		 
	 void onSqueezePlayerYearStateChangeEvent(PlayerEvent event, String id, String year);
	 void onSqueezePlayerGenreStateChangeEvent(PlayerEvent event, String id, String genre);
	 void onSqueezePlayerRemoteTitleStateChangeEvent(PlayerEvent event, String id, String title);
	 
}
