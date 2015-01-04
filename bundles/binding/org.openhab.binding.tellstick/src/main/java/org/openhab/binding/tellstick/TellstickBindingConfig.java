/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick;

import org.openhab.binding.tellstick.internal.TellstickController;
import org.openhab.core.binding.BindingConfig;

/**
 * Config holder object for tellstick binding
 * 
 * @since 1.5.0
 * @author jarlebh
 * 
 */
public class TellstickBindingConfig implements BindingConfig {
	private int id;
	private TellstickValueSelector valueSelector;
	private TellstickValueSelector usageSelector;
	private String itemName;
	private String protocol;
	private int resend = 1;
	private long resendInterval = TellstickController.DEFAULT_INTERVAL_BETWEEN_SEND;

	public TellstickBindingConfig() {
		super();
	}

	/**
	 * Get the deviceId.
	 * The deviceID in Telldus Center (tdtool -l)
	 * 
	 * @return The id of the device
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * The type of value (Temp, Humid).
	 * 
	 * @return
	 */
	public TellstickValueSelector getValueSelector() {
		return valueSelector;
	}

	public void setValueSelector(TellstickValueSelector valueSelector) {
		this.valueSelector = valueSelector;
	}

	/**
	 * The name of the device from openhab config.
	 * 
	 * @return The name
	 */
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * The "real" usage of the value, for example battery status might be sendt
	 * as humid data.
	 * 
	 * @return The "real" usage.
	 */
	public TellstickValueSelector getUsageSelector() {
		return usageSelector;
	}

	public void setUsageSelector(TellstickValueSelector usageSelector) {
		this.usageSelector = usageSelector;
	}

	@Override
	public String toString() {
		return "TellstickBindingConfig [id=" + id + ", valueSelector=" + valueSelector + ", usageSelector="
				+ usageSelector + ", itemName=" + itemName + ", resend=" + resend + "]";
	}

	public void setResend(int numberOfResends) {
		this.resend = numberOfResends;
	}

	/**
	 * Number of resends for this device.
	 * 
	 * @return The number
	 */
	public int getResend() {
		return resend;
	}

	/** Get the model of a sensor value.
	 * oregon, fineoffset, madolyn
	 * 
	 * @return the model
	 */
	public String getProtocol() {
		return protocol;
	}

	/** Set the model.
	 * 
	 * @param model the model to set
	 */
	public void setProtocol(String proto) {
		this.protocol = proto;
	}

	/** 
	 * Resend interval for this device. 
	 * @return the interval in ms
	 */
	public long getResendInterval() {
		return resendInterval;
	}

	/**
	 * Set the resend interval for this device. 
	 * @param resendInterval in ms
	 */
	public void setResendInterval(long resendInterval) {
		this.resendInterval = resendInterval;
	}
	
}
