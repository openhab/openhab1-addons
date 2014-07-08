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
import java.util.List;
import java.util.Map;

import org.openhab.binding.homematic.internal.binrpc.BinRpcRequest;
import org.openhab.binding.homematic.internal.binrpc.BinRpcResponse;
import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.CcuClient;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client implementation for sending messages via binrpc to the CCU.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BinRpcClient implements CcuClient {
	private final static Logger logger = LoggerFactory.getLogger(BinRpcClient.class);
	private final static boolean TRACE_ENABLED = logger.isTraceEnabled();

	private HomematicConfig config = HomematicContext.getInstance().getConfig();

	public BinRpcClient() {
		logger.info("Starting {}", this.getClass().getSimpleName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(HmInterface hmInterface) throws CcuClientException {
		BinRpcRequest request = new BinRpcRequest("init");
		request.addArg(config.getBinRpcCallbackUrl());
		request.addArg(hmInterface.toString());
		sendMessage(hmInterface, request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void release(HmInterface hmInterface) throws CcuClientException {
		BinRpcRequest request = new BinRpcRequest("init");
		request.addArg(config.getBinRpcCallbackUrl());
		sendMessage(hmInterface, request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDatapointValue(HmDatapoint dp, String datapointName, Object value) throws CcuClientException {
		HmInterface hmInterface = dp.getChannel().getDevice().getHmInterface();
		String address = dp.getChannel().getAddress();
		if (dp.isIntegerValue() && value instanceof Double) {
			value = ((Number) value).intValue();
		}

		BinRpcRequest request = new BinRpcRequest("setValue");
		request.addArg(address);
		request.addArg(datapointName);
		request.addArg(value);
		sendMessage(hmInterface, request);
	}

	/**
	 * Sends a BIN-RPC message and parses the response to see if there was an
	 * error.
	 */
	private synchronized void sendMessage(HmInterface hmInterface, BinRpcRequest request) throws CcuClientException {
		Socket socket = null;
		try {
			if (TRACE_ENABLED) {
				logger.trace("Client BinRpcRequest {}", request);
			}
			socket = new Socket(config.getHost(), hmInterface.getPort());
			socket.setSoTimeout(5000);
			socket.getOutputStream().write(request.createMessage());
			BinRpcResponse resp = new BinRpcResponse(socket.getInputStream(), false);

			if (TRACE_ENABLED) {
				logger.trace("Client BinRpcResponse: {}", resp.toString());
			}
			List<Object> data = resp.getResponseData();
			if (data.size() > 0) {
				Object response = data.get(0);
				if (response instanceof String) {
					if (!"".equals((String) response)) {
						throw new IOException("Unknown Result: " + response);
					}
				} else if (response instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) response;
					Object faultCode = map.get("faultCode");
					Object faultString = map.get("faultString");
					throw new IOException(faultCode + " " + faultString);
				}
			}
		} catch (ConnectException cex) {
			logger.info("Can't connect to interface {}", hmInterface);
		} catch (Exception ex) {
			throw new CcuClientException(ex.getMessage() + "(sending " + request + ")", ex);
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
