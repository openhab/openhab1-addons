/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.bus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.IllegalClassException;
import org.openhab.binding.knx.config.KNXBindingProvider;
import org.openhab.binding.knx.config.KNXTypeMapper;
import org.openhab.binding.knx.internal.connection.KNXConnection;
import org.openhab.binding.knx.internal.connection.KNXConnectionListener;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.Settings;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXTimeoutException;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListenerEx;

/**
 * This is the central class that takes care of the event exchange between openHAB and KNX.
 * It is fully connected (read and write) to the openHAB event bus and also has write access
 * to KNX while as well listening for incoming KNX messages.
 *
 * The received messages are converted into the right format for the other bus and published
 * to it.
 *
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class KNXBinding extends AbstractBinding<KNXBindingProvider>implements KNXConnectionListener {

    private static final Logger logger = LoggerFactory.getLogger(KNXBinding.class);

    /** to keep track of all KNX type mappers */
    protected Collection<KNXTypeMapper> typeMappers = new HashSet<KNXTypeMapper>();

    /**
     * used to store events that we have sent ourselves; we need to remember them for not reacting to them
     */
    private List<String> ignoreEventList = Collections.synchronizedList(new ArrayList<String>());

    private KNXBusReaderScheduler mKNXBusReaderScheduler = new KNXBusReaderScheduler();

    private boolean mKNXConnectionEstablished;

    private ItemRegistry itemRegistry;

    public void activate(ComponentContext componentContext) {
        logger.debug("KNXBinding: activating, calimero library version {}", Settings.getLibraryVersion());
        KNXConnection.addConnectionListener(this);
        KNXConnection.addProcessListenerEx(getProcessListenerEx());
        mKNXBusReaderScheduler.start();
        KNXConnection.bundleActivated = true;
    }

    public void deactivate(ComponentContext componentContext) {
        logger.debug("KNXBinding: deactivating");
        KNXConnection.removeProcessListenerEx();
        for (KNXBindingProvider provider : providers) {
            provider.removeBindingChangeListener(this);
        }
        providers.clear();
        mKNXBusReaderScheduler.stop();
        KNXConnection.disconnect();
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        logger.debug("KNX:: setItemRegistry");
        this.itemRegistry = itemRegistry;
    }

    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        logger.debug("KNX:: unsetItemRegistry");
        this.itemRegistry = null;
    }

    public void addKNXTypeMapper(KNXTypeMapper typeMapper) {
        this.typeMappers.add(typeMapper);
    }

    public void removeKNXTypeMapper(KNXTypeMapper typeMapper) {
        this.typeMappers.remove(typeMapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.core.binding.AbstractBinding#internalReceiveCommand(java.lang.String,
     * org.openhab.core.types.Command)
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.trace("Received command (item='{}', command='{}')", itemName, command.toString());
        if (!isEcho(itemName, command)) {
            writeToKNX(itemName, command);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.core.binding.AbstractBinding#internalReceiveUpdate(java.lang.String,
     * org.openhab.core.types.State)
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        logger.debug("Received update (item='{}', state='{}')", itemName, newState.toString());
        if (!isEcho(itemName, newState)) {
            writeToKNX(itemName, newState);
        }
    }

    private boolean isEcho(String itemName, Type type) {
        String ignoreEventListKey = itemName + type.toString();
        if (ignoreEventList.remove(ignoreEventListKey)) {
            logger.trace(
                    "We received this event (item='{}', state='{}') from KNX, so we don't send it back again -> ignore!",
                    itemName, type.toString());
            return true;
        } else {
            return false;
        }
    }

    private void writeToKNX(String itemName, Type value) {
        Iterable<Datapoint> datapoints = getDatapoints(itemName, value.getClass());
        if (datapoints != null) {
            ProcessCommunicator pc = KNXConnection.getCommunicator();
            if (pc != null) {
                for (Datapoint datapoint : datapoints) {
                    try {
                        pc.write(datapoint, toDPTValue(value, datapoint.getDPT()));
                        logger.debug("Wrote value '{}' to datapoint '{}'", value, datapoint);
                    } catch (KNXTimeoutException e) {
                        // Missing confirmation reply from TUNNELING connections are ignored here.
                        // ROUTING connections are by default unconfirmed.
                        logger.warn("KNXTimeoutException - set 'busaddr' in config to '0.0.0' to solve! Message: ",
                                e.getMessage());
                    } catch (KNXException e) {
                        logger.warn(
                                "Value '{}' could not be sent to the KNX bus using datapoint '{}' - retrying one time: {}",
                                new Object[] { value, datapoint, e.getMessage() });
                        try {
                            // do a second try, maybe the reconnection was successful
                            pc = KNXConnection.getCommunicator();
                            pc.write(datapoint, toDPTValue(value, datapoint.getDPT()));
                            logger.debug("Wrote value '{}' to datapoint '{}' on second try", value, datapoint);
                        } catch (KNXTimeoutException e1) {
                            // Missing confirmation reply from TUNNELING connections are ignored here.
                            // ROUTING connections are by default unconfirmed.
                            logger.warn("KNXTimeoutException - set 'busaddr' in config to '0.0.0' to solve! Message: ",
                                    e1.getMessage());
                        } catch (KNXException e1) {
                            logger.error(
                                    "Value '{}' could not be sent to the KNX bus using datapoint '{}' - giving up after second try: {}",
                                    new Object[] { value, datapoint, e1.getMessage() });
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see tuwien.auto.calimero.process.ProcessListener#groupWrite(tuwien.auto.calimero.process.ProcessEvent)
     */
    /**
     * If <code>knx:ignorelocalevents=true</code> is set in configuration, it prevents internal events
     * coming from 'openHAB event bus' a second time to be sent back to the 'openHAB event bus'.
     *
     * @param e the {@link ProcessEvent} to handle.
     */
    private void onGroupEvent(ProcessEvent e) {
        logger.debug("onGroupEvent: Event ASDU:'{}' Destination:'{}' SourceAddr:'{}' ", e.getASDU(),
                e.getDestination(), e.getSourceAddr());
        if (!(KNXConnection.getIgnoreLocalSourceEvents()
                && e.getSourceAddr().toString().equalsIgnoreCase(KNXConnection.getLocalSourceAddr()))) {
            readFromKNX(e);
        } else {
            logger.warn("Ignoring local Event, received from my local Source address {} for Group address {}.",
                    e.getSourceAddr().toString(), e.getDestination().toString());
        }
    }

    /**
     * Handles the given {@link ProcessEvent}. After finding the corresponding
     * Item (by iterating through all known group addresses) this Item is updated.
     * Each item is added to a special list to identify and avoid echo's in
     * the <code>receiveUpdate</code> and <code>receiveCommand</code> methods.
     *
     * @param e the {@link ProcessEvent} to handle.
     */
    private void readFromKNX(ProcessEvent e) {
        try {
            GroupAddress destination = e.getDestination();
            byte[] asdu = e.getASDU();
            if (asdu.length == 0) {
                return;
            }
            String[] itemList = getItemNames(destination);
            if (itemList.length == 0) {
                logger.debug("Received telegram for unknown group address {}", destination.toString());
            }
            for (String itemName : itemList) {
                Iterable<Datapoint> datapoints = getDatapoints(itemName, destination);
                if (datapoints != null) {
                    for (Datapoint datapoint : datapoints) {
                        Type type = getType(datapoint, asdu);
                        if (type != null) {
                            // we need to make sure that we won't send out this event to
                            // the knx bus again, when receiving it on the openHAB bus
                            ignoreEventList.add(itemName + type.toString());
                            logger.trace("Added event (item='{}', type='{}') to the ignore event list", itemName,
                                    type.toString());

                            if (type instanceof Command && isCommandGA(destination)) {
                                eventPublisher.postCommand(itemName, (Command) type);
                            } else if (type instanceof State) {
                                eventPublisher.postUpdate(itemName, (State) type);
                            } else {
                                throw new IllegalClassException("Cannot process datapoint of type " + type.toString());
                            }

                            logger.trace("Processed event (item='{}', type='{}', destination='{}')", itemName,
                                    type.toString(), destination.toString());
                        } else {
                            final char[] hexCode = "0123456789ABCDEF".toCharArray();
                            StringBuilder sb = new StringBuilder(2 + asdu.length * 2);
                            sb.append("0x");
                            for (byte b : asdu) {
                                sb.append(hexCode[(b >> 4) & 0xF]);
                                sb.append(hexCode[(b & 0xF)]);
                            }

                            logger.debug(
                                    "Ignoring KNX bus data: couldn't transform to an openHAB type (not supported). Destination='{}', datapoint='{}', data='{}'",
                                    new Object[] { destination.toString(), datapoint.toString(), sb.toString() });
                        }
                    }
                }
            }
        } catch (RuntimeException re) {
            logger.error("Error while receiving event from KNX bus: " + re.toString());
        }
    }

    public void addBindingProvider(KNXBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    public void removeBindingProvider(KNXBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.core.binding.AbstractBinding#bindingChanged(org.openhab.core.binding.BindingProvider,
     * java.lang.String)
     */
    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        logger.trace("bindingChanged() for item {} msg received.", itemName);
        if (mKNXConnectionEstablished) {
            if (provider instanceof KNXBindingProvider) {
                KNXBindingProvider knxProvider = (KNXBindingProvider) provider;
                for (Datapoint datapoint : knxProvider.getReadableDatapoints()) {
                    if (datapoint.getName().equals(itemName)) {
                        logger.debug("Initializing read of item {}.", itemName);
                        if (!mKNXBusReaderScheduler.scheduleRead(datapoint,
                                knxProvider.getAutoRefreshTime(datapoint))) {
                            logger.warn("Couldn't add to KNX bus reader scheduler (bindingChanged, datapoint='{}')",
                                    datapoint);
                        }
                        break;
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.core.binding.AbstractBinding#allBindingsChanged(org.openhab.core.binding.BindingProvider)
     */
    @Override
    public void allBindingsChanged(BindingProvider provider) {
        logger.trace("allBindingsChanged() msg received.");
        if (mKNXConnectionEstablished) {
            logger.debug("Initializing readable DPs.");
            if (provider instanceof KNXBindingProvider) {
                KNXBindingProvider knxProvider = (KNXBindingProvider) provider;
                mKNXBusReaderScheduler.clear();
                for (Datapoint datapoint : knxProvider.getReadableDatapoints()) {
                    mKNXBusReaderScheduler.readOnce(datapoint);

                    int autoRefreshTimeInSecs = knxProvider.getAutoRefreshTime(datapoint);
                    if (autoRefreshTimeInSecs > 0) {
                        if (!mKNXBusReaderScheduler.scheduleRead(datapoint,
                                knxProvider.getAutoRefreshTime(datapoint))) {
                            logger.warn("Couldn't add to KNX bus reader scheduler (allBindingsChanged, datapoint='{}')",
                                    datapoint);
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.knx.internal.connection.KNXConnectionListener#connectionEstablished()
     */
    @Override
    public void connectionEstablished() {
        logger.trace("connectionEstablished() msg received. Initializing readable DPs.");
        mKNXConnectionEstablished = true;
        for (KNXBindingProvider knxProvider : providers) {
            for (Datapoint datapoint : knxProvider.getReadableDatapoints()) {
                mKNXBusReaderScheduler.readOnce(datapoint);

                int autoRefreshTimeInSecs = knxProvider.getAutoRefreshTime(datapoint);
                if (autoRefreshTimeInSecs > 0) {
                    if (!mKNXBusReaderScheduler.scheduleRead(datapoint, autoRefreshTimeInSecs)) {
                        logger.warn("Couldn't add to KNX bus reader scheduler (connectionEstablished, datapoint='{}')",
                                datapoint);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.knx.internal.connection.KNXConnectionListener#connectionLost()
     */
    @Override
    public void connectionLost() {
        logger.trace("connectionLost() msg received.");
        mKNXConnectionEstablished = false;
        mKNXBusReaderScheduler.clear();
    }

    private void writeKNXReadResponse(ProcessEvent e) {
        logger.debug("writeKNXReadRequest ");
        if (itemRegistry != null) {
            for (KNXBindingProvider provider : providers) {
                for (String itemName : provider.getListeningItemNames(e.getDestination())) {
                    logger.debug("Found ReadCommand for Item '{}' Items.size '{}'", itemName,
                            itemRegistry.getItems().size());
                    Item item = null;
                    try {
                        item = itemRegistry.getItem(itemName);
                    } catch (ItemNotFoundException e2) {
                        logger.debug("Item not found for '{}'", itemName);
                    }
                    if (item != null && item.getState().toString() != "Uninitialized") {
                        Iterable<Datapoint> datapoints = getDatapoints(itemName, e.getDestination());
                        if (datapoints != null) {
                            for (Datapoint datapoint : datapoints) {
                                KNXConnection.writeReadResponse(datapoint, item.getState().toString());
                            }
                        }
                    }
                }
            }
        }

    }

    private ProcessListenerEx getProcessListenerEx() {
        ProcessListenerEx processListenerEx = new ProcessListenerEx() {

            @Override
            public void groupWrite(final ProcessEvent e) {
                logger.debug("KNX:: groupWrite '{}'", e.getDestination());
                onGroupEvent(e);
            }

            @Override
            public void groupReadResponse(final ProcessEvent e) {
                logger.debug("KNX:: groupReadResponse Destination '{}' Source '{}' localSource '{}'",
                        e.getDestination(), e.getSourceAddr(), KNXConnection.getLocalSourceAddr());
                onGroupEvent(e);
            }

            @Override
            public void groupReadRequest(final ProcessEvent e) {
                logger.debug("KNX:: groupReadRequest Destination '{}' Source '{}' localSource '{}'", e.getDestination(),
                        e.getSourceAddr(), KNXConnection.getLocalSourceAddr());
                // only known as responding addresses get an answer
                for (KNXBindingProvider knxProvider : providers) {
                    for (Datapoint datapoint : knxProvider.getRespondingDatapoints()) {
                        logger.debug("KNX:: groupReadRequest is responding '{}' = '{}'", datapoint.getMainAddress(),
                                e.getDestination());
                        if (datapoint.getMainAddress().getRawAddress() == e.getDestination().getRawAddress()) {
                            logger.debug("KNX:: groupReadRequest DO");
                            writeKNXReadResponse(e);
                        }
                    }
                }
            }

            @Override
            public void detached(final DetachEvent e) {
                logger.debug("KNX:: detached");
            }
        };
        return processListenerEx;
    }

    /**
     * Determines whether the given <code>groupAddress</code> is the address which
     * will be interpreted as the command type. This method iterates over all
     * registered KNX binding providers to find the result.
     *
     * @param groupAddress the group address to check
     * @return true, if it is a command GA
     */
    private boolean isCommandGA(GroupAddress groupAddress) {
        for (KNXBindingProvider provider : providers) {
            if (!provider.isCommandGA(groupAddress)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns all listening item names. This method iterates over all registered KNX binding providers and aggregates
     * the result.
     *
     * @param groupAddress
     *            the group address that the items are listening to
     * @return an array of all listening items
     */
    private String[] getItemNames(GroupAddress groupAddress) {
        List<String> itemNames = new ArrayList<String>();
        for (KNXBindingProvider provider : providers) {
            for (String itemName : provider.getListeningItemNames(groupAddress)) {
                itemNames.add(itemName);
            }
        }
        return itemNames.toArray(new String[itemNames.size()]);
    }

    /**
     * Returns the datapoints for a given item and group address. This method iterates over all registered KNX binding
     * providers to find the result.
     *
     * @param itemName
     *            the item name for the datapoint
     * @param groupAddress
     *            the group address associated to the datapoint
     * @return the datapoints which corresponds to the given item and group address
     */
    private Iterable<Datapoint> getDatapoints(String itemName, GroupAddress groupAddress) {
        for (KNXBindingProvider provider : providers) {
            Iterable<Datapoint> datapoints = provider.getDatapoints(itemName, groupAddress);
            if (datapoints != null) {
                return datapoints;
            }
        }
        return null;
    }

    /**
     * Transforms the raw KNX bus data of a given datapoint into an openHAB type (command or state)
     *
     * @param datapoint
     *            the datapoint to which the data belongs
     * @param asdu
     *            the byte array of the raw data from the KNX bus
     * @return the openHAB command or state that corresponds to the data
     */
    private Type getType(Datapoint datapoint, byte[] asdu) {
        for (KNXTypeMapper typeMapper : typeMappers) {
            Type type = typeMapper.toType(datapoint, asdu);
            if (type != null) {
                return type;
            }
        }
        return null;
    }

    /**
     * Returns the datapoints for a given item and type class. This method iterates over all registered KNX binding
     * providers to find the result.
     *
     * @param itemName
     *            the item name for the datapoints
     * @param typeClass
     *            the type class associated to the datapoints
     * @return the datapoints which corresponds to the given item and type class
     */
    private Iterable<Datapoint> getDatapoints(final String itemName, final Class<? extends Type> typeClass) {
        Set<Datapoint> datapoints = new HashSet<Datapoint>();
        for (KNXBindingProvider provider : providers) {
            for (Datapoint datapoint : provider.getDatapoints(itemName, typeClass)) {
                datapoints.add(datapoint);
            }
        }
        return datapoints;
    }

    /**
     * Transforms an openHAB type (command or state) into a datapoint type value for the KNX bus.
     *
     * @param type
     *            the openHAB command or state to transform
     * @param dpt
     *            the datapoint type to which should be converted
     *
     * @return the corresponding KNX datapoint type value as a string
     */
    private String toDPTValue(Type type, String dpt) {
        for (KNXTypeMapper typeMapper : typeMappers) {
            String value = typeMapper.toDPTValue(type, dpt);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
