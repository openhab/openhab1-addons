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

import eu.aleon.aleoncean.packet.CommonCommandCode;
import eu.aleon.aleoncean.packet.CommonCommandPacket;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.packet.ResponseReturnCode;
import eu.aleon.aleoncean.packet.response.NoDataResponse;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class CoWrIdBase extends CommonCommandPacket {

    private final EnOceanId baseId = new EnOceanId();

    public CoWrIdBase() {
        super(CommonCommandCode.CO_WR_IDBASE);
    }

    @Override
    public void setCommonCommandData(final byte[] commonCommandData) {

        super.setCommonCommandData(commonCommandData); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getCommonCommandData() {
        assert getBaseId().isBaseId();
        return getBaseId().getBytes();
    }

    @Override
    public Response inspectResponsePacket(final ResponsePacket packet) throws UnknownResponseException {
        switch (packet.getReturnCode()) {
            case ResponseReturnCode.RET_OK:
            case ResponseReturnCode.RET_NOT_SUPPORTED:
            case ResponseReturnCode.FLASH_HW_ERROR:
            case ResponseReturnCode.BASEID_OUT_OF_RANGE:
            case ResponseReturnCode.BASEID_MAX_REACHED:
                return new NoDataResponse();

            default:
                throw new UnknownResponseException();
        }
    }

    public EnOceanId getBaseId() {
        return baseId;
    }

    public void setBaseId(final EnOceanId baseId) {
        this.baseId.fill(baseId);
    }
}
