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
public class UserDataUTEQuery extends UserDataUTE {

    private static final byte QUERYTYPE_TEACH_IN_REQUEST = (byte) 0;
    private static final byte QUERYTYPE_DELETION_REQUEST = (byte) 1;
    private static final byte QUERYTYPE_NOT_SPECIFIED = (byte) 2;
    private static final byte QUERYTYPE_NOT_USED = (byte) 3;

    public enum QueryType {

        TEACH_IN_REQUEST(QUERYTYPE_TEACH_IN_REQUEST),
        DELETION_REQUEST(QUERYTYPE_DELETION_REQUEST),
        NOT_SPECIFIED(QUERYTYPE_NOT_SPECIFIED),
        NOT_USED(QUERYTYPE_NOT_USED);

        private final byte queryType;

        private QueryType(final byte queryType) {
            this.queryType = queryType;
        }

        public byte toByte() {
            return queryType;
        }

        public static QueryType fromByte(final byte queryType) {
            switch (queryType) {
                case QUERYTYPE_TEACH_IN_REQUEST:
                    return TEACH_IN_REQUEST;
                case QUERYTYPE_DELETION_REQUEST:
                    return DELETION_REQUEST;
                case QUERYTYPE_NOT_SPECIFIED:
                    return NOT_SPECIFIED;
                case QUERYTYPE_NOT_USED:
                default:
                    return NOT_USED;
            }
        }
    }

    public UserDataUTEQuery() {
        super();
        setCmd(Command.QUERY);
    }

    public UserDataUTEQuery(final byte[] data) {
        super(data);
        setCmd(Command.QUERY);
    }

    public boolean getResponseExpected() {
        return getDataBit(6, 6) == 0;
    }

    public void setResponseExpected(final boolean expected) {
        setDataBit(6, 6, expected ? 0 : 1);
    }

    public QueryType getQueryType() {
        return QueryType.fromByte((byte) getDataRange(6, 5, 6, 4));
    }

    public void setQueryType(final QueryType queryType) {
        setDataRange(queryType.toByte(), 6, 5, 6, 4);
    }

    @Override
    public String toString() {
        final String str = String.format("UserDataUTEQuery{%s, responseExpected=%b, queryType=0x%02X}",
                                         super.toString(), getResponseExpected(), getQueryType().toByte());
        return str;
    }

}
