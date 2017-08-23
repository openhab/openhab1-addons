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
package eu.aleon.aleoncean.packet.commoncommand;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import eu.aleon.aleoncean.packet.CommonCommandCode;
import eu.aleon.aleoncean.packet.CommonCommandPacket;
import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.packet.ResponseReturnCode;
import eu.aleon.aleoncean.packet.response.NoDataResponse;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;
import eu.aleon.aleoncean.util.CalculationUtil;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class CoWrSleepPacket extends CommonCommandPacket {

    private static final int DEEP_SLEEP_PERIOD_RAW_VALUE_MIN = 0;
    private static final int DEEP_SLEEP_PERIOD_RAW_VALUE_MAX = 0x00FFFFFF;

    public static final int DEEP_SLEEP_PERIOD_DEFAULT_MAX_VALUE = 0;

    private int deepSleepPeriod;

    public CoWrSleepPacket() {
        super(CommonCommandCode.CO_WR_SLEEP);
    }

    @Override
    public void setCommonCommandData(final byte[] commonCommandData) {
        final ByteBuffer bb = ByteBuffer.wrap(commonCommandData);
        bb.order(ByteOrder.BIG_ENDIAN);

        setDeepSleepPeriod(bb.getInt());
    }

    @Override
    public byte[] getCommonCommandData() {
        final int rawCommonCommandDataLength = 4 /* deep sleep period */;

        final byte[] rawCommonCommandData = new byte[rawCommonCommandDataLength];
        final ByteBuffer bb = ByteBuffer.wrap(rawCommonCommandData);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.putInt(getDeepSleepPeriod());

        return rawCommonCommandData;
    }

    @Override
    public Response inspectResponsePacket(final ResponsePacket packet) throws UnknownResponseException {
        switch (packet.getReturnCode()) {
            case ResponseReturnCode.RET_OK:
            case ResponseReturnCode.RET_NOT_SUPPORTED:
                return new NoDataResponse();

            default:
                throw new UnknownResponseException();
        }
    }

    public int getDeepSleepPeriod() {
        return deepSleepPeriod;
    }

    public void setDeepSleepPeriod(final int deepSleepPeriod) {
        this.deepSleepPeriod = CalculationUtil.fitInRange(deepSleepPeriod, DEEP_SLEEP_PERIOD_RAW_VALUE_MIN, DEEP_SLEEP_PERIOD_RAW_VALUE_MAX);
    }

}
