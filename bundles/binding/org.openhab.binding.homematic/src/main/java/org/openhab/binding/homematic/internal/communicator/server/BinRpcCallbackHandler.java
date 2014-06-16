/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openhab.binding.homematic.internal.binrpc.BinRpcRequest;
import org.openhab.binding.homematic.internal.binrpc.BinRpcResponse;
import org.openhab.binding.homematic.internal.communicator.CcuCallbackReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads a BIN-RPC message from the socket and handles the method call.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BinRpcCallbackHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BinRpcCallbackHandler.class);
	private final static boolean TRACE_ENABLED = logger.isTraceEnabled();

	private static final byte BIN_EMPTY_STRING[] = { 'B', 'i', 'n', 0, 0, 0, 0, 8, 0, 0, 0, 3, 0, 0, 0, 0 };
	private static final byte BIN_EMPTY_ARRAY[] = { 'B', 'i', 'n', 0, 0, 0, 0, 8, 0, 0, 1, 0, 0, 0, 0, 0 };

	private Socket socket;
	private CcuCallbackReceiver callbackReceiver;

	public BinRpcCallbackHandler(Socket socket, CcuCallbackReceiver callbackReceiver) {
		this.socket = socket;
		this.callbackReceiver = callbackReceiver;
	}

	/**
	 * Reads the event from the CCU and handles the method call.
	 */
	@Override
	public void run() {
		try {
			BinRpcResponse response = new BinRpcResponse(socket.getInputStream(), true);
			if (TRACE_ENABLED) {
				logger.trace("Event BinRpcResponse: {}", response.toString());
			}
			byte[] returnValue = handleMethodCall(response.getMethodName(), response.getResponseData());
			if (returnValue != null) {
				socket.getOutputStream().write(returnValue);
			}
		} catch (EOFException eof) {
			// ignore
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				socket.close();
			} catch (IOException ex) {
				// ignore
			}
		}
	}

	/**
	 * Returns a valid result of the method called by the CCU.
	 */
	private byte[] handleMethodCall(String methodName, List<?> responseData) throws Exception {
		if ("event".equals(methodName)) {
			handleEvent(responseData);
			return BIN_EMPTY_STRING;
		} else if ("listDevices".equals(methodName)) {
			return BIN_EMPTY_ARRAY;
		} else if ("deleteDevices".equals(methodName)) {
			return BIN_EMPTY_ARRAY;
		} else if ("newDevices".equals(methodName)) {
			callbackReceiver.newDevices(null, null);
			return BIN_EMPTY_ARRAY;
		} else if ("system.listMethods".equals(methodName)) {
			return createEmptyMessage();
		} else if ("system.multicall".equals(methodName)) {
			for (Object o : (List<?>) responseData.get(0)) {
				Map<?, ?> call = (Map<?, ?>) o;
				String method = call.get("methodName").toString();
				List<?> data = (List<?>) call.get("params");
				handleMethodCall(method, data);
			}
			return createEmptyMessage();
		} else {
			logger.warn("Unknown method called by CCU: " + methodName);
			return createEmptyMessage();
		}
	}

	/**
	 * Creates an empty message for sending back to the CCU.
	 */
	private byte[] createEmptyMessage() {
		BinRpcRequest request = new BinRpcRequest(null);
		List<Object> result = new ArrayList<Object>();
		result.add("event");
		request.addArg(result);
		return request.createMessage();
	}

	/**
	 * Populates the extracted event to the callbackReceiver.
	 */
	private void handleEvent(List<?> parms) {
		String interfaceId = parms.get(0).toString();
		String address = parms.get(1).toString();
		String attribute = parms.get(2).toString();
		Object value = parms.get(3);

		callbackReceiver.event(interfaceId, address, attribute, value);
	}

}
