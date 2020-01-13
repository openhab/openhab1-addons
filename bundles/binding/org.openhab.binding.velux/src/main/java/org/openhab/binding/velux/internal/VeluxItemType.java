/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.velux.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enumeration of Types of a Velux item.
 * <br>
 * Provides information about:
 * <ul>
 * <li>associated thing identified by String</li>
 * <li>defined channel identified by String</li>
 * <li>{@link VeluxItemType#getItemClass getItemClass} item class</li>
 * <li>{@link VeluxItemType#isReadable isReadable} about a read possibility</li>
 * <li>{@link VeluxItemType#isWritable isWritable} about a write possibility</li>
 * <li>{@link VeluxItemType#isExecutable isExecutable} about an execute possibility</li>
 * <li>{@link VeluxItemType#isToBeRefreshed isToBeRefreshed} about necessarily to be refreshed.</li>
 * </ul>
 *
 * In addition there are helper methods providing information about:
 *
 * <ul>
 * <li>{@link VeluxItemType#getByString getByString} the enum by identifier string,</li>
 * <li>{@link VeluxItemType#getByThingAndChannel getByThingAndChannel} to retrieve an enum instance selected by Thing
 * and Channel identifier,</li>
 * <li>{@link VeluxItemType#getThingIdentifiers getThingIdentifiers} to retrieve any Thing identifiers as array of
 * String,</li>
 * <li>{@link VeluxItemType#getChannelIdentifiers getChannelIdentifiers} to retrieve any Channel identifiers as array of
 * String.</li>
 * </ul>
 * <p>
 * Within this enumeration, the expected behaviour of the OpenHAB item (resp. Channel)
 * is set. For each kind of Channel (i.e. bridge or device) parameter a
 * set of information is defined:
 * <ul>
 * <li>
 * Unique identification by:
 * <ul>
 * <li>Thing name as string,</li>
 * <li>Channel name as string,</li>
 * </ul>
 * </li>
 * <li>Channel type as OpenHAB type,</li>
 * <li>ability flag whether this item is to be read,</li>
 * <li>ability flag whether this item is able to be modified,</li>
 * <li>ability flag whether this item is to be used as execution trigger.</li>
 * </ul>
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 *
 */
public enum VeluxItemType {
    BRIDGE_RELOAD(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_RELOAD,
            SwitchItem.class, false, false, true, false),
    SCENE_ACTION(VeluxBindingConstants.THING_VELUX_SCENE, VeluxBindingConstants.CHANNEL_SCENE_ACTION, SwitchItem.class,
            false, false, true, false),
    SCENE_SILENTMODE(VeluxBindingConstants.THING_VELUX_SCENE, VeluxBindingConstants.CHANNEL_SCENE_SILENTMODE,
            SwitchItem.class, false, true, false, false),
    BRIDGE_STATUS(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_STATUS,
            StringItem.class, true, true, true, true, 1),
    BRIDGE_TIMESTAMP(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_TIMESTAMP,
            NumberItem.class, true, true, true, true, 1),
    BRIDGE_DO_DETECTION(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_DO_DETECTION,
            SwitchItem.class, false, false, true, false),
    BRIDGE_FIRMWARE(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_FIRMWARE,
            StringItem.class, true, false, false, false),
    BRIDGE_IPADDRESS(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_IPADDRESS,
            StringItem.class, true, false, false, false),
    BRIDGE_SUBNETMASK(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_SUBNETMASK,
            StringItem.class, true, false, false, false),
    BRIDGE_DEFAULTGW(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_DEFAULTGW,
            StringItem.class, true, false, false, false),
    BRIDGE_DHCP(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_DHCP, SwitchItem.class,
            true, false, false, false),
    BRIDGE_WLANSSID(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_WLANSSID,
            StringItem.class, true, false, false, false),
    BRIDGE_WLANPASSWORD(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_WLANPASSWORD,
            StringItem.class, true, false, false, false),
    BRIDGE_PRODUCTS(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_PRODUCTS,
            StringItem.class, true, false, false, true, 15),
    BRIDGE_SCENES(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_SCENES,
            StringItem.class, true, false, false, true, 5760),
    BRIDGE_CHECK(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_CHECK, StringItem.class,
            true, false, false, true, 5760),
    BRIDGE_SHUTTER(VeluxBindingConstants.THING_VELUX_BRIDGE, VeluxBindingConstants.CHANNEL_BRIDGE_SHUTTER,
            RollershutterItem.class, false, true, false, false),
    ACTUATOR_SERIAL(VeluxBindingConstants.THING_VELUX_ACTUATOR, VeluxBindingConstants.CHANNEL_ACTUATOR_SERIAL,
            RollershutterItem.class, true, true, false, true, 20);

    private String thingIdentifier;
    private String channelIdentifier;
    private Class<? extends Item> itemClass;
    private boolean itemIsReadable;
    private boolean itemIsWritable;
    private boolean itemIsExecutable;
    private boolean itemIsToBeRefreshed;
    private int itemsRefreshDivider;

    private static Logger logger = LoggerFactory.getLogger(VeluxItemType.class);

    VeluxItemType(String thingIdentifier, String channelIdentifier, Class<? extends Item> itemClass, boolean isReadable,
            boolean isWritable, boolean isExecutable, boolean isToBeRefreshed, int refreshDivider) {
        this.thingIdentifier = thingIdentifier;
        this.channelIdentifier = channelIdentifier;
        this.itemClass = itemClass;
        this.itemIsReadable = isReadable;
        this.itemIsWritable = isWritable;
        this.itemIsExecutable = isExecutable;
        this.itemIsToBeRefreshed = isToBeRefreshed;
        this.itemsRefreshDivider = refreshDivider;
    }

    VeluxItemType(String thingIdentifier, String channelIdentifier, Class<? extends Item> itemClass, boolean isReadable,
            boolean isWritable, boolean isExecutable, boolean isToBeRefreshed) {
        this(thingIdentifier, channelIdentifier, itemClass, isReadable, isWritable, isExecutable, isToBeRefreshed, 1);
    }

    VeluxItemType(String thingIdentifier, String channelIdentifier, Class<? extends Item> itemClass, boolean isReadable,
            boolean isWritable, boolean isExecutable) {
        this(thingIdentifier, channelIdentifier, itemClass, isReadable, isWritable, isExecutable, false, 1);
    }

    @Override
    public String toString() {
        return this.thingIdentifier + "/" + this.channelIdentifier;
    }

    /**
     * {@link VeluxItemType} access method to query Identifier on this type of item.
     *
     * @return <b>thingIdentifier</b> of type String describing the value of the enum {@link VeluxItemType}
     *         return
     */
    public String getIdentifier() {
        return this.thingIdentifier;
    }

    /**
     * {@link VeluxItemType} access method to query the appropriate type of item.
     *
     * @return <b>itemClass</b> of type Item describing the possible type of this item.
     */
    public Class<? extends Item> getItemClass() {
        return this.itemClass;
    }

    /**
     * {@link VeluxItemType} access method to query Read possibility on this type of item.
     *
     * @return <b>itemIsReadable</b> of type boolean describing the ability to perform a write operation.
     */
    public boolean isReadable() {
        logger.trace("isReadable() returns {}.", this.itemIsReadable);
        return this.itemIsReadable;
    }

    /**
     * {@link VeluxItemType} access method to query Write possibility on this type of item.
     *
     * @return <b>itemIsWritable</b> of type boolean describing the ability to perform a write operation.
     */
    public boolean isWritable() {
        logger.trace("isWritable() returns {}.", this.itemIsWritable);
        return this.itemIsWritable;
    }

    /**
     * {@link VeluxItemType} access method to query Execute possibility on this type of item.
     *
     * @return <b>isExecute</b> of type boolean describing the ability to perform a write operation.
     */
    public boolean isExecutable() {
        logger.trace("isExecutable() returns {}.", this.itemIsExecutable);
        return this.itemIsExecutable;
    }

    /**
     * {@link VeluxItemType} access method to query the need of refreshMSecs on this type of item.
     *
     * @return <b>isExecute</b> of type boolean describing the ability to perform a write operation.
     */
    public boolean isToBeRefreshed() {
        logger.trace("isToBeRefreshed() returns {}.", this.itemIsToBeRefreshed);
        return this.itemIsToBeRefreshed;
    }

    /**
     * {@link VeluxItemType} access method to query the refreshMSecs interval on this type of item.
     *
     * @return <b>refreshDivider</b> of type int describing the factor.
     */
    public int getRefreshDivider() {
        logger.trace("getRefreshDivider() returns {}.", this.itemsRefreshDivider);
        return this.itemsRefreshDivider;
    }

    /**
     * {@link VeluxItemType} access method to find an enum by thing name.
     *
     * @param thingIdentifier as name of requested Thing of type String.
     *
     * @return <b>veluxItemType</b> of type VeluxItemType describing the appropriate enum.
     */
    public VeluxItemType getByString(String thingIdentifier) {
        logger.trace("getByString({}) called.", thingIdentifier);
        try {
            return VeluxItemType.valueOf(thingIdentifier);
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

    /**
     * {@link VeluxItemType} access method to find an enum by name.
     *
     * @param thingIdentifier as name of requested Thing of type String.
     * @param channelIdentifier as name of requested Channel of type String.
     *
     * @return <b>veluxItemType</b> of type VeluxItemType describing the appropriate enum.
     */
    public static VeluxItemType getByThingAndChannel(String thingIdentifier, String channelIdentifier) {
        logger.trace("getByThingAndChannel({},{}) called.", thingIdentifier, channelIdentifier);
        for (VeluxItemType v : VeluxItemType.values()) {
            logger.trace("getByThingAndChannel() work on {} and {}.", v.thingIdentifier, v.channelIdentifier);
            if ((thingIdentifier.equals(v.thingIdentifier)) && (channelIdentifier.equals(v.channelIdentifier))) {
                logger.trace("getByThingAndChannel() returns enum.");
                return v;
            }
        }
        logger.trace("getByThingAndChannel() returns null.");
        return null;
    }

    /**
     * {@link VeluxItemType} access method to find an enum by name.
     *
     * @return <b>veluxItemType</b> of type VeluxItemType describing the appropriate enum.
     */
    public static String[] getThingIdentifiers() {
        logger.trace("getThingIdentifiers() called.");
        Set<List<String>> uniqueSet = new HashSet<List<String>>();
        for (VeluxItemType v : VeluxItemType.values()) {
            logger.trace("getThingIdentifiers() adding {}.", v.thingIdentifier);
            uniqueSet.add(Arrays.asList(v.thingIdentifier));
        }
        return uniqueSet.toArray(new String[uniqueSet.size()]);
    }

    /**
     * {@link VeluxItemType} access method to find an enum by name.
     *
     * @param thingIdentifier as name of requested Thing of type String.
     *
     * @return <b>veluxItemType</b> of type VeluxItemType describing the appropriate enum.
     */
    public static String[] getChannelIdentifiers(String thingIdentifier) {
        logger.trace("getChannelIdentifiers() called.");
        Set<List<String>> uniqueSet = new HashSet<List<String>>();
        for (VeluxItemType v : VeluxItemType.values()) {
            if (thingIdentifier.equals(v.thingIdentifier)) {
                uniqueSet.add(Arrays.asList(v.channelIdentifier));
            }
        }
        return uniqueSet.toArray(new String[uniqueSet.size()]);
    }
}
