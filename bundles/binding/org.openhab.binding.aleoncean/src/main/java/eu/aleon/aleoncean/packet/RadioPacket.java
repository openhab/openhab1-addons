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
import eu.aleon.aleoncean.packet.response.NoDataResponse;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;

/**
 * Representation of a RADIO packet.
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RadioPacket extends ESP3Packet {

    private static final int POS_CHOICE = DATA_START;

    private byte choice;

    private byte[] userData;

    private EnOceanId senderId = new EnOceanId();

    private byte status;

    private byte subTelNum = 3;

    private EnOceanId destinationId = EnOceanId.getBroadcast();

    private byte dBm = (byte) 0xFF;

    private byte securityLevel = 0;

    public RadioPacket() {
        this(RadioChoice.UNSET);
    }

    public RadioPacket(final byte choice) {
        super(PacketType.RADIO);
        setChoice(choice);
    }

    @Override
    public void setData(final byte[] data) {
        final int userDataLen = data.length - (1 /* choice */
                                               + EnOceanId.LENGTH
                                               + 1 /* status */);

        int pos = 0;

        choice = data[pos++];

        byte[] tmp;
        tmp = new byte[userDataLen];
        System.arraycopy(data, pos, tmp, 0, userDataLen);
        setUserDataRaw(tmp);
        pos += userDataLen;

        setSenderId(new EnOceanId(data, pos));
        pos += EnOceanId.LENGTH;

        status = data[pos++];
    }

    @Override
    public byte[] getData() {
        final byte[] rawUserData = getUserDataRaw();

        final int rawDataLength = 1 /* choice */
                                  + rawUserData.length
                                  + EnOceanId.LENGTH
                                  + 1 /* status */;

        final byte[] rawData = new byte[rawDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawData);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.put(getChoice());
        bb.put(rawUserData);
        bb.put(getSenderId().getBytes());
        bb.put(getStatus());

        return rawData;
    }

    @Override
    public void setOptionalData(final byte[] optionalData) {
        int pos = 0;

        subTelNum = optionalData[pos++];

        destinationId = new EnOceanId(optionalData, pos);
        pos += EnOceanId.LENGTH;

        dBm = optionalData[pos++];

        securityLevel = optionalData[pos++];
    }

    @Override
    public byte[] getOptionalData() {
        final int rawOptionalDataLength = 1 /* subTelNum */
                                          + EnOceanId.LENGTH /* destination Id */
                                          + 1 /* dBm */
                                          + 1 /* security level */;

        final byte[] rawOptionalData = new byte[rawOptionalDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawOptionalData);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.put(getSubTelNum());
        bb.put(getDestinationId().getBytes());
        bb.put(getdBm());
        bb.put(getSecurityLevel());

        return bb.array();
    }

    @Override
    public Response inspectResponsePacket(final ResponsePacket packet) throws UnknownResponseException {
        switch (packet.getReturnCode()) {
            case ResponseReturnCode.RET_OK:
            case ResponseReturnCode.RET_NOT_SUPPORTED:
            case ResponseReturnCode.RET_WRONG_PARAM:
                return new NoDataResponse();

            default:
                throw new UnknownResponseException();
        }
    }

    public byte[] getUserDataRaw() {
        return userData;
    }

    public void setUserDataRaw(final byte[] userData) {
        this.userData = userData;
    }

    public EnOceanId getSenderId() {
        return senderId;
    }

    public void setSenderId(final EnOceanId senderId) {
        this.senderId = senderId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(final byte status) {
        this.status = status;
    }

    public byte getChoice() {
        return choice;
    }

    public final void setChoice(final byte choice) {
        this.choice = choice;
    }

    public byte getSubTelNum() {
        return subTelNum;
    }

    public void setSubTelNum(final byte subTelNum) {
        this.subTelNum = subTelNum;
    }

    public EnOceanId getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(final EnOceanId destinationId) {
        this.destinationId = destinationId;
    }

    public byte getdBm() {
        return dBm;
    }

    public void setdBm(final byte dBm) {
        this.dBm = dBm;
    }

    public byte getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(final byte securityLevel) {
        this.securityLevel = securityLevel;
    }

    public static byte getChoice(final byte[] raw) {
        return raw[POS_CHOICE];
    }

    @Override
    public String toString() {
        final String str = String.format(
                "RadioPacket{%s, choice=0x%02X, senderId=%s, destinationId=%s, status=0x%02X, subTelNum=0x%02X, dBm=0x%02X, securityLevel=0x%02X}",
                super.toString(), getChoice(), getSenderId().toString(), getDestinationId().toString(), getStatus(), getSubTelNum(), getdBm(), getSecurityLevel()
        );
        return str;
    }

}
