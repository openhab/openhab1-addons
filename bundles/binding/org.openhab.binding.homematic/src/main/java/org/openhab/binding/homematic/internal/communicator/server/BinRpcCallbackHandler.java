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
import java.util.Map;

import org.openhab.binding.homematic.internal.binrpc.BinRpcResponse;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackReceiver;
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

	private static final byte BIN_EMPTY_STRING[] = { 'B', 'i', 'n', 1, 0, 0, 0, 8, 0, 0, 0, 3, 0, 0, 0, 0 };
	private static final byte BIN_EMPTY_ARRAY[] = { 'B', 'i', 'n', 1, 0, 0, 0, 8, 0, 0, 1, 0, 0, 0, 0, 0 };
	private static final byte BIN_EMPTY_EVENT_LIST[] = { 'B', 'i', 'n', 1, 0, 0, 0, 21, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0,
			0, 3, 0, 0, 0, 5, 'e', 'v', 'e', 'n', 't' };
	private static final byte BIN_LIST_METHODS_RESPONSE[] = { 'B', 'i', 'n', 1, 0, 0, 0, 45, 0, 0, 1, 0, 0, 0, 0, 2, 0, 0, 0, 3, 0,
			0, 0, 16, 's', 'y', 's', 't', 'e', 'm', '.', 'm', 'u', 'l', 't', 'i', 'c', 'a', 'l', 'l', 0, 0, 0, 3, 0, 0,
			0, 5, 'e', 'v', 'e', 'n', 't' };

	private Socket socket;
	private HomematicCallbackReceiver callbackReceiver;

	public BinRpcCallbackHandler(Socket socket, HomematicCallbackReceiver callbackReceiver) {
		this.socket = socket;
		this.callbackReceiver = callbackReceiver;
	}

	/**
	 * Reads the event from the Homematic server and handles the method call.
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
	 * Returns a valid result of the method called by the Homematic server.
	 */
	private byte[] handleMethodCall(String methodName, Object[] responseData) throws Exception {
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
			return BIN_LIST_METHODS_RESPONSE;
		} else if ("system.multicall".equals(methodName)) {
			for (Object o : (Object[]) responseData[0]) {
				Map<?, ?> call = (Map<?, ?>) o;
				String method = call.get("methodName").toString();
				Object[] data = (Object[]) call.get("params");
				handleMethodCall(method, data);
			}
			return BIN_EMPTY_EVENT_LIST;
		} else {
			logger.warn("Unknown method called by Homematic server: " + methodName);
			return BIN_EMPTY_EVENT_LIST;
		}
	}

	/**
	 * Populates the extracted event to the callbackReceiver.
	 */
	private void handleEvent(Object[] parms) {
		String interfaceId = parms[0].toString();
		String address = parms[1].toString();
		String attribute = parms[2].toString();
		Object value = parms[3];

		callbackReceiver.event(interfaceId, address, attribute, value);
	}

}
