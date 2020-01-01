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
package org.openhab.binding.gc100ir.internal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.openhab.binding.gc100ir.internal.response.GC100IRCommand;
import org.openhab.binding.gc100ir.internal.response.GC100IRCommandCode;
import org.openhab.binding.gc100ir.lib.GC100IRControlPoint;
import org.openhab.binding.gc100ir.util.GC100ItemBean;
import org.openhab.binding.gc100ir.util.GC100ItemUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the TCP/IP socket connection for a single GC100 instance.
 *
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 */
public class GC100IRConnector {

    private static final Logger logger = LoggerFactory.getLogger(GC100IRConnector.class);

    // the GC100 instance
    private final GC100IRHost gc100;
    private GC100IRControlPoint gc100ControlPoint;

    private boolean connected = false;

    private static GC100ItemUtility gc100ItemUtility;

    /**
     * @param gc100
     *            The host to connect to. Give a reachable hostname or ip
     *            address, without protocol or port
     */
    public GC100IRConnector(GC100IRHost gc100) {

        this.gc100 = gc100;
        this.gc100ControlPoint = GC100IRControlPoint.getInstance();
        String deviceResponse = null;
        try {
            deviceResponse = this.gc100ControlPoint.queryGC100(GC100IRControlPoint.GC_100_QUERY_MESSAGE_GET_DEVICES,
                    gc100.getHostname());
        } catch (UnknownHostException e) {
            logger.error("UnknownHostException: ", e);
        } catch (IOException e) {
            logger.error("IOException: ", e);
        }

        if (deviceResponse != null) {
            gc100ControlPoint.parseDevices(gc100.getHostname(), deviceResponse);
        }

        gc100ItemUtility = org.openhab.binding.gc100ir.util.GC100ItemUtility.getInstance();
    }

    /**
     * Used to check the connection status with GC100 TCP/IP socket.
     *
     * @return true if connected otherwise false
     */
    public boolean ping() {

        if (!gc100ControlPoint.isGC100ItemConnected(gc100.getHostname())) {
            this.connected = false;
        }
        HashMap<String, GC100ItemBean> items = gc100ItemUtility.getAllItems();
        if (!items.isEmpty()) {
            for (String itemName : items.keySet()) {
                GC100ItemBean itemBean = items.get(itemName);
                if (!gc100ControlPoint.connectTarget(gc100.getHostname(), itemBean.getModule(),
                        itemBean.getConnector())) {
                    logger.warn("Problem in connecting {} GC100 Module: {} of Connector: {}", gc100.getHostname(),
                            itemBean.getModule(), itemBean.getConnector());
                }
            }
        }
        return this.connected;
    }

    /***
     * Check if the connection to the GC100 instance is active
     *
     * @return true if an active connection to the GC100 instance exists, false
     *         otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Attempts to create a connection to the GC100 host.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public void open() throws IOException, InterruptedException, ExecutionException {
        HashMap<String, GC100ItemBean> items = gc100ItemUtility.getAllItems();
        if (!items.isEmpty()) {
            for (String itemName : items.keySet()) {
                GC100ItemBean itemBean = items.get(itemName);
                if (!gc100ControlPoint.connectTarget(gc100.getHostname(), itemBean.getModule(),
                        itemBean.getConnector())) {
                    logger.warn("Problem in connecting host {} with item: {} of module: {} connector: {}",
                            gc100.getHostname(), itemName, itemBean.getModule(), itemBean.getConnector());
                }
            }
        }
        if (gc100ControlPoint.isGC100ItemConnected(gc100.getHostname())) {
            this.connected = true;
        }
    }

    /***
     * Close this connection to the GC100 instance
     */
    public void close() {
        HashMap<String, GC100ItemBean> items = gc100ItemUtility.getAllItems();
        if (!items.isEmpty()) {
            for (String itemName : items.keySet()) {
                GC100ItemBean itemBean = items.get(itemName);
                if (gc100ControlPoint.isGC100ItemConnected(gc100.getHostname())) {
                    gc100ControlPoint.disconnectTarget(gc100.getHostname(), itemBean.getModule(),
                            itemBean.getConnector());
                    logger.warn("Closing connection with item: {} of module: {} connector: {}", itemName,
                            itemBean.getModule(), itemBean.getConnector());
                }
            }
        }
    }

    /**
     * Used to get the GC100IRHost instance.
     *
     * @return instance of GC100IRHost
     */
    public GC100IRHost getGC100() {
        return gc100;
    }

    /**
     * Gets the GC100ItemBean according to itemName.
     *
     * @param itemName
     * @return GC100ItemBean instance.
     */
    public GC100ItemBean getGC100ItemBean(String itemName) {
        return gc100ItemUtility.getAllItems().get(itemName);
    }

    /**
     * Invokes the command over the GC100 IR device according to the GC100ItemBean.
     *
     * @param itemName
     */
    public void invokeCommand(String itemName) {
        GC100ItemBean itemBean = getGC100ItemBean(itemName);
        String preparedCode = gc100ItemUtility.prepareCode(itemBean);
        GC100IRCommand result = gc100ControlPoint.doAction(gc100.getHostname(), itemBean.getModule(),
                itemBean.getConnector(), preparedCode);
        if (GC100IRCommandCode.COMPLETED_SUCCESSFULLY != result.getCommandCode()) {
            logger.warn(
                    "Failed to execute command over GC100: {} with Module: {} of Connector: {} to Item: {}, the response was {}",
                    gc100.getHostname(), itemBean.getModule(), itemBean.getConnector(), itemName, result);
            return;
        }
        logger.info(
                "Successfully executed command over GC100: {} with Module: {} of Connector: {} to Item: {}, the response was {}",
                gc100.getHostname(), itemBean.getModule(), itemBean.getConnector(), itemName, result);
    }
}
