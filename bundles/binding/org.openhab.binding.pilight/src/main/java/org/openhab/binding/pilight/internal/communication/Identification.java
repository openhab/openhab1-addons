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
package org.openhab.binding.pilight.internal.communication;

/**
 * This object is sent to pilight right after the initial connection. It describes what kind of client we want to be.
 *
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Identification {

    public static String ACTION_IDENTIFY = "identify";

    private String action;

    private Options options;

    public Identification() {
        this.action = ACTION_IDENTIFY;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}
