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
    COVER("playback/cover"),
    PLAY("playback/play"),
    PAUSE("playback/pause"),
    PLAYPAUSE("playback/playpause"),
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
