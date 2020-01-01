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
 * A SourceOutput is the audio stream which is produced by a (@link Source}
 *
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class SourceOutput extends AbstractAudioDeviceConfig {

    private Source source;

    public SourceOutput(int id, String name, Module module) {
        super(id, name, module);
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

}
