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
 * @author Markus Wolters
 * @author Ben Jones
 * @since 1.3.0
 */
public interface SqueezePlayerEventListener {
	
	 void powerChangeEvent(PlayerEvent event, String playerId, boolean isPowered);
	 void modeChangeEvent(PlayerEvent event, String playerId, Mode mode);
	 void volumeChangeEvent(PlayerEvent event, String playerId, int volume);
	 void muteChangeEvent(PlayerEvent event, String playerId, boolean isMuted);

	 void titleChangeEvent(PlayerEvent event, String playerId, String title);
	 void albumChangeEvent(PlayerEvent event, String playerId, String album);	 
	 void artistChangeEvent(PlayerEvent event, String playerId, String artist);	 
	 void artChangeEvent(PlayerEvent event, String playerId, String art);		 
	 void yearChangeEvent(PlayerEvent event, String playerId, String year);
	 void genreChangeEvent(PlayerEvent event, String playerId, String genre);
	 void remoteTitleChangeEvent(PlayerEvent event, String playerId, String title);
     
}
