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

import eu.aleon.aleoncean.packet.commoncommand.CoRdIdBase;
import eu.aleon.aleoncean.packet.commoncommand.CoRdVersionPacket;
import eu.aleon.aleoncean.packet.commoncommand.CoWrIdBase;
import eu.aleon.aleoncean.packet.commoncommand.CoWrResetPacket;
import eu.aleon.aleoncean.packet.commoncommand.CoWrSleepPacket;
import eu.aleon.aleoncean.packet.radio.RadioPacket1BS;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.RadioPacketADT;
import eu.aleon.aleoncean.packet.radio.RadioPacketMSC;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS;
import eu.aleon.aleoncean.packet.radio.RadioPacketUTE;
import eu.aleon.aleoncean.packet.radio.RadioPacketVLD;
import eu.aleon.aleoncean.packet.smartackcommand.SaRdLearnMode;
import eu.aleon.aleoncean.packet.smartackcommand.SaWrLearnConfirm;
import eu.aleon.aleoncean.packet.smartackcommand.SaWrLearnMode;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class ESP3PacketFactory {

    private static RadioPacket createRadioPacket(final byte[] raw) {
        final byte choice = RadioPacket.getChoice(raw);
        switch (choice) {
            case RadioChoice.RORG_RPS:
                return new RadioPacketRPS();
            case RadioChoice.RORG_1BS:
                return new RadioPacket1BS();
            case RadioChoice.RORG_4BS:
                return new RadioPacket4BS();
            case RadioChoice.RORG_VLD:
                return new RadioPacketVLD();
            case RadioChoice.RORG_MSC:
                return new RadioPacketMSC();
            case RadioChoice.RORG_ADT:
                return new RadioPacketADT();
            case RadioChoice.RORG_UTE:
                return new RadioPacketUTE();
            default:
                return new RadioPacket();
        }
    }

    private static CommonCommandPacket createCommonCommandPacket(final byte[] raw) {
        final byte code = CommonCommandPacket.getCommonCommandCode(raw);
        switch (code) {
            case CommonCommandCode.CO_WR_SLEEP:
                return new CoWrSleepPacket();
            case CommonCommandCode.CO_WR_RESET:
                return new CoWrResetPacket();
            case CommonCommandCode.CO_RD_VERSION:
                return new CoRdVersionPacket();
            case CommonCommandCode.CO_WR_IDBASE:
                return new CoWrIdBase();
            case CommonCommandCode.CO_RD_IDBASE:
                return new CoRdIdBase();
            default:
                return new CommonCommandPacket();
        }
    }

    private static SmartAckCommandPacket createSmartAckCommandPacket(final byte[] raw) {
        final byte code = SmartAckCommandPacket.getSmartAckCode(raw);
        switch (code) {
            case SmartAckCode.SA_WR_LEARNMODE:
                return new SaWrLearnMode();
            case SmartAckCode.SA_RD_LEARNMODE:
                return new SaRdLearnMode();
            case SmartAckCode.SA_WR_LEARNCONFIRM:
                return new SaWrLearnConfirm();
            default:
                return new SmartAckCommandPacket();
        }
    }

    private static ESP3Packet createESP3Packet(final byte[] raw) {
        final byte packetType = ESP3Packet.getPacketType(raw);
        switch (packetType) {
            case PacketType.RADIO:
                return createRadioPacket(raw);
            case PacketType.RESPONSE:
                return new ResponsePacket();
            case PacketType.COMMON_COMMAND:
                return createCommonCommandPacket(raw);
            case PacketType.SMART_ACK_COMMAND:
                return createSmartAckCommandPacket(raw);
            default:
                return new ESP3Packet();
        }
    }

    public static ESP3Packet fromRaw(final byte[] raw) {
        final ESP3Packet packet = createESP3Packet(raw);
        packet.fillFromRaw(raw);
        return packet;
    }

    private ESP3PacketFactory() {
    }

}
