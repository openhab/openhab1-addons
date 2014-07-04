/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.server;

import java.net.InetAddress;

import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackReceiver;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackServer;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackHandler;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server implementation for receiving messages via XML-RPC from the CCU. It's a
 * glue class for the XML-RPC framework developed by Mathias Ewald and Thomas
 * Letsch.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class XmlRpcCallbackServer implements HomematicCallbackServer {
	private final static Logger logger = LoggerFactory.getLogger(XmlRpcCallbackServer.class);

	private HomematicConfig config = HomematicContext.getInstance().getConfig();

	private HomematicCallbackReceiver callbackReceiver;
	private CallbackServer callbackServer;

	public XmlRpcCallbackServer(HomematicCallbackReceiver callbackReceiver) {
		this.callbackReceiver = callbackReceiver;
	}

	@Override
	public void start() throws Exception {
		logger.info("Starting {} at port {}", this.getClass().getSimpleName(), config.getCallbackPort());

		CallbackHandler handler = new CallbackHandler();
		handler.registerCallbackReceiver(callbackReceiver);

		callbackServer = new CallbackServer(InetAddress.getByName(config.getCallbackHost()), config.getCallbackPort(),
				handler);

		callbackServer.start();
	}

	@Override
	public void shutdown() {
		logger.debug("Shutting down {}", this.getClass().getSimpleName());

		if (callbackServer != null) {
			callbackServer.stop();
			callbackServer = null;
		}
	}

}
