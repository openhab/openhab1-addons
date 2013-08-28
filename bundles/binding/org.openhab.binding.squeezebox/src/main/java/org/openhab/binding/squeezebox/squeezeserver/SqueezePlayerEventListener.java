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
