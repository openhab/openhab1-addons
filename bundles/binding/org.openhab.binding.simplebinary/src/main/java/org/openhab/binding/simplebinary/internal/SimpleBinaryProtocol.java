/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.awt.Color;
import java.util.Map;

import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.DeviceConfig;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryBindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class of protocol
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryProtocol {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryProtocol.class);

    static int INCREASE_STEP = 5;

    /**
     * Compile data "new data" request packet
     *
     * @param busAddress
     *            Address connected device
     * @param forceAllDataAsNew
     *            Tell device all his output items should be mark as new
     * @return
     */
    public static SimpleBinaryItemData compileNewDataFrame(int busAddress, boolean forceAllDataAsNew) {
        byte[] data = new byte[4];
        byte control = forceAllDataAsNew ? (byte) 1 : (byte) 0;

        data[0] = (byte) (busAddress & 0xFF);
        data[1] = (byte) 0xD0;
        data[2] = control;
        data[3] = evalCRC(data, 3);

        return new SimpleBinaryItemData((byte) 0xD0, busAddress, data);
    }

    /**
     * Compile data "read data" request packet
     *
     * @param itemConfig
     *            Requested item configuration
     * @return
     */
    public static SimpleBinaryItemData compileReadDataFrame(DeviceConfig itemConfig) {
        byte[] data = new byte[5];

        data[0] = (byte) (itemConfig.getDeviceAddress() & 0xFF);
        data[1] = (byte) 0xD1;
        data[2] = (byte) (itemConfig.getItemAddress() & 0xFF);
        data[3] = (byte) ((itemConfig.getItemAddress() & 0xFF00) >> 8);
        data[4] = evalCRC(data, 4);

        return new SimpleBinaryItemData((byte) 0xD1, itemConfig.getDeviceAddress(), data);
    }

    /**
     * Compile command data for specific item
     *
     * @param itemName
     * @param command
     * @param config
     * @return
     */
    public static SimpleBinaryItem compileDataFrame(String itemName, Type command, SimpleBinaryBindingConfig itemConfig,
            DeviceConfig deviceConfig) {
        byte[] data = compileDataFrameEx(itemName, command, itemConfig, deviceConfig);

        if (data == null) {
            return null;
        }

        return new SimpleBinaryItem(itemName, itemConfig, data[1], deviceConfig.getDeviceAddress(),
                deviceConfig.getItemAddress(), data);
    }

    /**
     * Compile command data for specific item
     *
     * @param itemName
     * @param command
     * @param config
     * @return
     */
    public static byte[] compileDataFrameEx(String itemName, Type command, SimpleBinaryBindingConfig itemConfig,
            DeviceConfig deviceConfig) {
        byte[] data;

        if (logger.isDebugEnabled()) {
            logger.debug("compileDataFrame(): item:{}|datatype:{}", itemName, itemConfig.getDataType());
        }

        switch (itemConfig.getDataType()) {
            case BYTE:
                data = new byte[6];
                data[1] = (byte) 0xDA;
                break;
            case WORD:
                data = new byte[7];
                data[1] = (byte) 0xDB;
                break;
            case DWORD:
            case FLOAT:
                data = new byte[9];
                data[1] = (byte) 0xDC;
                break;
            case HSB:
            case RGB:
            case RGBW:
                data = new byte[9];
                data[1] = (byte) 0xDD;
                break;
            case ARRAY:
                int arraylen = itemConfig.getDataLenght();
                data = new byte[7 + arraylen];
                data[1] = (byte) 0xDE;
                // length
                data[4] = (byte) (arraylen & 0xFF);
                data[5] = (byte) ((arraylen >> 8) & 0xFF);
                break;
            default:
                return null;
        }

        int datalen = data.length;

        // bus address
        data[0] = (byte) deviceConfig.getDeviceAddress();
        // item address / ID
        data[2] = (byte) (deviceConfig.getDeviceAddress() & 0xFF);
        data[3] = (byte) ((deviceConfig.getDeviceAddress() >> 8) & 0xFF);

        switch (itemConfig.getDataType()) {
            case BYTE:
                if (command instanceof PercentType) {
                    PercentType cmd = (PercentType) command;
                    data[4] = cmd.byteValue();

                    if (itemConfig.itemType.isAssignableFrom(DimmerItem.class)) {
                        ((DimmerItem) itemConfig.item).setState(new PercentType(cmd.byteValue()));
                    } else if (itemConfig.itemType.isAssignableFrom(RollershutterItem.class)) {
                        ((RollershutterItem) itemConfig.item).setState(new PercentType(cmd.byteValue()));
                    }
                } else if (command instanceof DecimalType) {
                    DecimalType cmd = (DecimalType) command;
                    data[4] = cmd.byteValue();
                } else if (command instanceof OpenClosedType) {
                    OpenClosedType cmd = (OpenClosedType) command;
                    if (cmd == OpenClosedType.OPEN) {
                        data[4] = 1;
                    } else {
                        data[4] = 0;
                    }
                } else if (command instanceof OnOffType) {
                    OnOffType cmd = (OnOffType) command;

                    if (itemConfig.itemType.isAssignableFrom(SwitchItem.class)) {
                        if (cmd == OnOffType.ON) {
                            data[4] = 1;
                        } else {
                            data[4] = 0;
                        }
                    } else if (itemConfig.itemType.isAssignableFrom(DimmerItem.class)) {
                        if (cmd == OnOffType.ON) {
                            PercentType val = ((PercentType) (itemConfig.item).getStateAs(PercentType.class));
                            if (val == null) {
                                data[4] = 100;
                                ((DimmerItem) itemConfig.item).setState(PercentType.HUNDRED);
                            } else {
                                data[4] = val.byteValue();
                                ((DimmerItem) itemConfig.item).setState(PercentType.ZERO);
                            }
                        } else {
                            data[4] = 0;
                        }
                    } else {
                        logger.error("Unsupported command type {} for datatype {}", command.getClass().toString(),
                                itemConfig.getDataType());
                    }

                } else if (command instanceof IncreaseDecreaseType
                        && itemConfig.itemType.isAssignableFrom(DimmerItem.class)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("IncreaseDecreaseType - DimmerItem");
                    }

                    DecimalType val = ((DecimalType) ((DimmerItem) (itemConfig.item)).getStateAs(DecimalType.class));

                    if (val == null) {
                        return null;
                    }

                    int brightness = Math.round((val.floatValue() * 100));

                    IncreaseDecreaseType upDownCmd = (IncreaseDecreaseType) command;

                    if (upDownCmd == IncreaseDecreaseType.INCREASE) {
                        brightness += INCREASE_STEP;

                        if (brightness > 100) {
                            brightness = 100;
                        }
                    } else {
                        brightness -= INCREASE_STEP;

                        if (brightness < 0) {
                            brightness = 0;
                        }
                    }

                    data[4] = (byte) brightness;

                    ((DimmerItem) itemConfig.item).setState(new PercentType(brightness));
                } else {
                    logger.error("Unsupported command type {} for datatype {}", command.getClass().toString(),
                            itemConfig.getDataType());
                    return null;
                }
                break;
            case WORD:
                if (command instanceof PercentType) {
                    PercentType cmd = (PercentType) command;
                    data[4] = cmd.byteValue();
                    data[5] = 0x0;
                } else if (command instanceof DecimalType) {
                    DecimalType cmd = (DecimalType) command;
                    data[4] = (byte) (cmd.intValue() & 0xFF);
                    data[5] = (byte) ((cmd.intValue() >> 8) & 0xFF);
                } else if (command instanceof StopMoveType) {
                    StopMoveType cmd = (StopMoveType) command;

                    data[4] = 0x0;
                    data[5] = (byte) (cmd.equals(StopMoveType.MOVE) ? 0x1 : 0x2);
                } else if (command instanceof UpDownType) {
                    UpDownType cmd = (UpDownType) command;

                    data[4] = 0x0;
                    data[5] = (byte) (cmd.equals(UpDownType.UP) ? 0x4 : 0x8);
                } else {
                    logger.error("Unsupported command type {} for datatype {}", command.getClass().toString(),
                            itemConfig.getDataType());
                    return null;
                }
                break;
            case DWORD:
                if (command instanceof DecimalType) {
                    DecimalType cmd = (DecimalType) command;
                    data[4] = (byte) (cmd.intValue() & 0xFF);
                    data[5] = (byte) ((cmd.intValue() >> 8) & 0xFF);
                    data[6] = (byte) ((cmd.intValue() >> 16) & 0xFF);
                    data[7] = (byte) ((cmd.intValue() >> 24) & 0xFF);
                } else {
                    logger.error("Unsupported command type {} for datatype {}", command.getClass().toString(),
                            itemConfig.getDataType());
                    return null;
                }
                break;
            case FLOAT:
                if (command instanceof DecimalType) {
                    DecimalType cmd = (DecimalType) command;
                    float value = cmd.floatValue();
                    int bits = Float.floatToIntBits(value);

                    data[4] = (byte) (bits & 0xFF);
                    data[5] = (byte) ((bits >> 8) & 0xFF);
                    data[6] = (byte) ((bits >> 16) & 0xFF);
                    data[7] = (byte) ((bits >> 24) & 0xFF);
                } else {
                    logger.error("Unsupported command type {} for datatype {}", command.getClass().toString(),
                            itemConfig.getDataType());
                    return null;
                }
                break;
            case HSB:
            case RGB:
            case RGBW:

                HSBType hsbVal;
                Item item = itemConfig.item;

                if (logger.isDebugEnabled()) {
                    logger.debug(item.toString());
                }

                if (command instanceof OnOffType) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("OnOffType");
                    }
                    OnOffType onOffCmd = (OnOffType) command;

                    if (onOffCmd == OnOffType.OFF) {
                        hsbVal = new HSBType(new Color(0, 0, 0));
                    } else {
                        hsbVal = ((HSBType) item.getStateAs(HSBType.class));
                    }
                } else if (command instanceof IncreaseDecreaseType) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("IncreaseDecreaseType");
                    }

                    hsbVal = ((HSBType) item.getStateAs(HSBType.class));
                    int brightness = hsbVal.getBrightness().intValue();

                    IncreaseDecreaseType upDownCmd = (IncreaseDecreaseType) command;

                    if (upDownCmd == IncreaseDecreaseType.INCREASE) {
                        brightness += INCREASE_STEP;

                        if (brightness > 100) {
                            brightness = 100;
                        }
                    } else {
                        brightness -= INCREASE_STEP;

                        if (brightness < 0) {
                            brightness = 0;
                        }
                    }

                    hsbVal = new HSBType(hsbVal.getHue(), hsbVal.getSaturation(), new PercentType(brightness));

                    ((ColorItem) item).setState(hsbVal);
                } else if (command instanceof HSBType) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("HSBType");
                    }
                    hsbVal = (HSBType) command;

                    ((ColorItem) item).setState(hsbVal);
                } else if (command instanceof PercentType) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("PercentType");
                    }
                    hsbVal = ((HSBType) item.getStateAs(HSBType.class));
                } else {
                    logger.error("Unsupported command type {} for datatype {}", command.getClass().toString(),
                            itemConfig.getDataType());
                    return null;
                }

                if (hsbVal != null) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Item {}: Red={} Green={} Blue={}", item.getName(), hsbVal.getRed(),
                                hsbVal.getGreen(), hsbVal.getBlue());
                        logger.trace("         Hue={} Sat={} Bri={}", hsbVal.getHue(), hsbVal.getSaturation(),
                                hsbVal.getBrightness());
                    }

                    HSBType cmd = hsbVal;

                    if (itemConfig.getDataType() == SimpleBinaryTypes.HSB) {
                        data[4] = cmd.getHue().byteValue();
                        data[5] = cmd.getSaturation().byteValue();
                        data[6] = cmd.getBrightness().byteValue();
                        data[7] = 0x0;
                    } else if (itemConfig.getDataType() == SimpleBinaryTypes.RGB) {
                        long red = Math.round((cmd.getRed().doubleValue() * 2.55));
                        long green = Math.round((cmd.getGreen().doubleValue() * 2.55));
                        long blue = Math.round((cmd.getBlue().doubleValue() * 2.55));

                        if (red > 255) {
                            red = 255;
                        }
                        if (green > 255) {
                            green = 255;
                        }
                        if (blue > 255) {
                            blue = 255;
                        }

                        if (logger.isDebugEnabled()) {
                            logger.debug("         Converted to 0-255: Red={} Green={} Blue={}", red, green, blue);
                        }

                        data[4] = (byte) (red & 0xFF);
                        data[5] = (byte) (green & 0xFF);
                        data[6] = (byte) (blue & 0xFF);
                        data[7] = 0x0;
                    } else if (itemConfig.getDataType() == SimpleBinaryTypes.RGBW) {
                        long red = Math.round((cmd.getRed().doubleValue() * 2.55));
                        long green = Math.round((cmd.getGreen().doubleValue() * 2.55));
                        long blue = Math.round((cmd.getBlue().doubleValue() * 2.55));
                        byte white;

                        if (red > 255) {
                            red = 255;
                        }
                        if (green > 255) {
                            green = 255;
                        }
                        if (blue > 255) {
                            blue = 255;
                        }

                        if (logger.isDebugEnabled()) {
                            logger.debug("         Converted to 0-255: Red={} Green={} Blue={}", red, green, blue);
                        }

                        byte[] rgbw = calcWhite(red, green, blue);
                        red = rgbw[0];
                        green = rgbw[1];
                        blue = rgbw[2];
                        white = rgbw[3];

                        if (logger.isDebugEnabled()) {
                            logger.debug("         Converted to RGBW: Red={} Green={} Blue={} White={}", red & 0xFF,
                                    green & 0xFF, blue & 0xFF, white & 0xFF);
                        }

                        data[4] = (byte) (red & 0xFF);
                        data[5] = (byte) (green & 0xFF);
                        data[6] = (byte) (blue & 0xFF);
                        data[7] = white;
                    }
                }

                break;
            case ARRAY:
                if (command instanceof StringType) {
                    StringType cmd = (StringType) command;
                    String str = cmd.toString();

                    for (int i = 0; i < itemConfig.getDataLenght(); i++) {
                        if (str.length() <= itemConfig.getDataLenght()) {
                            data[6 + i] = (byte) str.charAt(i);
                        } else {
                            data[6 + i] = 0x0;
                        }
                    }
                } else {
                    logger.error("Unsupported command type {} for datatype {}", command.getClass().toString(),
                            itemConfig.getDataType());
                    return null;
                }
                break;
            default:
                return null;
        }

        data[datalen - 1] = evalCRC(data, datalen - 1);

        return data;
    }

    /**
     * Calculate white part for RGB
     *
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private static byte[] calcWhite(long red, long green, long blue) {

        byte[] result = new byte[4];
        float M = Math.max(Math.max(red, green), blue);
        float m = Math.min(Math.min(red, green), blue);

        float Wo = 0;

        double Ro = 0;
        double Go = 0;
        double Bo = 0;

        if (m > 0) {
            if (m / M < 0.5) {
                Wo = (m * M) / (M - m);
            } else {
                Wo = M;
            }

            // int Q = 100;

            float K = m / (Wo + M);

            Ro = Math.floor(red - K * Wo);
            Go = Math.floor(green - K * Wo);
            Bo = Math.floor(blue - K * Wo);
        } else {
            Ro = red;
            Go = green;
            Bo = blue;
            Wo = 0;
        }

        result[0] = (byte) Math.round(Ro);
        result[1] = (byte) Math.round(Go);
        result[2] = (byte) Math.round(Bo);
        result[3] = (byte) Math.round(Math.floor(Wo));

        return result;
    }

    /**
     * Decompile received message
     *
     * @param data
     * @param itemsConfig
     * @param deviceName
     * @return Decompiled data message
     * @throws NoValidCRCException
     * @throws NoValidItemInConfig
     * @throws UnknownMessageException
     * @throws ModeChangeException
     */
    public static SimpleBinaryMessage decompileData(SimpleBinaryByteBuffer data,
            Map<String, SimpleBinaryBindingConfig> itemsConfig, String deviceName)
            throws NoValidCRCException, NoValidItemInConfig, UnknownMessageException, ModeChangeException {

        return decompileData(data, itemsConfig, deviceName, false);
    }

    /**
     * Decompile received message
     *
     * @param data
     * @param itemsConfig
     * @param deviceName
     * @param letDataInBuffer
     * @return Decompiled data message
     * @throws NoValidCRCException
     * @throws NoValidItemInConfig
     * @throws UnknownMessageException
     * @throws ModeChangeException
     */
    public static SimpleBinaryMessage decompileData(SimpleBinaryByteBuffer data,
            Map<String, SimpleBinaryBindingConfig> itemsConfig, String deviceName, boolean letDataInBuffer)
            throws NoValidCRCException, NoValidItemInConfig, UnknownMessageException, ModeChangeException {
        byte devId = data.get();
        byte msgId = data.get();
        int address = 0;
        byte[] rawPacket;
        byte[] rawData = null;
        byte crc = 0, calcCrc;

        try {

            switch (msgId) {
                case (byte) 0xDA:
                    // check minimal length
                    if (data.remaining() < 4) {
                        return null;
                    }

                    address = data.getShort();
                    rawData = new byte[1];
                    rawData[0] = data.get();
                    data.rewind();
                    rawPacket = new byte[5];
                    data.get(rawPacket);
                    crc = data.get();
                    // check message crc
                    calcCrc = evalCRC(rawPacket);
                    if (crc != calcCrc) {
                        throw new NoValidCRCException(crc, calcCrc);
                    }

                    break;
                case (byte) 0xDB:
                    // check minimal length
                    if (data.remaining() < 5) {
                        return null;
                    }

                    address = data.getShort();
                    rawData = new byte[2];
                    data.get(rawData);
                    data.rewind();
                    rawPacket = new byte[6];
                    data.get(rawPacket);
                    crc = data.get();
                    // check message crc
                    calcCrc = evalCRC(rawPacket);
                    if (crc != calcCrc) {
                        throw new NoValidCRCException(crc, calcCrc);
                    }

                    break;

                case (byte) 0xDC:
                case (byte) 0xDD:
                    // check minimal length
                    if (data.remaining() < 7) {
                        return null;
                    }

                    address = data.getShort();
                    rawData = new byte[4];
                    data.get(rawData);
                    data.rewind();
                    rawPacket = new byte[8];
                    data.get(rawPacket);
                    crc = data.get();
                    // check message crc
                    calcCrc = evalCRC(rawPacket);
                    if (crc != calcCrc) {
                        throw new NoValidCRCException(crc, calcCrc);
                    }

                    break;

                case (byte) 0xDE:
                    // check minimal length
                    if (data.remaining() < 6) {
                        return null;
                    }

                    address = data.getShort();
                    int length = data.getShort();

                    // check declared length
                    if (data.remaining() < length + 1) {
                        return null;
                    }

                    byte[] array = new byte[length];
                    data.get(array);
                    rawData = array;
                    data.rewind();
                    rawPacket = new byte[6 + length];
                    data.get(rawPacket);
                    crc = data.get();
                    // check message crc
                    calcCrc = evalCRC(rawPacket);
                    if (crc != calcCrc) {
                        throw new NoValidCRCException(crc, calcCrc);
                    }

                    break;
                case (byte) 0xE0:
                case (byte) 0xE1:
                case (byte) 0xE2:
                case (byte) 0xE3:
                case (byte) 0xE4:
                case (byte) 0xE5:
                case (byte) 0xE6:
                    data.rewind();
                    rawPacket = new byte[3];
                    data.get(rawPacket);
                    crc = data.get();
                    // check message crc
                    calcCrc = evalCRC(rawPacket);
                    if (crc != calcCrc) {
                        throw new NoValidCRCException(crc, calcCrc);
                    }

                    return new SimpleBinaryMessage(msgId, devId, -1); // ,findItem(itemsConfig, deviceName, devId,
                                                                      // address)
                default:
                    // data.clear();

                    throw new UnknownMessageException(String.format("Unknown message ID: 0x%02X", msgId));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (letDataInBuffer) {
                data.rewind();
            }
        }

        if (rawData != null) {
            Map.Entry<String, SimpleBinaryBindingConfig> itemConfig = findItem(itemsConfig, deviceName, devId, address);

            if (itemConfig == null) {
                throw new NoValidItemInConfig(deviceName, devId, address);
            }

            SimpleBinaryItem item = new SimpleBinaryItem(itemConfig.getKey(), itemConfig.getValue(), msgId, devId,
                    address, rawData);

            return item;
        }

        return null;
    }

    /**
     * Try to find correct item
     *
     * @param itemsConfig
     * @param deviceName
     * @param devId
     * @param address
     * @return
     */
    public static Map.Entry<String, SimpleBinaryBindingConfig> findItem(
            Map<String, SimpleBinaryBindingConfig> itemsConfig, String deviceName, int devId, int address) {
        for (Map.Entry<String, SimpleBinaryBindingConfig> item : itemsConfig.entrySet()) {
            SimpleBinaryBindingConfig cfg = item.getValue();
            for (DeviceConfig d : cfg.devices) {
                if (d.getPortName().equals(deviceName) && d.getDeviceAddress() == devId
                        && d.getItemAddress() == address) {
                    return item;
                }
            }
        }

        return null;
    }

    /**
     * Return CRC8 of data
     *
     * @param data
     * @return
     */
    public static byte evalCRC(byte[] data) {
        return evalCRC(data, data.length);
    }

    /**
     * Return CRC8 of data with specific length
     *
     * @param data
     * @param length
     * @return
     */
    public static byte evalCRC(byte[] data, int length) {
        int crc = 0;
        int i, j;

        for (j = 0; j < length; j++) {
            crc ^= (data[j] << 8);

            for (i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc ^= (0x1070 << 3);
                }

                crc <<= 1;
            }
        }

        byte result = (byte) (crc >> 8);

        if (logger.isTraceEnabled()) {
            logger.trace("Counting CRC8 of: {}", arrayToString(data, length));
            logger.trace("CRC8 result: 0x{}", Integer.toHexString(result & 0xFF).toUpperCase());
        }

        return result;
    }

    /**
     * Convert data array to string
     *
     * @param data
     * @param length
     * @return
     */
    public static String arrayToString(byte[] data, int length) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < length; i++) {
            byte b = data[i];
            if (s.length() == 0) {
                s.append("[");
            } else {
                s.append(" ");
            }

            // if(SimpleBinaryBinding.JavaVersion >= 1.8)
            // s.append("0x" + Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase());
            // else
            s.append("0x" + Integer.toHexString(b & 0xFF).toUpperCase());
        }

        s.append("]");

        return s.toString();
    }
}
