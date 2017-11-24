/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package be.devlaminck.openwebnet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myhome.fcrisciani.connector.MyHomeJavaConnector;

/**
 * This thread reads every event on the bticino bus and then converts & publishes
 * it to the openhab
 *
 * Based on code from Mauro Cicolella (as part of the FREEDOMOTIC framework)
 * (https ://github.com/freedomotic/freedomotic/tree/master/plugins/devices/
 * openwebnet) and on code of Flavio Fcrisciani
 * (https://github.com/fcrisciani/java-myhome-library) released under EPL
 *
 * @author Tom De Vlaminck, Lago Moreno, Andrea Carabillo
 * @serial 1.0
 * @since 1.7.0
 */
public class MonitorSessionThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(MonitorSessionThread.class);

    private OpenWebNet pluginReference = null;
    private String ipAddress = null;
    private Integer port = 0;
    private String passwd = null;

    @Override
    public void run() {
        // connect to own gateway
        logger.debug("Connecting to ipaddress {} on port {} with passord {}", ipAddress, port, passwd);
        pluginReference.myPlant = new MyHomeJavaConnector(ipAddress, port, passwd);
        try {
            pluginReference.myPlant.startMonitoring();
            while (!Thread.interrupted()) {
                try {
                    String readFrame = pluginReference.myPlant.readMonitoring();
                    if (readFrame != null) {
                        buildEventFromFrame(readFrame);
                    }
                } catch (InterruptedException ex) {
                    logger.error("MonitorSessionThread.run, exception : " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            logger.error("MonitorSessionThread.run, exception : " + ex.getMessage());
        }
        logger.info("Stopped MonitorSessionThread thread");
    }

    public MonitorSessionThread(OpenWebNet pluginReference, String ipAddress, Integer port) {
        this(pluginReference, ipAddress, port, "");
    }

    public MonitorSessionThread(OpenWebNet pluginReference, String ipAddress, Integer port, String passwd) {
        this.pluginReference = pluginReference;
        this.ipAddress = ipAddress;
        this.port = port;
        this.passwd = passwd;
    }

    public void buildEventFromFrame(String frame) {
        logger.debug("Received OpenWebNet frame '{}' now translate it to an event.", frame);
        String who = null;
        String what = null;
        String where = null;
        String objectClass = null;
        String objectName = null;
        String messageType = null;
        String messageDescription = null;
        String[] frameParts = null;
        ProtocolRead event = null;

        if (frame.isEmpty()) {
            logger.error("Empty frame");
            return;
        }

        int length = frame.length();
        if (!frame.endsWith("##")) {
            logger.error("Malformed frame " + frame + " " + frame.substring(length - 2, length));
            return;
        }

        if (frame.equals(OWNUtilities.MSG_OPEN_ACK)) {
            messageType = "ack";
            return;
        }

        if (frame.equals(OWNUtilities.MSG_OPEN_NACK)) {
            messageType = "nack";
            return;
        }

        // Status request frame
        if (frame.substring(0, 2).equalsIgnoreCase("*#")) {
            // remove *# and ##
            frame = frame.substring(2, length - 2);

            // remove *# and ##
            frameParts = frame.split("\\*"); // * is reserved so it must be
                                             // escaped
            who = frameParts[0];
            where = frameParts[1];
            objectName = who + "*" + where;
            event = new ProtocolRead(frame);

            if (who.equalsIgnoreCase("1")) {
                objectClass = "Light";
                objectName = who + "*" + where;
                if (frameParts[2].equalsIgnoreCase("1")) {
                    String level = frameParts[3];
                    String speed = frameParts[4];
                    messageDescription = "Luminous intensity change";
                    if (level != null) {
                        event.addProperty("level", level);
                    }
                    if (speed != null) {
                        event.addProperty("speed", speed);
                    }
                }
                if (frameParts[2].equalsIgnoreCase("2")) {
                    String hour = frameParts[3];
                    String min = frameParts[4];
                    String sec = frameParts[5];
                    messageDescription = "Luminous intensity change";
                    if (hour != null) {
                        event.addProperty("hour", hour);
                    }
                    if (min != null) {
                        event.addProperty("min", min);
                    }
                    if (sec != null) {
                        event.addProperty("sec", sec);
                    }
                }
            }
            // POWER MANAGEMENT
            if (who.equalsIgnoreCase("3")) {
                objectClass = "Powermeter";
                objectName = who + "*" + where;
                String voltage = null;
                String current = null;
                String power = null;
                String energy = null;
                if (frameParts[3].equalsIgnoreCase("0")) {
                    voltage = frameParts[3];
                    current = frameParts[4];
                    power = frameParts[5];
                    energy = frameParts[6];
                    messageDescription = "Load control status";
                    if (voltage != null) {
                        event.addProperty("voltage", voltage);
                    }
                    if (current != null) {
                        event.addProperty("current", current);
                    }
                    if (power != null) {
                        event.addProperty("power", power);
                    }
                    if (energy != null) {
                        event.addProperty("energy", energy);
                    }
                }
                if (frameParts[3].equalsIgnoreCase("1")) {
                    voltage = frameParts[3];
                    if (voltage != null) {
                        event.addProperty("voltage", voltage);
                    }
                    messageDescription = "Voltage status";
                }
                if (frameParts[3].equalsIgnoreCase("2")) {
                    current = frameParts[3];
                    if (current != null) {
                        event.addProperty("current", current);
                    }
                    messageDescription = "Current status";
                }
                if (frameParts[3].equalsIgnoreCase("3")) {
                    power = frameParts[3];
                    if (power != null) {
                        event.addProperty("power", power);
                    }
                    messageDescription = "Power status";
                }
                if (frameParts[3].equalsIgnoreCase("4")) {
                    energy = frameParts[3];
                    if (energy != null) {
                        event.addProperty("energy", energy);
                    }
                    messageDescription = "Energy status";
                }
            }
            // TERMOREGULATION
            if (who.equalsIgnoreCase("4")) {
                messageType = "thermoregulation";
                objectClass = "Thermo";
                objectName = who + "*" + where;

                if (frameParts[2].equalsIgnoreCase("0")) {
                    String temperature = frameParts[3];
                    temperature = OWNUtilities.convertTemperature(temperature);
                    messageDescription = "Temperature value";
                    if (temperature != null) {
                        event.addProperty("temperature", temperature);
                    }
                } else {
                    logger.debug("other temperature message");

                }
            }
            // GATEWAY CONTROL
            if (who.equalsIgnoreCase("13")) {
                objectClass = "Gateway";
                objectName = who;

                String hour = null;
                String minute = null;
                String second = null;
                String timeZone = null;
                String dayWeek = null;
                String day = null;
                String month = null;
                String year = null;
                String version = null;
                String release = null;
                String build = null;
                if (frameParts[2].equalsIgnoreCase("0")) {
                    hour = frameParts[3];
                    minute = frameParts[4];
                    second = frameParts[5];
                    timeZone = frameParts[6]; // aggiungere funzione conversione
                    messageType = "gatewayControl";
                    messageDescription = "Time request";
                    if (hour != null) {
                        event.addProperty("hour", hour);
                    }
                    if (minute != null) {
                        event.addProperty("minute", minute);
                    }
                    if (second != null) {
                        event.addProperty("second", second);
                    }
                    if (timeZone != null) {
                        event.addProperty("timeZone", timeZone);
                    }
                }
                if (frameParts[2].equalsIgnoreCase("1")) {
                    dayWeek = OWNUtilities.dayName(frameParts[3]);
                    day = frameParts[4];
                    month = frameParts[5];
                    year = frameParts[6];
                    messageType = "gatewayControl";
                    messageDescription = "Date request";
                    if (dayWeek != null) {
                        event.addProperty("dayWeek", dayWeek);
                    }
                    if (day != null) {
                        event.addProperty("day", day);
                    }
                    if (month != null) {
                        event.addProperty("month", month);
                    }
                    if (year != null) {
                        event.addProperty("year", year);
                    }
                }
                if (frameParts[2].equalsIgnoreCase("10")) {
                    String ip1 = frameParts[3];
                    String ip2 = frameParts[4];
                    String ip3 = frameParts[5];
                    String ip4 = frameParts[6];
                    messageType = "gatewayControl";
                    messageDescription = "IP request";
                    event.addProperty("ip-address", ip1 + "." + ip2 + "." + ip3 + "." + ip4);
                }
                if (frameParts[2].equalsIgnoreCase("11")) {
                    String netmask1 = frameParts[3];
                    String netmask2 = frameParts[4];
                    String netmask3 = frameParts[5];
                    String netmask4 = frameParts[6];
                    messageType = "gatewayControl";
                    messageDescription = "Netmask request";
                    event.addProperty("netmask", netmask1 + "." + netmask2 + "." + netmask3 + "." + netmask4);
                }
                if (frameParts[2].equalsIgnoreCase("12")) {
                    String mac1 = frameParts[3];
                    String mac2 = frameParts[4];
                    String mac3 = frameParts[5];
                    String mac4 = frameParts[6];
                    String mac5 = frameParts[7];
                    String mac6 = frameParts[8];
                    messageType = "gatewayControl";
                    messageDescription = "MAC request";
                    event.addProperty("mac-address",
                            mac1 + ":" + mac2 + ":" + mac3 + ":" + mac4 + ":" + mac5 + ":" + mac6);
                }
                if (frameParts[2].equalsIgnoreCase("15")) {
                    String model = OWNUtilities.gatewayModel(frameParts[3]);
                    messageType = "gatewayControl";
                    messageDescription = "Model request";
                    event.addProperty("model", model);
                }
                if (frameParts[2].equalsIgnoreCase("16")) {
                    version = frameParts[3];
                    release = frameParts[4];
                    build = frameParts[5];
                    messageType = "gatewayControl";
                    messageDescription = "Firmware version request";
                    event.addProperty("firmware - version", version + "." + release + "." + build);
                }
                if (frameParts[2].equalsIgnoreCase("17")) {
                    String days = frameParts[3];
                    String hours = frameParts[4];
                    String minutes = frameParts[5];
                    String seconds = frameParts[6];
                    messageType = "gatewayControl";
                    messageDescription = "Uptime request";
                    event.addProperty("uptime ", days + "D:" + hours + "H:" + minutes + "m:" + seconds + "s");
                }
                if (frameParts[2].equalsIgnoreCase("22")) {
                    hour = frameParts[3];
                    minute = frameParts[4];
                    second = frameParts[5];
                    timeZone = frameParts[6];
                    String weekDay = OWNUtilities.dayName(frameParts[7]);
                    day = frameParts[8];
                    month = frameParts[9];
                    year = frameParts[10];
                    messageType = "gatewayControl";
                    messageDescription = "Date&Time request";
                    event.addProperty("date", weekDay + " " + day + "/" + month + "/" + year);
                    event.addProperty("time", hour + ":" + minute + ":" + second + " (" + timeZone + ")");
                }
                if (frameParts[2].equalsIgnoreCase("23")) {
                    version = frameParts[3];
                    release = frameParts[4];
                    build = frameParts[5];
                    messageType = "gatewayControl";
                    messageDescription = "Kernel version request";
                    event.addProperty("kernel - version", version + "." + release + "." + build);
                }
                if (frameParts[2].equalsIgnoreCase("24")) {
                    version = frameParts[3];
                    release = frameParts[4];
                    build = frameParts[5];
                    messageType = "gatewayControl";
                    messageDescription = "Distribution version request";
                    event.addProperty("distribution - version", version + "." + release + "." + build);
                }
            }
            // Basic and evolved CEN
            if (who.equalsIgnoreCase("15")) {
                objectClass = "CEN_Basic_Evolved";
                objectName = who + "*" + where;
                what = frameParts[2];
                String[] what_parts = what.split("#");

                if (what_parts.length == 1) {
                    // push button n
                    event.addProperty("push_button_n", what_parts[0]);
                    // type of pressure
                    event.addProperty("pressure", "Virtual pressure");

                } else if (what_parts.length == 2) {
                    // push button n
                    event.addProperty("push_button_n", what_parts[0]);
                    if (what_parts[0].equalsIgnoreCase("1")) {
                        // type of pressure
                        event.addProperty("pressure", "Virtual release after short pressure");
                    }
                    if (what_parts[0].equalsIgnoreCase("2")) {
                        // type of pressure
                        event.addProperty("pressure", "Virtual release after an extended pressure");
                    }
                    if (what_parts[0].equalsIgnoreCase("3")) {
                        // type of pressure
                        event.addProperty("pressure", "Virtual extended pressure");
                    }
                } else {
                    logger.debug("other CEN Basic or Evolved message");
                }
            }

            event.addProperty("who", who);
            if (where != null) {
                event.addProperty("where", where);
            }
            if (messageDescription != null) {
                event.addProperty("messageDescription", messageDescription);
            }
            if (messageType != null) {
                event.addProperty("messageType", messageType);
            }
            // notify event
            logger.debug("{} Rx: {} ({})", OWNUtilities.getDateTime(), frame, messageDescription);

            // Notify all the listeners an event has been received
            pluginReference.notifyEvent(event);
        }

        // Command frame
        if (!(frame.substring(0, 2).equalsIgnoreCase("*#")) && (frame.substring(0, 1).equalsIgnoreCase("*"))) {
            // remove delimiter chars * and ##
            frame = frame.substring(1, length - 2);
            frameParts = frame.split("\\*"); // * is reserved so it must be
                                             // escaped
            who = frameParts[0];
            what = frameParts[1];
            // Burglar Central Unit Status Request = *#5##
            if (frameParts.length >= 3) {
                where = frameParts[2];
            } else {
                where = "";
            }
            event = new ProtocolRead(frame);
            objectName = who + "*" + where;
            boolean virtual_where = false;
            String[] what_parts;
            switch (Integer.parseInt(who)) {
                // LIGHTING
                case 1:
                    messageType = "Lighting";
                    objectClass = "Light";

                    // For virtual configuration we receive for light on 1000#1
                    // so assuming the second part is the what
                    what_parts = what.split("#");
                    if (what_parts.length > 1) {
                        virtual_where = true;
                        // take the last part for the what
                        what = what_parts[what_parts.length - 1];
                    }

                    switch (Integer.parseInt(what)) {
                        // Light OFF
                        case 0:
                            messageDescription = "Light OFF";
                            break;
                        // Light ON
                        case 1:
                            messageDescription = "Light ON";
                            break;
                        default:
                            if (Integer.parseInt(what) >= 2 && Integer.parseInt(what) <= 10) {
                                messageDescription = "Light Dimmer";
                            }
                            break;
                    }
                    break; // close LIGHTING switch

                // AUTOMATION
                case 2:
                    messageType = "Automation";
                    objectClass = "Automation";

                    switch (Integer.parseInt(what)) {
                        case 0:
                            messageDescription = "Automation STOP";
                            break;
                        case 1:
                            messageDescription = "Automation UP";
                            break;
                        case 2:
                            messageDescription = "Automation DOWN";
                            break;
                    }
                    break; // close AUTOMATION switch

                // POWER MANAGEMENT
                case 3:
                    objectClass = "Powermeter";
                    messageType = "Power management";
                    switch (Integer.parseInt(what)) {
                        case 0:
                            messageDescription = "Load disable";
                            break;
                        case 1:
                            messageDescription = "Load enable";
                            break;
                        case 2:
                            messageDescription = "Load forced";
                            break;
                        case 3:
                            messageDescription = "Stop load forced";
                            break;
                    }
                    break; // close POWER MANAGEMENT switch

                // THERMOREGULATION
                case 4:
                    messageType = "thermoregulation";
                    objectClass = "Thermo";

                    switch (Integer.parseInt(what)) {
                        case 0:
                            messageDescription = "Conditioning";
                            break;
                        case 1:
                            messageDescription = "Heating";
                            break;
                        case 20:
                            messageDescription = "Remote Control disabled";
                            break;
                        case 21:
                            messageDescription = "Remote Control enabled";
                            break;
                        case 22:
                            messageDescription = "At least one Probe OFF";
                            break;
                        case 23:
                            messageDescription = "At least one Probe in protection";
                            break;
                        case 24:
                            messageDescription = "At least one Probe in manual";
                            break;
                        case 30:
                            messageDescription = "Failure discovered";
                            break;
                        case 31:
                            messageDescription = "Central Unit battery KO";
                            break;
                        case 103:
                            messageDescription = "OFF Heating";
                            break;
                        case 110:
                            messageDescription = "Manual Heating";
                            break;
                        case 111:
                            messageDescription = "Automatic Heating";
                            break;
                        case 202:
                            messageDescription = "AntiFreeze";
                            break;
                        case 203:
                            messageDescription = "OFF Conditioning";
                            break;
                        case 210:
                            messageDescription = "Manual Conditioning";
                            break;
                        case 211:
                            messageDescription = "Automatic Conditioning";
                            break;
                        case 302:
                            messageDescription = "Thermal Protection";
                            break;
                        case 303:
                            messageDescription = "Generic OFF";
                            break;
                        case 311:
                            messageDescription = "Automatic Generic";
                            break;
                    }
                    break; // close THERMOREGULATION switch

                // BURGLAR ALARM
                case 5:
                    messageType = "alarm";
                    objectClass = "Alarm";

                    switch (Integer.parseInt(what)) {
                        case 0:
                            messageDescription = "System on maintenance";
                            break;
                        case 4:
                            messageDescription = "Battery fault";
                            break;
                        case 5:
                            messageDescription = "Battery OK";
                            break;
                        case 6:
                            messageDescription = "No Network";
                            break;
                        case 7:
                            messageDescription = "Network OK";
                            break;
                        case 8:
                            messageDescription = "System engaged";
                            break;
                        case 9:
                            messageDescription = "System disengaged";
                            break;
                        case 10:
                            messageDescription = "Battery KO";
                            break;
                        case 11: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Zone " + where + " engaged";
                        }
                            break;
                        case 12: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Aux " + where + " in Technical alarm ON";
                        }
                            break;
                        case 13: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Aux " + where + " in Technical alarm RESET";
                        }
                            break;
                        case 15: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Zone " + where + " in Intrusion alarm";
                        }
                            break;
                        case 16: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Zone " + where + " in Tampering alarm";
                        }
                            break;
                        case 17: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Zone " + where + " in Anti-panic alarm";
                        }
                            break;
                        case 18: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Zone " + where + " divided";
                        }
                            break;
                        case 31: // prelevare la sottostringa di where #N
                        {
                            messageDescription = "Silent alarm from aux " + where;
                        }
                            break;
                    }
                    break; // close BURGLAR ALARM switch

                // SOUND SYSTEM
                case 16:
                    messageType = "Sound System";
                    objectClass = "Sound";

                    switch (Integer.parseInt(what)) {
                        case 0:
                            messageDescription = "ON Baseband";
                            break;
                        case 3:
                            messageDescription = "ON Stereo channel";
                            break;
                        case 10:
                            messageDescription = "OFF Baseband";
                            break;
                        case 13:
                            messageDescription = "OFF Stereo channel";
                            break;
                        case 30:
                            messageDescription = "Sleep on baseband";
                            break;
                        case 33:
                            messageDescription = "Sleep on stereo channel";
                            break;
                    }
                    break; // close SOUND SYSTEM switch

                // CEN (Basic & Evolved)
                case 15:
                    messageType = "CEN Basic and Evolved";
                    objectClass = "CEN";

                    what_parts = what.split("#");

                    if (what_parts.length == 1) {
                        // type of pressure
                        messageDescription = "Virtual pressure";

                    } else if (what_parts.length == 2) {
                        if (what_parts[0].equalsIgnoreCase("1")) {
                            // type of pressure
                            messageDescription = "Virtual release after short pressure";
                        } else if (what_parts[0].equalsIgnoreCase("2")) {
                            // type of pressure
                            messageDescription = "Virtual release after an extended pressure";
                        } else if (what_parts[0].equalsIgnoreCase("3")) {
                            // type of pressure
                            messageDescription = "Virtual extended pressure";
                        }
                    } else {
                        messageDescription = "other CEN Basic or Evolved message";
                    }
            } // close switch(who)

            if (who != null) {
                event.addProperty("who", who);
            }
            if (what != null) {
                event.addProperty("what", what);
            }
            if (where != null) {
                event.addProperty("where", where);
                // Indicate virtual where message
                event.addProperty("virtual", virtual_where ? "true" : "false");
            }
            if (messageType != null) {
                event.addProperty("messageType", messageType);
            }
            if (messageDescription != null) {
                event.addProperty("messageDescription", messageDescription);
            }
            if (objectClass != null) {
                event.addProperty("object.class", objectClass);
            }
            if (objectName != null) {
                event.addProperty("object.name", objectName);
            }
            logger.debug("Frame {} is {} message. Notify it as OpenHab event {}", frame, messageType,
                    (messageDescription == "No Description set") ? "" : messageDescription); // for debug

            // Notify all the listeners an event has been received
            pluginReference.notifyEvent(event);
        }
    }
}
