/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.SwallowedExceptionListener;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.pooling.EndpointPoolConfiguration;
import org.openhab.binding.modbus.internal.pooling.ModbusSlaveConnectionFactoryImpl;
import org.openhab.binding.modbus.internal.pooling.ModbusSlaveEndpoint;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.ModbusSlaveConnection;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.util.BitVector;
import net.wimpi.modbus.util.SerialParameters;

/**
 * Modbus binding allows to connect to multiple Modbus slaves as TCP master.
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusBinding extends AbstractActiveBinding<ModbusBindingProvider> implements ManagedService {

    private static final long DEFAULT_POLL_INTERVAL = 200;

    /**
     * Time to wait between connection passive+borrow, i.e. time to wait between
     * transactions
     * Default 60ms for TCP slaves, Siemens S7 1212 PLC couldn't handle faster
     * requests with default settings.
     */
    private static final long DEFAULT_TCP_INTER_TRANSACTION_DELAY_MILLIS = 60;

    /**
     * Time to wait between connection passive+borrow, i.e. time to wait between
     * transactions
     * Default 35ms for Serial slaves, motivation discussed
     * here https://community.openhab.org/t/connection-pooling-in-modbus-binding/5246/111?u=ssalonen
     */
    private static final long DEFAULT_SERIAL_INTER_TRANSACTION_DELAY_MILLIS = 35;

    private static final Logger logger = LoggerFactory.getLogger(ModbusBinding.class);

    private static final String UDP_PREFIX = "udp";
    private static final String TCP_PREFIX = "tcp";
    private static final String SERIAL_PREFIX = "serial";

    private static final String VALID_CONFIG_KEYS = "connection|id|start|length|type|valuetype|rawdatamultiplier|writemultipleregisters|updateunchangeditems|postundefinedonreaderror";
    private static final Pattern EXTRACT_MODBUS_CONFIG_PATTERN = Pattern.compile(
            "^(" + TCP_PREFIX + "|" + UDP_PREFIX + "|" + SERIAL_PREFIX + "|)\\.(.*?)\\.(" + VALID_CONFIG_KEYS + ")$");

    /** Stores instances of all the slaves defined in cfg file */
    private static Map<String, ModbusSlave> modbusSlaves = new ConcurrentHashMap<>();

    private static GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();

    static {
        // When the pool is exhausted, multiple calling threads may be simultaneously blocked waiting for instances to
        // become available. As of pool 1.5, a "fairness" algorithm has been implemented to ensure that threads receive
        // available instances in request arrival order.
        poolConfig.setFairness(true);
        // Limit one connection per endpoint (i.e. same ip:port pair or same serial device).
        // If there are multiple read/write requests to process at the same time, block until previous one finishes
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxTotalPerKey(1);

        // block infinitely when exhausted
        poolConfig.setMaxWaitMillis(-1);

        // make sure we return connected connections from/to connection pool
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);

        // disable JMX
        poolConfig.setJmxEnabled(false);
    }

    /**
     * We use connection pool to ensure that only single transaction is ongoing per each endpoint. This is especially
     * important with serial slaves but practice has shown that even many tcp slaves have limited
     * capability to handle many connections at the same time
     *
     * Relevant discussion at the time of implementation:
     * - https://community.openhab.org/t/modbus-connection-problem/6108/
     * - https://community.openhab.org/t/connection-pooling-in-modbus-binding/5246/
     */
    private static KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> connectionPool;
    private static ModbusSlaveConnectionFactoryImpl connectionFactory;

    private static void reconstructConnectionPool() {
        connectionFactory = new ModbusSlaveConnectionFactoryImpl();
        GenericKeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> genericKeyedObjectPool = new GenericKeyedObjectPool<>(
                connectionFactory, poolConfig);
        genericKeyedObjectPool.setSwallowedExceptionListener(new SwallowedExceptionListener() {

            @Override
            public void onSwallowException(Exception e) {
                logger.error("Connection pool swallowed unexpected exception: {}", e.getMessage());

            }
        });
        connectionPool = genericKeyedObjectPool;
    }

    /**
     * For testing
     */
    static KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> getReconstructedConnectionPoolForTesting() {
        reconstructConnectionPool();
        return connectionPool;
    }

    /** slaves update interval in milliseconds */
    public static long pollInterval = DEFAULT_POLL_INTERVAL;

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
        clearAndClose();
    }

    @Override
    protected long getRefreshInterval() {
        return pollInterval;
    }

    @Override
    protected String getName() {
        return "Modbus Polling Service";
    }

    /**
     * Parses configuration creating Modbus slave instances defined in cfg file
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        for (ModbusBindingProvider provider : providers) {
            if (!provider.providesBindingFor(itemName)) {
                continue;
            }
            logger.trace("Received command '{}' for item '{}'", command, itemName);
            ModbusBindingConfig config = provider.getConfig(itemName);
            List<ItemIOConnection> writeConnections = config.getWriteConnectionsByCommand(command);
            Comparator<ItemIOConnection> byLastPolledTime = (ItemIOConnection a, ItemIOConnection b) -> Long
                    .compareUnsigned(a.getPollNumber(), b.getPollNumber());
            // find out the most recently polled state (from any slave as long as it is bound to this item!) for this
            // item (if available)
            Optional<State> previouslyPolledState = config.getReadConnections().stream().max(byLastPolledTime)
                    .map(connection -> connection.getPreviouslyPolledState());

            for (ItemIOConnection writeConnection : writeConnections) {
                ModbusSlave slave = modbusSlaves.get(writeConnection.getSlaveName());
                if (writeConnection.supportsCommand(command)) {
                    Transformation transformation = writeConnection.getTransformation();
                    Command transformedCommand = transformation == null ? command
                            : transformation.transformCommand(config.getItemAcceptedCommandTypes(), command);
                    logger.trace(
                            "Executing command '{}' (transformed from '{}' using transformation {}) using item '{}' IO connection {} (writeIndex={}, previouslyPolledState={})",
                            transformedCommand, command, transformation, itemName, writeConnection,
                            previouslyPolledState);
                    slave.executeCommand(itemName, transformedCommand, writeConnection.getIndex(),
                            previouslyPolledState);
                } else {
                    logger.trace(
                            "Command '{}' using item '{}' IO connection {} not triggered/supported by the IO connection",
                            command, itemName, writeConnection);
                }

            }
        }
    }

    /**
     * Posts update event to OpenHAB bus for "holding" and "input register" type slaves
     *
     * @param binding ModbusBinding to get item configuration from BindingProviding
     * @param registers data received from slave device in the last pollInterval
     * @param itemName item to update
     */
    protected void internalUpdateItem(String slaveName, InputRegister[] registers, String itemName) {
        for (ModbusBindingProvider provider : providers) {
            if (!provider.providesBindingFor(itemName)) {
                continue;
            }
            ModbusBindingConfig config = provider.getConfig(itemName);
            List<ItemIOConnection> connections = config.getReadConnectionsBySlaveName(slaveName);
            for (ItemIOConnection connection : connections) {
                ModbusSlave slave = modbusSlaves.get(slaveName);
                String slaveValueType = slave.getValueType();
                double rawDataMultiplier = slave.getRawDataMultiplier();

                String valueType = connection.getEffectiveValueType(slaveValueType);

                /* receive data manipulation */
                State newState = extractStateFromRegisters(registers, connection.getIndex(), valueType);
                // Convert newState (DecimalType) to on/off kind of state if we have "boolean item" (Switch, Contact
                // etc). In other cases (such as Number items) newStateBoolean will be UNDEF
                State newStateBoolean = provider.getConfig(itemName).translateBoolean2State(
                        connection.getPreviouslyPolledState(), !newState.equals(DecimalType.ZERO));
                // If we have boolean item (newStateBoolean is not UNDEF)
                if (!UnDefType.UNDEF.equals(newStateBoolean)) {
                    newState = newStateBoolean;
                } else if ((rawDataMultiplier != 1) && (config.getItemClass().isAssignableFrom(NumberItem.class))) {
                    double tmpValue = ((DecimalType) newState).doubleValue() * rawDataMultiplier;
                    newState = new DecimalType(String.valueOf(tmpValue));
                }
                boolean stateChanged = !newState.equals(connection.getPreviouslyPolledState());
                if (connection.supportsState(newState, stateChanged, slave.isUpdateUnchangedItems())) {
                    logger.trace(
                            "internalUpdateItem(Register): Updating slave {} item {}, state {} (changed={}) matched ItemIOConnection {}.",
                            slaveName, itemName, newState, stateChanged, connection);
                    Transformation transformation = connection.getTransformation();
                    State transformedState = transformation == null ? newState
                            : transformation.transformState(config.getItemAcceptedDataTypes(), newState);
                    eventPublisher.postUpdate(itemName, transformedState);
                    connection.setPreviouslyPolledState(newState);
                } else {
                    logger.trace(
                            "internalUpdateItem(Register): Not updating slave {} item {} since state {} (changed={}) not supported by ItemIOConnection {}.",
                            slaveName, itemName, newState, stateChanged, connection);
                }
            }
        }
    }

    /**
     * Posts update event to OpenHAB bus for all types of slaves when there is a read error
     *
     * @param binding ModbusBinding to get item configuration from BindingProviding
     * @param error
     * @param itemName item to update
     */
    protected void internalUpdateReadErrorItem(String slaveName, Exception error, String itemName) {
        ModbusSlave slave = modbusSlaves.get(slaveName);
        if (!slave.isPostUndefinedOnReadError()) {
            return;
        }
        State newState = UnDefType.UNDEF;
        for (ModbusBindingProvider provider : providers) {
            if (!provider.providesBindingFor(itemName)) {
                continue;
            }

            ModbusBindingConfig config = provider.getConfig(itemName);
            List<ItemIOConnection> connections = config.getReadConnectionsBySlaveName(slaveName);
            for (ItemIOConnection connection : connections) {
                boolean stateChanged = !newState.equals(connection.getPreviouslyPolledState());
                if (connection.supportsState(newState, stateChanged, slave.isUpdateUnchangedItems())) {
                    logger.trace(
                            "internalUpdateReadErrorItem: Updating slave {} item {}, state {} (changed={}) matched ItemIOConnection {}.",
                            slaveName, itemName, newState, stateChanged, connection);
                    // Note: no transformation with errors, always emit the UNDEFINED
                    eventPublisher.postUpdate(itemName, newState);
                    connection.setPreviouslyPolledState(newState);
                } else {
                    logger.trace(
                            "internalUpdateReadErrorItem: Not updating slave {} item {} since state {} (changed={}) not supported by ItemIOConnection {}.",
                            slaveName, itemName, newState, stateChanged, connection);
                }
            }
        }
    }

    /**
     * Read data from registers and convert the result to DecimalType
     * Interpretation of <tt>index</tt> goes as follows depending on type
     *
     * BIT:
     * - a single bit is read from the registers
     * - indices between 0...15 (inclusive) represent bits of the first register
     * - indices between 16...31 (inclusive) represent bits of the second register, etc.
     * - index 0 refers to the least significant bit of the first register
     * - index 1 refers to the second least significant bit of the first register, etc.
     * INT8:
     * - a byte (8 bits) from the registers is interpreted as signed integer
     * - index 0 refers to low byte of the first register, 1 high byte of first register
     * - index 2 refers to low byte of the second register, 3 high byte of second register, etc.
     * - it is assumed that each high and low byte is encoded in most significant bit first order
     * UINT8:
     * - same as INT8 except values are interpreted as unsigned integers
     * INT16:
     * - register with index (counting from zero) is interpreted as 16 bit signed integer.
     * - it is assumed that each register is encoded in most significant bit first order
     * UINT16:
     * - same as INT16 except values are interpreted as unsigned integers
     * INT32:
     * - registers (2 * index) and ( 2 *index + 1) are interpreted as signed 32bit integer.
     * - it assumed that the first register contains the most significant 16 bits
     * - it is assumed that each register is encoded in most significant bit first order
     * UINT32:
     * - same as UINT32 except values are interpreted as unsigned integers
     * FLOAT32:
     * - registers (2 * index) and ( 2 *index + 1) are interpreted as signed 32bit floating point number.
     * - it assumed that the first register contains the most significant 16 bits
     * - it is assumed that each register is encoded in most significant bit first order
     *
     * @param registers
     *            list of registers, each register represent 16bit of data
     * @param index
     *            zero based item index. Interpretation of this depends on type
     * @param type
     *            item type, e.g. unsigned 16bit integer (<tt>ModbusBindingProvider.VALUE_TYPE_UINT16</tt>)
     * @return number representation queried value
     * @throws IllegalArgumentException when <tt>type</tt> does not match a known type
     * @throws IndexOutOfBoundsException when <tt>index</tt> is out of bounds of registers
     *
     */
    private DecimalType extractStateFromRegisters(InputRegister[] registers, int index, String type) {
        if (type.equals(ModbusBindingProvider.VALUE_TYPE_BIT)) {
            return new DecimalType((registers[index / 16].toUnsignedShort() >> (index % 16)) & 1);
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_INT8)) {
            return new DecimalType(registers[index / 2].toBytes()[1 - (index % 2)]);
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_UINT8)) {
            return new DecimalType((registers[index / 2].toUnsignedShort() >> (8 * (index % 2))) & 0xff);
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_INT16)) {
            ByteBuffer buff = ByteBuffer.allocate(2);
            buff.put(registers[index].toBytes());
            return new DecimalType(buff.order(ByteOrder.BIG_ENDIAN).getShort(0));
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_UINT16)) {
            return new DecimalType(registers[index].toUnsignedShort());
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_INT32)) {
            ByteBuffer buff = ByteBuffer.allocate(4);
            buff.put(registers[index * 2 + 0].toBytes());
            buff.put(registers[index * 2 + 1].toBytes());
            return new DecimalType(buff.order(ByteOrder.BIG_ENDIAN).getInt(0));
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_UINT32)) {
            ByteBuffer buff = ByteBuffer.allocate(8);
            buff.position(4);
            buff.put(registers[index * 2 + 0].toBytes());
            buff.put(registers[index * 2 + 1].toBytes());
            return new DecimalType(buff.order(ByteOrder.BIG_ENDIAN).getLong(0));
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_FLOAT32)) {
            ByteBuffer buff = ByteBuffer.allocate(4);
            buff.put(registers[index * 2 + 0].toBytes());
            buff.put(registers[index * 2 + 1].toBytes());
            return new DecimalType(buff.order(ByteOrder.BIG_ENDIAN).getFloat(0));
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_INT32_SWAP)) {
            ByteBuffer buff = ByteBuffer.allocate(4);
            buff.put(registers[index * 2 + 1].toBytes());
            buff.put(registers[index * 2 + 0].toBytes());
            return new DecimalType(buff.order(ByteOrder.BIG_ENDIAN).getInt(0));
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_UINT32_SWAP)) {
            ByteBuffer buff = ByteBuffer.allocate(8);
            buff.position(4);
            buff.put(registers[index * 2 + 1].toBytes());
            buff.put(registers[index * 2 + 0].toBytes());
            return new DecimalType(buff.order(ByteOrder.BIG_ENDIAN).getLong(0));
        } else if (type.equals(ModbusBindingProvider.VALUE_TYPE_FLOAT32_SWAP)) {
            ByteBuffer buff = ByteBuffer.allocate(4);
            buff.put(registers[index * 2 + 1].toBytes());
            buff.put(registers[index * 2 + 0].toBytes());
            return new DecimalType(buff.order(ByteOrder.BIG_ENDIAN).getFloat(0));
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Posts update event to OpenHAB bus for "coil" and "discrete input" type slaves
     *
     * @param binding ModbusBinding to get item configuration from BindingProviding
     * @param registers data received from slave device in the last pollInterval
     * @param item item to update
     */
    protected void internalUpdateItem(String slaveName, BitVector coils, String itemName) {
        for (ModbusBindingProvider provider : providers) {
            if (!provider.providesBindingFor(itemName)) {
                continue;
            }
            ModbusBindingConfig config = provider.getConfig(itemName);
            List<ItemIOConnection> connections = config.getReadConnectionsBySlaveName(slaveName);
            for (ItemIOConnection connection : connections) {
                ModbusSlave slave = modbusSlaves.get(slaveName);

                if (connection.getIndex() >= slave.getLength()) {
                    logger.warn(
                            "Item '{}' read index '{}' is out-of-bounds. Slave '{}' has been configured "
                                    + "to read only '{}' bits. Check your configuration!",
                            itemName, connection.getIndex(), slaveName, slave.getLength());
                    continue;
                }

                boolean state = coils.getBit(connection.getIndex());
                State newState = config.translateBoolean2State(connection.getPreviouslyPolledState(), state);
                // For types not taking in OpenClosedType or OnOffType (e.g. Number items)
                // We fall back to DecimalType
                if (newState.equals(UnDefType.UNDEF)) {
                    newState = state ? new DecimalType(BigDecimal.ONE) : DecimalType.ZERO;
                }

                boolean stateChanged = !newState.equals(connection.getPreviouslyPolledState());

                if (connection.supportsState(newState, stateChanged, slave.isUpdateUnchangedItems())) {
                    Transformation transformation = connection.getTransformation();
                    State transformedState = transformation == null ? newState
                            : transformation.transformState(config.getItemAcceptedDataTypes(), newState);
                    logger.trace(
                            "internalUpdateItem(BitVector): Updating slave {} item {}, state {} (changed={}) matched ItemIOConnection {}.",
                            slaveName, itemName, newState, stateChanged, connection);
                    eventPublisher.postUpdate(itemName, transformedState);
                    connection.setPreviouslyPolledState(newState);
                } else {
                    logger.trace(
                            "internalUpdateItem(BitVector): Not updating slave {} item {} since state {} (changed={}) not supported by ItemIOConnection {}.",
                            slaveName, itemName, newState, stateChanged, connection);
                }

            }

        }
    }

    /**
     * Returns names of all the items, registered with this binding
     *
     * @return list of item names
     */
    public Collection<String> getItemNames() {
        Collection<String> items = null;
        for (BindingProvider provider : providers) {
            if (items == null) {
                items = provider.getItemNames();
            } else {
                items.addAll(provider.getItemNames());
            }
        }
        return items;
    }

    /**
     * updates all slaves from the modbusSlaves
     */
    @Override
    protected void execute() {
        Collection<ModbusSlave> slaves = new HashSet<>();
        synchronized (slaves) {
            slaves.addAll(modbusSlaves.values());
        }
        for (ModbusSlave slave : slaves) {
            slave.update(this);
        }
    }

    /**
     * Clear all configuration and close all connections
     */
    private void clearAndClose() {
        try {
            // Closes all connections by calling destroyObject method in the ObjectFactory implementation
            if (connectionPool != null) {
                connectionPool.close();
            }
        } catch (Exception e) {
            // Should not happen
            logger.error("Error clearing connections", e);
        }
        modbusSlaves.clear();
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        try {
            // remove all known items if configuration changed
            clearAndClose();
            reconstructConnectionPool();
            if (config == null) {
                logger.debug("Got null config!");
                return;
            }
            Enumeration<String> keys = config.keys();
            Map<String, EndpointPoolConfiguration> slavePoolConfigs = new HashMap<>();
            Map<ModbusSlaveEndpoint, EndpointPoolConfiguration> endpointPoolConfigs = new HashMap<>();
            while (keys.hasMoreElements()) {
                final String key = keys.nextElement();
                final String value = (String) config.get(key);
                try {
                    // the config-key enumeration contains additional keys that we
                    // don't want to process here ...
                    if ("service.pid".equals(key)) {
                        continue;
                    }

                    Matcher matcher = EXTRACT_MODBUS_CONFIG_PATTERN.matcher(key);
                    if (!matcher.matches()) {
                        if ("poll".equals(key)) {
                            if (StringUtils.isNotBlank((String) config.get(key))) {
                                pollInterval = Integer.valueOf((String) config.get(key));
                            }
                        } else if ("writemultipleregisters".equals(key)) {
                            // XXX: ugly to touch base class but kept here for backwards compat
                            // FIXME: should this be deprecated as introduced as slave specific parameter?
                            ModbusSlave.setWriteMultipleRegisters(Boolean.valueOf(config.get(key).toString()));
                        } else {
                            logger.debug(
                                    "given modbus-slave-config-key '{}' does not follow the expected pattern or 'serial.<slaveId>.<{}>'",
                                    key, VALID_CONFIG_KEYS);
                        }
                        continue;
                    }

                    matcher.reset();
                    matcher.find();

                    String slave = matcher.group(2);

                    ModbusSlave modbusSlave = modbusSlaves.get(slave);
                    EndpointPoolConfiguration endpointPoolConfig = slavePoolConfigs.get(slave);
                    if (modbusSlave == null) {
                        if (matcher.group(1).equals(TCP_PREFIX)) {
                            modbusSlave = new ModbusTcpSlave(slave, connectionPool);
                        } else if (matcher.group(1).equals(UDP_PREFIX)) {
                            modbusSlave = new ModbusUdpSlave(slave, connectionPool);
                        } else if (matcher.group(1).equals(SERIAL_PREFIX)) {
                            modbusSlave = new ModbusSerialSlave(slave, connectionPool);
                        } else {
                            throw new ConfigurationException(slave, "the given slave type '" + slave + "' is unknown");
                        }
                        endpointPoolConfig = new EndpointPoolConfiguration();
                        // Do not give up if the connection attempt fails on the first time...
                        endpointPoolConfig.setConnectMaxTries(Modbus.DEFAULT_RETRIES);
                        logger.debug("modbusSlave '{}' instanciated", slave);
                        modbusSlaves.put(slave, modbusSlave);
                    }

                    String configKey = matcher.group(3);

                    if ("connection".equals(configKey)) {
                        String[] chunks = value.split(":");
                        Iterator<String> settingIterator = stringArrayIterator(chunks);
                        if (modbusSlave instanceof ModbusIPSlave) {
                            ((ModbusIPSlave) modbusSlave).setHost(settingIterator.next());
                            //
                            // Defaults for endpoint and slave
                            //
                            modbusSlave.setRetryDelayMillis(DEFAULT_TCP_INTER_TRANSACTION_DELAY_MILLIS);
                            endpointPoolConfig.setPassivateBorrowMinMillis(DEFAULT_TCP_INTER_TRANSACTION_DELAY_MILLIS);

                            //
                            // Optional parameters
                            //
                            try {
                                ((ModbusIPSlave) modbusSlave).setPort(Integer.valueOf(settingIterator.next()));

                                long passivateBorrowMinMillis = Long.parseLong(settingIterator.next());
                                modbusSlave.setRetryDelayMillis(passivateBorrowMinMillis);
                                endpointPoolConfig.setPassivateBorrowMinMillis(passivateBorrowMinMillis);

                                endpointPoolConfig.setReconnectAfterMillis(Integer.parseInt(settingIterator.next()));

                                // time to wait before trying connect closed connection. Note that
                                // ModbusSlaveConnectionFactoryImpl makes sure that max{passivateBorrowMinMillis, this
                                // parameter} is waited between connection attempts
                                endpointPoolConfig.setInterConnectDelayMillis(Long.parseLong(settingIterator.next()));

                                endpointPoolConfig.setConnectMaxTries(Integer.parseInt(settingIterator.next()));
                                endpointPoolConfig.setConnectTimeoutMillis(Integer.parseInt(settingIterator.next()));
                            } catch (NoSuchElementException e) {
                                // Some of the optional parameters are missing -- it's ok!
                            }
                            if (settingIterator.hasNext()) {
                                String errMsg = String
                                        .format("%s Has too many colon (:) separated connection settings for a tcp/udp modbus slave. "
                                                + "Expecting at most 6 parameters: hostname (mandatory) and "
                                                + "optionally (in this order) port number, "
                                                + "interTransactionDelayMillis, reconnectAfterMillis,"
                                                + "interConnectDelayMillis, connectMaxTries, connectTimeout.", key);
                                throw new ConfigurationException(key, errMsg);
                            }
                        } else if (modbusSlave instanceof ModbusSerialSlave) {
                            SerialParameters serialParameters = new SerialParameters();
                            serialParameters.setPortName(settingIterator.next());
                            //
                            // Defaults for endpoint and slave
                            //
                            endpointPoolConfig.setReconnectAfterMillis(-1); // never "disconnect" (close/open serial
                                                                            // port)
                                                                            // serial connection between borrows
                            modbusSlave.setRetryDelayMillis(DEFAULT_SERIAL_INTER_TRANSACTION_DELAY_MILLIS);
                            endpointPoolConfig
                                    .setPassivateBorrowMinMillis(DEFAULT_SERIAL_INTER_TRANSACTION_DELAY_MILLIS);

                            //
                            // Optional parameters
                            //
                            try {
                                serialParameters.setBaudRate(settingIterator.next());
                                serialParameters.setDatabits(settingIterator.next());
                                serialParameters.setParity(settingIterator.next());
                                serialParameters.setStopbits(settingIterator.next());
                                serialParameters.setEncoding(settingIterator.next());

                                // time to wait between connection passive+borrow, i.e. time to wait between
                                // transactions
                                long passivateBorrowMinMillis = Long.parseLong(settingIterator.next());
                                modbusSlave.setRetryDelayMillis(passivateBorrowMinMillis);
                                endpointPoolConfig.setPassivateBorrowMinMillis(passivateBorrowMinMillis);

                                serialParameters.setReceiveTimeoutMillis(settingIterator.next());
                                serialParameters.setFlowControlIn(settingIterator.next());
                                serialParameters.setFlowControlOut(settingIterator.next());
                            } catch (NoSuchElementException e) {
                                // Some of the optional parameters are missing -- it's ok!
                            }
                            if (settingIterator.hasNext()) {
                                String errMsg = String.format(
                                        "%s Has too many colon (:) separated connection settings for a serial modbus slave. "
                                                + "Expecting at most 9 parameters (got %d): devicePort (mandatory), "
                                                + "and 0 or more optional parameters (in this order): "
                                                + "baudRate, dataBits, parity, stopBits, "
                                                + "encoding, interTransactionWaitMillis, "
                                                + "receiveTimeoutMillis, flowControlIn, flowControlOut",
                                        key, chunks.length);
                                throw new ConfigurationException(key, errMsg);
                            }

                            ((ModbusSerialSlave) modbusSlave).setSerialParameters(serialParameters);
                        }
                    } else if ("start".equals(configKey)) {
                        modbusSlave.setStart(Integer.valueOf(value));
                    } else if ("length".equals(configKey)) {
                        modbusSlave.setLength(Integer.valueOf(value));
                    } else if ("id".equals(configKey)) {
                        modbusSlave.setId(Integer.valueOf(value));
                    } else if ("type".equals(configKey)) {
                        if (ArrayUtils.contains(ModbusBindingProvider.SLAVE_DATA_TYPES, value)) {
                            modbusSlave.setType(value);
                        } else {
                            throw new ConfigurationException(configKey,
                                    "the given slave type '" + value + "' is invalid");
                        }
                    } else if ("valuetype".equals(configKey)) {
                        if (ArrayUtils.contains(ModbusBindingProvider.VALUE_TYPES, value)) {
                            modbusSlave.setValueType(value);
                        } else {
                            throw new ConfigurationException(configKey,
                                    "the given value type '" + value + "' is invalid");
                        }
                    } else if ("rawdatamultiplier".equals(configKey)) {
                        modbusSlave.setRawDataMultiplier(Double.valueOf(value.toString()));
                    } else if ("updateunchangeditems".equals(configKey)) {
                        modbusSlave.setUpdateUnchangedItems(Boolean.valueOf(value.toString()));
                    } else if ("postundefinedonreaderror".equals(configKey)) {
                        modbusSlave.setPostUndefinedOnReadError(Boolean.valueOf(value.toString()));
                    } else {
                        throw new ConfigurationException(configKey,
                                "the given configKey '" + configKey + "' is unknown");
                    }
                    modbusSlaves.put(slave, modbusSlave);
                    slavePoolConfigs.put(slave, endpointPoolConfig);
                } catch (Exception e) {
                    String errMsg = String.format("Exception when parsing configuration parameter %s = %s  --  %s %s",
                            key, value, e.getClass().getName(), e.getMessage());
                    logger.error(errMsg);
                    throw new ConfigurationException(key, errMsg);
                }
            }
            // Finally, go through each slave definition, and combine the slave pool configurations
            for (Entry<String, EndpointPoolConfiguration> slaveEntry : slavePoolConfigs.entrySet()) {
                String slave = slaveEntry.getKey();
                EndpointPoolConfiguration poolConfiguration = slaveEntry.getValue();
                ModbusSlaveEndpoint endpoint = modbusSlaves.get(slave).getEndpoint();
                EndpointPoolConfiguration existingPoolConfiguration = endpointPoolConfigs.get(endpoint);
                // Do we have two slaves with same endpoint, but different pool configuration parameters? Warn if we do.
                if (existingPoolConfiguration != null && !existingPoolConfiguration.equals(poolConfiguration)) {
                    logger.warn(
                            "Slave {} (endpoint {}) has different retry/connection delay "
                                    + "(EndpointPoolConfiguration) etc. settings. Replacing {} with {}",
                            slave, endpoint, existingPoolConfiguration, poolConfiguration);
                }
                endpointPoolConfigs.put(endpoint, poolConfiguration);
            }
            connectionFactory.applyEndpointPoolConfigs(endpointPoolConfigs);
            logger.debug("Parsed the following slave->endpoint configurations: {}. If the endpoint is same, "
                    + "connections are shared between the instances.", slavePoolConfigs);
            logger.debug("Parsed the following pool configurations: {}", endpointPoolConfigs);
            logger.debug("config looked good");
            setProperlyConfigured(true);
        } catch (ConfigurationException ce) {
            setProperlyConfigured(false);
            throw ce;
        }
    }

    private static Iterator<String> stringArrayIterator(final String[] chunks) {
        Iterator<String> settingIterator = new Iterator<String>() {

            private ResettableIterator inner = IteratorUtils.arrayIterator(chunks);

            @Override
            public String next() {
                return (String) inner.next();
            }

            @Override
            public boolean hasNext() {
                return inner.hasNext();
            }

            @Override
            public void remove() {
                inner.remove();
            }

        };
        return settingIterator;
    }

}
