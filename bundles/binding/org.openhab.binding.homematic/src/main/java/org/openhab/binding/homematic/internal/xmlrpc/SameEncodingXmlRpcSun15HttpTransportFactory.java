package org.openhab.binding.homematic.internal.xmlrpc;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;

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
