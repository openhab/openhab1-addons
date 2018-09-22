/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux;

import org.openhab.binding.velux.internal.VeluxItemType;

/**
 * The {@link VeluxBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * For an in-depth view of the available Item type with description of parameters, take a look onto {@link VeluxItemType}.
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBindingConstants {

    /** Basic binding identification */
    public static final String BINDING_ID = "velux";

    // List of all thing ids
    public static final String THING_VELUX_SCENE = "scene";
    public static final String THING_VELUX_BRIDGE = "bridge";

    // Id of support bridge
    public static final String BRIDGE_TYPE = "klf200";

    // List of all bridge channel ids
    public static final String CHANNEL_BRIDGE_STATUS = "status";
    public static final String CHANNEL_BRIDGE_DO_DETECTION = "doDetection";
    public static final String CHANNEL_BRIDGE_FIRMWARE = "firmware";
    public static final String CHANNEL_BRIDGE_IPADDRESS = "ipAddress";
    public static final String CHANNEL_BRIDGE_SUBNETMASK = "subnetMask";
    public static final String CHANNEL_BRIDGE_DEFAULTGW = "defaultGW";
    public static final String CHANNEL_BRIDGE_DHCP = "DHCP";
    public static final String CHANNEL_BRIDGE_WLANSSID = "WLANSSID";
    public static final String CHANNEL_BRIDGE_WLANPASSWORD = "WLANPassword";
    public static final String CHANNEL_BRIDGE_PRODUCTS = "products";
    public static final String CHANNEL_BRIDGE_SCENES = "scenes";
    public static final String CHANNEL_BRIDGE_CHECK = "check";
    public static final String CHANNEL_BRIDGE_SHUTTER = "shutter";

    // List of all scene channel ids
    public static final String CHANNEL_SCENE_ACTION = "action";
    public static final String CHANNEL_SCENE_SILENTMODE = "silentMode";

    /** Helper definitions */
    public static final String BINDING_KVPAIR_SEPARATOR = ";";
    public static final String BINDING_KV_SEPARATOR = "=";
    public static final String BINDING_VALUE_SEPARATOR = "#";
    public static final String BINDING_VALUES_SEPARATOR = ",";
    public static final String BINDING_ID_THING = "thing";
    public static final String BINDING_ID_CHANNEL = "channel";

}
/**
 * end-of-VeluxBindingConstants.java
 */
