package org.openhab.binding.tinkerforge.internal.tools;

public class NDEFRecord {
    private final int[] buffer;
    private TNF tnf;
    private boolean idLengthSet;
    private boolean shortRecord;
    private boolean chunked;
    private boolean messageEnd;
    private boolean messageBegin;
    private int typeLength;
    private int payloadLength;
    private int idLength;
    private int type;

    private NDEFRecord(int[] buffer) {
        this.buffer = buffer;
    }

    public static NDEFRecord fromBuffer(int[] buffer) {
        NDEFRecord record = new NDEFRecord(buffer);
        record.initFromBuffer();

        return record;
    }

    private void initFromBuffer() {
        // TODO validate
        if (buffer.length < 5) {
            throw new IllegalArgumentException("cant parse buffer data");
        }

        tnf = TNF.getByTypeId((byte) (buffer[0] & 7));
        idLengthSet = (buffer[0] >> 3 & 1) == 1;
        shortRecord = (buffer[0] >> 4 & 1) == 1;
        chunked = (buffer[0] >> 5 & 1) == 1;
        messageEnd = (buffer[0] >> 6 & 1) == 1;
        messageBegin = (buffer[0] >> 7 & 1) == 1;
        typeLength = buffer[1];
        payloadLength = buffer[2];

        if (idLengthSet) {
            throw new UnsupportedOperationException("cant handle messages with id length flag");
        }

        if (typeLength > 1) {
            throw new UnsupportedOperationException("type length > 1 are not supported yet");
        }

        type = buffer[3];

        // todo: 3 ndef structure -> description of payload e.g. for well known tags
        parseWkt();
    }

    private void parseWkt() {
        // we assume that data starts at 4
        // Examples of NFC Forum Global Types: “U”, “Cfq”, “Trip-to-Texas”.
        // defined in the type field
        // U 0x55 steht für URI | https://learn.adafruit.com/adafruit-pn532-rfid-nfc/ndef
    }

    public String getDataInfo() {
        return "";
    }

    public static enum TNF {
        EMPTY(0x00),
        NFC_FORUM_WELL_KNOWN_TYPE(0x01),
        MEDIA_TYPE(0x02),
        ABSOULTE_URI(0x03),
        NFC_FORUM_EXTERNAL_TYPE(0x04),
        UNKNOWN(0x05),
        UNCHANGED(0x06),
        RESERVED(0x07);

        private final byte typeID;

        private TNF(int typeID) {
            this.typeID = (byte) typeID;
        }

        public static TNF getByTypeId(byte typeId) {
            if (typeId == EMPTY.typeID) {
                return EMPTY;
            }
            if (typeId == NFC_FORUM_WELL_KNOWN_TYPE.typeID) {
                return NFC_FORUM_WELL_KNOWN_TYPE;
            }
            if (typeId == MEDIA_TYPE.typeID) {
                return MEDIA_TYPE;
            }
            if (typeId == ABSOULTE_URI.typeID) {
                return ABSOULTE_URI;
            }
            if (typeId == NFC_FORUM_EXTERNAL_TYPE.typeID) {
                return NFC_FORUM_EXTERNAL_TYPE;
            }
            if (typeId == UNKNOWN.typeID) {
                return UNKNOWN;
            }
            if (typeId == UNCHANGED.typeID) {
                return UNCHANGED;
            }
            if (typeId == RESERVED.typeID) {
                return RESERVED;
            }

            throw new UnsupportedOperationException(
                    String.format("the type name format with id %d is not supported", typeId));
        }

    }
}
