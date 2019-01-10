/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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