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
package org.openhab.binding.tinkerforge.internal.tools;

public class LinePositionText {
    private short line;
    private short position;
    private String text;

    public LinePositionText(short line, short position, String text) {
        this.line = line;
        this.position = position;
        this.text = text;
    }

    public short getLine() {
        return line;
    }

    public short getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }
}