package org.openhab.binding.dsmr.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.SerialPort;

/**
 * Class for storing port settings
 * This class does store 4 serial parameters (baudrate, databits, parity, stopbits)
 * for use in DSMRPort.
 *
 * This class can also convert a string setting (<speed> <databits><parity><stopbits>)
 * to a DSMRPortSettings object (e.g. 115200 8N1)
 *
 * @author M. Volaart
 * @since 1.9.0
 */
public class DSMRPortSettings {
    /* logger */
    private static final Logger logger = LoggerFactory.getLogger(DSMRPortSettings.class);

    /* Regular expression for validating portsettings parameter */
    private static final String PORT_SETTING_REGEX = "(\\d+)\\s+(\\d)([neoNEO])(1\\.5|1|2)";

    /* Fixed settings for high speed communication (DSMR V4 and up) */
    public static final DSMRPortSettings HIGH_SPEED_SETTINGS = new DSMRPortSettings(115200, SerialPort.DATABITS_8,
            SerialPort.PARITY_NONE, SerialPort.STOPBITS_1);

    /* Fixed settings for low speed communication (DSMR V3 and down) */
    public static final DSMRPortSettings LOW_SPEED_SETTINGS = new DSMRPortSettings(9600, SerialPort.DATABITS_7,
            SerialPort.PARITY_EVEN, SerialPort.STOPBITS_1);

    /* Serial port parameters */
    private final int baudrate;
    private final int databits;
    private final int parity;
    private final int stopbits;

    /**
     * Construct a new PortSpeed object
     *
     * @param baudrate
     *            baudrate of the port
     * @param databits
     *            no data bits to use (use SerialPort.DATABITS_* constant)
     * @param parity
     *            parity to use (use SerialPort.PARITY_* constant)
     * @param stopbits
     *            no stopbits to use (use SerialPort.STOPBITS_* constant)
     */
    public DSMRPortSettings(int baudrate, int databits, int parity, int stopbits) {
        this.baudrate = baudrate;
        this.databits = databits;
        this.parity = parity;
        this.stopbits = stopbits;
    }

    /**
     * Returns the baudrate
     *
     * @return baudrate setting
     */
    public int getBaudrate() {
        return baudrate;
    }

    /**
     * Returns the number of data bits
     *
     * @return databits setting
     */
    public int getDataBits() {
        return databits;
    }

    /**
     * Returns the parity setting
     *
     * @return parity setting
     */
    public int getParity() {
        return parity;
    }

    /**
     * Returns the number of stop bits
     *
     * @return stop bits setting
     */
    public int getStopbits() {
        return stopbits;
    }

    @Override
    public String toString() {
        String toString = "Baudrate:" + baudrate + ", databits:" + databits;

        switch (parity) {
            case SerialPort.PARITY_EVEN:
                toString += ", parity:even";
                break;
            case SerialPort.PARITY_MARK:
                toString += ", parity:mark";
                break;
            case SerialPort.PARITY_NONE:
                toString += ", parity:none";
                break;
            case SerialPort.PARITY_ODD:
                toString += ", parity:odd";
                break;
            case SerialPort.PARITY_SPACE:
                toString += ", parity:space";
                break;
            default:
                toString += ", parity:<unknown>";
                break;
        }
        switch (stopbits) {
            case SerialPort.STOPBITS_1:
                toString += ", stopbits:1";
                break;
            case SerialPort.STOPBITS_1_5:
                toString += ", stopbits:1.5";
                break;
            case SerialPort.STOPBITS_2:
                toString += ", stopbits:2";
                break;
            default:
                toString += ", stopbits:<unknown>";
                break;
        }
        return toString;
    }

    /**
     *
     * @param portSettings
     * @return
     */
    public static DSMRPortSettings getPortSettingsFromString(String portSettings) {
        Matcher m = Pattern.compile(PORT_SETTING_REGEX).matcher(portSettings);

        if (m.find()) {
            int baudrate = Integer.parseInt(m.group(1));
            int databits = Integer.parseInt(m.group(2));
            int parity;
            int stopbits;

            char parityChar = m.group(3).toUpperCase().charAt(0);
            switch (parityChar) {
                case 'E':
                    parity = SerialPort.PARITY_EVEN;
                    break;
                case 'O':
                    parity = SerialPort.PARITY_ODD;
                    break;
                case 'N':
                    parity = SerialPort.PARITY_NONE;
                    break;
                default:
                    logger.error("Invalid parity ({}), ignoring fixed port settings.", parityChar);

                    return null;
            }
            String stopbitsString = m.group(4);

            if (stopbitsString.equals("1")) {
                stopbits = SerialPort.STOPBITS_1;
            } else if (stopbitsString.equals("1.5")) {
                stopbits = SerialPort.STOPBITS_1_5;
            } else if (stopbitsString.equals("2")) {
                stopbits = SerialPort.STOPBITS_2;
            } else {
                logger.error("Invalid stop bits({}), ignoring fixed port settings.", stopbitsString);

                return null;
            }
            return new DSMRPortSettings(baudrate, databits, parity, stopbits);
        } else {
            return null;
        }
    }
}
