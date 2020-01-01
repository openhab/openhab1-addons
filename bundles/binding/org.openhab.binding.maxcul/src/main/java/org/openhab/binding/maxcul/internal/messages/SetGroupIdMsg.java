/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.maxcul.internal.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SetGroupId Message
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class SetGroupIdMsg extends BaseMsg {

    final private int SET_GROUP_ID_PAYLOAD_LEN = 1;

    private static final Logger logger = LoggerFactory.getLogger(SetGroupIdMsg.class);

    private byte groupIdToSet;

    public SetGroupIdMsg(byte msgCount, byte msgFlag, String srcAddr, String dstAddr, byte groupIdToSet) {
        super(msgCount, msgFlag, MaxCulMsgType.SET_GROUP_ID, (byte) 0x0, srcAddr, dstAddr);

        this.groupIdToSet = groupIdToSet;
        byte[] payload = new byte[SET_GROUP_ID_PAYLOAD_LEN];

        payload[0] = groupIdToSet;

        super.appendPayload(payload);
    }

    public SetGroupIdMsg(String rawmsg) {
        super(rawmsg);
        groupIdToSet = this.payload[0];
    }

    /**
     * Print output as decoded fields
     */
    @Override
    protected void printFormattedPayload() {
        logger.debug("\t Group ID => " + this.groupIdToSet);
    }
}
