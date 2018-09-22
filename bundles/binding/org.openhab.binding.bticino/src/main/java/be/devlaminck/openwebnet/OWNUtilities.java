/**
 * Copyright (c) 2010-2018, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package be.devlaminck.openwebnet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Utilities for OpenWebNet - OpenHab binding Based on code from Mauro Cicolella
 * (as part of the FREEDOMOTIC framework)
 * (https://github.com/freedomotic/freedomotic
 * /tree/master/plugins/devices/openwebnet) and on code of Flavio Fcrisciani
 * release as EPL (https://github.com/fcrisciani/java-myhome-library)
 *
 * @author Tom De Vlaminck, LAGO Moreno
 * @author Reinhard Freuis - various enhancements for heating, rollershutter
 * @serial 1.0
 * @since 1.7.0
 *
 */
public class OWNUtilities {

    // ------------------------------------------------------------------------

    /**
     * OWN Control Messages
     */
    public final static String MSG_OPEN_ACK = "*#*1##";
    public final static String MSG_OPEN_NACK = "*#*0##";

    // ------------------------------------------------------------------------

    /**
     * Create the frame to send to the own gateway
     *
     * @param c
     * @return
     */
    public static String createFrame(ProtocolRead c) {
        String frame = null;
        String address[] = null;
        String who = null;
        String what = null;
        String where = null;

        who = c.getProperty("who");
        what = c.getProperty("what");
        address = c.getProperty("address").split("\\*");
        where = address[0];

        if (who.equals("4")) {
            // Heating Control
            if (what.substring(0, 2).equals("14")) {
                // Change Temperature Set Point
                frame = "*#" + who + "*#" + where + "*#" + what + "##";
            } else {
                switch (Integer.parseInt(what)) {
                    case 40:
                        // Request Temperature Probe
                        frame = "*" + who + "*" + what + "*" + where + "##";
                        break;
                    case 110:
                        // Change to Manual Mode with fixed SP 20°C
                        frame = "*#" + who + "*#" + where + "*#14*0200*3##";
                        break;
                    default:
                        // Change Modes
                        frame = "*" + who + "*" + what + "*#" + where + "##";
                        break;
                }
            }
        } else {
            frame = "*" + who + "*" + what + "*" + where + "##";
        }

        return (frame);
    }

    // ------------------------------------------------------------------------

    /**
     * Get datetime on format "dd/MM/yyyy HH:mm:ss"
     *
     * @return
     */
    public static String getDateTime() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return (sdf.format(calendar.getTime()));
    }

    // ------------------------------------------------------------------------

    /**
     * Convert temperature from bticino bus format to readable format
     *
     * @param temperature
     * @return
     */
    public static String convertTemperature(String temperature) {
        String temp = "";

        switch (temperature.length()) {
            case 1:
                // for positive set point shift at probe
                temp = temperature.substring(0, 1);
                break;
            case 2:
                // for negative set point shift at probe
                if (!temperature.substring(0, 1).equalsIgnoreCase("0")) {
                    temp = "-";
                }
                temp += temperature.substring(1, 2);
                break;
            default:
                temp = temperature.substring(1, 3);
                temp += ".";
                temp += temperature.substring(3);
                break;
        }
        return (temp);
    }

    // ------------------------------------------------------------------------

    /**
     * Convert day from number to name
     *
     * @param dayNumber
     * @return
     */
    public static String dayName(String dayNumber) {
        String dayName = null;
        switch (Integer.parseInt(dayNumber)) {
            case (0):
                dayName = "Sunday";
                break;
            case (1):
                dayName = "Monday";
                break;
            case (2):
                dayName = "Tuesday";
                break;
            case (3):
                dayName = "Wednesday";
                break;
            case (4):
                dayName = "Thursday";
                break;
            case (5):
                dayName = "Friday";
                break;
            case (6):
                dayName = "Saturday";
                break;
            default:
                dayName = "Invalid day number [" + dayNumber + "]";
        }
        return (dayName);
    }

    // ------------------------------------------------------------------------

    /**
     * Get model name from model number
     *
     * @param modelNumber
     * @return
     */
    public static String gatewayModel(String modelNumber) {
        String model = null;
        switch (new Integer(Integer.parseInt(modelNumber))) {
            case (2):
                model = "MHServer";
            case (4):
                model = "MH20F0";
            case (6):
                model = "F452V";
            case (11):
                model = "MHServer2";
            case (12):
                model = "F453AV";
            case (13):
                model = "H4684";
            default:
                model = "Unknown";
        }
        return (model);
    }

    // ------------------------------------------------------------------------
}
