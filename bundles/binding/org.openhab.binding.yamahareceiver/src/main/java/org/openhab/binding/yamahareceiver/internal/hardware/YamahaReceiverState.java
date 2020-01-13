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
package org.openhab.binding.yamahareceiver.internal.hardware;

/**
 * Receiver state
 *
 * @author Eric Thill
 * @since 1.6.0
 */
public class YamahaReceiverState {

    private final boolean power;
    private final String input;
    private final String surroundProgram;
    private final float volume;
    private final boolean mute;

    public YamahaReceiverState(boolean power, String input, String surroundProgram, float volume, boolean mute) {
        this.power = power;
        this.input = input;
        this.surroundProgram = surroundProgram;
        this.volume = volume;
        this.mute = mute;
    }

    public boolean isPower() {
        return power;
    }

    public String getInput() {
        return input;
    }

    public String getSurroundProgram() {
        return surroundProgram;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isMute() {
        return mute;
    }
}
