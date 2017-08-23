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

import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.packet.ResponseReturnCode;
import eu.aleon.aleoncean.packet.SmartAckCode;
import eu.aleon.aleoncean.packet.SmartAckCommandPacket;
import eu.aleon.aleoncean.packet.response.NoDataResponse;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;
import eu.aleon.aleoncean.packet.response.smartackcommand.SaRdLearnModeResponseOk;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class SaRdLearnMode extends SmartAckCommandPacket {

    public SaRdLearnMode() {
        super(SmartAckCode.SA_RD_LEARNMODE);
    }

    @Override
    public Response inspectResponsePacket(final ResponsePacket packet) throws UnknownResponseException {
        switch (packet.getReturnCode()) {
            case ResponseReturnCode.RET_OK:
                final SaRdLearnModeResponseOk response = new SaRdLearnModeResponseOk();
                response.fromResponsePacket(packet);
                return response;

            case ResponseReturnCode.RET_NOT_SUPPORTED:
                return new NoDataResponse();

            default:
                throw new UnknownResponseException();
        }
    }
}
