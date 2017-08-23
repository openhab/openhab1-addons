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
package eu.aleon.aleoncean.packet.radio.userdata;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataUTEResponse extends UserDataUTE {

    private static final byte REQUESTTYPE_NOT_ACCEPTED_GENERAL_REASON = (byte) 0;
    private static final byte REQUESTTYPE_ACCEPTED_TEACH_IN = (byte) 1;
    private static final byte REQUESTTYPE_ACCEPTED_DELETION = (byte) 2;
    private static final byte REQUESTTYPE_NOT_ACCEPTED_EEP_UNSUPPORTED = (byte) 3;

    public enum ResponseType {

        NOT_ACCEPTED_GENERAL_REASON(REQUESTTYPE_NOT_ACCEPTED_GENERAL_REASON),
        ACCEPTED_TEACH_IN(REQUESTTYPE_ACCEPTED_TEACH_IN),
        ACCEPTED_DELETION(REQUESTTYPE_ACCEPTED_DELETION),
        NOT_ACCEPTED_EEP_UNSUPPORTED(REQUESTTYPE_NOT_ACCEPTED_EEP_UNSUPPORTED);

        private final byte responseType;

        private ResponseType(final byte responseType) {
            this.responseType = responseType;
        }

        public byte toByte() {
            return responseType;
        }

        public static ResponseType fromByte(final byte responseType) {
            switch (responseType) {
                case REQUESTTYPE_NOT_ACCEPTED_GENERAL_REASON:
                    return NOT_ACCEPTED_GENERAL_REASON;
                case REQUESTTYPE_ACCEPTED_TEACH_IN:
                    return ACCEPTED_TEACH_IN;
                case REQUESTTYPE_ACCEPTED_DELETION:
                    return ACCEPTED_DELETION;
                case REQUESTTYPE_NOT_ACCEPTED_EEP_UNSUPPORTED:
                    return NOT_ACCEPTED_EEP_UNSUPPORTED;
                default:
                    return NOT_ACCEPTED_GENERAL_REASON;
            }
        }
    }

    public UserDataUTEResponse() {
        super();
        setCmd(Command.RESPONSE);
    }

    public UserDataUTEResponse(final byte[] data) {
        super(data);
        setCmd(Command.RESPONSE);
    }

    public UserDataUTEResponse(final UserDataUTEQuery query) {
        this();
        setChoice(query.getChoice());
        setFunc(query.getFunc());
        setType(query.getType());
        setManufacturerId(query.getManufacturerId());
        setNumOfChannels(query.getNumOfChannels());
        setBidirectionalCommunication(query.getBidirectionalCommunication());
    }

    public ResponseType getResponseType() {
        return ResponseType.fromByte((byte) getDataRange(6, 5, 6, 4));
    }

    public void setResponseType(final ResponseType responseType) {
        setDataRange(responseType.toByte(), 6, 5, 6, 4);
    }

    @Override
    public String toString() {
        final String str = String.format("UserDataUTEResponse{%s, responseType=0x%02X}",
                                         super.toString(), getResponseType().toByte());
        return str;
    }

}
