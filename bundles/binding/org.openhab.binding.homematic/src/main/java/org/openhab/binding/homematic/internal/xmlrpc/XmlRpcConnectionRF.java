/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class adds RF specific methods of the CCU interface. It connects to port
 * 2001 of the given address.
 * 
 * @see XmlRpcConnection
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class XmlRpcConnectionRF extends XmlRpcConnection {

    private static final Logger logger = LoggerFactory.getLogger(XmlRpcConnectionRF.class);

    private final String address;
    private XmlRpcClient xmlRpcClient;

    public XmlRpcConnectionRF(String address) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        this.address = address;
        URL url = null;
        try {
            url = new URL("http://" + address + ":2001");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed homematic server url: http://" + address + ":2001", e);
        }

        logger.debug("Connecting to " + url);
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);
        config.setConnectionTimeout(3000);
        config.setReplyTimeout(3000);
        config.setEncoding("ISO-8859-1");

        xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);
        xmlRpcClient.setTransportFactory(new SameEncodingXmlRpcSun15HttpTransportFactory(xmlRpcClient));
    }

    @Override
    protected XmlRpcClient getXmlRpcClient() {
        return xmlRpcClient;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Integer getPort() {
        return 2001;
    }

    public void activateLinkParamset() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void addDevice() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void changekey() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void deleteDevice() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void determineParameter() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public Integer getInstallMode() throws NumberFormatException {
        logger.trace("called getInstallMode");
        Object[] params = {};
        return Integer.parseInt(executeRPC("getInstallMode", params).toString());
    }

    public void getKeyMismatchDevice() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void getLinkInfo() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void getLinkPeers() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void listTeams() {
        throw new RuntimeException("not yet implemented");
    }

    public void reportValueUsage() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void restoreConfigToDevice() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void rssiInfo() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void setInstallMode(Boolean on) {
        logger.warn("called setInstallMode: " + on);
        Object[] params = { on };
        executeRPC("setInstallMode", params);
    }

    public void setLinkInfo() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void setTeam() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void setTempKey() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void listBidcosInterfaces() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void setBidcosInterfaces() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    public void getServiceMessages() {

        Object[] params = {};
        Object[] value = (Object[]) executeRPC("getServiceMessages", params);

        for (Object o : value) {
            Object[] values = (Object[]) o;
            for (Object entry : values) {
                System.out.println(entry);
            }
            System.out.println("");
        }

    }

    public Object getMetdata(String objectId, String dataId) {

        Object[] params = { objectId, dataId };
        Object value = executeRPC("getMetadata", params);

        return value;
    }

    public void setMetadata() {
        logger.warn("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

}
