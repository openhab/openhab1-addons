/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.callback;

import java.util.Arrays;
import java.util.Map;
import java.util.TimeZone;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.XmlRpcRequestConfig;
import org.apache.xmlrpc.client.XmlRpcClientRequestImpl;
import org.apache.xmlrpc.metadata.XmlRpcListableHandlerMapping;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This custom implementation handles system.* requests. There is a standard
 * system.* handler provided by Apache XML-RPC but it does not implement
 * system.multicall. This method is required for communication with a Homematic
 * CCU, so I had to implement it manually here.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class SystemHandler {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private XmlRpcListableHandlerMapping mapping;

    public SystemHandler(XmlRpcListableHandlerMapping pMapping) {
        this.mapping = pMapping;
    }

    public String[][] methodSignature(String methodName) throws XmlRpcException {
        return mapping.getMethodSignature(methodName);
    }

    public String methodHelp(String methodName) throws XmlRpcException {
        return mapping.getMethodHelp(methodName);
    }

    public String[] listMethods() throws XmlRpcException {
        return mapping.getListMethods();
    }

    public String[] listMethods(String param) throws XmlRpcException {
        return mapping.getListMethods();
    }

    public Object[] multicall(String interfaceId) throws XmlRpcException {
        log.debug("multicall: {}", interfaceId);
        return new Object[] {};
    }

    @SuppressWarnings("unchecked")
    public Object[] multicall(Object[] calls) throws XmlRpcException {

        log.debug("multicall: " + Arrays.toString(calls));

        for (Object obj : calls) {
            Map<String, Object> call = (Map<String, Object>) obj;
            String methodname = call.get("methodName").toString();
            Object[] params = (Object[]) call.get("params");

            log.debug("calls to {} with params {}", methodname, Arrays.toString(calls));

            XmlRpcRequest req = new XmlRpcClientRequestImpl(new XmlRpcRequestConfig() {

                @Override
                public TimeZone getTimeZone() {
                    return null;
                }

                @Override
                public boolean isEnabledForExtensions() {
                    return false;
                }

            }, methodname, params);

            XmlRpcHandler handler = mapping.getHandler(methodname);
            handler.execute(req);
        }

        log.debug("end of multicall");

        return new Object[] {};
    }

    public static void addSystemHandler(final PropertyHandlerMapping pMapping) throws XmlRpcException {

        final RequestProcessorFactoryFactory factory = pMapping.getRequestProcessorFactoryFactory();
        final SystemHandler systemHandler = new SystemHandler(pMapping);

        pMapping.setRequestProcessorFactoryFactory(

        new RequestProcessorFactoryFactory() {

            @Override
            public RequestProcessorFactory getRequestProcessorFactory(@SuppressWarnings("rawtypes") Class pClass) throws XmlRpcException {

                if (SystemHandler.class.equals(pClass)) {

                    return new RequestProcessorFactory() {
                        @Override
                        public Object getRequestProcessor(XmlRpcRequest request) throws XmlRpcException {
                            return systemHandler;
                        }
                    };

                } else {
                    return factory.getRequestProcessorFactory(pClass);

                }
            }

        }

        );

        pMapping.addHandler("system", SystemHandler.class);
    }

}
