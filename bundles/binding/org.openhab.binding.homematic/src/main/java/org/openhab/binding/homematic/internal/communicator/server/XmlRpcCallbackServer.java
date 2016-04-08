/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.server;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackReceiver;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackServer;
import org.openhab.binding.homematic.internal.rpc.XmlRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD;

/**
 * Reads a XML-RPC message and handles the method call.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class XmlRpcCallbackServer implements HomematicCallbackServer {
    private final static Logger logger = LoggerFactory.getLogger(XmlRpcCallbackServer.class);
    private final static boolean TRACE_ENABLED = logger.isTraceEnabled();

    private static final String XML_EMPTY_STRING = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<methodResponse><params><param><value></value></param></params></methodResponse>";
    private static final String XML_EMPTY_ARRAY = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<methodResponse><params><param><value><array><data></data></array></value></param></params></methodResponse>";
    private static final String XML_EMPTY_EVENT_LIST = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<methodResponse><params><param><value><array><data><value>event</value></data></array></value></param></params></methodResponse>";
    private static final String XML_LIST_METHODS_RESPONSE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<methodResponse><params><param><value><array><data><value>event</value><value>system.multicall</value></data></array></value></param></params></methodResponse>";

    private XmlRpcHTTPD xmlRpcHTTPD;
    private HomematicCallbackReceiver callbackReceiver;

    public XmlRpcCallbackServer(HomematicCallbackReceiver callbackReceiver) {
        this.callbackReceiver = callbackReceiver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws Exception {
        HomematicConfig config = HomematicContext.getInstance().getConfig();
        logger.info("Starting {} at port {}", this.getClass().getSimpleName(), config.getCallbackPort());

        xmlRpcHTTPD = new XmlRpcHTTPD(config.getCallbackPort());
        xmlRpcHTTPD.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        logger.debug("Shutting down {}", this.getClass().getSimpleName());
        if (xmlRpcHTTPD != null) {
            xmlRpcHTTPD.stop();
        }
    }

    /**
     * A XML-RPC server based on a tiny HTTP server.
     *
     * @author Gerhard Riegler
     * @since 1.9.0
     */
    private class XmlRpcHTTPD extends NanoHTTPD {

        public XmlRpcHTTPD(int port) {
            super(port);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Response serve(IHTTPSession session) {
            try {
                Map<String, String> request = new HashMap<String, String>();
                session.parseBody(request);

                if (TRACE_ENABLED) {
                    logger.trace("Server original XmlRpcMessage: \n{}", request.get("postData"));
                }

                XmlRpcResponse response = new XmlRpcResponse(
                        new ByteArrayInputStream(request.get("postData").getBytes("ISO-8859-1")));
                if (TRACE_ENABLED) {
                    logger.trace("Server parsed XmlRpcMessage: \n{}", response);
                }
                String returnValue = handleMethodCall(response.getMethodName(), response.getResponseData());
                if (TRACE_ENABLED) {
                    logger.trace("Server response XmlRpcMessage: \n{}", returnValue);
                }
                return newFixedLengthResponse(returnValue);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                return newFixedLengthResponse(XML_EMPTY_STRING);
            }
        }

        /**
         * Returns a valid result of the method called by the Homematic server.
         */
        private String handleMethodCall(String methodName, Object[] responseData) throws Exception {
            if ("event".equals(methodName)) {
                handleEvent(responseData);
                return XML_EMPTY_STRING;
            } else if ("listDevices".equals(methodName) || "deleteDevices".equals(methodName)
                    || "updateDevice".equals(methodName)) {
                return XML_EMPTY_ARRAY;
            } else if ("newDevices".equals(methodName)) {
                callbackReceiver.newDevices(null, null);
                return XML_EMPTY_ARRAY;
            } else if ("system.listMethods".equals(methodName)) {
                return XML_LIST_METHODS_RESPONSE;
            } else if ("system.multicall".equals(methodName)) {
                for (Object o : (Object[]) responseData[0]) {
                    Map<?, ?> call = (Map<?, ?>) o;
                    String method = call.get("methodName").toString();
                    Object[] data = (Object[]) call.get("params");
                    handleMethodCall(method, data);
                }
                return XML_EMPTY_EVENT_LIST;
            } else {
                logger.warn("Unknown method called by Homematic server: " + methodName);
                return XML_EMPTY_EVENT_LIST;
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

}
