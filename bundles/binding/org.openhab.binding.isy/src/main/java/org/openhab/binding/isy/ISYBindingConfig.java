/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.isy.internal.ISYControl;
import org.openhab.binding.isy.internal.ISYNodeType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Holds binding configuration
 * 
 * @author Tim Diekmann
 * @since 1.10.0
 */
public class ISYBindingConfig implements BindingConfig {

    /**
     * Constructor
     * 
     * @param item
     *            full item from openhab
     * @param type
     *            the node type in ISY, rough translation.
     * @param controller
     *            the address of the controller.
     * @param address
     *            the address of the item to monitor for changes. same as
     *            controller if not specified.
     */
    public ISYBindingConfig(Item item, ISYNodeType type, String controller, String address) {
        this(item, type, controller, address, ISYControl.ST);
    }

    /**
     * Constructor
     * 
     * @param item
     *            full item from openhab
     * @param type
     *            the node type in ISY, rough translation.
     * @param controller
     *            the address of the controller.
     * @param address
     *            the address of the item to monitor for changes. same as
     *            controller if not specified.
     * @param command
     *            the command the system is sending for this controller.
     */
    public ISYBindingConfig(Item item, ISYNodeType type, String controller, String address, ISYControl command) {
        this.item = item;
        this.type = type;
        this.controller = controller;
        if (StringUtils.isNotBlank(address)) {
            this.address = address;
        } else {
            this.address = controller;
        }
        if (command != null) {
            this.controlCommand = command;
        } else {
            this.controlCommand = ISYControl.ST;
        }
    }

    private final Item item;
    private final ISYNodeType type;
    private final String controller;
    private final String address;
    private final ISYControl controlCommand;

    /**
     * 
     * @return the full item the binding is for.
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * The name of the binding is attached to.
     * 
     * @return the name of the binding.
     */
    public String getItemName() {
        return this.item.getName();
    }

    /**
     * The types of node that the binding is linking to. Used internally to
     * handle translations as needed.
     * 
     * @return the ISY node type we are working with.
     */
    public ISYNodeType getType() {
        return this.type;
    }

    /**
     * Address used to send commands to, e.g. node or scene. This is ctrl in the
     * item config. Controller address in the format "x.y.z.w". This can be the
     * address of a ISY Insteon Scene. Insteon addresses use the first three
     * bytes and the last byte identified a particular capability of the device.
     * 
     * Example for a wall switch: ctrl=28.2F.24.1
     */
    public String getController() {
        return this.controller;
    }

    /**
     * The Insteon address to monitor for the change. A scene does not report a
     * status change, only the devices in the scene do. Specify the controller
     * of the scene here. If this is not specified in the configuration it is
     * the same as the controller to detect state changes.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * ISY command associated with the node. ISY SDK calls them UDControl
     */
    public ISYControl getControlCommand() {
        return this.controlCommand;
    }

    public String toString() {
        return new StringBuilder().append("ctrl=").append(controller).append("|addr:").append(address).append("|cmd:")
                .append(controlCommand).append("|type:").append(type).append("|item:").append(item.getName())
                .toString();
    }
}
