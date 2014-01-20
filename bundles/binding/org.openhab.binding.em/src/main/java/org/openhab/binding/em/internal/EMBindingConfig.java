/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.em.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * This class represents item configurations for the em binding.
 * 
 * @author Till Klocke
 * @since 1.4.0
 * 
 */
public class EMBindingConfig implements BindingConfig {

	/**
	 * Enum representing the available device types
	 * 
	 * @author Till Klocke
	 * @since 1.4.0
	 * 
	 */
	public static enum EMType {

		EM1000S("01"), EM100EM("02"), EM1000GZ("03");

		private static Map<String, EMType> typeMap = new HashMap<String, EMBindingConfig.EMType>();
		static {
			for (EMType type : EMType.values()) {
				typeMap.put(type.getTypeValue(), type);
			}
		}

		private String value;

		private EMType(String value) {
			this.value = value;
		}

		public String getTypeValue() {
			return value;
		}

		public static EMType getFromTypeValue(String typeValue) {
			return typeMap.get(typeValue);
		}
	}

	public static enum Datapoint {
		CUMULATED_VALUE, TOP_VALUE, LAST_VALUE;
	}

	private EMType type;
	private String address;
	private Datapoint datapoint;
	double correctionFactor;
	private Item item;

	public EMBindingConfig(EMType type, String address, Datapoint datapoint, Item item, double correctionFactor) {
		this.type = type;
		this.address = address;
		this.datapoint = datapoint;
		this.item = item;
		this.correctionFactor = correctionFactor;
	}

	public EMType getType() {
		return type;
	}

	public void setType(EMType type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Datapoint getDatapoint() {
		return datapoint;
	}

	public void setDatapoint(Datapoint datapoint) {
		this.datapoint = datapoint;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public double getCorrectionFactor() {
		return correctionFactor;
	}

	public void setCorrectionFactor(double correctionFactor) {
		this.correctionFactor = correctionFactor;
	}
}
