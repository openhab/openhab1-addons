/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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

    void currentPlaylistIndexEvent(PlayerEvent event);

    void currentPlayingTimeEvent(PlayerEvent event);

    void numberPlaylistTracksEvent(PlayerEvent event);

    void currentPlaylistShuffleEvent(PlayerEvent event);

    void currentPlaylistRepeatEvent(PlayerEvent event);

    void titleChangeEvent(PlayerEvent event);

    void albumChangeEvent(PlayerEvent event);

    void artistChangeEvent(PlayerEvent event);

    void coverArtChangeEvent(PlayerEvent event);

    void yearChangeEvent(PlayerEvent event);

    void genreChangeEvent(PlayerEvent event);

    void remoteTitleChangeEvent(PlayerEvent event);

    void irCodeChangeEvent(PlayerEvent event);
}
