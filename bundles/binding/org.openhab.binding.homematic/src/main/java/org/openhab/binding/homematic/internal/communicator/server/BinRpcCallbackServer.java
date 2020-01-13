/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.homematic.internal.communicator.server;

import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackReceiver;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server implementation for receiving messages via BIN-RPC from the Homematic
 * server.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BinRpcCallbackServer implements HomematicCallbackServer {
    private final static Logger logger = LoggerFactory.getLogger(BinRpcCallbackServer.class);

    private Thread networkServiceThread;
    private BinRpcNetworkService networkService;
    private HomematicCallbackReceiver callbackReceiver;
    private HomematicConfig config = HomematicContext.getInstance().getConfig();

    /**
     * Creates the server and registers a callbackReceiver.
     */
    public BinRpcCallbackServer(HomematicCallbackReceiver callbackReceiver) {
        this.callbackReceiver = callbackReceiver;
    }

    /**
     * Starts the server threads.
     */
    @Override
    public void start() throws Exception {
        logger.info("Starting {} at port {}", this.getClass().getSimpleName(), config.getCallbackPort());

        networkService = new BinRpcNetworkService(callbackReceiver);
        networkServiceThread = new Thread(networkService);
        networkServiceThread.start();
    }

    /**
     * Stops the server.
     */
    @Override
    public void shutdown() {
        logger.debug("Shutting down {}", this.getClass().getSimpleName());
        try {
            if (networkService != null) {
                networkService.shutdown();
            }
            if (networkServiceThread != null) {
                networkServiceThread.interrupt();
                networkServiceThread.join(5000);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
