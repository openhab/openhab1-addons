/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.velux.bridge.slip.util;

import java.util.Arrays;

/**
 * Utility class for handling of packets i.e. array of bytes.
 *
 * <P>
 * Methods available:
 * <UL>
 * <LI>{@link #Packet(byte[])} creates a Packet object based on the array of bytes.</LI>
 * <LI>{@link #length()} returns the number of bytes contained within this Packet.</LI>
 * <LI>{@link #toByteArray} returns the complete Packet as array of bytes.</LI>
 * <LI>{@link #getByteArray} returns the a range of bytes as array of bytes.</LI>
 * <LI>{@link #toString} returns the complete packet as human-readable String.</LI>
 * <LI>{@link #getOneByteValue} returns the value of one byte as int.</LI>
 * <LI>{@link #setOneByteValue} sets the value of one byte within the Packet.</LI>
 * <LI>{@link #getTwoByteValue} returns the value of two bytes as int.</LI>
 * <LI>{@link #setTwoByteValue} sets the value of two bytes within the Packet.</LI>
 * <LI>{@link #getFourByteValue(int)} returns the value of four bytes as int.</LI>
 * <LI>{@link #setFourByteValue} sets the value of four bytes within the Packet.</LI>
 * <LI>{@link #getString} returns the value of a range of bytes as String.</LI>
 * <LI>{@link #setString} sets the value of a range of bytes within the Packet.</LI>
 * </UL>
 * Static methods are:
 * <UL>
 * <LI>{@link #shortToString} converts a byte into a human-readable hex-String.</LI>
 * <LI>{@link #byteArrayToInt} converts a range of four bytes into an int.</LI>
 * <LI>{@link #intToIPAddressString} converts an int into a String.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class Packet {

    /*
     * ===========================================================
     * Internal Objects
     */

    private byte[] data;

    /*
     * ===========================================================
     * Constructor Method
     */

    /**
     * Constructor: Create a {@link org.openhab.binding.velux.bridge.slip.util.Packet Packet} out of a sequence of
     * bytes.
     *
     * @param thisData Packet as Array of bytes.
     */
    public Packet(byte[] thisData) {
        this.data = thisData;
    }

    /*
     * ===========================================================
     * Access Methods
     */

    /**
     * Returns the length of the {@link org.openhab.binding.velux.bridge.slip.util.Packet Packet}..
     *
     * @return <b>packetLength</b>
     *         of type int.
     */
    public int length() {
        return data.length;
    }

    /**
     * Returns the complete {@link org.openhab.binding.velux.bridge.slip.util.Packet Packet} as sequence of bytes.
     *
     * @return <b>packet</b>
     *         of type Array-of-byte.
     */
    public byte[] toByteArray() {
        return data;
    }

    /**
     * Returns a part of the {@link org.openhab.binding.velux.bridge.slip.util.Packet Packet} as sequence of bytes
     * starting at position (n) up to the position (n+length-1).
     *
     * @param position Position (n) within the packet.
     * @param length Length of the intended slice as int.
     * @return <b>packet</b> of type Array-of-byte.
     */
    public byte[] getByteArray(int position, int length) {
        return Arrays.copyOfRange(data, position, position + length);
    }

    /**
     * Returns the complete {@link org.openhab.binding.velux.bridge.slip.util.Packet Packet}
     * as human-readable sequence of hex bytes each separated by the given separator.
     *
     * @param separator as of Type String.
     * @return <b>packetString</b> of type String.
     */
    public String toString(String separator) {
        StringBuilder sb = new StringBuilder();
        for (byte b : this.data) {
            sb.append(String.format("%02X", b));
            sb.append(separator);
        }
        if (sb.lastIndexOf(separator) > 0) {
            sb.deleteCharAt(sb.lastIndexOf(separator));
        }
        return (sb.toString());
    }

    /**
     * Returns the complete {@link org.openhab.binding.velux.bridge.slip.util.Packet Packet}
     * as human-readable sequence of hex bytes each separated by a blank.
     *
     * @return <b>packetString</b> of type String.
     */
    @Override
    public String toString() {
        return this.toString(" ");
    }

    /**
     * Returns the value of the byte at (n)th position as int value for convenience.
     *
     * @param position Position (n) within the packet.
     * @return <b>value</b> of type int.
     */
    public int getOneByteValue(int position) {
        return (data[position] & 0xff);
    }

    /**
     * Modifies the value of the byte at (n)th position by setting it to the value passed as int.
     *
     * @param position Position (n) within the packet.
     * @param value of type int.
     */
    public void setOneByteValue(int position, int value) {
        data[position] = (byte) value;
    }

    /**
     * Returns the value of the bytes at the positions (n)th and (n+1) as int value for convenience.
     * <P>
     * Note: Big-endian LSB-0 encoding.
     * </P>
     *
     * @param position Position (n) within the packet.
     * @return <b>value</b> of type int.
     */
    public int getTwoByteValue(int position) {
        return 0x00 << 24 | 0x00 << 16 | (data[position] & 0xff) << 8 | (data[position + 1] & 0xff);
    }

    /**
     * Modifies the value of the bytes at the positions (n) and (n+1) by setting it to the value passed as int.
     * <P>
     * Note: Big-endian LSB-0 encoding.
     * </P>
     *
     * @param position Position (n) within the packet.
     * @param value of type int.
     */
    public void setTwoByteValue(int position, int value) {
        data[position] = (byte) ((value >>> 8) & 0xFF);
        data[position + 1] = (byte) (value & 0xFF);
    }

    /**
     * Returns the value of the bytes at the positions (n)th to (n+3) as int value for convenience.
     * <P>
     * Note: Big-endian LSB-0 encoding.
     * </P>
     *
     * @param position Position (n) within the packet.
     * @return <b>value</b> of type int.
     */
    public int getFourByteValue(int position) {
        return data[position] << 24 | (data[position + 1] & 0xff) << 16 | (data[position + 2] & 0xff) << 8
                | (data[position + 3] & 0xff);
    }

    /**
     * Modifies the value of the bytes at the positions (n) to (n+3) by setting it to the value passed as int.
     * <P>
     * Note: Big-endian LSB-0 encoding.
     * </P>
     *
     * @param position Position (n) within the packet.
     * @param value of type int.
     */
    public void setFourByteValue(int position, int value) {
        data[position] = (byte) ((value >>> 24) & 0xFF);
        data[position + 1] = (byte) ((value >>> 16) & 0xFF);
        data[position + 2] = (byte) ((value >>> 8) & 0xFF);
        data[position + 3] = (byte) (value & 0xFF);
    }

    /**
     * Returns the char string converted byte-by-byte starting at the position (n) up to (n+length+1).
     * <P>
     * Note: Any trailing null char will be eliminated.
     * </P>
     *
     * @param position Position (n) within the packet.
     * @param length Length of the intended slice as int.
     * @return <b>value</b> of type String.
     */
    public String getString(int position, int length) {
        return new String(Arrays.copyOfRange(data, position, position + length - 1)).replace("\0", "");
    }

    /**
     * Modifies the value of the bytes starting at the position (n) by setting it to the character values passed as
     * String.
     * <P>
     * Note: The trailing null char will not be stored.
     * </P>
     *
     * @param position Position (n) within the packet.
     * @param text of type String.
     */
    public void setString(int position, String text) {
        System.arraycopy(text, 0, data, 0, text.length());
    }

    /*
     * ===========================================================
     * Conversion Methods
     */

    /**
     * Returns the hex char string representing the byte.
     *
     * @param oneByte of type byte to be converted.
     * @return <b>hexByteString</b> of type String.
     */
    public static String shortToString(int oneByte) {
        return String.format("%02X", oneByte);
    }

    public static int byteArrayToInt(byte[] data) {
        return data[0] << 24 | (data[1] & 0xff) << 16 | (data[2] & 0xff) << 8 | (data[3] & 0xff);
    }

    /**
     * Returns the dotted string representing an IP address.
     *
     * @param ipAddress of type int to be converted.
     * @return <b>ipAddressString</b> of type String.
     */
    public static String intToIPAddressString(int ipAddress) {
        return String.format("%d.%d.%d.%d", ((ipAddress >>> 24) & 0xFF), ((ipAddress >>> 16) & 0xFF),
                ((ipAddress >>> 8) & 0xFF), (ipAddress & 0xFF));
    }

}
