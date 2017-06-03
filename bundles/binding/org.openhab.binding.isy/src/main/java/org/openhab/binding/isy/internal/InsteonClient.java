/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nanoxml.XMLElement;
import com.udi.isy.jsdk.insteon.ISYInsteonClient;
import com.universaldevices.client.NoDeviceException;
import com.universaldevices.common.properties.UDProperty;
import com.universaldevices.device.model.UDAction;
import com.universaldevices.device.model.UDControl;
import com.universaldevices.device.model.UDFolder;
import com.universaldevices.device.model.UDGroup;
import com.universaldevices.device.model.UDNode;
import com.universaldevices.security.upnp.UPnPSecurity;
import com.universaldevices.upnp.UDProxyDevice;

/**
 * ISY SDK Insteon client implementation
 * 
 * @author Tim Diekmann
 * @since 1.10.0
 */
public class InsteonClient extends ISYInsteonClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String user;
    private final String password;
    private final ISYModelChangeListener listener;

    /**
     * Constructor
     * 
     */
    public InsteonClient(final String user, final String password, final ISYModelChangeListener listener) {
        super();

        this.user = user;
        this.password = password;
        this.listener = listener;
    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#onLinkerEvent(com.universaldevices.upnp.UDProxyDevice,
     *      java.lang.String, com.nanoxml.XMLElement)
     */
    @Override
    public void onLinkerEvent(final UDProxyDevice arg0, final String arg1, final XMLElement arg2) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#onNodeDeviceIdChanged
     *      (com.universaldevices.upnp.UDProxyDevice,
     *      com.universaldevices.device.model.UDNode)
     */
    @Override
    public void onNodeDeviceIdChanged(final UDProxyDevice arg0, final UDNode arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#
     *      onNodeDevicePropertiesRefreshed(com.universaldevices.upnp.UDProxyDevice,
     *      com.universaldevices.device.model.UDNode)
     */
    @Override
    public void onNodeDevicePropertiesRefreshed(final UDProxyDevice arg0, final UDNode arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#
     *      onNodeDevicePropertiesRefreshedComplete
     *      (com.universaldevices.upnp.UDProxyDevice)
     */
    @Override
    public void onNodeDevicePropertiesRefreshedComplete(final UDProxyDevice arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#
     *      onNodeDevicePropertyChanged(com.universaldevices.upnp.UDProxyDevice,
     *      com.universaldevices.device.model.UDNode,
     *      com.universaldevices.common.properties.UDProperty)
     */
    @Override
    public void onNodeDevicePropertyChanged(final UDProxyDevice arg0, final UDNode arg1, final UDProperty<?> arg2) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#onNodeErrorCleared
     *      (com.universaldevices.upnp.UDProxyDevice,
     *      com.universaldevices.device.model.UDNode)
     */
    @Override
    public void onNodeErrorCleared(final UDProxyDevice arg0, final UDNode arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#onNodeRevised(com.universaldevices.upnp.UDProxyDevice,
     *      com.universaldevices.device.model.UDNode)
     */
    @Override
    public void onNodeRevised(final UDProxyDevice arg0, final UDNode arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.universaldevices.device.model.IModelChangeListener#
     *      onNodeSupportedTypeInfoChanged(com.universaldevices.upnp.UDProxyDevice,
     *      java.lang.String)
     */
    @Override
    public void onNodeSupportedTypeInfoChanged(final UDProxyDevice arg0, final String arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.udi.isy.jsdk.ISYClient#onDeviceOffLine()
     */
    @Override
    public void onDeviceOffLine() {
        this.logger.info("ISY is now offline");
    }

    /**
     * @see com.udi.isy.jsdk.ISYClient#onDeviceOnLine()
     */
    @Override
    public void onDeviceOnLine() {
        this.logger.info("ISY is now online");

        UDProxyDevice device = getDevice();

        if (device == null) {
            return;
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Supported controls:");
            try {
                for (UDControl control : getControls().values()) {
                    this.logger.debug(String.format(" %s, %s %s", control.name, control.desc,
                            control.isNumeric && control.numericUnit != null ? control.numericUnit : ""));

                    if (control.actions != null && !control.actions.isEmpty()) {
                        for (UDAction action : control.actions.values()) {
                            this.logger.debug(String.format("    %s %s %s", action.name, action.label,
                                    action.desc != null ? action.desc : ""));
                        }
                    }
                }
            } catch (NoDeviceException e) {
                this.logger.warn("Failed to get device", e);
            }
        }

        if (device.isSecurityEnabled() || device.securityLevel > UPnPSecurity.NO_SECURITY) {
            if (device.isAuthenticated && device.isOnline) {
                return;
            }

            try {
                this.logger.info("Authenticating...");

                if (!authenticate(this.user, this.password)) {
                    this.logger.warn("Authentication failed");

                    return;
                } else {
                    this.logger.info("Authenticated");
                }
            } catch (NoDeviceException ne) {
                this.logger.warn("No device", ne);
            }
        } else {
            device.subscribeToEvents(true);
        }
    }

    /**
     * @see com.udi.isy.jsdk.ISYClient#onDeviceSpecific(java.lang.String,
     *      java.lang.String, com.nanoxml.XMLElement)
     */
    @Override
    public void onDeviceSpecific(final String arg0, final String arg1, final XMLElement arg2) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * @see com.udi.isy.jsdk.ISYClient#onDiscoveringNodes()
     */
    @Override
    public void onDiscoveringNodes() {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onFolderRemoved(java.lang.String)
     */
    @Override
    public void onFolderRemoved(final String arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onFolderRenamed(com.universaldevices.device
     *      .model.UDFolder)
     */
    @Override
    public void onFolderRenamed(final UDFolder arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onGroupRemoved(java.lang.String)
     */
    @Override
    public void onGroupRemoved(final String arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onGroupRenamed(com.universaldevices.device
     *      .model.UDGroup)
     */
    @Override
    public void onGroupRenamed(final UDGroup arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onInternetAccessDisabled()
     */
    @Override
    public void onInternetAccessDisabled() {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onInternetAccessEnabled(java.lang.String)
     */
    @Override
    public void onInternetAccessEnabled(final String arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onModelChanged(com.universaldevices.device
     *      .model.UDControl, java.lang.Object,
     *      com.universaldevices.device.model.UDNode)
     */
    @Override
    public void onModelChanged(final UDControl control, final Object action, final UDNode node) {

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("onModelChanged(%s,%s,%s)", control != null ? control.name : "", action,
                    node != null ? String.format("%s[%s]", node.name, node.address) : "N/A"));
        }

        if (control == null || action == null || node == null) {
            return; // invalid update
        }

        this.listener.onModelChanged(control, action, node);
    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNetworkRenamed(java.lang.String)
     */
    @Override
    public void onNetworkRenamed(final String arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNewFolder(com.universaldevices.device.model
     *      .UDFolder)
     */
    @Override
    public void onNewFolder(final UDFolder arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNewGroup(com.universaldevices.device.model
     *      .UDGroup)
     */
    @Override
    public void onNewGroup(final UDGroup arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNewNode(com.universaldevices.device.model
     *      .UDNode)
     */
    @Override
    public void onNewNode(final UDNode arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeDiscoveryStopped()
     */
    @Override
    public void onNodeDiscoveryStopped() {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeEnabled(com.universaldevices.device.
     *      model.UDNode, boolean)
     */
    @Override
    public void onNodeEnabled(final UDNode arg0, final boolean arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeError(com.universaldevices.device.model
     *      .UDNode)
     */
    @Override
    public void onNodeError(final UDNode node) {
        this.logger.warn("Node {} reports error", node.name);
    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeHasPendingDeviceWrites(com.universaldevices
     *      .device.model.UDNode, boolean)
     */
    @Override
    public void onNodeHasPendingDeviceWrites(final UDNode arg0, final boolean arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeIsWritingToDevice(com.universaldevices
     *      .device.model.UDNode, boolean)
     */
    @Override
    public void onNodeIsWritingToDevice(final UDNode arg0, final boolean arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeMovedAsMaster(com.universaldevices.device
     *      .model.UDNode, com.universaldevices.device.model.UDGroup)
     */
    @Override
    public void onNodeMovedAsMaster(final UDNode arg0, final UDGroup arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeMovedAsSlave(com.universaldevices.device
     *      .model.UDNode, com.universaldevices.device.model.UDGroup)
     */
    @Override
    public void onNodeMovedAsSlave(final UDNode arg0, final UDGroup arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeParentChanged(com.universaldevices.device
     *      .model.UDNode, com.universaldevices.device.model.UDNode)
     */
    @Override
    public void onNodeParentChanged(final UDNode arg0, final UDNode arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodePowerInfoChanged(com.universaldevices
     *      .device.model.UDNode)
     */
    @Override
    public void onNodePowerInfoChanged(final UDNode arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeRemoved(java.lang.String)
     */
    @Override
    public void onNodeRemoved(final String arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeRemovedFromGroup(com.universaldevices
     *      .device.model.UDNode, com.universaldevices.device.model.UDGroup)
     */
    @Override
    public void onNodeRemovedFromGroup(final UDNode arg0, final UDGroup arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeRenamed(com.universaldevices.device.
     *      model.UDNode)
     */
    @Override
    public void onNodeRenamed(final UDNode arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onNodeToGroupRoleChanged(com.universaldevices
     *      .device.model.UDNode, com.universaldevices.device.model.UDGroup,
     *      char)
     */
    @Override
    public void onNodeToGroupRoleChanged(final UDNode arg0, final UDGroup arg1, final char arg2) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onProgress(java.lang.String,
     *      com.nanoxml.XMLElement)
     */
    @Override
    public void onProgress(final String arg0, final XMLElement arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onSystemConfigChanged(java.lang.String,
     *      com.nanoxml.XMLElement)
     */
    @Override
    public void onSystemConfigChanged(final String arg0, final XMLElement arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onSystemStatus(boolean)
     */
    @Override
    public void onSystemStatus(final boolean arg0) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.udi.isy.jsdk.ISYClient#onTriggerStatus(java.lang.String,
     *      com.nanoxml.XMLElement)
     */
    @Override
    public void onTriggerStatus(final String arg0, final XMLElement arg1) {
        // Nothing in OH is picking this up yet.

    }

    /**
     * 
     * @see com.universaldevices.client.UDClient#onNewDeviceAnnounced(com.
     *      universaldevices.upnp.UDProxyDevice)
     */
    @Override
    public void onNewDeviceAnnounced(final UDProxyDevice device) {
        this.logger.info("Connected to ISY: {}", device.getFriendlyName());

    }

}
