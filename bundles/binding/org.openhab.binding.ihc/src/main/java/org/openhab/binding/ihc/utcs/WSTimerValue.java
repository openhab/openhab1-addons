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
 * Java class for WSTimerValue complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WSTimerValue">
 *   &lt;complexContent>
 *     &lt;extension base="{utcs.values}WSResourceValue">
 *       &lt;sequence>
 *         &lt;element name="milliseconds" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class WSTimerValue extends WSResourceValue {

	protected long milliseconds;

	/**
	 * Gets the value of the milliseconds property.
	 * 
	 */
	public long getMilliseconds() {
		return milliseconds;
	}

	/**
	 * Sets the value of the milliseconds property.
	 * 
	 */
	public void setMilliseconds(long value) {
		this.milliseconds = value;
	}

}
