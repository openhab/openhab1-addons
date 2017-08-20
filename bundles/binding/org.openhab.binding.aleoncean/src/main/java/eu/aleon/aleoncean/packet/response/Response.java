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
package eu.aleon.aleoncean.packet.response;

import eu.aleon.aleoncean.packet.ResponsePacket;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class Response {

    public void fromResponsePacket(final ResponsePacket packet) {
        setResponseData(packet.getResponseData());
        setOptionalData(packet.getOptionalData());
    }

    public void fillResponsePacket(final ResponsePacket packet) {
        packet.setResponseData(getResponseData());
        packet.setOptionalData(getOptionalData());
    }

    public abstract byte[] getResponseData();

    public abstract void setResponseData(final byte[] responseData);

    public abstract byte[] getOptionalData();

    public abstract void setOptionalData(final byte[] optionalData);

}
