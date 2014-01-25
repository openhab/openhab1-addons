/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.utcs;

/**
 * <p>
 * Java class for WSResourceValue complex type.
 * 
 */

public class WSResourceValue {

	protected int resourceID;

	WSResourceValue() {

	}

	WSResourceValue(int resourceID) {
		this.resourceID = resourceID;
	}

	/**
	 * Gets the value of the resource ID property.
	 * 
	 */
	public int getResourceID() {
		return resourceID;
	}

	/**
	 * Sets the value of the resource ID property.
	 * 
	 */
	public void setResourceID(int value) {
		this.resourceID = value;
	}
}
