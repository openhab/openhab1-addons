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
package org.openhab.binding.pulseaudio.internal.items;

/**
 * A Source is a device which is the source of an audio stream (recording
 * device) For example microphones or line-in jacks.
 *
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class Source extends AbstractAudioDeviceConfig {

    protected Sink monitorOf;

    public Source(int id, String name, Module module) {
        super(id, name, module);
    }

    public Sink getMonitorOf() {
        return monitorOf;
    }

    public void setMonitorOf(Sink sink) {
        this.monitorOf = sink;
    }

}
