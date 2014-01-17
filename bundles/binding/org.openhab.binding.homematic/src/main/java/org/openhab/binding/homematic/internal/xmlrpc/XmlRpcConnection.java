/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.client.TimingOutCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;
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

    public void addLink(String sender, String receiver, String name, String description) {
        log.warn("called unimplemented method");
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void clearConfigCache() {
        log.warn("called unimplemented method");
        throw new UnsupportedOperationException("not yet implemented");
    }

    @SuppressWarnings("unchecked")
    public DeviceDescription getDeviceDescription(String address) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }

        log.debug("called getDeviceDescription: " + address);

        Object[] params = { address };
        Object result = executeRPC("getDeviceDescription", params);

        return new DeviceDescription((Map<String, Object>) result);
    }

    public Set<Object> getLinks(String address, Integer flags) {
        log.debug("called unimplemented method");
        throw new RuntimeException("not yet implemented");
    }

    @SuppressWarnings("unchecked")
    public Paramset getParamset(String address, String paramsetType) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        if (paramsetType == null) {
            throw new IllegalArgumentException("paramsetType must not be null");
        }

        log.info("called getParamset: {}, {}", address, paramsetType);

        Object[] params = { address, paramsetType };
        Object result = executeRPC("getParamset", params);
        return new Paramset((Map<String, Object>) result);
    }

    @SuppressWarnings("unchecked")
    public ParamsetDescription getParamsetDescription(String address, String paramsetType) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        if (paramsetType == null) {
            throw new IllegalArgumentException("paramsetType must not be null");
        }

        log.debug("called getParamsetDescription: {}, {}", address, paramsetType);

        Object[] params = { address, paramsetType };
        Object result = executeRPC("getParamsetDescription", params);
        return new ParamsetDescription((Map<String, Object>) result);
    }

    public String getParamsetId(String address, String paramsetType) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        if (paramsetType == null) {
            throw new IllegalArgumentException("paramsetType must not be null");
        }

        log.debug("called getParamsetId: " + address + ", " + paramsetType);
        Object[] params = { address, paramsetType };
        return executeRPC("getParamsetId", params).toString();
    }

    public Object getValue(String address, String valueKey) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        if (valueKey == null) {
            throw new IllegalArgumentException("valueKey must not be null");
        }

        log.debug("called getValue: " + address + ", " + valueKey);
        Object[] params = { address, valueKey };
        return executeRPC("getValue", params);
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

    @SuppressWarnings("unchecked")
    public Set<DeviceDescription> listDevices() {
        log.debug("called listDevices");

        Object[] params = {};
        Object[] result = (Object[]) executeRPC("listDevices", params);

        Set<DeviceDescription> deviceDescriptions = new HashSet<DeviceDescription>();
        for (Object obj : result) {
            Map<String, Object> map = (Map<String, Object>) obj;
            deviceDescriptions.add(new DeviceDescription(map));
        }

        return deviceDescriptions;
    }

    public Integer logLevel() throws NumberFormatException {
        log.debug("called logLevel");
        Object[] params = {};
        return Integer.parseInt(executeRPC("logLevel", params).toString());
    }

    public Integer logLevel(Integer logLevel) throws NumberFormatException {
        if (logLevel == null) {
            throw new IllegalArgumentException("logLevel must not be null");
        }

        log.debug("called logLevel: " + logLevel);
        Object[] params = { logLevel };
        return Integer.parseInt(executeRPC("logLevel", params).toString());
    }

    public void putParamset(String address, String paramsetType, Paramset paramset) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }
        if (paramsetType == null) {
            throw new IllegalArgumentException("paramsetType must not be null");
        }

        log.debug("called putParamset: " + address + ", " + paramsetType + ", " + paramset);
        Object[] params = { address, paramsetType, paramset.getValues() };
        executeRPC("putParamset", params);
    }

    public void removeLink(String sender, String receiver) {
        throw new UnsupportedOperationException("not yet implemented");
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