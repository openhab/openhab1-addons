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
package org.openhab.binding.rfxcom.internal.messages;

import java.util.List;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * RFXCOM data class for interface message.
 *
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComInterfaceMessage extends RFXComBaseMessage {

    public enum SubType {
        INTERFACE_RESPONSE(0),

        UNKNOWN(255);

        private final int subType;

        SubType(int subType) {
            this.subType = subType;
        }

        SubType(byte subType) {
            this.subType = subType;
        }

        public byte toByte() {
            return (byte) subType;
        }

        public static SubType fromByte(int input) {
            for (SubType subType : SubType.values()) {
                if (subType.subType == input) {
                    return subType;
                }
            }

            return SubType.UNKNOWN;
        }
    }

    public enum Commands {
        RESET(0), // Reset the receiver/transceiver. No answer is transmitted!
        GET_STATUS(2), // Get Status, return firmware versions and configuration of the interface
        SET_MODE(3), // Set mode msg1-msg5, return firmware versions and configuration of the interface
        ENABLE_ALL(4), // Enable all receiving modes of the receiver/transceiver
        ENABLE_UNDECODED_PACKETS(5), // Enable reporting of undecoded packets
        SAVE_RECEIVING_MODES(6), // Save receiving modes of the receiver/transceiver in non-volatile memory
        T1(8), // For internal use by RFXCOM
        T2(9), // For internal use by RFXCOM

        UNKNOWN(255);

        private final int command;

        Commands(int command) {
            this.command = command;
        }

        Commands(byte command) {
            this.command = command;
        }

        public byte toByte() {
            return (byte) command;
        }

        public static Commands fromByte(int input) {
            for (Commands command : Commands.values()) {
                if (command.command == input) {
                    return command;
                }
            }

            return Commands.UNKNOWN;
        }
    }

    public enum TransceiverType {
        _310MHZ(80),
        _315MHZ(81),
        _433_92MHZ_RECEIVER_ONLY(82),
        _433_92MHZ_TRANSCEIVER(83),
        _868_00MHZ(85),
        _868_00MHZ_FSK(86),
        _868_30MHZ(87),
        _868_30MHZ_FSK(88),
        _868_35MHZ(89),
        _868_35MHZ_FSK(90),
        _868_95MHZ_FSK(91),

        UNKNOWN(255);

        private final int type;

        TransceiverType(int type) {
            this.type = type;
        }

        TransceiverType(byte type) {
            this.type = type;
        }

        public byte toByte() {
            return (byte) type;
        }

        public static TransceiverType fromByte(int input) {
            for (TransceiverType type : TransceiverType.values()) {
                if (type.type == input) {
                    return type;
                }
            }

            return TransceiverType.UNKNOWN;
        }
    }

    public enum FirmwareType {
        Type1_RFXRec(0),
        Type1(1),
        Type2(2),
        Ext(3),
        Ext2(4),

        UNKNOWN(255);

        private final int type;

        FirmwareType(int type) {
            this.type = type;
        }

        FirmwareType(byte type) {
            this.type = type;
        }

        public byte toByte() {
            return (byte) type;
        }
    }

    public SubType subType = SubType.UNKNOWN;
    public Commands command = Commands.UNKNOWN;
    public TransceiverType transceiverType = TransceiverType.UNKNOWN;
    public short firmwareVersion = 0;
    public FirmwareType firmwareType = FirmwareType.UNKNOWN;

    public boolean enableUndecodedPackets = false; // 0x80 - Undecoded packets
    public boolean enableRFU6Packets = false; // 0x40 - RFU6
    public boolean enableRFU5Packets = false; // 0x20 - RFU5
    public boolean enableRFU4Packets = false; // 0x10 - RFU4
    public boolean enableRFU3Packets = false; // 0x08 - RFU3
    public boolean enableFineOffsetPackets = false; // 0x04 - FineOffset / Viking (433.92)
    public boolean enableRubicsonPackets = false; // 0x02 - Rubicson (433.92)
    public boolean enableAEPackets = false; // 0x01 - AE (433.92)

    public boolean enableBlindsT1Packets = false; // 0x80 - BlindsT1/T2/T3 (433.92)
    public boolean enableBlindsT0Packets = false; // 0x40 - BlindsT0 (433.92)
    public boolean enableProGuardPackets = false; // 0x20 - ProGuard (868.35 FSK)
    public boolean enableFS20Packets = false; // 0x10 - FS20 (868.35)
    public boolean enableLaCrossePackets = false; // 0x08 - La Crosse (433.92/868.30)
    public boolean enableHidekiPackets = false; // 0x04 - Hideki/UPM (433.92)
    public boolean enableADPackets = false; // 0x02 - AD (433.92)
    public boolean enableMertikPackets = false; // 0x01 - Mertik (433.92)

    public boolean enableVisonicPackets = false; // 0x80 - Visonic (315/868.95)
    public boolean enableATIPackets = false; // 0x40 - ATI (433.92)
    public boolean enableOregonPackets = false; // 0x20 - Oregon Scientific (433.92)
    public boolean enableMeiantechPackets = false; // 0x10 - Meiantech (433.92)
    public boolean enableHomeEasyPackets = false; // 0x08 - HomeEasy EU (433.92)
    public boolean enableACPackets = false; // 0x04 - AC (433.92)
    public boolean enableARCPackets = false; // 0x02 - ARC (433.92)
    public boolean enableX10Packets = false; // 0x01 - X10 (310/433.92)

    public boolean enableHomeConfortPackets = false; // 0x02 - HomeConfort (433.92)
    public boolean enableKeeLoqPackets = false; // 0x01 - KeeLoq (433.92)

    public byte hardwareVersion1 = 0;
    public byte hardwareVersion2 = 0;
    public byte outputPower = 0;

    public RFXComInterfaceMessage() {

    }

    public RFXComInterfaceMessage(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Command = " + command;
        str += "\n - Transceiver type = " + transceiverType;
        str += "\n - Firmware version = " + firmwareVersion;
        str += "\n - Firmware type = " + firmwareType;
        str += "\n - Hardware version = " + hardwareVersion1 + "." + hardwareVersion2;
        str += "\n - Undecoded packets = " + enableUndecodedPackets;
        str += "\n - RFU6 packets = " + enableRFU6Packets;
        str += "\n - RFU5 packets = " + enableRFU5Packets;
        str += "\n - RFU4 packets = " + enableRFU4Packets;
        str += "\n - RFU3 packets = " + enableRFU3Packets;
        str += "\n - FineOffset / Viking (433.92) packets = " + enableFineOffsetPackets;
        str += "\n - Rubicson (433.92) packets = " + enableRubicsonPackets;
        str += "\n - AE (433.92) packets = " + enableAEPackets;

        str += "\n - BlindsT1/T2/T3 (433.92) packets = " + enableBlindsT1Packets;
        str += "\n - BlindsT0 (433.92) packets = " + enableBlindsT0Packets;
        str += "\n - ProGuard (868.35 FSK) packets = " + enableProGuardPackets;
        str += "\n - FS20 (868.35) packets = " + enableFS20Packets;
        str += "\n - La Crosse (433.92/868.30) packets = " + enableLaCrossePackets;
        str += "\n - Hideki/UPM (433.92) packets = " + enableHidekiPackets;
        str += "\n - AD (433.92) packets = " + enableADPackets;
        str += "\n - Mertik (433.92) packets = " + enableMertikPackets;

        str += "\n - Visonic (315/868.95) packets = " + enableVisonicPackets;
        str += "\n - ATI (433.92) packets = " + enableATIPackets;
        str += "\n - Oregon Scientific (433.92) packets = " + enableOregonPackets;
        str += "\n - Meiantech (433.92) packets = " + enableMeiantechPackets;
        str += "\n - HomeEasy EU (433.92) packets = " + enableHomeEasyPackets;
        str += "\n - AC (433.92) packets = " + enableACPackets;
        str += "\n - ARC (433.92) packets = " + enableARCPackets;
        str += "\n - X10 (310/433.92) packets = " + enableX10Packets;
        str += "\n - HomeConfort packets = " + enableHomeConfortPackets;
        str += "\n - KeeLoq (433.92) packets = " + enableKeeLoqPackets;

        return str;
    }

    /**
     * The format of message differ in some firmwares
     * Some messages with 13 bytes(14 with length) have an intermediate format
     * The firmware version is different for each hardware,
     * and the hardware version are in different position depending of message format
     * Actually we dont know how to detect correctly for all hardwares
     *
     * Apparently we can consider this versions are using new format
     * RFXtrx433 version 1 (HW version=1.0 ou 1.1 ?) : 95
     * RFXtrx433 version 2 (HW version 1.2??) : 195
     * RFXtrx433E (HW version 1.3??) : 251
     *
     */
    private boolean isIntermediateFormat() {
        return (firmwareVersion >= 95 && firmwareVersion <= 100) || (firmwareVersion >= 195 && firmwareVersion <= 200)
                || (firmwareVersion >= 251);
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        command = Commands.fromByte(data[4]);
        transceiverType = TransceiverType.fromByte(data[5]);

        firmwareType = FirmwareType.UNKNOWN;
        if (data.length > 14) {
            firmwareVersion = (short) ((data[6] & 0xff) + 1000);
            for (FirmwareType type : FirmwareType.values()) {
                if (type.toByte() == data[14]) {
                    firmwareType = type;
                    break;
                }
            }
        } else {
            firmwareVersion = (short) (data[6] & 0xff);
            if (transceiverType == TransceiverType._433_92MHZ_RECEIVER_ONLY && firmwareVersion < 162) {
                firmwareType = FirmwareType.Type1_RFXRec;
            } else if (transceiverType == TransceiverType._433_92MHZ_TRANSCEIVER && firmwareVersion < 162) {
                firmwareType = FirmwareType.Type1;
            } else if (transceiverType == TransceiverType._433_92MHZ_TRANSCEIVER
                    && (firmwareVersion >= 162 && firmwareVersion < 225)) {
                firmwareType = FirmwareType.Type2;
            } else {
                firmwareType = FirmwareType.Ext;
            }
        }

        enableUndecodedPackets = (data[7] & 0x80) != 0 ? true : false;
        enableRFU6Packets = (data[7] & 0x40) != 0 ? true : false;
        enableRFU5Packets = (data[7] & 0x20) != 0 ? true : false;
        enableRFU4Packets = (data[7] & 0x10) != 0 ? true : false;
        enableRFU3Packets = (data[7] & 0x08) != 0 ? true : false;
        enableFineOffsetPackets = (data[7] & 0x04) != 0 ? true : false;
        enableRubicsonPackets = (data[7] & 0x02) != 0 ? true : false;
        enableAEPackets = (data[7] & 0x01) != 0 ? true : false;

        enableBlindsT1Packets = (data[8] & 0x80) != 0 ? true : false;
        enableBlindsT0Packets = (data[8] & 0x40) != 0 ? true : false;
        enableProGuardPackets = (data[8] & 0x20) != 0 ? true : false;
        enableFS20Packets = (data[8] & 0x10) != 0 ? true : false;
        enableLaCrossePackets = (data[8] & 0x08) != 0 ? true : false;
        enableHidekiPackets = (data[8] & 0x04) != 0 ? true : false;
        enableADPackets = (data[8] & 0x02) != 0 ? true : false;
        enableMertikPackets = (data[8] & 0x01) != 0 ? true : false;

        enableVisonicPackets = (data[9] & 0x80) != 0 ? true : false;
        enableATIPackets = (data[9] & 0x40) != 0 ? true : false;
        enableOregonPackets = (data[9] & 0x20) != 0 ? true : false;
        enableMeiantechPackets = (data[9] & 0x10) != 0 ? true : false;
        enableHomeEasyPackets = (data[9] & 0x08) != 0 ? true : false;
        enableACPackets = (data[9] & 0x04) != 0 ? true : false;
        enableARCPackets = (data[9] & 0x02) != 0 ? true : false;
        enableX10Packets = (data[9] & 0x01) != 0 ? true : false;

        if (isIntermediateFormat() || data.length > 14) {
            enableHomeConfortPackets = (data[10] & 0x02) != 0 ? true : false;
            enableKeeLoqPackets = (data[10] & 0x01) != 0 ? true : false;

            hardwareVersion1 = data[11];
            hardwareVersion2 = data[12];

            outputPower = data[13];
        } else {
            hardwareVersion1 = data[10];
            hardwareVersion2 = data[11];
        }

    }

    @Override
    public byte[] decodeMessage() {

        byte[] data = new byte[(firmwareVersion < 1000) ? 14 : 21];

        data[0] = (byte) (data.length - 1);
        data[1] = RFXComBaseMessage.PacketType.INTERFACE_MESSAGE.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;
        data[4] = command.toByte();
        data[5] = transceiverType.toByte();
        data[6] = (byte) (((firmwareVersion > 1000) ? (firmwareVersion - 1000) : firmwareVersion) & 0xff);

        data[7] |= enableUndecodedPackets ? 0x80 : 0x00;
        data[7] |= enableRFU6Packets ? 0x40 : 0x00;
        data[7] |= enableRFU5Packets ? 0x20 : 0x00;
        data[7] |= enableRFU4Packets ? 0x10 : 0x00;
        data[7] |= enableRFU3Packets ? 0x08 : 0x00;
        data[7] |= enableFineOffsetPackets ? 0x04 : 0x00;
        data[7] |= enableRubicsonPackets ? 0x02 : 0x00;
        data[7] |= enableAEPackets ? 0x01 : 0x00;

        data[8] |= enableBlindsT1Packets ? 0x80 : 0x00;
        data[8] |= enableBlindsT0Packets ? 0x40 : 0x00;
        data[8] |= enableProGuardPackets ? 0x20 : 0x00;
        data[8] |= enableFS20Packets ? 0x10 : 0x00;
        data[8] |= enableLaCrossePackets ? 0x08 : 0x00;
        data[8] |= enableHidekiPackets ? 0x04 : 0x00;
        data[8] |= enableADPackets ? 0x02 : 0x00;
        data[8] |= enableMertikPackets ? 0x01 : 0x00;

        data[9] |= enableVisonicPackets ? 0x80 : 0x00;
        data[9] |= enableATIPackets ? 0x40 : 0x00;
        data[9] |= enableOregonPackets ? 0x20 : 0x00;
        data[9] |= enableMeiantechPackets ? 0x10 : 0x00;
        data[9] |= enableHomeEasyPackets ? 0x08 : 0x00;
        data[9] |= enableACPackets ? 0x04 : 0x00;
        data[9] |= enableARCPackets ? 0x02 : 0x00;
        data[9] |= enableX10Packets ? 0x01 : 0x00;

        if (isIntermediateFormat() || firmwareVersion > 1000) {
            data[10] |= enableHomeConfortPackets ? 0x02 : 0x00;
            data[10] |= enableKeeLoqPackets ? 0x01 : 0x00;

            data[11] = hardwareVersion1;
            data[12] = hardwareVersion2;
            data[13] = outputPower;
            if (firmwareVersion > 1000) {
                data[14] = firmwareType.toByte();
            }
        } else {
            data[10] = hardwareVersion1;
            data[11] = hardwareVersion2;
        }

        return data;
    }

    @Override
    public State convertToState(RFXComValueSelector valueSelector) throws RFXComException {

        throw new RFXComException("Not supported");
    }

    @Override
    public void convertFromState(RFXComValueSelector valueSelector, String id, Object subType, Type type,
            byte seqNumber) throws RFXComException {

        throw new RFXComException("Not supported");
    }

    @Override
    public Object convertSubType(String subType) throws RFXComException {

        for (SubType s : SubType.values()) {
            if (s.toString().equals(subType)) {
                return s;
            }
        }

        throw new RFXComException("Unknown sub type " + subType);
    }

    @Override
    public List<RFXComValueSelector> getSupportedValueSelectors() throws RFXComException {
        return null;
    }

}
