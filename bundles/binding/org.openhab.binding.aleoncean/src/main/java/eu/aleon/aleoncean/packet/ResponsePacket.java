/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class ResponsePacket extends ESP3Packet {

    private byte returnCode;

    private byte[] responseData;

    public ResponsePacket() {
        super(PacketType.RESPONSE);
    }

    @Override
    public void setData(final byte[] data) {
        int pos = 0;

        returnCode = data[pos++];

        final int responseDataLen = data.length - pos;
        final byte[] tmp = new byte[responseDataLen];
        System.arraycopy(data, pos, tmp, 0, responseDataLen);
        setResponseData(tmp);
        //pos += responseDataLen;
    }

    @Override
    public byte[] getData() {
        final byte[] rawResponseData = getResponseData();

        final int rawDataLength = 1 /* return code */
                                  + rawResponseData.length;

        final byte[] rawData = new byte[rawDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawData);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.put(getReturnCode());
        bb.put(rawResponseData);

        return rawData;

    }

    public byte getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(final byte returnCode) {
        this.returnCode = returnCode;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public void setResponseData(final byte[] responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        final String str = String.format(
                "ResponsePacket{%s, returnCode=0x%02X, responseData=%s}",
                super.toString(), getReturnCode(), Arrays.toString(getResponseData())
        );
        return str;
    }

}
