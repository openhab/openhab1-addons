package org.openhab.binding.tinkerforge.internal.tools;

import com.tinkerforge.BrickletNFC;
import com.tinkerforge.BrickletNFC.ReaderGetTagID;

/**
 * Tool for parsing a nfc tag.
 *
 * @author André Kühnert
 */
public class NFCTagInfo {
    private String tagIdAsHex;
    private ReaderGetTagID readerGetTagID;

    private NFCTagInfo() {
    }

    /**
     * Creates the {@link NFCTagInfo}
     *
     * @param readerGetTagID
     * @return
     */
    public static NFCTagInfo fromReaderGetTagID(ReaderGetTagID readerGetTagID) {
        NFCTagInfo info = new NFCTagInfo();
        info.readerGetTagID = readerGetTagID;

        return info;
    }

    /**
     * Gets the tag id as hex string (e.g. 0x4 0x9F ...). (lazy)
     *
     * @return
     */
    public String getTagIdAsHex() {
        if (tagIdAsHex == null) {
            tagIdAsHex = getTagIdAsHexString(readerGetTagID);
        }
        return tagIdAsHex;
    }

    /**
     * Gets the {@link TagType}
     *
     * @return
     */
    public TagType getTagType() {
        return TagType.getTagTypeFromId(readerGetTagID.tagType);
    }

    private static String getTagIdAsHexString(ReaderGetTagID getTagId) {
        StringBuilder tag = new StringBuilder();
        int i = 0;
        for (int v : getTagId.tagID) {
            if (i < getTagId.tagID.length - 1) {
                tag.append(String.format("0x%X ", v));
            } else {
                tag.append(String.format("0x%X", v));
            }
            i++;
        }
        return tag.toString();
    }

    public static enum TagType {
        MIFARE_CLASSIC(BrickletNFC.TAG_TYPE_MIFARE_CLASSIC),
        NFC_FORUM_TYPE_1(BrickletNFC.TAG_TYPE_TYPE1),
        NFC_FORUM_TYPE_2(BrickletNFC.TAG_TYPE_TYPE2),
        NFC_FORUM_TYPE_3(BrickletNFC.TAG_TYPE_TYPE3),
        NFC_FORUM_TYPE_4(BrickletNFC.TAG_TYPE_TYPE4);

        private final int typeId;

        private TagType(int typeId) {
            this.typeId = typeId;
        }

        public int getTypeId() {
            return typeId;
        }

        public static TagType getTagTypeFromId(int id) {
            if (id == MIFARE_CLASSIC.typeId) {
                return MIFARE_CLASSIC;
            }
            if (id == NFC_FORUM_TYPE_1.typeId) {
                return NFC_FORUM_TYPE_1;
            }
            if (id == NFC_FORUM_TYPE_2.typeId) {
                return NFC_FORUM_TYPE_2;
            }
            if (id == NFC_FORUM_TYPE_3.typeId) {
                return NFC_FORUM_TYPE_3;
            }
            if (id == NFC_FORUM_TYPE_4.typeId) {
                return NFC_FORUM_TYPE_4;
            }
            throw new IllegalArgumentException(String.format("Tag id %d not supported.", id));
        }
    }

}
