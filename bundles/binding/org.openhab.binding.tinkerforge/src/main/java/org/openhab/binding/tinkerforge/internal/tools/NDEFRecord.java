package org.openhab.binding.tinkerforge.internal.tools;

/**
 * Simple class to parse NDEF records.
 * The current implementation will only support NFC Forum Tag Type 2.
 * <ul>
 * <li>chunked data is not supported</li>
 * <li>only short record support</li>
 * <li>id flag not supported</li>
 * <li>only type name format 'NFC_FORUM_WELL_KNOWN_TYPE' is supported</li>
 * <li>for 'NFC_FORUM_WELL_KNOWN_TYPE' only Uri ('U'), Text ('T') and SmartPosterRecord ('Sp') are supported</li>
 * </ul>
 *
 * @author André Kühnert
 */
public abstract class NDEFRecord {
    protected final String ERROR_BUFFER_SIZE = "Cant parse content. Expected payload size exceeds buffer length.";
    // payload starts at min 3 + typeLength index
    protected static final int MIN_PAYLOAD_OFFSET = 3;
    protected final int[] buffer;
    protected final int offset;
    private final NDEFHeader header;

    private NDEFRecord(NDEFHeader header, int[] buffer, int offset) {
        this.header = header;
        this.buffer = buffer;
        this.offset = offset;
    }

    /**
     * Creates a {@link NDEFRecord} from a given buffer.
     *
     * @param buffer
     * @return
     * @throws NDEFParseException
     */
    public static NDEFRecord fromBuffer(int[] buffer) throws NDEFParseException {
        return fromBuffer(buffer, 0);
    }

    /**
     *
     * @param buffer The buffer containing the NDEF message.
     * @param offset The offset for the given buffer. Sometimes there are multiple NDEF messages included, so instead of
     *                   making a copy of the array, we use a offset to the next message.
     * @return
     * @throws NDEFParseException
     */
    public static NDEFRecord fromBuffer(int[] buffer, int offset) throws NDEFParseException {
        NDEFHeader header = NDEFHeader.fromBuffer(buffer, offset);
        if (header.tnf != TNF.NFC_FORUM_WELL_KNOWN_TYPE) {
            throw new UnsupportedOperationException(
                    String.format("tag from type '%s' not supported", header.tnf.name()));
        }

        return createNDEFRecord(header, buffer, offset);
    }

    private static NDEFRecord createNDEFRecord(NDEFHeader header, int[] buffer, int offset) throws NDEFParseException {
        String type = header.type;

        if ("Sp".equals(type)) {
            return SmartPosterRecord.fromRoot(header, buffer, offset);
        }

        if ("U".equals(type)) {
            return UriRecord.fromRoot(header, buffer, offset);
        }

        if ("T".equals(type)) {
            return TextRecord.fromRoot(header, buffer, offset);
        }

        throw new NDEFParseException(String.format("ndef type '%s' not supported yet", type));
    }

    /**
     * Gets the complete buffer as hex string (e.g. 0xD1 0x02 0x50 ...)
     *
     * @return
     */
    public String getRawDataAsHex() {
        StringBuilder tag = new StringBuilder();
        int i = 0;
        for (int v : buffer) {
            if (i < buffer.length - 1) {
                tag.append(String.format("0x%X ", v));
            } else {
                tag.append(String.format("0x%X", v));
            }
            i++;
        }
        return tag.toString();
    }

    protected abstract void init() throws NDEFParseException;

    public boolean isMessageBegin() {
        return header.messageBegin;
    }

    public boolean isMessageEnd() {
        return header.messageEnd;
    }

    public TNF getTNF() {
        return header.tnf;
    }

    public int getPayloadLength() {
        return header.payloadLength;
    }

    public int getTypeLength() {
        return header.typeLength;
    }

    public String getType() {
        return header.type;
    }

    /**
     * Exception which can happen while parsing the NDEF data.
     */
    public static class NDEFParseException extends Exception {
        private static final long serialVersionUID = 1L;

        public NDEFParseException() {
            super();
        }

        public NDEFParseException(String message) {
            super(message);
        }
    }

    /**
     * Type 'Sp'.
     * SmartPosterRecords contains at least one {@link UriRecord}. It can also contain a {@link TextRecord} and a
     * ActionRecord.
     *
     * NOTE: Action Records not supported yet.
     */
    public static class SmartPosterRecord extends NDEFRecord {
        private static final String ERROR_MSG = "The uri record is required for smart poster records. Failed to read?";
        private UriRecord uriRecord;
        private TextRecord textRecord;
        // TODO there could also be a action record

        private SmartPosterRecord(NDEFHeader header, int[] buffer, int offset) {
            super(header, buffer, offset);
        }

        public static SmartPosterRecord fromRoot(NDEFHeader header, int[] buffer, int offset)
                throws NDEFParseException {
            SmartPosterRecord record = new SmartPosterRecord(header, buffer, offset);
            record.init();
            return record;
        }

        @Override
        protected void init() throws NDEFParseException {
            int firstOffset = MIN_PAYLOAD_OFFSET + getTypeLength() + offset;
            NDEFRecord record1 = NDEFRecord.fromBuffer(buffer, firstOffset);

            assignRecordToField(record1);

            if (wasLastRecord(record1)) {
                return;
            }

            int secondOffset = firstOffset + MIN_PAYLOAD_OFFSET + record1.getTypeLength() + record1.getPayloadLength();
            NDEFRecord record2 = NDEFRecord.fromBuffer(buffer, secondOffset);

            assignRecordToField(record2);

            if (wasLastRecord(record2)) {
                return;
            }

            // Uri record cant be null. If this happens, there is something wrong
            if (uriRecord == null) {
                throw new NDEFParseException(ERROR_MSG);
            }
        }

        /**
         * Gets the {@link UriRecord} from this message.
         *
         * @return
         */
        public UriRecord getUriRecord() {
            return uriRecord;
        }

        /**
         * Gets the {@link TextRecord} from this message.
         *
         * @return the textRecord or null if no text record was saved.
         */
        public TextRecord getTextRecord() {
            return textRecord;
        }

        @Override
        public String toString() {
            String uriString = uriRecord.toString();
            String txtString = textRecord != null ? textRecord.toString() : "";
            return "URI Record:\n" + uriString + "\n\nText Record:\n" + txtString;
        }

        private boolean wasLastRecord(NDEFRecord record) throws NDEFParseException {
            boolean isIncomplete = record.isMessageEnd() && uriRecord == null;
            if (isIncomplete) {
                throw new NDEFParseException(ERROR_MSG);
            }
            return record.isMessageEnd();
        }

        private void assignRecordToField(NDEFRecord record) {
            if (record instanceof UriRecord) {
                uriRecord = (UriRecord) record;
            } else if (record instanceof TextRecord) {
                textRecord = (TextRecord) record;
            }
        }
    }

    /**
     * Type 'U'.
     * For defining uris.
     */
    public static class UriRecord extends NDEFRecord {
        private String uri;
        private URIIdentifierCode uriCode;

        private UriRecord(NDEFHeader header, int[] buffer, int offset) {
            super(header, buffer, offset);
        }

        public static UriRecord fromRoot(NDEFHeader header, int[] buffer, int offset) throws NDEFParseException {
            UriRecord record = new UriRecord(header, buffer, offset);
            record.init();
            return record;
        }

        @Override
        protected void init() throws NDEFParseException {
            uriCode = URIIdentifierCode.getCodeById(buffer[offset + 4]);
            int uriOffset = 5 + offset;
            if (getPayloadLength() + uriOffset - 2 > buffer.length - 1) {
                throw new NDEFParseException(ERROR_BUFFER_SIZE);
            }
            uri = new String(buffer, uriOffset, getPayloadLength() - 1);
        }

        /**
         * Gets the complete uri including the {@link URIIdentifierCode#prefix}
         * (e.g. http://some-uri.org)
         *
         * @return
         */
        public String getCompleteUri() {
            return uriCode.getPrefix() + uri;
        }

        /**
         * Gets the uri without the {@link URIIdentifierCode#prefix}.
         * (e.g. some-uri.org)
         *
         * @return
         */
        public String getUri() {
            return uri;
        }

        /**
         * Gets the {@link URIIdentifierCode}
         *
         * @return
         */
        public URIIdentifierCode getUriCode() {
            return uriCode;
        }

        @Override
        public String toString() {
            return getCompleteUri();
        }
    }

    /**
     * Type 'T'.
     * For defining text.
     */
    public static class TextRecord extends NDEFRecord {
        private String text;
        private String encoding;
        private String lang;

        private TextRecord(NDEFHeader header, int[] buffer, int offset) {
            super(header, buffer, offset);
        }

        public static TextRecord fromRoot(NDEFHeader header, int[] buffer, int offset) throws NDEFParseException {
            TextRecord record = new TextRecord(header, buffer, offset);
            record.init();
            return record;
        }

        @Override
        protected void init() throws NDEFParseException {
            int status = buffer[offset + 4];
            encoding = (status >> 7 & 1) == 1 ? "UTF-16" : "UTF-8";
            int langSize = status & 0x3F;
            lang = new String(buffer, offset + 5, langSize);

            int textOffset = offset + 5 + langSize;
            if (textOffset + getPayloadLength() - langSize - 2 > buffer.length - 1) {
                throw new NDEFParseException(ERROR_BUFFER_SIZE);
            }
            text = new String(buffer, textOffset, getPayloadLength() - langSize - 1);
        }

        /**
         * Gets the text, which is written on the tag.
         *
         * @return
         */
        public String getText() {
            return text;
        }

        /**
         * Ecoding could be 'UTF-8' or 'UTF-16'
         *
         * @return
         */
        public String getEncoding() {
            return encoding;
        }

        /**
         * Gets the IANA language code (e.g. 'en')
         *
         * @return
         */
        public String getLang() {
            return lang;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("encoding: ").append(encoding).append("\n").append("lang: ").append(lang).append("\n")
                    .append("text: ").append(text);
            return builder.toString();
        }
    }

    /**
     * Defines the header for each ndef message.
     */
    public static class NDEFHeader {
        TNF tnf;
        boolean idLengthSet;
        // if short record, the payload length is only 1 byte (max 255)
        boolean shortRecord;
        boolean chunked;
        boolean messageEnd;
        boolean messageBegin;
        int typeLength;
        int payloadLength;
        int idLength;
        String type;

        /**
         * Reads the buffer data for a given ndef message on the specified offset.
         *
         * @param buffer
         * @param offset
         * @return
         * @throws NDEFParseException
         */
        public static NDEFHeader fromBuffer(int[] buffer, int offset) throws NDEFParseException {
            if (buffer.length < 4 + offset) {
                throw new NDEFParseException("wrong offset or no data?");
            }

            NDEFHeader header = new NDEFHeader();
            header.tnf = TNF.getByTypeId((byte) (buffer[0 + offset] & 7));
            header.idLengthSet = (buffer[0 + offset] >> 3 & 1) == 1;
            header.shortRecord = (buffer[0 + offset] >> 4 & 1) == 1;
            header.chunked = (buffer[0 + offset] >> 5 & 1) == 1;
            header.messageEnd = (buffer[0 + offset] >> 6 & 1) == 1;
            header.messageBegin = (buffer[0 + offset] >> 7 & 1) == 1;
            header.typeLength = buffer[1 + offset];
            header.payloadLength = buffer[2 + offset];

            if (header.idLengthSet) {
                throw new NDEFParseException("cant handle messages with id length flag");
            }
            if (!header.shortRecord) {
                throw new NDEFParseException("can only handle short records");
            }

            // type is US ASCII encoded
            header.type = new String(buffer, MIN_PAYLOAD_OFFSET + offset, header.typeLength);
            return header;
        }
    }

    /**
     * Defines all known type name formats (TNF)
     */
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

        /**
         * Gets the type name format for a given id. If no TNF for the given id is known, a {@link NDEFParseException}
         * is thrown.
         *
         * @param typeId
         * @return
         * @throws NDEFParseException
         */
        public static TNF getByTypeId(byte typeId) throws NDEFParseException {
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

            throw new NDEFParseException(String.format("the type name format with id %d is not supported", typeId));
        }

    }

    public static enum URIIdentifierCode {
        NONE(""),
        HTTP_WWW("http://www."),
        HTTPS_WWW("https://www."),
        HTTP("http://"),
        HTTPS("https://"),
        TEL("tel:"),
        MAILTO("mailto:"),
        FTP_ANO("ftp://anonymous:anonymous@"),
        FTP_FTP("ftp://ftp."),
        FTPS("ftps://"),
        SFTP("sftp://"),
        SMB("smb://"),
        NFS("nfs://"),
        FTP("ftp://"),
        DAV("dav://"),
        NEWS("news:"),
        TELNET("telnet://"),
        IMAP("imap:"),
        RTSP("rtsp://"),
        URN("urn:"),
        POP("pop:"),
        SIP("sip:"),
        SIPS("sips:"),
        TFTP("tftp:"),
        BTSPP("btspp://"),
        BTL2CAP("btl2cap://"),
        BTGOEP("btgoep://"),
        TCPOBEX("tcpobex://"),
        IRDAOBEX("irdaobex://"),
        FILE("file://"),
        URN_EPC_ID("urn:epc:id:"),
        URN_EPC_TAG("urn:epc:tag:"),
        URN_EPC_PAT("urn:epc:pat:"),
        URN_EPC_RAW("urn:epc:raw:"),
        URN_EPC("urn:epc:"),
        URN_NFC("urn:nfc:");

        private final String prefix;

        private URIIdentifierCode(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Gets the prefix for the given identifier (e.g. 'http://www.')
         *
         * @return
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Gets the code by a given id. The id is checked if it exists. If not a {@link NDEFParseException} exception is
         * thrown.
         *
         * @param id
         * @return
         * @throws NDEFParseException
         */
        public static URIIdentifierCode getCodeById(int id) throws NDEFParseException {
            if (id >= URIIdentifierCode.values().length) {
                throw new NDEFParseException(String.format("the uri code with id '%d' is not known", id));
            }
            return URIIdentifierCode.values()[id];
        }
    }
}
