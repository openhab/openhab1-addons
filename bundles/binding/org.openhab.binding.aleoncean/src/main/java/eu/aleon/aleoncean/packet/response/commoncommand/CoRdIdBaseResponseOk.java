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
package eu.aleon.aleoncean.packet.response.commoncommand;

import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.response.Response;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class CoRdIdBaseResponseOk extends Response {

    private final EnOceanId baseId = new EnOceanId();

    private byte remainingWriteCycles;

    public CoRdIdBaseResponseOk() {

    }

    @Override
    public byte[] getResponseData() {
        return getBaseId().getBytes();
    }

    @Override
    public void setResponseData(final byte[] responseData) {
        setBaseId(new EnOceanId(responseData));
    }

    @Override
    public byte[] getOptionalData() {
        final byte[] optional = new byte[1];
        optional[0] = remainingWriteCycles;
        return optional;
    }

    @Override
    public void setOptionalData(final byte[] optionalData) {
        remainingWriteCycles = optionalData[0];
    }

    public EnOceanId getBaseId() {
        return baseId;
    }

    public void setBaseId(final EnOceanId id) {
        baseId.fill(id);
    }

    public byte getRemainingWriteCycles() {
        return remainingWriteCycles;
    }

    public void setRemainingWriteCycles(final byte remainingWriteCycles) {
        this.remainingWriteCycles = remainingWriteCycles;
    }

}
