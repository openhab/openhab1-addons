/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
 * 
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
