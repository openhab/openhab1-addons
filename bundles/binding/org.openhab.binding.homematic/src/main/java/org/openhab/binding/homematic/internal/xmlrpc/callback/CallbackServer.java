/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.callback;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.common.TypeConverterFactoryImpl;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.openhab.binding.homematic.internal.xmlrpc.DefaultHandlerHandlerMapping;
import org.openhab.binding.homematic.internal.xmlrpc.HomematicBindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CallbackServer opens a listening port on the local machine and waits for
 * XML-RPC requests to arrive. It expects XML-RPC requests from a CCU but
 * therefore the CCU#init() method must be called to register this server at the
 * CCU. Otherwise the CCU will not send requests.
 * 
 * The class requires a CallbackHandler object in the constructor. All method
 * calls from the CCU will be directed to this handler except for system.* calls
 * - they are handled by an instance of SystemHandler. The CallbackHandler
 * itself only dispatches calls to registered instances of CallbackReceiver.
 * This way you can register multiple CallbackReceivers to be notified about
 * certain events.
 * 
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class CallbackServer {

    private static final Logger logger = LoggerFactory.getLogger(CallbackServer.class);

    private WebServer webServer;

    public CallbackServer(InetAddress inetAddress, Integer port, CallbackHandler handler) {
        if (port == null) {
            throw new IllegalArgumentException("port must no be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler must no be null");
        }

        logger.debug("CallbackServer will listen on port " + port);

        /*
         * setup handlers: we need a single instance of CallbackHandler to be
         * used for callbacks, so we must set a custom
         * RequestProcessorFactoryFactory. Further, a handler for system method
         * calls (listMethods, methodHelp, multicall, etc must be registered.
         */

        DefaultHandlerHandlerMapping phm = new DefaultHandlerHandlerMapping("CallbackHandler");
        phm.setRequestProcessorFactoryFactory(new CallbackHandlerProcessorFactoryFactory(handler));
        phm.setVoidMethodEnabled(false);
        try {
            phm.addHandler("CallbackHandler", CallbackHandler.class);
            SystemHandler.addSystemHandler(phm);
        } catch (XmlRpcException e) {
            throw new HomematicBindingException(e);
        }

        /*
         * configure and start server
         */
        if (inetAddress != null) {
            webServer = new TimeoutWebServer(port, inetAddress);
        } else {
            webServer = new TimeoutWebServer(port);
        }

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
        xmlRpcServer.setHandlerMapping(phm);
        xmlRpcServer.setTypeConverterFactory(new TypeConverterFactoryImpl());

        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
        serverConfig.setEncoding("ISO-8859-1");

    }

    public void start() {
        logger.debug("Starting webserver ...");
        try {
            webServer.start();
        } catch (IOException e) {
            throw new HomematicBindingException(e);
        }
    }

    public void stop() {
        logger.debug("Stopping webserver ...");
        webServer.shutdown();
    }

    public class CallbackHandlerProcessorFactoryFactory implements RequestProcessorFactoryFactory {

        private final RequestProcessorFactory factory = new CallbackHandlerProcessorFactory();
        private final CallbackHandler handler;

        public CallbackHandlerProcessorFactoryFactory(CallbackHandler handler) {
            if (handler == null) {
                throw new IllegalArgumentException("handler must not be null");
            }
            this.handler = handler;
        }

        @Override
        public RequestProcessorFactory getRequestProcessorFactory(@SuppressWarnings("rawtypes") Class aClass) {
            return factory;
        }

        private class CallbackHandlerProcessorFactory implements RequestProcessorFactory {

            @Override
            public Object getRequestProcessor(XmlRpcRequest xmlRpcRequest) {
                return handler;
            }

        }
    }

}
