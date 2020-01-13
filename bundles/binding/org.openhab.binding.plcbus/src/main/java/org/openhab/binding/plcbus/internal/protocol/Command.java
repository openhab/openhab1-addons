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
 * Base Class for a PLC Bus Command
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public abstract class Command {

    private byte data1;
    private byte data2;

    /**
     * Returns the Id of the command
     * 
     * @return Id of command
     */
    public abstract byte getId();

    /**
     * Returns the first DataByte of Command
     * 
     * @return First DataByte of Command
     */
    protected byte getData1() {
        return data1;
    }

    /**
     * Sets the first DataByte
     */
    protected void setData1(byte data) {
        this.data1 = data;
    }

    /**
     * Returns the second DataByte of Command
     * 
     * @return Second DataByte of Command
     */
    protected byte getData2() {
        return data2;
    }

    /**
     * Sets the second DataByte
     */
    protected void setData2(byte data) {
        this.data2 = data;
    }

    /**
     * Return the DataBytes of the command
     * 
     * @return DataBytes
     */
    public List<Byte> getDataBytes() {
        List<Byte> result = new ArrayList<Byte>();

        result.add(data1);
        result.add(data2);

        return result;
    }

    /**
     * Parse the bytes
     * 
     * @param data
     *            Data to parses
     */
    public void parse(byte[] data) {
        data1 = data[0];
        data2 = data[1];
    }

}
