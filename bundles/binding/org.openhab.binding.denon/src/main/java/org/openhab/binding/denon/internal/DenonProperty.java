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
package org.openhab.binding.denon.internal;

/**
 * Subset of the usable properties.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public enum DenonProperty {

    INPUT("INPUT"),
    SURROUND_MODE("SURROUNDMODE"),
    COMMAND("COMMAND"),
    MASTER_VOLUME("MV"),
    ZONE_VOLUME("ZV"),
    POWER("PW"),
    POWER_MAINZONE("ZM"),
    MUTE("MU"),
    ARTIST("ARTIST"),
    TRACK("TRACK"),
    ALBUM("ALBUM");

    private String code;

    private DenonProperty(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
