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
        ;
        this.address = address;
        URL url = null;
        try {
            url = new URL("http://" + address + ":2001");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed homematic server url: http://" + address + ":2001", e);
        }

        logger.info("Connecting to " + url);

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);

        xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);
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
