/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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

import org.openhab.binding.homematic.internal.binrpc.BinRpcRequest;
import org.openhab.binding.homematic.internal.binrpc.BinRpcResponse;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmRssiInfo;
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
	private final static boolean TRACE_ENABLED = logger.isTraceEnabled();

	private HomematicContext context = HomematicContext.getInstance();

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
	 * {@inheritDoc}
	 */
	@Override
	public void init(HmInterface hmInterface) throws HomematicClientException {
		BinRpcRequest request = new BinRpcRequest("init");
		request.addArg(context.getConfig().getBinRpcCallbackUrl());
		request.addArg(hmInterface.toString());
		sendMessage(hmInterface, request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void release(HmInterface hmInterface) throws HomematicClientException {
		BinRpcRequest request = new BinRpcRequest("init");
		request.addArg(context.getConfig().getBinRpcCallbackUrl());
		sendMessage(hmInterface, request);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getAllValues(HmInterface hmInterface) throws HomematicClientException {
		BinRpcRequest request = new BinRpcRequest("getAllValues");
		request.addArg(Boolean.TRUE);
		return (Object[]) sendMessage(hmInterface, request)[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getAllSystemVariables(HmInterface hmInterface) throws HomematicClientException {
		BinRpcRequest request = new BinRpcRequest("getAllSystemVariables");
		return (Map<String, ?>) sendMessage(hmInterface, request)[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getDeviceDescription(HmInterface hmInterface, String address)
			throws HomematicClientException {
		BinRpcRequest request = new BinRpcRequest("getDeviceDescription");
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
		BinRpcRequest request = new BinRpcRequest("rssiInfo");
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
	public void setDatapointValue(HmInterface hmInterface, String address, String datapointName, Object value)
			throws HomematicClientException {
		BinRpcRequest request = new BinRpcRequest("setValue");
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
		BinRpcRequest request = new BinRpcRequest("setSystemVariable");
		request.addArg(name);
		request.addArg(value);
		sendMessage(hmInterface, request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeProgram(HmInterface hmInterface, String programName) throws HomematicClientException {
		BinRpcRequest request = new BinRpcRequest("runScript");
		request.addArg(programName);
		sendMessage(hmInterface, request);
	}

	/**
	 * Sends a BIN-RPC message and parses the response to see if there was an
	 * error.
	 */
	private synchronized Object[] sendMessage(HmInterface hmInterface, BinRpcRequest request)
			throws HomematicClientException {
		Socket socket = null;
		try {
			if (TRACE_ENABLED) {
				logger.trace("Client BinRpcRequest {}", request);
			}
			socket = new Socket(context.getConfig().getHost(), hmInterface.getPort());
			socket.setSoTimeout(5000);
			socket.getOutputStream().write(request.createMessage());
			BinRpcResponse resp = new BinRpcResponse(socket.getInputStream(), false);

			if (TRACE_ENABLED) {
				logger.trace("Client BinRpcResponse: {}", resp.toString());
			}
			Object[] data = resp.getResponseData();
			if (data != null && data.length > 0) {
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

}
