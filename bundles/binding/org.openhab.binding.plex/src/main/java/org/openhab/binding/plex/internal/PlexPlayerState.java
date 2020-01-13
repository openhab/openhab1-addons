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
package org.openhab.binding.plex.internal;

/**
 * The different states for the Plex clients
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public enum PlexPlayerState {

    Stopped,
    Buffering,
    Playing,
    Paused;

    public static PlexPlayerState of(String state) {
        for (PlexPlayerState playerState : values()) {
            if (playerState.toString().toLowerCase().equals(state)) {
                return playerState;
            }
        }

        return null;
    }
}
