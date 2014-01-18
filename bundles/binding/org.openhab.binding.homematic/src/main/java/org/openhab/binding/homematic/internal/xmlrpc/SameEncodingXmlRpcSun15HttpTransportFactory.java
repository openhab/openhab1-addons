/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;

/**
 * The TransportFactory for the SameEncodingXmlRpcSun15HttpTransport.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class SameEncodingXmlRpcSun15HttpTransportFactory extends XmlRpcSun15HttpTransportFactory {

    public SameEncodingXmlRpcSun15HttpTransportFactory(XmlRpcClient pClient) {
        super(pClient);
    }

    @Override
    public XmlRpcTransport getTransport() {
        SameEncodingXmlRpcSun15HttpTransport transport = new SameEncodingXmlRpcSun15HttpTransport(getClient());
        transport.setSSLSocketFactory(getSSLSocketFactory());
        return transport;
    }

}
