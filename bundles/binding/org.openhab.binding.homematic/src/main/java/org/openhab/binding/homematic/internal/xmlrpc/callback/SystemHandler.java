/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.homematic.internal.xmlrpc.callback;

import java.util.Arrays;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.XmlRpcRequestConfig;
import org.apache.xmlrpc.client.XmlRpcClientRequestImpl;
import org.apache.xmlrpc.metadata.XmlRpcListableHandlerMapping;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;

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

    private final Logger log = Logger.getLogger(getClass().getName());

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

    public int multicall(String interfaceId) throws XmlRpcException {
        log.fine("multicall: " + interfaceId);
        return 0;
    }

    @SuppressWarnings("unchecked")
    public int multicall(Object[] calls) throws XmlRpcException {

        log.fine("multicall: " + Arrays.toString(calls));

        for (Object obj : calls) {
            Map<String, Object> call = (Map<String, Object>) obj;
            String methodname = call.get("methodName").toString();
            Object[] params = (Object[]) call.get("params");

            log.fine("calls to " + methodname + " with params " + Arrays.toString(calls));

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

        log.fine("end of multicall");

        return 0;
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
