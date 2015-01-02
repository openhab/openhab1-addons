/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * See {@link DevicelistModel}.
 * 
 * @author Robert Bausdorf
 * @since 1.6
 * 
 */
@SuppressWarnings("restriction")
@XmlRootElement(name = "temperature")
@XmlType(propOrder = { "celsius", "offset" })
public class TemperatureModel {
	private BigDecimal celsius;
	private BigDecimal offset;

	public BigDecimal getCelsius() {
		return celsius;
	}

	public void setCelsius(BigDecimal celsius) {
		this.celsius = celsius;
	}

	public BigDecimal getOffset() {
		return offset;
	}

	public void setOffset(BigDecimal offset) {
		this.offset = offset;
	}

	public String toString() {
		StringBuilder out = new StringBuilder("temperature");
		out.append("[celsius=").append(this.getCelsius()).append(',');
		out.append("offset=").append(this.getOffset()).append(']');
		return out.toString();
	}
}
