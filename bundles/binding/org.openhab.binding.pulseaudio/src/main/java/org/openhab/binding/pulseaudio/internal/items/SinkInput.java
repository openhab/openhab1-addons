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
 * A SinkInput is an audio stream which can be routed to a {@link Sink}
 *
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class SinkInput extends AbstractAudioDeviceConfig {

    private Sink sink;

    public SinkInput(int id, String name, Module module) {
        super(id, name, module);
    }

    public Sink getSink() {
        return sink;
    }

    public void setSink(Sink sink) {
        this.sink = sink;
    }

}
