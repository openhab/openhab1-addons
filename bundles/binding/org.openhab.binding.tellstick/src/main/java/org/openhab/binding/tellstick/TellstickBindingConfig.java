package org.openhab.binding.tellstick;

import org.openhab.core.binding.BindingConfig;

public class TellstickBindingConfig implements BindingConfig {
	private int id;
	private boolean inBinding;
	private TellstickValueSelector valueSelector;
	private TellstickValueSelector usageSelector;
	private String itemName;
	private int resend = 1;
	
	
	public TellstickBindingConfig() {
		super();
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isInBinding() {
		return inBinding;
	}
	public void setInBinding(boolean inBinding) {
		this.inBinding = inBinding;
	}
	public TellstickValueSelector getValueSelector() {
		return valueSelector;
	}
	public void setValueSelector(TellstickValueSelector valueSelector) {
		this.valueSelector = valueSelector;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public TellstickValueSelector getUsageSelector() {
		return usageSelector;
	}
	public void setUsageSelector(TellstickValueSelector usageSelector) {
		this.usageSelector = usageSelector;
	}
	@Override
	public String toString() {
		return "TellstickBindingConfig [id=" + id + ", inBinding=" + inBinding
				+ ", valueSelector=" + valueSelector + ", usageSelector="
				+ usageSelector + ", itemName=" + itemName + ", resend="+resend+"]";
	}
	public void setResend(int numberOfResends) {
		this.resend = numberOfResends;
	}
	public int getResend() {
		return resend;
	}
	
	

}
