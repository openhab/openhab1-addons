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
package org.openhab.binding.velux.things;

import org.openhab.binding.velux.bridge.comm.BCgetProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux product.
 *
 * @author Guenther Schreiner - initial contribution.
 * @since 1.13.0
 */
public class VeluxProduct {
    private final Logger logger = LoggerFactory.getLogger(VeluxProduct.class);

    // Type definitions

    public static class ProductBridgeIndex {
        private int id;

        public int toInt() {
            return id;
        }

        public ProductBridgeIndex(int id) {
            this.id = id;
        }
    }

    @Deprecated
    public static class ProductName {
    }

    // Class internal

    private VeluxProductName name;
    private VeluxProductType typeId;
    private ProductBridgeIndex bridgeProductIndex;

    private boolean v2 = false;
    private int order = 0;
    private int placement = 0;
    private int velocity = 0;
    private int variation = 0;
    private int powerMode = 0;
    private String serialNumber = "";
    private int state = 0;
    private int currentPosition = 0;
    private int target = 0;
    private int remainingTime = 0;
    private int timeStamp = 0;
    private boolean isModified = true;

    // Constructor

    public VeluxProduct(VeluxProductName name, VeluxProductType typeId, ProductBridgeIndex bridgeProductIndex) {
        logger.trace("VeluxProduct(v1,name={}) created.", name.toString());
        this.name = name;
        this.typeId = typeId;
        this.bridgeProductIndex = bridgeProductIndex;
        this.isModified = true;
    }

    /**
     * @param name This field Name holds the name of the actuator, ex. “Window 1”. This field is 64 bytes
     *            long, formatted as UTF-8 characters.
     * @param typeId This field indicates the node type, ex. Window, Roller shutter, Light etc.
     * @param bridgeProductIndex NodeID is an Actuator index in the system table, to get information from. It must be a
     *            value from 0 to 199.
     * @param order Order can be used to store a sort order. The sort order is used in client end, when
     *            presenting a list of nodes for the user.
     * @param placement Placement can be used to store a room group index or house group index number.
     * @param velocity This field indicates what velocity the node is operation with.
     * @param variation More detail information like top hung, kip, flat roof or sky light window.
     * @param powerMode This field indicates the power mode of the node (ALWAYS_ALIVE/LOW_POWER_MODE).
     * @param serialNumber This field tells the serial number of the node. This field is 8 bytes.
     * @param state This field indicates the operating state of the node.
     * @param currentPosition This field indicates the current position of the node.
     * @param target This field indicates the target position of the current operation.
     * @param remainingTime This field indicates the remaining time for a node activation in seconds.
     * @param timeStamp UTC time stamp for last known position.
     */
    public VeluxProduct(VeluxProductName name, VeluxProductType typeId, ProductBridgeIndex bridgeProductIndex,
            int order, int placement, int velocity, int variation, int powerMode, String serialNumber, int state,
            int currentPosition, int target, int remainingTime, int timeStamp) {
        logger.trace("VeluxProduct(v2,name={}) created.", name.toString());
        this.name = name;
        this.typeId = typeId;
        this.bridgeProductIndex = bridgeProductIndex;
        this.v2 = true;
        this.order = order;
        this.placement = placement;
        this.velocity = velocity;
        this.variation = variation;
        this.powerMode = powerMode;
        this.serialNumber = serialNumber;
        this.state = state;
        this.currentPosition = currentPosition;
        this.target = target;
        this.remainingTime = remainingTime;
        this.timeStamp = timeStamp;
        this.isModified = true;
    }

    // Utility methods

    @Override
    public VeluxProduct clone() {
        if (this.v2) {
            return new VeluxProduct(this.name, this.typeId, this.bridgeProductIndex, this.order, this.placement,
                    this.velocity, this.variation, this.powerMode, this.serialNumber, this.state, this.currentPosition,
                    this.target, this.remainingTime, this.timeStamp);
        } else {
            return new VeluxProduct(this.name, this.typeId, this.bridgeProductIndex);
        }
    }

    // Class access methods

    public VeluxProductName getProductName() {
        return this.name;
    }

    public VeluxProductType getProductType() {
        return this.typeId;
    }

    public ProductBridgeIndex getBridgeProductIndex() {
        return this.bridgeProductIndex;
    }

    @Override
    public String toString() {
        if (this.v2) {
            return String.format("Product \"%s\" / %s (bridgeIndex=%d,serial=%s,position=%04X)", this.name, this.typeId,
                    this.bridgeProductIndex.toInt(), this.serialNumber, this.currentPosition);
        } else {
            return String.format("Product \"%s\" / %s (bridgeIndex %d)", this.name, this.typeId,
                    this.bridgeProductIndex.toInt());
        }
    }

    // Class helper methods

    public String getProductUniqueIndex() {
        return this.name.toString().concat("#").concat(this.typeId.toString());
    }

    // Getter and Setter methods

    /**
     * @return <b>v2</b> as type boolean signals the availability of firmware version two (product) details.
     */
    public boolean isV2() {
        return v2;
    }

    /**
     * @return <b>order</b> as type int describes the user-oriented sort-order.
     */
    public int getOrder() {
        return order;
    }

    /**
     * @return <B>placement</B> as type int is used to describe a group index or house group index number.
     */
    public int getPlacement() {
        return placement;
    }

    /**
     * @return <B>velocity</B> as type int describes what velocity the node is operation with
     */
    public int getVelocity() {
        return velocity;
    }

    /**
     * @return <B>variation</B> as type int describes detail information like top hung, kip, flat roof or sky light
     *         window.
     */
    public int getVariation() {
        return variation;
    }

    /**
     * @return <B>powerMode</B> as type int is used to show the power mode of the node (ALWAYS_ALIVE/LOW_POWER_MODE).
     */
    public int getPowerMode() {
        return powerMode;
    }

    /**
     * @return <B>serialNumber</B> as type String is the serial number of 8 bytes length of the node.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @return <B>state</B> as type int is used to operating state of the node.
     */
    public int getState() {
        return state;
    }

    /**
     * @param newState Update the operating state of the node.
     * @return <B>modified</B> as type boolean to signal a real modification.
     */
    public boolean setState(int newState) {
        if (this.state == newState) {
            return false;
        } else {
            logger.trace("setState(name={},index={}) state {} replaced by {}.", name.toString(),
                    bridgeProductIndex.toInt(), this.state, newState);
            this.state = newState;
            this.isModified = true;
            return true;
        }
    }

    /**
     * @return <B>currentPosition</B> as type int signals the current position of the node.
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * @param newCurrentPosition Update the current position of the node.
     * @return <B>modified</B> as boolean to signal a real modification.
     */
    public boolean setCurrentPosition(int newCurrentPosition) {
        if (this.currentPosition == newCurrentPosition) {
            return false;
        } else {
            logger.trace("setCurrentPosition(name={},index={}) currentPosition {} replaced by {}.", name.toString(),
                    bridgeProductIndex.toInt(), this.currentPosition, newCurrentPosition);
            this.currentPosition = newCurrentPosition;
            this.isModified = true;
            return true;
        }
    }

    /**
     * @return <b>target</b> as type int shows the target position of the current operation.
     */
    public int getTarget() {
        return target;
    }

    /**
     * @param newTarget Update the target position of the current operation.
     * @return <b>modified</b> as boolean to signal a real modification.
     */
    public boolean setTarget(int newTarget) {
        if (this.target == newTarget) {
            return false;
        } else {
            logger.trace("setCurrentPosition(name={},index={}) target {} replaced by {}.", name.toString(),
                    bridgeProductIndex.toInt(), this.target, newTarget);
            this.target = newTarget;
            this.isModified = true;
            return true;
        }
    }

    /**
     * @return <b>remainingTime</b> as type int describes the intended remaining time of current operation.
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * @return <b>timeStamp</b> as type int describes the current time.
     */
    public int getTimeStamp() {
        return timeStamp;
    }

    /**
     * @return <b>isModified</b> as type boolean describes whether this product has been modified.
     */
    public boolean isModified() {
        logger.trace("isModified() returns {}.", this.isModified);
        return this.isModified;
    }

    public void resetModifiedFlag() {
        logger.trace("resetModifiedFlag() called.");
        this.isModified = false;
    }

    @Deprecated
    public VeluxProduct(BCgetProducts.BCproduct productDescr) {
    }

    @Deprecated
    public VeluxProduct(VeluxProduct product) {
    }

    @Deprecated
    public VeluxProduct.ProductName getName() {
        return null;
    }

    @Deprecated
    public VeluxProduct.ProductTypeId getTypeId() {
        return null;
    }

    @Deprecated
    public static VeluxProduct.ProductTypeId get() {
        return null;
    }

    @Deprecated
    public enum ProductTypeId {
        ROLLER_SHUTTER(2, 0),
        OTHER(3, 0),
        WINDOW_OPENER(4, 1),
        SOLAR_SLIDER(10, 2),
        UNDEFTYPE(-1);

        // Constructor
        @Deprecated
        ProductTypeId(int typeId) {
        }

        @Deprecated
        ProductTypeId(int typeId, int subTypeId) {
        }

        @Deprecated
        ProductTypeId(String categoryString) {
        }

        @Deprecated
        public int getTypeId() {
            return 0;
        }

        @Deprecated
        public int getSubtype() {
            return 0;
        }

        @Deprecated
        public static ProductTypeId get(int typeId) {
            return null;
        }
    }

}
