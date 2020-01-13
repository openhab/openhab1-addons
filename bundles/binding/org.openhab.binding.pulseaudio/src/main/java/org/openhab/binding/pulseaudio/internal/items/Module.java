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
 * In order to add a {@link Sink} to the pulseaudio server you have to
 * load a corresponding module. Current Module objects are needed to
 * be able to remove sinks from the pulseaudio server.
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class Module extends AbstractDeviceConfig {

    private String argument;

    public Module(int id, String name) {
        super(id, name);
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    @Override
    public String toString() {
        return name;
    }

}
