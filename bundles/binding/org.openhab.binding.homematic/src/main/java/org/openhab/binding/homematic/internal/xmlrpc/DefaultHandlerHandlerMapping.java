/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;

/**
 * The Apache XML-RPC API has no support for a default handler. A default
 * handler is a handler that accepts calls without a specific handler mentioned.
 * The Homematic CCU makes calls with method names like "event" or
 * "updateDevice" where Apache XML-RPC would expect e.g "Handler1.event" or
 * "Handler1.updateDevice". The class simply extends PropertyHandlerMapping and
 * overwrites getHandler(String pHandlerName). It checks the super.getHandler()
 * method for a successful handler lookup and prepends the name of a given
 * default handler on failure.
 * 
 * Example: A call to the method "event" arrives. This method will check
 * {@link PropertyHandlerMapping#getHandler(String)}for a handler. If this
 * lookup fails the name of the default handler is prepended. If the default
 * handler name is "Handler1", the method will ask the same super method again
 * for a handler for "Handler1.event".
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class DefaultHandlerHandlerMapping extends PropertyHandlerMapping {

    private final Logger log = Logger.getLogger(getClass().getName());

    private String defaultHandlerName;

    public DefaultHandlerHandlerMapping(String defaultHandlerName) {
        if (defaultHandlerName == null) {
            throw new IllegalArgumentException("defaultHandlerName must not be null");
        }
        this.defaultHandlerName = defaultHandlerName;
    }

    @Override
    public XmlRpcHandler getHandler(String pHandlerName) throws XmlRpcNoSuchHandlerException, XmlRpcException {
        if (pHandlerName == null) {
            throw new IllegalArgumentException("pHandlerName must not be null");
        }

        log.fine("somebody asked for a handler for " + pHandlerName);

        XmlRpcHandler result = null;

        try {
            result = super.getHandler(pHandlerName);
        } catch (Exception ex) {
            // ignore
        }

        log.fine("super class methods returned " + result);

        if (result == null) {
            result = super.getHandler(defaultHandlerName + "." + pHandlerName);
            log.fine("asking super class method for CallbackHandler." + pHandlerName);
            log.fine("result was " + result);
        }

        if (result == null) {
            throw new XmlRpcNoSuchHandlerException("No such handler: " + pHandlerName);
        }

        return result;
    }

}
