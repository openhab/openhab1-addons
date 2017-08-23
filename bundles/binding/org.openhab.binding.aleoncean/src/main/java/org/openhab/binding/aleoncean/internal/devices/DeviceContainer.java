/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package org.openhab.binding.aleoncean.internal.devices;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.openhab.binding.aleoncean.internal.AleonceanBindingConfig;
import org.openhab.binding.aleoncean.internal.converter.ConverterFactory;
import org.openhab.binding.aleoncean.internal.converter.StandardConverter;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.device.Device;
import eu.aleon.aleoncean.device.DeviceFactory;
import eu.aleon.aleoncean.device.DeviceParameterUpdatedEvent;
import eu.aleon.aleoncean.device.DeviceParameterUpdatedListener;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;
import eu.aleon.aleoncean.device.StandardDevice;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class DeviceContainer implements DeviceParameterUpdatedListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceContainer.class);

    private final SortedMap<Integer, Device> devices = new TreeMap<>();
    private final SortedMap<String, ItemInfo> itemNameInfos = new TreeMap<>();
    private final SortedMap<Device, List<String>> deviceToItemNames = new TreeMap<>();
    private final SortedMap<EnOceanId, List<Device>> remoteAddressToDevice = new TreeMap<>();

    private final ESP3Connector connector;

    private EventPublisher eventPublisher = null;

    /**
     * Create a new device container instance.
     *
     * @param connector The ESP3 connector that will be used to write to.
     */
    public DeviceContainer(final ESP3Connector connector) {
        this.connector = connector;
    }

    /**
     * Start usage of the device container.
     *
     * This function must be called, before any handling is done.
     */
    public void start() {
        // ATM there is nothing to do on start.
        // Cleanup is done in the stop function.
        // The caller have to set the event publisher and add items.
    }

    /**
     * Stop usage of the device container.
     *
     * This function must be called, before the object is lost (to prevent memory leaks).
     */
    public void stop() {
        setEventPublisher(null);
        while (!itemNameInfos.isEmpty()) {
            final String itemName = itemNameInfos.firstKey();
            itemRemove(itemName);
        }
    }

    /**
     * Set the event publisher that should be used.
     *
     * The event publisher will be used to post updates (states, commands).
     *
     * @param eventPublisher The event publisher that should be used to post updates.
     */
    public void setEventPublisher(final EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    private int getDeviceId(final Class<? extends Device> type, final EnOceanId remoteId, final EnOceanId localId) {
        final String id = String.format("#%s#%s#%s#", type.toString(), remoteId.toString(), localId.toString());
        return id.hashCode();
    }

    ItemInfo getItemInfoForItemName(final String itemName) {
        return itemNameInfos.get(itemName);
    }

    List<String> getItemNamesForDevice(final Device device) {
        return deviceToItemNames.get(device);
    }

    private void addRemoteAddressToDevice(final Device device) {
        List<Device> list = remoteAddressToDevice.get(device.getAddressRemote());
        if (list == null) {
            list = new LinkedList<>();
            list.add(device);
            remoteAddressToDevice.put(device.getAddressRemote(), list);
        } else {
            if (!list.contains(device)) {
                list.add(device);
            }
        }
    }

    private void delRemoteAddressToDevice(final Device device) {
        final List<Device> list = remoteAddressToDevice.get(device.getAddressRemote());
        if (list == null) {
        } else {
            list.remove(device);
            if (list.isEmpty()) {
                remoteAddressToDevice.remove(device.getAddressRemote());
            }
        }
    }

    private boolean itemAdd(final String itemName, final AleonceanBindingConfig config) {

        /*
         * Generate a device ID by special parameters, so we could find an existing device object.
         */
        final int deviceId = getDeviceId(config.getType(), config.getRemoteId(), config.getLocalId());

        // Is the device already known or have we to create a new one.
        final boolean alreadyExists = devices.containsKey(deviceId);

        // Declare an object for the device and an object that holds a list with items handled by that device.
        final Device device;
        final List<String> list;

        if (alreadyExists) {
            // Use existing device and existing list.
            device = devices.get(deviceId);
            list = getItemNamesForDevice(device);
        } else {
            // Create a new device.
            device = DeviceFactory.createFromClass(config.getType(), connector, config.getRemoteId(), config.getLocalId());
            if (device == null) {
                LOGGER.warn("Device creation failed");
                return false;
            }

            // Register listener
            device.addParameterUpdatedListener(this);

            // Create a new List.
            list = new LinkedList<>();
        }

        /*
         * The last step is to setup a converter.
         */
        StandardConverter converter;

        try {
            // Find a converter class for the item.
            final Class<? extends StandardConverter> converterClass;
            converterClass = ConverterFactory.getConverterClass(config.getParameter(), config.getItemType(), config.getAcceptedDataTypes(),
                                                                config.getAcceptedCommandTypes(), config.getConvParam());

            // Proceed only is a converter class is found.
            if (converterClass == null) {
                LOGGER.warn("No converter class found! item type: {}, parameter: {}, device class: {}", config.getItemType(), config.getParameter(),
                            device.getClass());
                return false;
            }

            // Create a converter instance of the converter class.
            converter = ConverterFactory.createFromClass(converterClass, config.getActionIn());

            // Proceed only if a converter is available.
            if (converter == null) {
                LOGGER.warn("Cannot create converter from class: {}", converterClass);
                return false;
            }
        } catch (final IllegalArgumentException ex) {
            LOGGER.warn("Parameter not handled by device.\n{}", ex);
            return false;
        }

        // If the device (and the list) has (have) been created, save it (them).
        if (!alreadyExists) {
            devices.put(deviceId, device);
            deviceToItemNames.put(device, list);
            addRemoteAddressToDevice(device);
        }

        // Save item informations and add the item to the list of items handled by that device.
        final ItemInfo itemInfo = new ItemInfo(device, converter, config);
        itemNameInfos.put(itemName, itemInfo);
        list.add(itemName);

        // Perhaps there are already some information about that parameter
        // (device already exists, well known parameters, ...).
        try {
            final Object parameterValue = device.getByParameter(config.getParameter());
            if (parameterValue != null) {
                publishParameter(itemName, itemInfo, parameterValue);
            }
        } catch (final IllegalDeviceParameterException ex) {
            LOGGER.warn("Get value for item by parameter failed; name: {}, info: {}, parameter: {}\n{}", itemName, itemInfo, config.getParameter(), ex);
        }

        // Return with a success indication.
        return true;
    }

    public void itemChanged(final String itemName, final AleonceanBindingConfig config) {
        if (itemNameInfos.containsKey(itemName)) {
            itemRemove(itemName);
        }

        if (config != null) {
            itemAdd(itemName, config);
        }
    }

    public void itemRemove(final String itemName) {
        // Save item info and remove reference by item name.
        final ItemInfo itemInfo = getItemInfoForItemName(itemName);
        itemNameInfos.remove(itemName);

        // We have to remove the item name from the list of items, that are suffered by a device.
        final Device device = itemInfo.getDevice();
        final List<String> list = getItemNamesForDevice(device);
        list.remove(itemName);

        // If a device does not suffer any items, we could remove all references to that device.
        if (list.isEmpty()) {
            deviceToItemNames.remove(device);

            delRemoteAddressToDevice(device);

            final int deviceId = getDeviceId(device.getClass(), device.getAddressRemote(), device.getAddressLocal());
            devices.remove(deviceId);

            // At least release the listener.
            device.removeParameterUpdatedListener(this);
        }
    }

    public void handleIncomingRadioPacket(final RadioPacket packet) {
        final List<Device> deviceList = remoteAddressToDevice.get(packet.getSenderId());
        if (deviceList == null) {
            return;
        }

        for (final Device device : deviceList) {
            device.parseRadioPacket(packet);
        }
    }

    public void handleReceivedCommand(final String itemName, final Command command) {
        final ItemInfo itemInfo = getItemInfoForItemName(itemName);
        if (itemInfo != null) {
            final StandardConverter converter = itemInfo.getConverter();
            converter.commandFromOpenHAB(eventPublisher, itemName, itemInfo, command);
        }
    }

    public void handleReceivedState(final String itemName, final State state) {
        final ItemInfo itemInfo = getItemInfoForItemName(itemName);
        if (itemInfo != null) {
            final StandardConverter converter = itemInfo.getConverter();
            converter.stateFromOpenHAB(eventPublisher, itemName, itemInfo, state);
        }
    }

    private void publishParameter(final String itemName, final ItemInfo itemInfo, final Object value) {
        final StandardConverter converter = itemInfo.getConverter();
        converter.parameterFromDevice(eventPublisher, itemName, itemInfo, value);
    }

    @Override
    public void parameterUpdated(final DeviceParameterUpdatedEvent event) {
        final StandardDevice device = (StandardDevice) event.getSource();

        LOGGER.debug("Parameter changed: remoteId={}, parameter={}, value: {} => {}, initiation: {}",
                     device.getAddressRemote(),
                     event.getParameter(),
                     event.getOldValue(),
                     event.getNewValue(),
                     event.getInitiation());

        final List<String> itemNames = getItemNamesForDevice(device);
        for (final String itemName : itemNames) {
            final ItemInfo itemInfo = getItemInfoForItemName(itemName);
            if (itemInfo.getParameter().equals(event.getParameter())) {
                switch (event.getInitiation()) {
                    case RADIO_PACKET:
                    case INTERNAL_LOGIC:
                        final Object parameterValue = event.getNewValue();
                        if (parameterValue != null) {
                            publishParameter(itemName, itemInfo, parameterValue);
                        }
                        break;

                    case SET_PARAMETER:
                        LOGGER.debug("Parameter update notifications caused by 'set parameter' will be ignored (ATM).");
                        break;

                    default:
                        LOGGER.warn("Unhandled case: {}", event.getInitiation());
                        break;
                }

            }
        }
    }
}
