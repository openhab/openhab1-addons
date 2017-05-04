/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.pool2.KeyedObjectPool;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.pooling.ModbusSlaveEndpoint;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.ModbusSlaveConnection;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.BitVector;

/**
 * ModbusSlave class is an abstract class that server as a base class for
 * MobvusTCPSlave and ModbusSerialSlave instantiates physical Modbus slave.
 * It is responsible for polling data from physical device using appropriate connection.
 * It is also responsible for updating physical devices according to OpenHAB commands
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public abstract class ModbusSlave {

    private static final Logger logger = LoggerFactory.getLogger(ModbusSlave.class);

    /** name - slave name from cfg file, used for items binding */
    protected String name = null;
    protected ModbusSlaveEndpoint endpoint;

    private static boolean writeMultipleRegisters = false;

    public static void setWriteMultipleRegisters(boolean setwmr) {
        writeMultipleRegisters = setwmr;
    }

    /**
     * Type of data provided by the physical device
     * "coil" and "discrete" use boolean (bit) values
     * "input" and "holding" use byte values
     */
    private String type;

    private KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> connectionPool;

    /** Modbus slave id */
    private int id = 1;

    /** starting reference and number of item to fetch from the device */
    private int start = 0;

    private int length = 0;

    /**
     * How to interpret Modbus register values.
     * Examples:
     * uint16 - one register - one unsigned integer value (default)
     * int32 - every two registers will be interpreted as single 32-bit integer value
     * bit - every register will be interpreted as 16 independent 1-bit values
     */
    private String valueType = ModbusBindingProvider.VALUE_TYPE_UINT16;

    /**
     * A multiplier for the raw incoming data
     *
     * @note rawMultiplier can also be used for divisions, by simply
     *       setting the value smaller than zero.
     *
     *       E.g.:
     *       - data/100 ... rawDataMultiplier=0.01
     */
    private double rawDataMultiplier = 1.0;

    private Object storage;
    private Exception readError;
    protected ModbusTransaction transaction = null;

    /**
     * Does the binding post updates even when the item did not change it's state?
     *
     * default is "false"
     */
    private boolean updateUnchangedItems = false;

    /**
     * Does the binding post UNDEF update event when read fails
     *
     * default is "false"
     */
    private boolean postUndefinedOnReadError = false;

    /**
     * @param slave slave name from cfg file used for item binding
     * @connectionPool pool to create connections
     */
    public ModbusSlave(String slave, KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> connectionPool) {
        this.name = slave;
        this.connectionPool = connectionPool;
    }

    /**
     * writes data to Modbus device corresponding to OpenHAB command
     * works only with types "coil" and "holding"
     *
     * @param command OpenHAB command received
     * @param writeIndex
     * @param readIndex read index to use for commands that require previously polled value
     *
     */
    public void executeCommand(String itemName, Command command, int writeIndex,
            Optional<State> previouslyPolledState) {
        try {
            if (ModbusBindingProvider.TYPE_COIL.equals(getType())) {
                setCoil(command, writeIndex);
            } else if (ModbusBindingProvider.TYPE_HOLDING.equals(getType())) {
                setRegister(itemName, command, writeIndex, previouslyPolledState);
            }
        } catch (Exception e) {
            // Error already logged, just continue as normal
        }
    }

    /**
     * Calculates boolean value that will be written to the device as a result of OpenHAB command
     * Used with item bound to "coil" type slaves
     *
     * @param command OpenHAB command received by the item. OnOffType, OpenClosedType and DecimalType are handled.
     * @return new boolean value to be written to the device
     */
    protected static boolean translateCommand2Boolean(Command command) {
        if (command.equals(OnOffType.ON)) {
            return true;
        }
        if (command.equals(OnOffType.OFF)) {
            return false;
        }
        if (command.equals(OpenClosedType.OPEN)) {
            return true;
        }
        if (command.equals(OpenClosedType.CLOSED)) {
            return false;
        }
        if (command instanceof DecimalType) {
            // Transformation might return DecimalType commands even for coil items
            return !command.equals(DecimalType.ZERO);
        }
        throw new IllegalArgumentException("command not supported");
    }

    /**
     * Performs physical write to device when slave type is "coil"
     *
     * @param command command received from OpenHAB
     * @param writeIndex
     * @throws ModbusConnectionException when connection cannot be established
     * @throws ModbusException ModbusIOException on IO errors, ModbusSlaveException with protocol level exceptions
     * @throws ModbusUnexpectedTransactionIdException when response transaction id does not match the request
     */
    private void setCoil(Command command, int writeIndex)
            throws ModbusConnectionException, ModbusException, ModbusUnexpectedTransactionIdException {
        int coilOffset = writeIndex;
        boolean b = translateCommand2Boolean(command);
        doSetCoil(getStart() + coilOffset, b);
    }

    /**
     * Performs physical write to device when slave type is "holding" using Modbus FC06 function
     *
     * @param command command received from OpenHAB
     * @param config
     * @throws ModbusConnectionException when connection cannot be established
     * @throws ModbusException ModbusIOException on IO errors, ModbusSlaveException with protocol level exceptions
     * @throws ModbusUnexpectedTransactionIdException when response transaction id does not match the request
     */
    protected void setRegister(String itemName, Command command, int writeIndex, Optional<State> previouslyPolledState)
            throws ModbusConnectionException, ModbusException, ModbusUnexpectedTransactionIdException {
        int writeRegister = getStart() + writeIndex;

        Register newValue;
        if (command instanceof IncreaseDecreaseType || command instanceof UpDownType) {
            if (!previouslyPolledState.isPresent()) {
                logger.warn("Not polled value for item {}. Cannot process command {}", itemName, command);
                return;
            }
            State prevState = previouslyPolledState.get();

            if (!(prevState instanceof Number)) {
                logger.warn("Previously polled value ({}) is not number, cannot process command {}",
                        previouslyPolledState, command);
                return;
            }
            int prevValue = ((Number) prevState).intValue();
            newValue = new SimpleRegister();
            if (command.equals(IncreaseDecreaseType.INCREASE) || command.equals(UpDownType.UP)) {
                newValue.setValue(prevValue + 1);
            } else if (command.equals(IncreaseDecreaseType.DECREASE) || command.equals(UpDownType.DOWN)) {
                newValue.setValue(prevValue - 1);
            }
        } else if (command instanceof DecimalType) {
            newValue = new SimpleRegister();
            newValue.setValue(((DecimalType) command).intValue());
        } else if (command instanceof OnOffType || command instanceof OpenClosedType) {
            newValue = new SimpleRegister();
            if (command.equals(OnOffType.ON) || command.equals(OpenClosedType.OPEN)) {
                newValue.setValue(1);
            } else if (command.equals(OnOffType.OFF) || command.equals(OpenClosedType.CLOSED)) {
                newValue.setValue(0);
            }
        } else {
            logger.warn("Item {} received unsupported command: {}. Not setting register.", itemName, command);
            return;
        }

        ModbusRequest request = null;
        if (writeMultipleRegisters) {
            Register[] regs = new Register[1];
            regs[0] = newValue;
            request = new WriteMultipleRegistersRequest(writeRegister, regs);
        } else {
            request = new WriteSingleRegisterRequest(writeRegister, newValue);
        }
        request.setUnitID(getId());
        logger.debug("ModbusSlave ({}): FC{} ref={} value={}", name, request.getFunctionCode(), writeRegister,
                newValue.getValue());
        executeWriteRequest(request);
    }

    /**
     * @return slave name from cfg file
     */
    public String getName() {
        return name;
    }

    /**
     * Sends boolean (bit) data to the device using Modbus FC05 function
     *
     * @param writeRegister
     * @param b
     * @throws ModbusUnexpectedTransactionIdException
     * @throws ModbusException
     * @throws ModbusConnectionException
     */
    public void doSetCoil(int writeRegister, boolean b)
            throws ModbusConnectionException, ModbusException, ModbusUnexpectedTransactionIdException {
        ModbusRequest request = new WriteCoilRequest(writeRegister, b);
        request.setUnitID(getId());
        logger.debug("ModbusSlave ({}): FC05 ref={} value={}", name, writeRegister, b);
        executeWriteRequest(request);
    }

    /**
     *
     * @param request
     * @throws ModbusConnectionException when connection cannot be established
     * @throws ModbusException ModbusIOException on IO errors, ModbusSlaveException with protocol level exceptions
     * @throws ModbusUnexpectedTransactionIdException when response transaction id does not match the request
     */
    private void executeWriteRequest(ModbusRequest request)
            throws ModbusConnectionException, ModbusException, ModbusUnexpectedTransactionIdException {
        ModbusSlaveEndpoint endpoint = getEndpoint();
        ModbusSlaveConnection connection = null;
        ModbusResponse response;
        int requestTransactionID;
        try {
            connection = getConnection(endpoint);
            if (connection == null) {
                logger.warn("ModbusSlave ({}): not connected -- aborting request {}", name, request);
                throw new ModbusConnectionException(endpoint);
            }
            synchronized (transaction) {
                transaction.setRequest(request);
                try {
                    logger.trace(
                            "Executing modbus request {} using transaction {} (global transaction id before increment {}) to write data",
                            transaction.getRequest(), transaction, transaction.getTransactionID());
                    transaction.execute();
                } catch (Exception e) {
                    // Note, one could catch ModbusIOException and ModbusSlaveException if more detailed
                    // exception handling is required. For now, all exceptions are handled the same way with writes.
                    logger.error("ModbusSlave ({}): error when executing write request ({}): {}", name, request,
                            e.getMessage());
                    invalidate(endpoint, connection);
                    // set connection to null such that it is not returned to pool
                    connection = null;
                    throw e;
                }
                response = transaction.getResponse();
                logger.trace("ModbusSlave ({}): response for write (FC={}) {}", name, response.getFunctionCode(),
                        response.getHexMessage());
                requestTransactionID = transaction.getRequest().getTransactionID();
                if ((response.getTransactionID() != requestTransactionID) && !response.isHeadless()) {
                    logger.warn(
                            "ModbusSlave ({}): Transaction id of the response ({}) does not match request {} id {}.  Endpoint {}. Connection: {}. Ignoring response.",
                            name, response.getTransactionID(), request, requestTransactionID, endpoint, connection);
                    throw new ModbusUnexpectedTransactionIdException();
                }
            }
        } finally {
            returnConnection(endpoint, connection);
        }
    }

    protected ModbusSlaveConnection getConnection(ModbusSlaveEndpoint endpoint) {
        ModbusSlaveConnection connection = borrowConnection(endpoint);
        return connection;
    }

    private ModbusSlaveConnection borrowConnection(ModbusSlaveEndpoint endpoint) {
        ModbusSlaveConnection connection = null;
        long start = System.currentTimeMillis();
        try {
            connection = connectionPool.borrowObject(endpoint);
        } catch (Exception e) {
            invalidate(endpoint, connection);
            logger.warn("ModbusSlave ({}): Error getting a new connection for endpoint {}. Error was: {}", name,
                    endpoint, e.getMessage());
        }
        logger.trace("ModbusSlave ({}): borrowing connection (got {}) for endpoint {} took {} ms", name, connection,
                endpoint, System.currentTimeMillis() - start);
        return connection;
    }

    private void invalidate(ModbusSlaveEndpoint endpoint, ModbusSlaveConnection connection) {
        if (connection == null) {
            return;
        }
        try {
            connectionPool.invalidateObject(endpoint, connection);
        } catch (Exception e) {
            logger.warn("ModbusSlave ({}): Error invalidating connection in pool for endpoint {}. Error was: {}", name,
                    endpoint, e.getMessage());
        }
    }

    private void returnConnection(ModbusSlaveEndpoint endpoint, ModbusSlaveConnection connection) {
        if (connection == null) {
            return;
        }
        try {
            connectionPool.returnObject(endpoint, connection);
        } catch (Exception e) {
            logger.warn("ModbusSlave ({}): Error returning connection to pool for endpoint {}. Error was: {}", name,
                    endpoint, e.getMessage());
        }
        logger.trace("ModbusSlave ({}): returned connection for endpoint {}", name, endpoint);
    }

    /**
     * Reads data from the connected device and updates items with the new data
     *
     * @param binding ModbusBindig that stores providers information
     */
    public void update(ModbusBinding binding) {
        try {

            Object local = null;
            Exception localReadError = null;
            try {
                if (ModbusBindingProvider.TYPE_COIL.equals(getType())) {
                    ModbusRequest request = new ReadCoilsRequest(getStart(), getLength());
                    if (this instanceof ModbusSerialSlave) {
                        request.setHeadless();
                    }
                    ReadCoilsResponse response = (ReadCoilsResponse) getModbusData(request);
                    local = response.getCoils();
                } else if (ModbusBindingProvider.TYPE_DISCRETE.equals(getType())) {
                    ModbusRequest request = new ReadInputDiscretesRequest(getStart(), getLength());
                    ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) getModbusData(request);
                    local = response.getDiscretes();
                } else if (ModbusBindingProvider.TYPE_HOLDING.equals(getType())) {
                    ModbusRequest request = new ReadMultipleRegistersRequest(getStart(), getLength());
                    ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) getModbusData(request);
                    local = response.getRegisters();
                } else if (ModbusBindingProvider.TYPE_INPUT.equals(getType())) {
                    ModbusRequest request = new ReadInputRegistersRequest(getStart(), getLength());
                    ReadInputRegistersResponse response = (ReadInputRegistersResponse) getModbusData(request);
                    local = response.getRegisters();
                }
            } catch (ModbusException e) {
                // Logging already done in getModbusData
                localReadError = e;
            } catch (ModbusConnectionException e) {
                // Logging already done in getModbusData
                localReadError = e;
            } catch (ModbusUnexpectedTransactionIdException e) {
                // Logging already done in getModbusData
                localReadError = e;
            }

            if (storage == null) {
                storage = local;
                readError = localReadError;
            } else {
                synchronized (storage) {
                    storage = local;
                    readError = localReadError;
                }
            }
            Collection<String> items = binding.getItemNames();
            for (String item : items) {
                updateItem(binding, item);
            }
        } catch (Exception e) {
            logger.error("ModbusSlave ({}) error getting response from slave", name, e);
        }

    }

    /**
     * Updates OpenHAB item with data read from slave device
     * works only for type "coil" and "holding"
     *
     * @param binding ModbusBinding
     * @param item item to update
     */
    private void updateItem(ModbusBinding binding, String item) {
        if (readError == null) {
            if (ModbusBindingProvider.TYPE_COIL.equals(getType())
                    || ModbusBindingProvider.TYPE_DISCRETE.equals(getType())) {
                binding.internalUpdateItem(name, (BitVector) storage, item);
            } else if (ModbusBindingProvider.TYPE_HOLDING.equals(getType())
                    || ModbusBindingProvider.TYPE_INPUT.equals(getType())) {
                binding.internalUpdateItem(name, (InputRegister[]) storage, item);
            }
        } else {
            binding.internalUpdateReadErrorItem(name, readError, item);
        }
    }

    public boolean isUpdateUnchangedItems() {
        return updateUnchangedItems;
    }

    public void setUpdateUnchangedItems(boolean updateUnchangedItems) {
        this.updateUnchangedItems = updateUnchangedItems;
    }

    /**
     * Executes Modbus transaction that reads data from the device and returns response data
     *
     * @param request describes what data are requested from the device
     * @return response data
     * @throws ModbusConnectionException when connection cannot be established
     * @throws ModbusException ModbusIOException on IO errors, ModbusSlaveException with protocol level exceptions
     * @throws ModbusUnexpectedTransactionIdException when response transaction id does not match the request
     */
    private ModbusResponse getModbusData(ModbusRequest request)
            throws ModbusConnectionException, ModbusException, ModbusUnexpectedTransactionIdException {
        ModbusSlaveEndpoint endpoint = getEndpoint();
        ModbusSlaveConnection connection = null;
        ModbusResponse response = null;
        int requestTransactionID;
        try {
            connection = getConnection(endpoint);
            if (connection == null) {
                logger.warn("ModbusSlave ({}) not connected -- aborting read request {}. Endpoint {}", name, request,
                        endpoint);
                throw new ModbusConnectionException(endpoint);
            }
            request.setUnitID(getId());
            synchronized (transaction) {
                transaction.setRequest(request);
                try {
                    logger.trace(
                            "Executing modbus request {} using transaction {} (global  transaction id before increment {}) to read data",
                            transaction.getRequest(), transaction, transaction.getTransactionID());
                    transaction.execute();
                } catch (ModbusException e) {
                    logger.error(
                            "ModbusSlave ({}): Error getting modbus data for request {}. Error: {}. Endpoint {}. Connection: {}",
                            name, request, e.getMessage(), endpoint, connection);
                    invalidate(endpoint, connection);
                    // Invalidated connections should not be returned
                    connection = null;
                    throw e;
                }

                requestTransactionID = transaction.getRequest().getTransactionID();
                response = transaction.getResponse();

                if ((response.getTransactionID() != requestTransactionID) && !response.isHeadless()) {
                    logger.warn(
                            "ModbusSlave ({}): Transaction id of the response ({}) does not match request {} id {}.  Endpoint {}. Connection: {}. Ignoring response.",
                            name, response.getTransactionID(), request, requestTransactionID, endpoint, connection);
                    throw new ModbusUnexpectedTransactionIdException();
                }
            }
            logger.trace("ModbusSlave ({}): response for read (FC={}) {}", name, response.getFunctionCode(),
                    response.getHexMessage());
        } finally {
            returnConnection(endpoint, connection);
        }
        return response;
    }

    public ModbusSlaveEndpoint getEndpoint() {
        return endpoint;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public void setRawDataMultiplier(double value) {
        this.rawDataMultiplier = value;
    }

    public double getRawDataMultiplier() {
        return rawDataMultiplier;
    }

    public long getRetryDelayMillis() {
        if (transaction == null) {
            throw new IllegalStateException("transaction not initialized!");
        }
        return transaction.getRetryDelayMillis();
    }

    public void setRetryDelayMillis(long retryDelayMillis) {
        if (transaction == null) {
            throw new IllegalStateException("transaction not initialized!");
        }
        transaction.setRetryDelayMillis(retryDelayMillis);
    }

    public boolean isPostUndefinedOnReadError() {
        return postUndefinedOnReadError;
    }

    public void setPostUndefinedOnReadError(boolean postUndefinedOnReadError) {
        this.postUndefinedOnReadError = postUndefinedOnReadError;
    }
}
