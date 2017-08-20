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
package eu.aleon.aleoncean.packet.smartackcommand;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.packet.ResponseReturnCode;
import eu.aleon.aleoncean.packet.SmartAckCode;
import eu.aleon.aleoncean.packet.SmartAckCommandPacket;
import eu.aleon.aleoncean.packet.response.NoDataResponse;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;
import eu.aleon.aleoncean.util.CalculationUtil;
import eu.aleon.aleoncean.values.LearnInOutMode;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class SaWrLearnConfirm extends SmartAckCommandPacket {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaWrLearnConfirm.class);

    private static final int RESPONSE_TIME_MIN = 0;
    private static final int RESPONSE_TIME_MAX = 0xFFFF;

    private int responseTime;

    private LearnInOutMode confirmCode;

    private final EnOceanId postmasterCandidateId = new EnOceanId();

    private final EnOceanId smartAckClientId = new EnOceanId();

    public SaWrLearnConfirm() {
        super(SmartAckCode.SA_WR_LEARNCONFIRM);
    }

    @Override
    public void setSmartAckData(final byte[] smartAckData) {
        final ByteBuffer bb = ByteBuffer.wrap(smartAckData);
        bb.order(ByteOrder.BIG_ENDIAN);

        setResponseTime(((int) bb.getShort()) & 0xFFFF);

        final byte rawConfirmCode = bb.get();
        switch (rawConfirmCode) {
            case (byte) 0x00:
                setConfirmCode(LearnInOutMode.IN);
                break;
            case (byte) 0x20:
                setConfirmCode(LearnInOutMode.OUT);
                break;
            default:
                LOGGER.warn(String.format("The received value (0x%02X) for the smart ack confirm code (learn modus) is invalid.", rawConfirmCode));
                break;
        }

        final byte[] enOceanId = new byte[EnOceanId.LENGTH];

        bb.get(enOceanId);
        setPostmasterCandidateId(new EnOceanId(enOceanId));

        bb.get(enOceanId);
        setSmartAckClientId(new EnOceanId(enOceanId));
    }

    @Override
    public byte[] getSmartAckData() {
        final int rawSmartAckDataLength = 2 /* response time */
                                          + 1 /* confirm code */
                                          + EnOceanId.LENGTH /* postmaster candidate id */
                                          + EnOceanId.LENGTH /* smart ack client id */;

        final byte[] rawSmartAckData = new byte[rawSmartAckDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawSmartAckData);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.putShort((short) getResponseTime());

        final byte rawConfirmCode;
        switch (getConfirmCode()) {
            case IN:
                rawConfirmCode = (byte) 0x00;
                break;
            case OUT:
                rawConfirmCode = (byte) 0x20;
                break;
            default:
                rawConfirmCode = (byte) 0x00; /* Use IN as fallback. */

                LOGGER.warn("Invalid value returned from getConfirmCode().");
                break;
        }
        bb.put(rawConfirmCode);

        bb.put(getPostmasterCandidateId().getBytes());

        bb.put(getSmartAckClientId().getBytes());

        return rawSmartAckData;
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

    public int getResponseTime() {
        return CalculationUtil.fitInRange(responseTime, RESPONSE_TIME_MIN, RESPONSE_TIME_MAX);
    }

    /**
     * Set the response time for sensor in ms.
     *
     * The response time defines the time, the controller can prepare the data and send it to the postmaster.
     * This is only actual, if learn return code is learn in.
     * The time have to be fit in range from RESPONSE_TIME_MIN to RESPONSE_TIME_MAX.
     *
     * @param responseTime The response time that should be set.
     */
    public void setResponseTime(final int responseTime) {
        this.responseTime = CalculationUtil.fitInRange(responseTime, RESPONSE_TIME_MIN, RESPONSE_TIME_MAX);
    }

    public LearnInOutMode getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(final LearnInOutMode confirmCode) {
        this.confirmCode = confirmCode;
    }

    public EnOceanId getPostmasterCandidateId() {
        return postmasterCandidateId;
    }

    public void setPostmasterCandidateId(final EnOceanId postmasterCandidateId) {
        this.postmasterCandidateId.fill(postmasterCandidateId);
    }

    public EnOceanId getSmartAckClientId() {
        return smartAckClientId;
    }

    public void setSmartAckClientId(final EnOceanId smartAckClientId) {
        this.smartAckClientId.fill(smartAckClientId);
    }

}
