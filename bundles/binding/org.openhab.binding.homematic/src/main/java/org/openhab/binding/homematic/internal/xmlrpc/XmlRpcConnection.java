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

import org.apache.xmlrpc.client.TimingOutCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Homematic CCU consists of three XML-RPC interfaces (rf, wired and system)
 * with different sets of implemented methods. A large set of methods is common
 * to call three interfaces. This class implements this common set of methods.
 * Extending classes have to provide the getXmlRpcClient() method. The returned
 * XmlRpcClient object is used by the methods below.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public abstract class XmlRpcConnection {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected abstract XmlRpcClient getXmlRpcClient();

    public abstract String getAddress();

    public abstract Integer getPort();

    protected XmlRpcClient createXmlRpcClient() {
        if (getAddress() == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        URL url = null;
        try {
            url = new URL("http://" + getAddress() + ":" + getPort());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed homematic server url: http://" + getAddress() + ":" + getPort(), e);
        }

        log.debug("Connecting to " + url);
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);
        config.setConnectionTimeout(3000);
        config.setReplyTimeout(3000);
        config.setEncoding("ISO-8859-1");

        XmlRpcClient xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);
        xmlRpcClient.setTransportFactory(new SameEncodingXmlRpcSun15HttpTransportFactory(xmlRpcClient));
        return xmlRpcClient;
    }
    

    /**
     * Calls the CCU to initialize the XML-RPC callback connection.
     * 
     * @param url
     *            callback URL
     * @param interfaceId
     *            unique interface id
     * 
     * @see #release(String)
     */
    public void init(String url, String interfaceId) {
        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        ;
        if (interfaceId == null) {
            throw new IllegalArgumentException("interfaceId must not be null");
        }

        log.debug("called init: " + url + ", " + interfaceId);
        Object[] params = { url, interfaceId };
        executeRPC("init", params);
    }

    /**
     * Calls the CCU to release the XML-RPC callback connection.
     * 
     * @param interfaceId
     *            interface id used to establish the connection
     * 
     * @see #init(String, String)
     */
    public void release(String interfaceId) {

        init("", interfaceId);
    }

    public void setValue(String address, String valueKey, Object value) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        if (valueKey == null) {
            throw new IllegalArgumentException("valueKey must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        log.debug("called setValue: " + address + ", " + valueKey + ", " + value);
        Object[] params = { address, valueKey, value };
        executeRPC("setValue", params);
    }

    protected Object executeRPC(String methodName, Object[] params) {
        try {
            TimingOutCallback callback = new TimingOutCallback(5 * 1000);
            getXmlRpcClient().executeAsync(methodName, params, callback);
            return callback.waitForResponse();
        } catch (Exception e) {
            throw new HomematicBindingException(e);
        } catch (Throwable e) {
            log.error("Throwable catched", e);
            throw new HomematicBindingException("Throwable catched");
        }
    }
}