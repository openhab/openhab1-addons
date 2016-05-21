/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal.pooling;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.net.ModbusSlaveConnection;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.net.UDPMasterConnection;

/**
 * ModbusSlaveConnectionFactoryImpl responsible of the lifecycle of modbus slave connections
 *
 * The actual pool uses instance of this class to create and destroy connections as-needed.
 *
 * The overall functionality goes as follow
 * - create: create connection object but do not connect it yet
 * - destroyObject: close connection and free all resources. Called by the pool when the pool is being closed or the
 * object is invalidated.
 * - activateObject: prepare connection to be used. In practice, connect if disconnected
 * - passivateObject: passivate connection before returning it back to the pool. Currently, passivateObject closes all
 * IP-based connections every now and then (reconnectAfterMillis). Serial connections we keep open.
 * - wrap: wrap created connection to pooled object wrapper class. It tracks usage statistics and last connection time.
 *
 * Note that the implementation must be thread safe.
 *
 */
public class ModbusSlaveConnectionFactoryImpl
        extends BaseKeyedPooledObjectFactory<ModbusSlaveEndpoint, ModbusSlaveConnection> {

    private static class PooledConnection extends DefaultPooledObject<ModbusSlaveConnection> {

        private long lastConnected;

        public PooledConnection(ModbusSlaveConnection object) {
            super(object);
        }

        public long getLastConnected() {
            return lastConnected;
        }

        public void setLastConnected(long lastConnected) {
            this.lastConnected = lastConnected;
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(ModbusSlaveConnectionFactoryImpl.class);
    private volatile Map<ModbusSlaveEndpoint, EndpointPoolConfiguration> endpointPoolConfigs = new ConcurrentHashMap<ModbusSlaveEndpoint, EndpointPoolConfiguration>();
    private volatile Map<ModbusSlaveEndpoint, Long> lastPassivateMillis = new ConcurrentHashMap<ModbusSlaveEndpoint, Long>();
    private volatile Map<ModbusSlaveEndpoint, Long> lastConnectMillis = new ConcurrentHashMap<ModbusSlaveEndpoint, Long>();

    private InetAddress getInetAddress(ModbusIPSlaveEndpoint key) {
        try {
            return InetAddress.getByName(key.getAddress());
        } catch (UnknownHostException e) {
            logger.error("KeyedPooledModbusSlaveConnectionFactory: Unknown host: {}. Connection creation failed.",
                    e.getMessage());
            return null;
        }
    }

    @Override
    public ModbusSlaveConnection create(ModbusSlaveEndpoint endpoint) throws Exception {
        return endpoint.accept(new ModbusSlaveEndpointVisitor<ModbusSlaveConnection>() {
            @Override
            public ModbusSlaveConnection visit(ModbusSerialSlaveEndpoint modbusSerialSlavePoolingKey) {
                SerialConnection connection = new SerialConnection(modbusSerialSlavePoolingKey.getSerialParameters());
                logger.trace("Created connection {} for endpoint {}", connection, modbusSerialSlavePoolingKey);
                return connection;
            }

            @Override
            public ModbusSlaveConnection visit(ModbusTCPSlaveEndpoint key) {
                InetAddress address = getInetAddress(key);
                if (address == null) {
                    return null;
                }
                TCPMasterConnection connection = new TCPMasterConnection(address, key.getPort());
                logger.trace("Created connection {} for endpoint {}", connection, key);
                return connection;
            }

            @Override
            public ModbusSlaveConnection visit(ModbusUDPSlaveEndpoint key) {
                InetAddress address = getInetAddress(key);
                if (address == null) {
                    return null;
                }
                UDPMasterConnection connection = new UDPMasterConnection(address, key.getPort());
                logger.trace("Created connection {} for endpoint {}", connection, key);
                return connection;
            }
        });
    }

    @Override
    public PooledObject<ModbusSlaveConnection> wrap(ModbusSlaveConnection connection) {
        return new PooledConnection(connection);
    }

    @Override
    public void destroyObject(ModbusSlaveEndpoint endpoint, final PooledObject<ModbusSlaveConnection> obj) {
        obj.getObject().resetConnection();
    }

    @Override
    public void activateObject(ModbusSlaveEndpoint endpoint, PooledObject<ModbusSlaveConnection> obj) throws Exception {
        if (obj.getObject() == null) {
            return;
        }
        try {
            ModbusSlaveConnection connection = obj.getObject();
            EndpointPoolConfiguration config = endpointPoolConfigs.get(endpoint);

            if (connection.isConnected()) {
                if (config != null) {
                    long waited = waitAtleast(lastPassivateMillis.get(endpoint), config.getPassivateBorrowMinMillis());
                    logger.trace(
                            "Waited {}ms (passivateBorrowMinMillis {}ms) before giving returning connection {} for endpoint {}, to ensure delay between transactions.",
                            waited, config.getPassivateBorrowMinMillis(), obj.getObject(), endpoint);
                }
            } else {
                // invariant: !connection.isConnected()
                tryConnect(endpoint, obj, connection, config);
            }
        } catch (Exception e) {
            logger.error("Error connecting connection {} for endpoint {}: {}", obj.getObject(), endpoint,
                    e.getMessage());
        }
    }

    @Override
    public void passivateObject(ModbusSlaveEndpoint endpoint, PooledObject<ModbusSlaveConnection> obj) {
        ModbusSlaveConnection connection = obj.getObject();
        if (connection == null) {
            return;
        }
        logger.trace("Passivating connection {} for endpoint {}...", connection, endpoint);
        lastPassivateMillis.put(endpoint, System.currentTimeMillis());
        EndpointPoolConfiguration configuration = endpointPoolConfigs.get(endpoint);
        long reconnectAfterMillis = configuration == null ? 0 : configuration.getReconnectAfterMillis();
        long connectionAgeMillis = System.currentTimeMillis() - ((PooledConnection) obj).getLastConnected();
        if (reconnectAfterMillis == 0 || (reconnectAfterMillis > 0 && connectionAgeMillis > reconnectAfterMillis)) {
            logger.trace(
                    "(passivate) Connection {} (endpoint {}) age {}ms is over the reconnectAfterMillis={}ms limit -> disconnecting.",
                    connection, endpoint, connectionAgeMillis, reconnectAfterMillis);
            connection.resetConnection();
        } else {
            logger.trace(
                    "(passivate) Connection {} (endpoint {}) age ({}ms) is below the reconnectAfterMillis ({}ms) limit. Keep the connection open.",
                    connection, endpoint, connectionAgeMillis, reconnectAfterMillis);
        }
        logger.trace("...Passivated connection {} for endpoint {}", obj.getObject(), endpoint);
    }

    @Override
    public boolean validateObject(ModbusSlaveEndpoint key, PooledObject<ModbusSlaveConnection> p) {
        boolean valid = p.getObject() != null && p.getObject().isConnected();
        logger.trace("Validating endpoint {} connection {} -> {}", key, p.getObject(), valid);
        return valid;
    }

    public Map<ModbusSlaveEndpoint, EndpointPoolConfiguration> getEndpointPoolConfigs() {
        return endpointPoolConfigs;
    }

    public void applyEndpointPoolConfigs(Map<ModbusSlaveEndpoint, EndpointPoolConfiguration> endpointPoolConfigs) {
        this.endpointPoolConfigs = new ConcurrentHashMap<ModbusSlaveEndpoint, EndpointPoolConfiguration>(
                endpointPoolConfigs);
    }

    private void tryConnect(ModbusSlaveEndpoint endpoint, PooledObject<ModbusSlaveConnection> obj,
            ModbusSlaveConnection connection, EndpointPoolConfiguration config) throws Exception {
        if (connection.isConnected()) {
            return;
        }
        int tryIndex = 0;
        Long lastConnect = lastConnectMillis.get(endpoint);
        int maxTries = config == null ? 1 : config.getConnectMaxTries();
        do {
            try {
                if (config != null) {
                    long waited = waitAtleast(lastConnect,
                            Math.max(config.getInterConnectDelayMillis(), config.getPassivateBorrowMinMillis()));
                    if (waited > 0) {
                        logger.trace(
                                "Waited {}ms (interConnectDelayMillis {}ms, passivateBorrowMinMillis {}ms) before "
                                        + "connecting disconnected connection {} for endpoint {}, to allow delay "
                                        + "between connections re-connects",
                                waited, config.getInterConnectDelayMillis(), config.getPassivateBorrowMinMillis(),
                                obj.getObject(), endpoint);
                    }

                }
                connection.connect();
                long curTime = System.currentTimeMillis();
                ((PooledConnection) obj).setLastConnected(curTime);
                lastConnectMillis.put(endpoint, curTime);
                break;
            } catch (Exception e) {
                tryIndex++;
                logger.error("connect try {}/{} error: {}. Connection {}. Endpoint {}", tryIndex, maxTries,
                        e.getMessage(), connection, endpoint);
                if (tryIndex >= maxTries) {
                    logger.error("re-connect reached max tries {}, throwing last error: {}. Connection {}. Endpoint {}",
                            maxTries, e.getMessage(), connection, endpoint);
                    throw e;
                }
                lastConnect = System.currentTimeMillis();
            }
        } while (true);
    }

    private long waitAtleast(Long lastOperation, long waitMillis) {
        if (lastOperation == null) {
            return 0;
        }
        long millisSinceLast = System.currentTimeMillis() - lastOperation;
        long millisToWaitStill = Math.min(waitMillis, Math.max(0, waitMillis - millisSinceLast));
        try {
            Thread.sleep(millisToWaitStill);
        } catch (InterruptedException e) {
            logger.error("wait interrupted: {}", e.getMessage());
        }
        return millisToWaitStill;
    }

}