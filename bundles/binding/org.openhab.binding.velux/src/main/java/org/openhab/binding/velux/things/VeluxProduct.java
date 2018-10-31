/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux product.
 *
 * @author Guenther Schreiner - initial contribution.
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

	// Constructor

	public VeluxProduct(VeluxProductName name, VeluxProductType typeId, ProductBridgeIndex bridgeProductIndex) {
		logger.trace("VeluxProduct(v1) created.");
		this.name = name;
		this.typeId = typeId;
		this.bridgeProductIndex = bridgeProductIndex;
	}

	/**
	 * @param name			This field Name holds the name of the actuator, ex. “Window 1”. This field is 64 bytes long, formatted as UTF-8 characters.
	 * @param typeId		This field indicates the node type, ex. Window, Roller shutter, Light etc.
	 * @param bridgeProductIndex NodeID is an Actuator index in the system table, to get information from. It must be a value from 0 to 199.
	 * @param order			Order can be used to store a sort order. The sort order is used in client end, when presenting a list of nodes for the user. 
	 * @param placement		Placement can be used to store a room group index or house group index number.
	 * @param velocity		This field indicates what velocity the node is operation with.
	 * @param variation		More detail information like top hung, kip, flat roof or sky light window. 
	 * @param powerMode		This field indicates the power mode of the node (ALWAYS_ALIVE/LOW_POWER_MODE).
	 * @param serialNumber	This field tells the serial number of the node. This field is 8 bytes.
	 * @param state			This field indicates the operating state of the node.
	 * @param currentPosition	This field indicates the current position of the node.
	 * @param target		This field indicates the target position of the current operation.
	 * @param remainingTime	This field indicates the remaining time for a node activation in seconds.
	 * @param timeStamp		UTC time stamp for last known position.
	 */
	public VeluxProduct(VeluxProductName name, VeluxProductType typeId, ProductBridgeIndex bridgeProductIndex,
			int order, int placement, int velocity, int variation, int powerMode, String serialNumber,
			int state, int currentPosition, int target, int remainingTime, int timeStamp) {
		logger.trace("VeluxProduct(v2) created.");
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
	}

	// Utility methods

	public VeluxProduct clone() {
		if (this.v2) {
			return new VeluxProduct(this.name, this.typeId, this.bridgeProductIndex,
					this.order, this.placement, this.velocity, this.variation, this.powerMode, this.serialNumber,
					this.state, this.currentPosition, this.target, this.remainingTime, this.timeStamp);
		} else {
			return new VeluxProduct(this.name, this.typeId, this.bridgeProductIndex);
		}
	}

	// Class access methods

	public VeluxProductName getName() {
		return this.name;
	}

	public VeluxProductType getTypeId() {
		return this.typeId;
	}

	public ProductBridgeIndex getBridgeProductIndex() {
		return this.bridgeProductIndex;
	}

	@Override
	public String toString() {
		if (this.v2) {
			return String.format("Product \"%s\"/%s (bridgeIndex=%d,serial=%s,position=%d)",
					this.name, this.typeId,this.bridgeProductIndex.toInt(),
					this.serialNumber,this.currentPosition);
		} else {
			
		return String.format("Product \"%s\"/%s (bridgeIndex %d)", this.name, this.typeId,
				this.bridgeProductIndex.toInt());
		}
	}

	// Class helper methods

	public String getProductUniqueIndex() {
		return this.name.toString().concat("#").concat(this.typeId.toString());
	}

	// Getter and Setter methods 

	/**
	 * @return the v2
	 */
	public boolean isV2() {
		return v2;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @return the placement
	 */
	public int getPlacement() {
		return placement;
	}

	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return velocity;
	}

	/**
	 * @return the variation
	 */
	public int getVariation() {
		return variation;
	}

	/**
	 * @return the powerMode
	 */
	public int getPowerMode() {
		return powerMode;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state				Update the operating state of the node.
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the currentPosition
	 */
	public int getCurrentPosition() {
		return currentPosition;
	}
	/**
	 * @param currentPosition	Update the current position of the node.
	 */
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}
	/**
	 * @param target			Update the target position of the current operation.
	 */
	public void setTarget(int target) {
		this.target = target;
	}

	/**
	 * @return the remainingTime
	 */
	public int getRemainingTime() {
		return remainingTime;
	}

	/**
	 * @return the timeStamp
	 */
	public int getTimeStamp() {
		return timeStamp;
	}

}

/**
 * end-of-VeluxProduct.java
 */
