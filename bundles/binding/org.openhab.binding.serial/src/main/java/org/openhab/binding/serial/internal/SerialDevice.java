/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.serial.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TooManyListenersException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * This class represents a serial device that is linked to one or many String, Number, Switch or Rollershutter items
 *
 * @author Kai Kreuzer
 *
 */
public class SerialDevice implements SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SerialDevice.class);

    private String port;
    private int baud = 9600;

    private EventPublisher eventPublisher;

    private CommPortIdentifier portId;
    private SerialPort serialPort;
    private Charset charset;

    private InputStream inputStream;

    private OutputStream outputStream;

    private Map<String, ItemType> configMap;

    class ItemType {
        String pattern;
        boolean base64;
        String onCommand;
        String offCommand;
        String upCommand;
        String downCommand;
        String stopCommand;
        String format;
        Class<?> type;
    }

    public boolean isEmpty() {
        return configMap.isEmpty();
    }

    public void addConfig(String itemName, Class<?> type, String pattern, boolean base64, String onCommand,
            String offCommand, String upCommand, String downCommand, String stopCommand, String format) {
        if (configMap == null) {
            configMap = new HashMap<>();
        }

        ItemType typeItem = new ItemType();
        typeItem.pattern = pattern;
        typeItem.base64 = base64;
        typeItem.type = type;
        typeItem.onCommand = onCommand;
        typeItem.offCommand = offCommand;
        typeItem.upCommand = upCommand;
        typeItem.downCommand = downCommand;
        typeItem.stopCommand = stopCommand;
        typeItem.format = format;

        configMap.put(itemName, typeItem);
    }

    public void removeConfig(String itemName) {
        if (configMap != null) {
            ItemType type = configMap.get(itemName);
            if (type.pattern != null) {
                // We can safely remove any pattern
                // If there are any duplicates, they will be added to cache next time they are requested
                RegexPatternMatcher.removePattern(type.pattern);
            }

            configMap.remove(itemName);
        }
    }

    public SerialDevice(String port) {
        this(port, null);
    }

    public SerialDevice(String port, String charsetName) {
        this(port, 9600, charsetName);
    }

    public SerialDevice(String port, int baud) {
        this(port, baud, null);
    }

    public SerialDevice(String port, int baud, String charsetName) {
        this.port = port;
        this.baud = baud;
        setCharset(charsetName);
    }

    private void setCharset(String charsetName) {
        try {
            if (charsetName == null) {
                charset = Charset.defaultCharset();
            } else {
                charset = Charset.forName(charsetName);
            }

            logger.debug("Serial port '{}' charset '{}' set.", port, charsetName);
        } catch (IllegalCharsetNameException e) {
            logger.warn("Serial port '{}' charset '{}' not found.", port, charsetName);
            charset = Charset.defaultCharset();
        }
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    public String getPort() {
        return port;
    }

    public String getOnCommand(String itemName) {
        if (configMap.get(itemName) != null) {
            return configMap.get(itemName).onCommand;
        }

        return "";
    }

    public String getOffCommand(String itemName) {
        if (configMap.get(itemName) != null) {
            return configMap.get(itemName).offCommand;
        }

        return "";
    }

    public String getUpCommand(String itemName) {
        if (configMap.get(itemName) != null) {
            return configMap.get(itemName).upCommand;
        }

        return "";
    }

    public String getDownCommand(String itemName) {
        if (configMap.get(itemName) != null) {
            return configMap.get(itemName).downCommand;
        }

        return "";
    }

    public String getStopCommand(String itemName) {
        if (configMap.get(itemName) != null) {
            return configMap.get(itemName).stopCommand;
        }

        return "";
    }

    public String getFormat(String itemName) {
        if (configMap.get(itemName) != null) {
            return configMap.get(itemName).format;
        }

        return "";
    }

    /**
     * Initialize this device and open the serial port
     *
     * @throws InitializationException if port cannot be opened
     */
    @SuppressWarnings("rawtypes")
    public void initialize() throws InitializationException {
        // parse ports and if the default port is found, initialized the reader
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (id.getName().equals(port)) {
                    logger.debug("Serial port '{}' has been found.", port);
                    portId = id;
                }
            }
        }
        if (portId != null) {
            // initialize serial port
            try {
                serialPort = portId.open("openHAB", 2000);
            } catch (PortInUseException e) {
                throw new InitializationException(e);
            }

            try {
                inputStream = serialPort.getInputStream();
            } catch (IOException e) {
                throw new InitializationException(e);
            }

            try {
                serialPort.addEventListener(this);
            } catch (TooManyListenersException e) {
                throw new InitializationException(e);
            }

            // activate the DATA_AVAILABLE notifier
            serialPort.notifyOnDataAvailable(true);

            try {
                // set port parameters
                serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            } catch (UnsupportedCommOperationException e) {
                throw new InitializationException(e);
            }

            try {
                // get the output stream
                outputStream = serialPort.getOutputStream();
            } catch (IOException e) {
                throw new InitializationException(e);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
                if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    sb.append(id.getName() + "\n");
                }
            }
            throw new InitializationException(
                    "Serial port '" + port + "' could not be found. Available ports are:\n" + sb.toString());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                // we get here if data has been received
                StringBuilder sb = new StringBuilder();
                byte[] readBuffer = new byte[20];
                try {
                    do {
                        // read data from serial device
                        while (inputStream.available() > 0) {
                            int bytes = inputStream.read(readBuffer);
                            sb.append(new String(readBuffer, 0, bytes, charset));
                        }
                        try {
                            // add wait states around reading the stream, so that interrupted transmissions are merged
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // ignore interruption
                        }
                    } while (inputStream.available() > 0);
                    // sent data
                    String result = sb.toString();

                    // send data to the bus
                    logger.debug("Received message '{}' on serial port {}", result, port);

                    if (eventPublisher != null) {
                        if (configMap != null && !configMap.isEmpty()) {
                            for (Entry<String, ItemType> entry : configMap.entrySet()) {
                                String pattern = entry.getValue().pattern;
                                // use pattern
                                if (pattern != null) {
                                    try {
                                        String[] matches = RegexPatternMatcher.getMatches(pattern, result);

                                        for (int i = 0; i < matches.length; i++) {
                                            String match = matches[i];

                                            try {
                                                State state = null;

                                                if (entry.getValue().type.equals(NumberItem.class)) {
                                                    state = new DecimalType(match);
                                                } else if (entry.getValue().type == RollershutterItem.class) {
                                                    state = new PercentType(match);
                                                } else {
                                                    state = new StringType(match);
                                                }

                                                eventPublisher.postUpdate(entry.getKey(), state);
                                            } catch (NumberFormatException e) {
                                                logger.warn("Unable to convert regex result '{}' for item {} to number",
                                                        result, entry.getKey());
                                            }
                                        }
                                    } catch (TransformationException e) {
                                        logger.warn("Unable to transform!", e);
                                    }
                                } else if (entry.getValue().type == StringItem.class) {
                                    if (entry.getValue().base64) {
                                        result = Base64.encodeBase64String(result.getBytes(charset));
                                    }
                                    eventPublisher.postUpdate(entry.getKey(), new StringType(result));

                                } else if (entry.getValue().type == SwitchItem.class) {
                                    if (result.trim().isEmpty()) {
                                        eventPublisher.postUpdate(entry.getKey(), OnOffType.ON);
                                        eventPublisher.postUpdate(entry.getKey(), OnOffType.OFF);
                                    } else if (result.equals(getOnCommand(entry.getKey()))) {
                                        eventPublisher.postUpdate(entry.getKey(), OnOffType.ON);
                                    } else if (result.equals(getOffCommand(entry.getKey()))) {
                                        eventPublisher.postUpdate(entry.getKey(), OnOffType.OFF);
                                    }
                                } else if (entry.getValue().type == ContactItem.class) {
                                    if (result.trim().isEmpty()) {
                                        eventPublisher.postUpdate(entry.getKey(), OpenClosedType.CLOSED);
                                        eventPublisher.postUpdate(entry.getKey(), OpenClosedType.OPEN);
                                    } else if (result.equals(getOnCommand(entry.getKey()))) {
                                        eventPublisher.postUpdate(entry.getKey(), OpenClosedType.CLOSED);
                                    } else if (result.equals(getOffCommand(entry.getKey()))) {
                                        eventPublisher.postUpdate(entry.getKey(), OpenClosedType.OPEN);
                                    }
                                } else if (entry.getValue().type == RollershutterItem.class
                                        || entry.getValue().type == DimmerItem.class) {
                                    if (result.trim().isEmpty()) {
                                        eventPublisher.postUpdate(entry.getKey(), new PercentType(50));
                                    } else if (result.equals(getUpCommand(entry.getKey()))) {
                                        eventPublisher.postUpdate(entry.getKey(), PercentType.HUNDRED);
                                    } else if (result.equals(getDownCommand(entry.getKey()))) {
                                        eventPublisher.postUpdate(entry.getKey(), PercentType.ZERO);
                                    } else if (result.equals(getStopCommand(entry.getKey()))) {
                                        eventPublisher.postUpdate(entry.getKey(), new PercentType(50));
                                    }
                                }
                            }
                        }

                    }

                } catch (IOException e) {
                    logger.debug("Error receiving data on serial port {}: {}", port, e.getMessage());
                }
                break;
        }
    }

    /**
     * Sends a string to the serial port of this device
     *
     * @param msg the string to send
     */
    public void writeString(String msg) {
        logger.debug("Writing '{}' to serial port {}", msg, port);
        try {
            // write string to serial port
            if (msg.startsWith("BASE64:")) {
                outputStream.write(Base64.decodeBase64(msg.substring(7, msg.length())));
            } else {
                outputStream.write(msg.getBytes(charset));
            }

            outputStream.flush();
        } catch (IOException e) {
            logger.warn("Error writing '{}' to serial port {}: {}", msg, port, e.getMessage());
        }
    }

    /**
     * Close this serial device
     */
    public void close() {
        serialPort.removeEventListener();
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        serialPort.close();
    }
}
