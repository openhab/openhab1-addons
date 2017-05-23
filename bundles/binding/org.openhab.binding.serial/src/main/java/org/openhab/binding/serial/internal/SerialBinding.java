/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.serial.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;

/**
 * <p>
 * This class implements a binding of serial devices to openHAB.
 * The binding configurations are provided by the {@link GenericItemProvider}.
 * </p>
 *
 * <p>
 * The format of the binding configuration is simple and looks like this:
 * </p>
 * serial="&lt;port&gt;" where &lt;port&gt; is the identification of the serial port on the host system, e.g.
 * "COM1" on Windows, "/dev/ttyS0" on Linux or "/dev/tty.PL2303-0000103D" on Mac
 * <p>
 * Switch items with this binding will receive an ON-OFF update on the bus, whenever data becomes available on the
 * serial interface<br/>
 * String items will receive the submitted data in form of a string value as a status update, while openHAB commands to
 * a Switch item is
 * sent out as data through the serial interface.
 * </p>
 *
 * @author Kai Kreuzer
 *
 */
public class SerialBinding extends AbstractEventSubscriber implements BindingConfigReader {

    private Map<String, SerialDevice> serialDevices = new HashMap<>();

    /**
     * stores information about the which items are associated to which port. The map has this content structure:
     * itemname -> port
     */
    private Map<String, String> itemMap = new HashMap<String, String>();

    /**
     * stores information about the context of items. The map has this content structure: context -> Set of itemNames
     */
    private Map<String, Set<String>> contextMap = new HashMap<>();

    private EventPublisher eventPublisher = null;

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;

        for (SerialDevice serialDevice : serialDevices.values()) {
            serialDevice.setEventPublisher(eventPublisher);
        }
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;

        for (SerialDevice serialDevice : serialDevices.values()) {
            serialDevice.setEventPublisher(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveCommand(String itemName, Command command) {
        if (itemMap.keySet().contains(itemName)) {
            SerialDevice serialDevice = serialDevices.get(itemMap.get(itemName));
            if (command instanceof StringType) {
                serialDevice.writeString(command.toString());
            } else if (command instanceof OnOffType) {
                if (command == OnOffType.ON) {
                    serialDevice.writeString(serialDevice.getOnCommand(itemName));
                } else {
                    serialDevice.writeString(serialDevice.getOffCommand(itemName));
                }
            } else if (command instanceof UpDownType) {
                if (command == UpDownType.UP) {
                    serialDevice.writeString(serialDevice.getUpCommand(itemName));
                } else {
                    serialDevice.writeString(serialDevice.getDownCommand(itemName));
                }
            } else if (command instanceof StopMoveType) {
                if (command == StopMoveType.STOP) {
                    serialDevice.writeString(serialDevice.getStopCommand(itemName));
                }
            } else if (command instanceof PercentType) {
                serialDevice.writeString(command.format(serialDevice.getFormat(itemName)));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveUpdate(String itemName, State newStatus) {
        // ignore any updates
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "serial";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof StringItem || item instanceof NumberItem
                || item instanceof RollershutterItem || item instanceof ContactItem || item instanceof DimmerItem)) {
            throw new BindingConfigParseException("Item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only Switch-, Number-, Rollershutter-, Contact-, Dimmer- and StringItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {

        String pattern = null;
        boolean base64 = false;
        String onCommand = null;
        String offCommand = null;
        String upCommand = null;
        String downCommand = null;
        String stopCommand = null;
        String format = null;

        int parameterSplitterAt = bindingConfig.indexOf(",");

        if (parameterSplitterAt > 0) {
            String[] split = bindingConfig.substring(parameterSplitterAt + 1, bindingConfig.length() - 1).split("\\),");
            for (int i = 0; i < split.length; i++) {
                String substring = split[i];

                if (substring.startsWith("REGEX(")) {
                    pattern = substring.substring(6, substring.length());
                } else if (substring.startsWith("FORMAT(")) {
                    format = substring.substring(7, substring.length());
                } else if (substring.equals("BASE64")) {
                    base64 = true;
                } else if (substring.startsWith("ON(")) {
                    onCommand = substring.substring(3, substring.length());
                } else if (substring.startsWith("OFF(")) {
                    offCommand = substring.substring(4, substring.length());
                } else if (substring.startsWith("UP(")) {
                    upCommand = substring.substring(3, substring.length());
                } else if (substring.startsWith("DOWN(")) {
                    downCommand = substring.substring(5, substring.length());
                } else if (substring.startsWith("STOP(")) {
                    stopCommand = substring.substring(5, substring.length());
                }
            }
        }

        String portConfig[];
        if (parameterSplitterAt > 0) {
            portConfig = bindingConfig.substring(0, parameterSplitterAt).split("@");
        } else {
            portConfig = bindingConfig.split("@");
        }

        String port = portConfig[0];
        int baudRate = 0;

        if (portConfig.length > 1) {
            baudRate = Integer.parseInt(portConfig[1]);
        }

        SerialDevice serialDevice = serialDevices.get(port);
        if (serialDevice == null) {
            if (baudRate > 0) {
                serialDevice = new SerialDevice(port, baudRate);
            } else {
                serialDevice = new SerialDevice(port);
            }

            serialDevice.setEventPublisher(eventPublisher);
            try {
                serialDevice.initialize();
            } catch (InitializationException e) {
                throw new BindingConfigParseException("Could not open serial port " + port + ": " + e.getMessage());
            } catch (Throwable e) {
                throw new BindingConfigParseException("Could not open serial port " + port + ": " + e.getMessage());
            }

            serialDevices.put(port, serialDevice);
        }

        itemMap.put(item.getName(), port);

        serialDevice.addConfig(item.getName(), item.getClass(), pattern, base64, onCommand, offCommand, upCommand,
                downCommand, stopCommand, format);

        Set<String> itemNames = contextMap.get(context);
        if (itemNames == null) {
            itemNames = new HashSet<String>();
            contextMap.put(context, itemNames);
        }
        itemNames.add(item.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConfigurations(String context) {
        Set<String> itemNames = contextMap.get(context);
        if (itemNames != null) {
            for (String itemName : itemNames) {
                // we remove all information in the serial devices
                SerialDevice serialDevice = serialDevices.get(itemMap.get(itemName));
                itemMap.remove(itemName);
                if (serialDevice == null) {
                    continue;
                }

                serialDevice.removeConfig(itemName);

                // if there is no binding left, dispose this device
                if (serialDevice.isEmpty()) {
                    serialDevice.close();
                    serialDevices.remove(serialDevice.getPort());
                }
            }
            contextMap.remove(context);
        }
    }

}
