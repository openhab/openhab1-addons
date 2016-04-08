/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmRssiInfo;
import org.openhab.binding.homematic.internal.rpc.BinRpcRequest;
import org.openhab.binding.homematic.internal.rpc.BinRpcResponse;
import org.openhab.binding.homematic.internal.rpc.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client implementation for sending messages via BIN-RPC to the Homematic
 * server.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BinRpcClient implements RpcClient {
    private final static Logger logger = LoggerFactory.getLogger(BinRpcClient.class);
    protected final static boolean TRACE_ENABLED = logger.isTraceEnabled();

    protected HomematicContext context = HomematicContext.getInstance();

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws HomematicClientException {
        logger.debug("Starting {}", this.getClass().getSimpleName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() throws HomematicClientException {
        // nothing todo
    }

    /**
     * Returns a RpcRequest for this client.
     */
    protected RpcRequest createRpcRequest(String methodName) {
        return new BinRpcRequest(methodName);
    }

    /**
     * Returns the BIN-RPC url.
     */
    protected String getRpcCallbackUrl() {
        return "binary://" + context.getConfig().getCallbackHost() + ":" + context.getConfig().getCallbackPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(HmInterface hmInterface) throws HomematicClientException {
        RpcRequest request = createRpcRequest("init");
        request.addArg(getRpcCallbackUrl());
        request.addArg(hmInterface.toString());
        sendMessage(hmInterface, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(HmInterface hmInterface) throws HomematicClientException {
        RpcRequest request = createRpcRequest("init");
        request.addArg(getRpcCallbackUrl());
        sendMessage(hmInterface, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getAllValues(HmInterface hmInterface) throws HomematicClientException {
        RpcRequest request = createRpcRequest("getAllValues");
        request.addArg(Boolean.TRUE);
        return (Object[]) sendMessage(hmInterface, request)[0];
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, ?> getAllSystemVariables(HmInterface hmInterface) throws HomematicClientException {
        RpcRequest request = createRpcRequest("getAllSystemVariables");
        return (Map<String, ?>) sendMessage(hmInterface, request)[0];
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getDeviceDescription(HmInterface hmInterface, String address)
            throws HomematicClientException {
        RpcRequest request = createRpcRequest("getDeviceDescription");
        request.addArg(address);
        Object[] result = sendMessage(hmInterface, request);
        if (result != null && result.length > 0 && result[0] instanceof Map) {
            return (Map<String, String>) result[0];
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServerId getServerId(HmInterface hmInterface) throws HomematicClientException {
        Map<String, String> deviceDescription = getDeviceDescription(hmInterface, "BidCoS-RF");
        ServerId serverId = new ServerId(deviceDescription.get("TYPE"));
        serverId.setVersion(deviceDescription.get("FIRMWARE"));
        serverId.setAddress(deviceDescription.get("INTERFACE"));
        return serverId;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, HmRssiInfo> getRssiInfo(HmInterface hmInterface) throws HomematicClientException {
        RpcRequest request = createRpcRequest("rssiInfo");
        Map<String, HmRssiInfo> rssiList = new HashMap<String, HmRssiInfo>();
        Object[] result = sendMessage(hmInterface, request);
        if (result != null && result.length > 0 && result[0] instanceof Map) {
            Map<String, ?> devices = (Map<String, ?>) result[0];
            for (String sourceDevice : devices.keySet()) {
                Map<String, Object[]> targetDevices = (Map<String, Object[]>) devices.get(sourceDevice);
                for (String targetDevice : targetDevices.keySet()) {
                    if (targetDevice.equals(context.getServerId().getAddress())) {
                        Integer rssiDevice = (Integer) targetDevices.get(targetDevice)[0];
                        Integer rssiPeer = (Integer) targetDevices.get(targetDevice)[1];
                        HmRssiInfo rssiInfo = new HmRssiInfo(sourceDevice, rssiDevice, rssiPeer);
                        rssiList.put(rssiInfo.getAddress(), rssiInfo);
                    }
                }
            }
        }
        return rssiList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatapointValue(HmInterface hmInterface, String address, String datapointName, Object value)
            throws HomematicClientException {
        RpcRequest request = createRpcRequest("setValue");
        request.addArg(address);
        request.addArg(datapointName);
        request.addArg(value);
        sendMessage(hmInterface, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSystemVariable(HmInterface hmInterface, String name, Object value) throws HomematicClientException {
        RpcRequest request = createRpcRequest("setSystemVariable");
        request.addArg(name);
        request.addArg(value);
        sendMessage(hmInterface, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeProgram(HmInterface hmInterface, String programName) throws HomematicClientException {
        RpcRequest request = createRpcRequest("runScript");
        request.addArg(programName);
        sendMessage(hmInterface, request);
    }

    /**
     * Sends a BIN-RPC message and parses the response to see if there was an
     * error.
     */
    protected synchronized Object[] sendMessage(HmInterface hmInterface, RpcRequest request)
            throws HomematicClientException {
        Socket socket = null;
        try {
            if (TRACE_ENABLED) {
                logger.trace("Client BinRpcRequest:  {}", request);
            }
            socket = new Socket(context.getConfig().getHost(), hmInterface.getPort());
            socket.setSoTimeout(context.getConfig().getTimeout() * 1000);
            socket.getOutputStream().write(request.createMessage());
            BinRpcResponse resp = new BinRpcResponse(socket.getInputStream(), false);

            if (TRACE_ENABLED) {
                logger.trace("Client BinRpcResponse: {}", resp.toString());
            }
            Object[] data = resp.getResponseData();
            if (data != null && data.length > 0) {
                checkIfFault(data);
                return data;
            }
            throw new IOException("Unknown Result: " + data);
        } catch (ConnectException cex) {
            if (HmInterface.WIRED == hmInterface || HmInterface.CUXD == hmInterface) {
                logger.info("Interface {} not available, disabling support.", hmInterface);
                return null;
            }
            throw new HomematicClientException("Can't connect to interface " + hmInterface + ": " + cex.getMessage(),
                    cex);
        } catch (Exception ex) {
            throw new HomematicClientException(ex.getMessage() + " (sending " + request + ")", ex);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    /**
     * Checks if the response data contains a fault.
     */
    protected void checkIfFault(Object[] data) throws IOException {
        Object responseData = data[0];
        if (responseData instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) responseData;
            if (map.containsKey("faultCode")) {
                Object faultCode = map.get("faultCode");
                Object faultString = map.get("faultString");
                throw new IOException(faultCode + " " + faultString);
            }
        }
    }
}
