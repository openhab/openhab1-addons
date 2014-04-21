package org.openhab.binding.tellstick;

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
	private int resend = 1;

	public TellstickBindingConfig() {
		super();
	}

	/**
	 * Get the deviceId.
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

}
