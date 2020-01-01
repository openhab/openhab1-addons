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
package org.openhab.binding.plcbus.internal.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Baseclass for Frames in PLCBus Protocol
 *
 * @author Robin Lenz
 * @since 1.1.0
 *
 * @param <TDataFrame> Type of the containing DataFrame
 */
public abstract class Frame<TDataFrame extends DataFrame> {

    final static byte START_BYTE = 0x02;

    protected TDataFrame data;

    public Frame() {

    }

    public Frame(TDataFrame data) {
        this.data = data;
    }

    public byte getStartByte() {
        return START_BYTE;
    }

    public abstract byte getEndByte();

    public int getLength() {
        if (data == null) {
            return 0;
        }

        return data.getBytes().size();

    }

    public TDataFrame getData() {
        return data;
    }

    public List<Byte> getBytes() {
        List<Byte> result = new ArrayList<Byte>();

        result.add(getStartByte());
        result.add(Convert.toByte(getLength()));

        if (data != null) {
            result.addAll(data.getBytes());
        }

        result.add(getEndByte());

        return result;
    }

    public Command getCommand() {
        return data.getCommand();
    }

}
