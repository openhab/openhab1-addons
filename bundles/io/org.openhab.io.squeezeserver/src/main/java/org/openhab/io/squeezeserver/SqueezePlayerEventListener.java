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
 * @author Markus Wolters
 * @author Ben Jones
 * @since 1.3.0
 */
public interface SqueezePlayerEventListener {
	
	 void powerChangeEvent(PlayerEvent event);
	 void modeChangeEvent(PlayerEvent event);
	 void volumeChangeEvent(PlayerEvent event);
	 void muteChangeEvent(PlayerEvent event);

	 void titleChangeEvent(PlayerEvent event);
	 void albumChangeEvent(PlayerEvent event);	 
	 void artistChangeEvent(PlayerEvent event);	 
	 void coverArtChangeEvent(PlayerEvent event);		 
	 void yearChangeEvent(PlayerEvent event);
	 void genreChangeEvent(PlayerEvent event);
	 void remoteTitleChangeEvent(PlayerEvent event);
     
}
